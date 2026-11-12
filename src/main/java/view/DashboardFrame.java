package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import model.User;
import report.ReportService;
import util.UserSession;
import util.WindowManager;

public class DashboardFrame extends JFrame {

    // WARNA DASAR (COKLAT & KREM)

    private static final Color CREAM_BG =
            new Color(246, 240, 231);

    private static final Color CREAM_CARD =
            new Color(255, 252, 247);

    private static final Color CREAM_SOFT =
            new Color(240, 230, 216);

    private static final Color CREAM_HOVER =
            new Color(250, 244, 236);

    private static final Color BEIGE_BORDER =
            new Color(223, 208, 188);

    private static final Color BROWN_MAIN =
            new Color(98, 66, 48);

    private static final Color BROWN_HOVER =
            new Color(124, 88, 66);

    private static final Color BROWN_DARK =
            new Color(62, 40, 28);

    private static final Color BROWN_LIGHT =
            new Color(165, 130, 102);

    private static final Color TEXT_DARK =
            new Color(62, 44, 33);

    private static final Color TEXT_MUTED =
            new Color(136, 113, 94);

    // WARNA TEMA ADMIN (espresso, lebih gelap)

    private static final Color ADMIN_PRIMARY =
            new Color(62, 40, 28);

    private static final Color ADMIN_ACCENT =
            new Color(98, 66, 48);

    // WARNA TEMA OPERATOR (mocha, lebih hangat & terang)

    private static final Color OP_PRIMARY =
            new Color(139, 99, 72);

    private static final Color OP_ACCENT =
            new Color(160, 117, 88);

    // WARNA AKSEN KARTU MODUL

    private static final Color ACCENT_ORANGE =
            new Color(214, 111, 40);

    private static final Color ACCENT_TERRACOTTA =
            new Color(178, 74, 60);

    private static final Color ACCENT_GOLD =
            new Color(184, 134, 30);

    private static final Color ACCENT_PLUM =
            new Color(122, 80, 110);

    private static final Color ACCENT_GREEN =
            new Color(70, 130, 90);

    private static final Color ACCENT_TEAL =
            new Color(45, 120, 120);

    // UKURAN IKON MENU (piksel)

    private static final int MENU_ICON_SIZE = 20;

    // MEMBUAT WARNA PUDAR DARI WARNA AKSEN

    private static Color tint(Color c, double ratio) {

        int r = (int) (c.getRed() + (255 - c.getRed()) * ratio);
        int g = (int) (c.getGreen() + (255 - c.getGreen()) * ratio);
        int b = (int) (c.getBlue() + (255 - c.getBlue()) * ratio);

        return new Color(r, g, b);
    }

    // TEMA AKTIF (berubah sesuai role)

    private Color themePrimary = ADMIN_PRIMARY;
    private Color themeAccent = ADMIN_ACCENT;

    // FONT

    private static Font poppinsRegular;
    private static Font poppinsBold;

    static {
        loadFonts();
    }

    private static void loadFonts() {

        try {

            URL regularURL = DashboardFrame.class.getResource(
                    "/fonts/Poppins-Regular.ttf"
            );

            URL boldURL = DashboardFrame.class.getResource(
                    "/fonts/Poppins-Bold.ttf"
            );

            if (regularURL != null) {
                try (InputStream in = regularURL.openStream()) {
                    poppinsRegular = Font.createFont(
                            Font.TRUETYPE_FONT, in
                    );
                }
            }

            if (boldURL != null) {
                try (InputStream in = boldURL.openStream()) {
                    poppinsBold = Font.createFont(
                            Font.TRUETYPE_FONT, in
                    );
                }
            }

            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();

            if (poppinsRegular != null) {
                ge.registerFont(poppinsRegular);
            }

            if (poppinsBold != null) {
                ge.registerFont(poppinsBold);
            }

        } catch (Exception ex) {

            System.out.println(
                    "Font Poppins gagal dimuat: " + ex.getMessage()
            );
        }
    }

    private Font regular(float size) {

        if (poppinsRegular != null) {
            return poppinsRegular.deriveFont(size);
        }

        return new Font("SansSerif", Font.PLAIN, (int) size);
    }

    private Font bold(float size) {

        if (poppinsBold != null) {
            return poppinsBold.deriveFont(size);
        }

        return new Font("SansSerif", Font.BOLD, (int) size);
    }

    private Font italic(float size) {
        return regular(size).deriveFont(Font.ITALIC);
    }

    // KOMPONEN DASHBOARD

    private final JLabel lblUser = new JLabel();
    private final JLabel lblRole = new JLabel();

    private final JLabel lblWelcome = new JLabel();
    private final JLabel lblSubtitle = new JLabel();
    private final JLabel lblTitleInfo = new JLabel();

    // JUDUL AKSES CEPAT MODUL (berubah sesuai role)
    private final JLabel lblFlowTitle = new JLabel();

    // DAFTAR KARTU MODUL
    private final List<ModuleCard> moduleCards =
            new ArrayList<>();

    private final JPanel header = new JPanel(
            new BorderLayout(10, 5)
    );

    private final JPanel session = new JPanel(
            new GridLayout(1, 2, 15, 0)
    );

    private final JPanel infoPanel = new JPanel();

    private final JMenuItem miUsers =
            new JMenuItem("Users");

    private final JMenuItem miPeserta =
            new JMenuItem("Peserta");

    private final JMenuItem miInstruktur =
            new JMenuItem("Instruktur");

    private final JMenuItem miKursus =
            new JMenuItem("Kursus");

    private final JMenuItem miKelas =
            new JMenuItem("Kelas");

    // POPUP MENU

    private final JPopupMenu popupDashboard =
            new JPopupMenu();

    private final JMenuItem miTentang =
            new JMenuItem("Tentang Aplikasi");

    private final JMenuItem miRefresh =
            new JMenuItem("Refresh");

    // CONSTRUCTOR

    public DashboardFrame() {

        super("Dashboard - SIKURSUS Desktop");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().setBackground(CREAM_BG);

        buildMenu();
        buildPopupMenu();
        buildBody();
        applySession();

        setSize(getInitialWindowSize());

        setMinimumSize(new Dimension(800, 580));

        setLocationRelativeTo(null);

        setExtendedState(JFrame.NORMAL);
    }

    // UKURAN JENDELA

    private Dimension getInitialWindowSize() {

        Rectangle screen =
                GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getMaximumWindowBounds();

        int width = Math.min(1180, screen.width - 30);
        int height = Math.min(790, screen.height - 30);

        width = Math.max(800, width);
        height = Math.max(580, height);

        return new Dimension(width, height);
    }

    // KARTU MODUL (AKSES CEPAT)

    private class ModuleCard extends JPanel {

        private final boolean adminOnly;
        private final Runnable action;
        private final Color accent;

        private final JLabel lblTag;
        private final JLabel lblTitle;
        private final JLabel lblDesc;
        private final JLabel lblArrow;

        private boolean cardEnabled = true;
        private boolean hover = false;

        ModuleCard(
                String tag,
                String title,
                String desc,
                Color accent,
                boolean adminOnly,
                Runnable action
        ) {

            super(new BorderLayout(0, 8));

            this.adminOnly = adminOnly;
            this.action = action;
            this.accent = accent;

            setBackground(Color.WHITE);
            setOpaque(true);

            // BADGE KATEGORI
            lblTag = new JLabel(tag);
            lblTag.setFont(bold(9));
            lblTag.setOpaque(true);
            lblTag.setBorder(new EmptyBorder(4, 9, 4, 9));

            // PANAH
            lblArrow = new JLabel("\u2192");
            lblArrow.setFont(
                    new Font("SansSerif", Font.BOLD, 16)
            );

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);

            JPanel tagWrap = new JPanel(
                    new BorderLayout()
            );
            tagWrap.setOpaque(false);
            tagWrap.add(lblTag, BorderLayout.WEST);

            top.add(tagWrap, BorderLayout.CENTER);
            top.add(lblArrow, BorderLayout.EAST);

            // JUDUL & DESKRIPSI
            lblTitle = new JLabel(title);
            lblTitle.setFont(bold(15));

            lblDesc = new JLabel(desc);
            lblDesc.setFont(regular(10));

            JPanel text = new JPanel();
            text.setOpaque(false);
            text.setLayout(
                    new BoxLayout(text, BoxLayout.Y_AXIS)
            );

            lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

            text.add(lblTitle);
            text.add(Box.createVerticalStrut(4));
            text.add(lblDesc);

            add(top, BorderLayout.NORTH);
            add(text, BorderLayout.CENTER);

            // EVENT MOUSE (dipasang di kartu & semua isinya)
            MouseAdapter ma = new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!cardEnabled) return;
                    hover = true;
                    refreshLook();
                }

                @Override
                public void mouseExited(MouseEvent e) {

                    java.awt.Point p =
                            SwingUtilities.convertPoint(
                                    e.getComponent(),
                                    e.getPoint(),
                                    ModuleCard.this
                            );

                    if (ModuleCard.this.contains(p)) return;

                    hover = false;
                    refreshLook();
                }

                @Override
                public void mouseClicked(MouseEvent e) {

                    if (!cardEnabled) return;

                    if (SwingUtilities.isLeftMouseButton(e)) {
                        ModuleCard.this.action.run();
                    }
                }
            };

            addMouseListener(ma);
            attachMouse(this, ma);

            setCardEnabled(true);
        }

        private void attachMouse(
                Component c,
                MouseAdapter ma
        ) {

            if (c instanceof Container) {

                for (Component child
                        : ((Container) c).getComponents()) {

                    child.addMouseListener(ma);
                    attachMouse(child, ma);
                }
            }
        }

        // MENYESUAIKAN TAMPILAN (normal / hover / nonaktif)
        void refreshLook() {

            if (!cardEnabled) {

                Color off = new Color(190, 178, 165);

                lblTitle.setForeground(off);
                lblDesc.setForeground(off);
                lblTag.setForeground(off);
                lblTag.setBackground(new Color(245, 241, 235));
                lblArrow.setForeground(off);
                setBackground(new Color(251, 249, 245));

                setBorder(
                        BorderFactory.createCompoundBorder(
                                new LineBorder(
                                        BEIGE_BORDER, 1, true
                                ),
                                new EmptyBorder(14, 17, 14, 17)
                        )
                );

            } else if (hover) {

                lblTitle.setForeground(BROWN_DARK);
                lblDesc.setForeground(TEXT_MUTED);
                lblTag.setForeground(Color.WHITE);
                lblTag.setBackground(accent);
                lblArrow.setForeground(accent);
                setBackground(tint(accent, 0.94));

                setBorder(
                        BorderFactory.createCompoundBorder(
                                new LineBorder(
                                        accent, 2, true
                                ),
                                new EmptyBorder(13, 16, 13, 16)
                        )
                );

            } else {

                lblTitle.setForeground(BROWN_DARK);
                lblDesc.setForeground(TEXT_MUTED);
                lblTag.setForeground(accent);
                lblTag.setBackground(tint(accent, 0.86));
                lblArrow.setForeground(accent);
                setBackground(Color.WHITE);

                setBorder(
                        BorderFactory.createCompoundBorder(
                                new LineBorder(
                                        BEIGE_BORDER, 1, true
                                ),
                                new EmptyBorder(14, 17, 14, 17)
                        )
                );
            }

            repaint();
        }

        boolean isAdminOnly() {
            return adminOnly;
        }

        void setCardEnabled(boolean enabled) {

            cardEnabled = enabled;
            hover = false;

            String tip = enabled
                    ? "Klik untuk membuka modul"
                    : "Hanya dapat diakses oleh Administrator";

            setToolTipTextRecursive(this, tip);

            setCursor(
                    enabled
                            ? new Cursor(Cursor.HAND_CURSOR)
                            : new Cursor(Cursor.DEFAULT_CURSOR)
            );

            refreshLook();
        }

        private void setToolTipTextRecursive(
                JComponent c,
                String tip
        ) {

            c.setToolTipText(tip);

            for (Component child : c.getComponents()) {

                if (child instanceof JComponent) {
                    setToolTipTextRecursive(
                            (JComponent) child, tip
                    );
                }
            }
        }
    }

    // MEMBUAT KARTU MODUL

    private ModuleCard createModuleCard(
            String tag,
            String title,
            String desc,
            Color accent,
            boolean adminOnly,
            Runnable action
    ) {

        ModuleCard card = new ModuleCard(
                tag, title, desc, accent, adminOnly, action
        );

        moduleCards.add(card);

        return card;
    }

    // BODY DASHBOARD

    private void buildBody() {

        JPanel root = new JPanel(new BorderLayout());

        root.setBackground(CREAM_BG);

        JPanel body = new JPanel(new GridBagLayout());

        body.setBackground(CREAM_BG);

        root.setBorder(
                new EmptyBorder(12, 16, 14, 16)
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 4, 5, 4);

        // HEADER

        header.setBackground(ADMIN_PRIMARY);

        header.setBorder(
                BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 3, 0, BROWN_LIGHT),
                        new EmptyBorder(16, 22, 14, 22)
                )
        );

        JPanel headerText = new JPanel();

        headerText.setOpaque(false);

        headerText.setLayout(
                new BoxLayout(headerText, BoxLayout.Y_AXIS)
        );

        lblWelcome.setText(
                "SELAMAT DATANG DI SIKURSUS"
        );

        lblWelcome.setFont(bold(19));
        lblWelcome.setForeground(CREAM_CARD);

        lblWelcome.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        lblSubtitle.setText(
                "Sistem Informasi Manajemen Kursus"
        );

        lblSubtitle.setFont(regular(11));

        lblSubtitle.setForeground(
                new Color(230, 214, 195)
        );

        lblSubtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        headerText.add(lblWelcome);
        headerText.add(Box.createVerticalStrut(3));
        headerText.add(lblSubtitle);

        JLabel lblVersion = new JLabel(
                "DESKTOP APPLICATION"
        );

        lblVersion.setFont(bold(9));

        lblVersion.setForeground(
                new Color(220, 200, 178)
        );

        header.add(headerText, BorderLayout.CENTER);
        header.add(lblVersion, BorderLayout.EAST);

        gbc.gridy = 0;
        gbc.weighty = 0.0;

        body.add(header, gbc);

        // SESI PENGGUNA

        session.setBackground(CREAM_CARD);

        session.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BEIGE_BORDER, 1, true
                        ),
                        new EmptyBorder(11, 18, 11, 18)
                )
        );

        JPanel userPanel = new JPanel();

        userPanel.setOpaque(false);

        userPanel.setLayout(
                new BoxLayout(userPanel, BoxLayout.Y_AXIS)
        );

        JLabel lblUserCaption = new JLabel(
                "PENGGUNA AKTIF"
        );

        lblUserCaption.setFont(bold(9));
        lblUserCaption.setForeground(TEXT_MUTED);

        lblUser.setFont(bold(13));
        lblUser.setForeground(BROWN_DARK);

        userPanel.add(lblUserCaption);
        userPanel.add(Box.createVerticalStrut(3));
        userPanel.add(lblUser);

        JPanel rolePanel = new JPanel();

        rolePanel.setOpaque(false);

        rolePanel.setLayout(
                new BoxLayout(rolePanel, BoxLayout.Y_AXIS)
        );

        JLabel lblRoleCaption = new JLabel(
                "HAK AKSES"
        );

        lblRoleCaption.setFont(bold(9));
        lblRoleCaption.setForeground(TEXT_MUTED);

        lblRoleCaption.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        lblRole.setFont(bold(11));
        lblRole.setForeground(BROWN_MAIN);

        lblRole.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        lblRole.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        lblRole.setOpaque(true);
        lblRole.setBorder(
                new EmptyBorder(5, 10, 5, 10)
        );

        rolePanel.add(lblRoleCaption);
        rolePanel.add(Box.createVerticalStrut(3));
        rolePanel.add(lblRole);

        session.add(userPanel);
        session.add(rolePanel);

        gbc.gridy = 1;

        body.add(
                ViewUtil.section(
                        "Sesi Pengguna Aktif",
                        session
                ),
                gbc
        );

        // RINGKASAN SISTEM

        infoPanel.setLayout(
                new BoxLayout(infoPanel, BoxLayout.Y_AXIS)
        );

        infoPanel.setBackground(CREAM_CARD);

        infoPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BEIGE_BORDER, 1, true
                        ),
                        new EmptyBorder(14, 19, 14, 19)
                )
        );

        lblTitleInfo.setText("FINAL PROJECT PBO");

        lblTitleInfo.setFont(bold(17));
        lblTitleInfo.setForeground(BROWN_MAIN);

        lblTitleInfo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel lblMeeting = new JLabel(
                "Pertemuan 9 - 14"
        );

        lblMeeting.setFont(italic(11));
        lblMeeting.setForeground(BROWN_LIGHT);

        lblMeeting.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JPanel accentLine = new JPanel();

        accentLine.setBackground(BEIGE_BORDER);

        accentLine.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 2)
        );

        accentLine.setPreferredSize(
                new Dimension(10, 2)
        );

        accentLine.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel lblDescription = new JLabel(
                "<html>"
                + "<div style='width:650px;'>"
                + "Gunakan menu navigasi di bagian atas untuk "
                + "mengelola data master, transaksi, serta "
                + "pencetakan laporan sistem kursus."
                + "</div>"
                + "</html>"
        );

        lblDescription.setFont(regular(11));
        lblDescription.setForeground(TEXT_DARK);

        lblDescription.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // JUDUL AKSES CEPAT MODUL

        lblFlowTitle.setText("Akses Cepat Modul");

        lblFlowTitle.setFont(bold(13));
        lblFlowTitle.setForeground(BROWN_DARK);

        lblFlowTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // AKSES CEPAT MODUL (KARTU)

        // tooltip bernuansa coklat-krem
        UIManager.put("ToolTip.background", CREAM_SOFT);
        UIManager.put("ToolTip.foreground", TEXT_DARK);
        UIManager.put(
                "ToolTip.border",
                BorderFactory.createCompoundBorder(
                        new LineBorder(BROWN_LIGHT, 1),
                        new EmptyBorder(3, 6, 3, 6)
                )
        );
        UIManager.put("ToolTip.font", regular(11));

        JPanel flowPanel = new JPanel(
                new GridLayout(2, 3, 12, 12)
        );

        flowPanel.setOpaque(false);

        flowPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        flowPanel.add(createModuleCard(
                "PESERTA",
                "Data Peserta",
                "Kelola biodata & riwayat peserta",
                ACCENT_ORANGE,
                false,
                () -> WindowManager.open(
                        PesertaFrame.class, PesertaFrame::new
                )
        ));

        flowPanel.add(createModuleCard(
                "INSTRUKTUR",
                "Data Instruktur",
                "Manajemen pengajar & keahlian",
                ACCENT_TERRACOTTA,
                true,
                () -> WindowManager.open(
                        InstrukturFrame.class, InstrukturFrame::new
                )
        ));

        flowPanel.add(createModuleCard(
                "KURSUS",
                "Program Kursus",
                "Daftar materi, kurikulum & biaya",
                ACCENT_GOLD,
                true,
                () -> WindowManager.open(
                        KursusFrame.class, KursusFrame::new
                )
        ));

        flowPanel.add(createModuleCard(
                "KELAS",
                "Jadwal Kelas",
                "Atur ruang, kuota & jadwal",
                ACCENT_PLUM,
                true,
                () -> WindowManager.open(
                        KelasFrame.class, KelasFrame::new
                )
        ));

        flowPanel.add(createModuleCard(
                "TRANSAKSI",
                "Pendaftaran",
                "Registrasi peserta ke kelas baru",
                ACCENT_GREEN,
                false,
                () -> WindowManager.open(
                        PendaftaranFrame.class,
                        PendaftaranFrame::new
                )
        ));

        flowPanel.add(createModuleCard(
                "KEUANGAN",
                "Pembayaran",
                "Verifikasi tagihan & bukti bayar",
                ACCENT_TEAL,
                false,
                () -> WindowManager.open(
                        PembayaranFrame.class,
                        PembayaranFrame::new
                )
        ));

        // cegah kartu melar vertikal
        flowPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        flowPanel.getPreferredSize().height
                )
        );

        // DATABASE

        JPanel databasePanel = new JPanel(
                new GridLayout(2, 1, 0, 4)
        );

        databasePanel.setBackground(CREAM_SOFT);

        databasePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 4, 0, 0, BROWN_LIGHT),
                        new EmptyBorder(10, 13, 10, 13)
                )
        );

        JLabel lblDatabase = new JLabel(
                "DATABASE : db_sikursus"
        );

        lblDatabase.setFont(bold(11));
        lblDatabase.setForeground(BROWN_MAIN);

        JLabel lblArchitecture = new JLabel(
                "ARSITEKTUR : View -> DAO -> JDBC -> MySQL"
        );

        lblArchitecture.setFont(regular(10));
        lblArchitecture.setForeground(TEXT_MUTED);

        databasePanel.add(lblDatabase);
        databasePanel.add(lblArchitecture);

        databasePanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // cegah panel database melar vertikal
        databasePanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        databasePanel.getPreferredSize().height
                )
        );

        // TIPS

        JLabel lblTips = new JLabel(
                "<html>"
                + "<div style='width:650px;'>"
                + "<b>Tips:</b> Selesaikan pengisian data master "
                + "terlebih dahulu sebelum menguji menu transaksi."
                + "</div>"
                + "</html>"
        );

        lblTips.setFont(italic(10));
        lblTips.setForeground(TEXT_MUTED);

        lblTips.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // PENYUSUNAN KOMPONEN

        infoPanel.add(lblTitleInfo);
        infoPanel.add(Box.createVerticalStrut(2));

        infoPanel.add(lblMeeting);
        infoPanel.add(Box.createVerticalStrut(9));

        infoPanel.add(accentLine);
        infoPanel.add(Box.createVerticalStrut(9));

        infoPanel.add(lblDescription);
        infoPanel.add(Box.createVerticalStrut(11));

        infoPanel.add(lblFlowTitle);
        infoPanel.add(Box.createVerticalStrut(8));

        infoPanel.add(flowPanel);
        infoPanel.add(Box.createVerticalStrut(12));

        infoPanel.add(databasePanel);
        infoPanel.add(Box.createVerticalStrut(9));

        infoPanel.add(lblTips);

        // sisa ruang kosong ditaruh di bawah
        infoPanel.add(Box.createVerticalGlue());

        gbc.gridy = 2;
        gbc.weighty = 1.0;

        body.add(
                ViewUtil.section(
                        "Ringkasan & Panduan Sistem",
                        infoPanel
                ),
                gbc
        );

        root.add(body, BorderLayout.CENTER);

        setContentPane(root);

        applyCustomThemeRecursive(root);

        installPopupMenuRecursive(root);
    }

    // TEMA DASHBOARD BERDASARKAN ROLE

    private void applyRoleTheme(boolean admin) {

        if (admin) {

            // ADMIN: coklat espresso (gelap)
            themePrimary = ADMIN_PRIMARY;
            themeAccent = ADMIN_ACCENT;

            header.setBackground(ADMIN_PRIMARY);

            lblSubtitle.setText(
                    "Panel Administrator - Pengelolaan Sistem Kursus"
            );

            lblTitleInfo.setText(
                    "ADMINISTRATOR DASHBOARD"
            );

            lblTitleInfo.setForeground(ADMIN_PRIMARY);

            lblFlowTitle.setText(
                    "Akses Cepat Modul (Mode Administrator - Akses Penuh)"
            );

            session.setBackground(CREAM_CARD);

            session.setBorder(
                    BorderFactory.createCompoundBorder(
                            new LineBorder(
                                    ADMIN_ACCENT, 1, true
                            ),
                            new EmptyBorder(11, 18, 11, 18)
                    )
            );

            lblRole.setForeground(CREAM_CARD);
            lblRole.setBackground(ADMIN_PRIMARY);

            infoPanel.setBackground(CREAM_CARD);

        } else {

            // OPERATOR: coklat mocha (lebih hangat & terang)
            themePrimary = OP_PRIMARY;
            themeAccent = OP_ACCENT;

            header.setBackground(OP_PRIMARY);

            lblSubtitle.setText(
                    "Panel Operator - Pelayanan dan Operasional Kursus"
            );

            lblTitleInfo.setText(
                    "OPERATIONAL DASHBOARD"
            );

            lblTitleInfo.setForeground(OP_PRIMARY);

            lblFlowTitle.setText(
                    "Akses Cepat Modul (Mode Operator - Akses Terbatas)"
            );

            session.setBackground(
                    new Color(252, 247, 240)
            );

            session.setBorder(
                    BorderFactory.createCompoundBorder(
                            new LineBorder(
                                    BEIGE_BORDER, 1, true
                            ),
                            new EmptyBorder(11, 18, 11, 18)
                    )
            );

            lblRole.setForeground(OP_PRIMARY);
            lblRole.setBackground(CREAM_SOFT);

            infoPanel.setBackground(
                    new Color(252, 247, 240)
            );
        }

        lblSubtitle.setForeground(
                new Color(236, 222, 205)
        );

        // perbarui warna semua kartu
        for (ModuleCard card : moduleCards) {
            card.refreshLook();
        }

        repaint();
    }

    // POPUP MENU

    private void buildPopupMenu() {

        popupDashboard.setBackground(CREAM_CARD);

        popupDashboard.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                BEIGE_BORDER, 1, true
                        ),
                        new EmptyBorder(5, 5, 5, 5)
                )
        );

        miTentang.setFont(regular(11));
        miTentang.setForeground(TEXT_DARK);
        miTentang.setBackground(CREAM_CARD);
        miTentang.setOpaque(true);

        miTentang.setBorder(
                new EmptyBorder(9, 15, 9, 15)
        );

        miRefresh.setFont(regular(11));
        miRefresh.setForeground(TEXT_DARK);
        miRefresh.setBackground(CREAM_CARD);
        miRefresh.setOpaque(true);

        miRefresh.setBorder(
                new EmptyBorder(9, 15, 9, 15)
        );

        popupDashboard.add(miTentang);
        popupDashboard.add(miRefresh);

        miTentang.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "SIKURSUS Desktop\n"
                    + "Sistem Informasi Manajemen Kursus\n"
                    + "Project PBO - Program Studi Informatika",
                    "Tentang Aplikasi",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        miRefresh.addActionListener(
                e -> refreshDashboard()
        );
    }

    // PEMASANGAN POPUP MENU

    private void installPopupMenuRecursive(
            Component component
    ) {

        if (component instanceof JComponent) {

            JComponent jc = (JComponent) component;

            jc.setComponentPopupMenu(popupDashboard);

            for (Component child : jc.getComponents()) {
                installPopupMenuRecursive(child);
            }
        }
    }

    // REFRESH

    private void refreshDashboard() {

        applySession();

        revalidate();
        repaint();

        JOptionPane.showMessageDialog(
                this,
                "Dashboard berhasil diperbarui.",
                "Refresh Dashboard",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // DETEKSI WARNA BIRU

    private boolean isBlueColor(Color color) {

        if (color == null) {
            return false;
        }

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        return b > 100
                && b > r * 1.25
                && b > g * 1.10;
    }

    // TEMA WARNA

    private void applyCustomThemeRecursive(
            Component component
    ) {

        if (component instanceof JComponent) {

            JComponent jc = (JComponent) component;

            Color bg = jc.getBackground();

            if (isBlueColor(bg)) {
                jc.setBackground(BROWN_MAIN);
            }

            Color fg = jc.getForeground();

            if (isBlueColor(fg)) {

                if (component instanceof JLabel) {
                    jc.setForeground(BROWN_MAIN);
                } else {
                    jc.setForeground(CREAM_CARD);
                }
            }

            for (Component child : jc.getComponents()) {
                applyCustomThemeRecursive(child);
            }
        }
    }

    // MENU BAR

    private void buildMenu() {

        UIManager.put(
                "Menu.selectionBackground",
                BROWN_HOVER
        );

        UIManager.put(
                "Menu.selectionForeground",
                CREAM_CARD
        );

        // catatan: MenuItem.selectionBackground/Foreground
        // sengaja tidak diatur, supaya sorotan item dropdown
        // sama seperti item dropdown Master

        UIManager.put(
                "PopupMenu.background",
                CREAM_CARD
        );

        UIManager.put(
                "PopupMenu.foreground",
                TEXT_DARK
        );

        UIManager.put(
                "PopupMenu.border",
                BorderFactory.createLineBorder(
                        BEIGE_BORDER, 1
                )
        );

        JMenuBar bar = new JMenuBar();

        bar.setBackground(BROWN_MAIN);
        bar.setOpaque(true);

        bar.setBorder(
                new EmptyBorder(8, 14, 8, 14)
        );

        // MASTER

        JMenu master = createMenu("Master");

        master.add(miUsers);
        master.add(miPeserta);
        master.add(miInstruktur);
        master.add(miKursus);
        master.add(miKelas);

        // TRANSAKSI

        JMenu trx = createMenu("Transaksi");

        JMenuItem reg = createMenuItem("Pendaftaran");
        JMenuItem pay = createMenuItem("Pembayaran");

        trx.add(reg);
        trx.add(pay);

        // LAPORAN

        JMenu rep = createMenu("Laporan");

        JMenuItem rp = createMenuItem("Data Peserta");
        JMenuItem rr = createMenuItem("Data Pendaftaran");
        JMenuItem rt = createMenuItem("Data Pembayaran");

        JMenuItem rtp = createMenuItem(
                "Pembayaran per Periode"
        );

        rep.add(rp);
        rep.add(rr);
        rep.add(rt);
        rep.add(rtp);

        // KELUAR

        JMenu out = createMenu("Keluar");

        JMenuItem logout = createMenuItem("Logout");
        JMenuItem exit = createMenuItem("Tutup Aplikasi");

        out.add(logout);
        out.addSeparator();
        out.add(exit);

        bar.add(master);
        bar.add(trx);
        bar.add(rep);
        bar.add(out);

        setJMenuBar(bar);

        // EVENT MASTER

        miUsers.addActionListener(
                e -> WindowManager.open(
                        UserFrame.class, UserFrame::new
                )
        );

        miPeserta.addActionListener(
                e -> WindowManager.open(
                        PesertaFrame.class, PesertaFrame::new
                )
        );

        miInstruktur.addActionListener(
                e -> WindowManager.open(
                        InstrukturFrame.class, InstrukturFrame::new
                )
        );

        miKursus.addActionListener(
                e -> WindowManager.open(
                        KursusFrame.class, KursusFrame::new
                )
        );

        miKelas.addActionListener(
                e -> WindowManager.open(
                        KelasFrame.class, KelasFrame::new
                )
        );

        // EVENT TRANSAKSI

        reg.addActionListener(
                e -> WindowManager.open(
                        PendaftaranFrame.class,
                        PendaftaranFrame::new
                )
        );

        pay.addActionListener(
                e -> WindowManager.open(
                        PembayaranFrame.class,
                        PembayaranFrame::new
                )
        );

        // EVENT LAPORAN

        rp.addActionListener(
                e -> ReportService.showPeserta(this)
        );

        rr.addActionListener(
                e -> ReportService.showPendaftaranSemua(this)
        );

        rt.addActionListener(
                e -> ReportService.showPembayaranSemua(this)
        );

        rtp.addActionListener(
                e -> showPaymentPeriod()
        );

        // EVENT KELUAR

        logout.addActionListener(
                e -> logout()
        );

        exit.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Apakah kamu yakin ingin menutup aplikasi?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // IKON MENU
        // lokasi file: src/main/resources/icons/

        // master
        miUsers.setIcon(loadMenuIcon("users"));
        miPeserta.setIcon(loadMenuIcon("peserta"));
        miInstruktur.setIcon(loadMenuIcon("instruktur"));
        miKursus.setIcon(loadMenuIcon("kursus"));
        miKelas.setIcon(loadMenuIcon("kelas"));

        // transaksi
        reg.setIcon(loadMenuIcon("pendaftaran"));
        pay.setIcon(loadMenuIcon("pembayaran"));

        // laporan
        rp.setIcon(loadMenuIcon("laporan_peserta"));
        rr.setIcon(loadMenuIcon("laporan_pendaftaran"));
        rt.setIcon(loadMenuIcon("laporan_pembayaran"));
        rtp.setIcon(loadMenuIcon("laporan_periode"));

        // keluar
        logout.setIcon(loadMenuIcon("logout"));
        exit.setIcon(loadMenuIcon("tutup"));
    }

    // MEMBUAT MENU

    private JMenu createMenu(String title) {

        JMenu menu = new JMenu(title);

        menu.setFont(bold(12));
        menu.setForeground(CREAM_CARD);
        menu.setBackground(BROWN_MAIN);
        menu.setOpaque(true);

        menu.setBorder(
                new EmptyBorder(7, 15, 7, 15)
        );

        menu.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return menu;
    }

    // MEMBUAT ITEM MENU
    // dibuat polos (tanpa warna khusus) agar sama
    // seperti item dropdown Master

    private JMenuItem createMenuItem(String title) {

        return new JMenuItem(title);
    }

    // MEMUAT IKON MENU
    // mencoba beberapa kemungkinan nama file, lalu
    // mengecilkan ikon ke ukuran MENU_ICON_SIZE

    private ImageIcon loadMenuIcon(String name) {

        String capitalized =
                name.substring(0, 1).toUpperCase()
                + name.substring(1);

        String[] candidates = {
            "/icons/" + name + ".png",
            "/icons/" + name + ".png.png",
            "/icons/" + capitalized + ".png",
            "/icons/" + name.toUpperCase() + ".png",
            "/" + name + ".png"
        };

        for (String path : candidates) {

            URL u = getClass().getResource(path);

            if (u != null) {

                ImageIcon raw = new ImageIcon(u);

                Image scaled = raw.getImage().getScaledInstance(
                        MENU_ICON_SIZE,
                        MENU_ICON_SIZE,
                        Image.SCALE_SMOOTH
                );

                return new ImageIcon(scaled);
            }
        }

        System.out.println(
                "Ikon menu tidak ditemukan: " + name
        );

        return null;
    }

    // HAK AKSES PENGGUNA

    private void applySession() {

        User u = UserSession.getCurrentUser();

        if (u == null) {

            dispose();

            SwingUtilities.invokeLater(
                    () -> new LoginFrame().setVisible(true)
            );

            return;
        }

        lblUser.setText(
                "Pengguna: " + u.getUsername()
        );

        lblRole.setText(
                u.getRole().toUpperCase()
        );

        boolean admin = u.isAdmin();

        miUsers.setEnabled(admin);
        miInstruktur.setEnabled(admin);
        miKursus.setEnabled(admin);
        miKelas.setEnabled(admin);

        miPeserta.setEnabled(true);

        // KARTU MODUL MENGIKUTI HAK AKSES
        for (ModuleCard card : moduleCards) {

            if (card.isAdminOnly()) {
                card.setCardEnabled(admin);
            }
        }

        applyRoleTheme(admin);
    }

    // LAPORAN RENTANG TANGGAL

    private void showPaymentPeriod() {

        String a = JOptionPane.showInputDialog(
                this,
                "Tanggal awal (yyyy-MM-dd):",
                ViewUtil.today()
        );

        if (a == null) return;

        String b = JOptionPane.showInputDialog(
                this,
                "Tanggal akhir (yyyy-MM-dd):",
                ViewUtil.today()
        );

        if (b == null) return;

        try {

            Date tanggalAwal = Date.valueOf(a.trim());
            Date tanggalAkhir = Date.valueOf(b.trim());

            if (tanggalAkhir.before(tanggalAwal)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Tanggal akhir tidak boleh lebih awal dari tanggal awal.",
                        "Rentang Tanggal",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            ReportService.showPembayaranPeriode(
                    this,
                    tanggalAwal,
                    tanggalAkhir
            );

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Format tanggal harus yyyy-MM-dd.",
                    "Format Tanggal",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // LOGOUT

    private void logout() {

        WindowManager.closeAll();

        UserSession.clear();

        dispose();

        SwingUtilities.invokeLater(
                () -> new LoginFrame().setVisible(true)
        );
    }
}