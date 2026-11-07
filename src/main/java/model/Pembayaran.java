package model;

import java.math.BigDecimal;
import java.sql.Date;

public class Pembayaran {
    private int idPembayaran;
    private int idPendaftaran;
    private Date tanggalBayar;
    private BigDecimal nominal;
    private String metode;
    private String status;
    private String namaPeserta;
    private String namaKelas;

    public int getIdPembayaran() { return idPembayaran; }
    public void setIdPembayaran(int idPembayaran) { this.idPembayaran = idPembayaran; }
    public int getIdPendaftaran() { return idPendaftaran; }
    public void setIdPendaftaran(int idPendaftaran) { this.idPendaftaran = idPendaftaran; }
    public Date getTanggalBayar() { return tanggalBayar; }
    public void setTanggalBayar(Date tanggalBayar) {
        if (tanggalBayar == null) throw new IllegalArgumentException("Tanggal bayar wajib diisi");
        this.tanggalBayar = tanggalBayar;
    }
    public BigDecimal getNominal() { return nominal; }
    public void setNominal(BigDecimal nominal) {
        if (nominal == null || nominal.signum() <= 0) throw new IllegalArgumentException("Nominal harus lebih dari 0");
        this.nominal = nominal;
    }
    public String getMetode() { return metode; }
    public void setMetode(String metode) {
        String value = metode == null ? "" : metode.trim().toUpperCase();
        if (!("TUNAI".equals(value) || "TRANSFER".equals(value) || "QRIS".equals(value))) throw new IllegalArgumentException("Metode pembayaran tidak valid");
        this.metode = value;
    }
    public String getStatus() { return status; }
    public void setStatus(String status) {
        String value = status == null ? "" : status.trim().toUpperCase();
        if (!("MENUNGGU".equals(value) || "LUNAS".equals(value) || "BATAL".equals(value))) throw new IllegalArgumentException("Status pembayaran tidak valid");
        this.status = value;
    }
    public String getNamaPeserta() { return namaPeserta; }
    public void setNamaPeserta(String namaPeserta) { this.namaPeserta = namaPeserta; }
    public String getNamaKelas() { return namaKelas; }
    public void setNamaKelas(String namaKelas) { this.namaKelas = namaKelas; }
}