/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import common.Matrix;

/**
 *
 * @author Administrator
 */
public class FormN {

    private double detJ;
    private double[] n;

    public FormN(double r, double s, double t, double[] x, double[] y,
            double[] z) {

        //确定形函数矩阵
        n = new double[8];

        n[0] = 0.125 * (1 - r) * (1 - s) * (1 - t);
        n[1] = 0.125 * (1 + r) * (1 - s) * (1 - t);
        n[2] = 0.125 * (1 + r) * (1 + s) * (1 - t);
        n[3] = 0.125 * (1 - r) * (1 + s) * (1 - t);
        n[4] = 0.125 * (1 - r) * (1 - s) * (1 + t);
        n[5] = 0.125 * (1 + r) * (1 - s) * (1 + t);
        n[6] = 0.125 * (1 + r) * (1 + s) * (1 + t);
        n[7] = 0.125 * (1 - r) * (1 + s) * (1 + t);
        //形函数对自然坐标偏导数矩阵
        double[][] d = new double[3][8];

        d[0][0] = (1 - s) * (1 - t) / (-8.0);
        d[0][1] = (1 - s) * (1 - t) / (8.0);
        d[0][2] = (1 + s) * (1 - t) / (8.0);
        d[0][3] = (1 + s) * (1 - t) / (-8.0);
        d[0][4] = (1 - s) * (1 + t) / (-8.0);
        d[0][5] = (1 - s) * (1 + t) / (8.0);
        d[0][6] = (1 + s) * (1 + t) / (8.0);
        d[0][7] = (1 + s) * (1 + t) / (-8.0);

        d[1][0] = (1 - r) * (1 - t) / (-8.0);
        d[1][1] = (1 + r) * (1 - t) / (-8.0);
        d[1][2] = (1 + r) * (1 - t) / (8.0);
        d[1][3] = (1 - r) * (1 - t) / (8.0);
        d[1][4] = (1 - r) * (1 + t) / (-8.0);
        d[1][5] = (1 + r) * (1 + t) / (-8.0);
        d[1][6] = (1 + r) * (1 + t) / (8.0);
        d[1][7] = (1 - r) * (1 + t) / (8.0);

        d[2][0] = (1 - r) * (1 - s) / (-8.0);
        d[2][1] = (1 + r) * (1 - s) / (-8.0);
        d[2][2] = (1 + r) * (1 + s) / (-8.0);
        d[2][3] = (1 - r) * (1 + s) / (-8.0);
        d[2][4] = (1 - r) * (1 - s) / (8.0);
        d[2][5] = (1 + r) * (1 - s) / (8.0);
        d[2][6] = (1 + r) * (1 + s) / (8.0);
        d[2][7] = (1 - r) * (1 + s) / (8.0);

        //形成雅克比矩阵
        double[][] jacobiArr = new double[3][3];
        for (int kk = 0; kk <= 2; kk++) {
            for (int ll = 0; ll <= 7; ll++) {
                jacobiArr[kk][0] = jacobiArr[kk][0] + d[kk][ll] * x[ll];
                jacobiArr[kk][1] = jacobiArr[kk][1] + d[kk][ll] * y[ll];
                jacobiArr[kk][2] = jacobiArr[kk][2] + d[kk][ll] * z[ll];
            }
        }

        //求雅可比矩阵的行列式|J|
        detJ = new Matrix(jacobiArr).getDetValue();
    }

    /**
     * @return the detJ
     */
    public double getDetJ() {
        return detJ;
    }

    /**
     * @return the n
     */
    public double[] getN() {
        return n;
    }

}
