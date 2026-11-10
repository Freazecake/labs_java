package com.factory.io;

import com.factory.model.Truck;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonFileHandler {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");

    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class,
                        (JsonSerializer<Date>) (src, type,
                                ctx) -> new JsonPrimitive(src == null ? "" : SDF.format(src)))
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, type, ctx) -> {
                    try {
                        String s = json.getAsString();
                        return (s == null || s.isEmpty()) ? null : SDF.parse(s);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .setPrettyPrinting()
                .create();
    }

    public static void writeToJson(String fileName, List<Truck> trucks) throws IOException {
        try (Writer w = new FileWriter(fileName)) {
            createGson().toJson(trucks, w);
        }
    }

    public static List<Truck> readFromJson(String fileName) throws IOException {
        File f = new File(fileName);
        if (!f.exists())
            return new ArrayList<>();
        Type type = new TypeToken<List<Truck>>() {
        }.getType();
        try (Reader r = new FileReader(f)) {
            List<Truck> result = createGson().fromJson(r, type);
            return result != null ? result : new ArrayList<>();
        }
    }
}