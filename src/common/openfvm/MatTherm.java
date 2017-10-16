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
public class MatTherm {
    private String spheat;
    private String thcond;

    /**
     * @return the spheat
     */
    public String getSpheat() {
        return spheat;
    }

    /**
     * @param spheat the spheat to set
     */
    public void setSpheat(String spheat) {
        this.spheat = spheat;
    }

    /**
     * @return the thcond
     */
    public String getThcond() {
        return thcond;
    }

    /**
     * @param thcond the thcond to set
     */
    public void setThcond(String thcond) {
        this.thcond = thcond;
    }
}
