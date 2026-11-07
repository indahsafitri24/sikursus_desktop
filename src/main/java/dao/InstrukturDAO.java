package dao;

import config.Database;
import java.sql.*;
import java.util.*;
import model.Instruktur;
import util.LookupItem;

public class InstrukturDAO {

    private Instruktur map(ResultSet rs) throws SQLException {

        return new Instruktur(
            rs.getInt("id_instruktur"),
            rs.getString("nama"),
            rs.getString("no_hp"),
            rs.getString("keahlian"),
            rs.getString("email")
        );
    }

    public boolean insert(Instruktur x) throws SQLException {

        String sql = "INSERT INTO instruktur "
                + "(nama,keahlian,no_hp,email) "
                + "VALUES(?,?,?,?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            bind(p, x);

            return p.executeUpdate() == 1;
        }
    }

    public boolean update(Instruktur x) throws SQLException {

        String sql = "UPDATE instruktur SET "
                + "nama=?,keahlian=?,no_hp=?,email=? "
                + "WHERE id_instruktur=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            bind(p, x);
            p.setInt(5, x.getId());

            return p.executeUpdate() > 0;
        }
    }

    private void bind(PreparedStatement p, Instruktur x)
            throws SQLException {

        p.setString(1, x.getNama());
        p.setString(2, x.getKeahlian());
        p.setString(3, x.getNoHp());
        p.setString(4, x.getEmail());
    }

    public boolean delete(int id) throws SQLException {

        String sql = "DELETE FROM instruktur "
                + "WHERE id_instruktur=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);

            return p.executeUpdate() > 0;
        }
    }

    public List<Instruktur> findAll() throws SQLException {
        return search("");
    }

    public List<Instruktur> search(String q) throws SQLException {

        List<Instruktur> out = new ArrayList<>();

        String sql = "SELECT id_instruktur,nama,keahlian,"
                + "no_hp,email FROM instruktur "
                + "WHERE nama LIKE ? OR keahlian LIKE ? "
                + "ORDER BY id_instruktur DESC";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            String z = "%" + (q == null ? "" : q.trim()) + "%";

            p.setString(1, z);
            p.setString(2, z);

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

        String sql = "SELECT id_instruktur,nama,keahlian "
                + "FROM instruktur ORDER BY nama";

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