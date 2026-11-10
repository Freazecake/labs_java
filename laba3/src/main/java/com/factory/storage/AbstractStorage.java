package com.factory.storage;

import com.factory.model.Truck;

import java.util.List;

public abstract class AbstractStorage {
    public abstract void add(Truck item);

    public abstract void update(Truck item);

    public abstract void delete(int id);

    public abstract List<Truck> getAll();

    public abstract Truck findById(int id);

    public abstract void clear();

    public abstract int size();
}