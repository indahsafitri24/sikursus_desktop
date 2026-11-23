package report;

import config.Database;
import java.awt.Component;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public final class ReportService {

    private ReportService() {
    }

    // ===== Praktikum 12 =====
    public static void tampilkanLaporanPeserta() {
        showPeserta(null);
    }

    public static void showPeserta(Component parent) {
        show(parent, "laporan_peserta", new HashMap<String, Object>());
    }

    // ===== Praktikum 13 =====
    public static void cetakPendaftaran(int idPendaftaran) {
        showPendaftaranById(null, idPendaftaran);
    }

    public static void showPendaftaranById(Component parent, int idPendaftaran) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_ID_PENDAFTARAN", idPendaftaran);

        System.out.println("ID pendaftaran dikirim ke report: " + idPendaftaran);

        show(parent, "laporan_pendaftaran", params);
    }

    public static void showPendaftaranSemua(Component parent) {
        show(parent, "laporan_pendaftaran_semua", new HashMap<String, Object>());
    }

    public static void showPembayaranSemua(Component parent) {
        show(parent, "laporan_pembayaran_semua", new HashMap<String, Object>());
    }

    public static void showPembayaranPeriode(Component parent, Date awal, Date akhir) {
        Map<String, Object> params = new HashMap<>();
        params.put("P_TANGGAL_AWAL", awal);
        params.put("P_TANGGAL_AKHIR", akhir);

        System.out.println("Periode pembayaran: " + awal + " s.d. " + akhir);

        show(parent, "laporan_pembayaran", params);
    }

    private static void show(Component parent, String baseName, Map<String, Object> params) {

        try (Connection conn = Database.getConnection()) {

            JasperReport report = loadReport(baseName);

            JasperPrint print = JasperFillManager.fillReport(report, params, conn);

            if (print.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Data laporan tidak ditemukan untuk kriteria yang dipilih."
                );
                return;
            }

            // false = menutup viewer TIDAK menutup seluruh aplikasi
            JasperViewer.viewReport(print, false);

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    parent,
                    "Gagal membuka laporan " + baseName + ":\n" + ex.getMessage(),
                    "JasperReports",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static JasperReport loadReport(String baseName) throws Exception {

        // 1) Muat file .jasper (hasil compile)
        String compiled = "/report/" + baseName + ".jasper";

        try (InputStream in = ReportService.class.getResourceAsStream(compiled)) {
            if (in != null) {
                try {
                    return (JasperReport) JRLoader.loadObject(in);
                } catch (Exception ex) {
                    System.err.println("Gagal memuat " + compiled
                            + " (" + ex.getMessage() + "), compile ulang dari .jrxml");
                }
            }
        }

        // 2) Cadangan: compile langsung dari .jrxml
        String source = "/report/" + baseName + ".jrxml";

        try (InputStream in = ReportService.class.getResourceAsStream(source)) {
            if (in == null) {
                throw new IllegalStateException(
                        "File laporan tidak ditemukan di resource: " + source
                );
            }
            return JasperCompileManager.compileReport(in);
        }
    }

    public static void exportPdf(Component parent, JasperPrint print, String defaultName) {

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File(defaultName));

        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            JasperExportManager.exportReportToPdfFile(
                    print,
                    chooser.getSelectedFile().getAbsolutePath()
            );
            JOptionPane.showMessageDialog(parent, "PDF berhasil disimpan.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Export PDF gagal: " + ex.getMessage(),
                    "JasperReports",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}