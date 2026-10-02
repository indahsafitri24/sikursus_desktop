package app;

import model.Kursus;

public class DemoKursus {

    public static void main(String[] args) {

        Kursus k1 = new Kursus("Java", 100000);

        double hasil = k1.hitungTotal(5);

        System.out.println("Nama Kursus : " + k1.getNama());
        System.out.println("Biaya       : Rp " + k1.getBiaya());
        System.out.println("Jumlah      : 5 kali");
        System.out.println("Total       : Rp " + hasil);
    }
}