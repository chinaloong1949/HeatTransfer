/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import heattransfer.MainFrame;

/**
 *
 * @author yk
 */
public class TimePanel implements Runnable {

    MainFrame mainframe = null;

    public TimePanel(MainFrame mainframe) {
        this.mainframe = mainframe;
    }

    public void run() {
        while (true) {
            mainframe.replaceTimePanel();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
