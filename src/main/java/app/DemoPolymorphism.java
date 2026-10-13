package app;

import java.util.ArrayList;
import java.util.List;

import model.Orang;
import model.Peserta;
import model.Instruktur;
import model.Admin;

public class DemoPolymorphism {

    public static void main(String[] args) {

        ArrayList<Orang> daftarOrang = new ArrayList<>();

        // 3 object Peserta
        daftarOrang.add(new Peserta(
                1,
                "indah safitri",
                "085261384670",
                "2924004",
                "informatika"
        ));

        daftarOrang.add(new Peserta(
                2,
                "nur syifa",
                "085362604055",
                "2625063",
                "bimbingan dan konseling"
        ));

        daftarOrang.add(new Peserta(
                3,
                "ahmad martondi",
                "082177902583",
                "2924005",
                "informatika"
        ));

        // 2 object Instruktur
        daftarOrang.add(new Instruktur(
                101,
                "indah safitri",
                "085261384670",
                "Java Desktop"
        ));

        daftarOrang.add(new Instruktur(
                102,
                "nur syifa",
                "085362604055",
                "Data Science"
        ));

        // 1 object Admin
        daftarOrang.add(new Admin(
                201,
                "indah safitri",
                "085261384670",
                "Pengelola Sistem"
        ));

        // Menampilkan seluruh data dengan polymorphism
        System.out.println("================================");
        System.out.println("      DEMO POLYMORPHISM");
        System.out.println("================================");

        cetakSemua(daftarOrang);

        // Mengecek jenis object
        System.out.println();
        System.out.println("=== CEK JENIS OBJECT ===");

        for (Orang orang : daftarOrang) {

            if (orang instanceof Peserta) {
                System.out.println(
                        orang.getNama() + " adalah Peserta"
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

        // Polymorphism melalui parameter method
        System.out.println();
        System.out.println("=== DEMO METHOD PARAMETER ===");

        for (Orang orang : daftarOrang) {
            tampilkanOrang(orang);
        }
    }

    // Method untuk mencetak seluruh data Orang
    public static void cetakSemua(List<Orang> daftar) {
        for (Orang orang : daftar) {
            System.out.println(orang.getInfo());
            System.out.println("--------------------------------");
        }
    }

    // Method menerima object dari class Orang
    public static void tampilkanOrang(Orang orang) {
        System.out.println("Data SIKURSUS:");
        System.out.println(orang.getInfo());
        System.out.println();
    }
}