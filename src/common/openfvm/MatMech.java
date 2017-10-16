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
public class MatMech {
    private String elastmod;
    private double poisson;

    /**
     * @return the elastmod
     */
    public String getElastmod() {
        return elastmod;
    }

    /**
     * @param elastmod the elastmod to set
     */
    public void setElastmod(String elastmod) {
        this.elastmod = elastmod;
    }

    /**
     * @return the poisson
     */
    public double getPoisson() {
        return poisson;
    }

    /**
     * @param poisson the poisson to set
     */
    public void setPoisson(double poisson) {
        this.poisson = poisson;
    }
}
