package com.factory.menu;

import com.factory.io.JsonFileHandler;
import com.factory.io.TxtFileHandler;
import com.factory.io.XmlFileHandler;
import com.factory.model.Truck;
import com.factory.storage.ListStorage;
import com.factory.storage.MapStorage;
import com.factory.util.Archiver;
import com.factory.util.Encryptor;
import com.factory.util.Validator;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");
    private static final String TXT_FILE = "trucks.txt";
    private static final String XML_FILE = "trucks.xml";
    private static final String JSON_FILE = "trucks.json";
    private static final String ZIP_FILE = "trucks.zip";
    // MD5 от пароля "admin"
    private static final String ADMIN_PASSWORD_HASH = "21232f297a57a5a743894a0e4a801fc3";

    private final ListStorage listStorage;
    private final MapStorage mapStorage;
    private final Scanner scanner = new Scanner(System.in);

    public Menu(ListStorage listStorage, MapStorage mapStorage) {
        this.listStorage = listStorage;
        this.mapStorage = mapStorage;
    }

    public void show() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        addTruck();
                        break;
                    case "2":
                        editTruck();
                        break;
                    case "3":
                        deleteTruck();
                        break;
                    case "4":
                        listStorage.printAll();
                        break;
                    case "5":
                        mapStorage.printAll();
                        break;
                    case "6":
                        sortMenu();
                        break;
                    case "7":
                        saveTxt();
                        break;
                    case "8":
                        loadTxt();
                        break;
                    case "9":
                        saveXml();
                        break;
                    case "10":
                        loadXml();
                        break;
                    case "11":
                        saveJson();
                        break;
                    case "12":
                        loadJson();
                        break;
                    case "13":
                        demoEncryption();
                        break;
                    case "14":
                        adminLogin();
                        break;
                    case "15":
                        archiveData();
                        break;
                    case "0":
                        System.out.println("Выход.");
                        return;
                    default:
                        System.out.println("Неверный выбор");
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n========== ФАБРИКА ГРУЗОВИКОВ ==========");
        System.out.println("1. Добавить грузовик");
        System.out.println("2. Редактировать грузовик");
        System.out.println("3. Удалить грузовик");
        System.out.println("4. Вывести (List)");
        System.out.println("5. Вывести (Map)");
        System.out.println("6. Сортировка");
        System.out.println("7. Сохранить в TXT");
        System.out.println("8. Загрузить из TXT");
        System.out.println("9. Сохранить в XML");
        System.out.println("10. Загрузить из XML");
        System.out.println("11. Сохранить в JSON");
        System.out.println("12. Загрузить из JSON");
        System.out.println("13. Демо шифрования (MD5+AES)");
        System.out.println("14. Вход администратора");
        System.out.println("15. Архивировать данные (zip)");
        System.out.println("0. Выход");
        System.out.print("Выбор: ");
    }

    private void addTruck() {
        Truck t = readTruckFromConsole(null);
        if (t == null)
            return;
        listStorage.add(t);
        mapStorage.add(t);
        System.out.println("Добавлено: " + t);
    }

    private void editTruck() {
        System.out.print("ID для редактирования: ");
        int id = parseInt(scanner.nextLine(), -1);
        if (id < 0) {
            System.out.println("Неверный ID");
            return;
        }
        Truck existing = listStorage.findById(id);
        if (existing == null) {
            System.out.println("Не найдено");
            return;
        }
        System.out.println("Текущий: " + existing);
        Truck updated = readTruckFromConsole(existing);
        if (updated == null)
            return;
        listStorage.update(updated);
        mapStorage.update(updated);
        System.out.println("Обновлено: " + updated);
    }

    private void deleteTruck() {
        System.out.print("ID для удаления: ");
        int id = parseInt(scanner.nextLine(), -1);
        if (id < 0) {
            System.out.println("Неверный ID");
            return;
        }
        if (listStorage.findById(id) == null) {
            System.out.println("Не найдено");
            return;
        }
        listStorage.delete(id);
        mapStorage.delete(id);
        System.out.println("Удалён id=" + id);
    }

    private Truck readTruckFromConsole(Truck existing) {
        Truck t = new Truck();
        try {
            if (existing != null) {
                t.setId(existing.getId());
                System.out.println("(Enter — оставить текущее)");
            } else {
                int newId = generateNewId();
                System.out.print("ID [" + newId + "]: ");
                String s = scanner.nextLine().trim();
                t.setId(s.isEmpty() ? newId : Integer.parseInt(s));
            }
            t.setType(readString("Тип", existing == null ? null : existing.getType()));
            t.setModel(readString("Модель", existing == null ? null : existing.getModel()));
            t.setEnginePower(readDouble("Мощность", existing == null ? null : existing.getEnginePower()));
            t.setMaxSpeed(readInt("Макс. скорость", existing == null ? null : existing.getMaxSpeed()));
            t.setProductionDate(readDate("Дата выпуска (dd.MM.yyyy)",
                    existing == null ? null : existing.getProductionDate()));
            t.setPrice(readDouble("Цена", existing == null ? null : existing.getPrice()));
            return t;
        } catch (Exception e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
            return null;
        }
    }

    private int generateNewId() {
        int max = 0;
        for (Truck t : listStorage.getAll())
            if (t.getId() > max)
                max = t.getId();
        return max + 1;
    }

    private String readString(String label, String current) {
        while (true) {
            System.out.print(label + (current != null ? " [" + current + "]" : "") + ": ");
            String s = scanner.nextLine().trim();
            if (s.isEmpty() && current != null)
                return current;
            if (Validator.isNotBlank(s))
                return s;
            System.out.println("Поле не может быть пустым");
        }
    }

    private double readDouble(String label, Double cur) {
        while (true) {
            System.out.print(label + (cur != null ? " [" + cur + "]" : "") + ": ");
            String s = scanner.nextLine().trim();
            if (s.isEmpty() && cur != null)
                return cur;
            if (Validator.isPositiveDouble(s))
                return Double.parseDouble(s);
            System.out.println("Введите положительное число");
        }
    }

    private int readInt(String label, Integer cur) {
        while (true) {
            System.out.print(label + (cur != null ? " [" + cur + "]" : "") + ": ");
            String s = scanner.nextLine().trim();
            if (s.isEmpty() && cur != null)
                return cur;
            if (Validator.isPositiveInt(s))
                return Integer.parseInt(s);
            System.out.println("Введите целое положительное число");
        }
    }

    private Date readDate(String label, Date cur) {
        while (true) {
            System.out.print(label + (cur != null ? " [" + SDF.format(cur) + "]" : "") + ": ");
            String s = scanner.nextLine().trim();
            if (s.isEmpty() && cur != null)
                return cur;
            if (Validator.isValidDate(s)) {
                try {
                    return SDF.parse(s);
                } catch (Exception ignored) {
                }
            }
            System.out.println("Введите дату dd.MM.yyyy");
        }
    }

    private void sortMenu() {
        System.out.println("1-цена, 2-мощность, 3-скорость, 4-дата, 5-id");
        String c = scanner.nextLine().trim();
        Comparator<Truck> cmp;
        switch (c) {
            case "1":
                cmp = Comparator.comparingDouble(Truck::getPrice);
                break;
            case "2":
                cmp = Comparator.comparingDouble(Truck::getEnginePower);
                break;
            case "3":
                cmp = Comparator.comparingInt(Truck::getMaxSpeed);
                break;
            case "4":
                cmp = Comparator.comparing(Truck::getProductionDate,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "5":
                cmp = Comparator.comparingInt(Truck::getId);
                break;
            default:
                System.out.println("Неверный выбор");
                return;
        }
        List<Truck> list = listStorage.getAll();
        list.sort(cmp);
        list.forEach(System.out::println);
    }

    private void saveTxt() {
        TxtFileHandler.writeToFile(TXT_FILE, listStorage.getAll());
        System.out.println("Сохранено в " + TXT_FILE);
    }

    private void loadTxt() {
        reloadStorages(TxtFileHandler.readFromFile(TXT_FILE));
    }

    private void saveXml() throws Exception {
        XmlFileHandler.writeToXml(XML_FILE, listStorage.getAll());
        System.out.println("Сохранено в " + XML_FILE);
    }

    private void loadXml() throws Exception {
        reloadStorages(XmlFileHandler.readFromXml(XML_FILE));
    }

    private void saveJson() throws Exception {
        JsonFileHandler.writeToJson(JSON_FILE, listStorage.getAll());
        System.out.println("Сохранено в " + JSON_FILE);
    }

    private void loadJson() throws Exception {
        reloadStorages(JsonFileHandler.readFromJson(JSON_FILE));
    }

    private void reloadStorages(List<Truck> trucks) {
        listStorage.clear();
        mapStorage.clear();
        trucks.forEach(t -> {
            listStorage.add(t);
            mapStorage.add(t);
        });
        System.out.println("Загружено: " + trucks.size());
    }

    private void demoEncryption() throws Exception {
        System.out.print("Строка для MD5: ");
        System.out.println("MD5: " + Encryptor.md5(scanner.nextLine()));

        System.out.print("Ключ AES: ");
        String key = scanner.nextLine();
        System.out.print("Данные для AES: ");
        String data = scanner.nextLine();
        String enc = Encryptor.encryptAes(data, key);
        System.out.println("Зашифровано: " + enc);
        System.out.println("Расшифровано: " + Encryptor.decryptAes(enc, key));
    }

    private void adminLogin() {
        System.out.print("Пароль администратора: ");
        String pwd = scanner.nextLine();
        if (Encryptor.md5(pwd).equals(ADMIN_PASSWORD_HASH)) {
            System.out.println("Доступ разрешён");
        } else {
            System.out.println("Неверный пароль (подсказка: admin)");
        }
    }

    private void archiveData() throws Exception {
        TxtFileHandler.writeToFile(TXT_FILE, listStorage.getAll());
        Archiver.zipFile(TXT_FILE, ZIP_FILE);
        System.out.println("Архив: " + ZIP_FILE);
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }
}