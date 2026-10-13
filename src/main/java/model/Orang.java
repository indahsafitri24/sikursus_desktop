package model;

public class Orang {

    private int id;
    private String nama;
    private String noHp;

    public Orang(int id, String nama, String noHp) {
        setId(id);
        setNama(nama);
        setNoHp(noHp);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException(
                    "ID tidak boleh negatif"
            );
        }
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Nama wajib diisi"
            );
        }
        this.nama = nama.trim();
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        if (noHp == null || !noHp.matches("[0-9]+")) {
            throw new IllegalArgumentException(
                    "Nomor HP hanya boleh berisi angka"
            );
        }
        this.noHp = noHp;
    }

    public String getInfo() {
        return "ID: " + id
                + " | Nama: " + nama
                + " | No. HP: " + noHp;
    }
}