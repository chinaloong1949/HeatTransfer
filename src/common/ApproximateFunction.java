/*
 * 1.可以调用该类的polynomialInterpolation()方法求给定数据点的多项式插值方法的系数数组
 * 2.
 */
package common;

import point.Point2D;

/**
 *
 * @author yk
 */
public class ApproximateFunction {

    Point2D[] pointList;
    int pointNum;

    public ApproximateFunction(Point2D[] pointList) {
        this.pointList = pointList;
        pointNum = pointList.length;

    }

    public double[] polynomialInterpolation() {
        double[] xList = new double[pointNum];
        double[] solution = new double[pointNum];
        for (int i = 0; i <= pointNum - 1; i++) {
            xList[i] = pointList[i].getX();
            solution[i] = 0;
        }
        double detValue = new Vandermonde(xList).getDetValue();
        if (detValue == 0) {
            //Vandermonde行列式等于0，差值多项式不存在或者不唯一
            System.out.println("Vandermonde行列式等于0，差值多项式不存在或者不唯一");
        } else {
            double[][] array = new double[pointNum][pointNum + 1];
            for (int i = 0; i <= pointNum - 1; i++) {
                for (int j = 0; j <= pointNum - 1; j++) {
                    array[i][j] = Math.pow(xList[i], j);
                }
                array[i][pointNum] = pointList[i].getY();
            }
            Matrix matrix = new Matrix(array);
            matrix.printMatrix();
            solution = matrix.getSolution();
        }
        return solution;
    }

    public double lagrangeInterpolation(double x) {
        //根据给定数据点求x处的函数值
        double solution = 0;
        double[] l = new double[pointNum];
        double fenzi;//oumiga=fenzi/(x-x_i)
        double fenmu;//a=1/fenmu
        for (int i = 0; i <= pointNum - 1; i++) {
            fenzi = 1;
            fenmu = 1;
            for (int k = 0; k <= pointNum - 1; k++) {
                if (k != i) {
                    fenmu = fenmu * (pointList[i].getX() - pointList[k].getX());
                    fenzi = fenzi * (x - pointList[k].getX());
                }
            }
            l[i] = fenzi / fenmu;
            solution = solution + l[i] * pointList[i].getY();
        }

        return solution;
    }

    public void newtonInterpolation() {

    }

    public double chaShang(double[] xList) {
        double result;
        int n = xList.length;
        if (n == 1) {
            for (int i = 0; i <= pointNum - 1; i++) {
                if (xList[0] == pointList[i].getX()) {
                    result = pointList[i].getY();
                }
            }
        }
        double[] xList1 = new double[n - 1];
        double[] xList2 = new double[n - 1];
        for (int i = 0; i <= n - 3; i++) {
            xList1[i] = xList[i];
            xList2[i] = xList[i];
        }
        xList1[n - 2] = xList[n];
        xList2[n - 2] = xList[n - 1];
        result = (chaShang(xList1) - chaShang(xList2)) / (pointList[n].getX() - pointList[n - 1].getX());

        return result;
    }

}
