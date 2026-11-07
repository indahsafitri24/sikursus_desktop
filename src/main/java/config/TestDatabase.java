package config;

import java.sql.Connection;

public class TestDatabase {
    public static void main(String[] args) {
        try (Connection conn = Database.getConnection()) {
            System.out.println(conn != null && conn.isValid(2)
                    ? "KONEKSI BERHASIL!!"
                    : "Koneksi terbentuk tetapi tidak valid");
        } catch (Exception ex) {
            System.out.println("KONEKSI GAGAL: " + ex.getMessage());
        }
    }
}