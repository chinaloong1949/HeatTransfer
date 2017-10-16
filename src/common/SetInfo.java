/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import element.Part;
import java.io.File;

/**
 *
 * @author yk
 */
public class SetInfo {

    private File k_file = null;
    private int analysisType = 0;//0定常；1非定常
    private double analysisType_totalTime = 0.1;
    private int analysisType_timeSteps = 100;
    private double analysisType_initialTime = 0;
    private File materialInitFile = null;
    private String material = "water";
    private double referPressure = 101325;
    private Boundary[] boundary = null;
    private int maxIterNum = 1;
    private double maxErr = 0.01;
    private Part[] parts = null;

    public SetInfo(File k_file, int analysisType, double d1, int d2,
            double d3, double rp, Boundary[] boundary) {
        this.k_file = k_file;
        this.analysisType = analysisType;
        this.analysisType_initialTime = d1;
        this.analysisType_timeSteps = d2;
        this.analysisType_totalTime = d3;
        this.referPressure = rp;
        this.boundary = boundary;
    }

    public SetInfo() {

    }

    /**
     * @return the k_file
     */
    public File getK_file() {
        return k_file;
    }

    /**
     * @param k_file the k_file to set
     */
    public void setK_file(File k_file) {
        this.k_file = k_file;
    }

    /**
     * @return the analysisType
     */
    public int getAnalysisType() {
        return analysisType;
    }

    /**
     * @param analysisType the analysisType to set
     */
    public void setAnalysisType(int analysisType) {
        this.analysisType = analysisType;
    }

    /**
     * @return the analysisType_totalTime
     */
    public double getAnalysisType_totalTime() {
        return analysisType_totalTime;
    }

    /**
     * @param analysisType_totalTime the analysisType_totalTime to set
     */
    public void setAnalysisType_totalTime(double analysisType_totalTime) {
        this.analysisType_totalTime = analysisType_totalTime;
    }

    /**
     * @return the analysisType_timeSteps
     */
    public int getAnalysisType_timeSteps() {
        return analysisType_timeSteps;
    }

    /**
     * @param analysisType_timeSteps the analysisType_timeSteps to set
     */
    public void setAnalysisType_timeSteps(int analysisType_timeSteps) {
        this.analysisType_timeSteps = analysisType_timeSteps;
    }

    /**
     * @return the analysisType_initialTime
     */
    public double getAnalysisType_initialTime() {
        return analysisType_initialTime;
    }

    /**
     * @param analysisType_initialTime the analysisType_initialTime to set
     */
    public void setAnalysisType_initialTime(double analysisType_initialTime) {
        this.analysisType_initialTime = analysisType_initialTime;
    }

    /**
     * @return the material
     */
    public String getMaterial() {
        return material;
    }

    /**
     * @param material the material to set
     */
    public void setMaterial(String material) {
        this.material = material;
    }

    /**
     * @return the referPressure
     */
    public double getReferPressure() {
        return referPressure;
    }

    /**
     * @param referPressure the referPressure to set
     */
    public void setReferPressure(double referPressure) {
        this.referPressure = referPressure;
    }

    /**
     * @return the boundary
     */
    public Boundary[] getBoundary() {
        return boundary;
    }

    /**
     * @param boundary the boundary to set
     */
    public void setBoundary(Boundary[] boundary) {
        this.boundary = boundary;
    }

    /**
     * @return the materialInitFile
     */
    public File getMaterialInitFile() {
        return materialInitFile;
    }

    /**
     * @param materialInitFile the materialInitFile to set
     */
    public void setMaterialInitFile(File materialInitFile) {
        this.materialInitFile = materialInitFile;
    }

    /**
     * @return the maxIterNum
     */
    public int getMaxIterNum() {
        return maxIterNum;
    }

    /**
     * @param maxIterNum the maxIterNum to set
     */
    public void setMaxIterNum(int maxIterNum) {
        this.maxIterNum = maxIterNum;
    }

    /**
     * @return the maxErr
     */
    public double getMaxErr() {
        return maxErr;
    }

    /**
     * @param maxErr the maxErr to set
     */
    public void setMaxErr(double maxErr) {
        this.maxErr = maxErr;
    }

    /**
     * @return the parts
     */
    public Part[] getParts() {
        return parts;
    }

    /**
     * @param parts the parts to set
     */
    public void setParts(Part[] parts) {
        this.parts = parts;
    }
}
