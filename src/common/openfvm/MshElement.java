/*
 * OpenFVM的struct {}msh_element
 */
package common.openfvm;

import point.Point3D;

/**
 *
 * @author Administrator
 */
public class MshElement {

    private int index;

    private int type;

    private Point3D normal;
    private Point3D celement;

    private int nbnodes;
    private int[] node;

    private int nbfaces;
    private int[] face;

    private double dp;
    private double Lp;
    private double Ap;
    private double Vp;

    private int physreg;
    private int elemreg;

    private int process;

    private int bc;

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the normal
     */
    public Point3D getNormal() {
        return normal;
    }

    /**
     * @param normal the normal to set
     */
    public void setNormal(Point3D normal) {
        this.normal = normal;
    }

    /**
     * @return the celement
     */
    public Point3D getCelement() {
        return celement;
    }

    /**
     * @param celement the celement to set
     */
    public void setCelement(Point3D celement) {
        this.celement = celement;
    }

    /**
     * @return the nbnodes
     */
    public int getNbnodes() {
        return nbnodes;
    }

    /**
     * @param nbnodes the nbnodes to set
     */
    public void setNbnodes(int nbnodes) {
        this.nbnodes = nbnodes;
    }

    public void setNodeSize(int size) {
        this.node = new int[size];
    }

    /**
     * @return the node
     */
    public int getNode(int i) {
        return node[i];
    }

    /**
     * @param node the node to set
     */
    public void setNode(int i, int nodeIndex) {
        this.node[i] = nodeIndex;
    }

    /**
     * @return the nbfaces
     */
    public int getNbfaces() {
        return nbfaces;
    }

    /**
     * @param nbfaces the nbfaces to set
     */
    public void setNbfaces(int nbfaces) {
        this.nbfaces = nbfaces;
    }
    
    public void setFaceSize(int size){
        this.face=new int[size];
    }

    /**
     * @return the face
     */
    public int getFace(int i) {
        return face[i];
    }

    /**
     * @param faceIndex the face to set
     */
    public void setFace(int i, int faceIndex) {
        this.face[i] = faceIndex;
    }

    /**
     * @return the dp
     */
    public double getDp() {
        return dp;
    }

    /**
     * @param dp the dp to set
     */
    public void setDp(double dp) {
        this.dp = dp;
    }

    /**
     * @return the Lp
     */
    public double getLp() {
        return Lp;
    }

    /**
     * @param Lp the Lp to set
     */
    public void setLp(double Lp) {
        this.Lp = Lp;
    }

    /**
     * @return the Ap
     */
    public double getAp() {
        return Ap;
    }

    /**
     * @param Ap the Ap to set
     */
    public void setAp(double Ap) {
        this.Ap = Ap;
    }

    /**
     * @return the Vp
     */
    public double getVp() {
        return Vp;
    }

    /**
     * @param Vp the Vp to set
     */
    public void setVp(double Vp) {
        this.Vp = Vp;
    }

    /**
     * @return the physreg
     */
    public int getPhysreg() {
        return physreg;
    }

    /**
     * @param physreg the physreg to set
     */
    public void setPhysreg(int physreg) {
        this.physreg = physreg;
    }

    /**
     * @return the elemreg
     */
    public int getElemreg() {
        return elemreg;
    }

    /**
     * @param elemreg the elemreg to set
     */
    public void setElemreg(int elemreg) {
        this.elemreg = elemreg;
    }

    /**
     * @return the process
     */
    public int getProcess() {
        return process;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(int process) {
        this.process = process;
    }

    /**
     * @return the bc
     */
    public int getBc() {
        return bc;
    }

    /**
     * @param bc the bc to set
     */
    public void setBc(int bc) {
        this.bc = bc;
    }
}
