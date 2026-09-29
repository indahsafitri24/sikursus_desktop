package app;

import java.util.ArrayList;
import model.Orang;
import model.Peserta;
import model.Instruktur;

public class DemoInheritance {

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

        System.out.println("=== DATA SIKURSUS ===");

        for (Orang orang : daftarOrang) {
            System.out.println(orang.getInfo());
        }

        System.out.println();
        System.out.println("Jumlah object: " + daftarOrang.size());

        // Pengujian setter dari parent
        Peserta pesertaUji = new Peserta(
                4,
                "Nama Lama",
                "085261384670",
                "2924004",
                "informatika"
        );

        pesertaUji.setNama("indah cantik");

        System.out.println();
        System.out.println("=== HASIL UJI SETTER ===");
        System.out.println(pesertaUji.getInfo());
    }
}