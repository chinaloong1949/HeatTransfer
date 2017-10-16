/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Administrator
 */
public class Parameter {

    private String ulength;
    private String umass;
    private String utime;
    private String uenergy;
    private String utemperature;

    private int inertia;

    private float dfactor;

    private float st;

    private int[] scheme = new int[6];

    private int restart;

    private int wbinary;

    private int steady;
    private int adjdt;

    private float maxCp;

    private float[] mtol = new float[6];
    private int[] miter = new int[6];

    private int northocor;
    private float orthof;

    private float[] ftol = new float[6];

    private int ncicsamsteps;
    private int ncicsamcor;
    private float kq;
    private int nsav;

    private int[] calc = new int[6];

    private int savflux;

    private int[] fsav = new int[6];
    private int[] csav = new int[6];

    private int fvec;
    private int cvec;

    private int smooth;

    private int[] vortex = new int[3];
    private int streamf;

    private float t0;

    private float t1;

    private float dt;
    private float[] g = new float[3];

    private int[] probe = new int[6];
    private int[] msolver = new int[6];

    private int[] mprecond = new int[6];
    private int[] timemethod = new int[6];

    private float[] ef = new float[6];
    private int fill;
    private float pf;
    private int intbcphysreg;

    /**
     * @return the ulength
     */
    public String getUlength() {
        return ulength;
    }

    /**
     * @param ulength the ulength to set
     */
    public void setUlength(String ulength) {
        this.ulength = ulength;
    }

    /**
     * @return the umass
     */
    public String getUmass() {
        return umass;
    }

    /**
     * @param umass the umass to set
     */
    public void setUmass(String umass) {
        this.umass = umass;
    }

    /**
     * @return the utime
     */
    public String getUtime() {
        return utime;
    }

    /**
     * @param utime the utime to set
     */
    public void setUtime(String utime) {
        this.utime = utime;
    }

    /**
     * @return the uenergy
     */
    public String getUenergy() {
        return uenergy;
    }

    /**
     * @param uenergy the uenergy to set
     */
    public void setUenergy(String uenergy) {
        this.uenergy = uenergy;
    }

    /**
     * @return the utemperature
     */
    public String getUtemperature() {
        return utemperature;
    }

    /**
     * @param utemperature the utemperature to set
     */
    public void setUtemperature(String utemperature) {
        this.utemperature = utemperature;
    }

    /**
     * @return the inertia
     */
    public int getInertia() {
        return inertia;
    }

    /**
     * @param inertia the inertia to set
     */
    public void setInertia(int inertia) {
        this.inertia = inertia;
    }

    /**
     * @return the dfactor
     */
    public float getDfactor() {
        return dfactor;
    }

    /**
     * @param dfactor the dfactor to set
     */
    public void setDfactor(float dfactor) {
        this.dfactor = dfactor;
    }

    /**
     * @return the st
     */
    public float getSt() {
        return st;
    }

    /**
     * @param st the st to set
     */
    public void setSt(float st) {
        this.st = st;
    }

    /**
     * @return the scheme
     */
    public int[] getScheme() {
        return scheme;
    }

    /**
     * @param scheme the scheme to set
     */
    public void setScheme(int[] scheme) {
        this.scheme = scheme;
    }

    /**
     * @return the restart
     */
    public int getRestart() {
        return restart;
    }

    /**
     * @param restart the restart to set
     */
    public void setRestart(int restart) {
        this.restart = restart;
    }

    /**
     * @return the wbinary
     */
    public int getWbinary() {
        return wbinary;
    }

    /**
     * @param wbinary the wbinary to set
     */
    public void setWbinary(int wbinary) {
        this.wbinary = wbinary;
    }

    /**
     * @return the steady
     */
    public int getSteady() {
        return steady;
    }

    /**
     * @param steady the steady to set
     */
    public void setSteady(int steady) {
        this.steady = steady;
    }

    /**
     * @return the adjdt
     */
    public int getAdjdt() {
        return adjdt;
    }

    /**
     * @param adjdt the adjdt to set
     */
    public void setAdjdt(int adjdt) {
        this.adjdt = adjdt;
    }

    /**
     * @return the maxCp
     */
    public float getMaxCp() {
        return maxCp;
    }

    /**
     * @param maxCp the maxCp to set
     */
    public void setMaxCp(float maxCp) {
        this.maxCp = maxCp;
    }

    /**
     * @return the mtol
     */
    public float[] getMtol() {
        return mtol;
    }

    /**
     * @param mtol the mtol to set
     */
    public void setMtol(float[] mtol) {
        this.mtol = mtol;
    }

    /**
     * @return the miter
     */
    public int[] getMiter() {
        return miter;
    }

    /**
     * @param miter the miter to set
     */
    public void setMiter(int[] miter) {
        this.miter = miter;
    }

    /**
     * @return the northocor
     */
    public int getNorthocor() {
        return northocor;
    }

    /**
     * @param northocor the northocor to set
     */
    public void setNorthocor(int northocor) {
        this.northocor = northocor;
    }

    /**
     * @return the orthof
     */
    public float getOrthof() {
        return orthof;
    }

    /**
     * @param orthof the orthof to set
     */
    public void setOrthof(float orthof) {
        this.orthof = orthof;
    }

    /**
     * @return the ftol
     */
    public float[] getFtol() {
        return ftol;
    }

    /**
     * @param ftol the ftol to set
     */
    public void setFtol(float[] ftol) {
        this.ftol = ftol;
    }

    /**
     * @return the ncicsamsteps
     */
    public int getNcicsamsteps() {
        return ncicsamsteps;
    }

    /**
     * @param ncicsamsteps the ncicsamsteps to set
     */
    public void setNcicsamsteps(int ncicsamsteps) {
        this.ncicsamsteps = ncicsamsteps;
    }

    /**
     * @return the ncicsamcor
     */
    public int getNcicsamcor() {
        return ncicsamcor;
    }

    /**
     * @param ncicsamcor the ncicsamcor to set
     */
    public void setNcicsamcor(int ncicsamcor) {
        this.ncicsamcor = ncicsamcor;
    }

    /**
     * @return the kq
     */
    public float getKq() {
        return kq;
    }

    /**
     * @param kq the kq to set
     */
    public void setKq(float kq) {
        this.kq = kq;
    }

    /**
     * @return the nsav
     */
    public int getNsav() {
        return nsav;
    }

    /**
     * @param nsav the nsav to set
     */
    public void setNsav(int nsav) {
        this.nsav = nsav;
    }

    /**
     * @return the calc
     */
    public int[] getCalc() {
        return calc;
    }

    /**
     * @param calc the calc to set
     */
    public void setCalc(int[] calc) {
        this.calc = calc;
    }

    /**
     * @return the savflux
     */
    public int getSavflux() {
        return savflux;
    }

    /**
     * @param savflux the savflux to set
     */
    public void setSavflux(int savflux) {
        this.savflux = savflux;
    }

    /**
     * @return the fsav
     */
    public int[] getFsav() {
        return fsav;
    }

    /**
     * @param fsav the fsav to set
     */
    public void setFsav(int[] fsav) {
        this.fsav = fsav;
    }

    /**
     * @return the csav
     */
    public int[] getCsav() {
        return csav;
    }

    /**
     * @param csav the csav to set
     */
    public void setCsav(int[] csav) {
        this.csav = csav;
    }

    /**
     * @return the fvec
     */
    public int getFvec() {
        return fvec;
    }

    /**
     * @param fvec the fvec to set
     */
    public void setFvec(int fvec) {
        this.fvec = fvec;
    }

    /**
     * @return the cvec
     */
    public int getCvec() {
        return cvec;
    }

    /**
     * @param cvec the cvec to set
     */
    public void setCvec(int cvec) {
        this.cvec = cvec;
    }

    /**
     * @return the smooth
     */
    public int getSmooth() {
        return smooth;
    }

    /**
     * @param smooth the smooth to set
     */
    public void setSmooth(int smooth) {
        this.smooth = smooth;
    }

    /**
     * @return the vortex
     */
    public int[] getVortex() {
        return vortex;
    }

    /**
     * @param vortex the vortex to set
     */
    public void setVortex(int[] vortex) {
        this.vortex = vortex;
    }

    /**
     * @return the streamf
     */
    public int getStreamf() {
        return streamf;
    }

    /**
     * @param streamf the streamf to set
     */
    public void setStreamf(int streamf) {
        this.streamf = streamf;
    }

    /**
     * @return the t0
     */
    public float getT0() {
        return t0;
    }

    /**
     * @param t0 the t0 to set
     */
    public void setT0(float t0) {
        this.t0 = t0;
    }

    /**
     * @return the t1
     */
    public float getT1() {
        return t1;
    }

    /**
     * @param t1 the t1 to set
     */
    public void setT1(float t1) {
        this.t1 = t1;
    }

    /**
     * @return the dt
     */
    public float getDt() {
        return dt;
    }

    /**
     * @param dt the dt to set
     */
    public void setDt(float dt) {
        this.dt = dt;
    }

    /**
     * @return the g
     */
    public float[] getG() {
        return g;
    }

    /**
     * @param g the g to set
     */
    public void setG(float[] g) {
        this.g = g;
    }

    /**
     * @return the probe
     */
    public int[] getProbe() {
        return probe;
    }

    /**
     * @param probe the probe to set
     */
    public void setProbe(int[] probe) {
        this.probe = probe;
    }

    /**
     * @return the msolver
     */
    public int[] getMsolver() {
        return msolver;
    }

    /**
     * @param msolver the msolver to set
     */
    public void setMsolver(int[] msolver) {
        this.msolver = msolver;
    }

    /**
     * @return the mprecond
     */
    public int[] getMprecond() {
        return mprecond;
    }

    /**
     * @param mprecond the mprecond to set
     */
    public void setMprecond(int[] mprecond) {
        this.mprecond = mprecond;
    }

    /**
     * @return the timemethod
     */
    public int[] getTimemethod() {
        return timemethod;
    }

    /**
     * @param timemethod the timemethod to set
     */
    public void setTimemethod(int[] timemethod) {
        this.timemethod = timemethod;
    }

    /**
     * @return the ef
     */
    public float[] getEf() {
        return ef;
    }

    /**
     * @param ef the ef to set
     */
    public void setEf(float[] ef) {
        this.ef = ef;
    }

    /**
     * @return the fill
     */
    public int getFill() {
        return fill;
    }

    /**
     * @param fill the fill to set
     */
    public void setFill(int fill) {
        this.fill = fill;
    }

    /**
     * @return the pf
     */
    public float getPf() {
        return pf;
    }

    /**
     * @param pf the pf to set
     */
    public void setPf(float pf) {
        this.pf = pf;
    }

    /**
     * @return the intbcphysreg
     */
    public int getIntbcphysreg() {
        return intbcphysreg;
    }

    /**
     * @param intbcphysreg the intbcphysreg to set
     */
    public void setIntbcphysreg(int intbcphysreg) {
        this.intbcphysreg = intbcphysreg;
    }
    
    
}
