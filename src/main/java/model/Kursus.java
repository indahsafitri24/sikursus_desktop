package model;

public class Kursus {

    private String nama;
    private double biaya;

    public Kursus() {
        this.nama = "";
        this.biaya = 0;
    }

    public Kursus(String nama, double biaya) {
        setNama(nama);
        setBiaya(biaya);
    }

    public double hitungTotal(int jumlah) {
        if (jumlah < 0) {
            throw new IllegalArgumentException(
                    "Jumlah kursus tidak boleh negatif"
            );
        }
        return biaya * jumlah;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Nama kursus wajib diisi"
            );
        }
        this.nama = nama.trim();
    }

    public double getBiaya() {
        return biaya;
    }

    public void setBiaya(double biaya) {
        if (biaya < 0
                || Double.isNaN(biaya)
                || Double.isInfinite(biaya)) {
            throw new IllegalArgumentException(
                    "Biaya kursus tidak boleh negatif atau tidak valid"
            );
        }
        this.biaya = biaya;
    }
}