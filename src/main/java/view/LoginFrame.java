
package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import dao.UserDAO;
import model.User;
import util.UserSession;

public class LoginFrame extends JFrame {

    // WARNA TEMA

    private final Color BG = new Color(250, 246, 238);
    private final Color CARD = new Color(255, 252, 246);

    private final Color BROWN = new Color(121, 85, 61);
    private final Color BROWN_DARK = new Color(91, 61, 43);
    private final Color BROWN_LIGHT = new Color(157, 119, 91);

    private final Color CREAM = new Color(255, 243, 223);
    private final Color INPUT_BG = new Color(249, 245, 237);
    private final Color BORDER = new Color(226, 213, 194);

    private final Color BUTTON_EXIT = new Color(238, 225, 205);

    // KOMPONEN

    private final JTextField txtUsername = new JTextField();
    private final JPasswordField pwdPassword = new JPasswordField();

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

        setSize(720, 620);
        setMinimumSize(new Dimension(700, 600));

        setResizable(true);
        setLocationRelativeTo(null);

        build();
    }

    // TAMPILAN UTAMA

    private void build() {

        JPanel root = new JPanel(new GridBagLayout());

        root.setBackground(BG);

        root.setBorder(
            new EmptyBorder(22, 28, 22, 28)
        );

        JPanel card = new JPanel(
            new BorderLayout(0, 0)
        );

        card.setBackground(CARD);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(0, 0, 0, 0)
            )
        );

        // HEADER

        JPanel header = new JPanel();

        header.setLayout(
            new BoxLayout(header, BoxLayout.Y_AXIS)
        );

        header.setBackground(BROWN_DARK);

        header.setBorder(
            new EmptyBorder(22, 15, 22, 15)
        );

        JLabel title = new JLabel("SIKURSUS");

        title.setFont(
            appFont(Font.BOLD, 36)
        );

        title.setForeground(CREAM);

        title.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel subtitle = new JLabel(
            "DESKTOP MANAGEMENT SYSTEM"
        );

        subtitle.setFont(
            appFont(Font.BOLD, 11)
        );

        subtitle.setForeground(
            new Color(235, 218, 193)
        );

        subtitle.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        card.add(header, BorderLayout.NORTH);

        // FORM LOGIN

        JPanel formWrapper = new JPanel(
            new GridBagLayout()
        );

        formWrapper.setBackground(CARD);

        formWrapper.setBorder(
            new EmptyBorder(12, 30, 8, 30)
        );

        JPanel form = new JPanel(
            new GridBagLayout()
        );

        form.setBackground(CARD);

        GridBagConstraints g = new GridBagConstraints();

        g.gridx = 0;
        g.weightx = 1.0;
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel welcome = new JLabel(
            "Selamat Datang!",
            SwingConstants.CENTER
        );

        welcome.setFont(
            appFont(Font.BOLD, 23)
        );

        welcome.setForeground(BROWN_DARK);

        g.gridy = 0;
        g.insets = new Insets(0, 0, 5, 0);

        g.fill = GridBagConstraints.NONE;
        g.anchor = GridBagConstraints.CENTER;

        form.add(welcome, g);

        JLabel description = new JLabel(
            "Silakan login untuk melanjutkan ke sistem",
            SwingConstants.CENTER
        );

        description.setFont(
            appFont(Font.PLAIN, 12)
        );

        description.setForeground(BROWN_LIGHT);

        g.gridy = 1;
        g.insets = new Insets(0, 0, 23, 0);

        form.add(description, g);

        JLabel lblUsername = createLabel("Username");

        g.gridy = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        g.insets = new Insets(0, 0, 7, 0);

        form.add(lblUsername, g);

        styleInput(txtUsername);

        txtUsername.setToolTipText(
            "Masukkan username"
        );

        g.gridy = 3;
        g.insets = new Insets(0, 0, 17, 0);

        form.add(txtUsername, g);

        JLabel lblPassword = createLabel("Password");

        g.gridy = 4;
        g.insets = new Insets(0, 0, 7, 0);

        form.add(lblPassword, g);

        styleInput(pwdPassword);

        pwdPassword.setToolTipText(
            "Masukkan password"
        );

        g.gridy = 5;
        g.insets = new Insets(0, 0, 12, 0);

        form.add(pwdPassword, g);

        JLabel note = new JLabel(
            "Masukkan username dan password akun Anda.",
            SwingConstants.CENTER
        );

        note.setFont(
            appFont(Font.PLAIN, 12)
        );

        note.setForeground(BROWN_LIGHT);

        g.gridy = 6;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.CENTER;

        g.insets = new Insets(0, 0, 0, 0);

        form.add(note, g);

        GridBagConstraints formConstraint =
            new GridBagConstraints();

        formConstraint.gridx = 0;
        formConstraint.gridy = 0;

        formConstraint.weightx = 1.0;
        formConstraint.weighty = 1.0;

        formConstraint.fill =
            GridBagConstraints.HORIZONTAL;

        formConstraint.anchor =
            GridBagConstraints.CENTER;

        formWrapper.add(form, formConstraint);

        card.add(formWrapper, BorderLayout.CENTER);

        // TOMBOL

        JButton btnKeluar = createButton(
            "KELUAR",
            BUTTON_EXIT,
            BROWN_DARK
        );

        JButton btnLogin = createButton(
            "LOGIN",
            BROWN_DARK,
            CREAM
        );

        JPanel buttons = new JPanel(
            new GridLayout(1, 2, 18, 0)
        );

        buttons.setBackground(CARD);

        buttons.setPreferredSize(
            new Dimension(600, 44)
        );

        buttons.add(btnKeluar);
        buttons.add(btnLogin);

        JPanel buttonWrapper = new JPanel(
            new BorderLayout()
        );

        buttonWrapper.setBackground(CARD);

        buttonWrapper.add(
            buttons,
            BorderLayout.CENTER
        );

        // FOOTER

        JLabel footer = new JLabel(
            "SIKURSUS Desktop  |  Sistem Informasi Kursus",
            SwingConstants.CENTER
        );

        footer.setFont(
            appFont(Font.PLAIN, 10)
        );

        footer.setForeground(BROWN_LIGHT);

        JPanel bottom = new JPanel();

        bottom.setLayout(
            new BoxLayout(bottom, BoxLayout.Y_AXIS)
        );

        bottom.setBackground(CARD);

        bottom.setBorder(
            new EmptyBorder(5, 30, 18, 30)
        );

        buttonWrapper.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        footer.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        bottom.add(buttonWrapper);

        bottom.add(
            Box.createVerticalStrut(14)
        );

        bottom.add(footer);

        card.add(bottom, BorderLayout.SOUTH);

        // POSISI CARD

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;

        c.weightx = 1.0;
        c.weighty = 1.0;

        c.fill = GridBagConstraints.BOTH;

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
            appFont(Font.BOLD, 13)
        );

        label.setForeground(BROWN_DARK);

        return label;
    }

    // INPUT

    private void styleInput(JTextField field) {

        field.setFont(
            appFont(Font.PLAIN, 14)
        );

        field.setForeground(BROWN_DARK);
        field.setBackground(INPUT_BG);

        field.setCaretColor(BROWN);

        field.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1),
                new EmptyBorder(8, 13, 8, 13)
            )
        );

        field.setPreferredSize(
            new Dimension(600, 43)
        );

        field.setMinimumSize(
            new Dimension(300, 43)
        );

        field.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 43)
        );

        field.addFocusListener(
            new java.awt.event.FocusAdapter() {

                @Override
                public void focusGained(
                    java.awt.event.FocusEvent e
                ) {

                    field.setBorder(
                        BorderFactory.createCompoundBorder(
                            new LineBorder(BROWN_LIGHT, 1),
                            new EmptyBorder(8, 13, 8, 13)
                        )
                    );
                }

                @Override
                public void focusLost(
                    java.awt.event.FocusEvent e
                ) {

                    field.setBorder(
                        BorderFactory.createCompoundBorder(
                            new LineBorder(BORDER, 1),
                            new EmptyBorder(8, 13, 8, 13)
                        )
                    );
                }
            }
        );
    }

    // TOMBOL

    private JButton createButton(
        String text,
        Color background,
        Color foreground
    ) {

        JButton button = new JButton(text);

        button.setFont(
            appFont(Font.BOLD, 12)
        );

        button.setForeground(foreground);
        button.setBackground(background);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setRolloverEnabled(true);

        button.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(
                    text.equals("KELUAR")
                        ? BORDER
                        : BROWN_DARK,
                    1
                ),
                new EmptyBorder(8, 15, 8, 15)
            )
        );

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        button.setPreferredSize(
            new Dimension(200, 44)
        );

        button.getModel().addChangeListener(e -> {

            ButtonModel model = button.getModel();

            if (model.isPressed() && model.isArmed()) {

                if (text.equals("LOGIN")) {
                    button.setBackground(
                        new Color(75, 49, 34)
                    );
                } else {
                    button.setBackground(
                        new Color(226, 211, 190)
                    );
                }

            } else if (model.isRollover()) {

                if (text.equals("LOGIN")) {
                    button.setBackground(BROWN);
                } else {
                    button.setBackground(
                        new Color(245, 235, 218)
                    );
                }

            } else {

                button.setBackground(background);
            }
        });

        return button;
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
