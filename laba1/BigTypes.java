package laba1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Formatter;

public class BigTypes {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Введите x: ");
        String xStr = reader.readLine();
        BigDecimal x = new BigDecimal(xStr);

        System.out.print("Введите k (натуральное число): ");
        String kStr = reader.readLine();
        int k = Integer.parseInt(kStr);

        BigDecimal epsilon = BigDecimal.ONE.scaleByPowerOfTen(-k);

        int precision = 50;

        BigDecimal taylorResult = TaylorSeries.computeExpBigDecimal(x, epsilon, precision);

        double xDouble = x.doubleValue();
        double standardResult = Math.exp(xDouble);
        BigDecimal standardBD = new BigDecimal(standardResult, new MathContext(precision));

        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);

        BigInteger roundedResult = taylorResult.multiply(BigDecimal.valueOf(1000))
                .setScale(0, BigDecimal.ROUND_HALF_UP)
                .toBigInteger();

        formatter.format("Результат (округленный целым): %d%n", roundedResult);
        formatter.format("Восьмеричный вид: %o%n", roundedResult);
        formatter.format("Шестнадцатеричный вид: %x%n", roundedResult);
        formatter.format("Шестнадцатеричный вид (с префиксом): %#x%n", roundedResult);

        formatter.format("%nРезультат ряда Тейлора (BigDecimal):%n");
        formatter.format("%30." + (k + 1) + "f%n", taylorResult);
        formatter.format("Стандартное значение (double):%n");
        formatter.format("%30." + (k + 1) + "f%n", standardBD);

        formatter.format("%nС флагами:%n");
        formatter.format("%+30." + (k + 1) + "f  (флаг +)%n", taylorResult);
        formatter.format("%-30." + (k + 1) + "f  (флаг -)%n", taylorResult);

        BigDecimal absResult = taylorResult.abs();
        formatter.format("%030." + (k + 1) + "f  (флаг 0)%n", absResult);
        formatter.format("%#30." + (k + 1) + "f  (флаг #)%n", taylorResult);

        System.out.println(formatter.toString());
        formatter.close();

        System.out.printf("%nSystem.out.printf:%n");
        System.out.printf("%30." + (k + 1) + "f%n", taylorResult);

        BigDecimal difference = taylorResult.subtract(standardBD).abs();
        System.out.printf("Разница: %" + (k + 5) + "." + (k + 1) + "f%n", difference);

        reader.close();
    }
}