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
public class Vandermonde {

    double[][] array;
    int n;

    public Vandermonde(double[] inArray) {

        this.n = inArray.length;
        array = new double[n][n];
        for (int i = 0; i <= n - 1; i++) {
            for (int j = 0; j <= n - 1; j++) {
                array[i][j] = Math.pow(inArray[i], j);
            }
        }
        //new Matrix(array).printMatrix();
    }

    public double getDetValue() {
        double det = 1;
        for (int j = 1; j <= n - 1; j++) {
            for (int i = 0; i <= j - 1; i++) {
                det = det * (array[j][1] - array[i][1]);
            }
        }
        return det;
    }

}
