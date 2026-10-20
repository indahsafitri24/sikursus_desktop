package latihan;

import java.util.Scanner;

public class luasdankelilingbidangdatar {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // Menampilkan pilihan menu
        System.out.println("=== MENU HITUNG BIDANG DATAR ===");
        System.out.println("1. Persegi Panjang");
        System.out.println("2. Lingkaran");
        System.out.print("Pilih jenis bidang datar (1/2): ");
        int pilihan = input.nextInt();

        System.out.println("--------------------------------");

        // Proses berdasarkan pilihan
        if (pilihan == 1) {

            // Nama bidang
            String namaBidang = "Persegi Panjang";

            // Input
            System.out.print("Masukkan Panjang (cm): ");
            double panjang = input.nextDouble();

            System.out.print("Masukkan Lebar (cm)  : ");
            double lebar = input.nextDouble();

            // Menghitung luas
            double luas = panjang * lebar;

            // Menghitung keliling
            double keliling = 2 * (panjang + lebar);

            // Output
            System.out.println("\n========= HASIL REKAP =========");
            System.out.println("Nama Bidang : " + namaBidang);
            System.out.println("Panjang     : " + panjang + " cm");
            System.out.println("Lebar       : " + lebar + " cm");
            System.out.println("-------------------------------");
            System.out.printf("Luas        : %.2f cm²%n", luas);
            System.out.printf("Keliling    : %.2f cm%n", keliling);

        } else if (pilihan == 2) {

            // Nama bidang
            String namaBidang = "Lingkaran";

            // Input
            System.out.print("Masukkan Jari-jari (cm): ");
            double r = input.nextDouble();

            // Menghitung luas
            double luas = Math.PI * r * r;

            // Menghitung keliling
            double keliling = 2 * Math.PI * r;

            // Output
            System.out.println("\n========= HASIL REKAP =========");
            System.out.println("Nama Bidang : " + namaBidang);
            System.out.println("Jari-jari   : " + r + " cm");
            System.out.println("-------------------------------");
            System.out.printf("Luas        : %.2f cm²%n", luas);
            System.out.printf("Keliling    : %.2f cm%n", keliling);

        } else {

            // Jika pilihan tidak valid
            System.out.println("Error: Pilihan menu tidak valid!");
        }

        System.out.println("===============================");

        input.close();
    }
}