package com.factory;

import com.factory.model.Truck;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TruckTest {
    @Test
    void settersGetters() {
        Truck t = new Truck();
        t.setId(1);
        t.setType("Volvo");
        t.setModel("FH16");
        t.setEnginePower(750);
        t.setMaxSpeed(120);
        Date d = new Date();
        t.setProductionDate(d);
        t.setPrice(100000);

        assertEquals(1, t.getId());
        assertEquals("Volvo", t.getType());
        assertEquals("FH16", t.getModel());
        assertEquals(750, t.getEnginePower());
        assertEquals(120, t.getMaxSpeed());
        assertEquals(d, t.getProductionDate());
        assertEquals(100000, t.getPrice());
    }

    @Test
    void equalsById() {
        Truck a = new Truck();
        a.setId(5);
        Truck b = new Truck();
        b.setId(5);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toStringHasModel() {
        Truck t = new Truck();
        t.setId(1);
        t.setModel("Scania");
        assertTrue(t.toString().contains("Scania"));
    }
}