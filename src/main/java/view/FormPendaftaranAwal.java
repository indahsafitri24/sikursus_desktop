package view;

import model.Kursus;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JOptionPane;

public class FormPendaftaranAwal extends javax.swing.JFrame {

        public FormPendaftaranAwal() {
        initComponents();
            setLocationRelativeTo(null);

            getContentPane().setBackground(
                new java.awt.Color(251, 251, 240)
            );

            txtHasilPendaftaran.setFont(
                new java.awt.Font(
                    "Monospaced",
                    java.awt.Font.PLAIN,
                    12
                )
            );

            txtHasilPendaftaran.setEditable(false);
            txtKodeKursus.setText("");
            txtBiayaKursus.setEditable(false);
            txtDiskon.setEditable(false);

            aturBiayaDanDiskon();
        }

        private void aturBiayaDanDiskon() {
            String namaKursus = String.valueOf(
                cmbNamaKursus.getSelectedItem()
            );

            String level = String.valueOf(
                cmbLevelKursus.getSelectedItem()
            );

            double biaya;

            switch (namaKursus) {
                case "Java":
                    biaya = 100000;
                    break;

                case "Data Science":
                    biaya = 200000;
                    break;

                case "UI/UX":
                    biaya = 300000;
                    break;

                default:
                    biaya = 0;
                    break;
            }

            int diskon;

            if ("Basic".equalsIgnoreCase(level)) {
                diskon = 0;
            } else if ("Intermediate".equalsIgnoreCase(level)) {
                diskon = 10;
            } else if ("Advanced".equalsIgnoreCase(level)) {
                diskon = 20;
            } else {
                diskon = 0;
            }

            NumberFormat format =
                NumberFormat.getNumberInstance(
                    new Locale("id", "ID")
                );

            txtBiayaKursus.setText(
                "Rp " + format.format(biaya)
            );

            txtDiskon.setText(diskon + "");     
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtKodeKursus = new javax.swing.JTextField();
        cmbNamaKursus = new javax.swing.JComboBox<>();
        btnReset = new javax.swing.JButton();
        scrollHasilPendaftaran = new javax.swing.JScrollPane();
        txtHasilPendaftaran = new javax.swing.JTextArea();
        lblKodeKursus = new javax.swing.JLabel();
        btnProses = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnKeluar = new javax.swing.JButton();
        lblKodeKursus1 = new javax.swing.JLabel();
        lblLevel = new javax.swing.JLabel();
        cmbLevelKursus = new javax.swing.JComboBox<>();
        lblBiayaKursus = new javax.swing.JLabel();
        txtBiayaKursus = new javax.swing.JTextField();
        lblDiskon = new javax.swing.JLabel();
        txtDiskon = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 204, 255));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));

        txtKodeKursus.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        txtKodeKursus.setToolTipText("");
        txtKodeKursus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeKursusActionPerformed(evt);
            }
        });

        cmbNamaKursus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Java", "Data Science", "UI/UX" }));
        cmbNamaKursus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbNamaKursusActionPerformed(evt);
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

        txtHasilPendaftaran.setColumns(20);
        txtHasilPendaftaran.setRows(5);
        scrollHasilPendaftaran.setViewportView(txtHasilPendaftaran);

        lblKodeKursus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblKodeKursus.setForeground(new java.awt.Color(117, 84, 57));
        lblKodeKursus.setText("Kode Kursus");

        btnProses.setBackground(new java.awt.Color(51, 0, 153));
        btnProses.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnProses.setForeground(new java.awt.Color(255, 255, 255));
        btnProses.setText("PROSES");
        btnProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProsesActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Perpetua", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(117, 84, 57));
        jLabel5.setText("FORM PENDAFTARAN");

        btnKeluar.setBackground(new java.awt.Color(153, 0, 0));
        btnKeluar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnKeluar.setForeground(new java.awt.Color(255, 255, 255));
        btnKeluar.setText("KELUAR");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        lblKodeKursus1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblKodeKursus1.setForeground(new java.awt.Color(117, 84, 57));
        lblKodeKursus1.setText("Nama Kursus");

        lblLevel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblLevel.setForeground(new java.awt.Color(117, 84, 57));
        lblLevel.setText("Level");

        cmbLevelKursus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Basic", "Intermediate", "Advanced" }));
        cmbLevelKursus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLevelKursusActionPerformed(evt);
            }
        });

        lblBiayaKursus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblBiayaKursus.setForeground(new java.awt.Color(117, 84, 57));
        lblBiayaKursus.setText("Biaya Kursus");

        lblDiskon.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDiskon.setForeground(new java.awt.Color(117, 84, 57));
        lblDiskon.setText("Diskon (%)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollHasilPendaftaran, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKodeKursus)
                            .addComponent(lblKodeKursus1)
                            .addComponent(lblLevel)
                            .addComponent(lblBiayaKursus)
                            .addComponent(lblDiskon))
                        .addGap(75, 75, 75)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtKodeKursus)
                            .addComponent(cmbNamaKursus, 0, 310, Short.MAX_VALUE)
                            .addComponent(cmbLevelKursus, 0, 310, Short.MAX_VALUE)
                            .addComponent(txtBiayaKursus)
                            .addComponent(txtDiskon))))
                .addGap(0, 66, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(117, 117, 117))
            .addGroup(layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnProses, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(107, 107, 107))
            .addGroup(layout.createSequentialGroup()
                .addGap(236, 236, 236)
                .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKodeKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKodeKursus))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKodeKursus1)
                    .addComponent(cmbNamaKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbLevelKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLevel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBiayaKursus)
                    .addComponent(txtBiayaKursus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDiskon))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset)
                    .addComponent(btnProses))
                .addGap(37, 37, 37)
                .addComponent(btnKeluar)
                .addGap(18, 18, 18)
                .addComponent(scrollHasilPendaftaran, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKodeKursusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeKursusActionPerformed

    }//GEN-LAST:event_txtKodeKursusActionPerformed

    private void cmbNamaKursusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNamaKursusActionPerformed
        aturBiayaDanDiskon();
    }//GEN-LAST:event_cmbNamaKursusActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
       int pilihan = JOptionPane.showConfirmDialog(
            this,
            "Kosongkan seluruh input??",
            "Konfirmasi Reset",
            JOptionPane.YES_NO_OPTION
        );

        if (pilihan == JOptionPane.YES_OPTION) {
            txtKodeKursus.setText("");
            cmbNamaKursus.setSelectedIndex(0);
            cmbLevelKursus.setSelectedIndex(0);
            txtHasilPendaftaran.setText("");

            aturBiayaDanDiskon();

            txtKodeKursus.requestFocus();
        }
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProsesActionPerformed
        String kode = txtKodeKursus.getText().trim();

        if (kode.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Kode kursus wajib diisi!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            txtKodeKursus.requestFocus();
            return;
        }

        String namaKursus = String.valueOf(
            cmbNamaKursus.getSelectedItem()
        );

        String level = String.valueOf(
            cmbLevelKursus.getSelectedItem()
        );

        double biaya;

        switch (namaKursus) {
            case "Java":
                biaya = 100000;
                break;
            case "Data Science":
                biaya = 200000;
                break;
            case "UI/UX":
                biaya = 300000;
                break;
            default:
                biaya = 0;
                break;
        }

        int diskon;

        if ("Basic".equalsIgnoreCase(level)) {
            diskon = 0;
        } else if ("Intermediate".equalsIgnoreCase(level)) {
            diskon = 10;
        } else if ("Advanced".equalsIgnoreCase(level)) {
            diskon = 20;
        } else {
            diskon = 0;
        }

        double potongan = biaya * diskon / 100.0;
        double totalBayar = biaya - potongan;

        NumberFormat format =
            NumberFormat.getNumberInstance(new Locale("id", "ID"));

        txtHasilPendaftaran.setText(
            "====================================\n"
            + "       HASIL PENDAFTARAN KURSUS\n"
            + "====================================\n\n"
            + "Kode Kursus  : " + kode + "\n"
            + "Nama Kursus  : " + namaKursus + "\n"
            + "Level        : " + level + "\n"
            + "Biaya Kursus : Rp " + format.format(biaya) + "\n"
            + "Diskon       : " + diskon + "%\n"
            + "Potongan     : Rp " + format.format(potongan) + "\n"
            + "------------------------------------\n"
            + "Total Bayar  : Rp " + format.format(totalBayar) + "\n"
            + "===================================="
        );

        txtHasilPendaftaran.setCaretPosition(0);
    }//GEN-LAST:event_btnProsesActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        int pilihan = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin keluar?",
            "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION
        );

        if (pilihan == JOptionPane.YES_OPTION) {
            dispose();
        }
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void cmbLevelKursusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLevelKursusActionPerformed
        aturBiayaDanDiskon();
    }//GEN-LAST:event_cmbLevelKursusActionPerformed

    public static void main(String args[]) {

        try {
            for (
                javax.swing.UIManager.LookAndFeelInfo info
                : javax.swing.UIManager.getInstalledLookAndFeels()
            ) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(
                        info.getClassName()
                    );
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FormPendaftaranAwal.class.getName()
            ).log(
                java.util.logging.Level.SEVERE,
                null,
                ex
            );
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new FormPendaftaranAwal().setVisible(true);
                }
            }
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnProses;
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cmbLevelKursus;
    private javax.swing.JComboBox<String> cmbNamaKursus;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel lblBiayaKursus;
    private javax.swing.JLabel lblDiskon;
    private javax.swing.JLabel lblKodeKursus;
    private javax.swing.JLabel lblKodeKursus1;
    private javax.swing.JLabel lblLevel;
    private javax.swing.JScrollPane scrollHasilPendaftaran;
    private javax.swing.JTextField txtBiayaKursus;
    private javax.swing.JTextField txtDiskon;
    private javax.swing.JTextArea txtHasilPendaftaran;
    private javax.swing.JTextField txtKodeKursus;
    // End of variables declaration//GEN-END:variables
}
