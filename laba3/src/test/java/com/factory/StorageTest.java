package com.factory;

import com.factory.model.Truck;
import com.factory.storage.ListStorage;
import com.factory.storage.MapStorage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {
    private Truck make(int id) {
        Truck t = new Truck();
        t.setId(id);
        t.setType("T" + id);
        t.setModel("M" + id);
        t.setEnginePower(100);
        t.setMaxSpeed(80);
        t.setPrice(1000);
        return t;
    }

    @Test
    void listStorageCrud() {
        ListStorage s = new ListStorage();
        s.add(make(1));
        s.add(make(2));
        assertEquals(2, s.size());
        assertNotNull(s.findById(1));
        s.delete(1);
        assertNull(s.findById(1));
    }

    @Test
    void mapStorageCrud() {
        MapStorage s = new MapStorage();
        s.add(make(1));
        s.add(make(2));
        assertEquals(2, s.size());
        s.delete(2);
        assertNull(s.findById(2));
    }

    @Test
    void mapKeepsSortedById() {
        MapStorage s = new MapStorage();
        s.add(make(3));
        s.add(make(1));
        s.add(make(2));
        List<Integer> ids = s.getAll().stream().map(Truck::getId).toList();
        assertEquals(List.of(1, 2, 3), ids);
    }

    @Test
    void updateExisting() {
        ListStorage s = new ListStorage();
        Truck t = make(1);
        s.add(t);
        t.setModel("Updated");
        s.update(t);
        assertEquals("Updated", s.findById(1).getModel());
    }
}