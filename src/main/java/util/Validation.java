package util;

import java.math.BigDecimal;
import java.sql.Date;

public final class Validation {
    private Validation() {}

    public static String required(String value, String label) {
        String v = value == null ? "" : value.trim();
        if (v.isEmpty()) throw new IllegalArgumentException(label + " wajib diisi");
        return v;
    }

    public static int positiveInt(String value, String label) {
        try {
            int n = Integer.parseInt(required(value, label));
            if (n <= 0) throw new IllegalArgumentException(label + " harus lebih dari 0");
            return n;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " harus berupa angka bulat");
        }
    }

    public static BigDecimal positiveMoney(String value, String label) {
        try {
            BigDecimal n = new BigDecimal(required(value, label).replace(".", "").replace(",", "."));
            if (n.signum() <= 0) throw new IllegalArgumentException(label + " harus lebih dari 0");
            return n;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " harus berupa angka");
        }
    }

    public static Date date(String value, String label) {
        try {
            return Date.valueOf(required(value, label));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(label + " harus berformat yyyy-MM-dd");
        }
    }
}