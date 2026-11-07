
package view;

import dao.KursusDAO;
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
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import model.Kursus;

public class KursusFrame extends JFrame {

    private final KursusDAO dao = new KursusDAO();
    private int selected = -1;

    private JTextField kode;
    private JTextField nama;
    private JTextField biaya;
    private JTextField search;

    private JComboBox<String> level;

    private JTable table;
    private DefaultTableModel tableModel;

    private JButton simpan;
    private JButton ubah;
    private JButton hapus;
    private JButton reset;
    private JButton cari;

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

    public KursusFrame() {

        setTitle("Master Data Kursus - SIKURSUS Desktop");

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

        applyFont(getContentPane());

        load("");
        clearForm();
    }

    // FONT
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
    }

    private Font getFont(int style, int size) {
        return new Font(FONT_NAME, style, size);
    }

    private void applyFont(Component component) {

        Font current = component.getFont();

        int style = current == null ? Font.PLAIN : current.getStyle();
        int size = current == null ? FONT_NORMAL : current.getSize();

        component.setFont(getFont(style, size));

        if (component instanceof Container) {

            for (Component child : ((Container) component).getComponents()) {
                applyFont(child);
            }
        }

        if (component instanceof JTable) {

            JTable tbl = (JTable) component;

            tbl.setFont(getFont(Font.PLAIN, FONT_SMALL));
            tbl.setRowHeight(32);

            if (tbl.getTableHeader() != null) {
                tbl.getTableHeader().setFont(
                        getFont(Font.BOLD, FONT_SMALL)
                );
            }
        }
    }

    // HEADER
    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(BROWN_DARK);
        header.setPreferredSize(new Dimension(100, 85));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel title = new JLabel(
                "MASTER DATA KURSUS",
                SwingConstants.CENTER
        );

        title.setFont(getFont(Font.BOLD, FONT_TITLE));
        title.setForeground(CREAM);

        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
    }

    // MAIN CONTENT
    private void createMainContent() {

        mainContent = new JPanel(new BorderLayout(0, 15));

        mainContent.setBackground(BG);
        mainContent.setBorder(
                new EmptyBorder(18, 22, 22, 22)
        );

        add(mainContent, BorderLayout.CENTER);
    }

    // FORM KURSUS
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

        JLabel formTitle = new JLabel("Form Kursus");

        formTitle.setFont(getFont(Font.BOLD, 16));
        formTitle.setForeground(BROWN_DARK);

        JPanel heading = new JPanel();

        heading.setLayout(
                new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        heading.setBackground(WHITE);
        heading.add(formTitle);
        heading.add(Box.createVerticalStrut(8));

        kode = createTextField();
        nama = createTextField();
        biaya = createTextField();

        level = new JComboBox<>(
                new String[]{
                    "BASIC",
                    "INTERMEDIATE",
                    "ADVANCED"
                }
        );

        styleComboBox();

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(WHITE);

        addFormRow(inputPanel, "Kode Kursus", kode, 0);
        addFormRow(inputPanel, "Nama Kursus", nama, 1);
        addFormRow(inputPanel, "Level", level, 2);
        addFormRow(inputPanel, "Biaya", biaya, 3);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 12, 5)
        );

        buttonPanel.setBackground(WHITE);

        simpan = createButton("SIMPAN", BTN_BLUE);
        ubah = createButton("UBAH", BTN_BLUE);
        hapus = createButton("HAPUS", BTN_RED);
        reset = createButton("RESET", BTN_GRAY);

        buttonPanel.add(simpan);
        buttonPanel.add(ubah);
        buttonPanel.add(hapus);
        buttonPanel.add(reset);

        JPanel formBody = new JPanel(new BorderLayout(0, 8));
        formBody.setBackground(WHITE);

        formBody.add(inputPanel, BorderLayout.CENTER);
        formBody.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(heading, BorderLayout.NORTH);
        formCard.add(formBody, BorderLayout.CENTER);

        mainContent.add(formCard, BorderLayout.NORTH);

        simpan.addActionListener(e -> save());
        ubah.addActionListener(e -> update());
        hapus.addActionListener(e -> delete());

        reset.addActionListener(e -> {
            search.setText("");
            load("");
            clearForm();
        });

        getRootPane().setDefaultButton(simpan);

        ubah.setEnabled(false);
        hapus.setEnabled(false);
    }

    // BARIS INPUT
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

    // TEXT FIELD
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

    // COMBO BOX
    private void styleComboBox() {

        level.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        level.setBackground(WHITE);
        level.setForeground(TEXT);

        level.setPreferredSize(new Dimension(200, 36));
        level.setBorder(BorderFactory.createLineBorder(BORDER));

        level.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus
                );

                label.setBorder(new EmptyBorder(0, 8, 0, 0));

                return label;
            }
        });
    }

    // BUTTON
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

                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

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
        button.setPreferredSize(new Dimension(105, 36));

        button.addMouseListener(new java.awt.event.MouseAdapter() {

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

    // TABEL DATA KURSUS
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

        JLabel dataTitle = new JLabel("Data Kursus");

        dataTitle.setFont(getFont(Font.BOLD, 16));
        dataTitle.setForeground(BROWN_DARK);

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 5)
        );

        searchPanel.setBackground(WHITE);

        JLabel lblCari = new JLabel("Pencarian");

        lblCari.setFont(getFont(Font.PLAIN, FONT_NORMAL));
        lblCari.setForeground(TEXT);

        search = createTextField();
        search.setPreferredSize(new Dimension(220, 34));

        cari = createButton("CARI", BTN_BLUE);
        cari.setPreferredSize(new Dimension(85, 34));

        searchPanel.add(lblCari);
        searchPanel.add(search);
        searchPanel.add(cari);

        JPanel topContent = new JPanel(
                new BorderLayout(0, 8)
        );

        topContent.setBackground(WHITE);
        topContent.add(dataTitle, BorderLayout.WEST);
        topContent.add(searchPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(WHITE);
        topPanel.add(topContent, BorderLayout.CENTER);

        tableModel = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Kode",
                    "Nama Kursus",
                    "Level",
                    "Biaya"
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

        table.setSelectionBackground(new Color(235, 221, 201));
        table.setSelectionForeground(BROWN_DARK);

        JTableHeader tableHeader = table.getTableHeader();

        tableHeader.setFont(getFont(Font.BOLD, FONT_SMALL));
        tableHeader.setBackground(TABLE_HEADER);
        tableHeader.setForeground(BROWN_DARK);
        tableHeader.setPreferredSize(new Dimension(100, 38));
        tableHeader.setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(220);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);

        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setFont(getFont(Font.PLAIN, FONT_SMALL));

        table.getColumnModel().getColumn(0)
                .setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(3)
                .setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(WHITE);

        dataPanel.add(topPanel, BorderLayout.NORTH);
        dataPanel.add(scroll, BorderLayout.CENTER);

        mainContent.add(dataPanel, BorderLayout.CENTER);

        cari.addActionListener(e -> load(search.getText()));
        search.addActionListener(e -> load(search.getText()));

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                pick();
            }
        });
    }

    // DATA KURSUS
    private Kursus data() {

        double n;

        try {

            n = Double.parseDouble(
                    biaya.getText().trim().replace(",", "")
            );

        } catch (Exception ex) {

            throw new IllegalArgumentException(
                    "Biaya harus berupa angka"
            );
        }

        return new Kursus(
                Math.max(selected, 0),
                kode.getText(),
                nama.getText(),
                level.getSelectedItem().toString(),
                n
        );
    }

    // SIMPAN DATA
    private void save() {

        try {

            boolean berhasil = dao.insert(data());

            if (berhasil) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data kursus berhasil disimpan.",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE
                );

                load("");
                clearForm();
            }

        } catch (Exception ex) {

            handle(ex, "Menyimpan kursus");
        }
    }

    // UBAH DATA
    private void update() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih kursus terlebih dahulu."
            );

            return;
        }

        try {

            boolean berhasil = dao.update(data());

            if (berhasil) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data kursus berhasil diubah.",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE
                );

                load("");
                clearForm();
            }

        } catch (Exception ex) {

            handle(ex, "Mengubah kursus");
        }
    }

    // HAPUS DATA
    private void delete() {

        if (selected < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih kursus terlebih dahulu."
            );

            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus kursus "
                + nama.getText() + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            if (dao.delete(selected)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data kursus berhasil dihapus."
                );

                load("");
                clearForm();
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                    this,
                    "Menghapus kursus",
                    ex
            );
        }
    }

    // MEMUAT DATA
    private void load(String q) {

        try {

            List<Kursus> list = dao.search(q);

            tableModel.setRowCount(0);

            for (Kursus x : list) {

                tableModel.addRow(new Object[]{
                    x.getIdKursus(),
                    x.getKode(),
                    x.getNama(),
                    x.getLevel(),
                    String.format("%,.0f", x.getBiaya())
                });
            }

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                    this,
                    "Memuat kursus",
                    ex
            );
        }
    }

    // PILIH DATA
    private void pick() {

        int r = table.getSelectedRow();

        if (r < 0) {
            return;
        }

        selected = Integer.parseInt(
                table.getValueAt(r, 0).toString()
        );

        kode.setText(
                table.getValueAt(r, 1).toString()
        );

        nama.setText(
                table.getValueAt(r, 2).toString()
        );

        level.setSelectedItem(
                table.getValueAt(r, 3)
        );

        biaya.setText(
                table.getValueAt(r, 4)
                        .toString()
                        .replace(",", "")
        );

        simpan.setEnabled(false);
        ubah.setEnabled(true);
        hapus.setEnabled(true);

        simpan.repaint();
        ubah.repaint();
        hapus.repaint();
    }

    // RESET FORM
    private void clearForm() {

        selected = -1;

        kode.setText("");
        nama.setText("");
        biaya.setText("");

        level.setSelectedIndex(0);

        table.clearSelection();

        simpan.setEnabled(true);
        ubah.setEnabled(false);
        hapus.setEnabled(false);

        simpan.repaint();
        ubah.repaint();
        hapus.repaint();

        kode.requestFocus();
    }

    // ERROR HANDLER
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
                    ex.getMessage(),
                    "Terjadi Kesalahan",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
