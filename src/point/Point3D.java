/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package point;

import java.util.ArrayList;

/**
 *
 * @author yk
 */
public class Point3D {

    private double x;
    private double y;
    private double z;
    private Point3D[] endPoints = null;
    private ArrayList<Point3D[]> pairPoints = new ArrayList<>();

    private int pointId = -1;

    public Point3D(int pointId, double x, double y, double z) {
        this.pointId = pointId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean coincide(Point3D p) {
        boolean isCoincide = false;
        if (p.x == this.x && p.y == this.y && p.z == this.z) {
            isCoincide = true;
        }
        return isCoincide;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public int getPointId() {
        return pointId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public static Point3D getMiddlePoint3D(Point3D p1, Point3D p2) {
        Point3D mid = new Point3D(-1, (p1.getX() + p2.getX()) / 2.0,
                (p1.getY() + p2.getY()) / 2.0,
                (p1.getZ() + p2.getZ()) / 2.0);
        return mid;
    }

    public static void printTitle() {
        System.out.println(" 节点编号       x坐标       y坐标       z坐标   是否"
                + "边界节点     速度u     速度v");
    }

    public void printPoint3D() {
        System.out.print(String.format("%8d", pointId) + String.format("%12.4f",
                x) + String.format("%12.4f", y) + String.format("%12.4f", z));

        System.out.println("");
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * @return the endPoints
     */
    public Point3D[] getEndPoints() {
        return endPoints;
    }

    /**
     * @param endPoints the endPoints to set
     */
    public void setEndPoints(Point3D point1, Point3D point2) {
        this.endPoints = new Point3D[2];
        this.endPoints[0] = point1;
        this.endPoints[1] = point2;
    }

    public void addMidPoints(Point3D midPoint, Point3D anotherEndPoint) {
        Point3D[] p = new Point3D[]{midPoint, anotherEndPoint};
        this.pairPoints.add(p);
    }

    public Point3D getMidPoints(Point3D point) {
        Point3D[] pairPoint;
        for (Point3D[] pairPoint1 : this.pairPoints) {
            pairPoint = pairPoint1;
            if (pairPoint[1].equals(point)) {
                return pairPoint[0];
            }
        }
        return null;
    }

}
