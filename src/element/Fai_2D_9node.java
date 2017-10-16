/*
 * 平面四边形 二维二次九节点单元
 * 类中x1和x2指的是高斯积分的积分点，都是x坐标，与计算问题无关，至于高斯积分点的选取有关
 * 类中
 */
package element;

import common.Matrix;
import point.Point2D;

/**
 *
 * @author yk
 */
public class Fai_2D_9node {

    private Point2D[] point;

    public Fai_2D_9node(Point2D[] point) {
        this.point = point;
    }

    public double[] getN_i(double kesi, double ita) {
        double[] arr = new double[9];
        arr[0] = kesi * ita * (kesi - 1) * (ita - 1) / 4.;
        arr[1] = ita * (1 - kesi * kesi) * (ita - 1) / 2.;
        arr[2] = kesi * ita * (1 + kesi) * (ita - 1) / 4.;
        arr[3] = kesi * (kesi - 1) * (1 - ita * ita) / 2.;
        arr[4] = (1 - kesi * kesi) * (1 - ita * ita);
        arr[5] = kesi * (kesi + 1) * (1 - ita * ita) / 2.;
        arr[6] = kesi * ita * (kesi - 1) * (ita + 1) / 4.;
        arr[7] = ita * (1 - kesi * kesi) * (ita + 1) / 2.;
        arr[8] = kesi * ita * (kesi + 1) * (ita + 1) / 4.;
        return arr;
    }

    public Matrix getJacobiMatrix(double kesi, double ita) {
        Matrix matrix;
        double[][] arr = new double[2][2];
        Matrix fai_kesi = getFai_kesiMatrix(kesi, ita);
        Matrix fai_ita = getFai_itaMatrix(kesi, ita);
        Matrix x_matrix = getX_matrix();
        Matrix y_matrix = getY_matrix();
        arr[0][0] = Matrix.cross(fai_kesi, x_matrix).toArray()[0][0];
        arr[0][1] = Matrix.cross(fai_kesi, y_matrix).toArray()[0][0];
        arr[1][0] = Matrix.cross(fai_ita, x_matrix).toArray()[0][0];
        arr[1][1] = Matrix.cross(fai_ita, y_matrix).toArray()[0][0];
        matrix = new Matrix(arr);
        return matrix;
    }

    public double[] getFai_kesi(double kesi, double ita) {//fai对kesi求偏导
        double[] arr = new double[9];
        arr[0] = ita * (ita - 1) * (2 * kesi - 1) / 4.;
        arr[1] = -1 * kesi * ita * (ita - 1);
        arr[2] = ita * (ita - 1) * (2 * kesi + 1) / 4.;
        arr[3] = (1 - ita * ita) * (2 * kesi - 1) / 2.;
        arr[4] = -2 * kesi * (1 - ita * ita);
        arr[5] = (1 - ita * ita) * (2 * kesi + 1) / 2.;
        arr[6] = ita * (ita + 1) * (2 * kesi - 1) / 4.;
        arr[7] = -1 * kesi * ita * (ita + 1);
        arr[8] = ita * (ita + 1) * (2 * kesi + 1) / 4.;
        return arr;
    }

    public Matrix getFai_kesiMatrix(double kesi, double ita) {
        return new Matrix(true, getFai_kesi(kesi, ita));
    }

    public double[] getFai_ita(double kesi, double ita) {//fai对ita求偏导
        double[] arr = new double[9];
        arr[0] = kesi * (kesi - 1) * (2 * ita - 1) / 4.;
        arr[1] = (1 - kesi * kesi) * (2 * ita - 1) / 2.;
        arr[2] = kesi * (kesi + 1) * (2 * ita - 1) / 4.;
        arr[3] = -kesi * ita * (kesi - 1);
        arr[4] = -2. * ita * (1 - kesi * kesi);
        arr[5] = -kesi * ita * (kesi + 1);
        arr[6] = kesi * (kesi - 1) * (2 * ita + 1) / 4.;
        arr[7] = (1 - kesi * kesi) * (2 * ita + 1) / 2.;
        arr[8] = kesi * (kesi + 1) * (2 * ita + 1) / 4.;
        return arr;
    }

    public Matrix getFai_itaMatrix(double kesi, double ita) {
        return new Matrix(true, getFai_ita(kesi, ita));
    }

    private Matrix getX_matrix() {
        double[] arr = new double[9];
        for (int i = 0; i <= 8; i++) {
            arr[i] = point[i].getX();
        }
        return new Matrix(false, arr);
    }

    private Matrix getY_matrix() {
        double[] arr = new double[9];
        for (int i = 0; i <= 8; i++) {
            arr[i] = point[i].getY();
        }
        return new Matrix(false, arr);
    }

}
