/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javax.swing.JOptionPane;
import model.Kursus;

/**
 *
 * @author ASUS
 */
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
    
    // Mengambil jenis peserta
    private String getJenisPeserta() {
        if (rbMahasiswa.isSelected()) {
            return "Mahasiswa";
        } else {
            return "Umum";
        }
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


    // Menghitung biaya fasilitas tambahan
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

    // Membuat ringkasan fasilitas
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


    // Memproses pendaftaran
    private void prosesPendaftaran() {

        String nama = txtNama.getText().trim();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nama peserta wajib diisi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
            );
            txtNama.requestFocus();
            return;
        }
        
        String kode = new String(txtKodeAkses.getPassword());

        if (!KODE_AKSES.equals(kode)) {
            JOptionPane.showMessageDialog(
                this,
                "Kode akses demo salah!",
                "Validasi",
                JOptionPane.WARNING_MESSAGE
            );

            txtKodeAkses.setText("");
            txtKodeAkses.requestFocus();
            return;
        }

        if (!rbMahasiswa.isSelected() && !rbUmum.isSelected()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Silakan pilih jenis peserta!"
            );
            return;
        }

        if (cmbKursus.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih kursus terlebih dahulu!",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String jenis = getJenisPeserta();
        String kursus = (String) cmbKursus.getSelectedItem();

        double biayaDasar = hitungBiayaDasar(kursus);
        double biayaFasilitas = hitungBiayaFasilitas();

        double diskon = 0;

        if (rbMahasiswa.isSelected()) {
            diskon = biayaDasar * 0.10;
        }

        double total = biayaDasar - diskon + biayaFasilitas;

        java.text.NumberFormat format =
                java.text.NumberFormat.getCurrencyInstance(
                        new java.util.Locale("id", "ID")
                );
 
        String garis = "====================================";

        String hasil = "";
        hasil += "PENDAFTARAN KURSUS\n";
        hasil += garis + "\n";

        hasil += String.format("%-17s : %s%n", "Nama", nama);
        hasil += String.format("%-17s : %s%n", "Jenis Peserta", jenis);
        hasil += String.format("%-17s : %s%n", "Kursus", kursus);
        hasil += String.format("%-17s : %s%n", "Biaya Dasar", format.format(biayaDasar));
        hasil += String.format("%-17s : %s%n", "Diskon", format.format(diskon));
        hasil += String.format("%-17s : %s%n", "Fasilitas", format.format(biayaFasilitas));

        hasil += "\nRincian Fasilitas:\n";
        hasil += buatRingkasanFasilitas() + "\n";

        hasil += garis + "\n";
        hasil += String.format("%-17s : %s%n", "TOTAL", format.format(total));
        hasil += garis;

        pengelolaHasil.tampilkanHasil(hasil);

        JOptionPane.showMessageDialog(
                this,
                "Pendaftaran berhasil diproses!"
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupPeserta = new javax.swing.ButtonGroup();
        lblJudul = new javax.swing.JLabel();
        lblSubjudul = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        lblJenisPeserta = new javax.swing.JLabel();
        rbMahasiswa = new javax.swing.JRadioButton();
        rbUmum = new javax.swing.JRadioButton();
        lblKursus = new javax.swing.JLabel();
        cmbKursus = new javax.swing.JComboBox<>();
        lblFasilitas = new javax.swing.JLabel();
        cbModul = new javax.swing.JCheckBox();
        cbSertifikat = new javax.swing.JCheckBox();
        cbRekaman = new javax.swing.JCheckBox();
        btnProses = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        lblHasil = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtHasil = new javax.swing.JTextArea();
        lblKodeAkses = new javax.swing.JLabel();
        txtKodeAkses = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(251, 251, 240));

        lblJudul.setFont(new java.awt.Font("Perpetua", 1, 36)); // NOI18N
        lblJudul.setForeground(new java.awt.Color(117, 84, 57));
        lblJudul.setText("PENDAFTARAN KURSUS");

        lblSubjudul.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSubjudul.setText("Silakan isi data pendaftaran");

        lblNama.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNama.setForeground(new java.awt.Color(117, 84, 57));
        lblNama.setText("Nama Peserta");

        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });

        lblJenisPeserta.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblJenisPeserta.setForeground(new java.awt.Color(117, 84, 57));
        lblJenisPeserta.setText("Jenis Peserta");

        buttonGroupPeserta.add(rbMahasiswa);
        rbMahasiswa.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rbMahasiswa.setText("Mahasiswa");

        buttonGroupPeserta.add(rbUmum);
        rbUmum.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        rbUmum.setText("Umum");
        rbUmum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbUmumActionPerformed(evt);
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

        btnProses.setBackground(new java.awt.Color(51, 0, 153));
        btnProses.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnProses.setForeground(new java.awt.Color(255, 255, 255));
        btnProses.setText("PROSES");
        btnProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProsesActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 111, Short.MAX_VALUE)
                .addComponent(lblJudul)
                .addGap(109, 109, 109))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(209, 209, 209)
                        .addComponent(lblSubjudul))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(252, 252, 252)
                        .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(rbMahasiswa)
                        .addGap(64, 64, 64)
                        .addComponent(rbUmum))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(cbModul)
                        .addGap(43, 43, 43)
                        .addComponent(cbSertifikat)
                        .addGap(44, 44, 44)
                        .addComponent(cbRekaman))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblHasil)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblKodeAkses)
                                .addComponent(txtNama)
                                .addComponent(cmbKursus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblNama)
                                .addComponent(lblJenisPeserta)
                                .addComponent(lblKursus)
                                .addComponent(lblFasilitas)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                                .addComponent(txtKodeAkses)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(40, 40, 40)
                                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(50, 50, 50))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblJudul)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubjudul)
                .addGap(18, 18, 18)
                .addComponent(lblNama)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblKodeAkses)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKodeAkses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblJenisPeserta)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbMahasiswa)
                    .addComponent(rbUmum))
                .addGap(9, 9, 9)
                .addComponent(lblKursus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblFasilitas)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbModul)
                    .addComponent(cbSertifikat)
                    .addComponent(cbRekaman))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset)
                    .addComponent(btnProses))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnKeluar)
                .addGap(19, 19, 19)
                .addComponent(lblHasil)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        lblJudul.getAccessibleContext().setAccessibleName("lblJudul");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rbUmumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbUmumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbUmumActionPerformed

    private void cmbKursusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKursusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbKursusActionPerformed

    private void cbSertifikatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSertifikatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSertifikatActionPerformed

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
    prosesPendaftaran();
    }//GEN-LAST:event_btnProsesActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
    int pilihan = JOptionPane.showConfirmDialog(
        this,
        "Kosongkan seluruh input?",
        "Konfirmasi Reset",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );

    if (pilihan == JOptionPane.YES_OPTION) {

        txtNama.setText("");
        txtKodeAkses.setText("");

        buttonGroupPeserta.clearSelection();

        cmbKursus.setSelectedIndex(0);

        cbModul.setSelected(false);
        cbSertifikat.setSelected(false);
        cbRekaman.setSelected(false);

        pengelolaHasil.kosongkanHasil();

        JOptionPane.showMessageDialog(
            this,
            "Semua data berhasil direset!",
            "Reset Berhasil",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
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
    private javax.swing.JButton btnProses;
    private javax.swing.JButton btnReset;
    private javax.swing.ButtonGroup buttonGroupPeserta;
    private javax.swing.JCheckBox cbModul;
    private javax.swing.JCheckBox cbRekaman;
    private javax.swing.JCheckBox cbSertifikat;
    private javax.swing.JComboBox<String> cmbKursus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFasilitas;
    private javax.swing.JLabel lblHasil;
    private javax.swing.JLabel lblJenisPeserta;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblKodeAkses;
    private javax.swing.JLabel lblKursus;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblSubjudul;
    private javax.swing.JRadioButton rbMahasiswa;
    private javax.swing.JRadioButton rbUmum;
    private javax.swing.JTextArea txtHasil;
    private javax.swing.JPasswordField txtKodeAkses;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
