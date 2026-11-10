package com.factory.storage;

import com.factory.model.Truck;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Truck> trucks = new ArrayList<>();

    @Override
    public void add(Truck item) {
        trucks.add(item);
    }

    @Override
    public void update(Truck item) {
        for (int i = 0; i < trucks.size(); i++) {
            if (trucks.get(i).getId() == item.getId()) {
                trucks.set(i, item);
                return;
            }
        }
        throw new IllegalArgumentException("Грузовик с id=" + item.getId() + " не найден");
    }

    @Override
    public void delete(int id) {
        trucks.removeIf(t -> t.getId() == id);
    }

    @Override
    public List<Truck> getAll() {
        return trucks;
    }

    @Override
    public Truck findById(int id) {
        return trucks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void clear() {
        trucks.clear();
    }

    @Override
    public int size() {
        return trucks.size();
    }

    public void printAll() {
        Iterator<Truck> it = trucks.iterator();
        while (it.hasNext())
            System.out.println(it.next());
    }
}