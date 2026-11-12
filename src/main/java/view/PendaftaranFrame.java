package view;

import dao.KelasDAO;
import dao.PendaftaranDAO;
import dao.PesertaDAO;
import model.Pendaftaran;
import report.ReportService;
import util.LookupItem;
import util.Validation;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PendaftaranFrame extends JFrame {

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

    // ================= KOMPONEN =================

    private JComboBox<LookupItem> peserta;
    private JComboBox<LookupItem> kelas;
    private JTextField tanggal;
    private JComboBox<String> status;

    private JTextField search;

    private final JTable table = new JTable(
            ViewUtil.tableModel(
                    "ID", "NIM", "Peserta", "Kelas",
                    "Kursus", "Tanggal", "Status"
            )
    );

    private JLabel lblJumlah;

    private final PendaftaranDAO dao = new PendaftaranDAO();
    private int selected = -1;

    // ================= FONT POPPINS =================

    private static Font poppinsRegular;
    private static Font poppinsBold;

    static {
        loadFonts();
    }

    private static void loadFonts() {

        try {

            URL regularURL = PendaftaranFrame.class.getResource(
                    "/fonts/Poppins-Regular.ttf"
            );

            URL boldURL = PendaftaranFrame.class.getResource(
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

    public PendaftaranFrame() {

        super("Transaksi Pendaftaran - SIKURSUS Desktop");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ViewUtil.setupFrame(this, 1180, 780);

        setGlobalFont();

        build();

        applyFont(getContentPane());

        loadLookups();
        load("");
    }

    // ================= FONT GLOBAL =================

    private void setGlobalFont() {

        Font defaultFont = appFont(Font.PLAIN, FONT_NORMAL);

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

    private void applyFont(Component component) {

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
                applyFont(child);
            }
        }

        if (component instanceof JTable) {

            JTable tbl = (JTable) component;

            tbl.setFont(appFont(Font.PLAIN, FONT_SMALL));
            tbl.setRowHeight(36);

            JTableHeader header = tbl.getTableHeader();

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

    // ================= INPUT PILIHAN =================

    private class InputCombo<T> extends JComboBox<T> {

        InputCombo() {
            super();
            init();
        }

        InputCombo(T[] items) {
            super(items);
            init();
        }

        private void init() {

            setOpaque(false);
            setUI(new ComboUI());
            setRenderer(new ComboRenderer());

            setBorder(new EmptyBorder(0, 8, 0, 0));
            setPreferredSize(new Dimension(200, 38));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        }

        @Override
        protected void paintBorder(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            if (isFocusOwner() || isPopupVisible()) {

                g2.setColor(BROWN_LIGHT);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(
                        1, 1, getWidth() - 3, getHeight() - 3, 12, 12
                );

            } else {

                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(
                        0, 0, getWidth() - 1, getHeight() - 1, 12, 12
                );
            }

            g2.dispose();
        }
    }

    private class ComboUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            return new ArrowButton();
        }

        @Override
        public void paintCurrentValueBackground(
                Graphics g, Rectangle bounds, boolean hasFocus
        ) {
            // dikosongkan: latar digambar oleh InputCombo
        }

        @Override
        public void paintCurrentValue(
                Graphics g, Rectangle bounds, boolean hasFocus
        ) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            Object item = comboBox.getSelectedItem();

            String text = item == null ? "" : item.toString();

            g2.setFont(appFont(Font.PLAIN, FONT_SMALL));
            g2.setColor(TEXT);

            FontMetrics fm = g2.getFontMetrics();

            int x = bounds.x + 5;
            int y = bounds.y
                    + (bounds.height - fm.getHeight()) / 2
                    + fm.getAscent();

            g2.drawString(text, x, y);

            g2.dispose();
        }

        @Override
        protected ComboPopup createPopup() {

            BasicComboPopup popup =
                    (BasicComboPopup) super.createPopup();

            popup.setBorder(
                    BorderFactory.createLineBorder(BORDER, 1)
            );

            return popup;
        }
    }

    private class ArrowButton extends JButton {

        ArrowButton() {

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setFocusable(false);
            setOpaque(false);

            setBorder(new EmptyBorder(0, 0, 0, 0));

            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(34, 34);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_PURE
            );

            g2.setColor(BROWN_LIGHT);

            g2.setStroke(
                    new BasicStroke(
                            2f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            int cx = getWidth() / 2;
            int cy = getHeight() / 2;

            g2.drawPolyline(
                    new int[]{cx - 5, cx, cx + 5},
                    new int[]{cy - 2, cy + 3, cy - 2},
                    3
            );

            g2.dispose();
        }
    }

    private class ComboRenderer extends DefaultListCellRenderer {

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

            label.setFont(appFont(Font.PLAIN, FONT_SMALL));
            label.setBorder(new EmptyBorder(9, 14, 9, 14));

            if (isSelected) {
                label.setBackground(ROW_SELECT);
                label.setForeground(BROWN_DARK);
            } else {
                label.setBackground(WHITE);
                label.setForeground(TEXT);
            }

            return label;
        }
    }

    // ================= MEMBANGUN TAMPILAN =================

    private void build() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // ================= HEADER =================

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

        JLabel lblTitle = new JLabel("PENDAFTARAN");

        lblTitle.setFont(appFont(Font.BOLD, FONT_TITLE));
        lblTitle.setForeground(CREAM);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Kelola transaksi pendaftaran : tambah, ubah, hapus, cetak, dan cari data."
        );

        subtitle.setFont(appFont(Font.PLAIN, FONT_SMALL));
        subtitle.setForeground(new Color(230, 214, 195));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        textBox.add(Box.createVerticalGlue());
        textBox.add(lblTitle);
        textBox.add(Box.createVerticalStrut(2));
        textBox.add(subtitle);
        textBox.add(Box.createVerticalGlue());

        header.add(textBox, BorderLayout.CENTER);

        root.add(header, BorderLayout.NORTH);

        // ================= MAIN CONTENT =================

        JPanel mainContent = new JPanel(
                new BorderLayout(0, 16)
        );

        mainContent.setBackground(BG);

        mainContent.setBorder(
                new EmptyBorder(18, 24, 22, 24)
        );

        root.add(mainContent, BorderLayout.CENTER);

        // ================= FORM PENDAFTARAN =================

        RoundedPanel formCard = new RoundedPanel(
                new BorderLayout(0, 12)
        );

        formCard.setBorder(
                new EmptyBorder(16, 22, 16, 22)
        );

        // Judul form
        JLabel formTitle = new JLabel("Form Pendaftaran");

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
        peserta = new InputCombo<LookupItem>();
        kelas = new InputCombo<LookupItem>();

        tanggal = new InputField("Contoh: 2026-01-15");
        tanggal.setText(ViewUtil.today());

        status = new InputCombo<String>(
                new String[]{"DAFTAR", "AKTIF", "SELESAI", "BATAL"}
        );

        // Panel input (2 kolom x 2 baris)
        JPanel inputPanel = new JPanel(new GridBagLayout());

        inputPanel.setOpaque(false);

        addField(inputPanel, "Peserta", peserta, 0, 0);
        addField(inputPanel, "Kelas", kelas, 1, 0);

        addField(inputPanel, "Status", status, 0, 1);
        addField(inputPanel, "Tanggal Daftar", tanggal, 1, 1);

        // Panel tombol (rata tengah)
        JButton simpan = createButton("SIMPAN", BTN_BLUE);
        JButton ubah = createButton("UBAH", BTN_BLUE);
        JButton hapus = createButton("HAPUS", BTN_RED);
        JButton reset = createButton("RESET", BTN_GRAY);
        JButton semua = createButton("CETAK SEMUA", BTN_GREEN);
        JButton terpilih = createButton("CETAK TERPILIH", BTN_GREEN);

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 0)
        );

        buttonPanel.setOpaque(false);

        buttonPanel.add(simpan);
        buttonPanel.add(ubah);
        buttonPanel.add(hapus);
        buttonPanel.add(reset);
        buttonPanel.add(semua);
        buttonPanel.add(terpilih);

        JPanel formBody = new JPanel(
                new BorderLayout(0, 14)
        );

        formBody.setOpaque(false);

        formBody.add(inputPanel, BorderLayout.CENTER);
        formBody.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(heading, BorderLayout.NORTH);
        formCard.add(formBody, BorderLayout.CENTER);

        mainContent.add(formCard, BorderLayout.NORTH);

        // ================= DATA PENDAFTARAN =================

        RoundedPanel dataPanel = new RoundedPanel(
                new BorderLayout(0, 12)
        );

        dataPanel.setBorder(
                new EmptyBorder(16, 22, 20, 22)
        );

        // Judul tabel
        JLabel dataTitle = new JLabel("Data Pendaftaran");

        dataTitle.setFont(appFont(Font.BOLD, 15));
        dataTitle.setForeground(BROWN_DARK);
        dataTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblJumlah = new JLabel("Menampilkan 0 data pendaftaran");

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
        search = new InputField("Cari peserta, kelas, atau status...");

        search.setPreferredSize(new Dimension(270, 38));

        JButton cari = createButton("CARI", BTN_BLUE);

        cari.setPreferredSize(new Dimension(90, 38));

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );

        searchPanel.setOpaque(false);

        searchPanel.add(search);
        searchPanel.add(cari);

        JPanel topPanel = new JPanel(
                new BorderLayout()
        );

        topPanel.setOpaque(false);

        topPanel.add(titleBox, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        // Tabel
        styleTable();

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(
                BorderFactory.createLineBorder(BORDER)
        );

        scroll.getViewport().setBackground(WHITE);

        dataPanel.add(topPanel, BorderLayout.NORTH);
        dataPanel.add(scroll, BorderLayout.CENTER);

        mainContent.add(dataPanel, BorderLayout.CENTER);

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

        cari.addActionListener(e -> load(search.getText()));
        search.addActionListener(e -> load(search.getText()));

        table.getSelectionModel().addListSelectionListener(e -> pick());

        getRootPane().setDefaultButton(simpan);
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

        int width = text.equals("CETAK TERPILIH") ? 160
                : text.equals("CETAK SEMUA") ? 140
                : 108;

        button.setPreferredSize(
                new Dimension(width, 38)
        );

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

            setHorizontalAlignment(columnAlign(column));

            return this;
        }
    }

    // Rata kolom: ID, NIM, Tanggal, Status di tengah;
    // Peserta, Kelas, dan Kursus di kiri.
    private int columnAlign(int column) {

        if (column == 2 || column == 3 || column == 4) {
            return SwingConstants.LEFT;
        }

        return SwingConstants.CENTER;
    }

    // ================= STYLE TABEL =================

    private void styleTable() {

        table.setFont(appFont(Font.PLAIN, FONT_SMALL));
        table.setForeground(TEXT);
        table.setBackground(WHITE);
        table.setRowHeight(36);

        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        table.setGridColor(new Color(240, 230, 216));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);

        table.setSelectionBackground(ROW_SELECT);
        table.setSelectionForeground(BROWN_DARK);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Header tabel
        JTableHeader tableHeader = table.getTableHeader();

        tableHeader.setFont(appFont(Font.BOLD, FONT_SMALL));
        tableHeader.setBackground(TABLE_HEADER);
        tableHeader.setForeground(BROWN_DARK);
        tableHeader.setPreferredSize(new Dimension(100, 40));
        tableHeader.setReorderingAllowed(false);

        // Renderer sel & header
        CellRenderer leftRenderer =
                new CellRenderer(SwingConstants.LEFT);

        CellRenderer centerRenderer =
                new CellRenderer(SwingConstants.CENTER);

        CellRenderer rightRenderer =
                new CellRenderer(SwingConstants.RIGHT);

        HeaderRenderer headerRenderer = new HeaderRenderer();

        for (int i = 0; i < table.getColumnCount(); i++) {

            int align = columnAlign(i);

            DefaultTableCellRenderer cellRenderer =
                    align == SwingConstants.LEFT ? leftRenderer
                    : align == SwingConstants.RIGHT ? rightRenderer
                    : centerRenderer;

            table.getColumnModel().getColumn(i)
                    .setHeaderRenderer(headerRenderer);

            table.getColumnModel().getColumn(i)
                    .setCellRenderer(cellRenderer);
        }
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

            lblJumlah.setText(
                    "Menampilkan " + list.size() + " data pendaftaran"
            );

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