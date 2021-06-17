package main.java.gui;

import main.java.domain.CoffeeSort;
import main.java.exceptions.InvalidCoffeeSortException;
import main.java.validator.CoffeeSortValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static main.java.common.Utils.FONT_FOR_WIDGETS;
import static main.java.gui.GUIUtils.displayMessage;
import static main.java.gui.GUIUtils.getButton;

public class AddCoffeeSortGUI extends JDialog {
    private final String[] columnsHeader = new String[]{"Страна изготовления", "Кислотность",
            "Дата сбора", "Дата обжарки", "Страна доставки", "Название сорта", "Глубина обжарки"};
    private final JTextField[] out;
    private JLabel messageLabel;
    private CoffeeSort result = null;


    public AddCoffeeSortGUI(JFrame parent) {
        super(parent, "Add extra coffee sort", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Box contents = new Box(BoxLayout.Y_AXIS);
        Box headers = new Box(BoxLayout.X_AXIS);
        Box forms = new Box(BoxLayout.X_AXIS);
        Box buttonsAndLabel = new Box(BoxLayout.X_AXIS);

        out = new JTextField[columnsHeader.length];
        for (int i = 0; i < columnsHeader.length; i++) {
            JTextField infoField = new JTextField(columnsHeader[i]);
            infoField.setEditable(false);
            infoField.setHorizontalAlignment(SwingConstants.CENTER);
            infoField.setMaximumSize(new Dimension(columnsHeader[i].length() * 10, 26));
            headers.add(infoField);

            JTextField inputField = new JTextField();
            inputField.setHorizontalAlignment(SwingConstants.CENTER);
            inputField.setMaximumSize(new Dimension(columnsHeader[i].length() * 10, 26));
            inputField.requestFocusInWindow();
            out[i] = inputField;
            forms.add(inputField);
        }

        contents.add(headers);
        contents.add(forms);

        buttonsAndLabel.add(getToolBarWithButtons());
        buttonsAndLabel.add(getMessageLabel());

        contents.add(buttonsAndLabel);
        setContentPane(contents);
        setSize(800, 210);
        setPreferredSize(new Dimension(800, 210));
        setMinimumSize(new Dimension(800, 210));
        setMaximumSize(new Dimension(1200, 210));
        setVisible(true);
    }

    private JToolBar getToolBarWithButtons() {

        JButton submitButton = getButton("Submit", new SubmitActionListener());
        JButton cancelButton = getButton("Cancel", new CancelActionListener());

        JToolBar toolBar = getToolBarForButtons();
        toolBar.add(submitButton);
        toolBar.addSeparator(new Dimension(2, 0));
        toolBar.add(cancelButton);
        toolBar.addSeparator(new Dimension(2, 0));

        return toolBar;
    }


    private JToolBar getToolBarForButtons() {
        JToolBar toolBar = new JToolBar();
        toolBar.setBorderPainted(false);
        toolBar.setFloatable(false);
        return toolBar;
    }


    private JLabel getMessageLabel() {
        messageLabel = new JLabel();
        messageLabel.setFont(FONT_FOR_WIDGETS);
        displayMessage(messageLabel, "Please, submit all fields to add the sort", GUIUtils.MessageType.INFO);
        return messageLabel;
    }


    private class SubmitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String manufacture = out[0].getText().trim(), acidity = out[1].getText().trim(),
                    collectionDate = out[2].getText().trim(), fireDate = out[3].getText().trim(),
                    delivery = out[4].getText().trim(), name = out[5].getText().trim(),
                    roast = out[6].getText().trim();
            try {
                if (CoffeeSortValidator.valid(manufacture, acidity, collectionDate, fireDate, delivery, name, roast)) {
                    result = new CoffeeSort(manufacture, acidity, collectionDate, fireDate, delivery, name, roast);
                    setVisible(false);
                }
            } catch (InvalidCoffeeSortException invalidCoffeeSortException) {
                displayMessage(messageLabel, invalidCoffeeSortException.getMessage(), GUIUtils.MessageType.WARN);
            }
        }
    }

    private class CancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }

    public CoffeeSort getResult() {
        return result;
    }
}
