package main.java.app;

import main.java.common.Utils;
import main.java.gui.CoffeeSortsAppGUI;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<CoffeeSortsAppGUI> frames;

    Controller() {
        frames = new ArrayList<>();
    }

    Controller(List<CoffeeSortsAppGUI> list) {
        frames = list;
    }

    public void add(CoffeeSortsAppGUI val) {
        frames.add(val);
    }

    public void notifyFrames(Utils.Update update, int num) {
        for (int i = 0; i < frames.size(); i++) {
            if (i != num) {
                frames.get(i).update(update);
            }
        }
    }
}
