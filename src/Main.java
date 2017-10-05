import java.util.Scanner;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.lang.Math;

public class Main {

    public final static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            //  Handle any exceptions.
        }
    }

    public static void printMenu() {
        System.out.printf("MENU : \n");
        System.out.printf("1. Solusi persamaan lanjar\n");
        System.out.printf("2. Hampiran Interpolasi\n");
        System.out.printf("3. Matriks Hilbert\n");
        System.out.printf("4. Fungsi e\n");
        System.out.printf("0. Keluar\n");
    }

    public static void linearSolution() {
        Scanner scanner = new Scanner(System.in);
        String fileName;

        System.out.printf("Pilihan metode input :\n");
        System.out.printf("1. Masukan dari Keyboard\n");
        System.out.printf("2. Masukan dari File Eksternal\n\n");

        System.out.printf("Pilih metode input :  ");


        int m = scanner.nextInt();

        while (m != 1 && m != 2) {
            System.out.printf("Input Salah\n");
            System.out.printf("Pilih metode Input :  ");
            m = scanner.nextInt();
        }

        Matrix mat = new Matrix(0, 0);

        switch (m) {
            case 1 :
                System.out.printf("\nMasukkan jumlah persamaan : ");
                int l = scanner.nextInt();

                System.out.print("Masukkan jumlah variabel : ");
                int n = scanner.nextInt();

                mat = new Matrix(l, n);

                mat.setData();

                System.out.println();
                break;
            case 2 :
                System.out.printf("\nMasukkan nama file eksternal input : ");
                fileName = scanner.next();

                //Proses Pembacaan File External
                mat.readFileSPL(fileName, mat);

                break;
        }

        System.out.println("Matriks Input : ");

        mat.printMatrix();

        System.out.println("Hasil Eliminasi Gauss : ");
        mat.gauss();
        mat.printMatrix();

        System.out.println("Hasil Eliminasi Gauss - Jordan : ");
        mat.gaussJordan();
        mat.printMatrix();

        System.out.println("Solusi : ");
        mat.solutionMatrix();

        mat.printToFile(true);

    }

    public static void interpolation() {
        Scanner scanner = new Scanner(System.in);
        String fileName;

        System.out.printf("Pilihan metode input :\n");
        System.out.printf("1. Masukan dari Keyboard\n");
        System.out.printf("2. Masukan dari File Eksternal\n\n");

        System.out.printf("Pilih metode input :  ");

        int m = scanner.nextInt();
        int l = 0;

        while (m != 1 && m != 2) {
            System.out.printf("\nMasukan Salah\n");
            System.out.printf("Pilih metode Input :  ");
            m = scanner.nextInt();
        }

        Matrix mat = new Matrix(0, 0);

        switch (m) {
            case 1 :
                System.out.print("\nMasukkan jumlah titik : ");
                l = scanner.nextInt();

                System.out.println();

                mat = new Matrix(l, l);

                mat.setDataInterpolasi(l);

                break;
            case 2 :
                System.out.printf("\nMasukkan nama file eksternal input : ");
                fileName = scanner.next();

                System.out.println();

                //Proses Pembacaan File External
                mat.readFileInterpolasi(fileName, mat);

                break;
        }

        mat.interpolasi(l);

        System.out.printf("\nApakah anda ingin mencari nilai hampiran? (y/n) : ");
        char c = scanner.next().charAt(0);

        while(c == 'y') {
            System.out.println();
            System.out.print("X = ");
            double d = scanner.nextDouble();
            System.out.printf("f(%.2f) = %.2f\n\n", d, mat.hampiranInterpolasi(d));

            System.out.printf("Apakah anda ingin mencari nilai hampiran? (y/n) : ");
            c = scanner.next().charAt(0);
        }

        System.out.println();

        mat.printToFile(false);

        System.out.println();

    }

    public static void hilbert() {
        Scanner scanner = new Scanner(System.in);
        int n;

        System.out.printf("Masukkan dimensi Matriks Hilbert n : ");
        n = scanner.nextInt();

        System.out.println();

        Matrix mat = new Matrix(n,n);
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                double d = (1.0 /(i+j+1));
                mat.changeData(i,j,d);
            }
            mat.changeData(i,n,1);
        }

        System.out.println("Matriks Input : ");

        mat.printMatrix();

        System.out.println("Hasil Eliminasi Gauss : ");
        mat.gauss();
        mat.printMatrix();

        System.out.println("Hasil Eliminasi Gauss - Jordan : ");
        mat.gaussJordan();
        mat.printMatrix();

        System.out.println("Solusi : ");
        mat.solutionMatrix();

        mat.printToFile(true);

        System.out.println();

    }

    public static double f(double d) {
        return (1.0 / (Math.exp(d) * (1 + Math.sqrt(d) + (d * d))));
    }

    public static void fungsiE() {
        Scanner scanner = new Scanner(System.in);
        double a, b;
        int n;
        double h;

        System.out.println("Fungsi yang akan dihampiri :                                        ");
        System.out.println();
        System.out.println("    f(x) =  e^(-x) / (1 + √x + x^2)                                 ");
        System.out.println();
        System.out.println("    di selang           [a , b]     | a, b  bilangan bulat positif  ");
        System.out.println("    dan jumlah titik       n        | n     bilangan bulat positif  ");
        System.out.println();

        System.out.print("Masukkan nilai a : ");
        a = scanner.nextDouble();

        System.out.print("Masukkan nilai b : ");
        b = scanner.nextDouble();

        System.out.print("Masukkan nilai n : ");
        n = scanner.nextInt();

        System.out.println();

        h = (b - a) / n;

        Matrix mat = new Matrix(n, n);
        mat.dataInterpolasi = new double[2][n+2];

        int i = 0;
        double x = a;

        while(x<=b) {
            mat.dataInterpolasi[0][i] = x;
            mat.dataInterpolasi[1][i] = f(x);
            x+=h;
            i++;
        }

        mat.interpolasi(0);

        System.out.printf("\nApakah anda ingin mencari nilai hampiran? (y/n) : ");
        char c = scanner.next().charAt(0);

        while(c == 'y') {
            System.out.println();
            System.out.print("X = ");
            double d = scanner.nextDouble();
            System.out.printf("f(%.2f) = %.2f\n\n", d, mat.hampiranInterpolasi(d));

            System.out.printf("Apakah anda ingin mencari nilai hampiran? (y/n) : ");
            c = scanner.next().charAt(0);
        }

        System.out.println();

        mat.printToFile(false);

        System.out.println();

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean jalan = true;
        char c;
        int m;

        while (jalan) {

            clearConsole();

            System.out.println("Selamat Datang di       ");
            System.out.println();

            System.out.println("  ,-.----.                                                                                                                                   ");
            System.out.println("  \\    /  \\                                 ,--,                      ____    .--.--.               ,--,                                   ");
            System.out.println("  |   :    \\                      ,---,   ,--.'|                    ,'  , `. /  /    '.           ,--.'|                                    ");
            System.out.println("  |   |  .\\ :  __  ,-.   ,---.  ,---.'|   |  | :                 ,-+-,.' _ ||  :  /`. /    ,---.  |  | :                       __  ,-.      ");
            System.out.println("  .   :  |: |,' ,'/ /|  '   ,'\\ |   | :   :  : '              ,-+-. ;   , ||;  |  |--`    '   ,'\\ :  : '       .---.         ,' ,'/ /|     ");
            System.out.println("  |   |   \\ :'  | |' | /   /   |:   : :   |  ' |      ,---.  ,--.'|'   |  |||  :  ;_     /   /   ||  ' |     /.  ./|  ,---.  '  | |' |      ");
            System.out.println("  |   : .   /|  |   ,'.   ; ,. ::     |,-.'  | |     /     \\|   |  ,', |  |, \\  \\    `. .   ; ,. :'  | |   .-' . ' | /     \\ |  |   ,'   ");
            System.out.println("  ;   | |`-' '  :  /  '   | |: :|   : '  ||  | :    /    /  |   | /  | |--'   `----.   \\'   | |: :|  | :  /___/ \\: |/    /  |'  :  /       ");
            System.out.println("  |   | ;    |  | '   '   | .; :|   |  / :'  : |__ .    ' / |   : |  | ,      __ \\  \\  |'   | .; :'  : |__.   \\  ' .    ' / ||  | '       ");
            System.out.println("  :   ' |    ;  : |   |   :    |'   : |: ||  | '.'|'   ;   /|   : |  |/      /  /`--'  /|   :    ||  | '.'|\\   \\   '   ;   /|;  : |        ");
            System.out.println("  :   : :    |  , ;    \\   \\  / |   | '/ :;  :    ;'   |  / |   | |`-'      '--'.     /  \\   \\  / ;  :    ; \\   \\  '   |  / ||  , ;    ");
            System.out.println("  |   | :     ---'      `----'  |   :    ||  ,   / |   :    |   ;/            `--'---'    `----'  |  ,   /   \\   \\ |   :    | ---'         ");
            System.out.println("  `---'.|                       /    \\  /  ---`-'   \\   \\  /'---'                                  ---`-'     '---\" \\   \\  /            ");
            System.out.println("    `---`                       `-'----'             `----'                                                          `----'                  ");


            System.out.println("                                                                         By :                                                      ");
            System.out.printf(  "                                                           13516083 / Abram Perdanaputra          \n");
            System.out.printf(  "                                                           13516086 / Dandy Arif Rahman           \n");
            System.out.printf(  "                                                           13516062 / Yusuf Rahmat Pratama        \n");

            printMenu();

            System.out.println();

            System.out.printf("Masukkan pilihan anda : ");
            m = scanner.nextInt();

            System.out.println();

            switch (m) {
                case 0 :
                    jalan = false;
                    break;
                case 1 :
                    linearSolution();
                    System.out.printf("Apakah anda ingin kembali ke menu utama? (y/n) : ");
                    c = scanner.next().charAt(0);
                    System.out.println();

                    if(c == 'n') {
                        jalan = false;
                    }
                    break;
                case 2 :
                    interpolation();
                    System.out.printf("Apakah anda ingin kembali ke menu utama? (y/n) : ");
                    c = scanner.next().charAt(0);
                    System.out.println();

                    if(c == 'n') {
                        jalan = false;
                    }
                    break;
                case 3 :
                    hilbert();
                    System.out.printf("Apakah anda ingin kembali ke menu utama? (y/n) : ");
                    c = scanner.next().charAt(0);
                    System.out.println();

                    if(c == 'n') {
                        jalan = false;
                    }
                    break;
                case 4 :
                    fungsiE();
                    System.out.printf("Apakah anda ingin kembali ke menu utama? (y/n) : ");
                    c = scanner.next().charAt(0);
                    System.out.println();

                    if(c == 'n') {
                        jalan = false;
                    }
                    break;
                default :
                    System.out.printf("Input Salah\n");
                    break;
            }

            System.out.print("\033[H\033[2J");
            System.out.flush();

            if (m == 0 || jalan == false) {
                System.out.println("Terimakasih sudah menggunakan program kami...");
                System.out.println("13516062");
                System.out.println("13516083");
                System.out.println("13516086");
            }
        }
    }
}
