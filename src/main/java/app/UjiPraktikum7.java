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

                try {
                    Method biayaDasar =
                            method("hitungBiayaDasar", String.class);

                    Method biayaFasilitas =
                            method("hitungBiayaFasilitas");

                    Method jenisPeserta =
                            method("getJenisPeserta");

                    Method ringkasanFasilitas =
                            method("buatRingkasanFasilitas");

                    JRadioButton rbMahasiswa =
                            field(form, "rbMahasiswa", JRadioButton.class);

                    JRadioButton rbUmum =
                            field(form, "rbUmum", JRadioButton.class);

                    JComboBox<?> cmbKursus =
                            field(form, "cmbKursus", JComboBox.class);

                    JCheckBox cbModul =
                            field(form, "cbModul", JCheckBox.class);

                    JCheckBox cbSertifikat =
                            field(form, "cbSertifikat", JCheckBox.class);

                    JCheckBox cbRekaman =
                            field(form, "cbRekaman", JCheckBox.class);

                    int pass = 0;
                    int total = 3;

                    System.out.println("==================================");
                    System.out.println("     UJI PRAKTIKUM 7 SIKURSUS");
                    System.out.println("==================================");

                    // Skenario 1: Mahasiswa + Java Desktop
                    // Modul + Sertifikat = Rp575.000
                    rbMahasiswa.setSelected(true);
                    rbUmum.setSelected(false);
                    cmbKursus.setSelectedItem("Java Desktop");
                    cbModul.setSelected(true);
                    cbSertifikat.setSelected(true);
                    cbRekaman.setSelected(false);

                    pass += cekSkenario(
                            form,
                            biayaDasar,
                            biayaFasilitas,
                            jenisPeserta,
                            ringkasanFasilitas,
                            "Skenario 1 - Mahasiswa - Java Desktop",
                            575000
                    );

                    // Skenario 2: Umum + Data Science
                    // Rekaman = Rp690.000
                    rbMahasiswa.setSelected(false);
                    rbUmum.setSelected(true);
                    cmbKursus.setSelectedItem("Data Science");
                    cbModul.setSelected(false);
                    cbSertifikat.setSelected(false);
                    cbRekaman.setSelected(true);

                    pass += cekSkenario(
                            form,
                            biayaDasar,
                            biayaFasilitas,
                            jenisPeserta,
                            ringkasanFasilitas,
                            "Skenario 2 - Umum - Data Science",
                            690000
                    );

                    cmbKursus.setSelectedItem("UI/UX Design");
                    cbModul.setSelected(false);
                    cbSertifikat.setSelected(false);
                    cbRekaman.setSelected(false);

                    pass += cekSkenario(
                            form,
                            biayaDasar,
                            biayaFasilitas,
                            jenisPeserta,
                            ringkasanFasilitas,
                            "Skenario 3 - Umum - UI/UX Design",
                            400000
                    );

                    System.out.println("----------------------------------");
                    System.out.println(
                            "HASIL: " + pass + "/" + total
                            + " TEST PASS"
                    );
                    System.out.println("==================================");

                    hasil[0] = pass;

                } catch (Exception ex) {
                    System.out.println("ERROR: " + ex.toString());
                    ex.printStackTrace();

                } finally {
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
            Method biayaDasar,
            Method biayaFasilitas,
            Method jenisPeserta,
            Method ringkasanFasilitas,
            String namaSkenario,
            double biayaHarapan) {

        try {
            JComboBox<?> cmbKursus =
                    field(form, "cmbKursus", JComboBox.class);

            String kursus =
                    cmbKursus.getSelectedItem().toString();

            String jenis =
                    (String) jenisPeserta.invoke(form);

            double dasar =
                    ((Number) biayaDasar.invoke(form, kursus))
                    .doubleValue();

            double fasilitas =
                    ((Number) biayaFasilitas.invoke(form))
                    .doubleValue();

            double diskon = "Mahasiswa".equals(jenis)
                    ? dasar * 0.10
                    : 0;

            double total = dasar - diskon + fasilitas;

            boolean hasilBenar =
                    Math.abs(total - biayaHarapan) < 0.001;

            boolean tanpaFasilitas =
                    !field(form, "cbModul", JCheckBox.class).isSelected()
                    && !field(form, "cbSertifikat", JCheckBox.class).isSelected()
                    && !field(form, "cbRekaman", JCheckBox.class).isSelected();

            if (tanpaFasilitas) {
                String ringkasan =
                        (String) ringkasanFasilitas.invoke(form);

                hasilBenar = hasilBenar
                        && ringkasan.contains(
                                "Tidak ada fasilitas tambahan"
                        );
            }

            if (hasilBenar) {
                System.out.println(
                        "PASS | " + namaSkenario
                        + " | Total: Rp " + (long) total
                );
                return 1;

            } else {
                System.out.println(
                        "FAIL | " + namaSkenario
                        + " | Total: Rp " + (long) total
                        + " | Harapan: Rp " + (long) biayaHarapan
                );
                return 0;
            }

        } catch (Exception ex) {
            System.out.println(
                    "FAIL | " + namaSkenario
                    + " | " + ex.toString()
            );
            ex.printStackTrace();
            return 0;
        }
    }

    private static Method method(
            String nama,
            Class<?>... parameter) throws Exception {

        Method m = FormPendaftaranKursus.class
                .getDeclaredMethod(nama, parameter);

        m.setAccessible(true);
        return m;
    }

    private static <T> T field(
            Object object,
            String nama,
            Class<T> tipe) throws Exception {

        Field f = object.getClass().getDeclaredField(nama);
        f.setAccessible(true);
        return tipe.cast(f.get(object));
    }
}