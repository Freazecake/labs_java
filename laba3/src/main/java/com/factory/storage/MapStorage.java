package com.factory.storage;

import com.factory.model.Truck;

import java.util.*;

public class MapStorage extends AbstractStorage {
    private final Map<Integer, Truck> trucks = new TreeMap<>();

    @Override
    public void add(Truck item) {
        trucks.put(item.getId(), item);
    }

    @Override
    public void update(Truck item) {
        if (!trucks.containsKey(item.getId()))
            throw new IllegalArgumentException("Грузовик с id=" + item.getId() + " не найден");
        trucks.put(item.getId(), item);
    }

    @Override
    public void delete(int id) {
        trucks.remove(id);
    }

    @Override
    public List<Truck> getAll() {
        return new ArrayList<>(trucks.values());
    }

    @Override
    public Truck findById(int id) {
        return trucks.get(id);
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
        Iterator<Map.Entry<Integer, Truck>> it = trucks.entrySet().iterator();
        while (it.hasNext())
            System.out.println(it.next().getValue());
    }
}