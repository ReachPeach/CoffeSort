package main.java.gui;

import main.java.app.Controller;
import main.java.common.Utils;
import main.java.domain.CoffeeSort;
import main.java.exceptions.AppPersistenceException;
import main.java.models.CoffeeSortListModel;
import main.java.models.CoffeeSortTableModel;
import main.java.persistence.CoffeeSortPersistence;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static main.java.common.Utils.FONT_FOR_WIDGETS;

public class CoffeeSortsAppGUI {
    // Заголовки столбцов
    private final Object[] columnsHeader = new String[]{"Страна изготовления", "Кислотность",
            "Дата сбора", "Дата обжарки", "Страна доставки", "Название сорта", "Глубина обжарки"};

    private final Controller parent;
    private final int num;
    private final JFrame frame;
    private JButton addButton;
    private JButton deleteButton;
    private JButton changeButton;
    private JTable table;

    protected CoffeeSortListModel current;

    private final CoffeeSortPersistence coffeeSortDAO;

    protected JTable getTable() {
        return table;
    }

    protected CoffeeSortListModel getCurrent() {
        return current;
    }

    public CoffeeSortsAppGUI(Controller parent, int num, CoffeeSortPersistence dao) {
        this.parent = parent;
        this.num = num;

        frame = new JFrame("Coffee Sorts App");
        addWidgetsToFrame(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(350, 50); // x, y
        frame.setMinimumSize(new Dimension(900, 300));
        frame.pack();
        frame.setVisible(true);

        coffeeSortDAO = dao;
        initDataAndUpdateUI();
    }

    private void initDataAndUpdateUI() {

        try {
            List<CoffeeSort> l = coffeeSortDAO.getAll();
            current = new CoffeeSortListModel(l);
            String[][] values = new String[l.size()][];
            for (int i = 0; i < l.size(); i++) values[i] = l.get(i).toStringArray();
            table.setModel(new CoffeeSortTableModel(values, columnsHeader));

            DefaultTableCellRenderer centreRender = new DefaultTableCellRenderer();
            centreRender.setHorizontalAlignment(SwingConstants.CENTER);
            for (int i = 0; i < columnsHeader.length; i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centreRender);
            }

        } catch (AppPersistenceException e) {
            doExceptionRoutine(Utils.ActionType.LOAD);
        }

        if (getCoffeeSortsModel().isEmpty()) {
            deleteButton.setEnabled(false);
            changeButton.setEnabled(false);
        }
    }

    protected void addWidgetsToFrame(JFrame frame) {

        Container pane = frame.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        pane.add(getTableInScrollPane());
        pane.add(getToolBarWithButtons());
    }


    protected JScrollPane getTableInScrollPane() {

        table = new JTable() {
            public boolean getScrollableTracksViewportWidth() {
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        table.setToolTipText("Coffee sorts table: click a Coffee sort to change or delete");
        table.setBorder(new EmptyBorder(1, 1, 5, 1));
        table.setRowHeight(26);
        //table.setEnabled(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFont(FONT_FOR_WIDGETS);

        JScrollPane scroller = new JScrollPane(table);
        scroller.setMinimumSize(new Dimension(1200, 200)); // wxh
        scroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scroller;
    }


    protected JToolBar getToolBarWithButtons() {

        addButton = getButton("Add");
        changeButton = getButton("Change");
        deleteButton = getButton("Delete");

        JToolBar toolBar = getToolBarForButtons();
        toolBar.add(addButton);
        toolBar.addSeparator(new Dimension(2, 0));
        toolBar.add(changeButton);
        toolBar.addSeparator(new Dimension(2, 0));
        toolBar.add(deleteButton);

        return toolBar;
    }

    private void doExceptionRoutine(Utils.ActionType action) {

        String s = action.toString();

        String msg = "<html><center>" +
                "An database exception has occurred<br>" +
                "while " + s + " the Note(s). Please check<br>" +
                "the log to view the technical details.<br>" +
                "Exiting the app." + "</center></html>";

        JOptionPane.showMessageDialog(frame, new JLabel(msg),
                "Error",
                JOptionPane.PLAIN_MESSAGE);

        System.exit(0);
    }

    private JToolBar getToolBarForButtons() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBorderPainted(false);
        toolBar.setFloatable(false);
        return toolBar;
    }

    protected JButton getButton(String label__) {

        JButton button = new JButton(label__);
        button.setBorderPainted(false);

        switch (label__) {
            case "Add":
                button.addActionListener(new AddActionListener());
                break;
            case "Delete":
                button.addActionListener(new DeleteActionListener());
                break;
            case "Change":
                button.addActionListener(new ChangeActionListener());
                break;
            default:
                throw new IllegalArgumentException("*** Invalid button label:" + label__ + " ***");
        }

        button.setToolTipText(label__);
        return button;
    }

    private void updateGUI() {
        table.updateUI();
    }

    public void update(Utils.Update update) {

        switch (update.getType()) {
            case ADD:
                current.addElem(update.getSort());
                getCoffeeSortsModel().add(update.getSort());
                break;
            case CHANGE:
                current.removeItem(update.getInd());
                current.addElem(update.getInd(), update.getSort());
                getCoffeeSortsModel().update(update.getInd(), update.getSort());
                break;
            case DELETE:
                current.removeItem(update.getInd());
                getCoffeeSortsModel().removeRow(update.getInd());
        }
        updateGUI();
    }

    protected CoffeeSortTableModel getCoffeeSortsModel() {
        return (CoffeeSortTableModel) table.getModel();
    }


    private class AddActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            addButton.setEnabled(false);
            changeButton.setEnabled(false);
            deleteButton.setEnabled(false);
            AddCoffeeSortGUI added = new AddCoffeeSortGUI(frame);

            CoffeeSort newSort = added.getResult();

            if (newSort != null) {

                try {
                    newSort = coffeeSortDAO.create(newSort);
                } catch (AppPersistenceException appPersistenceException) {
                    doExceptionRoutine(Utils.ActionType.CREATE);
                }
                current.addElem(newSort);
                getCoffeeSortsModel().add(newSort);
                sendChanges(Utils.UpdateType.ADD, current.size() - 1, newSort);
            }

            updateGUI();
            addButton.setEnabled(true);
            changeButton.setEnabled(true);
            deleteButton.setEnabled(true);

            if (getCoffeeSortsModel().isEmpty()) {
                deleteButton.setEnabled(false);
                changeButton.setEnabled(false);
            }
        }
    }


    private class DeleteActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int ix = table.getSelectedRow();
            if (ix < 0) return;


            addButton.setEnabled(false);
            changeButton.setEnabled(false);
            deleteButton.setEnabled(false);


            CoffeeSort selected = current.get(ix);
            try {
                coffeeSortDAO.delete(selected.getId());
            } catch (AppPersistenceException appPersistenceException) {
                doExceptionRoutine(Utils.ActionType.DELETE);
            }
            current.removeItem(ix);
            getCoffeeSortsModel().remove(ix);
            sendChanges(Utils.UpdateType.DELETE, ix, selected);
            updateGUI();


            addButton.setEnabled(true);
            changeButton.setEnabled(true);
            deleteButton.setEnabled(true);

            if (getCoffeeSortsModel().isEmpty()) {
                deleteButton.setEnabled(false);
                changeButton.setEnabled(false);
            }


        }
    }

    private class ChangeActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int ix = table.getSelectedRow();
            if (ix < 0) return;

            addButton.setEnabled(false);
            changeButton.setEnabled(false);
            deleteButton.setEnabled(false);


            CoffeeSort selected = current.get(ix);
            ChangeCoffeeSortGUI changed = new ChangeCoffeeSortGUI(selected, frame);

            CoffeeSort result = changed.getResult();

            if (result != null) {
                result.setId(selected.getId());
                try {
                    coffeeSortDAO.update(result);
                } catch (AppPersistenceException appPersistenceException) {
                    doExceptionRoutine(Utils.ActionType.CHANGE);
                }

                current.removeItem(ix);
                current.addElem(ix, result);
                getCoffeeSortsModel().update(ix, result);
                sendChanges(Utils.UpdateType.CHANGE, ix, result);
                updateGUI();
            }

            addButton.setEnabled(true);
            changeButton.setEnabled(true);
            deleteButton.setEnabled(true);

            if (getCoffeeSortsModel().isEmpty()) {
                deleteButton.setEnabled(false);
                changed.setEnabled(false);
            }
        }
    }

    private void sendChanges(Utils.UpdateType type, int ix, CoffeeSort result) {
        parent.notifyFrames(new Utils.Update(type, ix, result), num);
    }
}