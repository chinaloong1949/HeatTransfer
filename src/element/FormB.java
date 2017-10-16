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
public class FormB {

    private double[][] b = new double[3][8];
    private double detJ;

    public FormB(double r, double s, double t, double[] x, double[] y,
            double[] z) {
        double[][] d = new double[3][8];
        double[][] jacobi = new double[3][3];
        Matrix inverseJ = new Matrix(3, 3);

        //<editor-fold defaultstate="collapsed" desc="确定形函数对局部坐标偏导数矩阵d">
        //确定形函数对局部坐标偏导数矩阵
        d[0][0] = (1 - s) * (1 - t) / -8.0;
        d[0][1] = (1 - s) * (1 - t) / 8.0;
        d[0][2] = (1 + s) * (1 - t) / 8.0;
        d[0][3] = (1 + s) * (1 - t) / -8.0;
        d[0][4] = (1 - s) * (1 + t) / -8.0;
        d[0][5] = (1 - s) * (1 + t) / 8.0;
        d[0][6] = (1 + s) * (1 + t) / 8.0;
        d[0][7] = (1 + s) * (1 + t) / -8.0;

        d[1][0] = (1 - r) * (1 - t) / -8.0;
        d[1][1] = (1 + r) * (1 - t) / -8.0;
        d[1][2] = (1 + r) * (1 - t) / 8.0;
        d[1][3] = (1 - r) * (1 - t) / 8.0;
        d[1][4] = (1 - r) * (1 + t) / -8.0;
        d[1][5] = (1 + r) * (1 + t) / -8.0;
        d[1][6] = (1 + r) * (1 + t) / 8.0;
        d[1][7] = (1 - r) * (1 + t) / 8.0;

        d[2][0] = (1 - r) * (1 - s) / -8.0;
        d[2][1] = (1 + r) * (1 - s) / -8.0;
        d[2][2] = (1 + r) * (1 + s) / -8.0;
        d[2][3] = (1 - r) * (1 + s) / -8.0;
        d[2][4] = (1 - r) * (1 - s) / 8.0;
        d[2][5] = (1 + r) * (1 - s) / 8.0;
        d[2][6] = (1 + r) * (1 + s) / 8.0;
        d[2][7] = (1 - r) * (1 + s) / 8.0;
        //</editor-fold>

        //求雅克比矩阵
        for (int kk = 0; kk <= 2; kk++) {
            for (int ll = 0; ll <= 7; ll++) {
                jacobi[kk][0] = jacobi[kk][0] + d[kk][ll] * x[ll];
                jacobi[kk][1] = jacobi[kk][1] + d[kk][ll] * y[ll];
                jacobi[kk][2] = jacobi[kk][2] + d[kk][ll] * z[ll];
            }
        }

        //求雅克比矩阵的行列式
        detJ = new Matrix(jacobi).getDetValue();

        inverseJ = ((new Matrix(jacobi)).inverseMatrix());

        //逐列求出矩阵b中的元素
        //(3,3)*(3,8)=(3,8)
        b = Matrix.cross(inverseJ, new Matrix(d)).toArray();
    }

    public double getDetJ() {
        return detJ;
    }

    public double[][] getB() {
        return b;
    }
}
