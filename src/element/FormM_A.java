/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 *
 * @author Administrator
 */
public class FormM_A {

    private double[] fyp=new double[4];
    private double area;

    public FormM_A(double kesi, double ita, double[] x, double[] y, double[] z) {
        formM_A(kesi, ita, x, y, z);
    }

    private double[] formM_A(double kesi,
            double ita, double[] x, double[] y, double[] z) {

        double[][] jacobi = new double[2][3];
        double[] fyp_kesi = new double[4];
        double[] fyp_ita = new double[4];
        fyp[0] = 0.25 * (1 - kesi) * (1 - ita);
        fyp[1] = 0.25 * (1 + kesi) * (1 - ita);
        fyp[2] = 0.25 * (1 + kesi) * (1 + ita);
        fyp[3] = 0.25 * (1 - kesi) * (1 + ita);

        fyp_kesi[0] = -0.25 * (1 - ita);
        fyp_kesi[1] = 0.25 * (1 - ita);
        fyp_kesi[2] = 0.25 * (1 + ita);
        fyp_kesi[3] = -0.25 * (1 + ita);
        fyp_ita[0] = -0.25 * (1 - kesi);
        fyp_ita[1] = -0.25 * (1 + kesi);
        fyp_ita[2] = 0.25 * (1 + kesi);
        fyp_ita[3] = 0.25 * (1 - kesi);

        for (int m = 0; m <= 3; m++) {
            jacobi[0][0] = jacobi[0][0] + fyp_kesi[m] * x[m];
            jacobi[0][1] = jacobi[0][1] + fyp_kesi[m] * y[m];
            jacobi[0][2] = jacobi[0][2] + fyp_kesi[m] * z[m];
            jacobi[1][0] = jacobi[1][0] + fyp_ita[m] * x[m];
            jacobi[1][1] = jacobi[1][1] + fyp_ita[m] * y[m];
            jacobi[1][2] = jacobi[1][2] + fyp_ita[m] * z[m];
        }

        //确定微元面积A
        area=0.0;
        area = area + pow((jacobi[0][0] * jacobi[1][1]
                - jacobi[0][1] * jacobi[1][0]), 2);
        area = area + pow((jacobi[0][1] * jacobi[1][2]
                - jacobi[0][2] * jacobi[1][1]), 2);
        area = area + pow((jacobi[0][2] * jacobi[1][0]
                - jacobi[0][0] * jacobi[1][2]), 2);
        area = sqrt(area);
        return fyp;
    }

    public double[] getFyp() {
        return this.fyp;
    }

    public double getArea() {
        return this.area;
    }
}
