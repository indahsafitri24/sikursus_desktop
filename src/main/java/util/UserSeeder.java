package util;

import config.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public final class UserSeeder {
    private UserSeeder() {}

    public static void ensureDemoUsers() throws SQLException {
        ensure("admin_demo", "Demo#2026".toCharArray(), "ADMIN");
        ensure("operator_demo", "Demo#2026".toCharArray(), "OPERATOR");
        ensure("admin", "Admin@123".toCharArray(), "ADMIN");
        ensure("operator", "Operator@123".toCharArray(), "OPERATOR");
    }

    private static void ensure(String username, char[] password, String role) throws SQLException {
        try {
            String check = "SELECT 1 FROM users WHERE username=?";
            try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(check)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return;
                }
            }
            String sql = "INSERT INTO users(username,password_hash,role,aktif) VALUES (?,?,?,1)";
            try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, PasswordUtil.hash(password));
                ps.setString(3, role);
                ps.executeUpdate();
            }
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    public static void main(String[] args) throws Exception {
        ensureDemoUsers();
        System.out.println("User demo siap.");
    }
}