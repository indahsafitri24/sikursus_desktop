package model;

public class Instruktur extends Orang {

    private String keahlian;
    private String email;

    // Constructor kosong untuk kebutuhan Form dan DAO
    public Instruktur() {
        super(0, "-", "");
        this.keahlian = "-";
        this.email = "";
    }

    // Constructor lama
    public Instruktur(int id, String nama,
                     String noHp, String keahlian) {

        super(id, nama, noHp);

        setKeahlian(keahlian);
        setEmail("");
    }

    // Constructor untuk database
    public Instruktur(int id, String nama,
                      String noHp, String keahlian,
                      String email) {

        super(id, nama, noHp);

        setKeahlian(keahlian);
        setEmail(email);
    }

    public String getKeahlian() {
        return keahlian;
    }

    public void setKeahlian(String keahlian) {
        if (keahlian == null
                || keahlian.trim().isEmpty()) {

            throw new IllegalArgumentException(
                "Keahlian wajib diisi"
            );
        }

        this.keahlian = keahlian.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? "" : email.trim();
    }

    @Override
    public String getInfo() {
        return "[Instruktur] " + super.getInfo()
                + " | Keahlian: " + keahlian;
    }
}