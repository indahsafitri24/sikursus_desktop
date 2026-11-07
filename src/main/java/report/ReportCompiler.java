package report;

import java.io.File;
import net.sf.jasperreports.engine.JasperCompileManager;

public class ReportCompiler {

    private static final String[] REPORTS = {

        "laporan_peserta",
        "laporan_pendaftaran_semua",
        "laporan_pendaftaran",
        "laporan_pembayaran_semua",
        "laporan_pembayaran"

    };

    public static void main(String[] args)
            throws Exception {

        File dir = new File(
            "src/main/resources/report"
        );

        if (!dir.isDirectory()) {

            throw new IllegalStateException(
                "Folder report tidak ditemukan: "
                + dir.getAbsolutePath()
            );
        }

        for (String name : REPORTS) {

            File src = new File(
                dir,
                name + ".jrxml"
            );

            File dst = new File(
                dir,
                name + ".jasper"
            );

            if (!src.isFile()) {

                System.out.println(
                    "File tidak ditemukan: "
                    + src.getName()
                );

                continue;
            }

            JasperCompileManager.compileReportToFile(
                src.getAbsolutePath(),
                dst.getAbsolutePath()
            );

            System.out.println(
                "OK: " + dst.getPath()
            );
        }

        System.out.println(
            "Proses kompilasi report selesai."
        );
    }
}