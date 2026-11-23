package util;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
        String text = required(value, label);   // kosong -> "... wajib diisi"
        try {
            // LocalDate.parse ketat: 2026-11-31 atau 2026-02-30 langsung ditolak
            return Date.valueOf(LocalDate.parse(text));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    label + " harus berformat yyyy-MM-dd dan tanggalnya valid");
        }
    }
}