/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author yk
 */
public class Boundary {

    private String boundaryName = null;
    private int nodeSetId = -1;
    private String varName = "";
    private double value = 0;
    private double u = 0;
    private double v = 0;
    private double w = 0;

    public Boundary(String boundaryName, int nodeSetId, String var, double value) {
        this.boundaryName = boundaryName;
        this.nodeSetId = nodeSetId;
        this.varName = var;
        this.value = value;
    }

    public Boundary(String boundaryName, int nodeSetId, String var,
            double u, double v, double w) {
        this.boundaryName = boundaryName;
        this.nodeSetId = nodeSetId;
        this.varName = var;
        this.u = u;
        this.v = v;
        this.w = w;
    }

    /**
     * @return the boundaryName
     */
    public String getBoundaryName() {
        return boundaryName;
    }

    /**
     * @return the nodeSetId
     */
    public int getNodeSetId() {
        return nodeSetId;
    }

    /**
     * @return the varName
     */
    public String getVarName() {
        return varName;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param nodeSetId the nodeSetId to set
     */
    public void setNodeSetId(int nodeSetId) {
        this.nodeSetId = nodeSetId;
    }

    /**
     * @param varName the varName to set
     */
    public void setVarName(String varName) {
        this.varName = varName;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the u
     */
    public double getU() {
        return u;
    }

    /**
     * @param u the u to set
     */
    public void setU(double u) {
        this.u = u;
    }

    /**
     * @return the v
     */
    public double getV() {
        return v;
    }

    /**
     * @param v the v to set
     */
    public void setV(double v) {
        this.v = v;
    }

    /**
     * @return the w
     */
    public double getW() {
        return w;
    }

    /**
     * @param w the w to set
     */
    public void setW(double w) {
        this.w = w;
    }

}
