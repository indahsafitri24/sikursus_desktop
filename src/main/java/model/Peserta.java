package model;

public class Peserta extends Orang {

    private String nim;
    private String prodi;

    public Peserta(int id, String nama, String noHp,
                   String nim, String prodi) {

        super(id, nama, noHp);
        setNim(nim);
        setProdi(prodi);
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        if (nim == null || nim.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "NIM wajib diisi"
            );
        }
        this.nim = nim.trim();
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        if (prodi == null || prodi.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Program studi wajib diisi"
            );
        }
        this.prodi = prodi.trim();
    }

    @Override
    public String getInfo() {
        return "[Peserta] " + super.getInfo()
                + " | NIM: " + nim
                + " | Prodi: " + prodi;
    }
}