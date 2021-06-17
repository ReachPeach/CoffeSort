package main.java.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUIUtils {

    protected static void displayMessage(JLabel messageLabel, String msg, MessageType type) {
        if (type == MessageType.WARN) {

            messageLabel.setText(" " + msg);
            messageLabel.setForeground(Color.RED);
        } else if (type == MessageType.INFO) {
            messageLabel.setText(" " + msg);
            messageLabel.setForeground(Color.BLUE);
        } else {
            messageLabel.setText("");
        }
    }

    protected static JButton getButton(String name, ActionListener listener) {

        JButton button = new JButton(name);
        button.setBorderPainted(false);

        button.addActionListener(listener);

        button.setToolTipText(name);
        return button;
    }

    protected enum MessageType {INFO, WARN, NONE}

}
