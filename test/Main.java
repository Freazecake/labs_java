import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    static class Student {
        String name;
        int year;
        BigDecimal average;

        Student(String n, int y, BigDecimal a) {
            this.name = n;
            this.year = y;
            this.average = a;
        }

        @Override
        public String toString() {
            return String.format("%s, %d grade, average: %s", name, year, average.toString());
        }
    }

    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("students.txt"));
            List<Student> students = new ArrayList<>();

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(";");

                String name = parts[0].trim();
                int year = Integer.parseInt(parts[1].trim());
                BigDecimal average = new BigDecimal(parts[2].trim());

                students.add(new Student(name, year, average));
            }

            Comparator<Student> comp = (s1, s2) -> s2.name.compareToIgnoreCase(s1.name);
            students.sort(comp);

            Comparator<Student> byAgeAsc = Comparator.comparingInt(s -> s.year);
            students.sort(byAgeAsc);

            List<String> output = new ArrayList<>();
            for (Student student : students) {
                output.add(student.toString());
            }

            Files.write(Paths.get("output.txt"), output);
            BufferedWriter bf = new BufferedWriter(new FileWriter("out.txt"));
            for (Student student : students) {
                bf.write(student.toString());
                bf.newLine();
            }
            bf.close();
        } catch (IOException err) {
            System.out.println(err.toString());
        }

    }
}
