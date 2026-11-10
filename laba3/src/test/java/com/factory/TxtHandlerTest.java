package com.factory;

import com.factory.io.TxtFileHandler;
import com.factory.model.Truck;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TxtHandlerTest {
    private static final String FILE = "test_trucks.txt";

    @AfterEach
    void clean() {
        new File(FILE).delete();
        new File("errors.log").delete();
    }

    @Test
    void roundTrip() {
        Truck t = new Truck();
        t.setId(1);
        t.setType("Volvo");
        t.setModel("FH16");
        t.setEnginePower(750);
        t.setMaxSpeed(120);
        t.setPrice(100000);
        TxtFileHandler.writeToFile(FILE, List.of(t));

        List<Truck> loaded = TxtFileHandler.readFromFile(FILE);
        assertEquals(1, loaded.size());
        assertEquals("Volvo", loaded.get(0).getType());
    }

    @Test
    void invalidLineSkipped() throws Exception {
        try (PrintWriter pw = new PrintWriter(FILE)) {
            pw.println("1;Volvo;FH16;750;120;15.03.2020;120000");
            pw.println("Bad line");
            pw.println("3;Scania;R500;500;110;10.01.2021;115000");
        }
        List<Truck> loaded = TxtFileHandler.readFromFile(FILE);
        assertEquals(2, loaded.size());
    }
}