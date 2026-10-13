package app;

import model.Instruktur;
import model.Kursus;
import model.Peserta;

public class UjiValidasi {

    private interface Aksi {
        void jalankan();
    }

    public static void main(String[] args) {

        int pass = 0;
        int total = 9;

        // 1. Peserta valid
        pass += harusBerhasil("Peserta valid", new Aksi() {
            @Override
            public void jalankan() {
                new Peserta(
                        1,
                        "ndaahh",
                        "085261384670",
                        "2924004",
                        "Informatika"
                );
            }
        });

        // 2. Instruktur valid
        pass += harusBerhasil("Instruktur valid", new Aksi() {
            @Override
            public void jalankan() {
                new Instruktur(
                        2,
                        "syifa",
                        "085362604055",
                        "Java Desktop"
                );
            }
        });

        // 3. ID negatif
        pass += harusDitolak("ID negatif", new Aksi() {
            @Override
            public void jalankan() {
                new Peserta(
                        -1,
                        "ndaahh",
                        "085261384670",
                        "2924004",
                        "Informatika"
                );
            }
        });

        // 4. Nama kosong
        pass += harusDitolak("Nama kosong", new Aksi() {
            @Override
            public void jalankan() {
                new Peserta(
                        1,
                        "",
                        "085261384670",
                        "2924004",
                        "Informatika"
                );
            }
        });

        // 5. Nomor HP berisi huruf
        pass += harusDitolak("No HP huruf", new Aksi() {
            @Override
            public void jalankan() {
                new Peserta(
                        1,
                        "ndaahh",
                        "telepon",
                        "2924004",
                        "Informatika"
                );
            }
        });

        // 6. NIM kosong
        pass += harusDitolak("NIM kosong", new Aksi() {
            @Override
            public void jalankan() {
                new Peserta(
                        1,
                        "ndaahh",
                        "085261384670",
                        "",
                        "Informatika"
                );
            }
        });

        // 7. Prodi kosong
        pass += harusDitolak("Prodi kosong", new Aksi() {
            @Override
            public void jalankan() {
                new Peserta(
                        1,
                        "ndaahh",
                        "085261384670",
                        "2924004",
                        ""
                );
            }
        });

        // 8. Keahlian kosong
        pass += harusDitolak("Keahlian kosong", new Aksi() {
            @Override
            public void jalankan() {
                new Instruktur(
                        2,
                        "syifa",
                        "085362604055",
                        ""
                );
            }
        });

        // 9. Biaya negatif
        pass += harusDitolak("Biaya negatif", new Aksi() {
            @Override
            public void jalankan() {
                Kursus k = new Kursus();
                k.setBiaya(-100000);
            }
        });

        System.out.println();
        System.out.println("================================");
        System.out.println("HASIL: " + pass + "/" + total
                + " TEST PASS");
        System.out.println("================================");

        if (pass != total) {
            throw new IllegalStateException(
                    "Masih ada test validasi yang gagal"
            );
        }
    }

    private static int harusBerhasil(
            String nama,
            Aksi aksi) {

        try {

            aksi.jalankan();

            System.out.println("PASS | " + nama);

            return 1;

        } catch (RuntimeException ex) {

            System.out.println(
                    "FAIL | " + nama
                    + " | " + ex.getMessage()
            );

            return 0;
        }
    }

    private static int harusDitolak(
            String nama,
            Aksi aksi) {

        try {

            aksi.jalankan();

            System.out.println(
                    "FAIL | " + nama
                    + " | Input tidak ditolak"
            );

            return 0;

        } catch (IllegalArgumentException ex) {

            System.out.println(
                    "PASS | " + nama
                    + " | DITOLAK: " + ex.getMessage()
            );

            return 1;
        }
    }
}