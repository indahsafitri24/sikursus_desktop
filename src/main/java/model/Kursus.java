package model;

public class Kursus {

    private String nama;
    private double biaya;

    public Kursus() {
    }

    public Kursus(String nama, double biaya) {
        this.nama = nama;
        this.biaya = biaya;
    }

    public double hitungTotal(int jumlah) {
        return biaya * jumlah;
    }

    public String getNama() {
        return nama;
    }

    public double getBiaya() {
        return biaya;
    }
}