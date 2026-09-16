package laba1.src.test.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import laba1.src.main.java.TaylorSeries;

class TaylorSeriesTest {

    @Test
    void computesExponentialForPositiveValue() {
        double actual = TaylorSeries.computeExpDouble(1.0, 1e-12);

        assertEquals(Math.exp(1.0), actual, 1e-11);
    }

    @Test
    void computesExponentialForNegativeValue() {
        double actual = TaylorSeries.computeExpDouble(-1.0, 1e-12);

        assertEquals(Math.exp(-1.0), actual, 1e-11);
    }

    @Test
    void computesExpOfZeroExactly() {
        assertEquals(1.0, TaylorSeries.computeExpDouble(0.0, 1e-12));
    }

    @Test
    void computesBigDecimalExponential() {
        BigDecimal actual = TaylorSeries.computeExpBigDecimal(
                BigDecimal.ONE,
                new BigDecimal("1E-20"),
                40);

        BigDecimal expected = new BigDecimal("2.7182818284590452353602874713526624977572");

        BigDecimal error = actual.subtract(expected).abs();

        assertTrue(error.compareTo(new BigDecimal("1E-20")) <= 0);
    }

    @Test
    void computesBigDecimalExpOfZeroExactly() {
        assertEquals(BigDecimal.ONE, TaylorSeries.computeExpBigDecimal(
                BigDecimal.ZERO,
                new BigDecimal("1E-20"),
                40));
    }
}
