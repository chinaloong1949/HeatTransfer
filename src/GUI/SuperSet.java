/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import heattransfer.MainFrame;

/**
 *
 * @author Administrator
 */
public class SuperSet {

    public SuperSet(String tab, MainFrame mainFrame) {
        switch (tab) {
            case "splitChar":
                System.out.println("设置文件分割符；");
                new SplitCharSet(mainFrame);
                break;
            case "gaussPointNum":
                System.out.println("设置高斯勒德让积分点数目；");
                new SuperSetFrame(mainFrame);
                break;
            case "command":
                System.out.println("设置高斯勒德让积分点数目；");
                new SuperSetFrame(mainFrame);
                break;
            default:
                System.out.println("命令字符串无效！");
        }
    }
}
