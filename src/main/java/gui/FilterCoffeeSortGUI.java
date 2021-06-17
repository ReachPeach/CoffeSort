package main.java.gui;

import main.java.app.Controller;
import main.java.common.Utils;
import main.java.domain.Filter;
import main.java.exceptions.FilterException;
import main.java.models.CoffeeSortListModel;
import main.java.models.CoffeeSortTableModel;
import main.java.models.FilterTableModel;
import main.java.persistence.CoffeeSortPersistence;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static main.java.common.Utils.FONT_FOR_WIDGETS;
import static main.java.gui.GUIUtils.displayMessage;

public class FilterCoffeeSortGUI extends CoffeeSortsAppGUI {
    private final String[] columnsHeaders = {"Тип фильтра", "границы"};
    private JTable allElems;
    private CoffeeSortListModel allElemsList;

    private JTable filtersTable;
    private List<Filter> applied;

    public FilterCoffeeSortGUI(Controller parent, int num, CoffeeSortPersistence dao) {
        super(parent, num, dao);
        additionalInit();
    }

    public void copyAll(JTable tableFrom, JTable tableTo) {
        final TableModel tableAModel = tableFrom.getModel();
        final DefaultTableModel copy = new CoffeeSortTableModel(tableAModel.getRowCount(), 0);
        for (int column = 0; column < tableAModel.getColumnCount(); column++) {
            copy.addColumn(tableAModel.getColumnName(column));
            for (int row = 0; row < tableAModel.getRowCount(); row++)
                copy.setValueAt(tableAModel.getValueAt(row, column), row, column);
        }
        tableTo.setModel(copy);
    }


    private void additionalInit() {
        applied = new ArrayList<>();
        allElems = new JTable(new CoffeeSortTableModel());
        copyAll(getTable(), allElems);
        allElemsList = new CoffeeSortListModel(getCurrent());

        filtersTable.setModel(new FilterTableModel(columnsHeaders));

        DefaultTableCellRenderer centreRender = new DefaultTableCellRenderer();
        centreRender.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnsHeaders.length; i++) {
            filtersTable.getColumnModel().getColumn(i).setCellRenderer(centreRender);
        }
    }


    private Box filterAcidity, filterManufacture, filterCollection;

    @Override
    protected void addWidgetsToFrame(JFrame frame) {

        Container pane = frame.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        pane.add(getTableInScrollPane());
        pane.add(getToolBarWithButtons());
        pane.add(getFiltersBar());
        pane.add(getFilterTable());
    }

    protected JScrollPane getFilterTable() {

        filtersTable = new JTable() {
            public boolean getScrollableTracksViewportWidth() {
                return getPreferredSize().width < getParent().getWidth();
            }
        };
        filtersTable.setToolTipText("Filter table: double-click row to delete it");
        filtersTable.setRowHeight(26);
        //table.setEnabled(false);
        filtersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        filtersTable.setFont(FONT_FOR_WIDGETS);
        filtersTable.addMouseListener(new ListMouseListener());
        filtersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        filtersTable.setMaximumSize(new Dimension(1200, 50));
        JScrollPane scroller = new JScrollPane(filtersTable);
        scroller.setMaximumSize(new Dimension(1200, 50)); // wxh
        scroller.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scroller;
    }

    private Box getFiltersBar() {
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(filterAcidity = getFilter("acidity", Utils.FilterType.ACIDITY));
        contents.add(filterManufacture = getFilter("manufacture country", Utils.FilterType.MANUFACTURE));
        contents.add(filterCollection = getFilter("collection date", Utils.FilterType.COLLECTION));

        return contents;
    }

    private Box getFilter(String name, Utils.FilterType type) {
        Box result = new Box(BoxLayout.X_AXIS);
        JButton applyFilter = getButton("Filter " + name + ":", type);
        JTextField from = new JTextField();
        from.setHorizontalAlignment(SwingConstants.CENTER);
        from.setPreferredSize(new Dimension(52, 26));
        JLabel helper = new JLabel(" ");

        result.add(applyFilter);
        result.add(from);

        if (!name.equals("manufacture country")) {
            JTextField to = new JTextField();
            to.setHorizontalAlignment(SwingConstants.CENTER);
            from.setMaximumSize(new Dimension(52, 26));
            to.setMaximumSize(new Dimension(52, 26));
            result.add(to);
            helper.setText("Insert 2 values!");
        } else {
            from.setMaximumSize(new Dimension(70, 26));
            helper.setText("Insert 1 value!");
        }

        result.add(helper);

        return result;
    }

    private JButton getButton(String label__, Utils.FilterType type) {

        JButton button = new JButton(label__);
        button.setBorderPainted(false);

        button.addActionListener(new ApplyFilterListener(type));

        button.setToolTipText(label__);
        return button;
    }


    private void updateGUI(boolean applyAgain) {
        if (applyAgain) {
            current = new CoffeeSortListModel(allElemsList);
            copyAll(allElems, getTable());
            applied.forEach(this::applyFilter);
        }
        filtersTable.updateUI();
        getTable().updateUI();
    }

    public void update(Utils.Update update) {

        switch (update.getType()) {
            case ADD:
                allElemsList.addElem(update.getSort());
                getListedCoffeeSortsModel().add(update.getSort());
                break;
            case CHANGE:
                allElemsList.removeItem(update.getInd());
                allElemsList.addElem(update.getInd(), update.getSort());
                getListedCoffeeSortsModel().update(update.getInd(), update.getSort());
                break;
            case DELETE:
                allElemsList.removeItem(update.getInd());
                getCoffeeSortsModel().removeRow(update.getInd());
        }
        updateGUI(true);
    }

    private Box getFilterBoxByType(Utils.FilterType type) {
        switch (type) {
            case COLLECTION:
                return filterCollection;
            case MANUFACTURE:
                return filterManufacture;
            case ACIDITY:
                return filterAcidity;
        }
        return null;
    }

    private class ApplyFilterListener implements ActionListener {
        private final Utils.FilterType type;

        public ApplyFilterListener(Utils.FilterType type) {
            this.type = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Box filter = getFilterBoxByType(type);

            JButton button = (JButton) filter.getComponent(0);

            JTextField from = (JTextField) filter.getComponent(1);
            String filterFrom = from.getText().trim();

            JTextField to = null;
            String filterTo = null;
            if (type != Utils.FilterType.MANUFACTURE) {
                to = (JTextField) filter.getComponent(2);
                filterTo = to.getText().trim();
            }

            JLabel help = (JLabel) filter.getComponent(filter.getComponentCount() - 1);

            try {
                switch (type) {
                    case ACIDITY:
                        try {
                            float leftBoard = Float.parseFloat(filterFrom);
                            float rightBoard = Float.parseFloat(filterTo);
                            if (leftBoard > rightBoard) {
                                throw new FilterException("Right boarder is less then left!");
                            }

                            applied.add(new Filter(Utils.FilterType.ACIDITY, leftBoard, rightBoard));
                        } catch (NumberFormatException ex) {
                            throw new FilterException("Illegal format of floats on imput!", ex);
                        }
                        break;
                    case COLLECTION:
                        try {
                            Date leftBoard = Date.valueOf(filterFrom);
                            Date rightBoard = Date.valueOf(filterTo);

                            if (rightBoard.before(leftBoard)) {
                                throw new FilterException("Right boarder is less then left!");
                            }

                            applied.add(new Filter(Utils.FilterType.COLLECTION, leftBoard, rightBoard));
                        } catch (IllegalArgumentException ex) {
                            throw new FilterException("Illegal format of dates on input!", ex);
                        }
                        break;
                    case MANUFACTURE:
                        if (filterFrom.chars().allMatch(c -> Character.isUpperCase(c) && Character.isAlphabetic(c)) && filterFrom.length() == 3) {
                            applied.add(new Filter(Utils.FilterType.MANUFACTURE, filterFrom));
                        } else {
                            throw new FilterException("Invalid format of country name on input!");
                        }
                        break;
                }

            } catch (FilterException ex) {
                displayMessage(help, ex.getMessage(), GUIUtils.MessageType.WARN);
                return;
            }
            getListedFiltersModel().add(applied.get(applied.size() - 1));
            applyFilter(applied.get(applied.size() - 1));
            updateGUI(false);

            displayMessage(help, "Filter added!", GUIUtils.MessageType.INFO);
            from.setText("");
            if (filterTo != null) {
                to.setText("");
            }
        }
    }

    private CoffeeSortTableModel getListedCoffeeSortsModel() {
        return (CoffeeSortTableModel) allElems.getModel();
    }

    private FilterTableModel getListedFiltersModel() {
        return (FilterTableModel) filtersTable.getModel();
    }

    private void applyFilter(Filter filter) {
        switch (filter.getType()) {
            case MANUFACTURE:
                applyFilter(filter.getName().toString());
                break;
            case ACIDITY:
                applyFilter(Float.parseFloat(filter.getFrom().toString()), Float.parseFloat(filter.getTo().toString()));
                break;
            case COLLECTION:
                applyFilter(Date.valueOf(filter.getFrom().toString()), Date.valueOf(filter.getTo().toString()));
                break;
        }
        //getListedFiltersModel().add(filter);
        updateGUI(false);
    }

    private void applyFilter(String filterDate) {
        getCoffeeSortsModel().filter(filterDate);
        current.filter(filterDate);
    }

    private void applyFilter(Float from, Float to) {
        getCoffeeSortsModel().filter(from, to);
        current.filter(from, to);
    }

    private void applyFilter(Date from, Date to) {
        getCoffeeSortsModel().filter(from, to);
        current.filter(from, to);
    }

    private class ListMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if ((e.getButton() == MouseEvent.BUTTON1) &&
                    (e.getClickCount() == 2)) { // double click on left button
                int ind = filtersTable.getSelectedRow();
                if (ind < 0) return;
                applied.remove(ind);
                getListedFiltersModel().remove(ind);
                updateGUI(true);
            }
        }
    }
}
