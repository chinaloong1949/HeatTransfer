/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.openfvm;

import point.Point3D;

/**
 *
 * @author Administrator
 */
public class MshFace {

    private int index;

    private int type;

    private int nbnodes;
    private int[] node;

    private int element;

    private Point3D cface;
    private int pair;

    private Point3D n;

    private Point3D A;
    private double Aj;

    private Point3D d;
    private double dj;
    private double kj;
    private Point3D rpl;
    private Point3D rnl;

    private int physreg;
    private int elemreg;

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
    
    public void setNodeSize(int size){
        this.node=new int[size];
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
     * @return the element
     */
    public int getElement() {
        return element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(int element) {
        this.element = element;
    }

    /**
     * @return the cface
     */
    public Point3D getCface() {
        return cface;
    }

    /**
     * @param cface the cface to set
     */
    public void setCface(Point3D cface) {
        this.cface = cface;
    }

    /**
     * @return the pair
     */
    public int getPair() {
        return pair;
    }

    /**
     * @param pair the pair to set
     */
    public void setPair(int pair) {
        this.pair = pair;
    }

    /**
     * @return the n
     */
    public Point3D getN() {
        return n;
    }

    /**
     * @param n the n to set
     */
    public void setN(Point3D n) {
        this.n = n;
    }

    /**
     * @return the A
     */
    public Point3D getA() {
        return A;
    }

    /**
     * @param A the A to set
     */
    public void setA(Point3D A) {
        this.A = A;
    }

    /**
     * @return the Aj
     */
    public double getAj() {
        return Aj;
    }

    /**
     * @param Aj the Aj to set
     */
    public void setAj(double Aj) {
        this.Aj = Aj;
    }

    /**
     * @return the d
     */
    public Point3D getD() {
        return d;
    }

    /**
     * @param d the d to set
     */
    public void setD(Point3D d) {
        this.d = d;
    }

    /**
     * @return the dj
     */
    public double getDj() {
        return dj;
    }

    /**
     * @param dj the dj to set
     */
    public void setDj(double dj) {
        this.dj = dj;
    }

    /**
     * @return the kj
     */
    public double getKj() {
        return kj;
    }

    /**
     * @param kj the kj to set
     */
    public void setKj(double kj) {
        this.kj = kj;
    }

    /**
     * @return the rpl
     */
    public Point3D getRpl() {
        return rpl;
    }

    /**
     * @param rpl the rpl to set
     */
    public void setRpl(Point3D rpl) {
        this.rpl = rpl;
    }

    /**
     * @return the rnl
     */
    public Point3D getRnl() {
        return rnl;
    }

    /**
     * @param rnl the rnl to set
     */
    public void setRnl(Point3D rnl) {
        this.rnl = rnl;
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
