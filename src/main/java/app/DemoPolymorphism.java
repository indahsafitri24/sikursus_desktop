package app;

import java.util.ArrayList;
import java.util.List;
import model.Admin;
import model.Instruktur;
import model.Orang;
import model.Peserta;

public class DemoPolymorphism {

    public static void main(String[] args) {

        Peserta peserta1 = new Peserta(
                1, "indah safitri", "085261384670",
                "2924004", "informatika");

        Peserta peserta2 = new Peserta(
                2, "nur syifa", "085362604055",
                "2625063", "bimbingan dan konseling");

        Peserta peserta3 = new Peserta(
                3, "ahmad martondi", "082177902583",
                "2924005", "informatika");

        Instruktur instruktur1 = new Instruktur(
                101, "indah safitri", "085261384670",
                "Java Desktop");

        Instruktur instruktur2 = new Instruktur(
                102, "nur syifa", "085362604055",
                "Data Science");

        Admin admin = new Admin(
                201, "indah safitri", "085261384670",
                "Pengelola Sistem");

        List<Orang> daftarOrang = new ArrayList<Orang>();
        daftarOrang.add(peserta1);
        daftarOrang.add(peserta2);
        daftarOrang.add(peserta3);
        daftarOrang.add(instruktur1);
        daftarOrang.add(instruktur2);
        daftarOrang.add(admin);

        System.out.println("=== LOOP POLYMORPHISM ===");
        cetakSemua(daftarOrang);

        System.out.println("\n=== POLYMORPHIC ARGUMENT ===");

        tampilkanInfo(peserta1);
        tampilkanInfo(peserta2);
        tampilkanInfo(peserta3);
        tampilkanInfo(instruktur1);
        tampilkanInfo(instruktur2);
        tampilkanInfo(admin);

        System.out.println("\n=== CEK JENIS OBJECT ===");

        for (Orang orang : daftarOrang) {
            if (orang instanceof Peserta) {
                Peserta p = (Peserta) orang;

                System.out.println(
                        orang.getNama() + " adalah Peserta"
                );
                System.out.println(
                        "NIM khusus peserta: " + p.getNim()
                );

            } else if (orang instanceof Instruktur) {
                System.out.println(
                        orang.getNama() + " adalah Instruktur"
                );

            } else if (orang instanceof Admin) {
                System.out.println(
                        orang.getNama() + " adalah Admin"
                );
            }
        }

        System.out.println("\nJumlah object: " + daftarOrang.size());
    }

    public static void tampilkanInfo(Orang orang) {
        System.out.println(orang.getInfo());
    }

    public static void cetakSemua(List<Orang> daftar) {
        for (Orang orang : daftar) {
            System.out.println(orang.getInfo());
        }
    }
}