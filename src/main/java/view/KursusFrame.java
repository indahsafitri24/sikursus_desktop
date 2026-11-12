package view;

import dao.KursusDAO;
import model.Kursus;

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
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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

            URL regularURL = KursusFrame.class.getResource(
                    "/fonts/Poppins-Regular.ttf"
            );

            URL boldURL = KursusFrame.class.getResource(
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

    public KursusFrame() {

        setTitle("Master Data Kursus - SIKURSUS Desktop");

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

        applyFont(getContentPane());

        load("");
        clearForm();
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

        Font current = component.getFont();

        int style = current == null ? Font.PLAIN : current.getStyle();
        int size = current == null ? FONT_NORMAL : current.getSize();

        component.setFont(appFont(style, size));

        if (component instanceof Container) {

            for (Component child : ((Container) component).getComponents()) {
                applyFont(child);
            }
        }

        if (component instanceof JTable) {

            JTable tbl = (JTable) component;

            tbl.setFont(appFont(Font.PLAIN, FONT_SMALL));
            tbl.setRowHeight(36);

            if (tbl.getTableHeader() != null) {
                tbl.getTableHeader().setFont(
                        appFont(Font.BOLD, FONT_SMALL)
                );
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

    // ================= INPUT PILIHAN (LEVEL) =================

    private class InputCombo extends JComboBox<String> {

        InputCombo(String[] items) {

            super(items);

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

        JLabel title = new JLabel("MASTER DATA KURSUS");

        title.setFont(appFont(Font.BOLD, FONT_TITLE));
        title.setForeground(CREAM);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Kelola data kursus: tambah, ubah, hapus, dan cari data."
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

    // ================= FORM KURSUS =================

    private void createForm() {

        RoundedPanel formCard = new RoundedPanel(
                new BorderLayout(0, 12)
        );

        formCard.setBorder(
                new EmptyBorder(16, 22, 16, 22)
        );

        // Judul form
        JLabel formTitle = new JLabel("Form Kursus");

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
        kode = new InputField("Contoh: JV-101");
        nama = new InputField("Masukkan nama kursus");
        biaya = new InputField("Contoh: 1500000");

        level = new InputCombo(
                new String[]{
                    "BASIC",
                    "INTERMEDIATE",
                    "ADVANCED"
                }
        );

        // Panel input (2 kolom x 2 baris)
        JPanel inputPanel = new JPanel(new GridBagLayout());

        inputPanel.setOpaque(false);

        addField(inputPanel, "Kode Kursus", kode, 0, 0);
        addField(inputPanel, "Nama Kursus", nama, 1, 0);

        addField(inputPanel, "Level", level, 0, 1);
        addField(inputPanel, "Biaya", biaya, 1, 1);

        // Panel tombol (rata tengah)
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 0)
        );

        buttonPanel.setOpaque(false);

        simpan = createButton("SIMPAN", BTN_BLUE);
        ubah = createButton("UBAH", BTN_BLUE);
        hapus = createButton("HAPUS", BTN_RED);
        reset = createButton("RESET", BTN_GRAY);

        buttonPanel.add(simpan);
        buttonPanel.add(ubah);
        buttonPanel.add(hapus);
        buttonPanel.add(reset);

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
                new Dimension(108, 38)
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
                    (column == 0 || column == 3)
                            ? SwingConstants.CENTER
                            : SwingConstants.LEFT
            );

            return this;
        }
    }

    // ================= TABEL DATA KURSUS =================

    private void createDataTable() {

        RoundedPanel dataPanel = new RoundedPanel(
                new BorderLayout(0, 12)
        );

        dataPanel.setBorder(
                new EmptyBorder(16, 22, 20, 22)
        );

        // Judul tabel
        JLabel dataTitle = new JLabel("Data Kursus");

        dataTitle.setFont(appFont(Font.BOLD, 15));
        dataTitle.setForeground(BROWN_DARK);
        dataTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblJumlah = new JLabel("Menampilkan 0 data kursus");

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
        search = new InputField("Cari kode, nama, atau level...");

        search.setPreferredSize(new Dimension(270, 38));

        cari = createButton("CARI", BTN_BLUE);

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

        // Model tabel
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

        // Header tabel
        JTableHeader tableHeader = table.getTableHeader();

        tableHeader.setFont(appFont(Font.BOLD, FONT_SMALL));
        tableHeader.setBackground(TABLE_HEADER);
        tableHeader.setForeground(BROWN_DARK);
        tableHeader.setPreferredSize(new Dimension(100, 40));
        tableHeader.setReorderingAllowed(false);

        // Lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(220);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);

        // Renderer sel & header (ID dan Level rata tengah)
        CellRenderer leftRenderer =
                new CellRenderer(SwingConstants.LEFT);

        CellRenderer centerRenderer =
                new CellRenderer(SwingConstants.CENTER);

        HeaderRenderer headerRenderer = new HeaderRenderer();

        for (int i = 0; i < table.getColumnCount(); i++) {

            table.getColumnModel().getColumn(i)
                    .setHeaderRenderer(headerRenderer);

            table.getColumnModel().getColumn(i)
                    .setCellRenderer(
                            (i == 0 || i == 3)
                                    ? centerRenderer
                                    : leftRenderer
                    );
        }

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(WHITE);

        dataPanel.add(topPanel, BorderLayout.NORTH);
        dataPanel.add(scroll, BorderLayout.CENTER);

        mainContent.add(dataPanel, BorderLayout.CENTER);

        // Event pencarian
        cari.addActionListener(e -> load(search.getText()));
        search.addActionListener(e -> load(search.getText()));

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                pick();
            }
        });
    }

    // ================= DATA KURSUS =================

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

    // ================= SIMPAN DATA =================

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

    // ================= UBAH DATA =================

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

    // ================= HAPUS DATA =================

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

    // ================= MEMUAT DATA =================

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

            lblJumlah.setText(
                    "Menampilkan " + list.size() + " data kursus"
            );

        } catch (SQLException ex) {

            ViewUtil.sqlError(
                    this,
                    "Memuat kursus",
                    ex
            );
        }
    }

    // ================= PILIH DATA =================

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

    // ================= RESET FORM =================

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

    // ================= ERROR HANDLER =================

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