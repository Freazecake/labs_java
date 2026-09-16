package laba1.src.main.java;

import java.util.Scanner;
import java.util.Formatter;

public class Types {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите x: ");
        double x = scanner.nextDouble();

        System.out.print("Введите степень экспоненты: ");
        int k = scanner.nextInt();

        double epsilon = Math.pow(10, -k);

        double taylorResult = TaylorSeries.computeExpDouble(x, epsilon);

        double standardResult = Math.exp(x);

        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);

        long roundedResult = Math.round(taylorResult * 1000);
        formatter.format("Результат (округленный целым): %d%n", roundedResult);
        formatter.format("Восьмеричный вид: %o%n", roundedResult);
        formatter.format("Шестнадцатеричный вид: %x%n", roundedResult);
        formatter.format("Шестнадцатеричный вид (с префиксом): %#x%n", roundedResult);

        formatter.format("%nРезультат ряда Тейлора:%n");
        formatter.format("%15." + (k + 1) + "f%n", taylorResult);
        formatter.format("Стандартное значение (Math.exp):%n");
        formatter.format("%15." + (k + 1) + "f%n", standardResult);

        formatter.format("%nС флагами:%n");
        formatter.format("%+15." + (k + 1) + "f  (флаг +)%n", taylorResult);
        formatter.format("%-15." + (k + 1) + "f  (флаг -)%n", taylorResult);
        formatter.format("%015." + (k + 1) + "f  (флаг 0)%n", Math.abs(taylorResult));
        formatter.format("%#15." + (k + 1) + "f  (флаг #)%n", taylorResult);

        System.out.println(formatter.toString());
        formatter.close();

        System.out.printf("%nSystem.out.printf:%n");
        System.out.printf("%15." + (k + 1) + "f%n", taylorResult);
        System.out.printf("Разница: %" + (k + 3) + "." + (k + 1) + "e%n",
                Math.abs(taylorResult - standardResult));

        scanner.close();
    }
}