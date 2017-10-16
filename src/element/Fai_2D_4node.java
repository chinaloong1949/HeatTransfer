/*
 * 平面四边形 二维一次四节点单元
 */
package element;

import common.Matrix;

/**
 *
 * @author yk
 */
public class Fai_2D_4node {

    public double[] getN_i(double x1, double y2) {
        double[] arr = new double[4];
        arr[0] = (1 - x1) * (1 - y2) / 4.;
        arr[1] = (1 + x1) * (1 - y2) / 4.;
        arr[2] = (1 + x1) * (1 + y2) / 4.;
        arr[3] = (1 - x1) * (1 + y2) / 4.;
        return arr;
    }

    public Matrix getN_iMatrix(boolean isHorizental, double x1, double y2) {
        return new Matrix(isHorizental, getN_i(x1, y2));
    }

    public Matrix getJacobi() {
        Matrix matrix = null;

        return matrix;
    }

}
