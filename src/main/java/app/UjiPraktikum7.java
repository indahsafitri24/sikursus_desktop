package app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import view.FormPendaftaranKursus;

public class UjiPraktikum7 {

    public static void main(String[] args) throws Exception {

        final int[] hasil = {0};

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {

                FormPendaftaranKursus form =
                        new FormPendaftaranKursus();

                System.out.println("==================================");
                System.out.println("     UJI PRAKTIKUM 7 SIKURSUS");
                System.out.println("==================================");

                int pass = 0;
                int total = 3;

                try {

                    // SKENARIO 1
                    setField(form, "rbMahasiswa", true);
                    setField(form, "rbUmum", false);
                    setField(form, "cmbKursus", "Java Desktop");
                    setField(form, "cbModul", true);
                    setField(form, "cbSertifikat", true);
                    setField(form, "cbRekaman", false);

                    pass += cekSkenario(
                            form,
                            "Mahasiswa - Java Desktop",
                            575000
                    );

                    // SKENARIO 2
                    setField(form, "rbMahasiswa", false);
                    setField(form, "rbUmum", true);
                    setField(form, "cmbKursus", "Data Science");
                    setField(form, "cbModul", false);
                    setField(form, "cbSertifikat", false);
                    setField(form, "cbRekaman", true);

                    pass += cekSkenario(
                            form,
                            "Umum - Data Science",
                            690000
                    );

                    // SKENARIO 3
                    setField(form, "rbMahasiswa", false);
                    setField(form, "rbUmum", true);
                    setField(form, "cmbKursus", "UI/UX Design");
                    setField(form, "cbModul", false);
                    setField(form, "cbSertifikat", false);
                    setField(form, "cbRekaman", false);

                    pass += cekSkenario(
                            form,
                            "Umum - UI/UX Design",
                            400000
                    );

                } catch (Exception ex) {

                    System.out.println("ERROR: " + ex.toString());
                    ex.printStackTrace();

                } finally {

                    System.out.println("----------------------------------");
                    System.out.println(
                            "HASIL: " + pass + "/" + total
                            + " TEST PASS"
                    );
                    System.out.println("==================================");

                    hasil[0] = pass;
                    form.dispose();
                }
            }
        });

        if (hasil[0] != 3) {
            throw new IllegalStateException(
                    "Pengujian belum berhasil seluruhnya"
            );
        }
    }

    private static int cekSkenario(
            FormPendaftaranKursus form,
            String nama,
            double biayaHarapan) {

        try {

            String kursus = getKursus(form);

            double dasar = ((Number) panggilMethod(
                    form,
                    "hitungBiayaDasar",
                    new Class[]{String.class},
                    new Object[]{kursus}
            )).doubleValue();

            double fasilitas = ((Number) panggilMethod(
                    form,
                    "hitungBiayaFasilitas",
                    new Class[]{},
                    new Object[]{}
            )).doubleValue();

            String jenis = (String) panggilMethod(
                    form,
                    "getJenisPeserta",
                    new Class[]{},
                    new Object[]{}
            );

            String ringkasan = (String) panggilMethod(
                    form,
                    "buatRingkasanFasilitas",
                    new Class[]{},
                    new Object[]{}
            );

            double diskon = 0;

            if ("Mahasiswa".equals(jenis)) {
                diskon = dasar * 0.10;
            }

            double total = dasar - diskon + fasilitas;

            boolean hasilBenar =
                    Math.abs(total - biayaHarapan) < 0.01;

            if (!getCheckbox(form, "cbModul")
                    && !getCheckbox(form, "cbSertifikat")
                    && !getCheckbox(form, "cbRekaman")) {

                hasilBenar = hasilBenar
                        && ringkasan.contains(
                                "Tidak ada fasilitas tambahan"
                        );
            }

            if (hasilBenar) {

                System.out.println(
                        "PASS | " + nama
                        + " | Total: Rp " + total
                );

                return 1;

            } else {

                System.out.println(
                        "FAIL | " + nama
                        + " | Total: Rp " + total
                        + " | Harapan: Rp " + biayaHarapan
                );

                return 0;
            }

        } catch (Exception ex) {

            System.out.println(
                    "FAIL | " + nama
                    + " | " + ex.toString()
            );

            ex.printStackTrace();

            return 0;
        }
    }

    private static String getKursus(
            FormPendaftaranKursus form) throws Exception {

        JComboBox cmb = (JComboBox) getField(
                form, "cmbKursus"
        );

        Object pilihan = cmb.getSelectedItem();

        if (pilihan == null) {
            throw new IllegalStateException(
                    "Kursus belum dipilih"
            );
        }

        return pilihan.toString();
    }

    private static boolean getCheckbox(
            FormPendaftaranKursus form,
            String nama) throws Exception {

        JCheckBox cb = (JCheckBox) getField(form, nama);

        return cb.isSelected();
    }

    private static void setField(
            FormPendaftaranKursus form,
            String nama,
            Object nilai) throws Exception {

        Object komponen = getField(form, nama);

        if (komponen instanceof JRadioButton) {

            ((JRadioButton) komponen)
                    .setSelected((Boolean) nilai);

        } else if (komponen instanceof JCheckBox) {

            ((JCheckBox) komponen)
                    .setSelected((Boolean) nilai);

        } else if (komponen instanceof JComboBox) {

            ((JComboBox) komponen)
                    .setSelectedItem(nilai);
        }
    }

    private static Object getField(
            FormPendaftaranKursus form,
            String nama) throws Exception {

        Field field = FormPendaftaranKursus.class
                .getDeclaredField(nama);

        field.setAccessible(true);

        return field.get(form);
    }

    private static Object panggilMethod(
            FormPendaftaranKursus form,
            String nama,
            Class[] parameter,
            Object[] nilai) throws Exception {

        Method method = FormPendaftaranKursus.class
                .getDeclaredMethod(nama, parameter);

        method.setAccessible(true);

        return method.invoke(form, nilai);
    }
}