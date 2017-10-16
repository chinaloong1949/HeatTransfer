/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author yk
 */
public class Determinant {

    double[][] matrix;
    double value;

    public Determinant(double[][] matrix) {
        int n = matrix.length;

        this.matrix = matrix;
        double sum = 0;
        if (n > 2) {
            for (int i = 0; i <= n - 1; i++) {

                double[][] cofactor = new double[n - 1][n - 1];
                for (int j = 0; j <= n - 2; j++) {
                    for (int k = 0; k <= n - 2; k++) {
                        if (k < i) {
                            cofactor[j][k] = matrix[j + 1][k];
                        } else {
                            cofactor[j][k] = matrix[j + 1][k + 1];
                        }
                    }
                }
                if (i % 2 != 0) {
                    sum = sum - (new Determinant(cofactor).getValue()) * matrix[0][i];
                } else {
                    sum = sum + (new Determinant(cofactor).getValue()) * matrix[0][i];
                }

            }
        } else if (n == 2) {
            sum = matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1];
        } else if (n == 1) {
            sum = matrix[0][0];
        } else {
            System.out.println("矩阵维数有误，当前矩阵为" + n + "维矩阵");
        }
        value = sum;
    }

    public double getValue() {
        return value;
    }
}
