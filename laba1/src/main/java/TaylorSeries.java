package laba1.src.main.java;

public class TaylorSeries {

    public static double computeExpDouble(double x, double epsilon) {
        double sum = 1.0;
        double term = 1.0;
        int n = 1;

        while (Math.abs(term) >= epsilon) {
            term *= x / n;
            sum += term;
            n++;
        }

        return sum;
    }

    public static java.math.BigDecimal computeExpBigDecimal(
            java.math.BigDecimal x,
            java.math.BigDecimal epsilon,
            int precision) {

        java.math.MathContext mc = new java.math.MathContext(precision);
        java.math.BigDecimal sum = java.math.BigDecimal.ONE;
        java.math.BigDecimal term = java.math.BigDecimal.ONE;
        int n = 1;

        while (term.abs().compareTo(epsilon) >= 0) {
            java.math.BigDecimal denominator = java.math.BigDecimal.valueOf(n);
            term = term.multiply(x, mc).divide(denominator, mc);
            sum = sum.add(term, mc);
            n++;
        }

        return sum;
    }
}