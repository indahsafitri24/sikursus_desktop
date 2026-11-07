
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.User;
import report.ReportService;
import util.UserSession;
import util.WindowManager;

public class DashboardFrame extends JFrame {

    // WARNA

    private static final Color CREAM_BG =
            new Color(247, 242, 234);

    private static final Color CREAM_CARD =
            new Color(255, 253, 249);

    private static final Color CREAM_SOFT =
            new Color(239, 229, 215);

    private static final Color BEIGE_BORDER =
            new Color(222, 207, 188);

    private static final Color BROWN_MAIN =
            new Color(91, 57, 39);

    private static final Color BROWN_HOVER =
            new Color(120, 81, 59);

    private static final Color BROWN_DARK =
            new Color(54, 33, 24);

    private static final Color BROWN_LIGHT =
            new Color(153, 116, 88);

    private static final Color TEXT_DARK =
            new Color(66, 47, 35);

    private static final Color TEXT_MUTED =
            new Color(139, 116, 96);

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

        header.setBackground(BROWN_MAIN);

        header.setBorder(
                new EmptyBorder(16, 22, 16, 22)
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
                new Color(235, 219, 200)
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

        accentLine.setBackground(BROWN_LIGHT);

        accentLine.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 3)
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

        JLabel lblFlowTitle = new JLabel(
                "ALUR UTAMA SISTEM"
        );

        lblFlowTitle.setFont(bold(12));
        lblFlowTitle.setForeground(BROWN_MAIN);

        lblFlowTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // ALUR UTAMA

        JPanel flowPanel = new JPanel(
                new GridLayout(1, 5, 7, 0)
        );

        flowPanel.setOpaque(false);

        flowPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        String[] flowItems = {
            "Master Data",
            "Kelas",
            "Pendaftaran",
            "Pembayaran",
            "Laporan"
        };

        for (String item : flowItems) {

            JLabel label = new JLabel(
                    "<html><center>" + item + "</center></html>",
                    SwingConstants.CENTER
            );

            label.setFont(bold(15));

            label.setForeground(CREAM_CARD);
            label.setOpaque(true);
            label.setBackground(BROWN_MAIN);

            label.setBorder(
                    BorderFactory.createCompoundBorder(
                            new LineBorder(
                                    BROWN_LIGHT, 2, true
                            ),
                            new EmptyBorder(13, 5, 13, 5)
                    )
            );

            flowPanel.add(label);
        }

        // DATABASE

        JPanel databasePanel = new JPanel(
                new GridLayout(2, 1, 0, 4)
        );

        databasePanel.setBackground(CREAM_SOFT);

        databasePanel.setBorder(
                new EmptyBorder(10, 13, 10, 13)
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
        infoPanel.add(Box.createVerticalStrut(7));

        infoPanel.add(flowPanel);
        infoPanel.add(Box.createVerticalStrut(12));

        infoPanel.add(databasePanel);
        infoPanel.add(Box.createVerticalStrut(9));

        infoPanel.add(lblTips);

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

            header.setBackground(BROWN_DARK);

            lblSubtitle.setText(
                    "Panel Administrator - Pengelolaan Sistem Kursus"
            );

            lblTitleInfo.setText(
                    "ADMINISTRATOR DASHBOARD"
            );

            lblTitleInfo.setForeground(BROWN_DARK);

            session.setBackground(CREAM_CARD);

            session.setBorder(
                    BorderFactory.createCompoundBorder(
                            new LineBorder(
                                    BROWN_LIGHT, 1, true
                            ),
                            new EmptyBorder(11, 18, 11, 18)
                    )
            );

            lblRole.setForeground(CREAM_CARD);
            lblRole.setBackground(BROWN_MAIN);

            infoPanel.setBackground(CREAM_CARD);

        } else {

            header.setBackground(BROWN_HOVER);

            lblSubtitle.setText(
                    "Panel Operator - Pelayanan dan Operasional Kursus"
            );

            lblTitleInfo.setText(
                    "OPERATIONAL DASHBOARD"
            );

            lblTitleInfo.setForeground(BROWN_HOVER);

            session.setBackground(
                    new Color(250, 246, 239)
            );

            session.setBorder(
                    BorderFactory.createCompoundBorder(
                            new LineBorder(
                                    BEIGE_BORDER, 1, true
                            ),
                            new EmptyBorder(11, 18, 11, 18)
                    )
            );

            lblRole.setForeground(BROWN_MAIN);
            lblRole.setBackground(CREAM_SOFT);

            infoPanel.setBackground(
                    new Color(250, 246, 239)
            );
        }

        lblSubtitle.setForeground(
                new Color(230, 214, 195)
        );

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

        UIManager.put(
                "MenuItem.selectionBackground",
                BROWN_HOVER
        );

        UIManager.put(
                "MenuItem.selectionForeground",
                CREAM_CARD
        );

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

        JMenuItem rp = createMenuItem("Peserta - Semua");
        JMenuItem rr = createMenuItem("Pendaftaran - Semua");
        JMenuItem rt = createMenuItem("Pembayaran - Semua");

        JMenuItem rtp = createMenuItem(
                "Pembayaran - Rentang Tanggal"
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

        miPeserta.setIcon(loadIcon("/icons/peserta.png"));
        miInstruktur.setIcon(loadIcon("/icons/instruktur.png"));
        miKursus.setIcon(loadIcon("/icons/kursus.png"));
        miKelas.setIcon(loadIcon("/icons/kelas.png"));
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

    private JMenuItem createMenuItem(String title) {

        JMenuItem item = new JMenuItem(title);

        item.setFont(regular(11));
        item.setForeground(CREAM_CARD);
        item.setBackground(BROWN_MAIN);
        item.setOpaque(true);

        item.setBorder(
                new EmptyBorder(9, 17, 9, 17)
        );

        item.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return item;
    }

    // MEMUAT IKON

    private ImageIcon loadIcon(String path) {

        URL u = getClass().getResource(path);

        return u == null ? null : new ImageIcon(u);
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
