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
public class EleTri_3 {

    public int node1;
    public int node2;
    public int node3;
    public int elementId;
    public int PID;
    public double[][] a = new double[3][3];
    public double[][] k_e = new double[3][3];

    public EleTri_3(int node1, int node2, int node3) {
        this.node1 = node1;
        this.node2 = node2;
        this.node3 = node3;

    }

    public EleTri_3(int EID, int PID, int node1, int node2, int node3) {
        this.elementId = EID;
        this.PID = PID;
        this.node1 = node1;
        this.node2 = node2;
        this.node3 = node3;
    }

    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public void setA(double[][] a) {
        this.a = a;
    }

    public void setK_e(double[][] k_e) {
        this.k_e = k_e;
    }

}
