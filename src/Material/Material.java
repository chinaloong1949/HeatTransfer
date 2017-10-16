/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MATERIAL;

/**
 *
 * @author yk
 */
public class Material {

    private int materilId = -1;
    private String name = "default name";
    private String source = "default source";
    private boolean isSelected = false;
    private String describe = "default describe";
    private double density = 0;
    private double miu = 0.0;//Pa*s
    private double boSongBi = 0.0;
    private double yangShiMoLiang = 0.0;
    private double tiJiMoLiang = 0.0;
    private double jianQieMoLiang = 0.0;
    private double quFuQiangDu = 0.0;
    private double daorexishu = 0.0;

    public Material(int id, String name, double density) {
        this.materilId = id;
        this.name = name;
        this.density = density;
    }

    public Material(int id, String name) {
        this.materilId = id;
        this.name = name;
    }

    public Material() {
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public boolean isSelected() {
        return isIsSelected();
    }

    public String getDescribe() {
        return describe;
    }

    /**
     * @return the miu
     */
    public double getMiu() {
        return miu;
    }

    /**
     * @param miu the miu to set
     */
    public void setMiu(double miu) {
        this.miu = miu;
    }

    /**
     * @return the density
     */
    public double getDensity() {
        return density;
    }

    /**
     * @param density the density to set
     */
    public void setDensity(double density) {
        this.density = density;
    }

    /**
     * @return the boSongBi
     */
    public double getBoSongBi() {
        return boSongBi;
    }

    /**
     * @param boSongBi the boSongBi to set
     */
    public void setBoSongBi(double boSongBi) {
        this.boSongBi = boSongBi;
    }

    /**
     * @return the yangShiMoLiang
     */
    public double getYangShiMoLiang() {
        return yangShiMoLiang;
    }

    /**
     * @param yangShiMoLiang the yangShiMoLiang to set
     */
    public void setYangShiMoLiang(double yangShiMoLiang) {
        this.yangShiMoLiang = yangShiMoLiang;
    }

    /**
     * @return the tiJiMoLiang
     */
    public double getTiJiMoLiang() {
        return tiJiMoLiang;
    }

    /**
     * @param tiJiMoLiang the tiJiMoLiang to set
     */
    public void setTiJiMoLiang(double tiJiMoLiang) {
        this.tiJiMoLiang = tiJiMoLiang;
    }

    /**
     * @return the jianQieMoLiang
     */
    public double getJianQieMoLiang() {
        return jianQieMoLiang;
    }

    /**
     * @param jianQieMoLiang the jianQieMoLiang to set
     */
    public void setJianQieMoLiang(double jianQieMoLiang) {
        this.jianQieMoLiang = jianQieMoLiang;
    }

    /**
     * @return the quFuQiangDu
     */
    public double getQuFuQiangDu() {
        return quFuQiangDu;
    }

    /**
     * @param quFuQiangDu the quFuQiangDu to set
     */
    public void setQuFuQiangDu(double quFuQiangDu) {
        this.quFuQiangDu = quFuQiangDu;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the materilId
     */
    public int getMaterilId() {
        return materilId;
    }

    /**
     * @param materilId the materilId to set
     */
    public void setMaterilId(int materilId) {
        this.materilId = materilId;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the isSelected
     */
    public boolean isIsSelected() {
        return isSelected;
    }

    /**
     * @param isSelected the isSelected to set
     */
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * @param describe the describe to set
     */
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    /**
     * @return the daorexishu
     */
    public double getDaorexishu() {
        return daorexishu;
    }

    /**
     * @param daorexishu the daorexishu to set
     */
    public void setDaorexishu(double daorexishu) {
        this.daorexishu = daorexishu;
    }

}
