package view;

import javax.swing.JTextArea;

public class txtHasil {

    private JTextArea areaHasil;

    public txtHasil(JTextArea areaHasil) {
        if (areaHasil == null) {
            throw new IllegalArgumentException("Area hasil tidak boleh kosong");
        }
        this.areaHasil = areaHasil;
    }

    public void tampilkanHasil(String hasil) {
        areaHasil.setText(hasil);
        areaHasil.setCaretPosition(0);
    }

    public void kosongkanHasil() {
        areaHasil.setText("");
    }
}