/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import javax.swing.JOptionPane;

/**
 *
 * @author yk
 */
public class OneDimension {

    int pointNum;
    double[] temperature;

    public OneDimension() {
        pointNum = Integer.parseInt(JOptionPane.showInputDialog(null, "请输入节点数量"));
        temperature = new double[pointNum];
    }

    public void setBoundaryCondition(int tab) {
        switch (tab) {
            case 1:
                //第一类边界条件

                break;
            case 2:
                //第二类边界条件

                break;
            case 3:
                //第三类边界条件

                break;
        }

    }

}
