package main.java.common;

import main.java.domain.CoffeeSort;

import java.awt.*;

public class Utils {

    public enum UpdateType {ADD, CHANGE, DELETE}

    public static class Update {
        private final UpdateType type;
        private final int ind;
        private final CoffeeSort sort;

        public UpdateType getType() {
            return type;
        }

        public int getInd() {
            return ind;
        }

        public CoffeeSort getSort() {
            return sort;
        }

        public Update(UpdateType type, int ind, CoffeeSort sort) {
            this.type = type;
            this.ind = ind;
            this.sort = sort;
        }
    }

    public enum FilterType {
        ACIDITY("Acidity"), MANUFACTURE("Manufacture"), COLLECTION("Collection");
        private final String text;

        FilterType(String s) {
            text = s;
        }

        public String toString() {
            return text;
        }

    }

    public static final Font FONT_FOR_WIDGETS =
            new Font("SansSerif", Font.PLAIN, 16);
    static final Font FONT_FOR_EDITOR =
            new Font("Comic Sans MS", Font.PLAIN, 16);

    enum MessageType {INFO, WARN, NONE}

    public enum ActionType {
        LOAD("LOAD"), CREATE("CREATE"), CHANGE("CHANGE"), DELETE("DELETE");
        private final String text;

        ActionType(String s) {
            this.text = s;
        }

        public String toString() {
            return text;
        }
    }

    public String[][] data = new String[][]{{"USA", "3.5", "1999-12-01", "2000-02-21", "RUS", "Perfecto", "4"},
            {"KAZ", "1.5", "23.11.2009", "1.1.2012", "GER", "Mediums", "2"},
            {"ISR", "7.0", "1.01.2020", "20.02.2020", "FIN", "Black", "8"}};


}
