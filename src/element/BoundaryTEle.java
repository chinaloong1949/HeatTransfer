/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

/**
 *
 * @author Administrator
 */
public class BoundaryTEle {

    private int elementId;
    private int[] surface;
    private double t0;
    private double t1;
    private double t2;
    private double t3;

    public BoundaryTEle(int id, int[] surface, double t0, double t1, double t2,
            double t3) {
        this.elementId = id;
        this.surface = surface;
        this.t0 = t0;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }

    /**
     * @return the elementId
     */
    public int getElementId() {
        return elementId;
    }

    /**
     * @param elementId the elementId to set
     */
    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    /**
     * @return the surface
     */
    public int[] getSurface() {
        return surface;
    }

    /**
     * @param surface the surface to set
     */
    public void setSurface(int[] surface) {
        this.surface = surface;
    }

    /**
     * @return the t0
     */
    public double getT0() {
        return t0;
    }

    /**
     * @param t0 the t0 to set
     */
    public void setT0(double t0) {
        this.t0 = t0;
    }

    /**
     * @return the t1
     */
    public double getT1() {
        return t1;
    }

    /**
     * @param t1 the t1 to set
     */
    public void setT1(double t1) {
        this.t1 = t1;
    }

    /**
     * @return the t2
     */
    public double getT2() {
        return t2;
    }

    /**
     * @param t2 the t2 to set
     */
    public void setT2(double t2) {
        this.t2 = t2;
    }

    /**
     * @return the t3
     */
    public double getT3() {
        return t3;
    }

    /**
     * @param t3 the t3 to set
     */
    public void setT3(double t3) {
        this.t3 = t3;
    }

}
