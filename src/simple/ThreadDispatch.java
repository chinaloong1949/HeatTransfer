/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import common.CalculateInfoBag;

/**
 *
 * @author Administrator
 */
public class ThreadDispatch extends Thread {

    String tab = null;
    CalculateInfoBag info;

    public ThreadDispatch(String name, String tab, CalculateInfoBag info) {
        super(name);
        this.tab = tab;
        this.info = info;
    }

    public void run() {
        if (tab.equals("FluidFlow")) {
//            new Example3_1_1(info);
            new FluidFlow(info);
        }
    }
}
