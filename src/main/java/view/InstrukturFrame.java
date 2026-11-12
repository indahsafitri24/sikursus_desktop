package view;

import dao.InstrukturDAO;
import model.Instruktur;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class InstrukturFrame extends JFrame {

    private final InstrukturDAO dao = new InstrukturDAO();
    private int selectedId = -1;

    private JTextField txtNama, txtKeahlian;
    private JTextField txtNoHp, txtEmail, txtCari;

    private JTable tblInstruktur;
    private DefaultTableModel tableModel;

    private JButton btnSimpan, btnUbah, btnHapus;
    private JButton btnReset, btnCetakSemua, btnCari;

    private JLabel lblJumlah;

    // ================= WARNA =================

    private final Color BG = new Color(250, 246, 238);
    private final Color WHITE = new Color(255, 252, 246);

    private final Color BROWN = new Color(121, 85, 61);
    private final Color BROWN_DARK = new Color(91, 61, 43);
    private final Color BROWN_LIGHT = new Color(157, 119, 91);

    private final Color CREAM = new Color(255, 243, 223);

    private final Color TEXT = new Color(75, 61, 51);
    private final Color MUTED = new Color(130, 112, 96);
    private final Color PLACEHOLDER = new Color(184, 165, 145);

    private final Color BORDER = new Color(226, 213, 194);
    private final Color FIELD_BG = new Color(251, 248, 242);
    private final Color TABLE_HEADER = new Color(238, 225, 205);
    private final Color ZEBRA = new Color(252, 248, 241);
    private final Color ROW_SELECT = new Color(235, 221, 201);

    // Warna tombol
    private final Color BTN_BLUE = new Color(145, 105, 76);
    private final Color BTN_RED = new Color(165, 91, 76);
    private final Color BTN_GRAY = new Color(137, 119, 102);
    private final Color BTN_GREEN = new Color(112, 132, 99);

    // Warna tombol ketika nonaktif
    private final Color DISABLED_BROWN = new Color(205, 181, 157);
    private final Color DISABLED_RED = new Color(211, 171, 160);

    private final int FONT_NORMAL = 12;
    private final int FONT_SMALL = 11;
    private final int FONT_TITLE = 20;

    private JPanel mainContent;

    // ================= FONT POPPINS =================

    private static Font poppinsRegular;
    private static Font poppinsBold;

    static {
        loadFonts();
    }

    private static void loadFonts() {

        try {

            URL regularURL = InstrukturFrame.class.getResource(
                    "/fonts/Poppins-Regular.ttf"
            );

            URL boldURL = InstrukturFrame.class.getResource(
                    "/fonts/Poppins-Bold.ttf"
            );

            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();

            if (regularURL != null) {
                try (InputStream in = regularURL.openStream()) {
                    poppinsRegular = Font.createFont(
                            Font.TRUETYPE_FONT, in
                    );
                    ge.registerFont(poppinsRegular);
                }
            }

            if (boldURL != null) {
                try (InputStream in = boldURL.openStream()) {
                    poppinsBold = Font.createFont(
                            Font.TRUETYPE_FONT, in
                    );
                    ge.registerFont(poppinsBold);
                }
            }

        } catch (Exception ex) {

            System.out.println(
                    "Font Poppins gagal dimuat: " + ex.getMessage()
            );
        }
    }

    private Font appFont(int style, int size) {

        if (style == Font.BOLD && poppinsBold != null) {
            return poppinsBold.deriveFont((float) size);
        }

        if (style != Font.BOLD && poppinsRegular != null) {
            return poppinsRegular.deriveFont((float) size);
        }

        return new Font("Segoe UI", style, size);
    }

    // ================= CONSTRUCTOR =================

    public InstrukturFrame() {

        setTitle("Master Data Instruktur - SIKURSUS Desktop");

        setSize(1180, 780);
        setMinimumSize(new Dimension(1000, 720));

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

    // ================= FONT GLOBAL =================

    private void setGlobalFont() {

        Font defaultFont = appFont(Font.PLAIN, FONT_NORMAL);

        UIManager.put("defaultFont", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
        UIManager.put("TableHeader.font", defaultFont);
        UIManager.put("OptionPane.messageFont", defaultFont);
        UIManager.put("OptionPane.buttonFont", defaultFont);
    }

    private void applyPoppins(Component component) {

        Font currentFont = component.getFont();

        int style = Font.PLAIN;
        int size = FONT_NORMAL;

        if (currentFont != null) {
            style = currentFont.getStyle();
            size = currentFont.getSize();
        }

        component.setFont(appFont(style, size));

        if (component instanceof Container) {

            Container container = (Container) component;

            for (Component child : container.getComponents()) {
                applyPoppins(child);
            }
        }

        if (component instanceof JTable) {

            JTable table = (JTable) component;

            table.setFont(appFont(Font.PLAIN, FONT_SMALL));
            table.setRowHeight(36);

            JTableHeader header = table.getTableHeader();

            if (header != null) {
                header.setFont(appFont(Font.BOLD, FONT_SMALL));
            }
        }
    }

    // ================= PANEL BERSUDUT MEMBULAT =================

    private class RoundedPanel extends JPanel {

        RoundedPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(WHITE);
            g2.fillRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1, 18, 18
            );

            g2.setColor(BORDER);
            g2.drawRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1, 18, 18
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // ================= INPUT TEKS =================

    private class InputField extends JTextField {

        private final String hint;

        InputField(String hint) {

            this.hint = hint;

            setOpaque(false);
            setForeground(TEXT);
            setCaretColor(BROWN);

            setBorder(new EmptyBorder(6, 13, 6, 13));

            setPreferredSize(new Dimension(200, 38));

            addFocusListener(new FocusAdapter() {

                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(FIELD_BG);
            g2.fillRoundRect(
                    0, 0, getWidth(), getHeight(), 12, 12
            );

            g2.dispose();

            super.paintComponent(g);

            Graphics2D g3 = (Graphics2D) g.create();

            g3.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g3.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            if (isFocusOwner()) {

                g3.setColor(BROWN_LIGHT);
                g3.setStroke(new BasicStroke(2f));
                g3.drawRoundRect(
                        1, 1, getWidth() - 3, getHeight() - 3, 12, 12
                );

            } else {

                g3.setColor(BORDER);
                g3.setStroke(new BasicStroke(1f));
                g3.drawRoundRect(
                        0, 0, getWidth() - 1, getHeight() - 1, 12, 12
                );
            }

            if (getText().isEmpty() && hint != null) {

                g3.setFont(appFont(Font.PLAIN, FONT_SMALL));
                g3.setColor(PLACEHOLDER);

                FontMetrics fm = g3.getFontMetrics();

                int x = getInsets().left;
                int y = (getHeight() - fm.getHeight()) / 2
                        + fm.getAscent();

                g3.drawString(hint, x, y);
            }

            g3.dispose();
        }
    }

    // ================= HEADER =================

    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(BROWN_DARK);
        header.setPreferredSize(new Dimension(100, 104));

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 3, 0, BROWN_LIGHT),
                        new EmptyBorder(12, 28, 9, 28)
                )
        );

        JPanel textBox = new JPanel();

        textBox.setOpaque(false);

        textBox.setLayout(
                new BoxLayout(textBox, BoxLayout.Y_AXIS)
        );

        JLabel title = new JLabel("MASTER DATA INSTRUKTUR");

        title.setFont(appFont(Font.BOLD, FONT_TITLE));
        title.setForeground(CREAM);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Kelola data instruktur kursus: tambah, ubah, hapus, dan cari data."
        );

        subtitle.setFont(appFont(Font.PLAIN, FONT_SMALL));
        subtitle.setForeground(new Color(230, 214, 195));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        textBox.add(Box.createVerticalGlue());
        textBox.add(title);
        textBox.add(Box.createVerticalStrut(2));
        textBox.add(subtitle);
        textBox.add(Box.createVerticalGlue());

        header.add(textBox, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
    }

    // ================= MAIN CONTENT =================

    private void createMainContent() {

        mainContent = new JPanel(
                new BorderLayout(0, 16)
        );

        mainContent.setBackground(BG);

        mainContent.setBorder(
                new EmptyBorder(18, 24, 22, 24)
        );

        add(mainContent, BorderLayout.CENTER);
    }

    // ================= FORM INSTRUKTUR =================

    private void createForm() {

        RoundedPanel formCard = new RoundedPanel(
                new BorderLayout(0, 12)
        );

        formCard.setBorder(
                new EmptyBorder(16, 22, 16, 22)
        );

        // Judul form
        JLabel formTitle = new JLabel("Form Instruktur");

        formTitle.setFont(appFont(Font.BOLD, 15));
        formTitle.setForeground(BROWN_DARK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel formDesc = new JLabel(
                "Lengkapi data di bawah ini, lalu klik Simpan. "
                + "Pilih baris pada tabel untuk mengubah atau menghapus data."
        );

        formDesc.setFont(appFont(Font.PLAIN, FONT_SMALL));
        formDesc.setForeground(MUTED);
        formDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel line = new JPanel();

        line.setBackground(BORDER);
        line.setPreferredSize(new Dimension(10, 1));
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel heading = new JPanel();

        heading.setOpaque(false);

        heading.setLayout(
                new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        heading.add(formTitle);
        heading.add(Box.createVerticalStrut(2));
        heading.add(formDesc);
        heading.add(Box.createVerticalStrut(10));
        heading.add(line);

        // Input form
        txtNama = new InputField("Masukkan nama instruktur");
        txtKeahlian = new InputField("Contoh: Pemrograman Java");
        txtNoHp = new InputField("Contoh: 081234567890");
        txtEmail = new InputField("Contoh: nama@email.com");

        // Panel input (2 kolom x 2 baris)
        JPanel inputPanel = new JPanel(
                new GridBagLayout()
        );

        inputPanel.setOpaque(false);

        addField(inputPanel, "Nama Instruktur", txtNama, 0, 0);
        addField(inputPanel, "Keahlian", txtKeahlian, 1, 0);

        addField(inputPanel, "Nomor HP", txtNoHp, 0, 1);
        addField(inputPanel, "Email", txtEmail, 1, 1);

        // Panel tombol (rata tengah)
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 0)
        );

        buttonPanel.setOpaque(false);

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
                new BorderLayout(0, 14)
        );

        formBody.setOpaque(false);

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

    // ================= KOLOM INPUT (LABEL DI ATAS) =================

    private void addField(
            JPanel panel,
            String labelText,
            JComponent input,
            int col,
            int row
    ) {

        int cols = 2;

        int rightGap = (col == cols - 1) ? 0 : 18;
        int topGap = (row == 0) ? 0 : 12;

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = col;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(labelText);

        label.setFont(appFont(Font.BOLD, FONT_SMALL));
        label.setForeground(BROWN_DARK);

        gbc.gridy = row * 2;
        gbc.insets = new Insets(topGap, 0, 5, rightGap);

        panel.add(label, gbc);

        gbc.gridy = row * 2 + 1;
        gbc.insets = new Insets(0, 0, 0, rightGap);

        panel.add(input, gbc);
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

                g2.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
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

                // Latar tombol
                g2.setColor(backgroundColor);

                g2.fillRoundRect(
                        0, 0,
                        getWidth() - 1,
                        getHeight() - 1,
                        12, 12
                );

                // Garis tepi
                g2.setColor(backgroundColor.darker());

                g2.drawRoundRect(
                        0, 0,
                        getWidth() - 1,
                        getHeight() - 1,
                        12, 12
                );

                // Teks tombol
                g2.setFont(getFont());
                g2.setColor(textColor);

                FontMetrics fm = g2.getFontMetrics();

                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2
                        + fm.getAscent();

                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        button.setFont(
                appFont(Font.BOLD, FONT_SMALL)
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
                        text.equals("CETAK SEMUA") ? 140 : 108,
                        38
                )
        );

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                if (button.isEnabled()) {
                    button.setBackground(color.brighter());
                    button.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                button.setBackground(color);
                button.repaint();
            }
        });

        return button;
    }

    // ================= RENDERER TABEL =================

    private class CellRenderer extends DefaultTableCellRenderer {

        CellRenderer(int align) {
            setHorizontalAlignment(align);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table, value, isSelected, false, row, column
            );

            setFont(appFont(Font.PLAIN, FONT_SMALL));
            setBorder(new EmptyBorder(0, 12, 0, 12));

            if (isSelected) {
                setBackground(ROW_SELECT);
                setForeground(BROWN_DARK);
            } else {
                setBackground(row % 2 == 0 ? WHITE : ZEBRA);
                setForeground(TEXT);
            }

            return this;
        }
    }

    private class HeaderRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table, value, false, false, row, column
            );

            setFont(appFont(Font.BOLD, FONT_SMALL));
            setBackground(TABLE_HEADER);
            setForeground(BROWN_DARK);
            setOpaque(true);

            setBorder(
                    BorderFactory.createCompoundBorder(
                            new MatteBorder(0, 0, 2, 0, BORDER),
                            new EmptyBorder(0, 12, 0, 12)
                    )
            );

            setHorizontalAlignment(
                    column == 0
                            ? SwingConstants.CENTER
                            : SwingConstants.LEFT
            );

            return this;
        }
    }

    // ================= TABEL DATA INSTRUKTUR =================

    private void createDataTable() {

        RoundedPanel dataPanel = new RoundedPanel(
                new BorderLayout(0, 12)
        );

        dataPanel.setBorder(
                new EmptyBorder(16, 22, 20, 22)
        );

        // Judul tabel
        JLabel dataTitle = new JLabel("Data Instruktur");

        dataTitle.setFont(appFont(Font.BOLD, 15));
        dataTitle.setForeground(BROWN_DARK);
        dataTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblJumlah = new JLabel("Menampilkan 0 data instruktur");

        lblJumlah.setFont(appFont(Font.PLAIN, FONT_SMALL));
        lblJumlah.setForeground(MUTED);
        lblJumlah.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titleBox = new JPanel();

        titleBox.setOpaque(false);

        titleBox.setLayout(
                new BoxLayout(titleBox, BoxLayout.Y_AXIS)
        );

        titleBox.add(dataTitle);
        titleBox.add(Box.createVerticalStrut(2));
        titleBox.add(lblJumlah);

        // Pencarian
        txtCari = new InputField("Cari nama, keahlian, atau email...");

        txtCari.setPreferredSize(new Dimension(270, 38));

        btnCari = createButton("CARI", BTN_BLUE);

        btnCari.setPreferredSize(new Dimension(90, 38));

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );

        searchPanel.setOpaque(false);

        searchPanel.add(txtCari);
        searchPanel.add(btnCari);

        JPanel topPanel = new JPanel(
                new BorderLayout()
        );

        topPanel.setOpaque(false);

        topPanel.add(titleBox, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // Model tabel
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

        tblInstruktur.setFont(appFont(Font.PLAIN, FONT_SMALL));
        tblInstruktur.setForeground(TEXT);
        tblInstruktur.setBackground(WHITE);
        tblInstruktur.setRowHeight(36);

        tblInstruktur.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tblInstruktur.setGridColor(new Color(240, 230, 216));
        tblInstruktur.setShowVerticalLines(false);
        tblInstruktur.setShowHorizontalLines(true);
        tblInstruktur.setIntercellSpacing(new Dimension(0, 1));
        tblInstruktur.setFillsViewportHeight(true);

        tblInstruktur.setSelectionBackground(ROW_SELECT);
        tblInstruktur.setSelectionForeground(BROWN_DARK);

        // Header tabel
        JTableHeader tableHeader = tblInstruktur.getTableHeader();

        tableHeader.setFont(appFont(Font.BOLD, FONT_SMALL));
        tableHeader.setBackground(TABLE_HEADER);
        tableHeader.setForeground(BROWN_DARK);
        tableHeader.setPreferredSize(new Dimension(100, 40));
        tableHeader.setReorderingAllowed(false);

        // Lebar kolom
        tblInstruktur.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblInstruktur.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblInstruktur.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblInstruktur.getColumnModel().getColumn(3).setPreferredWidth(140);
        tblInstruktur.getColumnModel().getColumn(4).setPreferredWidth(220);

        // Renderer sel & header (ID rata tengah)
        CellRenderer leftRenderer =
                new CellRenderer(SwingConstants.LEFT);

        CellRenderer centerRenderer =
                new CellRenderer(SwingConstants.CENTER);

        HeaderRenderer headerRenderer = new HeaderRenderer();

        for (int i = 0; i < tblInstruktur.getColumnCount(); i++) {

            tblInstruktur.getColumnModel().getColumn(i)
                    .setHeaderRenderer(headerRenderer);

            tblInstruktur.getColumnModel().getColumn(i)
                    .setCellRenderer(
                            i == 0
                                    ? centerRenderer
                                    : leftRenderer
                    );
        }

        JScrollPane scroll = new JScrollPane(tblInstruktur);

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
        tblInstruktur.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                selectRow();
            }
        });
    }

    // ================= MEMUAT DATA =================

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

    // ================= MENGISI TABEL =================

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

        lblJumlah.setText(
                "Menampilkan " + data.size() + " data instruktur"
        );
    }

    // ================= PILIH DATA =================

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

    // ================= VALIDASI FORM =================

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

    // ================= MENGAMBIL DATA FORM =================

    private Instruktur getFormData() {

        Instruktur x = new Instruktur();

        x.setId(Math.max(selectedId, 0));
        x.setNama(txtNama.getText().trim());
        x.setKeahlian(txtKeahlian.getText().trim());
        x.setNoHp(txtNoHp.getText().trim());
        x.setEmail(txtEmail.getText().trim());

        return x;
    }

    // ================= SIMPAN DATA =================

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

    // ================= UBAH DATA =================

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

    // ================= HAPUS DATA =================

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

    // ================= REFRESH DATA =================

    private void refreshData() {

        txtCari.setText("");

        loadTable();
        clearForm();
    }

    // ================= CETAK DATA =================

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
            new InstrukturFrame().setVisible(true);
        });
    }
}