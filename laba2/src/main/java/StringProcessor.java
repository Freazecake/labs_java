package main.java;

import java.text.*;
import java.util.*;
import java.util.regex.Pattern;

public class StringProcessor {
    private static final Pattern TIME = Pattern.compile("[0-5]\\d\\\\([01]\\d|2[0-3])\\\\[0-5]\\d");
    private static final Pattern BINARY = Pattern.compile("-?[01]+");
    private static final String TIME_PATTERN = "mm\\HH\\ss";

    private final Random random;

    public StringProcessor(Random random) {
        this.random = random;
    }

    public record Result(List<String> lexemes,
            int[] numbers,
            List<String> sortedNumberLexemes,
            List<String> times,
            int randomNumber,
            String lineWithNumber,
            String removedSubstring,
            String finalLine) {
    }

    public void validateDelimiters(String delimiters) {
        if (delimiters == null || delimiters.isEmpty()) {
            throw new IllegalArgumentException("Строка разделителей пуста");
        }
        for (int i = 0; i < delimiters.length(); i++) {
            char c = delimiters.charAt(i);
            if (c == '\\') {
                throw new IllegalArgumentException("Символ '\\' входит в формат времени и не может быть разделителем");
            }
            if (delimiters.indexOf(c) != delimiters.lastIndexOf(c)) {
                throw new IllegalArgumentException("Разделители повторяются: '" + c + "'");
            }
        }
    }

    public List<String> tokenize(String line, String delimiters) {
        List<String> result = new ArrayList<>();
        if (delimiters.length() == 1) {
            for (String part : line.split(Pattern.quote(delimiters))) {
                if (!part.isEmpty()) {
                    result.add(part);
                }
            }
        } else {
            StringTokenizer st = new StringTokenizer(line, delimiters);
            while (st.hasMoreTokens()) {
                result.add(st.nextToken());
            }
        }
        return result;
    }

    public List<String> findNumberLexemes(List<String> lexemes) {
        List<String> found = new ArrayList<>();
        for (String lexeme : lexemes) {
            if (BINARY.matcher(lexeme).matches()) {
                try {
                    Integer.parseInt(lexeme, 2);
                    found.add(lexeme);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return found;
    }

    public int[] toNumbers(List<String> numberLexemes) {
        int[] numbers = new int[numberLexemes.size()];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(numberLexemes.get(i), 2);
        }
        return numbers;
    }

    public List<String> sortByValueDesc(List<String> numberLexemes) {
        String[] array = numberLexemes.toArray(new String[0]);
        Arrays.sort(array, Comparator.comparingInt((String s) -> Integer.parseInt(s, 2)).reversed());
        return Arrays.asList(array);
    }

    public boolean isTime(String lexeme) {
        if (!TIME.matcher(lexeme).matches()) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat(TIME_PATTERN);
        format.setLenient(false);
        try {
            format.parse(lexeme);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public List<String> findTimes(List<String> lexemes) {
        List<String> numbers = findNumberLexemes(lexemes);
        List<String> times = new ArrayList<>();
        for (String lexeme : lexemes) {
            if (!numbers.contains(lexeme) && isTime(lexeme)) {
                times.add(lexeme);
            }
        }
        return times;
    }

    public String formatTime(String lexeme) throws ParseException {
        Date date = new SimpleDateFormat(TIME_PATTERN).parse(lexeme);
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    public String insertNumber(String line, List<String> times, String delimiters, int value) {
        StringBuilder sb = new StringBuilder(line);
        if (times.isEmpty()) {
            sb.insert(line.length() / 2, value);
        } else {
            String time = times.get(0);
            int position = line.indexOf(time) + time.length();
            sb.insert(position, delimiters.charAt(0) + String.valueOf(value));
        }
        return sb.toString();
    }

    public int[] findShortestDigitToLetter(String line) {
        char[] chars = new char[line.length()];
        line.getChars(0, line.length(), chars, 0);

        int bestStart = -1;
        int bestLength = Integer.MAX_VALUE;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] < '0' || chars[i] > '9') {
                continue;
            }
            for (int j = i + 1; j < chars.length; j++) {
                if (isLatinLetter(line.charAt(j))) {
                    int length = j - i + 1;
                    if (length < bestLength) {
                        bestLength = length;
                        bestStart = i;
                    }
                    break;
                }
            }
        }
        return bestStart < 0 ? null : new int[] { bestStart, bestLength };
    }

    public String removeRange(String line, int[] range) {
        if (range == null) {
            return line;
        }
        return new StringBuilder(line).delete(range[0], range[0] + range[1]).toString();
    }

    private static boolean isLatinLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public Result process(String line, String delimiters) {
        validateDelimiters(delimiters);

        List<String> lexemes = tokenize(line, delimiters);
        List<String> numberLexemes = findNumberLexemes(lexemes);
        int[] numbers = toNumbers(numberLexemes);
        List<String> sorted = sortByValueDesc(numberLexemes);
        List<String> times = findTimes(lexemes);

        int randomNumber = random.nextInt(100);
        String withNumber = insertNumber(line, times, delimiters, randomNumber);

        int[] range = findShortestDigitToLetter(withNumber);
        String removed = range == null ? "" : withNumber.substring(range[0], range[0] + range[1]);
        String finalLine = removeRange(withNumber, range);

        return new Result(lexemes, numbers, sorted, times, randomNumber, withNumber, removed, finalLine);
    }

    public String report(String line, Result r) throws ParseException {
        Locale locale = Locale.forLanguageTag("by-BY");
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("Исходная строка: %s%n", line));
        sb.append(String.format("Лексемы (%d): %s%n", r.lexemes().size(), r.lexemes()));
        sb.append(String.format("Числа 2-й с/с (массив): %s%n", Arrays.toString(r.numbers())));
        sb.append(String.format("Числа, отсортированные по убыванию: %s%n", r.sortedNumberLexemes()));

        if (r.times().isEmpty()) {
            sb.append(String.format("Время ММ\\ЧЧ\\СС: не найдено%n"));
        }
        for (String time : r.times()) {
            String minutes = time.substring(0, time.indexOf('\\'));
            String seconds = time.substring(time.lastIndexOf('\\') + 1);
            sb.append(String.format("Время: %s (ММ=%s, СС=%s, ЧЧ:ММ:СС=%s)%n",
                    time, minutes, seconds, formatTime(time)));
        }

        sb.append(String.format("Случайное число: %d%n", r.randomNumber()));
        sb.append(String.format("Строка после вставки: %s%n", r.lineWithNumber()));
        sb.append(String.format("Удалённая подстрока: %s%n",
                r.removedSubstring().isEmpty() ? "не найдена" : r.removedSubstring()));
        sb.append(String.format("Итоговая строка: %s%n", r.finalLine()));
        sb.append(String.format("Итоговая строка задом наперёд: %s%n",
                new StringBuilder(r.finalLine()).reverse()));

        double share = r.lexemes().isEmpty() ? 0 : (double) r.numbers().length / r.lexemes().size();
        long sum = 0;
        for (int n : r.numbers()) {
            sum += n;
        }
        sb.append(String.format("Доля чисел среди лексем: %s (NumberFormat), %s (DecimalFormat)%n",
                NumberFormat.getPercentInstance(locale).format(share),
                new DecimalFormat("#0.00%").format(share)));
        sb.append(String.format("Сумма чисел как деньги: %s (BY), %s (US)%n",
                NumberFormat.getCurrencyInstance(locale).format(sum),
                NumberFormat.getCurrencyInstance(Locale.US).format(sum)));

        return sb.toString();
    }
}