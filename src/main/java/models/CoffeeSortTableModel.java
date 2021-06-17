package main.java.models;

import main.java.domain.CoffeeSort;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;

public class CoffeeSortTableModel extends DefaultTableModel {

    public CoffeeSortTableModel() {
        super();
    }

    public CoffeeSortTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    public CoffeeSortTableModel(Object[][] data, Object[] headers) {
        super(data, headers);
    }

    private int position(CoffeeSort coffeeSort) {
        for (int i = 0; i < getRowCount(); i++) {
            if (coffeeSort.equals(getDataVector().get(i))) {
                return i;
            }
        }
        return -1;
    }

    public boolean contains(CoffeeSort coffeeSort) {
        return position(coffeeSort) != -1;
    }

    public boolean isEmpty() {
        return getRowCount() == 0;
    }

    public void add(CoffeeSort sort) {
        addRow(sort.toStringArray());
    }

    public void remove(int ind) {
        removeRow(ind);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void update(int inx, CoffeeSort sort) {
        String[] vals = sort.toStringArray();
        for (int i = 0; i < getColumnCount(); i++) {
            setValueAt(vals[i], inx, i);
        }
    }

    public void filter(String name) {
        for (int i = getRowCount() - 1; i >= 0; i--) {
            Object val = getValueAt(i, 0);
            if (!name.equals(val.toString())) {
                removeRow(i);
            }
        }
    }

    public void filter(float from, float to) {
        for (int i = getRowCount() - 1; i >= 0; i--) {
            Object val = getValueAt(i, 1);
            float floatVal = Float.parseFloat(val.toString());
            if (!(from <= floatVal && floatVal <= to)) {
                removeRow(i);
            }
        }
    }

    public void filter(Date from, Date to) {
        for (int i = getRowCount() - 1; i >= 0; i--) {
            Object val = getValueAt(i, 2);
            Date dateVal = Date.valueOf(val.toString());
            if (!(from.compareTo(dateVal) <= 0 && to.compareTo(dateVal) >= 0)) {
                removeRow(i);
            }
        }
    }

}
