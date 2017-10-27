/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class OptimizationData {

    /**
     * @return the ansysPath
     */
    public String getAnsysPath() {
        return ansysPath;
    }

    /**
     * @param ansysPath the ansysPath to set
     */
    public void setAnsysPath(String ansysPath) {
        this.ansysPath = ansysPath;
    }

    /**
     * @return the fluentPath
     */
    public String getFluentPath() {
        return fluentPath;
    }

    /**
     * @param fluentPath the fluentPath to set
     */
    public void setFluentPath(String fluentPath) {
        this.fluentPath = fluentPath;
    }

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

    private File workingDirectory = new File("D:\\Users\\2017\\dissertation\\chapter3\\AoCao");

    private File modelFolder = null;
    private File meshFolder = null;
    private File resultFile = null;
    private File solveFolder = null;
    private ArrayList<File> meshFiles;
    private String modelVar[] = null;//模型参数变量名
    private String modelDiscription[] = null;//对应的模型参数变量的值
    private String modelUnit[] = null;//对应的模型参数变量对应值的单位
    private File createModelFile = null;
    private File createMeshFile = null;
    private File createSolveFile = null;
    private File dataProcessFile = null;
    private OptimizationType optimizationType = OptimizationType.Diff_Model;
    private FluidType fluidType = null;
    private File materialDataBaseFile = null;//当使用用户自定义材料库时，这个是库文件
    private File materialFile = new File("materials.csv");//材料文件，里面定义了计算使用的材料有哪些
    private File boundaryConditionFile = new File("boundaryConditions.csv");
    private boolean batchSolve = false;

    private String ansysPath;
    private String fluentPath;

    public enum FluidType {
        FluentDataBase, UserDefinedDataBase, SpecificProperties
    }

    public OptimizationData() {

    }

    public boolean checkModelData() {
        if (workingDirectory == null) {
            JOptionPane.showMessageDialog(null, "未设置工作目录");
            return false;
        }

        if (modelFolder == null) {
            JOptionPane.showMessageDialog(null, "模型保存文件夹不能为空");
            return false;
        }

        if ((modelVar.length == modelUnit.length)) {

        } else {
            JOptionPane.showMessageDialog(null, "模型参数变量名个数和变量名单位个数不相同");
            return false;
        }

        if (getCreateModelFile() == null) {
            JOptionPane.showMessageDialog(null, "没有建模文件");
            return false;
        }
        return true;
    }

    public boolean checkMeshData() {
        if (workingDirectory == null) {
            JOptionPane.showMessageDialog(null, "未设置工作目录");
            return false;
        }
        JOptionPane.showMessageDialog(null, "目前不支持变网格");
        return false;
    }

    public boolean checkSolveData() {

        if (workingDirectory == null) {
            JOptionPane.showMessageDialog(null, "未设置工作目录");
            return false;
        }

        if (this.meshFolder == null) {
            JOptionPane.showMessageDialog(null, "未设置网格文件夹！");
            return false;
        } else {
            meshFiles = new ArrayList<>();//meshFiles根据meshFolder初始化，首先清空
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
        System.out.println("mesh folder = " + meshFolder);
        System.out.println("total " + meshFiles.size() + " different mesh files");
        for (int i = 0; i < meshFiles.size(); i++) {
            System.out.println("\t" + meshFiles.get(i).getName());
        }
        if (batchSolve) {
            System.out.println("计算模式：" + "batch solve");
        } else {
            System.out.println("计算模式：" + "Optimization");
        }
        if (null != this.optimizationType) {
            switch (this.optimizationType) {
                case Diff_Solve:
                    System.out.println("计算考虑多个固定网格文件，变化材料和变化边界条件！");
                    break;
                case Diff_Mesh:
                    System.out.println("计算考虑多个固定模型，变化网格参数，变化材料和变化边界条件");
                    break;
                case Diff_Model:
                    System.out.println("计算考虑变化模型参数，变化网格文件，变化材料和变化边界条件");
                    break;
                default:
                    break;
            }
        }
        if (this.fluidType == FluidType.FluentDataBase) {
            System.out.println("use Fluent material data base");
        } else if (this.fluidType == FluidType.UserDefinedDataBase) {
            System.out.println("use User-Defined data base");
            System.out.println("\tUser-Defined data base name：" + this.materialDataBaseFile.getName());
        } else if (this.fluidType == FluidType.SpecificProperties) {
            System.out.println("use materials with specific properties");
        }

        if (this.createSolveFile == null) {
            JOptionPane.showMessageDialog(null, "计算设置文件不存在！");
            return false;
        } else {
            System.out.println("计算设置文件为：" + this.createSolveFile.getAbsolutePath());
        }

        return true;
    }

    public boolean checkData(int tab) {
        if (tab == 1) {
            if (checkModelData()) {
                if (checkMeshData()) {
                    if (checkSolveData()) {
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (tab == 2) {
            if (checkMeshData()) {
                if (checkSolveData()) {
                } else {
                    return false;
                }
            } else {
                return false;
            }

        }
        if (tab == 3) {
            if (checkSolveData()) {

            } else {
                return false;
            }
        }
        //调用Fluent运行
        Map<String, String> map = System.getenv();
        ansysPath = map.get("AWP_ROOT150");//Ansys的路径D:\Program Files\ANSYS Inc\v150
        fluentPath = ansysPath + "\\fluent\\ntbin\\win64\\fluent.exe";
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
     * @return the resultFile
     */
    public File getResultFile() {
        return resultFile;
    }

    /**
     * @param resultFile the resultFile to set
     */
    public void setResultFile(File resultFile) {
        this.resultFile = resultFile;
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

    /**
     * @return the workingDirectory
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * @param workingDirectory the workingDirectory to set
     */
    public void setWorkingDirectory(File workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    /**
     * @return the materialFile
     */
    public File getMaterialFile() {
        return materialFile;
    }

    /**
     * @param materialFile the materialFile to set
     */
    public void setMaterialFile(File materialFile) {
        this.materialFile = materialFile;
    }

    /**
     * @return the boundaryConditionFile
     */
    public File getBoundaryConditionFile() {
        return boundaryConditionFile;
    }

    /**
     * @param boundaryConditionFile the boundaryConditionFile to set
     */
    public void setBoundaryConditionFile(File boundaryConditionFile) {
        this.boundaryConditionFile = boundaryConditionFile;
    }

}
