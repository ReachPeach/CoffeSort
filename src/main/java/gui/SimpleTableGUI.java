package main.java.gui;

import main.java.persistence.CoffeeSortPersistence;
import main.java.app.Controller;

public class SimpleTableGUI extends CoffeeSortsAppGUI {

    public SimpleTableGUI(Controller parent, int num, CoffeeSortPersistence dao) {
        super(parent, num, dao);
    }
}