package view;

import javax.swing.JOptionPane;
import dao.PesertaDAO;
import model.Peserta;
import java.sql.SQLException;

public class FormPendaftaranKursus extends javax.swing.JFrame {
    
    private final String KODE_AKSES = "PBO2026";
    private txtHasil pengelolaHasil;
    
    public FormPendaftaranKursus() {
        initComponents();
        
        txtHasil.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        txtHasil.setEditable(false);
        txtHasil.setLineWrap(false);
        txtHasil.setWrapStyleWord(true);
    
        txtKodeAkses.setText("");
        pengelolaHasil = new txtHasil(txtHasil);
        setLocationRelativeTo(null);

        txtHasil.setEditable(false);
        txtHasil.setLineWrap(true);
        txtHasil.setWrapStyleWord(true);

        pengelolaHasil.kosongkanHasil();

        getContentPane().setBackground(
        new java.awt.Color(251,251,240)
    );

    setLocationRelativeTo(null);
    
    cmbKursus.removeAllItems();
    cmbKursus.addItem("-- Pilih Kursus --");
    cmbKursus.addItem("Java Desktop");
    cmbKursus.addItem("Data Science");
    cmbKursus.addItem("Web Programming");
    cmbKursus.addItem("UI/UX Design");

    txtHasil.setEditable(false);
    txtHasil.setLineWrap(true);
    txtHasil.setWrapStyleWord(true);

    }
    
    private String getJenisKelamin() {
    if (rbLaki.isSelected()) {
        return "L";
    } else if (rbPerempuan.isSelected()) {
        return "P";
    }

        return "";
    }
    
    private double hitungBiayaDasar(String kursus) {
        switch (kursus) {
            case "Java Desktop":
                return 500000;
            case "Data Science":
                return 650000;
            case "Web Programming":
                return 450000;
            case "UI/UX Design":
                return 400000;
            default:
                return 0;
        }
    }


    private double hitungBiayaFasilitas() {

        double total = 0;

        if (cbModul.isSelected()) {
            total += 50000;
        }

        if (cbSertifikat.isSelected()) {
            total += 75000;
        }

        if (cbRekaman.isSelected()) {
            total += 40000;
        }

        return total;
    }

    private String buatRingkasanFasilitas() {

        String[] fasilitasDipilih = new String[3];
        int jumlah = 0;

        if (cbModul.isSelected()) {
            fasilitasDipilih[jumlah] = "Modul Cetak";
            jumlah++;
        }

        if (cbSertifikat.isSelected()) {
            fasilitasDipilih[jumlah] = "Sertifikat";
            jumlah++;
        }

        if (cbRekaman.isSelected()) {
            fasilitasDipilih[jumlah] = "Akses Rekaman";
            jumlah++;
        }

        if (jumlah == 0) {
            return "Tidak ada fasilitas tambahan";
        }

        StringBuilder hasil = new StringBuilder();

        for (int i = 0; i < jumlah; i++) {
            hasil.append("- ")
                 .append(fasilitasDipilih[i])
                 .append("\n");
        }

        return hasil.toString();
    }


    private void prosesPendaftaran() {
    // 1. Mengambil data dari form
        String nim = txtNim.getText().trim();
        String nama = txtNama.getText().trim();
        String prodi = txtProdi.getText().trim();
        String noHp = txtNoHp.getText().trim();
        String email = txtEmail.getText().trim();
        String kode = new String(txtKodeAkses.getPassword());

        String jenisKelamin = getJenisKelamin();

        // 2. Validasi data wajib
        if (nim.isEmpty() || nama.isEmpty()
                || prodi.isEmpty() || jenisKelamin.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "NIM, nama, jenis kelamin, dan prodi wajib diisi.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 3. Validasi kode akses
        if (!KODE_AKSES.equals(kode)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Kode akses salah! Masukkan PBO2026.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );

            txtKodeAkses.setText("");
            txtKodeAkses.requestFocus();
            return;
        }

        // 4. Validasi pilihan kursus
        if (cmbKursus.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih kursus terlebih dahulu.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 5. Membuat objek Peserta
        Peserta peserta = new Peserta(
                nim,
                nama,
                jenisKelamin,
                prodi,
                noHp,
                email
        );

        // 6. Menyimpan data peserta ke database
        PesertaDAO dao = new PesertaDAO();

        try {
            boolean berhasil = dao.insert(peserta);

            if (!berhasil) {
                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta tidak berhasil disimpan.",
                        "Informasi",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // 7. Jika data berhasil tersimpan,
            // lanjut menghitung biaya kursus
            String kursus =
                    (String) cmbKursus.getSelectedItem();

            double biayaDasar = hitungBiayaDasar(kursus);
            double biayaFasilitas = hitungBiayaFasilitas();

            double diskon = 0;

            // Mempertahankan aturan diskon pada kode lama
            if (rbLaki.isSelected()) {
                diskon = biayaDasar * 0.10;
            }

            double total =
                    biayaDasar - diskon + biayaFasilitas;

            java.text.NumberFormat format =
                    java.text.NumberFormat.getCurrencyInstance(
                            new java.util.Locale("id", "ID")
                    );

            String garis =
                    "====================================";

            StringBuilder hasil = new StringBuilder();

            hasil.append("PENDAFTARAN KURSUS\n");
            hasil.append(garis).append("\n");

            hasil.append(String.format(
                    "%-17s : %s%n", "NIM", nim));

            hasil.append(String.format(
                    "%-17s : %s%n", "Nama", nama));

            hasil.append(String.format(
                    "%-17s : %s%n", "Jenis Kelamin",
                    rbLaki.isSelected() ? "Laki-laki" : "Perempuan"));

            hasil.append(String.format(
                    "%-17s : %s%n", "Program Studi", prodi));

            hasil.append(String.format(
                    "%-17s : %s%n", "Nomor HP", noHp));

            hasil.append(String.format(
                    "%-17s : %s%n", "Email", email));

            hasil.append(String.format(
                    "%-17s : %s%n", "Kursus", kursus));

            hasil.append(String.format(
                    "%-17s : %s%n", "Biaya Dasar",
                    format.format(biayaDasar)));

            hasil.append(String.format(
                    "%-17s : %s%n", "Diskon",
                    format.format(diskon)));

            hasil.append(String.format(
                    "%-17s : %s%n", "Fasilitas",
                    format.format(biayaFasilitas)));

            hasil.append("\nRincian Fasilitas:\n");
            hasil.append(buatRingkasanFasilitas());
            hasil.append("\n");

            hasil.append(garis).append("\n");

            hasil.append(String.format(
                    "%-17s : %s%n", "TOTAL",
                    format.format(total)));

            hasil.append(garis);

            // 8. Menampilkan ringkasan pendaftaran
            pengelolaHasil.tampilkanHasil(
                    hasil.toString()
            );

            // 9. Menampilkan pesan sukses
            JOptionPane.showMessageDialog(
                    this,
                    "Data peserta berhasil disimpan ke database!"
                    + "\nRingkasan pendaftaran berhasil dibuat.",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal menyimpan data peserta:\n"
                    + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupJenisKelamin = new javax.swing.ButtonGroup();
        lblJudul = new javax.swing.JLabel();
        lblSubjudul = new javax.swing.JLabel();
        lblNim = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblJenisKelamin = new javax.swing.JLabel();
        rbLaki = new javax.swing.JRadioButton();
        rbPerempuan = new javax.swing.JRadioButton();
        lblKursus = new javax.swing.JLabel();
        cmbKursus = new javax.swing.JComboBox<>();
        lblFasilitas = new javax.swing.JLabel();
        cbModul = new javax.swing.JCheckBox();
        cbSertifikat = new javax.swing.JCheckBox();
        cbRekaman = new javax.swing.JCheckBox();
        btnSimpan = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        lblHasil = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtHasil = new javax.swing.JTextArea();
        lblKodeAkses = new javax.swing.JLabel();
        txtKodeAkses = new javax.swing.JPasswordField();
        lblNama1 = new javax.swing.JLabel();
        lblProdi = new javax.swing.JLabel();
        txtProdi = new javax.swing.JTextField();
        lblNoHp = new javax.swing.JLabel();
        txtNoHp = new javax.swing.JTextField();
        txtNim = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(251, 251, 240));

        lblJudul.setFont(new java.awt.Font("Perpetua", 1, 36)); // NOI18N
        lblJudul.setForeground(new java.awt.Color(117, 84, 57));
        lblJudul.setText("PENDAFTARAN KURSUS");

        lblSubjudul.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSubjudul.setText("Silakan isi data pendaftaran");

        lblNim.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNim.setForeground(new java.awt.Color(117, 84, 57));
        lblNim.setText("NIM");

        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });

        lblJenisKelamin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblJenisKelamin.setForeground(new java.awt.Color(117, 84, 57));
        lblJenisKelamin.setText("Jenis Kelamin");

        buttonGroupJenisKelamin.add(rbLaki);
        rbLaki.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rbLaki.setText("Laki-laki");
        rbLaki.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLakiActionPerformed(evt);
            }
        });

        buttonGroupJenisKelamin.add(rbPerempuan);
        rbPerempuan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rbPerempuan.setText("Perempuan");
        rbPerempuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPerempuanActionPerformed(evt);
            }
        });

        lblKursus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblKursus.setForeground(new java.awt.Color(117, 84, 57));
        lblKursus.setText("Pilih Kursus");

        cmbKursus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbKursus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Java Desktop", "Data Science", "UI/UX Design", "Web Programming" }));
        cmbKursus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKursusActionPerformed(evt);
            }
        });

        lblFasilitas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblFasilitas.setForeground(new java.awt.Color(117, 84, 57));
        lblFasilitas.setText("Fasilitas tambahan");

        cbModul.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbModul.setText("Modul");

        cbSertifikat.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbSertifikat.setText("Sertifikat");
        cbSertifikat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSertifikatActionPerformed(evt);
            }
        });

        cbRekaman.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cbRekaman.setText("Rekaman");
        cbRekaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRekamanActionPerformed(evt);
            }
        });

        btnSimpan.setBackground(new java.awt.Color(51, 0, 153));
        btnSimpan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("SIMPAN");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnKeluar.setBackground(new java.awt.Color(153, 0, 0));
        btnKeluar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnKeluar.setForeground(new java.awt.Color(255, 255, 255));
        btnKeluar.setText("KELUAR");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        btnReset.setBackground(new java.awt.Color(0, 102, 0));
        btnReset.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnReset.setForeground(new java.awt.Color(255, 255, 255));
        btnReset.setText("RESET");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        lblHasil.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblHasil.setForeground(new java.awt.Color(117, 84, 57));
        lblHasil.setText("HASIL PENDAFTARAN");

        txtHasil.setEditable(false);
        txtHasil.setColumns(20);
        txtHasil.setLineWrap(true);
        txtHasil.setRows(5);
        txtHasil.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtHasil);

        lblKodeAkses.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblKodeAkses.setForeground(new java.awt.Color(117, 84, 57));
        lblKodeAkses.setText("Kode Akses");

        txtKodeAkses.setText("jPasswordField1");
        txtKodeAkses.setToolTipText("241006");

        lblNama1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNama1.setForeground(new java.awt.Color(117, 84, 57));
        lblNama1.setText("Nama Peserta");

        lblProdi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblProdi.setForeground(new java.awt.Color(117, 84, 57));
        lblProdi.setText("Program Studi");

        lblNoHp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNoHp.setForeground(new java.awt.Color(117, 84, 57));
        lblNoHp.setText("Nomor HP");

        txtNim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNimActionPerformed(evt);
            }
        });

        lblEmail.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblEmail.setForeground(new java.awt.Color(117, 84, 57));
        lblEmail.setText("Email");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblJudul)
                        .addGap(147, 147, 147))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblSubjudul)
                        .addGap(259, 259, 259))))
            .addGroup(layout.createSequentialGroup()
                .addGap(244, 244, 244)
                .addComponent(rbLaki)
                .addGap(65, 65, 65)
                .addComponent(rbPerempuan)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(193, 193, 193)
                .addComponent(cbModul)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addComponent(cbRekaman)
                .addGap(62, 62, 62)
                .addComponent(cbSertifikat)
                .addGap(153, 153, 153))
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblKursus)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbKursus, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtProdi, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNama, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKodeAkses, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNoHp, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblJenisKelamin)
                                    .addComponent(lblKodeAkses)
                                    .addComponent(lblNama1)
                                    .addComponent(lblNim))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtNim, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(34, 34, 34))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHasil)
                            .addComponent(lblFasilitas)
                            .addComponent(lblEmail)
                            .addComponent(lblNoHp)
                            .addComponent(lblProdi))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(89, 89, 89))))
            .addGroup(layout.createSequentialGroup()
                .addGap(293, 293, 293)
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblJudul)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubjudul)
                .addGap(10, 10, 10)
                .addComponent(lblNim)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblNama1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblKodeAkses)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKodeAkses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblJenisKelamin)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbLaki)
                    .addComponent(rbPerempuan))
                .addGap(13, 13, 13)
                .addComponent(lblKursus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblProdi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtProdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblNoHp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblEmail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblFasilitas)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbModul)
                    .addComponent(cbRekaman)
                    .addComponent(cbSertifikat))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset)
                    .addComponent(btnSimpan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnKeluar)
                .addGap(3, 3, 3)
                .addComponent(lblHasil)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        lblJudul.getAccessibleContext().setAccessibleName("lblJudul");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbPerempuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPerempuanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbPerempuanActionPerformed

    private void cmbKursusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKursusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbKursusActionPerformed

    private void cbSertifikatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSertifikatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSertifikatActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
    prosesPendaftaran();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        txtNim.setText("");
        txtNama.setText("");
        txtKodeAkses.setText("");
        txtProdi.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");

        buttonGroupJenisKelamin.clearSelection();

        cmbKursus.setSelectedIndex(0);

        cbModul.setSelected(false);
        cbSertifikat.setSelected(false);
        cbRekaman.setSelected(false);

        pengelolaHasil.kosongkanHasil();

        txtNim.requestFocus();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        int pilihan = JOptionPane.showConfirmDialog(
               this,
               "Apakah kamu yakin ingin keluar?",
               "Konfirmasi Keluar",
               JOptionPane.YES_NO_OPTION
        );

        if (pilihan == JOptionPane.YES_OPTION) {
           dispose();
        }
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    private void cbRekamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRekamanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbRekamanActionPerformed

    private void rbLakiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLakiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbLakiActionPerformed

    private void txtNimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNimActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormPendaftaranKursus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPendaftaranKursus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPendaftaranKursus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPendaftaranKursus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPendaftaranKursus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSimpan;
    private javax.swing.ButtonGroup buttonGroupJenisKelamin;
    private javax.swing.JCheckBox cbModul;
    private javax.swing.JCheckBox cbRekaman;
    private javax.swing.JCheckBox cbSertifikat;
    private javax.swing.JComboBox<String> cmbKursus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFasilitas;
    private javax.swing.JLabel lblHasil;
    private javax.swing.JLabel lblJenisKelamin;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKodeAkses;
    private javax.swing.JLabel lblKursus;
    private javax.swing.JLabel lblNama1;
    private javax.swing.JLabel lblNim;
    private javax.swing.JLabel lblNoHp;
    private javax.swing.JLabel lblProdi;
    private javax.swing.JLabel lblSubjudul;
    private javax.swing.JRadioButton rbLaki;
    private javax.swing.JRadioButton rbPerempuan;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextArea txtHasil;
    private javax.swing.JPasswordField txtKodeAkses;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNim;
    private javax.swing.JTextField txtNoHp;
    private javax.swing.JTextField txtProdi;
    // End of variables declaration//GEN-END:variables
}
