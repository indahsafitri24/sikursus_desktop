
package latihan;

import java.util.Scanner;
import model.Kursus;

public class biayakursus {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println("       SISTEM KASIR KURSUS IT");
        System.out.println("======================================");

        System.out.print(
            "Masukkan Kode Kursus "
            + "(JAVA-BSC / WEB-ADV / DS-BSC / UIUX-BSC): "
        );

        String kode = input.nextLine().trim().toUpperCase();

        if (kode.isEmpty()) {
            System.out.println("ERROR: Kode kursus wajib diisi.");
            input.close();
            return;
        }

        String namaKursus;
        String level;
        double biayaDasar;

        if (kode.startsWith("JAVA")) {
            namaKursus = "Java Desktop";
            level = "BASIC";
            biayaDasar = 500000;

        } else if (kode.startsWith("WEB")) {
            namaKursus = "Web Programming";
            level = "ADVANCED";
            biayaDasar = 450000;

        } else if (kode.startsWith("DS")) {
            namaKursus = "Data Science";
            level = "BASIC";
            biayaDasar = 650000;

        } else if (kode.startsWith("UIUX")) {
            namaKursus = "UI/UX Design";
            level = "BASIC";
            biayaDasar = 400000;

        } else {
            System.out.println("ERROR: Kode kursus tidak dikenali.");
            input.close();
            return;
        }

        Kursus kursus = new Kursus(
            kode,
            namaKursus,
            level,
            biayaDasar
        );

        int jumlah;

        // Input jumlah kursus dan validasi angka
        while (true) {
            try {
                System.out.print("Masukkan Jumlah Kursus: ");

                jumlah = Integer.parseInt(
                    input.nextLine().trim()
                );

                if (jumlah <= 0) {
                    System.out.println(
                        "ERROR: Jumlah kursus harus lebih dari 0."
                    );
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println(
                    "ERROR: Jumlah harus berupa angka bulat."
                );

                // Membersihkan sisa input yang tidak valid
                input.nextLine();
            }
        }

        double totalDasar = kursus.getBiaya() * jumlah;

        double biayaFasilitas = hitungBiayaFasilitas(kode);

        double totalKotor = totalDasar + biayaFasilitas;

        double persenDiskon = hitungPersenDiskon(totalKotor);

        double totalSetelahDiskon =
            kursus.hitungBiayaSetelahDiskon(persenDiskon * 100);

        double totalAkhir =
            totalSetelahDiskon * jumlah + biayaFasilitas
            - (biayaFasilitas * persenDiskon);

        double potongan = totalKotor - totalAkhir;

        String status = tentukanStatusHarga(totalAkhir);

        // Menampilkan nota
        System.out.println();
        System.out.println("======================================");
        System.out.println("              NOTA KURSUS");
        System.out.println("======================================");

        System.out.println("Kode Kursus      : " + kursus.getKode());
        System.out.println("Nama Kursus      : " + kursus.getNama());
        System.out.println("Level Kursus     : " + kursus.getLevel());
        System.out.println("Jumlah Kursus    : " + jumlah);

        System.out.printf(
            "Biaya Satuan     : Rp%,.0f%n",
            kursus.getBiaya()
        );

        System.out.printf(
            "Total Biaya      : Rp%,.0f%n",
            totalDasar
        );

        System.out.printf(
            "Biaya Fasilitas  : Rp%,.0f%n",
            biayaFasilitas
        );

        System.out.printf(
            "Total Kotor      : Rp%,.0f%n",
            totalKotor
        );

        System.out.printf(
            "Diskon           : %.0f%%%n",
            persenDiskon * 100
        );

        System.out.printf(
            "Potongan         : Rp%,.0f%n",
            potongan
        );

        System.out.println("--------------------------------------");

        System.out.printf(
            "TOTAL BAYAR      : Rp%,.0f%n",
            totalAkhir
        );

        System.out.println("Status           : " + status);
        System.out.println("======================================");

        input.close();
    }

    public static double hitungBiayaFasilitas(String kode) {

        if (kode.startsWith("JAVA")) {
            return 50000;

        } else if (kode.startsWith("WEB")) {
            return 75000;

        } else if (kode.startsWith("DS")) {
            return 75000;

        } else if (kode.startsWith("UIUX")) {
            return 40000;

        } else {
            return 0;
        }
    }

    public static double hitungPersenDiskon(double totalKotor) {

        if (totalKotor < 0) {
            throw new IllegalArgumentException(
                "Total biaya tidak boleh negatif"
            );
        }

        if (totalKotor >= 3000000) {
            return 0.15;

        } else if (totalKotor >= 1500000) {
            return 0.10;

        } else {
            return 0.05;
        }
    }

    public static String tentukanStatusHarga(double totalAkhir) {

        if (totalAkhir < 0) {
            throw new IllegalArgumentException(
                "Total akhir tidak boleh negatif"
            );
        }

        if (totalAkhir >= 2500000) {
            return "PREMIUM/MAHAL";

        } else if (totalAkhir >= 1500000) {
            return "STANDAR";

        } else {
            return "EKONOMIS/TERJANGKAU";
        }
    }
}
