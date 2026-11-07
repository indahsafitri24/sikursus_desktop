package model;

public class Kursus {

    private int idKursus;
    private String kode;
    private String nama;
    private String level;
    private double biaya;

    public Kursus() {
        this.idKursus = 0;
    }

    // Constructor lama
    public Kursus(String kode, String nama,
                  String level, double biaya) {

        this(0, kode, nama, level, biaya);
    }

    // Constructor untuk database dan DAO
    public Kursus(int idKursus, String kode,
                  String nama, String level, double biaya) {

        setIdKursus(idKursus);
        setKode(kode);
        setNama(nama);
        setLevel(level);
        setBiaya(biaya);
    }

    public int getIdKursus() {
        return idKursus;
    }

    public void setIdKursus(int idKursus) {
        if (idKursus < 0) {
            throw new IllegalArgumentException(
                "ID kursus tidak boleh negatif"
            );
        }
        this.idKursus = idKursus;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Kode kursus wajib diisi"
            );
        }
        this.kode = kode.trim();
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        if (level == null || level.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Level kursus wajib diisi"
            );
        }
        this.level = level.trim();
    }

    public double getBiaya() {
        return biaya;
    }

    public void setBiaya(double biaya) {
        if (biaya < 0) {
            throw new IllegalArgumentException(
                "Biaya tidak boleh negatif"
            );
        }
        this.biaya = biaya;
    }

    public double hitungBiayaSetelahDiskon(
            double persenDiskon) {

        if (persenDiskon < 0 || persenDiskon > 100) {
            throw new IllegalArgumentException(
                "Diskon harus berada pada rentang 0 sampai 100"
            );
        }

        return biaya - (biaya * persenDiskon / 100.0);
    }

    @Override
    public String toString() {
        return kode + " - " + nama + " (" + level + ")";
    }
}