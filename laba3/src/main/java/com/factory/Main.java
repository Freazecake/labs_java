package com.factory;

import com.factory.io.TxtFileHandler;
import com.factory.menu.Menu;
import com.factory.model.Truck;
import com.factory.storage.ListStorage;
import com.factory.storage.MapStorage;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ListStorage listStorage = new ListStorage();
        MapStorage mapStorage = new MapStorage();

        if (new File("trucks.txt").exists()) {
            List<Truck> loaded = TxtFileHandler.readFromFile("trucks.txt");
            for (Truck t : loaded) {
                listStorage.add(t);
                mapStorage.add(t);
            }
        }

        new Menu(listStorage, mapStorage).show();
    }
}