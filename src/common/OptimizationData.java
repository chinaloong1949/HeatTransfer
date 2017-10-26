/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class OptimizationData {

    /**
     * @return the fluidType
     */
    public FluidType getFluidType() {
        return fluidType;
    }

    /**
     * @param fluidType the fluidType to set
     */
    public void setFluidType(String fluidType) {
        switch (fluidType) {
            case "userDefinedDataBase":
                this.fluidType = FluidType.UserDefinedDataBase;
                break;
            case "fluentDataBase":
                this.fluidType = FluidType.FluentDataBase;
                break;
            case "specificProperties": {
                this.fluidType = FluidType.SpecificProperties;
            }
        }
    }

    public void setFluidType(FluidType fluidType) {
        this.fluidType = fluidType;
    }

    /**
     * @return the batchSolve
     */
    public boolean isBatchSolve() {
        return batchSolve;
    }

    /**
     * @param batchSolve the batchSolve to set
     */
    public void setBatchSolve(boolean batchSolve) {
        this.batchSolve = batchSolve;
    }

    public enum OptimizationType {
        Diff_Model, Diff_Mesh, Diff_Solve
    }

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
    private ArrayList<File> meshFiles = null;
    private String modelVar[] = null;//模型参数变量名
    private String modelDiscription[] = null;//对应的模型参数变量的值
    private String modelUnit[] = null;//对应的模型参数变量对应值的单位
    private File createModelFile = null;
    private File createMeshFile = null;
    private File createSolveFile = null;
    private File dataProcessFile = null;
    private OptimizationType optimizationType = null;
    private FluidType fluidType;
    private File materialDataBaseFile=null;//当使用用户自定义材料库时，这个是库文件
    private boolean batchSolve = false;

    public enum FluidType {
        FluentDataBase, UserDefinedDataBase, SpecificProperties
    }

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
        if (this.meshFolder == null) {
            JOptionPane.showMessageDialog(null, "未设置网格文件夹！");
            return false;
        } else {
            File[] fileList = meshFolder.listFiles();
            for (File fileList1 : fileList) {
                String name = fileList1.getName();
                if (name.trim().toLowerCase().endsWith(".msh")) {
                    meshFiles.add(fileList1);
                }
            }
            if (meshFiles.isEmpty()) {
                JOptionPane.showMessageDialog(null, "网格文件夹下没有找到后缀名为msh的网格文件！");
                return false;
            }
        }
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
     * @param modelDiscription 模型参数的备注，用于标记每个建模参数的意义，可以不设置
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

    /**
     * @return the optimizationType
     */
    public OptimizationType getOptimizationType() {
        return optimizationType;
    }

    /**
     * @param optimizationType the optimizationType to set
     */
    public void setOptimizationType(OptimizationType optimizationType) {
        this.optimizationType = optimizationType;
    }

    public void setOptimizationType(String str) {
        if (str.equals("Diff_Model")) {
            this.optimizationType = OptimizationType.Diff_Model;
        } else if (str.equals("Diff_Mesh")) {
            this.optimizationType = OptimizationType.Diff_Mesh;
        }
        if (str.equals("Diff_Solve")) {
            this.optimizationType = OptimizationType.Diff_Solve;
        }
    }

    /**
     * @return the meshFiles
     */
    public ArrayList<File> getMeshFiles() {
        return meshFiles;
    }

    /**
     * @param meshFiles the meshFiles to set
     */
    public void setMeshFiles(ArrayList<File> meshFiles) {
        this.meshFiles = meshFiles;
    }

    /**
     * @return the materialDataBaseFile
     */
    public File getMaterialDataBaseFile() {
        return materialDataBaseFile;
    }

    /**
     * @param materialDataBaseFile the materialDataBaseFile to set
     */
    public void setMaterialDataBaseFile(File materialDataBaseFile) {
        this.materialDataBaseFile = materialDataBaseFile;
    }

}
