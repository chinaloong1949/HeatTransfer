/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.openfvm;

/**
 *
 * @author Administrator
 */
public class OctNode {

    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;
    private double zmin;
    private double zmax;
    private double xmid;
    private double ymid;
    private double zmid;

    private int[] entities;
    private int nbentities;
    private int[] cnodes = new int[8];
    private OctNode[] nodes = new OctNode[8];

    /**
     * @return the xmin
     */
    public double getXmin() {
        return xmin;
    }

    /**
     * @param xmin the xmin to set
     */
    public void setXmin(double xmin) {
        this.xmin = xmin;
    }

    /**
     * @return the xmax
     */
    public double getXmax() {
        return xmax;
    }

    /**
     * @param xmax the xmax to set
     */
    public void setXmax(double xmax) {
        this.xmax = xmax;
    }

    /**
     * @return the ymin
     */
    public double getYmin() {
        return ymin;
    }

    /**
     * @param ymin the ymin to set
     */
    public void setYmin(double ymin) {
        this.ymin = ymin;
    }

    /**
     * @return the ymax
     */
    public double getYmax() {
        return ymax;
    }

    /**
     * @param ymax the ymax to set
     */
    public void setYmax(double ymax) {
        this.ymax = ymax;
    }

    /**
     * @return the zmin
     */
    public double getZmin() {
        return zmin;
    }

    /**
     * @param zmin the zmin to set
     */
    public void setZmin(double zmin) {
        this.zmin = zmin;
    }

    /**
     * @return the zmax
     */
    public double getZmax() {
        return zmax;
    }

    /**
     * @param zmax the zmax to set
     */
    public void setZmax(double zmax) {
        this.zmax = zmax;
    }

    /**
     * @return the xmid
     */
    public double getXmid() {
        return xmid;
    }

    /**
     * @param xmid the xmid to set
     */
    public void setXmid(double xmid) {
        this.xmid = xmid;
    }

    /**
     * @return the ymid
     */
    public double getYmid() {
        return ymid;
    }

    /**
     * @param ymid the ymid to set
     */
    public void setYmid(double ymid) {
        this.ymid = ymid;
    }

    /**
     * @return the zmid
     */
    public double getZmid() {
        return zmid;
    }

    /**
     * @param zmid the zmid to set
     */
    public void setZmid(double zmid) {
        this.zmid = zmid;
    }

    public void setEntitiesSize(int size) {
        this.entities = new int[size];
    }

    /**
     * @return the entities
     */
    public int getEntities(int i) {
        return entities[i];
    }

    /**
     * @param entities the entities to set
     */
    public void setEntities(int i, int value) {
        this.entities[i] = value;
    }

    /**
     * @return the nbentities
     */
    public int getNbentities() {
        return nbentities;
    }

    /**
     * @param nbentities the nbentities to set
     */
    public void setNbentities(int nbentities) {
        this.nbentities = nbentities;
    }

    /**
     * @return the cnodes
     */
    public int getCnodes(int i) {
        return cnodes[i];
    }

    /**
     * @param cnodes the cnodes to set
     */
    public void setCnodes(int[] cnodes) {
        this.cnodes = cnodes;
    }

    /**
     * @return the nodes
     */
    public OctNode getNodes(int i) {
        return nodes[i];
    }

    /**
     * @param nodes the nodes to set
     */
    public void setNodes(int i,OctNode node) {
        this.nodes[i] = node;
    }
}
