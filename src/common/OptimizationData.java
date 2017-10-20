/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import javax.mail.Folder;

/**
 *
 * @author Administrator
 */
public class OptimizationData {

    private Folder modelFolder = null;
    private Folder meshFolder = null;
    private Folder resultFolder = null;
    private Folder solveFolder = null;
    private String modelVar[];//模型参数变量名
    private double modelVal[];//对应的模型参数变量的值
    private String modelUnit[];//对应的模型参数变量对应值的单位
    
}
