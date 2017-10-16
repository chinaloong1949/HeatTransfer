/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import common.Matrix;
import point.Point3D;

/**
 *
 * @author yk
 */
public class Fai_3D_20node {

    //该类所占用的内存在每一个单元循环结束之后会释放，所以应尽量减小计算量，
    //内存占用影响不大
    private Point3D[] point;
    private double kesi;
    private double ita;
    private double zeta;
    private double[] fai_kesi;
    private double[] fai_ita;
    private double[] fai_zeta;
    private double[] n_i;
    private double[] kesi_i;
    private double[] ita_i;
    private double[] zeta_i;

    public Fai_3D_20node(Point3D[] point, double kesi, double ita, double zeta) {
        this.point = point;
        this.kesi = kesi;
        this.ita = ita;
        this.zeta = zeta;
        kesi_i = new double[]{
            -1, 1, 1, -1,
            -1, 1, 1, -1,
            0, 1, 0, -1,
            -1, 1, 1, -1,
            0, 1, 0, -1
        };
        ita_i = new double[]{
            -1, -1, 1, 1,
            -1, -1, 1, 1,
            -1, 0, 1, 0,
            -1, -1, 1, 1,
            -1, 0, 1, 0
        };
        zeta_i = new double[]{
            -1, -1, -1, -1,
            1, 1, 1, 1,
            -1, -1, -1, -1,
            0, 0, 0, 0,
            1, 1, 1, 1
        };
        fai_kesi = initFai_kesi(kesi, ita, zeta);
        fai_ita = initFai_ita(kesi, ita, zeta);
        fai_zeta = initFai_zeta(kesi, ita, zeta);
        n_i = initN_i(kesi, ita, zeta);
    }

    private double[] initN_i(double kesi, double ita, double zeta) {
        //传入节点坐标（），得到其对应的形函数值,【N1, N2, N3, N4, N5, N6, N7, N8】
        double[] arr = new double[20];
        for (int i = 0; i <= 7; i++) {
            arr[i] = (1 + kesi_i[i] * kesi) * (1 + ita_i[i] * ita)
                    * (1 + zeta_i[i] * zeta)
                    * (kesi_i[i] * kesi + ita_i[i] * ita + zeta_i[i] * zeta - 2)
                    / 8.;
        }
        int i = 8;
        arr[i] = (1 - kesi * kesi) * (1 + ita_i[i] * ita) * (1 + zeta_i[i] * zeta) / 4.0;
        i = 10;
        arr[i] = (1 - kesi * kesi) * (1 + ita_i[i] * ita) * (1 + zeta_i[i] * zeta) / 4.0;
        i = 16;
        arr[i] = (1 - kesi * kesi) * (1 + ita_i[i] * ita) * (1 + zeta_i[i] * zeta) / 4.0;
        i = 18;
        arr[i] = (1 - kesi * kesi) * (1 + ita_i[i] * ita) * (1 + zeta_i[i] * zeta) / 4.0;

        i = 9;
        arr[i] = (1 - ita * ita) * (1 + kesi_i[i] * kesi) * (1 + zeta_i[i] * zeta) / 4.0;
        i = 11;
        arr[i] = (1 - ita * ita) * (1 + kesi_i[i] * kesi) * (1 + zeta_i[i] * zeta) / 4.0;
        i = 17;
        arr[i] = (1 - ita * ita) * (1 + kesi_i[i] * kesi) * (1 + zeta_i[i] * zeta) / 4.0;
        i = 19;
        arr[i] = (1 - ita * ita) * (1 + kesi_i[i] * kesi) * (1 + zeta_i[i] * zeta) / 4.0;

        for (i = 12; i <= 15; i++) {
            arr[i] = (1 + kesi_i[i] * kesi) * (1 + ita_i[i] * ita)
                    * (1 - zeta * zeta) / 4.0;
        }

        return arr;
    }

    public Matrix getN_iMatrix(boolean isHorizontal) {
        return new Matrix(isHorizontal, n_i);
    }

    public Matrix getJacobiMatrix() {
        Matrix fai_kesiMatrix = getFai_kesiMatrix(true);
        Matrix fai_itaMatrix = getFai_itaMatrix(true);
        Matrix fai_zetaMatrix = getFai_zetaMatrix(true);
        Matrix xMatrix = new Matrix(false, getX());
        Matrix yMatrix = new Matrix(false, getY());
        Matrix zMatrix = new Matrix(false, getZ());
        double[][] jacobiArr = new double[3][3];
        jacobiArr[0][0] = Matrix.cross(fai_kesiMatrix, xMatrix).toArray()[0][0];//dx_dkesi
        jacobiArr[1][0] = Matrix.cross(fai_itaMatrix, xMatrix).toArray()[0][0];//dx_dita
        jacobiArr[2][0] = Matrix.cross(fai_zetaMatrix, xMatrix).toArray()[0][0];//dx_dzeta
        jacobiArr[0][1] = Matrix.cross(fai_kesiMatrix, yMatrix).toArray()[0][0];//dy_dkesi
        jacobiArr[1][1] = Matrix.cross(fai_itaMatrix, yMatrix).toArray()[0][0];//dy_dita
        jacobiArr[2][1] = Matrix.cross(fai_zetaMatrix, yMatrix).toArray()[0][0];//dy_dzeta
        jacobiArr[0][2] = Matrix.cross(fai_kesiMatrix, zMatrix).toArray()[0][0];//dz_dkesi
        jacobiArr[1][2] = Matrix.cross(fai_itaMatrix, zMatrix).toArray()[0][0];//dz_dita
        jacobiArr[2][2] = Matrix.cross(fai_zetaMatrix, zMatrix).toArray()[0][0];//dz_dzeta
        Matrix matrix = new Matrix(jacobiArr);
        return matrix;
    }

    private double[] initFai_kesi(double kesi, double ita, double zeta) {
        double[] arr = new double[20];
        for (int i = 0; i <= 7; i++) {
            arr[i] = 0.125 * (1 + ita_i[i] * ita) * (1 + zeta_i[i] * zeta)
                    * ((2 * kesi_i[i] * kesi_i[i] * kesi)
                    + ((ita_i[i] * ita + zeta_i[i] * zeta - 1) * kesi_i[i]));
        }
        int i = 8;
        //当kesi为零时
        arr[i] = -0.5 * kesi * (1 + ita_i[i] * ita) * (1 + zeta_i[i] * zeta);
        i = 10;
        arr[i] = -0.5 * kesi * (1 + ita_i[i] * ita) * (1 + zeta_i[i] * zeta);
        i = 16;
        arr[i] = -0.5 * kesi * (1 + ita_i[i] * ita) * (1 + zeta_i[i] * zeta);
        i = 18;
        arr[i] = -0.5 * kesi * (1 + ita_i[i] * ita) * (1 + zeta_i[i] * zeta);

        //当ita为零时
        i = 9;
        arr[i] = 0.25 * kesi_i[i] * (1 - ita * ita) * (1 + zeta_i[i] * zeta);
        i = 11;
        arr[i] = 0.25 * kesi_i[i] * (1 - ita * ita) * (1 + zeta_i[i] * zeta);
        i = 17;
        arr[i] = 0.25 * kesi_i[i] * (1 - ita * ita) * (1 + zeta_i[i] * zeta);
        i = 19;
        arr[i] = 0.25 * kesi_i[i] * (1 - ita * ita) * (1 + zeta_i[i] * zeta);

        //当zeta为零时
        i = 12;
        arr[i] = 0.25 * kesi_i[i] * (1 - zeta * zeta) * (1 + ita_i[i] * ita);
        i = 13;
        arr[i] = 0.25 * kesi_i[i] * (1 - zeta * zeta) * (1 + ita_i[i] * ita);
        i = 14;
        arr[i] = 0.25 * kesi_i[i] * (1 - zeta * zeta) * (1 + ita_i[i] * ita);
        i = 15;
        arr[i] = 0.25 * kesi_i[i] * (1 - zeta * zeta) * (1 + ita_i[i] * ita);
        return arr;
    }

    public Matrix getFai_kesiMatrix(boolean isHorizontal) {
        return new Matrix(isHorizontal, getFai_kesi());
    }

//    public Matrix getFai_kesiMatrix_20to8(boolean isHorizontal, double kesi, double ita, double zeta) {
//        double[] fai_kesi_20to8 = new double[8];
//        for (int i = 0; i <= 7; i++) {
//            fai_kesi_20to8[i] = fai_kesi[i];
//        }
//        return new Matrix(isHorizontal, fai_kesi_20to8);
//    }
    private double[] initFai_ita(double kesi, double ita, double zeta) {
        double[] arr = new double[20];
        for (int i = 0; i <= 7; i++) {
            arr[i] = 0.125 * (1 + kesi_i[i] * kesi) * (1 + zeta_i[i] * zeta)
                    * ((2 * ita_i[i] * ita_i[i] * ita)
                    + ((kesi_i[i] * kesi + zeta_i[i] * zeta - 1) * ita_i[i]));
        }
        int i = 8;
        arr[i] = 0.25 * ita_i[i] * (1 - kesi * kesi) * (1 + zeta_i[i] * zeta);
        i = 10;
        arr[i] = 0.25 * ita_i[i] * (1 - kesi * kesi) * (1 + zeta_i[i] * zeta);
        i = 16;
        arr[i] = 0.25 * ita_i[i] * (1 - kesi * kesi) * (1 + zeta_i[i] * zeta);
        i = 18;
        arr[i] = 0.25 * ita_i[i] * (1 - kesi * kesi) * (1 + zeta_i[i] * zeta);

        i = 9;
        arr[i] = -0.5 * ita * (1 + kesi_i[i] * kesi) * (1 + zeta_i[i] * zeta);
        i = 11;
        arr[i] = -0.5 * ita * (1 + kesi_i[i] * kesi) * (1 + zeta_i[i] * zeta);
        i = 17;
        arr[i] = -0.5 * ita * (1 + kesi_i[i] * kesi) * (1 + zeta_i[i] * zeta);
        i = 19;
        arr[i] = -0.5 * ita * (1 + kesi_i[i] * kesi) * (1 + zeta_i[i] * zeta);

        i = 12;
        arr[i] = 0.25 * ita_i[i] * (1 + kesi_i[i] * kesi) * (1 - zeta * zeta);
        i = 13;
        arr[i] = 0.25 * ita_i[i] * (1 + kesi_i[i] * kesi) * (1 - zeta * zeta);
        i = 14;
        arr[i] = 0.25 * ita_i[i] * (1 + kesi_i[i] * kesi) * (1 - zeta * zeta);
        i = 15;
        arr[i] = 0.25 * ita_i[i] * (1 + kesi_i[i] * kesi) * (1 - zeta * zeta);
        return arr;
    }

    public Matrix getFai_itaMatrix(boolean isHorizontal) {
        return new Matrix(isHorizontal, getFai_ita());
    }

//    public Matrix getFai_itaMatrix_20to8(boolean isHorizontal, double kesi, double ita, double zeta) {
//        double[] fai_ita_20to8 = new double[8];
//        for (int i = 0; i <= 7; i++) {
//            fai_ita_20to8[i] = fai_ita[i];
//        }
//        return new Matrix(isHorizontal, fai_ita_20to8);
//    }
    private double[] initFai_zeta(double kesi, double ita, double zeta) {
        double[] arr = new double[20];
        for (int i = 0; i <= 7; i++) {
            arr[i] = 0.125 * (1 + kesi_i[i] * kesi) * (1 + ita_i[i] * ita)
                    * ((2 * zeta_i[i] * zeta_i[i] * zeta)
                    + ((kesi_i[i] * kesi + ita_i[i] * ita - 1) * zeta_i[i]));
        }
        int ii = 8;
        arr[ii] = 0.25 * zeta_i[ii] * (1 - kesi * kesi) * (1 + ita_i[ii] * ita);
        ii = 10;
        arr[ii] = 0.25 * zeta_i[ii] * (1 - kesi * kesi) * (1 + ita_i[ii] * ita);
        ii = 16;
        arr[ii] = 0.25 * zeta_i[ii] * (1 - kesi * kesi) * (1 + ita_i[ii] * ita);
        ii = 18;
        arr[ii] = 0.25 * zeta_i[ii] * (1 - kesi * kesi) * (1 + ita_i[ii] * ita);

        ii = 9;
        arr[ii] = 0.25 * zeta_i[ii] * (1 + kesi_i[ii] * kesi) * (1 - ita * ita);
        ii = 11;
        arr[ii] = 0.25 * zeta_i[ii] * (1 + kesi_i[ii] * kesi) * (1 - ita * ita);
        ii = 17;
        arr[ii] = 0.25 * zeta_i[ii] * (1 + kesi_i[ii] * kesi) * (1 - ita * ita);
        ii = 19;
        arr[ii] = 0.25 * zeta_i[ii] * (1 + kesi_i[ii] * kesi) * (1 - ita * ita);

        ii = 12;
        arr[ii] = -0.5 * zeta * (1 + kesi_i[ii] * kesi) * (1 + ita_i[ii] * ita);
        ii = 13;
        arr[ii] = -0.5 * zeta * (1 + kesi_i[ii] * kesi) * (1 + ita_i[ii] * ita);
        ii = 14;
        arr[ii] = -0.5 * zeta * (1 + kesi_i[ii] * kesi) * (1 + ita_i[ii] * ita);
        ii = 15;
        arr[ii] = -0.5 * zeta * (1 + kesi_i[ii] * kesi) * (1 + ita_i[ii] * ita);
        return arr;
    }

    public Matrix getFai_zetaMatrix(boolean isHorizontal) {
        return new Matrix(isHorizontal, getFai_zeta());
    }

//    public Matrix getFai_zetaMatrix_20to8(boolean isHorizontal, double kesi, double ita, double zeta) {
//        double[] fai_zeta_20to8 = new double[8];
//        for (int i = 0; i <= 7; i++) {
//            fai_zeta_20to8[i] = fai_zeta[i];
//        }
//        return new Matrix(isHorizontal, fai_zeta_20to8);
//    }
    public double[] getX() {
        double[] xList = new double[20];
        for (int i = 0; i <= point.length - 1; i++) {
            xList[i] = point[i].getX();
        }
        return xList;
    }

    public double[] getY() {
        double[] yList = new double[20];
        for (int i = 0; i <= point.length - 1; i++) {
            yList[i] = point[i].getY();
        }
        return yList;
    }

    public double[] getZ() {
        double[] zList = new double[20];
        for (int i = 0; i <= point.length - 1; i++) {
            zList[i] = point[i].getZ();
        }
        return zList;
    }

    /**
     * @return the fai_kesi
     */
    public double[] getFai_kesi() {
        return fai_kesi;
    }

    /**
     * @return the fai_ita
     */
    public double[] getFai_ita() {
        return fai_ita;
    }

    /**
     * @return the fai_zeta
     */
    public double[] getFai_zeta() {
        return fai_zeta;
    }

}
