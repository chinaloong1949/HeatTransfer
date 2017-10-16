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
public class Part {

    private int PID;
    private int SECID;
    private int MID;
    private int EOSID;
    private int HGID;
    private int GRAV;
    private int ADPOPT;
    private int TMID;

    public Part() {
        PID = -1;
    }

    /**
     * @return the PID
     */
    public int getPID() {
        return PID;
    }

    /**
     * @param PID the PID to set
     */
    public void setPID(int PID) {
        this.PID = PID;
    }

    /**
     * @return the SECID
     */
    public int getSECID() {
        return SECID;
    }

    /**
     * @param SECID the SECID to set
     */
    public void setSECID(int SECID) {
        this.SECID = SECID;
    }

    /**
     * @return the MID
     */
    public int getMID() {
        return MID;
    }

    /**
     * @param MID the MID to set
     */
    public void setMID(int MID) {
        this.MID = MID;
    }

    /**
     * @return the EOSID
     */
    public int getEOSID() {
        return EOSID;
    }

    /**
     * @param EOSID the EOSID to set
     */
    public void setEOSID(int EOSID) {
        this.EOSID = EOSID;
    }

    /**
     * @return the HGID
     */
    public int getHGID() {
        return HGID;
    }

    /**
     * @param HGID the HGID to set
     */
    public void setHGID(int HGID) {
        this.HGID = HGID;
    }

    /**
     * @return the GRAV
     */
    public int getGRAV() {
        return GRAV;
    }

    /**
     * @param GRAV the GRAV to set
     */
    public void setGRAV(int GRAV) {
        this.GRAV = GRAV;
    }

    /**
     * @return the ADPOPT
     */
    public int getADPOPT() {
        return ADPOPT;
    }

    /**
     * @param ADPOPT the ADPOPT to set
     */
    public void setADPOPT(int ADPOPT) {
        this.ADPOPT = ADPOPT;
    }

    /**
     * @return the TMID
     */
    public int getTMID() {
        return TMID;
    }

    /**
     * @param TMID the TMID to set
     */
    public void setTMID(int TMID) {
        this.TMID = TMID;
    }
}
