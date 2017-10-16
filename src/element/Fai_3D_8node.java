/*
 * 六面体单元的插值函数
 * 传入点坐标（x,y,z)，即可得到其形函数矩阵值，即【N1, N2, N3, N4, N5, N6, N7, N8】
 */
package element;

import common.Matrix;
import point.Point3D;

/**
 *
 * @author yk
 */
public class Fai_3D_8node {

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
    private double[] n_i_surface;
    private double area_surface;

    public Fai_3D_8node(Point3D[] point, double kesi, double ita, double zeta) {
        this.point = point;
        this.kesi = kesi;
        this.ita = ita;
        this.zeta = zeta;
        fai_kesi = initFai_kesi(kesi, ita, zeta);
        fai_ita = initFai_ita(kesi, ita, zeta);
        fai_zeta = initFai_zeta(kesi, ita, zeta);
        n_i = initN_i(kesi, ita, zeta);
    }

    public Fai_3D_8node(Point3D[] point, double r, double s) {
        //这种情况表示是面积分
        this.point = point;
        this.kesi = r;
        this.ita = s;
        double[] x = new double[4];
        double[] y = new double[4];
        double[] z = new double[4];
        for (int i = 0; i < 4; i++) {
            x[i] = point[i].getX();
            y[i] = point[i].getY();
            z[i] = point[i].getZ();
        }
        FormM_A formM_A = new FormM_A(r, s, x, y, z);
        n_i_surface=formM_A.getFyp();
        area_surface=formM_A.getArea();
    }

    private double[] initN_i(double kesi, double ita, double zeta) {
        //传入节点坐标（），得到其对应的形函数值,【N1, N2, N3, N4, N5, N6, N7, N8】
        double[] arr = new double[8];
        arr[0] = (1 - kesi) * (1 - ita) * (1 - zeta) / 8.;
        arr[1] = (1 + kesi) * (1 - ita) * (1 - zeta) / 8.;
        arr[2] = (1 + kesi) * (1 + ita) * (1 - zeta) / 8.;
        arr[3] = (1 - kesi) * (1 + ita) * (1 - zeta) / 8.;
        arr[4] = (1 - kesi) * (1 - ita) * (1 + zeta) / 8.;
        arr[5] = (1 + kesi) * (1 - ita) * (1 + zeta) / 8.;
        arr[6] = (1 + kesi) * (1 + ita) * (1 + zeta) / 8.;
        arr[7] = (1 - kesi) * (1 + ita) * (1 + zeta) / 8.;
        return arr;
    }

    public Matrix getN_iMatrix(boolean isHorizontal) {
        return new Matrix(isHorizontal, n_i);
    }

    public Matrix getJacobiMatrix() {
        Matrix fai_kesiMatrix = getFai_kesiMatrix(true, kesi, ita, zeta);
        Matrix fai_itaMatrix = getFai_itaMatrix(true, kesi, ita, zeta);
        Matrix fai_zetaMatrix = getFai_zetaMatrix(true, kesi, ita, zeta);
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
        double[] arr = new double[8];

        arr[0] = -1. / 8. * (1 - ita) * (1 - zeta);
        arr[1] = 1. / 8. * (1 - ita) * (1 - zeta);
        arr[2] = 1. / 8. * (1 + ita) * (1 - zeta);
        arr[3] = -1. / 8. * (1 + ita) * (1 - zeta);
        arr[4] = -1. / 8. * (1 - ita) * (1 + zeta);
        arr[5] = 1. / 8. * (1 - ita) * (1 + zeta);
        arr[6] = 1. / 8. * (1 + ita) * (1 + zeta);
        arr[7] = -1. / 8. * (1 + ita) * (1 + zeta);
        return arr;
    }

    public Matrix getFai_kesiMatrix(boolean isHorizontal, double kesi, double ita, double zeta) {
        return new Matrix(isHorizontal, getFai_kesi());
    }

    private double[] initFai_ita(double kesi, double ita, double zeta) {
        double[] arr = new double[8];
        arr[0] = -1. / 8. * (1 - kesi) * (1 - zeta);
        arr[1] = -1. / 8. * (1 + kesi) * (1 - zeta);
        arr[2] = 1. / 8. * (1 + kesi) * (1 - zeta);
        arr[3] = 1. / 8. * (1 - kesi) * (1 - zeta);
        arr[4] = -1. / 8. * (1 - kesi) * (1 + zeta);
        arr[5] = -1. / 8. * (1 + kesi) * (1 + zeta);
        arr[6] = 1. / 8. * (1 + kesi) * (1 + zeta);
        arr[7] = 1. / 8. * (1 - kesi) * (1 + zeta);
        return arr;
    }

    public Matrix getFai_itaMatrix(boolean isHorizontal, double kesi, double ita, double zeta) {
        return new Matrix(isHorizontal, getFai_ita());
    }

    private double[] initFai_zeta(double kesi, double ita, double zeta) {
        double[] arr = new double[8];
        arr[0] = -1. / 8. * (1 - kesi) * (1 - ita);
        arr[1] = -1. / 8. * (1 + kesi) * (1 - ita);
        arr[2] = -1. / 8. * (1 + kesi) * (1 + ita);
        arr[3] = -1. / 8. * (1 - kesi) * (1 + ita);
        arr[4] = 1. / 8. * (1 - kesi) * (1 - ita);
        arr[5] = 1. / 8. * (1 + kesi) * (1 - ita);
        arr[6] = 1. / 8. * (1 + kesi) * (1 + ita);
        arr[7] = 1. / 8. * (1 - kesi) * (1 + ita);
        return arr;
    }

    public Matrix getFai_zetaMatrix(boolean isHorizontal, double kesi, double ita, double zeta) {
        return new Matrix(isHorizontal, getFai_zeta());
    }

    public double[] getX() {
        double[] xList = new double[8];
        for (int i = 0; i <= 7; i++) {
            xList[i] = point[i].getX();
        }
        return xList;
    }

    public double[] getY() {
        double[] yList = new double[8];
        for (int i = 0; i <= 7; i++) {
            yList[i] = point[i].getY();
        }
        return yList;
    }

    public double[] getZ() {
        double[] zList = new double[8];
        for (int i = 0; i <= 7; i++) {
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

    /**
     * @return the n_i_surface
     */
    public double[] getN_i_surface() {
        return n_i_surface;
    }

    /**
     * @return the area_surface
     */
    public double getArea_surface() {
        return area_surface;
    }

}
