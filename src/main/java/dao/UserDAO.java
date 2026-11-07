package dao;

import config.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.User;
import util.PasswordUtil;

public class UserDAO {
    public User authenticate(String username, char[] password) throws SQLException {
        String sql = "SELECT id_user,username,password_hash,role,aktif FROM users WHERE username=? AND aktif=1";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && PasswordUtil.verify(password, rs.getString("password_hash"))) {
                    return new User(rs.getInt("id_user"), rs.getString("username"), rs.getString("role"), rs.getBoolean("aktif"));
                }
            }
        }
        return null;
    }

    public List<User> findAll() throws SQLException {
        List<User> out = new ArrayList<>();
        String sql = "SELECT id_user,username,role,aktif FROM users ORDER BY username";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4)));
        }
        return out;
    }

    public void create(String username, char[] password, String role, boolean aktif) throws SQLException {
        String sql = "INSERT INTO users(username,password_hash,role,aktif) VALUES (?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, PasswordUtil.hash(password));
            ps.setString(3, role);
            ps.setBoolean(4, aktif);
            ps.executeUpdate();
        }
    }

    public boolean updateRoleStatus(int id, String role, boolean aktif) throws SQLException {
        String sql = "UPDATE users SET role=?,aktif=? WHERE id_user=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, role); ps.setBoolean(2, aktif); ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean resetPassword(int id, char[] password) throws SQLException {
        String sql = "UPDATE users SET password_hash=? WHERE id_user=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, PasswordUtil.hash(password)); ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id_user=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        }
    }
}