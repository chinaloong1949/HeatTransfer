package simple;

import element.EleTri_3;
import point.Point2D;

/*
 * 参见《计算流体力学有限元方法及其编程详解·毕超》第二章2.1.1实例
 */
/**
 *
 * @author yk
 */
public class Example2_1_1 {

    Point2D[] pointList = null;
    EleTri_3[] elementList = null;
    int numOfElement = 1;

    public Example2_1_1() {
        initData();
        //首先计算每个单元的a,b,c,并存入Element数据中，方便以后引用
        for (int num = 0; num <= numOfElement - 1; num++) {
            //建立一个临时数组point，里面存放当前单元的三个节点
            Point2D[] point = new Point2D[3];
            point[0] = pointList[elementList[num].node1];
            point[1] = pointList[elementList[num].node2];
            point[2] = pointList[elementList[num].node3];
            double a[][] = new double[3][3];//a用来存放a,b,c的数据，这里用一个3*3的数组a来存储
            double k_e[][] = new double[3][3];//参见《计算流体力学有限元方法及其编程详解·毕超》式2.21
            //其中a[0]存储a,a[1]存储b,a[2]存储c；
            double area = getTriAngleArea(point[0], point[1], point[2]);
            int i, j, k;
            i = 0;
            j = 1;
            k = 2;
            a[0][i] = (point[j].getX() * point[k].getY() - point[k].getX() * point[j].getY()) / (2 * area);
            a[1][i] = (point[j].getY() - point[k].getY()) / (2 * area);
            a[2][i] = (point[k].getX() - point[j].getY()) / (2 * area);
            i = 1;
            j = 2;
            k = 0;
            a[0][i] = (point[j].getX() * point[k].getY() - point[k].getX() * point[j].getY()) / (2 * area);
            a[1][i] = (point[j].getY() - point[k].getY()) / (2 * area);
            a[2][i] = (point[k].getX() - point[j].getY()) / (2 * area);
            i = 2;
            j = 0;
            k = 1;
            a[0][i] = (point[j].getX() * point[k].getY() - point[k].getX() * point[j].getY()) / (2 * area);
            a[1][i] = (point[j].getY() - point[k].getY()) / (2 * area);
            a[2][i] = (point[k].getX() - point[j].getY()) / (2 * area);
            elementList[num].setA(a);

            k_e[0][0] = (a[1][0] * a[1][0] + a[2][0] * a[2][0]) * area;
            k_e[0][1] = (a[1][0] * a[1][1] + a[2][0] * a[2][1]) * area;
            k_e[0][2] = (a[1][0] * a[1][2] + a[2][0] * a[2][2]) * area;

            k_e[1][0] = (a[1][1] * a[1][0] + a[2][1] * a[2][0]) * area;
            k_e[1][1] = (a[1][1] * a[1][1] + a[2][1] * a[2][1]) * area;
            k_e[1][2] = (a[1][1] * a[1][2] + a[2][1] * a[2][2]) * area;

            k_e[2][0] = (a[1][2] * a[1][0] + a[2][2] * a[2][0]) * area;
            k_e[2][1] = (a[1][2] * a[1][1] + a[2][2] * a[2][1]) * area;
            k_e[2][2] = (a[1][2] * a[1][2] + a[2][2] * a[2][2]) * area;
        }

        //组合系数K
        double[][] K = new double[pointList.length][pointList.length];
        for (int i = 0; i <= pointList.length - 1; i++) {
            for (int j = 0; j <= pointList.length - 1; j++) {
                K[i][j] = 0;
            }
        }
        for (int i = 0; i <= numOfElement - 1; i++) {
            K[elementList[i].node1][elementList[i].node1] = K[elementList[i].node1][elementList[i].node1] + elementList[i].k_e[1][1];
            K[elementList[i].node1][elementList[i].node2] = K[elementList[i].node1][elementList[i].node2] + elementList[i].k_e[1][2];
            K[elementList[i].node1][elementList[i].node3] = K[elementList[i].node1][elementList[i].node3] + elementList[i].k_e[1][3];
            K[elementList[i].node2][elementList[i].node1] = K[elementList[i].node2][elementList[i].node1] + elementList[i].k_e[2][1];
            K[elementList[i].node2][elementList[i].node2] = K[elementList[i].node2][elementList[i].node2] + elementList[i].k_e[2][2];
            K[elementList[i].node2][elementList[i].node3] = K[elementList[i].node2][elementList[i].node3] + elementList[i].k_e[2][3];
            K[elementList[i].node3][elementList[i].node1] = K[elementList[i].node3][elementList[i].node1] + elementList[i].k_e[3][1];
            K[elementList[i].node3][elementList[i].node2] = K[elementList[i].node3][elementList[i].node2] + elementList[i].k_e[3][2];
            K[elementList[i].node3][elementList[i].node3] = K[elementList[i].node3][elementList[i].node3] + elementList[i].k_e[3][3];
        }

    }

    private void initData() {

    }

    private double getTriAngleArea(Point2D point1, Point2D point2, Point2D point3) {
        double area = 0;
        int i, j, k;
        Point2D[] point = new Point2D[3];
        point[0] = point1;
        point[1] = point2;
        point[2] = point3;
        i = 0;
        j = 1;
        k = 2;
        area = ((point[j].getX() - point[i].getX()) * (point[k].getY() - point[i].getY())
                - (point[k].getX() - point[i].getX()) * (point[j].getY() - point[i].getY())) * 0.5;
        i = 1;
        j = 2;
        k = 0;
        area = area + ((point[j].getX() - point[i].getX()) * (point[k].getY() - point[i].getY())
                - (point[k].getX() - point[i].getX()) * (point[j].getY() - point[i].getY())) * 0.5;
        i = 2;
        j = 0;
        k = 1;
        area = area + ((point[j].getX() - point[i].getX()) * (point[k].getY() - point[i].getY())
                - (point[k].getX() - point[i].getX()) * (point[j].getY() - point[i].getY())) * 0.5;
        return area;
    }

}
