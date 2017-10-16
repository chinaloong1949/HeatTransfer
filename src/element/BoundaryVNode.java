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
public class BoundaryVNode {

    private int NodeId;
    private double u;
    private double v;
    private double w;

    public BoundaryVNode(int id, double u, double v, double w) {
        this.NodeId = id;
        this.u = u;
        this.v = v;
        this.w = w;
    }

    /**
     * @return the elementId
     */
    public int getNodeId() {
        return NodeId;
    }

    /**
     * @return the u
     */
    public double getU() {
        return u;
    }

    /**
     * @return the v
     */
    public double getV() {
        return v;
    }

    /**
     * @return the w
     */
    public double getW() {
        return w;
    }

    /**
     * @param u the u to set
     */
    public void setU(double u) {
        this.u = u;
    }

    /**
     * @param v the v to set
     */
    public void setV(double v) {
        this.v = v;
    }

    /**
     * @param w the w to set
     */
    public void setW(double w) {
        this.w = w;
    }

}
