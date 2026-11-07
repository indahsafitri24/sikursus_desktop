
package view;

import dao.InstrukturDAO;
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
import model.Instruktur;

public class InstrukturFrame extends JFrame {

    private final InstrukturDAO dao = new InstrukturDAO();
    private int selectedId = -1;

    private JTextField txtNama, txtKeahlian;
    private JTextField txtNoHp, txtEmail, txtCari;

    private JTable tblInstruktur;
    private DefaultTableModel tableModel;

    private JButton btnSimpan, btnUbah, btnHapus;
    private JButton btnReset, btnCetakSemua, btnCari;

    // Warna
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

    private final Color BTN_BLUE = new Color(145, 105, 76);
    private final Color BTN_RED = new Color(165, 91, 76);
    private final Color BTN_GRAY = new Color(137, 119, 102);
    private final Color BTN_GREEN = new Color(112, 132, 99);

    private final Color DISABLED_BROWN = new Color(205, 181, 157);
    private final Color DISABLED_RED = new Color(211, 171, 160);

    private final String FONT_NAME = "Segoe UI";

    private final int FONT_NORMAL = 13;
    private final int FONT_SMALL = 12;
    private final int FONT_TITLE = 22;

    private JPanel mainContent;

    // Constructor
    public InstrukturFrame() {

        setTitle("Master Data Instruktur - SIKURSUS Desktop");

        setSize(1150, 740);
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

    // Font
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
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", defaultFont);
        UIManager.put("OptionPane.messageFont", defaultFont);
        UIManager.put("OptionPane.buttonFont", defaultFont);
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

    // Header
    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(BROWN_DARK);
        header.setPreferredSize(new Dimension(100, 85));

        header.setBorder(
                new EmptyBorder(15, 25, 15, 25)
        );

        JLabel title = new JLabel(
                "MASTER DATA INSTRUKTUR",
                SwingConstants.CENTER
        );

        title.setFont(getFont(Font.BOLD, FONT_TITLE));
        title.setForeground(CREAM);

        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
    }

    // Main Content
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

    // Form Instruktur
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

        JLabel formTitle = new JLabel("Form Instruktur");

        formTitle.setFont(getFont(Font.BOLD, 16));
        formTitle.setForeground(BROWN_DARK);

        JPanel heading = new JPanel();

        heading.setLayout(
                new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        heading.setBackground(WHITE);
        heading.add(formTitle);
        heading.add(Box.createVerticalStrut(8));

        txtNama = createTextField();
        txtKeahlian = createTextField();
        txtNoHp = createTextField();
        txtEmail = createTextField();

        JPanel inputPanel = new JPanel(
                new GridBagLayout()
        );

        inputPanel.setBackground(WHITE);

        addFormRow(inputPanel, "Nama Instruktur", txtNama, 0);
        addFormRow(inputPanel, "Keahlian", txtKeahlian, 1);
        addFormRow(inputPanel, "Nomor HP", txtNoHp, 2);
        addFormRow(inputPanel, "Email", txtEmail, 3);

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

    // Baris Input
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

    // Text Field
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

    // Button
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

                g2.setColor(backgroundColor);

                g2.fillRoundRect(
                        0, 0,
                        getWidth() - 1,
                        getHeight() - 1,
                        8, 8
                );

                g2.setColor(backgroundColor.darker());

                g2.drawRoundRect(
                        0, 0,
                        getWidth() - 1,
                        getHeight() - 1,
                        8, 8
                );

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

    // Tabel Data Instruktur
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

        JLabel dataTitle = new JLabel("Data Instruktur");

        dataTitle.setFont(getFont(Font.BOLD, 16));
        dataTitle.setForeground(BROWN_DARK);
        dataTitle.setHorizontalAlignment(SwingConstants.LEFT);
        dataTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        dataTitle.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        dataTitle.getPreferredSize().height
                )
        );

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

        JPanel topContent = new JPanel(
                new BorderLayout(0, 8)
        );

        topContent.setBackground(WHITE);

        topContent.add(dataTitle, BorderLayout.WEST);
        topContent.add(searchPanel, BorderLayout.SOUTH);

        topPanel.add(topContent, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Nama",
                    "Keahlian",
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

        tblInstruktur = new JTable(tableModel);

        tblInstruktur.setFont(
                getFont(Font.PLAIN, FONT_SMALL)
        );

        tblInstruktur.setForeground(TEXT);
        tblInstruktur.setBackground(WHITE);
        tblInstruktur.setRowHeight(32);

        tblInstruktur.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tblInstruktur.setGridColor(BORDER);
        tblInstruktur.setShowVerticalLines(false);
        tblInstruktur.setShowHorizontalLines(true);
        tblInstruktur.setFillsViewportHeight(true);

        tblInstruktur.setSelectionBackground(
                new Color(235, 221, 201)
        );

        tblInstruktur.setSelectionForeground(BROWN_DARK);

        JTableHeader tableHeader = tblInstruktur.getTableHeader();

        tableHeader.setFont(
                getFont(Font.BOLD, FONT_SMALL)
        );

        tableHeader.setBackground(TABLE_HEADER);
        tableHeader.setForeground(BROWN_DARK);

        tableHeader.setPreferredSize(
                new Dimension(100, 38)
        );

        tableHeader.setReorderingAllowed(false);

        tblInstruktur.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblInstruktur.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblInstruktur.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblInstruktur.getColumnModel().getColumn(3).setPreferredWidth(140);
        tblInstruktur.getColumnModel().getColumn(4).setPreferredWidth(220);

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        centerRenderer.setFont(
                getFont(Font.PLAIN, FONT_SMALL)
        );

        tblInstruktur.getColumnModel().getColumn(0)
                .setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(tblInstruktur);

        scroll.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        scroll.getViewport().setBackground(WHITE);

        dataPanel.add(topPanel, BorderLayout.NORTH);
        dataPanel.add(scroll, BorderLayout.CENTER);

        mainContent.add(dataPanel, BorderLayout.CENTER);

        btnCari.addActionListener(e -> searchData());
        txtCari.addActionListener(e -> searchData());

        tblInstruktur.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectRow();
            }
        });
    }

    // Memuat Data
    private void loadTable() {

        try {

            List<Instruktur> data = dao.search("");
            fillTable(data);

        } catch (SQLException ex) {

            showError(
                    "Gagal memuat data: " + ex.getMessage()
            );
        }
    }

    // Mengisi Tabel
    private void fillTable(List<Instruktur> data) {

        tableModel.setRowCount(0);

        for (Instruktur x : data) {

            tableModel.addRow(new Object[]{
                x.getId(),
                x.getNama(),
                x.getKeahlian(),
                x.getNoHp(),
                x.getEmail()
            });
        }
    }

    // Memilih Data
    private void selectRow() {

        int row = tblInstruktur.getSelectedRow();

        if (row == -1) {
            return;
        }

        selectedId = Integer.parseInt(
                tblInstruktur.getValueAt(row, 0).toString()
        );

        txtNama.setText(
                tblInstruktur.getValueAt(row, 1).toString()
        );

        txtKeahlian.setText(
                tblInstruktur.getValueAt(row, 2).toString()
        );

        txtNoHp.setText(
                String.valueOf(
                        tblInstruktur.getValueAt(row, 3)
                )
        );

        txtEmail.setText(
                String.valueOf(
                        tblInstruktur.getValueAt(row, 4)
                )
        );

        btnSimpan.setEnabled(false);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);

        btnUbah.repaint();
        btnHapus.repaint();
        btnSimpan.repaint();
    }

    // Validasi Form
    private boolean validateForm() {

        if (txtNama.getText().trim().isEmpty()
                || txtKeahlian.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Nama dan Keahlian instruktur wajib diisi.",
                    "Validasi Form",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    // Mengambil Data Form
    private Instruktur getFormData() {

        Instruktur x = new Instruktur();

        x.setId(Math.max(selectedId, 0));
        x.setNama(txtNama.getText().trim());
        x.setKeahlian(txtKeahlian.getText().trim());
        x.setNoHp(txtNoHp.getText().trim());
        x.setEmail(txtEmail.getText().trim());

        return x;
    }

    // Simpan Data
    private void saveData() {

        if (!validateForm()) {
            return;
        }

        try {

            if (dao.insert(getFormData())) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data instruktur berhasil disimpan.",
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

    // Ubah Data
    private void updateData() {

        if (selectedId <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih instruktur pada tabel terlebih dahulu."
            );

            return;
        }

        if (!validateForm()) {
            return;
        }

        try {

            if (dao.update(getFormData())) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data instruktur berhasil diubah.",
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

    // Hapus Data
    private void deleteData() {

        if (selectedId <= 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data instruktur terlebih dahulu."
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus instruktur "
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
                        "Data instruktur berhasil dihapus."
                );

                refreshData();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Data instruktur tidak ditemukan."
                );
            }

        } catch (SQLException ex) {

            showError(
                    "Gagal menghapus data: " + ex.getMessage()
            );
        }
    }

    // Pencarian Data
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

    // Reset Form
    private void clearForm() {

        selectedId = -1;

        txtNama.setText("");
        txtKeahlian.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");

        tblInstruktur.clearSelection();

        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);

        btnSimpan.repaint();
        btnUbah.repaint();
        btnHapus.repaint();
    }

    // Refresh Data
    private void refreshData() {

        txtCari.setText("");

        loadTable();
        clearForm();
    }

    // Cetak Data
    private void printData() {

        try {

            boolean berhasil = tblInstruktur.print();

            if (berhasil) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data instruktur berhasil dicetak.",
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

    // Pesan Error
    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Terjadi Kesalahan",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // Main
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new InstrukturFrame().setVisible(true);
        });
    }
}
