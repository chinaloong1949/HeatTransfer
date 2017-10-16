/*
 * 该类用来产生一个【-1,1】之间的高斯积分点序列，传入的整数位高斯积分点个数
 */
package common;

import point.Point2D;

/**
 *
 * @author yk
 */
public class GaussPoint {

    Point2D[] pointList;

    public GaussPoint(int num) {
        //

        switch (num) {
            case 1:
                pointList = new Point2D[1];

                pointList[0] = new Point2D(0.0, 2.0);
                break;
            case 2:
                pointList = new Point2D[2];

                pointList[0] = new Point2D(-0.57735027, 1.0);
                pointList[1] = new Point2D(0.57735027, 1.0);
                break;
            case 3:
                pointList = new Point2D[3];

                pointList[0] = new Point2D(-0.77459667, 0.55555556);
                pointList[1] = new Point2D(0.0, 0.88888889);
                pointList[2] = new Point2D(0.77459667, 0.55555556);
                break;
            case 4:
                pointList = new Point2D[4];

                pointList[0] = new Point2D(-0.86113631, 0.34785485);
                pointList[1] = new Point2D(-0.33998104, 0.65214515);
                pointList[2] = new Point2D(0.33998104, 0.65214515);
                pointList[3] = new Point2D(0.86113631, 0.34785485);
                break;
            case 5:
                pointList = new Point2D[5];

                pointList[0] = new Point2D(-0.90617985, 0.23692689);
                pointList[1] = new Point2D(-0.53846931, 0.47862867);
                pointList[2] = new Point2D(0.0, 0.56888889);
                pointList[3] = new Point2D(0.53846931, 0.47862867);
                pointList[4] = new Point2D(0.90617985, 0.23692689);
                break;
            case 6:
                pointList = new Point2D[6];

                pointList[0] = new Point2D(0.932465514203152, 0.171324492379170);
                pointList[1] = new Point2D(0.661209386466265, 0.360761573048139);
                pointList[2] = new Point2D(0.238619186083197, 0.467913934572691);
                pointList[3] = new Point2D(-0.932465514203152, 0.171324492379170);
                pointList[4] = new Point2D(-0.661209386466265, 0.360761573048139);
                pointList[5] = new Point2D(-0.238619186083197, 0.467913934572691);

                break;
            default:
                break;
        }
    }

    public Point2D[] getPointList() {
        return pointList;
    }

}
