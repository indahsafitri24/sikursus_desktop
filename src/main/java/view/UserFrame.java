
package view;

import dao.UserDAO;
import model.User;
import util.UserSession;

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
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;

public class UserFrame extends JFrame {

    private final UserDAO dao = new UserDAO();
    private int selected = -1;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JCheckBox chkAktif;
    private JTextField txtCari;

    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnTambah;
    private JButton btnUbah;
    private JButton btnResetPass;
    private JButton btnHapus;
    private JButton btnReset;
    private JButton btnCari;

    private JPanel mainContent;

    // Warna desain
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

    public UserFrame() {

        setTitle("Master Users - SIKURSUS Desktop");

        setSize(1150, 740);
        setMinimumSize(new Dimension(950, 700));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();

        cmbRole = new JComboBox<>(
                new String[]{"ADMIN", "OPERATOR"}
        );

        chkAktif = new JCheckBox("Aktif", true);

        setGlobalFont();

        createHeader();
        createMainContent();
        createForm();
        createDataTable();

        applyFont(getContentPane());

        load();
        clear();
    }

    // Font
    private void setGlobalFont() {

        Font defaultFont = getFont(Font.PLAIN, FONT_NORMAL);

        javax.swing.UIManager.put("defaultFont", defaultFont);
        javax.swing.UIManager.put("Label.font", defaultFont);
        javax.swing.UIManager.put("Button.font", defaultFont);
        javax.swing.UIManager.put("TextField.font", defaultFont);
        javax.swing.UIManager.put("PasswordField.font", defaultFont);
        javax.swing.UIManager.put("ComboBox.font", defaultFont);
        javax.swing.UIManager.put("CheckBox.font", defaultFont);
        javax.swing.UIManager.put("Table.font", defaultFont);
        javax.swing.UIManager.put("TableHeader.font", defaultFont);
        javax.swing.UIManager.put("OptionPane.messageFont", defaultFont);
        javax.swing.UIManager.put("OptionPane.buttonFont", defaultFont);
    }

    private Font getFont(int style, int size) {
        return new Font(FONT_NAME, style, size);
    }

    private void applyFont(Component component) {

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
                applyFont(child);
            }
        }

        if (component instanceof JTable) {

            JTable tbl = (JTable) component;

            tbl.setFont(getFont(Font.PLAIN, FONT_SMALL));
            tbl.setRowHeight(32);

            JTableHeader header = tbl.getTableHeader();

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
                "MANAJEMEN USER & ROLE",
                SwingConstants.CENTER
        );

        title.setFont(getFont(Font.BOLD, FONT_TITLE));
        title.setForeground(CREAM);

        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
    }

    // Main content
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

    // Form user
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

        JLabel formTitle = new JLabel("Form User");

        formTitle.setFont(getFont(Font.BOLD, 16));
        formTitle.setForeground(BROWN_DARK);

        JPanel heading = new JPanel();

        heading.setLayout(
                new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        heading.setBackground(WHITE);
        heading.add(formTitle);
        heading.add(Box.createVerticalStrut(8));

        txtUsername = createTextField();
        txtPassword = new JPasswordField();

        stylePasswordField(txtPassword);

        cmbRole = new JComboBox<>(
                new String[]{"ADMIN", "OPERATOR"}
        );

        cmbRole.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        cmbRole.setBackground(WHITE);
        cmbRole.setForeground(TEXT);
        cmbRole.setPreferredSize(new Dimension(200, 36));
        cmbRole.setBorder(BorderFactory.createLineBorder(BORDER));

        chkAktif = new JCheckBox("Aktif", true);
        chkAktif.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        chkAktif.setForeground(TEXT);
        chkAktif.setBackground(WHITE);
        chkAktif.setFocusPainted(false);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(WHITE);

        addFormRow(inputPanel, "Username", txtUsername, 0);
        addFormRow(inputPanel, "Password / Password Baru", txtPassword, 1);
        addFormRow(inputPanel, "Role", cmbRole, 2);
        addFormRow(inputPanel, "Status Akun", chkAktif, 3);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 5)
        );

        buttonPanel.setBackground(WHITE);

        btnTambah = createButton("TAMBAH", BTN_BLUE);
        btnUbah = createButton("UBAH ROLE/STATUS", BTN_BLUE);
        btnResetPass = createButton("RESET PASSWORD", BTN_GREEN);
        btnHapus = createButton("HAPUS", BTN_RED);
        btnReset = createButton("RESET FORM", BTN_GRAY);

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUbah);
        buttonPanel.add(btnResetPass);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);

        JPanel formBody = new JPanel(
                new BorderLayout(0, 8)
        );

        formBody.setBackground(WHITE);
        formBody.add(inputPanel, BorderLayout.CENTER);
        formBody.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(heading, BorderLayout.NORTH);
        formCard.add(formBody, BorderLayout.CENTER);

        mainContent.add(formCard, BorderLayout.NORTH);

        btnTambah.addActionListener(e -> create());
        btnUbah.addActionListener(e -> update());
        btnResetPass.addActionListener(e -> resetPassword());
        btnHapus.addActionListener(e -> delete());
        btnReset.addActionListener(e -> clear());
    }

    // Baris input
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

    // Text field
    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        field.setForeground(TEXT);
        field.setBackground(WHITE);

        field.setPreferredSize(new Dimension(200, 36));

        field.setCaretColor(BROWN);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(5, 10, 5, 10)
                )
        );

        return field;
    }

    private void stylePasswordField(JPasswordField field) {

        field.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        field.setForeground(TEXT);
        field.setBackground(WHITE);
        field.setCaretColor(BROWN);

        field.setPreferredSize(new Dimension(200, 36));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER, 1),
                        new EmptyBorder(5, 10, 5, 10)
                )
        );
    }

    // Tombol
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

                    backgroundColor = text.equals("HAPUS")
                            ? DISABLED_RED : DISABLED_BROWN;

                    textColor = BROWN_DARK;

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

        button.setFont(getFont(Font.BOLD, FONT_SMALL));
        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        int width = text.equals("UBAH ROLE/STATUS") ? 155
                : text.equals("RESET PASSWORD") ? 150
                : text.equals("RESET FORM") ? 115
                : 100;

        button.setPreferredSize(new Dimension(width, 36));

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

    // Tabel user
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

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(WHITE);

        JLabel dataTitle = new JLabel("Daftar User");

        dataTitle.setFont(getFont(Font.BOLD, 16));
        dataTitle.setForeground(BROWN_DARK);
        dataTitle.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 5)
        );

        searchPanel.setBackground(WHITE);

        JLabel lblCari = new JLabel("Pencarian");

        lblCari.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        lblCari.setForeground(TEXT);

        txtCari = createTextField();
        txtCari.setPreferredSize(new Dimension(220, 34));

        btnCari = createButton("CARI", BTN_BLUE);
        btnCari.setPreferredSize(new Dimension(85, 34));

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
                    "Username",
                    "Role",
                    "Status Akun"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        table.setFont(getFont(Font.PLAIN, FONT_SMALL));
        table.setForeground(TEXT);
        table.setBackground(WHITE);
        table.setRowHeight(32);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);

        table.setSelectionBackground(
                new Color(235, 221, 201)
        );

        table.setSelectionForeground(BROWN_DARK);

        JTableHeader tableHeader = table.getTableHeader();

        tableHeader.setFont(getFont(Font.BOLD, FONT_SMALL));
        tableHeader.setBackground(TABLE_HEADER);
        tableHeader.setForeground(BROWN_DARK);

        tableHeader.setPreferredSize(new Dimension(100, 38));
        tableHeader.setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setFont(getFont(Font.PLAIN, FONT_SMALL));

        table.getColumnModel().getColumn(0)
                .setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(2)
                .setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(3)
                .setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        scroll.getViewport().setBackground(WHITE);

        dataPanel.add(topPanel, BorderLayout.NORTH);
        dataPanel.add(scroll, BorderLayout.CENTER);

        mainContent.add(dataPanel, BorderLayout.CENTER);

        btnCari.addActionListener(e -> searchData());
        txtCari.addActionListener(e -> searchData());

        table.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                pick();
            }
        });
    }

    // Memuat data
    private void load() {

        try {

            DefaultTableModel m = tableModel;

            m.setRowCount(0);

            List<User> data = dao.findAll();

            for (User u : data) {

                m.addRow(new Object[]{
                    u.getIdUser(),
                    u.getUsername(),
                    u.getRole(),
                    u.isAktif() ? "Aktif" : "Nonaktif"
                });
            }

        } catch (SQLException ex) {

            showError("Gagal memuat data user: " + ex.getMessage());
        }
    }

    // Memilih data
    private void pick() {

        int row = table.getSelectedRow();

        if (row < 0) {
            return;
        }

        selected = Integer.parseInt(
                table.getValueAt(row, 0).toString()
        );

        txtUsername.setText(
                table.getValueAt(row, 1).toString()
        );

        txtUsername.setEditable(false);

        cmbRole.setSelectedItem(
                table.getValueAt(row, 2).toString()
        );

        chkAktif.setSelected(
                "Aktif".equals(table.getValueAt(row, 3).toString())
        );
    }

    // Tambah user
    private void create() {

        char[] p = txtPassword.getPassword();

        try {

            if (txtUsername.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("Username wajib diisi");
            }

            dao.create(
                    txtUsername.getText().trim(),
                    p,
                    cmbRole.getSelectedItem().toString(),
                    chkAktif.isSelected()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "User berhasil ditambahkan.",
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
            );

            load();
            clear();

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (SQLException ex) {

            showError("Gagal menambah user: " + ex.getMessage());

        } finally {

            Arrays.fill(p, '\0');
        }
    }

    // Ubah role dan status
    private void update() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih user terlebih dahulu."
            );

            return;
        }

        try {

            dao.updateRoleStatus(
                    selected,
                    cmbRole.getSelectedItem().toString(),
                    chkAktif.isSelected()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Role dan status user berhasil diubah."
            );

            load();
            clear();

        } catch (SQLException ex) {

            showError("Gagal mengubah user: " + ex.getMessage());
        }
    }

    // Reset password
    private void resetPassword() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih user terlebih dahulu."
            );

            return;
        }

        char[] p = txtPassword.getPassword();

        try {

            dao.resetPassword(selected, p);

            JOptionPane.showMessageDialog(
                    this,
                    "Password berhasil direset.",
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtPassword.setText("");

        } catch (Exception ex) {

            showError("Gagal mereset password: " + ex.getMessage());

        } finally {

            Arrays.fill(p, '\0');
        }
    }

    // Hapus user
    private void delete() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih user terlebih dahulu."
            );

            return;
        }

        if (UserSession.getCurrentUser() != null
                && selected == UserSession.getCurrentUser().getIdUser()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Akun yang sedang dipakai tidak boleh dihapus."
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Hapus user terpilih?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            dao.delete(selected);

            JOptionPane.showMessageDialog(
                    this,
                    "User berhasil dihapus."
            );

            load();
            clear();

        } catch (SQLException ex) {

            showError("Gagal menghapus user: " + ex.getMessage());
        }
    }

    // Pencarian
    private void searchData() {

        String keyword = txtCari.getText().trim();

        try {

            if (keyword.isEmpty()) {
                load();
                return;
            }

            DefaultTableModel m = tableModel;
            m.setRowCount(0);

            for (User u : dao.findAll()) {

                if (u.getUsername().toLowerCase().contains(keyword.toLowerCase())
                        || u.getRole().toLowerCase().contains(keyword.toLowerCase())
                        || String.valueOf(u.getIdUser()).contains(keyword)) {

                    m.addRow(new Object[]{
                        u.getIdUser(),
                        u.getUsername(),
                        u.getRole(),
                        u.isAktif() ? "Aktif" : "Nonaktif"
                    });
                }
            }

            clearSelection();

        } catch (SQLException ex) {

            showError("Pencarian gagal: " + ex.getMessage());
        }
    }

    private void clearSelection() {

        selected = -1;
        txtUsername.setText("");
        txtUsername.setEditable(true);
        txtPassword.setText("");
        cmbRole.setSelectedIndex(1);
        chkAktif.setSelected(true);
        table.clearSelection();
    }

    // Reset form
    private void clear() {

        selected = -1;

        txtUsername.setText("");
        txtUsername.setEditable(true);

        txtPassword.setText("");

        cmbRole.setSelectedIndex(1);
        chkAktif.setSelected(true);

        txtCari.setText("");

        table.clearSelection();

        txtUsername.requestFocus();
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Terjadi Kesalahan",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
