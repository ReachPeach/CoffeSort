package main.java.models;

import main.java.domain.CoffeeSort;

import java.sql.Date;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoffeeSortListModel extends AbstractList<CoffeeSort> {
    private List<CoffeeSort> data;

    public CoffeeSortListModel(List<CoffeeSort> l) {
        data = new ArrayList<>(l);
    }

    @Override
    public CoffeeSort get(int index) {
        return data.get(index);
    }

    @Override
    public int size() {
        return data.size();
    }

    public void removeItem(int index) {
        data.remove(index);
    }

    public void addElem(CoffeeSort sort) {
        data.add(sort);
    }

    public void addElem(int ind, CoffeeSort sort) {
        data.add(ind, sort);
    }

    public void filter(String name) {
        data = data.stream().filter(elem -> elem.getName().equals(name)).collect(Collectors.toList());
    }

    public void filter(float from, float to) {
        data = data.stream().filter(elem -> Float.compare(from, elem.getAcidity()) <= 0 && Float.compare(elem.getAcidity(), to) <= 0)
                .collect(Collectors.toList());
    }

    public void filter(Date from, Date to) {
        data = data.stream().filter(elem -> from.compareTo(elem.getCollectionDate()) <= 0 && to.compareTo(elem.getCollectionDate()) >= 0)
                .collect(Collectors.toList());
    }
}
