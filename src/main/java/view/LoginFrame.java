package view;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import dao.UserDAO;
import model.User;
import util.UserSession;

public class LoginFrame extends JFrame {

    // WARNA TEMA

    private final Color BG = new Color(250, 246, 238);
    private final Color BG_END = new Color(238, 225, 205);
    private final Color CARD = new Color(255, 252, 246);

    private final Color BROWN = new Color(121, 85, 61);
    private final Color BROWN_DARK = new Color(91, 61, 43);
    private final Color BROWN_LIGHT = new Color(157, 119, 91);

    private final Color CREAM = new Color(255, 243, 223);
    private final Color INPUT_BG = new Color(249, 245, 237);
    private final Color BORDER = new Color(226, 213, 194);
    private final Color PLACEHOLDER = new Color(184, 165, 145);

    private final Color BUTTON_EXIT = new Color(238, 225, 205);

    // KOMPONEN

    private final JTextField txtUsername =
        new InputField("Masukkan username Anda");

    private final JPasswordField pwdPassword =
        new InputPassword("Masukkan password Anda");

    private final JCheckBox chkShow =
        new JCheckBox("Tampilkan password");

    private final UserDAO dao = new UserDAO();

    // FONT

    private Font poppinsRegular;
    private Font poppinsBold;

    private void loadPoppinsFonts() {

        try (
            InputStream regularStream =
                getClass().getResourceAsStream(
                    "/fonts/Poppins-Regular.ttf"
                );

            InputStream boldStream =
                getClass().getResourceAsStream(
                    "/fonts/Poppins-Bold.ttf"
                )
        ) {

            if (regularStream == null || boldStream == null) {
                throw new Exception(
                    "File font Poppins tidak ditemukan."
                );
            }

            poppinsRegular = Font.createFont(
                Font.TRUETYPE_FONT,
                regularStream
            );

            poppinsBold = Font.createFont(
                Font.TRUETYPE_FONT,
                boldStream
            );

            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .registerFont(poppinsRegular);

            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .registerFont(poppinsBold);

        } catch (Exception e) {

            System.err.println(
                "Gagal memuat Poppins: " + e.getMessage()
            );

            poppinsRegular = new Font(
                "SansSerif",
                Font.PLAIN,
                12
            );

            poppinsBold = new Font(
                "SansSerif",
                Font.BOLD,
                12
            );
        }
    }

    private Font appFont(int style, int size) {

        if (style == Font.BOLD) {
            return poppinsBold.deriveFont((float) size);
        }

        return poppinsRegular.deriveFont((float) size);
    }

    // CONSTRUCTOR

    public LoginFrame() {

        super("Login - SIKURSUS Desktop");

        loadPoppinsFonts();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(560, 760);
        setMinimumSize(new Dimension(520, 740));

        setResizable(true);
        setLocationRelativeTo(null);

        build();
    }

    // LATAR BELAKANG (GRADASI + LINGKARAN DEKORATIF)

    private class BackdropPanel extends JPanel {

        BackdropPanel() {
            super(new GridBagLayout());
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            g2.setPaint(
                new GradientPaint(0, 0, BG, 0, h, BG_END)
            );

            g2.fillRect(0, 0, w, h);

            g2.setColor(new Color(121, 85, 61, 22));

            int r1 = (int) (Math.min(w, h) * 0.30);
            g2.fillOval(-r1 / 2, -r1 / 2, r1 * 2, r1 * 2);

            int r2 = (int) (Math.min(w, h) * 0.22);
            g2.fillOval(w - r2, h - r2, r2 * 2, r2 * 2);

            g2.setColor(new Color(121, 85, 61, 14));

            int r3 = (int) (Math.min(w, h) * 0.12);
            g2.fillOval(w - r3 * 2, (int) (h * 0.08), r3 * 2, r3 * 2);

            g2.dispose();
        }
    }

    // KARTU MELAYANG (SUDUT MEMBULAT + BAYANGAN)

    private class CardPanel extends JPanel {

        private static final int SHADOW = 12;
        private static final int ARC = 30;

        CardPanel() {
            super(new GridBagLayout());
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            // bayangan lembut
            for (int i = 0; i < SHADOW; i++) {

                g2.setColor(new Color(62, 40, 28, 5));

                g2.fillRoundRect(
                    SHADOW - i,
                    SHADOW - i + 5,
                    w - (SHADOW - i) * 2,
                    h - (SHADOW - i) * 2,
                    ARC + i * 2,
                    ARC + i * 2
                );
            }

            // badan kartu
            RoundRectangle2D shape = new RoundRectangle2D.Float(
                SHADOW, SHADOW,
                w - SHADOW * 2, h - SHADOW * 2,
                ARC, ARC
            );

            g2.setColor(CARD);
            g2.fill(shape);

            // pita aksen di bagian atas kartu
            Shape oldClip = g2.getClip();

            g2.setClip(shape);

            g2.setPaint(
                new GradientPaint(
                    SHADOW, 0, BROWN_DARK,
                    w - SHADOW, 0, BROWN_LIGHT
                )
            );

            g2.fillRect(SHADOW, SHADOW, w - SHADOW * 2, 7);

            g2.setClip(oldClip);

            g2.setColor(BORDER);
            g2.draw(shape);

            g2.dispose();
        }
    }

    // LENCANA BULAT (LOGO)

    private class BadgePanel extends JPanel {

        BadgePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(78, 78));
        }

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

            int w = getWidth();
            int h = getHeight();

            // cincin luar
            g2.setColor(BORDER);
            g2.fillOval(0, 0, w, h);

            // lingkaran utama
            g2.setPaint(
                new GradientPaint(0, 0, BROWN_LIGHT, w, h, BROWN_DARK)
            );

            g2.fillOval(4, 4, w - 8, h - 8);

            // huruf S
            g2.setFont(appFont(Font.BOLD, 34));
            g2.setColor(CREAM);

            FontMetrics fm = g2.getFontMetrics();

            String s = "S";

            int x = (w - fm.stringWidth(s)) / 2;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.drawString(s, x, y);

            g2.dispose();
        }
    }

    // TOMBOL BERUJUNG MEMBULAT (PIL)

    private class RoundedButton extends JButton {

        private final Color normal;
        private final Color hover;
        private final Color pressed;
        private final Color edge;

        RoundedButton(
            String text,
            Color normal,
            Color hover,
            Color pressed,
            Color foreground,
            Color edge
        ) {

            super(text);

            this.normal = normal;
            this.hover = hover;
            this.pressed = pressed;
            this.edge = edge;

            setFont(appFont(Font.BOLD, 12));
            setForeground(foreground);

            setContentAreaFilled(false);
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setRolloverEnabled(true);

            setBorder(new EmptyBorder(8, 15, 8, 15));

            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setPreferredSize(new Dimension(200, 46));
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            ButtonModel m = getModel();

            Color fill = normal;

            if (m.isPressed() && m.isArmed()) {
                fill = pressed;
            } else if (m.isRollover()) {
                fill = hover;
            }

            int arc = getHeight();

            g2.setColor(fill);
            g2.fillRoundRect(
                0, 0, getWidth(), getHeight(), arc, arc
            );

            g2.setColor(edge);
            g2.drawRoundRect(
                0, 0, getWidth() - 1, getHeight() - 1, arc, arc
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // IKON KOTAK CENTANG

    private class CheckIcon implements Icon {

        private static final int SIZE = 20;

        @Override
        public void paintIcon(
            Component c, Graphics g, int x, int y
        ) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE
            );

            boolean on = ((AbstractButton) c).isSelected();

            if (on) {

                g2.setColor(BROWN_DARK);
                g2.fillRoundRect(x, y, SIZE, SIZE, 7, 7);

                // tanda centang
                g2.setColor(CREAM);
                g2.setStroke(
                    new BasicStroke(
                        2.2f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                    )
                );

                g2.drawPolyline(
                    new int[] { x + 5, x + 9, x + 15 },
                    new int[] { y + 10, y + 14, y + 6 },
                    3
                );

            } else {

                g2.setColor(INPUT_BG);
                g2.fillRoundRect(x, y, SIZE, SIZE, 7, 7);

                g2.setColor(BROWN_LIGHT);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(
                    x, y, SIZE - 1, SIZE - 1, 7, 7
                );
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }
    }

    // GAMBAR KOTAK INPUT (LATAR, GARIS TEPI, TEKS PETUNJUK)

    private void paintInputBackground(Graphics g, JComponent c) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(INPUT_BG);
        g2.fillRoundRect(
            0, 0, c.getWidth(), c.getHeight(), 16, 16
        );

        g2.dispose();
    }

    private void paintInputFrame(
        Graphics g,
        JComponent c,
        String hint,
        boolean empty
    ) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        int w = c.getWidth();
        int h = c.getHeight();

        // garis tepi (lebih tebal & coklat saat difokus)
        if (c.isFocusOwner()) {

            g2.setColor(BROWN_LIGHT);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, 16, 16);

        } else {

            g2.setColor(BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 16, 16);
        }

        // teks petunjuk saat kosong
        if (empty) {

            g2.setFont(appFont(Font.PLAIN, 13));
            g2.setColor(PLACEHOLDER);

            FontMetrics fm = g2.getFontMetrics();

            int x = c.getInsets().left;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent();

            g2.drawString(hint, x, y);
        }

        g2.dispose();
    }

    // INPUT USERNAME

    private class InputField extends JTextField {

        private final String hint;

        InputField(String hint) {

            this.hint = hint;

            setOpaque(false);

            addFocusListener(new java.awt.event.FocusAdapter() {

                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            paintInputBackground(g, this);

            super.paintComponent(g);

            paintInputFrame(g, this, hint, getText().isEmpty());
        }
    }

    // INPUT PASSWORD

    private class InputPassword extends JPasswordField {

        private final String hint;

        InputPassword(String hint) {

            this.hint = hint;

            setOpaque(false);

            addFocusListener(new java.awt.event.FocusAdapter() {

                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            paintInputBackground(g, this);

            super.paintComponent(g);

            paintInputFrame(
                g, this, hint, getPassword().length == 0
            );
        }
    }

    // TAMPILAN UTAMA

    private void build() {

        BackdropPanel root = new BackdropPanel();

        CardPanel card = new CardPanel();

        card.setBorder(
            new EmptyBorder(44, 50, 38, 50)
        );

        GridBagConstraints g = new GridBagConstraints();

        g.gridx = 0;
        g.weightx = 1.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.CENTER;

        // logo

        g.gridy = 0;
        g.fill = GridBagConstraints.NONE;
        g.insets = new Insets(0, 0, 12, 0);

        card.add(new BadgePanel(), g);

        // judul

        JLabel title = new JLabel(
            "SIKURSUS",
            SwingConstants.CENTER
        );

        title.setFont(appFont(Font.BOLD, 28));
        title.setForeground(BROWN_DARK);

        g.gridy = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(0, 0, 0, 0);

        card.add(title, g);

        JLabel subtitle = new JLabel(
            "DESKTOP MANAGEMENT SYSTEM",
            SwingConstants.CENTER
        );

        subtitle.setFont(appFont(Font.BOLD, 10));
        subtitle.setForeground(BROWN_LIGHT);

        g.gridy = 2;
        g.insets = new Insets(0, 0, 18, 0);

        card.add(subtitle, g);

        JLabel description = new JLabel(
            "Silakan masuk untuk melanjutkan",
            SwingConstants.CENTER
        );

        description.setFont(appFont(Font.PLAIN, 12));
        description.setForeground(BROWN);

        g.gridy = 3;
        g.insets = new Insets(0, 0, 22, 0);

        card.add(description, g);

        // username

        g.gridy = 4;
        g.insets = new Insets(0, 0, 6, 0);

        card.add(createLabel("Username"), g);

        styleInput(txtUsername);

        txtUsername.setToolTipText("Masukkan username");

        g.gridy = 5;
        g.insets = new Insets(0, 0, 16, 0);

        card.add(txtUsername, g);

        // password

        g.gridy = 6;
        g.insets = new Insets(0, 0, 6, 0);

        card.add(createLabel("Password"), g);

        styleInput(pwdPassword);

        pwdPassword.setToolTipText("Masukkan password");

        g.gridy = 7;
        g.insets = new Insets(0, 0, 10, 0);

        card.add(pwdPassword, g);

        // kotak centang tampilkan password

        final char defaultEcho = pwdPassword.getEchoChar();

        chkShow.setFont(appFont(Font.PLAIN, 11));
        chkShow.setForeground(BROWN);
        chkShow.setOpaque(false);
        chkShow.setFocusPainted(false);
        chkShow.setBorder(new EmptyBorder(2, 0, 2, 0));
        chkShow.setIconTextGap(9);
        chkShow.setCursor(new Cursor(Cursor.HAND_CURSOR));

        chkShow.setIcon(new CheckIcon());
        chkShow.setSelectedIcon(new CheckIcon());

        chkShow.addActionListener(e ->
            pwdPassword.setEchoChar(
                chkShow.isSelected() ? (char) 0 : defaultEcho
            )
        );

        g.gridy = 8;
        g.insets = new Insets(0, 0, 24, 0);

        card.add(chkShow, g);

        // tombol

        JButton btnLogin = new RoundedButton(
            "MASUK",
            BROWN_DARK,
            BROWN,
            new Color(75, 49, 34),
            CREAM,
            BROWN_DARK
        );

        JButton btnKeluar = new RoundedButton(
            "KELUAR",
            new Color(0, 0, 0, 0),
            BUTTON_EXIT,
            new Color(226, 211, 190),
            BROWN_DARK,
            BORDER
        );

        g.gridy = 9;
        g.insets = new Insets(0, 0, 10, 0);

        card.add(btnLogin, g);

        g.gridy = 10;
        g.insets = new Insets(0, 0, 18, 0);

        card.add(btnKeluar, g);

        // footer

        JLabel footer = new JLabel(
            "SIKURSUS Desktop  |  Sistem Informasi Kursus",
            SwingConstants.CENTER
        );

        footer.setFont(appFont(Font.PLAIN, 10));
        footer.setForeground(BROWN_LIGHT);

        g.gridy = 11;
        g.insets = new Insets(0, 0, 0, 0);

        card.add(footer, g);

        // kartu di tengah jendela

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;

        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;

        card.setPreferredSize(new Dimension(440, 690));

        root.add(card, c);

        setContentPane(root);

        getRootPane().setDefaultButton(btnLogin);

        // EVENT TOMBOL

        btnLogin.addActionListener(e -> login());

        btnKeluar.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah kamu yakin ingin keluar?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    // LABEL

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            appFont(Font.BOLD, 12)
        );

        label.setForeground(BROWN_DARK);

        return label;
    }

    // INPUT (GAYA KOTAK)

    private void styleInput(JTextField field) {

        field.setFont(
            appFont(Font.PLAIN, 13)
        );

        field.setForeground(BROWN_DARK);

        field.setCaretColor(BROWN);

        field.setBorder(
            new EmptyBorder(10, 15, 10, 15)
        );

        field.setPreferredSize(
            new Dimension(300, 46)
        );

        field.setMinimumSize(
            new Dimension(200, 46)
        );

        field.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 46)
        );
    }

    // PROSES LOGIN

    private void login() {

        String username = txtUsername.getText().trim();

        char[] password = pwdPassword.getPassword();

        try {

            if (username.isEmpty() || password.length == 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "Username dan password wajib diisi.",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            User user = dao.authenticate(
                username,
                password
            );

            if (user == null) {

                JOptionPane.showMessageDialog(
                    this,
                    "Login gagal. Periksa username dan password.",
                    "Login Gagal",
                    JOptionPane.WARNING_MESSAGE
                );

                pwdPassword.setText("");

                return;
            }

            UserSession.setCurrentUser(user);

            JOptionPane.showMessageDialog(
                this,
                "Login berhasil. Selamat datang, "
                + username + "!"
            );

            dispose();

            SwingUtilities.invokeLater(() -> {
                new DashboardFrame().setVisible(true);
            });

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Kesalahan database: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

        } finally {

            Arrays.fill(password, '\0');
        }
    }

    // MAIN

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LoginFrame frame = new LoginFrame();

            frame.setVisible(true);

        });
    }
}