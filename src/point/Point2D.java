/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package point;

/**
 *
 * @author yk
 */
public class Point2D {

    private double x;
    private double y;
    private int pointId;
    private double u = 0;
    private double v = 0;
    private boolean isBoundaryPoint = false;

    public Point2D(double x, double y) {
        this.pointId = 0;
        this.x = x;
        this.y = y;
    }

    public Point2D(int pointId, double x, double y) {
        this.pointId = pointId;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double[] getVelocity() {
        double[] velocity = new double[2];
        velocity[0] = u;
        velocity[1] = v;
        return velocity;
    }

    public void setVelocity(double u, double v) {
        this.u = u;
        this.v = v;
        this.isBoundaryPoint = true;
    }

    public static void printTitle() {
        System.out.println(" 节点编号       x坐标       y坐标   是否边界节点     速度u     速度v");
    }

    public void printPoint2D() {
        System.out.print(String.format("%8d", pointId) + String.format("%12.4f", x) + String.format("%12.4f", y));
        if (!isBoundaryPoint) {
            System.out.print("        否    ");
        } else {
            System.out.print("        是    " + String.format("%10.4f", u) + String.format("%10.4f", v));

        }
        System.out.println("");
    }
}
