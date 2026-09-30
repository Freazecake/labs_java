package test.java;

import main.java.StringProcessor;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringProcessorTest {
    private static class FixedRandom extends Random {
        @Override
        public int nextInt(int bound) {
            return 7;
        }
    }

    private StringProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new StringProcessor(new FixedRandom());
    }

    @Test
    void tokenizeWithOneDelimiterUsesSplit() {
        assertEquals(List.of("101", "abc", "11"), processor.tokenize("101,abc,,11", ","));
    }

    @Test
    void tokenizeWithSeveralDelimitersUsesTokenizer() {
        assertEquals(List.of("101", "abc", "11", "x"), processor.tokenize("101;abc, 11;;x", ";, "));
    }

    @Test
    void validateDelimitersRejectsRepeatsAndBackslash() {
        assertThrows(IllegalArgumentException.class, () -> processor.validateDelimiters("a,a"));
        assertThrows(IllegalArgumentException.class, () -> processor.validateDelimiters("a\\"));
        assertThrows(IllegalArgumentException.class, () -> processor.validateDelimiters(""));
    }

    @Test
    void findsBinaryNumbersAndConvertsThem() {
        List<String> lexemes = List.of("101", "-11", "12", "abc", "0", "1a");
        List<String> numbers = processor.findNumberLexemes(lexemes);
        assertEquals(List.of("101", "-11", "0"), numbers);
        assertArrayEquals(new int[] { 5, -3, 0 }, processor.toNumbers(numbers));
    }

    @Test
    void tooLongBinaryIsNotNumber() {
        String tooLong = "1".repeat(40);
        assertTrue(processor.findNumberLexemes(List.of(tooLong)).isEmpty());
    }

    @Test
    void sortsNumbersByValueDescending() {
        assertEquals(List.of("110", "11", "1"), processor.sortByValueDesc(List.of("1", "110", "11")));
    }

    @Test
    void validTime() {
        assertTrue(processor.isTime("30\\05\\10"));
        assertTrue(processor.isTime("00\\23\\59"));
    }

    @Test
    void invalidTime() {
        assertFalse(processor.isTime("61\\05\\10"));
        assertFalse(processor.isTime("30\\24\\10"));
        assertFalse(processor.isTime("30\\05\\60"));
        assertFalse(processor.isTime("30:05:10"));
        assertFalse(processor.isTime("1\\5\\1"));
    }

    @Test
    void findsTimesOnlyAmongNonNumbers() {
        List<String> lexemes = List.of("101", "30\\05\\10", "abc", "99\\99\\99");
        assertEquals(List.of("30\\05\\10"), processor.findTimes(lexemes));
    }

    @Test
    void formatsTimeAsHoursMinutesSeconds() throws ParseException {
        assertEquals("05:30:10", processor.formatTime("30\\05\\10"));
    }

    @Test
    void insertsNumberAfterTime() {
        String result = processor.insertNumber("a 30\\05\\10 b", List.of("30\\05\\10"), " ", 7);
        assertEquals("a 30\\05\\10 7 b", result);
    }

    @Test
    void insertsNumberInMiddleWhenNoTime() {
        assertEquals("abc7def", processor.insertNumber("abcdef", List.of(), " ", 7));
    }

    @Test
    void findsShortestDigitToLetterSubstring() {
        int[] range = processor.findShortestDigitToLetter("ab12c9d");
        assertArrayEquals(new int[] { 3, 2 }, range);
        assertEquals("ab19d", processor.removeRange("ab12c9d", range));
    }

    @Test
    void noSuchSubstringLeavesLineUnchanged() {
        assertNull(processor.findShortestDigitToLetter("abc 123 !"));
        assertEquals("abc 123 !", processor.removeRange("abc 123 !", null));
    }

    @Test
    void fullProcessing() {
        StringProcessor.Result r = processor.process("101 30\\05\\10 abc 12x", " ");

        assertEquals(List.of("101", "30\\05\\10", "abc", "12x"), r.lexemes());
        assertArrayEquals(new int[] { 5 }, r.numbers());
        assertEquals(List.of("30\\05\\10"), r.times());
        assertEquals(7, r.randomNumber());
        assertEquals("101 30\\05\\10 7 abc 12x", r.lineWithNumber());
        assertEquals("2x", r.removedSubstring());
        assertEquals("101 30\\05\\10 7 abc 1", r.finalLine());
    }

    @Test
    void reportContainsAllResults() throws ParseException {
        String line = "101 30\\05\\10 abc 12x";
        String report = processor.report(line, processor.process(line, " "));
        assertTrue(report.contains("[5]"));
        assertTrue(report.contains("05:30:10"));
        assertTrue(report.contains("Итоговая строка: 101 30\\05\\10 7 abc 1"));
        assertTrue(report.contains("%"));
    }
}