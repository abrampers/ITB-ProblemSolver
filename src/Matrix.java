import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Matrix {

    private int rowSize;
    private int colSize;
    private double[][] data;
    public double[][] dataInterpolasi;
    private double[] dataSolved;

    //initializer
    //create 100-by-100 matrix of 0
    public Matrix(int m, int n) {
        this.rowSize = m;
        this.colSize = n + 1;
        this.data = new double[100][100];
    }

    //getter
    //get number of row used
    public int getRowSize() {
        return rowSize;
    }

    //get number of column used
    public int getColSize() {
        return colSize;
    }

    public double getData(int i, int j) {

        return this.data[i][j];

    }

    //setter
    public void setData() {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < this.rowSize; i++) {
            System.out.println();
            System.out.println("Persamaan " + (i + 1) + " : ");
            for (int j = 0; j < this.colSize; j++) {
                if (j == colSize - 1) {
                    System.out.print("Konstanta : ");
                } else {
                    System.out.print("X" + (j + 1) + " : ");
                }
                this.data[i][j] = scanner.nextDouble();
            }
        }
    }

    public void setDataInterpolasi(int n) {
        Scanner scanner = new Scanner(System.in);
        dataInterpolasi = new double[2][n];

        for (int i = 0; i < n; i++) {
            System.out.println("Titik " + (i + 1) + ": ");
            System.out.print("  X  = ");    dataInterpolasi[0][i] = scanner.nextDouble();
            System.out.print("f(X) = ");    dataInterpolasi[1][i] = scanner.nextDouble();
            System.out.println();
        }
    }

    public void readFileSPL(String namaFile, Matrix mat) {
        File file = new File("test/" + namaFile);

		try {
            Scanner input = new Scanner(file);
            int col = 0;
            int row = 0;

            if(input.hasNextLine()) {
                row++;
                Scanner test = new Scanner(input.nextLine());

                while (test.hasNextDouble()) {
                    ++col;
                    test.nextDouble();
                }

                while (input.hasNextLine()) {
                    row++;

                    while (test.hasNextDouble()) {
                        ++col;
                        test.nextDouble();
                    }

                    input.nextLine();

                }

                mat.rowSize = row;
                mat.colSize = col;
                input.close();

                mat = new Matrix(row, col);

                input = new Scanner(file);

                for (int i = 0; i < rowSize; i++) {
                    for (int j = 0; j < colSize; j++) {
                        if (input.hasNextDouble()) {
                            data[i][j] = input.nextDouble();
                        }
                    }
                }

            }


		} catch(IOException e){
			System.out.println("File tidak ditemukan");
		}

	}

    public void readFileInterpolasi(String namaFile, Matrix mat) {
        File file = new File("test/" + namaFile);
		BufferedReader reader = null;

		try {
            Scanner input = new Scanner(file);
            int col = 0;
            int row = 0;

            if(input.hasNextLine()) {
                row++;
                Scanner test = new Scanner(input.nextLine());

                while (test.hasNextDouble()) {
                    ++col;
                    test.nextDouble();
                }

                while (input.hasNextLine()) {
                    row++;
                    while (test.hasNextDouble()) {
                        col++;
                        test.nextDouble();
                    }
                    input.nextLine();
                }

                rowSize = col;
                colSize = col + 1;

                input.close();

                input = new Scanner(file);
                dataInterpolasi = new double[2][col];

                for (int i = 0; i < col; i++) {
                    dataInterpolasi[0][i] = input.nextDouble();
                }

                for (int i = 0; i < col; i++) {
                    dataInterpolasi[1][i] = input.nextDouble();
                }

            }


		} catch(IOException e){
			System.out.println("File tidak ditemukan");
            //panggil fungsinya lagi
		}
	}

    public void changeData(int i, int j, double d) {
        this.data[i][j] = d;
    }

    //output
    public void printMatrix() {
        for (int i = 0; i < this.rowSize; i++) {
            for (int j = 0; j < this.colSize; j++) {
                System.out.printf("%.2f ", this.data[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printToFile(boolean spl) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Simpan solusi? (y/n) : ");
        char c = scanner.next().charAt(0);
        System.out.println();

        if (c == 'y') {

            System.out.printf("Masukkan nama file penyimpanan : ");
            String namaFile = scanner.next();

            System.out.println();

            if (spl) {
                try {

                    PrintWriter writer = new PrintWriter("test/" + namaFile, "UTF-8");

                    int indeks;
                    int count;
                    int rowCheck = rowSize;
                    int[] free = new int[colSize - 1]; Arrays.fill(free, (-1));
                    boolean check = false;
                    boolean first;

                    if (weirdRow(rowSize-1)) {
                        writer.println("SPL TIDAK memiliki solusi");
                    }
                    else {
                        while (zeroRow(rowCheck - 1)) {
                            rowCheck--;
                        }
                        if (rowCheck < (colSize - 1)) {
                            writer.println("SPL memiliki BANYAK SOLUSI");
                            check = true;
                        } else {
                            writer.println("SPL memiliki SOLUSI UNIK");
                        }
                        for (int i = 0; i < rowCheck; i++) {
                            indeks = -1;
                            count = 0;
                            first = false;
                            for (int j = 0; j < colSize; j++) {
                                if ((data[i][j] > 0.99) && (data[i][j] < 1.01) && (!first)) {
                                    indeks = j;
                                    first = true;
                                }
                                if (indeks == j) {
                                    writer.print("X" + (indeks + 1) + " =");
                                } else if ((data[i][j] < -0.01) || (data[i][j] > 0.01) || (j == (colSize - 1))) {
                                    double temp = data[i][j];
                                    if (j != colSize - 1) {
                                        temp *= (-1);
                                    }
                                    writer.print(" ");
                                    if (count != 0) {
                                        if (temp > 0) {
                                            writer.print("+ ");
                                        } else {
                                            temp *= (-1);
                                            writer.print("- ");
                                        }
                                    }
                                    writer.printf("%.2f",temp);
                                    if (check) {
                                        if (j != (colSize - 1)) {
                                            writer.print("X" + (j + 1));
                                            free[j] = j;
                                        }
                                    }
                                    count++;
                                }
                            }
                            writer.println();
                        }

                        if (check) {
                            for (int l = 0; l < (colSize - 1); l++) {
                                if (free[l] != (-1)) {
                                    writer.println("X" + (free[l] + 1) + " = free");
                                }
                            }
                        }
                    }
                    writer.close();

                    System.out.println("File " + namaFile + " berhasil disimpan di test/" + namaFile);

                    System.out.println();

                } catch ( IOException e ) {
                    System.out.println("Error dalam penyimpanan file");
                }
            } else {
                try {

                    PrintWriter writer = new PrintWriter("test/" + namaFile, "UTF-8");

                    //Print
                    writer.println("Persamaan Interpolasi : ");
                    writer.print("P(X) = ");

                    int count=0;
                    if(dataSolved[colSize - 2] != 0) {
                        if((dataSolved[colSize-2] > 0.01) || (dataSolved[colSize-2] < -0.01)) {
                            writer.printf("%.2fX^%d", dataSolved[colSize - 2], (colSize - 2));
                            count++;
                        }
                    }
                    for(int i = colSize - 3; i >= 0; i--) {
                        if((dataSolved[i] > 0.01) || (dataSolved[i] < -0.01)) {
                            if(dataSolved[i] < 0) {
                                writer.printf(" - %.2f",(dataSolved[i]*(-1)));
                                count++;
                            }
                            else {
                                if(count == 0) {
                                    writer.printf(" %.2f",dataSolved[i]);
                                }
                                else {
                                    writer.printf(" + %.2f",dataSolved[i]);
                                }
                                count++;
                            }
                            if(i > 0) {
                                writer.printf("X^%d",i);
                            }
                        }
                    }

                    writer.close();
                    System.out.println("File " + namaFile + " berhasil disimpan di test/" + namaFile);
                    System.out.println();
                } catch (IOException e) {
                    System.out.println("Error dalam penyimpanan file");
                }
            }
        }
    }

    public void copyMatrix(Matrix M) {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                this.data[i][j] = M.data[i][j];
            }
        }
    }

    //methods for gauss jordan
    //swap i'th row with the k'th row
    private void swapRow(int i, int k) {
        if (i != k) {
            for (int j = 0; j < colSize; j++) {
                double temp = data[i][j];
                data[i][j] = data[k][j];
                data[k][j] = temp;
            }
        }
    }

    private int maxAbsCol(int i, int j) {
        int idxMax = i;
        for (int k = i; k < rowSize; k++) {
            if ((data[idxMax][j] == 0) || ((data[k][j] != 0)&&(data[k][j] > data[idxMax][j]))&&(!zeroRow(k))&&(!weirdRow(k))) {
                idxMax = k;
            }
        }
        return idxMax;
    }

    private void pivotingPoint(int i, int j) {
        swapRow(i, maxAbsCol(i, j));
    }

    private void moveZeroRow() {
        for(int i = 0; i < rowSize; i++) {
            boolean check = false;
            for (int j = 0; j < (colSize - 1); j++) {
                if(data[i][j] != 0) {
                    check = true;
                    break;
                }
            }
            if(!check) {
                swapRow(i,(rowSize - 1));
            }
        }
    }

    private void divideRow(int i, double d) {
        for (int j = 0; j < colSize; j++) {
            data[i][j] /= d;
        }
    }

    private void substractRow(int i, double d, int k) {
        for (int j = 0; j < colSize; j++) {
            data[k][j] -= d * data[i][j];
        }
    }

    private boolean zeroRow(int i) {
        for (int j = 0; j < colSize; j++) {
            if (data[i][j] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean weirdRow(int i) {
        for (int j = 0; j < colSize - 1; j++) {
            if (data[i][j] != 0) return false;
        }

        return !(data[i][colSize - 1] == 0);
    }

    public void gauss() {
        //diperhatikan Kalo leading one nya ga di i, i dan kalo doi ga pembaginya 0
        int i = 0;
        moveZeroRow();
        for (int l = 0; l < colSize; l++) {
            pivotingPoint(i, l);
            if (data[i][l] != 0) {
                for (int k = i + 1; k <= rowSize; k++) {
                    if(k != rowSize) { //Kalo k=rowsize kan gaada data nya jadi ini mungkin error. Cmn perlu divideRow aja
                        double t = data[k][l] / data[i][l];
                        substractRow(i, t, k);
                    }
                    divideRow(i, data[i][l]);
                }
                i++;
            }
        }
    }

    public void gaussJordan() {
        int j;
        boolean check;
        gauss();
        for (int i = 0; i < rowSize; i++) {
            check = false;
            j = 0;
            while ((!check) && (j < colSize)) {
                if(data[i][j] > 0.99 && data[i][j] < 1.01) {
                    check = true;
                }
                else {
                    j++;
                }
            }
            if (check) {
                for (int k = 0; k < rowSize; k++) {
                    if(k != i) {
                        double t = data[k][j]/data[i][j];
                        substractRow(i, t, k);
                    }
                }
            }
        }
    }

    public void solutionMatrix() {
        int indeks;
        int count;
        int rowCheck = rowSize;
        int[] free = new int[colSize - 1]; Arrays.fill(free, (-1));
        boolean check = false;
        boolean first;

        if (weirdRow(rowSize-1)) {
            System.out.println("SPL TIDAK memiliki solusi");
        }
        else {
            while (zeroRow(rowCheck - 1)) {
                rowCheck--;
            }
            if (rowCheck < (colSize - 1)) {
                System.out.println("SPL memiliki BANYAK SOLUSI");
                check = true;
            } else {
                System.out.println("SPL memiliki SOLUSI UNIK");
            }
            for (int i = 0; i < rowCheck; i++) {
                indeks = -1;
                count = 0;
                first = false;
                for (int j = 0; j < colSize; j++) {
                    if ((data[i][j] > 0.99) && (data[i][j] < 1.01) && (!first)) {
                        indeks = j;
                        first = true;
                    }
                    if (indeks == j) {
                        System.out.print("X" + (indeks + 1) + " =");
                    } else if ((data[i][j] < -0.01) || (data[i][j] > 0.01) || (j == (colSize - 1))) {
                        double temp = data[i][j];
                        if (j != colSize - 1) {
                            temp *= (-1);
                        }
                        System.out.print(" ");
                        if (count != 0) {
                            if (temp > 0) {
                                System.out.print("+ ");
                            } else {
                                temp *= (-1);
                                System.out.print("- ");
                            }
                        }
                        System.out.printf("%.2f",temp);
                        if (check) {
                            if (j != (colSize - 1)) {
                                System.out.print("X" + (j + 1));
                                free[j] = j;
                            }
                        }
                        count++;
                    }
                }
                System.out.println();
            }

            if (check) {
                for (int l = 0; l < (colSize - 1); l++) {
                    if (free[l] != (-1)) {
                        System.out.println("X" + (free[l] + 1) + " = free");
                    }
                }
            }
        }
        System.out.println();
    }

    private double powerOf(double d, int t) {
        double hasil = 1;
        if(t == 0) {
            return 1;
        }
        for(int i = 0; i < t; i++) {
            hasil *= d;
        }
        return hasil;
    }

    public void interpolasi (int t) {
        Scanner scanner = new Scanner(System.in);
        dataSolved = new double[colSize - 1];

        //Membuat Matriks dari input titik
        for(int i = 0; i < rowSize; i++) {
            for(int j = 0; j < (colSize - 1); j++) {
                this.data[i][j] = powerOf(dataInterpolasi[0][i], (colSize - j - 2));
            }
            this.data[i][colSize - 1] = dataInterpolasi[1][i];
        }

        gaussJordan();
        for(int i = 0; i < rowSize; i++) {
            dataSolved[i] = data[rowSize - i - 1][colSize - 1];
        }

        //Print
        System.out.println("Persamaan Interpolasi : ");
        System.out.print("P(X) = ");

        int count=0;
        if(dataSolved[colSize - 2] != 0) {
            if((dataSolved[colSize-2] > 0.01) || (dataSolved[colSize-2] < -0.01)) {
                System.out.printf("%.2fX^%d", dataSolved[colSize - 2], (colSize - 2));
                count++;
            }
        }
        for(int i = colSize - 3; i >= 0; i--) {
            if((dataSolved[i] > 0.01) || (dataSolved[i] < -0.01)) {
                if(dataSolved[i] < 0) {
                    System.out.printf(" - %.2f",(dataSolved[i]*(-1)));
                    count++;
                }
                else {
                    if(count == 0) {
                        System.out.printf(" %.2f",dataSolved[i]);
                    }
                    else {
                        System.out.printf(" + %.2f",dataSolved[i]);
                    }
                    count++;
                }
                if(i > 0) {
                    System.out.printf("X^%d",i);
                }
            }
        }
        System.out.println();
    }

    public double hampiranInterpolasi(double x) {
    //Asumsi dataSolved sudah terdefinisi
        double d = 0;
        for(int i = 0; i < colSize-1; i++) {
            d += dataSolved[i] * powerOf(x, i);
        }
        return d;
    }
}
