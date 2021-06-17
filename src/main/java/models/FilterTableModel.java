package main.java.models;

import main.java.domain.Filter;

import javax.swing.table.DefaultTableModel;

public class FilterTableModel extends DefaultTableModel {

    public FilterTableModel(Object[] headers) {
        super(headers, 0);
    }


    public void add(Filter filter) {
        addRow(filter.toStringArray());
    }

    public void remove(int ind) {
        removeRow(ind);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }


}
