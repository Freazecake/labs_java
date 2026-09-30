package main.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        System.out.println("Источник данных: 1 - консоль, 2 - файл " + INPUT_FILE
                + " (1-я строка - лексемы, 2-я - разделители)");
        String choice = scanner.nextLine().trim();

        String line;
        String delimiters;
        try {
            if (choice.equals("2")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE, StandardCharsets.UTF_8))) {
                    line = reader.readLine();
                    delimiters = reader.readLine();
                }
                if (line == null || delimiters == null) {
                    System.out.println("В файле должно быть две строки");
                    return;
                }
            } else {
                System.out.println("Введите строку с лексемами:");
                line = scanner.nextLine();
                System.out.println("Введите символы-разделители (без повторов):");
                delimiters = scanner.nextLine();
            }
            scanner.close();

            StringProcessor processor = new StringProcessor(new Random());
            String report = processor.report(line, processor.process(line, delimiters));
            System.out.print(report);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE, StandardCharsets.UTF_8))) {
                writer.write(report);
            }
            System.out.println("Результат записан в " + OUTPUT_FILE);
        } catch (IllegalArgumentException | IOException | ParseException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}