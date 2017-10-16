/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

/**
 *
 * @author yk
 */
public class BoundaryPEle {

    private int elementId;
    private int boundarySurface;
    private double cos_theta_x;
    private double cos_theta_y;
    private double cos_theta_z;
    private double p1;
    private double p2;
    private double p3;
    private double p4;

    public BoundaryPEle(int id, int surface, double cosx, double cosy,
            double cosz, double p1, double p2, double p3, double p4) {
        this.elementId = id;
        this.boundarySurface = surface;
        this.cos_theta_x = cosx;
        this.cos_theta_y = cosy;
        this.cos_theta_z = cosz;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public boolean isEqual(BoundaryPEle boundary) {
        //如果该类的两个实例的elementId和boundarySurface相等，就认为两个实例相同
        boolean flag = false;
        if (boundary.getElementId() == this.elementId
                && boundary.getBoundarySurface() == this.boundarySurface) {
            flag = true;
        }
        return flag;
    }

    /**
     * @return the elementId
     */
    public int getElementId() {
        return elementId;
    }

    /**
     * @return the boundarySurface
     */
    public int getBoundarySurface() {
        return boundarySurface;
    }

    /**
     * @return the cos_theta_x
     */
    public double getCos_theta_x() {
        return cos_theta_x;
    }

    /**
     * @return the cos_theta_y
     */
    public double getCos_theta_y() {
        return cos_theta_y;
    }

    /**
     * @return the cos_theta_z
     */
    public double getCos_theta_z() {
        return cos_theta_z;
    }

    /**
     * @return the p1
     */
    public double getP1() {
        return p1;
    }

    /**
     * @return the p2
     */
    public double getP2() {
        return p2;
    }

    /**
     * @return the p3
     */
    public double getP3() {
        return p3;
    }

    /**
     * @return the p4
     */
    public double getP4() {
        return p4;
    }
}
