package dao;

import config.Database;
import java.sql.*;
import java.util.*;
import model.Kursus;
import util.LookupItem;

public class KursusDAO {

    private Kursus map(ResultSet r) throws SQLException {

        return new Kursus(
            r.getInt("id_kursus"),
            r.getString("kode_kursus"),
            r.getString("nama_kursus"),
            r.getString("level"),
            r.getDouble("biaya")
        );
    }

    public boolean insert(Kursus x) throws SQLException {

        String sql = "INSERT INTO kursus "
                + "(kode_kursus,nama_kursus,level,biaya) "
                + "VALUES(?,?,?,?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            bind(p, x);

            return p.executeUpdate() == 1;
        }
    }

    public boolean update(Kursus x) throws SQLException {

        String sql = "UPDATE kursus SET "
                + "kode_kursus=?,nama_kursus=?,"
                + "level=?,biaya=? "
                + "WHERE id_kursus=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            bind(p, x);
            p.setInt(5, x.getIdKursus());

            return p.executeUpdate() > 0;
        }
    }

    private void bind(PreparedStatement p, Kursus x)
            throws SQLException {

        p.setString(1, x.getKode());
        p.setString(2, x.getNama());
        p.setString(3, x.getLevel());
        p.setDouble(4, x.getBiaya());
    }

    public boolean delete(int id) throws SQLException {

        String sql = "DELETE FROM kursus WHERE id_kursus=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);

            return p.executeUpdate() > 0;
        }
    }

    public List<Kursus> findAll() throws SQLException {
        return search("");
    }

    public List<Kursus> search(String q) throws SQLException {

        List<Kursus> out = new ArrayList<>();

        String sql = "SELECT id_kursus,kode_kursus,"
                + "nama_kursus,level,biaya FROM kursus "
                + "WHERE kode_kursus LIKE ? "
                + "OR nama_kursus LIKE ? OR level LIKE ? "
                + "ORDER BY id_kursus DESC";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            String z = "%" + (q == null ? "" : q.trim()) + "%";

            p.setString(1, z);
            p.setString(2, z);
            p.setString(3, z);

            try (ResultSet r = p.executeQuery()) {

                while (r.next()) {
                    out.add(map(r));
                }
            }
        }

        return out;
    }

    public List<LookupItem> lookup() throws SQLException {

        List<LookupItem> out = new ArrayList<>();

        String sql = "SELECT id_kursus,kode_kursus,nama_kursus "
                + "FROM kursus ORDER BY nama_kursus";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {

                out.add(new LookupItem(
                    r.getInt(1),
                    r.getString(2) + " - " + r.getString(3)
                ));
            }
        }

        return out;
    }
}