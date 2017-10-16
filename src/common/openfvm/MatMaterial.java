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
public class MatMaterial {

    private double[] psi = new double[2];
    private String[] dens = new String[2];
    private String[] visc = new String[2];
    private MatTherm[] therm = new MatTherm[2];
    private MatMech[] mech = new MatMech[2];

    private double tens;
    private double bthcond;

    /**
     * @return the psi
     */
    public double[] getPsi() {
        return psi;
    }

    /**
     * @param psi the psi to set
     */
    public void setPsi(int i, double psi) {
        this.psi[i] = psi;
    }

    /**
     * @return the dens
     */
    public String[] getDens() {
        return dens;
    }

    /**
     * @param dens the dens to set
     */
    public void setDens(int i, String dens) {
        this.dens[i] = dens;
    }

    /**
     * @return the visc
     */
    public String[] getVisc() {
        return visc;
    }

    /**
     * @param visc the visc to set
     */
    public void setVisc(int i, String visc) {
        this.visc[i] = visc;
    }

    /**
     * @return the therm
     */
    public MatTherm[] getTherm() {
        return therm;
    }

    /**
     * @param therm the therm to set
     */
    public void setTherm(int i, MatTherm therm) {
        this.therm[i] = therm;
    }

    /**
     * @return the mech
     */
    public MatMech[] getMech() {
        return mech;
    }

    /**
     * @param mech the mech to set
     */
    public void setMech(int i,MatMech mech) {
        this.mech[i] = mech;
    }

    /**
     * @return the tens
     */
    public double getTens() {
        return tens;
    }

    /**
     * @param tens the tens to set
     */
    public void setTens(double tens) {
        this.tens = tens;
    }

    /**
     * @return the bthcond
     */
    public double getBthcond() {
        return bthcond;
    }

    /**
     * @param bthcond the bthcond to set
     */
    public void setBthcond(double bthcond) {
        this.bthcond = bthcond;
    }
}
