/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import javax.mail.Folder;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class OptimizationData {

    /**
     * @return the createMeshFile
     */
    public File getCreateMeshFile() {
        return createMeshFile;
    }

    /**
     * @param createMeshFile the createMeshFile to set
     */
    public void setCreateMeshFile(File createMeshFile) {
        this.createMeshFile = createMeshFile;
    }

    /**
     * @return the createSolveFile
     */
    public File getCreateSolveFile() {
        return createSolveFile;
    }

    /**
     * @param createSolveFile the createSolveFile to set
     */
    public void setCreateSolveFile(File createSolveFile) {
        this.createSolveFile = createSolveFile;
    }

    /**
     * @return the dataProcessFile
     */
    public File getDataProcessFile() {
        return dataProcessFile;
    }

    /**
     * @param dataProcessFile the dataProcessFile to set
     */
    public void setDataProcessFile(File dataProcessFile) {
        this.dataProcessFile = dataProcessFile;
    }

    private File modelFolder = null;
    private File meshFolder = null;
    private File resultFolder = null;
    private File solveFolder = null;
    private String modelVar[] = null;//模型参数变量名
    private String modelDiscription[] = null;//对应的模型参数变量的值
    private String modelUnit[] = null;//对应的模型参数变量对应值的单位
    private File createModelFile = null;
    private File createMeshFile = null;
    private File createSolveFile = null;
    private File dataProcessFile = null;

    public OptimizationData() {

    }

    public boolean checkModelData() {
        boolean flag = true;
        if (modelFolder == null) {
            flag = false;
            JOptionPane.showMessageDialog(null, "模型保存文件夹不能为空");
        }
        if ((modelVar == null) || (modelUnit == null)) {
            JOptionPane.showMessageDialog(null, "模型参数数据不全");
            flag = false;
        }
        if ((modelVar.length == modelUnit.length)) {

        } else {
            JOptionPane.showMessageDialog(null, "模型参数变量名个数和变量名单位个数不相同");
            flag = false;
        }

        if (getCreateModelFile() == null) {
            JOptionPane.showMessageDialog(null, "没有建模文件");
        }
        return flag;
    }

    public boolean checkSolveData() {

        return true;
    }

    public boolean checkData(int tab) {
        if (tab == 1) {
            checkModelData();
            checkMeshData();
            checkSolveData();
        } else if (tab == 3) {
            checkSolveData();
        }
        return true;
    }

    public boolean saveDataToFile(File file) {
        String xml = XStreamUtil.OptimizationDataToXML(this);
        FileOperate fo = new FileOperate(file);
        fo.writeToFileString(xml, false);
        return true;
    }

    public static OptimizationData readDataFromFile(File file) {
        String xml = new FileOperate(file).readFromFileStringWhole(file);
        OptimizationData data = XStreamUtil.OptimizationDataFromXML(xml);
        return data;
    }

    /**
     * @return the modelFolder
     */
    public File getModelFolder() {
        return modelFolder;
    }

    /**
     * @param modelFolder the modelFolder to set
     */
    public void setModelFolder(File modelFolder) {
        this.modelFolder = modelFolder;
    }

    /**
     * @return the meshFolder
     */
    public File getMeshFolder() {
        return meshFolder;
    }

    /**
     * @param meshFolder the meshFolder to set
     */
    public void setMeshFolder(File meshFolder) {
        this.meshFolder = meshFolder;
    }

    /**
     * @return the resultFolder
     */
    public File getResultFolder() {
        return resultFolder;
    }

    /**
     * @param resultFolder the resultFolder to set
     */
    public void setResultFolder(File resultFolder) {
        this.resultFolder = resultFolder;
    }

    /**
     * @return the solveFolder
     */
    public File getSolveFolder() {
        return solveFolder;
    }

    /**
     * @param solveFolder the solveFolder to set
     */
    public void setSolveFolder(File solveFolder) {
        this.solveFolder = solveFolder;
    }

    /**
     * @return the modelVar
     */
    public String[] getModelVar() {
        return modelVar;
    }

    /**
     * @param modelVar the modelVar to set
     */
    public void setModelVar(String[] modelVar) {
        this.modelVar = modelVar;
    }

    /**
     * @return the modelDiscription
     */
    public String[] getModelDiscription() {
        return modelDiscription;
    }

    /**
     * @param modelDiscription the modelVal to set
     */
    public void setModelDiscription(String[] modelDiscription) {
        this.modelDiscription = modelDiscription;
    }

    /**
     * @return the modelUnit
     */
    public String[] getModelUnit() {
        return modelUnit;
    }

    /**
     * @param modelUnit the modelUnit to set
     */
    public void setModelUnit(String[] modelUnit) {
        this.modelUnit = modelUnit;
    }

    /**
     * @return the createModelFile
     */
    public File getCreateModelFile() {
        return createModelFile;
    }

    /**
     * @param createModelFile the createModelFile to set
     */
    public void setCreateModelFile(File createModelFile) {
        this.createModelFile = createModelFile;
    }

}
