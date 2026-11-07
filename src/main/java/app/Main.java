package app;

import java.awt.EventQueue;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import util.UserSeeder;
import view.LoginFrame;

public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (Exception ignored) {}

        EventQueue.invokeLater(() -> {
            try {
                UserSeeder.ensureDemoUsers();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Database belum siap atau tabel users belum tersedia.\n"
                        + "Pastikan MySQL XAMPP aktif dan database db_sikursus sudah di-import melalui phpMyAdmin.\n\n"
                        + ex.getMessage(),
                        "SIKURSUS Desktop", JOptionPane.WARNING_MESSAGE);
            }
            new LoginFrame().setVisible(true);
        });
    }
}