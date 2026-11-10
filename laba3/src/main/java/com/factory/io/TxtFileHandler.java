package com.factory.io;

import com.factory.model.Truck;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TxtFileHandler {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");
    private static final String LOG_FILE = "errors.log";

    public static List<Truck> readFromFile(String fileName) {
        List<Truck> trucks = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            System.err.println("Файл не найден: " + fileName);
            return trucks;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                if (line.trim().isEmpty())
                    continue;
                try {
                    trucks.add(parseLine(line));
                } catch (Exception e) {
                    String msg = "Строка " + lineNum + ": \"" + line + "\" -> " + e.getMessage();
                    System.err.println("Ошибка чтения: " + msg);
                    logError(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trucks;
    }

    private static Truck parseLine(String line) throws Exception {
        String[] parts = line.split(";", -1);
        if (parts.length < 7)
            throw new IllegalArgumentException("Ожидалось 7 полей, найдено " + parts.length);

        Truck t = new Truck();
        t.setId(Integer.parseInt(parts[0].trim()));

        String type = parts[1].trim();
        if (type.isEmpty())
            throw new IllegalArgumentException("Пустой тип");
        t.setType(type);

        String model = parts[2].trim();
        if (model.isEmpty())
            throw new IllegalArgumentException("Пустая модель");
        t.setModel(model);

        double power = Double.parseDouble(parts[3].trim());
        if (power <= 0)
            throw new IllegalArgumentException("Мощность <= 0");
        t.setEnginePower(power);

        int speed = Integer.parseInt(parts[4].trim());
        if (speed <= 0)
            throw new IllegalArgumentException("Скорость <= 0");
        t.setMaxSpeed(speed);

        t.setProductionDate(SDF.parse(parts[5].trim()));

        double price = Double.parseDouble(parts[6].trim());
        if (price < 0)
            throw new IllegalArgumentException("Цена < 0");
        t.setPrice(price);
        return t;
    }

    public static void writeToFile(String fileName, List<Truck> trucks) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Truck t : trucks) {
                String date = t.getProductionDate() != null ? SDF.format(t.getProductionDate()) : "";
                bw.write(String.format("%d;%s;%s;%.2f;%d;%s;%.2f%n",
                        t.getId(), t.getType(), t.getModel(),
                        t.getEnginePower(), t.getMaxSpeed(), date, t.getPrice()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logError(String msg) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(new Date() + " " + msg);
        } catch (IOException ignored) {
        }
    }
}