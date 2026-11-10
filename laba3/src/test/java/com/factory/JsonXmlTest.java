package com.factory;

import com.factory.io.JsonFileHandler;
import com.factory.io.XmlFileHandler;
import com.factory.model.Truck;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonXmlTest {
    private static final String XML = "test_trucks.xml";
    private static final String JSON = "test_trucks.json";

    @AfterEach
    void clean() {
        new File(XML).delete();
        new File(JSON).delete();
    }

    private Truck make() {
        Truck t = new Truck();
        t.setId(10);
        t.setType("MAN");
        t.setModel("TGX");
        t.setEnginePower(540);
        t.setMaxSpeed(100);
        t.setProductionDate(new Date(1571000000000L));
        t.setPrice(98000);
        return t;
    }

    @Test
    void xmlRoundTrip() throws Exception {
        XmlFileHandler.writeToXml(XML, List.of(make()));
        List<Truck> loaded = XmlFileHandler.readFromXml(XML);
        assertEquals(1, loaded.size());
        assertEquals("MAN", loaded.get(0).getType());
    }

    @Test
    void jsonRoundTrip() throws Exception {
        JsonFileHandler.writeToJson(JSON, List.of(make()));
        List<Truck> loaded = JsonFileHandler.readFromJson(JSON);
        assertEquals(1, loaded.size());
        assertEquals("TGX", loaded.get(0).getModel());
        assertNotNull(loaded.get(0).getProductionDate());
    }
}