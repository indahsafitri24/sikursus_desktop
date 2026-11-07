package model;

import java.sql.Date;

public class Pendaftaran {
    private int idPendaftaran;
    private int idPeserta;
    private int idKelas;
    private Date tanggalDaftar;
    private String status;
    private String nim;
    private String namaPeserta;
    private String namaKelas;
    private String namaKursus;

    public int getIdPendaftaran() { return idPendaftaran; }
    public void setIdPendaftaran(int idPendaftaran) { this.idPendaftaran = idPendaftaran; }
    public int getIdPeserta() { return idPeserta; }
    public void setIdPeserta(int idPeserta) { this.idPeserta = idPeserta; }
    public int getIdKelas() { return idKelas; }
    public void setIdKelas(int idKelas) { this.idKelas = idKelas; }
    public Date getTanggalDaftar() { return tanggalDaftar; }
    public void setTanggalDaftar(Date tanggalDaftar) {
        if (tanggalDaftar == null) throw new IllegalArgumentException("Tanggal daftar wajib diisi");
        this.tanggalDaftar = tanggalDaftar;
    }
    public String getStatus() { return status; }
    public void setStatus(String status) {
        String value = status == null ? "" : status.trim().toUpperCase();
        if (!("DAFTAR".equals(value) || "AKTIF".equals(value) || "SELESAI".equals(value) || "BATAL".equals(value))) {
            throw new IllegalArgumentException("Status pendaftaran tidak valid");
        }
        this.status = value;
    }
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    public String getNamaPeserta() { return namaPeserta; }
    public void setNamaPeserta(String namaPeserta) { this.namaPeserta = namaPeserta; }
    public String getNamaKelas() { return namaKelas; }
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }
    public String getNamaKursus() { return namaKursus; }
    public void setNamaKursus(String namaKursus) { this.namaKursus = namaKursus; }
}