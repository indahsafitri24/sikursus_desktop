
package view;

import dao.KelasDAO;
import dao.PendaftaranDAO;
import dao.PesertaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.Pendaftaran;
import report.ReportService;
import util.LookupItem;
import util.Validation;

public class PendaftaranFrame extends JFrame {

    // ================= WARNA TEMA COKELAT DAN CREAM =================
    private static final Color BG = new Color(247, 242, 234);
    private static final Color CARD = new Color(255, 252, 246);
    private static final Color CREAM = new Color(239, 227, 208);
    private static final Color BORDER = new Color(222, 207, 188);

    private static final Color BROWN = new Color(91, 57, 39);
    private static final Color BROWN_DARK = new Color(84, 52, 35);
    private static final Color TEXT = new Color(75, 48, 33);
    private static final Color WHITE = new Color(255, 255, 255);

    // ================= KOMPONEN =================
    private final JComboBox<LookupItem> peserta = new JComboBox<>();
    private final JComboBox<LookupItem> kelas = new JComboBox<>();

    private final JTextField tanggal =
            new JTextField(ViewUtil.today());

    private final JTextField search = new JTextField(18);

    private final JComboBox<String> status =
            new JComboBox<>(new String[]{
                "DAFTAR", "AKTIF", "SELESAI", "BATAL"
            });

    private final JTable table = new JTable(
            ViewUtil.tableModel(
                    "ID", "NIM", "Peserta", "Kelas",
                    "Kursus", "Tanggal", "Status"
            )
    );

    private final PendaftaranDAO dao = new PendaftaranDAO();

    private int selected = -1;

    public PendaftaranFrame() {

        super("Transaksi Pendaftaran - SIKURSUS Desktop");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ViewUtil.setupFrame(this, 1080, 700);

        build();
        loadLookups();
        load("");
    }

    // ================= MEMBANGUN TAMPILAN =================
    private void build() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BROWN);
        header.setPreferredSize(new Dimension(1080, 85));

        JLabel lblTitle = new JLabel(
                "TRANSAKSI PENDAFTARAN",
                SwingConstants.CENTER
        );

        lblTitle.setFont(new Font("Arial", Font.BOLD, 23));
        lblTitle.setForeground(WHITE);

        header.add(lblTitle, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);

        // ================= BODY =================
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(18, 16, 15, 16));

        // ================= FORM PENDAFTARAN =================
        JPanel formSection = new JPanel(new BorderLayout(0, 10));

        formSection.setBackground(CARD);
        formSection.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1),
                        new EmptyBorder(15, 18, 15, 18)
                )
        );

        formSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblForm = new JLabel("Form Pendaftaran");
        lblForm.setFont(new Font("Arial", Font.BOLD, 15));
        lblForm.setForeground(BROWN_DARK);

        formSection.add(lblForm, BorderLayout.NORTH);

        // ================= INPUT FORM =================
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CARD);

        addFormRow(form, 0, "Peserta", peserta);
        addFormRow(form, 1, "Kelas", kelas);
        addFormRow(form, 2, "Tanggal Daftar", tanggal);
        addFormRow(form, 3, "Status", status);

        formSection.add(form, BorderLayout.CENTER);

        // ================= TOMBOL FORM =================
        JButton simpan = createButton(
                "SIMPAN", BROWN, WHITE
        );

        JButton ubah = createButton(
                "UBAH",
                new Color(205, 181, 158),
                BROWN_DARK
        );

        JButton hapus = createButton(
                "HAPUS",
                new Color(205, 166, 154),
                BROWN_DARK
        );

        JButton reset = createButton(
                "RESET",
                new Color(139, 119, 101),
                WHITE
        );

        JButton semua = createButton(
                "CETAK SEMUA",
                new Color(111, 132, 99),
                WHITE
        );

        JButton terpilih = createButton(
                "CETAK TERPILIH",
                new Color(111, 132, 99),
                WHITE
        );

        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 2)
        );

        actionPanel.setBackground(CARD);

        actionPanel.add(simpan);
        actionPanel.add(ubah);
        actionPanel.add(hapus);
        actionPanel.add(reset);
        actionPanel.add(semua);
        actionPanel.add(terpilih);

        formSection.add(actionPanel, BorderLayout.SOUTH);

        formSection.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 340)
        );

        body.add(formSection);
        body.add(Box.createVerticalStrut(15));

        // ================= DATA PENDAFTARAN =================
        JPanel dataSection = new JPanel(
                new BorderLayout(0, 12)
        );

        dataSection.setBackground(CARD);

        dataSection.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1),
                        new EmptyBorder(15, 18, 15, 18)
                )
        );

        dataSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblData = new JLabel(
                "Data Pendaftaran",
                SwingConstants.LEFT
        );

        lblData.setFont(new Font("Arial", Font.BOLD, 16));
        lblData.setForeground(BROWN_DARK);

        dataSection.add(lblData, BorderLayout.NORTH);

        // ================= PENCARIAN =================
        JButton cari = createButton(
                "CARI", BROWN, WHITE
        );

        JPanel filter = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 3)
        );

        filter.setBackground(CARD);

        JLabel lblCari = createLabel("Pencarian");

        styleInput(search);
        search.setPreferredSize(new Dimension(250, 35));

        filter.add(lblCari);
        filter.add(search);
        filter.add(cari);

        // ================= TABEL =================
        styleTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER, 1));
        scroll.getViewport().setBackground(CARD);

        JPanel tableArea = new JPanel(new BorderLayout(0, 8));
        tableArea.setBackground(CARD);

        tableArea.add(filter, BorderLayout.NORTH);
        tableArea.add(scroll, BorderLayout.CENTER);

        dataSection.add(tableArea, BorderLayout.CENTER);

        body.add(dataSection);

        root.add(body, BorderLayout.CENTER);

        setContentPane(root);

        // ================= EVENT TOMBOL =================
        simpan.addActionListener(e -> save());

        ubah.addActionListener(e -> update());

        hapus.addActionListener(e -> delete());

        reset.addActionListener(e -> clear());

        semua.addActionListener(
                e -> ReportService.showPendaftaranSemua(this)
        );

        terpilih.addActionListener(e -> {
            if (selected < 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pilih baris pendaftaran."
                );
            } else {
                ReportService.showPendaftaranById(this, selected);
            }
        });

        cari.addActionListener(
                e -> load(search.getText())
        );

        search.addActionListener(
                e -> load(search.getText())
        );

        table.getSelectionModel().addListSelectionListener(
                e -> pick()
        );

        getRootPane().setDefaultButton(simpan);
    }

    // ================= BARIS FORM =================
    private void addFormRow(
            JPanel panel,
            int row,
            String label,
            JComponent input
    ) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = row;
        gbc.insets = new Insets(4, 2, 4, 2);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lbl = createLabel(label);

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipadx = 65;

        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0;

        input.setPreferredSize(new Dimension(200, 36));

        if (input instanceof JTextField) {
            styleInput((JTextField) input);
        } else if (input instanceof JComboBox) {
            styleCombo((JComboBox<?>) input);
        }

        panel.add(input, gbc);
    }

    // ================= LABEL =================
    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(TEXT);

        return label;
    }

    // ================= TOMBOL =================
    private JButton createButton(
            String text,
            Color background,
            Color foreground
    ) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(background.darker(), 1),
                        new EmptyBorder(9, 20, 9, 20)
                )
        );

        button.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        button.setOpaque(true);

        return button;
    }

    // ================= STYLE INPUT =================
    private void styleInput(JTextField field) {

        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setForeground(TEXT);
        field.setBackground(CARD);
        field.setCaretColor(BROWN);

        field.setBorder(
                new LineBorder(BORDER, 1)
        );
    }

    // ================= STYLE COMBOBOX =================
    private void styleCombo(JComboBox<?> combo) {

        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setForeground(TEXT);
        combo.setBackground(CARD);
    }

    // ================= STYLE TABEL =================
    private void styleTable() {

        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setBackground(CARD);

        table.setRowHeight(32);

        table.setSelectionBackground(CREAM);
        table.setSelectionForeground(BROWN_DARK);

        table.setGridColor(BORDER);
        table.setShowGrid(true);

        table.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        JTableHeader header = table.getTableHeader();

        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(CREAM);
        header.setForeground(BROWN_DARK);

        header.setPreferredSize(
                new Dimension(100, 38)
        );

        header.setReorderingAllowed(false);
    }

    // ================= MEMUAT PILIHAN =================
    private void loadLookups() {

        try {

            peserta.removeAllItems();

            for (LookupItem x : new PesertaDAO().lookup()) {
                peserta.addItem(x);
            }

            kelas.removeAllItems();

            for (LookupItem x : new KelasDAO().lookup()) {
                kelas.addItem(x);
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                    this,
                    "Memuat pilihan pendaftaran",
                    ex
            );
        }
    }

    // ================= MENGAMBIL DATA FORM =================
    private Pendaftaran data() {

        LookupItem p = (LookupItem) peserta.getSelectedItem();
        LookupItem k = (LookupItem) kelas.getSelectedItem();

        if (p == null || k == null) {
            throw new IllegalArgumentException(
                    "Peserta dan kelas wajib tersedia"
            );
        }

        Pendaftaran x = new Pendaftaran();

        x.setIdPendaftaran(Math.max(selected, 0));
        x.setIdPeserta(p.getId());
        x.setIdKelas(k.getId());

        x.setTanggalDaftar(
                Validation.date(
                        tanggal.getText(),
                        "Tanggal daftar"
                )
        );

        x.setStatus(status.getSelectedItem().toString());

        return x;
    }

    // ================= SIMPAN =================
    private void save() {

        try {

            dao.insert(data());

            load("");
            clear();

        } catch (Exception ex) {

            handle(ex, "Menyimpan pendaftaran");
        }
    }

    // ================= UBAH =================
    private void update() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih pendaftaran."
            );

            return;
        }

        try {

            dao.update(data());

            load("");
            clear();

        } catch (Exception ex) {

            handle(ex, "Mengubah pendaftaran");
        }
    }

    // ================= HAPUS =================
    private void delete() {

        if (selected < 0) {
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this,
                "Hapus pendaftaran? Pembayaran terkait harus dihapus lebih dahulu.",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {

            return;
        }

        try {

            dao.delete(selected);

            load("");
            clear();

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                    this,
                    "Menghapus pendaftaran",
                    ex
            );
        }
    }

    // ================= MEMUAT DATA TABEL =================
    private void load(String q) {

        try {

            List<Pendaftaran> list = dao.search(q);

            DefaultTableModel m =
                    (DefaultTableModel) table.getModel();

            m.setRowCount(0);

            for (Pendaftaran x : list) {

                m.addRow(new Object[]{
                    x.getIdPendaftaran(),
                    x.getNim(),
                    x.getNamaPeserta(),
                    x.getNamaKelas(),
                    x.getNamaKursus(),
                    x.getTanggalDaftar(),
                    x.getStatus()
                });
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                    this,
                    "Memuat pendaftaran",
                    ex
            );
        }
    }

    // ================= MEMILIH DATA TABEL =================
    private void pick() {

        int r = table.getSelectedRow();

        if (r < 0) {
            return;
        }

        selected = Integer.parseInt(
                table.getValueAt(r, 0).toString()
        );

        try {

            for (Pendaftaran x : dao.findAll()) {

                if (x.getIdPendaftaran() == selected) {

                    ViewUtil.selectLookup(
                            peserta,
                            x.getIdPeserta()
                    );

                    ViewUtil.selectLookup(
                            kelas,
                            x.getIdKelas()
                    );

                    tanggal.setText(
                            x.getTanggalDaftar().toString()
                    );

                    status.setSelectedItem(x.getStatus());

                    break;
                }
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                    this,
                    "Membaca pendaftaran",
                    ex
            );
        }
    }

    // ================= RESET FORM =================
    private void clear() {

        selected = -1;

        tanggal.setText(ViewUtil.today());

        status.setSelectedIndex(0);

        table.clearSelection();

        loadLookups();

        peserta.requestFocus();
    }

    // ================= PENANGANAN ERROR =================
    private void handle(Exception ex, String action) {

        if (ex instanceof SQLException) {

            ViewUtil.sqlError(
                    this,
                    action,
                    (SQLException) ex
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );
        }
    }
}
