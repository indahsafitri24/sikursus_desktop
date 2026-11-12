package view;

import dao.UserDAO;
import model.User;
import util.UserSession;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
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

    private JLabel lblJumlah;

    private JPanel mainContent;

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

    // ================= FONT POPPINS =================

    private static Font poppinsRegular;
    private static Font poppinsBold;

    static {
        loadFonts();
    }

    private static void loadFonts() {

        try {

            URL regularURL = UserFrame.class.getResource(
                    "/fonts/Poppins-Regular.ttf"
            );

            URL boldURL = UserFrame.class.getResource(
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

    public UserFrame() {

        setTitle("Master Users - SIKURSUS Desktop");

        setSize(1180, 780);
        setMinimumSize(new Dimension(1000, 720));

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

    // ================= FONT GLOBAL =================

    private void setGlobalFont() {

        Font defaultFont = appFont(Font.PLAIN, FONT_NORMAL);

        UIManager.put("defaultFont", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("PasswordField.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("CheckBox.font", defaultFont);
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

    // ================= INPUT PASSWORD =================

    private class InputPassword extends JPasswordField {

        private final String hint;

        InputPassword(String hint) {

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

            if (getPassword().length == 0 && hint != null) {

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

    // ================= INPUT PILIHAN (ROLE) =================

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

        JLabel title = new JLabel("MANAJEMEN USER & ROLE");

        title.setFont(appFont(Font.BOLD, FONT_TITLE));
        title.setForeground(CREAM);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Kelola akun pengguna: tambah, ubah role/status, reset password, hapus, dan cari data."
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

    // ================= FORM USER =================

    private void createForm() {

        RoundedPanel formCard = new RoundedPanel(
                new BorderLayout(0, 12)
        );

        formCard.setBorder(
                new EmptyBorder(16, 22, 16, 22)
        );

        // Judul form
        JLabel formTitle = new JLabel("Form User");

        formTitle.setFont(appFont(Font.BOLD, 15));
        formTitle.setForeground(BROWN_DARK);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel formDesc = new JLabel(
                "Lengkapi data di bawah ini, lalu klik Tambah. "
                + "Pilih baris pada tabel untuk mengubah role/status atau menghapus user."
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
        txtUsername = new InputField("Masukkan username");
        txtPassword = new InputPassword("Masukkan password");

        cmbRole = new InputCombo(
                new String[]{"ADMIN", "OPERATOR"}
        );

        chkAktif = new JCheckBox("Aktif", true);
        chkAktif.setFont(appFont(Font.PLAIN, FONT_SMALL));
        chkAktif.setForeground(TEXT);
        chkAktif.setOpaque(false);
        chkAktif.setFocusPainted(false);
        chkAktif.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chkAktif.setPreferredSize(new Dimension(200, 38));

        // Panel input (2 kolom x 2 baris)
        JPanel inputPanel = new JPanel(new GridBagLayout());

        inputPanel.setOpaque(false);

        addField(inputPanel, "Username", txtUsername, 0, 0);
        addField(inputPanel, "Password / Password Baru", txtPassword, 1, 0);

        addField(inputPanel, "Role", cmbRole, 0, 1);
        addField(inputPanel, "Status Akun", chkAktif, 1, 1);

        // Panel tombol (rata tengah)
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 0)
        );

        buttonPanel.setOpaque(false);

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
                new BorderLayout(0, 14)
        );

        formBody.setOpaque(false);

        formBody.add(inputPanel, BorderLayout.CENTER);
        formBody.add(buttonPanel, BorderLayout.SOUTH);

        formCard.add(heading, BorderLayout.NORTH);
        formCard.add(formBody, BorderLayout.CENTER);

        mainContent.add(formCard, BorderLayout.NORTH);

        // Event tombol
        btnTambah.addActionListener(e -> create());
        btnUbah.addActionListener(e -> update());
        btnResetPass.addActionListener(e -> resetPassword());
        btnHapus.addActionListener(e -> delete());
        btnReset.addActionListener(e -> clear());
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

        int width = text.equals("UBAH ROLE/STATUS") ? 155
                : text.equals("RESET PASSWORD") ? 150
                : text.equals("RESET FORM") ? 115
                : 100;

        button.setPreferredSize(
                new Dimension(width, 38)
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
                    column == 1
                            ? SwingConstants.LEFT
                            : SwingConstants.CENTER
            );

            return this;
        }
    }

    // ================= TABEL DATA USER =================

    private void createDataTable() {

        RoundedPanel dataPanel = new RoundedPanel(
                new BorderLayout(0, 12)
        );

        dataPanel.setBorder(
                new EmptyBorder(16, 22, 20, 22)
        );

        // Judul tabel
        JLabel dataTitle = new JLabel("Daftar User");

        dataTitle.setFont(appFont(Font.BOLD, 15));
        dataTitle.setForeground(BROWN_DARK);
        dataTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblJumlah = new JLabel("Menampilkan 0 data user");

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
        txtCari = new InputField("Cari ID, username, atau role...");

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
        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);

        // Renderer sel & header (ID, Role, dan Status Akun rata tengah)
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
                            i == 1
                                    ? leftRenderer
                                    : centerRenderer
                    );
        }

        JScrollPane scroll = new JScrollPane(table);

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
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                pick();
            }
        });
    }

    // ================= MEMUAT DATA =================

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

            lblJumlah.setText(
                    "Menampilkan " + data.size() + " data user"
            );

        } catch (SQLException ex) {

            showError("Gagal memuat data user: " + ex.getMessage());
        }
    }

    // ================= PILIH DATA =================

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

    // ================= TAMBAH USER =================

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

    // ================= UBAH ROLE DAN STATUS =================

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

    // ================= RESET PASSWORD =================

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

    // ================= HAPUS USER =================

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

    // ================= PENCARIAN DATA =================

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

            lblJumlah.setText(
                    "Menampilkan " + m.getRowCount() + " data user"
            );

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

    // ================= RESET FORM =================

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

    // ================= PESAN ERROR =================

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Terjadi Kesalahan",
                JOptionPane.ERROR_MESSAGE
        );
    }
}