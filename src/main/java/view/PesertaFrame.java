
package view;

import dao.PesertaDAO;
import model.Peserta;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PesertaFrame extends JFrame {

    private final PesertaDAO dao = new PesertaDAO();
    private int selectedId = -1;

    private JTextField txtNim, txtNama, txtProdi;
    private JTextField txtNoHp, txtEmail, txtCari;

    private JComboBox<String> cmbJenisKelamin;

    private JTable tblPeserta;
    private DefaultTableModel tableModel;

    private JButton btnSimpan, btnUbah, btnHapus;
    private JButton btnReset, btnCetakSemua, btnCari;

    // ================= WARNA =================

    private final Color BG = new Color(250, 246, 238);
    private final Color WHITE = new Color(255, 252, 246);

    private final Color BROWN = new Color(121, 85, 61);
    private final Color BROWN_DARK = new Color(91, 61, 43);
    private final Color BROWN_LIGHT = new Color(157, 119, 91);

    private final Color CREAM = new Color(255, 243, 223);

    private final Color TEXT = new Color(75, 61, 51);
    private final Color MUTED = new Color(130, 112, 96);

    private final Color BORDER = new Color(226, 213, 194);
    private final Color TABLE_HEADER = new Color(238, 225, 205);

    // Warna tombol
    private final Color BTN_BLUE = new Color(145, 105, 76);
    private final Color BTN_RED = new Color(165, 91, 76);
    private final Color BTN_GRAY = new Color(137, 119, 102);
    private final Color BTN_GREEN = new Color(112, 132, 99);

    // Warna tombol ketika nonaktif
    private final Color DISABLED_BROWN = new Color(205, 181, 157);
    private final Color DISABLED_RED = new Color(211, 171, 160);

    private final String FONT_NAME = "Segoe UI";

    private final int FONT_NORMAL = 13;
    private final int FONT_SMALL = 12;
    private final int FONT_TITLE = 22;

    private JPanel mainContent;

    // ================= CONSTRUCTOR =================

    public PesertaFrame() {

        setTitle("Master Data Peserta - SIKURSUS Desktop");

        setSize(1150, 850);
        setMinimumSize(new Dimension(950, 700));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        setGlobalFont();

        createHeader();
        createMainContent();
        createForm();
        createDataTable();

        applyPoppins(getContentPane());

        loadTable();
        clearForm();
    }

    // ================= FONT =================

    private void setGlobalFont() {

        Font defaultFont = new Font(
                FONT_NAME,
                Font.PLAIN,
                FONT_NORMAL
        );

        UIManager.put("defaultFont", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", defaultFont);
        UIManager.put("OptionPane.messageFont", defaultFont);
        UIManager.put("OptionPane.buttonFont", defaultFont);

        // Tidak menggunakan Button.disabledText putih.
        // Warna teks tombol diatur langsung pada komponen tombol.
    }

    private Font getFont(int style, int size) {
        return new Font(FONT_NAME, style, size);
    }

    private void applyPoppins(Component component) {

        Font currentFont = component.getFont();

        int style = Font.PLAIN;
        int size = FONT_NORMAL;

        if (currentFont != null) {
            style = currentFont.getStyle();
            size = currentFont.getSize();
        }

        component.setFont(getFont(style, size));

        if (component instanceof Container) {

            Container container = (Container) component;

            for (Component child : container.getComponents()) {
                applyPoppins(child);
            }
        }

        if (component instanceof JTable) {

            JTable table = (JTable) component;

            table.setFont(getFont(Font.PLAIN, FONT_SMALL));
            table.setRowHeight(32);

            JTableHeader header = table.getTableHeader();

            if (header != null) {
                header.setFont(getFont(Font.BOLD, FONT_SMALL));
            }
        }
    }

    // ================= HEADER =================

    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(BROWN_DARK);
        header.setPreferredSize(new Dimension(100, 85));

        header.setBorder(
                new EmptyBorder(15, 25, 15, 25)
        );

        JLabel title = new JLabel(
                "MASTER DATA PESERTA",
                SwingConstants.CENTER
        );

        title.setFont(getFont(Font.BOLD, FONT_TITLE));
        title.setForeground(CREAM);

        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
    }

    // ================= MAIN CONTENT =================

    private void createMainContent() {

        mainContent = new JPanel(
                new BorderLayout(0, 15)
        );

        mainContent.setBackground(BG);

        mainContent.setBorder(
                new EmptyBorder(18, 22, 22, 22)
        );

        add(mainContent, BorderLayout.CENTER);
    }

    // ================= FORM PESERTA =================

    private void createForm() {

        JPanel formCard = new JPanel(
                new BorderLayout(0, 12)
        );

        formCard.setBackground(WHITE);

        formCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(15, 18, 15, 18)
                )
        );

        JLabel formTitle = new JLabel("Form Peserta");

        formTitle.setFont(getFont(Font.BOLD, 16));
        formTitle.setForeground(BROWN_DARK);

        JPanel heading = new JPanel();

        heading.setLayout(
                new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        heading.setBackground(WHITE);
        heading.add(formTitle);
        heading.add(Box.createVerticalStrut(8));

        // Input form
        txtNim = createTextField();
        txtNama = createTextField();
        txtProdi = createTextField();
        txtNoHp = createTextField();
        txtEmail = createTextField();

        cmbJenisKelamin = new JComboBox<>(
                new String[]{
                    "Pilih Jenis Kelamin",
                    "Laki-laki",
                    "Perempuan"
                }
        );

        cmbJenisKelamin.setFont(
                getFont(Font.PLAIN, FONT_NORMAL)
        );

        cmbJenisKelamin.setBackground(WHITE);
        cmbJenisKelamin.setForeground(TEXT);

        cmbJenisKelamin.setPreferredSize(
                new Dimension(100, 36)
        );

        cmbJenisKelamin.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        // Panel input
        JPanel inputPanel = new JPanel(
                new GridBagLayout()
        );

        inputPanel.setBackground(WHITE);

        addFormRow(inputPanel, "NIM", txtNim, 0);
        addFormRow(inputPanel, "Nama Peserta", txtNama, 1);
        addFormRow(inputPanel, "Jenis Kelamin", cmbJenisKelamin, 2);
        addFormRow(inputPanel, "Program Studi", txtProdi, 3);
        addFormRow(inputPanel, "Nomor HP", txtNoHp, 4);
        addFormRow(inputPanel, "Email", txtEmail, 5);

        // Panel tombol
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 5)
        );

        buttonPanel.setBackground(WHITE);

        btnSimpan = createButton("SIMPAN", BTN_BLUE);
        btnUbah = createButton("UBAH", BTN_BLUE);
        btnHapus = createButton("HAPUS", BTN_RED);
        btnReset = createButton("RESET", BTN_GRAY);
        btnCetakSemua = createButton("CETAK SEMUA", BTN_GREEN);

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnUbah);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnCetakSemua);

        JPanel formBody = new JPanel(
                new BorderLayout(0, 8)
        );

        formBody.setBackground(WHITE);

        formBody.add(inputPanel, BorderLayout.CENTER);
        formBody.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(heading, BorderLayout.NORTH);
        formCard.add(formBody, BorderLayout.CENTER);

        mainContent.add(formCard, BorderLayout.NORTH);

        // Event tombol
        btnSimpan.addActionListener(e -> saveData());
        btnUbah.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> deleteData());

        btnReset.addActionListener(e -> {
            txtCari.setText("");
            loadTable();
            clearForm();
        });

        btnCetakSemua.addActionListener(e -> printData());
    }

    // ================= BARIS INPUT =================

    private void addFormRow(
            JPanel panel,
            String labelText,
            Component input,
            int row
    ) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = row;
        gbc.insets = new Insets(4, 3, 4, 3);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(labelText);

        label.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        label.setForeground(TEXT);

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.ipadx = 5;

        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 0;

        panel.add(input, gbc);
    }

    // ================= TEXT FIELD =================

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        field.setForeground(TEXT);
        field.setBackground(WHITE);

        field.setPreferredSize(
                new Dimension(200, 36)
        );

        field.setCaretColor(BROWN);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(5, 10, 5, 10)
                )
        );

        return field;
    }

    // ================= BUTTON =================

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                Color backgroundColor;
                Color textColor;

                if (!isEnabled()) {

                    if (text.equals("HAPUS")) {
                        backgroundColor = DISABLED_RED;
                    } else {
                        backgroundColor = DISABLED_BROWN;
                    }

                    textColor = new Color(91, 61, 43);

                } else {

                    backgroundColor = getBackground();
                    textColor = Color.WHITE;
                }

                // Menggambar background tombol
                g2.setColor(backgroundColor);

                g2.fillRoundRect(
                        0, 0,
                        getWidth() - 1,
                        getHeight() - 1,
                        8, 8
                );

                // Menggambar garis tepi
                g2.setColor(backgroundColor.darker());

                g2.drawRoundRect(
                        0, 0,
                        getWidth() - 1,
                        getHeight() - 1,
                        8, 8
                );

                // Menggambar teks tombol
                g2.setFont(getFont());

                g2.setColor(textColor);

                java.awt.FontMetrics fm = g2.getFontMetrics();

                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();

                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() - fm.getHeight()) / 2
                        + textHeight;

                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        button.setFont(
                getFont(Font.BOLD, FONT_SMALL)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setPreferredSize(
                new Dimension(
                        text.equals("CETAK SEMUA") ? 135 : 105,
                        36
                )
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {

                if (button.isEnabled()) {
                    button.setBackground(color.brighter());
                    button.repaint();
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {

                button.setBackground(color);
                button.repaint();
            }
        });

        return button;
    }

    // ================= TABEL DATA PESERTA =================

    private void createDataTable() {

        JPanel dataPanel = new JPanel(
                new BorderLayout(0, 12)
        );

        dataPanel.setBackground(WHITE);

        dataPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(15, 18, 18, 18)
                )
        );

        JPanel topPanel = new JPanel(
                new BorderLayout()
        );

        topPanel.setBackground(WHITE);

        JLabel dataTitle = new JLabel("Data Peserta");

        dataTitle.setFont(getFont(Font.BOLD, 16));
        dataTitle.setForeground(BROWN_DARK);

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 5)
        );

        searchPanel.setBackground(WHITE);

        JLabel lblCari = new JLabel("Pencarian");

        lblCari.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        lblCari.setForeground(TEXT);

        txtCari = createTextField();

        txtCari.setPreferredSize(
                new Dimension(220, 34)
        );

        btnCari = createButton("CARI", BTN_BLUE);

        btnCari.setPreferredSize(
                new Dimension(85, 34)
        );

        searchPanel.add(lblCari);
        searchPanel.add(txtCari);
        searchPanel.add(btnCari);

        JPanel topContent = new JPanel();

        topContent.setLayout(
                new BoxLayout(topContent, BoxLayout.Y_AXIS)
        );

        topContent.setBackground(WHITE);

        topContent.add(dataTitle);
        topContent.add(Box.createVerticalStrut(8));
        topContent.add(searchPanel);

        topPanel.add(topContent, BorderLayout.CENTER);

        // Model tabel
        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "NIM",
                    "Nama",
                    "JK",
                    "Prodi",
                    "No. HP",
                    "Email"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPeserta = new JTable(tableModel);

        tblPeserta.setFont(
                getFont(Font.PLAIN, FONT_SMALL)
        );

        tblPeserta.setForeground(TEXT);
        tblPeserta.setBackground(WHITE);
        tblPeserta.setRowHeight(32);

        tblPeserta.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tblPeserta.setGridColor(BORDER);
        tblPeserta.setShowVerticalLines(false);
        tblPeserta.setShowHorizontalLines(true);
        tblPeserta.setFillsViewportHeight(true);

        tblPeserta.setSelectionBackground(
                new Color(235, 221, 201)
        );

        tblPeserta.setSelectionForeground(BROWN_DARK);

        // Header tabel
        JTableHeader tableHeader = tblPeserta.getTableHeader();

        tableHeader.setFont(
                getFont(Font.BOLD, FONT_SMALL)
        );

        tableHeader.setBackground(TABLE_HEADER);
        tableHeader.setForeground(BROWN_DARK);

        tableHeader.setPreferredSize(
                new Dimension(100, 38)
        );

        tableHeader.setReorderingAllowed(false);

        // Lebar kolom
        tblPeserta.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblPeserta.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblPeserta.getColumnModel().getColumn(2).setPreferredWidth(170);
        tblPeserta.getColumnModel().getColumn(3).setPreferredWidth(55);
        tblPeserta.getColumnModel().getColumn(4).setPreferredWidth(150);
        tblPeserta.getColumnModel().getColumn(5).setPreferredWidth(130);
        tblPeserta.getColumnModel().getColumn(6).setPreferredWidth(200);

        // Rata tengah ID dan JK
        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        centerRenderer.setFont(
                getFont(Font.PLAIN, FONT_SMALL)
        );

        tblPeserta.getColumnModel().getColumn(0)
                .setCellRenderer(centerRenderer);

        tblPeserta.getColumnModel().getColumn(3)
                .setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(tblPeserta);

        scroll.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        scroll.getViewport().setBackground(WHITE);

        dataPanel.add(topPanel, BorderLayout.NORTH);
        dataPanel.add(scroll, BorderLayout.CENTER);

        mainContent.add(dataPanel, BorderLayout.CENTER);

        // Event pencarian
        btnCari.addActionListener(e -> searchData());
        txtCari.addActionListener(e -> searchData());

        // Klik baris tabel
        tblPeserta.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectRow();
            }
        });
    }

    // ================= MEMUAT DATA =================

    private void loadTable() {

        try {

            List<Peserta> data = dao.findAll();
            fillTable(data);

        } catch (SQLException ex) {

            showError(
                    "Gagal memuat data: " + ex.getMessage()
            );
        }
    }

    // ================= MENGISI TABEL =================

    private void fillTable(List<Peserta> data) {

        tableModel.setRowCount(0);

        for (Peserta p : data) {

            tableModel.addRow(new Object[]{
                p.getId(),
                p.getNim(),
                p.getNama(),
                p.getJenisKelamin(),
                p.getProdi(),
                p.getNoHp(),
                p.getEmail()
            });
        }
    }

    // ================= PILIH DATA =================

    private void selectRow() {

        int row = tblPeserta.getSelectedRow();

        if (row == -1) {
            return;
        }

        selectedId = Integer.parseInt(
                tblPeserta.getValueAt(row, 0).toString()
        );

        txtNim.setText(
                tblPeserta.getValueAt(row, 1).toString()
        );

        txtNama.setText(
                tblPeserta.getValueAt(row, 2).toString()
        );

        String jk = tblPeserta.getValueAt(row, 3).toString();

        if ("L".equalsIgnoreCase(jk)) {
            cmbJenisKelamin.setSelectedItem("Laki-laki");

        } else if ("P".equalsIgnoreCase(jk)) {
            cmbJenisKelamin.setSelectedItem("Perempuan");

        } else {
            cmbJenisKelamin.setSelectedIndex(0);
        }

        txtProdi.setText(
                tblPeserta.getValueAt(row, 4).toString()
        );

        txtNoHp.setText(
                tblPeserta.getValueAt(row, 5).toString()
        );

        txtEmail.setText(
                tblPeserta.getValueAt(row, 6).toString()
        );

        btnSimpan.setEnabled(false);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);

        btnUbah.repaint();
        btnHapus.repaint();
        btnSimpan.repaint();
    }

    // ================= VALIDASI FORM =================

    private boolean validateForm() {

        if (txtNim.getText().trim().isEmpty()
                || txtNama.getText().trim().isEmpty()
                || txtProdi.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "NIM, Nama, dan Program Studi wajib diisi.",
                    "Validasi Form",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        if (cmbJenisKelamin.getSelectedIndex() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Silakan pilih jenis kelamin.",
                    "Validasi Form",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    // ================= JENIS KELAMIN =================

    private String getJenisKelamin() {

        String pilihan =
                cmbJenisKelamin.getSelectedItem().toString();

        if (pilihan.equals("Laki-laki")) {
            return "L";
        }

        if (pilihan.equals("Perempuan")) {
            return "P";
        }

        return null;
    }

    // ================= MENGAMBIL DATA FORM =================

    private Peserta getFormData() {

        return new Peserta(
                txtNim.getText().trim(),
                txtNama.getText().trim(),
                getJenisKelamin(),
                txtProdi.getText().trim(),
                txtNoHp.getText().trim(),
                txtEmail.getText().trim()
        );
    }

    // ================= SIMPAN DATA =================

    private void saveData() {

        if (!validateForm()) {
            return;
        }

        try {

            Peserta p = getFormData();

            if (dao.insert(p)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta berhasil disimpan.",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE
                );

                refreshData();
            }

        } catch (IllegalArgumentException | SQLException ex) {

            showError(
                    "Gagal menyimpan data: " + ex.getMessage()
            );
        }
    }

    // ================= UBAH DATA =================

    private void updateData() {

        if (selectedId <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih peserta pada tabel terlebih dahulu."
            );

            return;
        }

        if (!validateForm()) {
            return;
        }

        try {

            Peserta p = getFormData();
            p.setId(selectedId);

            if (dao.update(p)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta berhasil diubah.",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE
                );

                refreshData();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Data tidak ditemukan atau tidak berubah."
                );
            }

        } catch (IllegalArgumentException | SQLException ex) {

            showError(
                    "Gagal mengubah data: " + ex.getMessage()
            );
        }
    }

    // ================= HAPUS DATA =================

    private void deleteData() {

        if (selectedId <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data peserta terlebih dahulu."
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus peserta "
                + txtNama.getText() + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            if (dao.delete(selectedId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta berhasil dihapus."
                );

                refreshData();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta tidak ditemukan."
                );
            }

        } catch (SQLException ex) {

            showError(
                    "Gagal menghapus data: " + ex.getMessage()
            );
        }
    }

    // ================= PENCARIAN DATA =================

    private void searchData() {

        String keyword = txtCari.getText().trim();

        try {

            if (keyword.isEmpty()) {
                loadTable();

            } else {
                fillTable(dao.search(keyword));
            }

            clearForm();

        } catch (SQLException ex) {

            showError(
                    "Pencarian gagal: " + ex.getMessage()
            );
        }
    }

    // ================= RESET FORM =================

    private void clearForm() {

        selectedId = -1;

        txtNim.setText("");
        txtNama.setText("");
        txtProdi.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");

        cmbJenisKelamin.setSelectedIndex(0);

        tblPeserta.clearSelection();

        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);

        btnSimpan.repaint();
        btnUbah.repaint();
        btnHapus.repaint();
    }

    // ================= REFRESH DATA =================

    private void refreshData() {

        txtCari.setText("");

        loadTable();
        clearForm();
    }

    // ================= CETAK DATA =================

    private void printData() {

        try {

            boolean berhasil = tblPeserta.print();

            if (berhasil) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta berhasil dicetak.",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (java.awt.print.PrinterException ex) {

            showError(
                    "Gagal mencetak: " + ex.getMessage()
            );
        }
    }

    // ================= PESAN ERROR =================

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Terjadi Kesalahan",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new PesertaFrame().setVisible(true);
        });
    }
}
