package main.java.app;

import main.java.persistence.CoffeeSortPersistence;
import main.java.gui.CoffeeSortsAppGUI;
import main.java.gui.FilterCoffeeSortGUI;
import main.java.gui.SimpleTableGUI;

import javax.swing.*;

public class ApplicationStarter {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controller controller = new Controller();
            CoffeeSortPersistence dao = new CoffeeSortPersistence();
            CoffeeSortsAppGUI simpleTable = new SimpleTableGUI(controller, 0, dao);
            CoffeeSortsAppGUI filterTable = new FilterCoffeeSortGUI(controller, 1, dao);
            controller.add(simpleTable);
            controller.add(filterTable);
        });
    }
}