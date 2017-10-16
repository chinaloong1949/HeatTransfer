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
public class BcdSurface {
    private int physreg;
    private BcdType bc;
    private String fu;
    private String fv;
    private String fw;
    private String fp;
    private String fT;
    private String fs;

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
     * @return the bd
     */
    public BcdType getBc() {
        return bc;
    }

    /**
     * @param bd the bd to set
     */
    public void setBc(BcdType bc) {
        this.bc = bc;
    }

    /**
     * @return the fu
     */
    public String getFu() {
        return fu;
    }

    /**
     * @param fu the fu to set
     */
    public void setFu(String fu) {
        this.fu = fu;
    }

    /**
     * @return the fv
     */
    public String getFv() {
        return fv;
    }

    /**
     * @param fv the fv to set
     */
    public void setFv(String fv) {
        this.fv = fv;
    }

    /**
     * @return the fw
     */
    public String getFw() {
        return fw;
    }

    /**
     * @param fw the fw to set
     */
    public void setFw(String fw) {
        this.fw = fw;
    }

    /**
     * @return the fp
     */
    public String getFp() {
        return fp;
    }

    /**
     * @param fp the fp to set
     */
    public void setFp(String fp) {
        this.fp = fp;
    }

    /**
     * @return the fT
     */
    public String getfT() {
        return fT;
    }

    /**
     * @param fT the fT to set
     */
    public void setfT(String fT) {
        this.fT = fT;
    }

    /**
     * @return the fs
     */
    public String getFs() {
        return fs;
    }

    /**
     * @param fs the fs to set
     */
    public void setFs(String fs) {
        this.fs = fs;
    }
}
