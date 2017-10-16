/*
 * 该类实现主界面到计算类的参数传递
 */
package common;

import MATERIAL.Material;
import element.BoundaryPEle;
import element.BoundaryTEle;
import element.BoundaryVNode;
import element.EleQuad_20;
import element.EleQuad_8;
import element.Part;
import java.io.File;
import java.io.FileOutputStream;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import joggle.ReadFileofOpenFVM;
import point.Point3D;

/**
 *
 * @author yk
 */
public class CalculateInfoBag {

    private EleQuad_8[] eleQuad_8;
    private EleQuad_20[] eleQuad_20;
    private Part[] parts;
    private int elementNum = 0;
    private Point3D[] nodeP;
    private Point3D[] nodeV;
    private int nodePNum = 0;
    private int nodeVNum = 0;
    private int[][] nodeIdSet = null;
    private int analysisType = 0;
    private double analysisType_totalTime = 0.1;
    private int analysisType_timeSteps = 10;
    private double analysisType_initialTime = 0;
    private int maxIterNum = 1;
    private double maxErr = 0.01;//计算停止需要达到的残差小于的值
    private ArrayList<Material> materialList = new ArrayList<>();
    private String material = "water";
    private double referPressure = 101325;
    private ArrayList<Boundary> boundary = new ArrayList<>();//所有边界的集合
    private int gaussPointNum = 2;
    private String command = "对角线归一代入法";
    private BoundaryPEle[] boundPressEle;//压力边界
    private BoundaryVNode[] boundVNode;//速度边界
    private BoundaryVNode[] boundVNode88;//速度单元和压力单元都是8节点六面体单元情况时候的速度边界
    private BoundaryTEle[] boundTempEle;
    private double scale = 1;
    private double[] tempInitial;

    /**
     * @return the eleQuad_8
     */
    public CalculateInfoBag() {

    }

    public CalculateInfoBag(String str) {
        if (str.equals("OpenFVM")) {
            boolean flag = false;
            File file = null;
            do {
                try {
                    file = new FileChooser("选择文件夹", 3).getFile();
                    file.getAbsolutePath();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "您所选择的文件夹"
                            + "不存在或无法打开！");
                    ex.printStackTrace();
                    flag = true;
                }
            } while (flag);
            ReadFileofOpenFVM openFVM = new ReadFileofOpenFVM(file);
        }
    }

    public void writeDataToFile(File file) {
        if (file.isDirectory()) {
            try {
                File bp1 = new File(file, "BP1.txt");
                File bp2 = new File(file, "BP2.txt");
                File bp3 = new File(file, "BP3.txt");
                File jxyv = new File(file, "JXYV.txt");
                File jxyp = new File(file, "JXYP.txt");
                File jmv = new File(file, "JMV.txt");
                File jmp = new File(file, "JMP.txt");
                File jbv = new File(file, "JBV.txt");
                File jbp = new File(file, "JBP.txt");

                FileOutputStream fos1 = new FileOutputStream(bp1);
                for (int i = 0; i <= nodeIdSet[0].length - 1; i++) {
                    fos1.flush();
                    String temp = String.format("%10d", nodeIdSet[0][i]);
                    fos1.write((temp).getBytes("UTF-8"));
                }

                FileOutputStream fos2 = new FileOutputStream(bp2);
                for (int i = 0; i <= nodeIdSet[1].length - 1; i++) {
                    fos2.flush();
                    String temp = String.format("%10d", nodeIdSet[1][i]);
                    fos2.write((temp).getBytes("UTF-8"));
                }

                FileOutputStream fos3 = new FileOutputStream(bp3);
                for (int i = 0; i <= nodeIdSet[2].length - 1; i++) {
                    fos3.flush();
                    String temp = String.format("%10d", nodeIdSet[2][i]);
                    fos3.write((temp).getBytes("UTF-8"));
                }

                fos1.close();
                fos2.close();
                fos3.close();

                FileOutputStream fos4 = new FileOutputStream(jxyv);
                for (int i = 0; i <= nodeV.length - 1; i++) {
                    fos4.flush();
                    String temp = String.format("%10f%10f%10f%n",
                            nodeV[i].getX(), nodeV[i].getY(),
                            nodeV[i].getZ());
                    fos4.write((temp).getBytes("UTF-8"));
                }
                fos4.close();

                FileOutputStream fos5 = new FileOutputStream(jxyp);
                for (int i = 0; i <= nodeP.length - 1; i++) {
                    fos5.flush();
                    String temp = String.format("%10f%10f%10f%n",
                            nodeP[i].getX(), nodeP[i].getY(),
                            nodeP[i].getZ());
                    fos5.write((temp).getBytes("UTF-8"));
                }
                fos5.close();

                FileOutputStream fos6 = new FileOutputStream(jmp);
                for (int i = 0; i <= eleQuad_8.length - 1; i++) {
                    fos6.flush();
                    String temp = String.format("%10d%10d%10d%10d%10d%10d%10d%10d%n",
                            eleQuad_8[i].getNodeList()[0],
                            eleQuad_8[i].getNodeList()[1],
                            eleQuad_8[i].getNodeList()[2],
                            eleQuad_8[i].getNodeList()[3],
                            eleQuad_8[i].getNodeList()[4],
                            eleQuad_8[i].getNodeList()[5],
                            eleQuad_8[i].getNodeList()[6],
                            eleQuad_8[i].getNodeList()[7]);
                    fos6.write((temp).getBytes("UTF-8"));
                }
                fos6.close();

                FileOutputStream fos7 = new FileOutputStream(jmv);
                for (int i = 0; i <= eleQuad_20.length - 1; i++) {
                    fos7.flush();
                    String temp = String.format("%10d%10d%10d%10d%10d"
                            + "%10d%10d%10d%10d%10d%10d%10d%10d%10d%10d"
                            + "%10d%10d%10d%10d%10d%n",
                            eleQuad_20[i].getNodeList()[0],
                            eleQuad_20[i].getNodeList()[1],
                            eleQuad_20[i].getNodeList()[2],
                            eleQuad_20[i].getNodeList()[3],
                            eleQuad_20[i].getNodeList()[4],
                            eleQuad_20[i].getNodeList()[5],
                            eleQuad_20[i].getNodeList()[6],
                            eleQuad_20[i].getNodeList()[7],
                            eleQuad_20[i].getNodeList()[8],
                            eleQuad_20[i].getNodeList()[9],
                            eleQuad_20[i].getNodeList()[10],
                            eleQuad_20[i].getNodeList()[11],
                            eleQuad_20[i].getNodeList()[12],
                            eleQuad_20[i].getNodeList()[13],
                            eleQuad_20[i].getNodeList()[14],
                            eleQuad_20[i].getNodeList()[15],
                            eleQuad_20[i].getNodeList()[16],
                            eleQuad_20[i].getNodeList()[17],
                            eleQuad_20[i].getNodeList()[18],
                            eleQuad_20[i].getNodeList()[19]);

                    fos7.write((temp).getBytes("UTF-8"));
                }
                fos7.close();

                //输出速度边界参数
                FileOutputStream fos8 = new FileOutputStream(jbv);
                for (int i = 0; i <= getVeloBoundary().length - 1; i++) {
                    fos8.flush();
                    String temp = String.format("%10d%10f%10f%10f%n",
                            getVeloBoundary()[i].getNodeId(), getVeloBoundary()[i].getU(),
                            getVeloBoundary()[i].getV(), getVeloBoundary()[i].getW());
                    fos8.write((temp).getBytes("UTF-8"));
                }
                fos8.close();

                //输出压力边界条件
                FileOutputStream fos9 = new FileOutputStream(jbp);
                for (int i = 0; i <= boundPressEle.length - 1; i++) {
                    fos9.flush();
                    String temp = String.format("%10d%7d%10f%10f%10f"
                            + "%10f%10f%10f%10f%n",
                            boundPressEle[i].getElementId(),
                            boundPressEle[i].getBoundarySurface(),
                            boundPressEle[i].getCos_theta_x(),
                            boundPressEle[i].getCos_theta_y(),
                            boundPressEle[i].getCos_theta_z(),
                            boundPressEle[i].getP1(),
                            boundPressEle[i].getP2(),
                            boundPressEle[i].getP3(),
                            boundPressEle[i].getP4());
                    fos9.write(temp.getBytes("UTF-8"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {

            }
        } else {
            JOptionPane.showMessageDialog(null, "选择文件不是文件夹！");
        }
    }

    public void initCalculateInfoBag() {
        eleQuad_20 = new EleQuad_20[eleQuad_8.length];

        ArrayList<Point3D> nodeVList = new ArrayList<>();
        for (int i = 0; i <= nodeP.length - 1; i++) {
            nodeVList.add(nodeP[i]);
        }
        for (int i = 0; i <= eleQuad_8.length - 1; i++) {
            //针对每一个8节点单元都插入12个节点，使其成为一个三维二次二十节点单元
            if (i % 1000 == 0) {
                System.out.println("处理进度：" + (i * 100 / (eleQuad_8.length - 1)) + "%,  " + i + "/" + eleQuad_8.length);
            }
            int[] nodeIdList = eleQuad_8[i].getNodeList();
            int[] nodeId_20 = new int[20];
            for (int j = 0; j <= 7; j++) {
                nodeId_20[j] = nodeIdList[j];
                //20节点单元的前八个节点编号与8节点单元相同
            }
            Point3D p1 = nodeP[nodeIdList[0] - 1];
            Point3D p2 = nodeP[nodeIdList[1] - 1];
            Point3D p3 = nodeP[nodeIdList[2] - 1];
            Point3D p4 = nodeP[nodeIdList[3] - 1];
            Point3D p5 = nodeP[nodeIdList[4] - 1];
            Point3D p6 = nodeP[nodeIdList[5] - 1];
            Point3D p7 = nodeP[nodeIdList[6] - 1];
            Point3D p8 = nodeP[nodeIdList[7] - 1];

            //检查p1和p2的中点是不是已经在nodeVList中
//            Point3D point8 = Point3D.getMiddlePoint3D(p1, p2);
//            int point8Id = -1;
//            for (int j = nodeP.length; j <= nodeVList.size() - 1; j++) {
//                if (point8.coincide(nodeVList.get(j))) {//coincide表示点重合，equal表示点相同
//                    point8Id = j + 1;//新添加的点编号为j+1,索引号为j
//                    break;
//                }
//            }
            Point3D point8 = p1.getMidPoints(p2);

            if (point8 == null) {
                point8 = Point3D.getMiddlePoint3D(p1, p2);
                int point8Id = nodeVList.size() + 1;
                point8.setPointId(point8Id);
                nodeVList.add(point8);
                point8.setEndPoints(p1, p2);
                p1.addMidPoints(point8, p2);
                p2.addMidPoints(point8, p1);
            }
            nodeId_20[8] = point8.getPointId();//将该点的ID添加到20节点单元节点列表第9个节点位置上

            Point3D point9 = p2.getMidPoints(p3);
            if (point9 == null) {
                point9 = Point3D.getMiddlePoint3D(p2, p3);
                int point9Id = nodeVList.size() + 1;
                point9.setPointId(point9Id);
                nodeVList.add(point9);
                point9.setEndPoints(p2, p3);
                p2.addMidPoints(point9, p3);
                p3.addMidPoints(point9, p2);
            }
            nodeId_20[9] = point9.getPointId();//将该点的ID添加到20节点单元节点列表第10个节点位置上

            Point3D point10 = p3.getMidPoints(p4);//需重构修改
            if (point10 == null) {
                point10 = Point3D.getMiddlePoint3D(p3, p4);
                int point10Id = nodeVList.size() + 1;
                point10.setPointId(point10Id);
                nodeVList.add(point10);
                point10.setEndPoints(p3, p4);
                p3.addMidPoints(point10, p4);
                p4.addMidPoints(point10, p3);
            }
            nodeId_20[10] = point10.getPointId();//需修改

            Point3D point11 = p4.getMidPoints(p1);
            if (point11 == null) {
                point11 = Point3D.getMiddlePoint3D(p4, p1);
                int point11Id = nodeVList.size() + 1;
                point11.setPointId(point11Id);
                nodeVList.add(point11);
                point11.setEndPoints(p4, p1);
                p4.addMidPoints(point11, p1);
                p1.addMidPoints(point11, p4);
            }
            nodeId_20[11] = point11.getPointId();//需修改

            Point3D point12 = p1.getMidPoints(p5);//需重构修改
            if (point12 == null) {
                point12 = Point3D.getMiddlePoint3D(p1, p5);
                int point12Id = nodeVList.size() + 1;
                point12.setPointId(point12Id);
                nodeVList.add(point12);
                point12.setEndPoints(p1, p5);
                p1.addMidPoints(point12, p5);
                p5.addMidPoints(point12, p1);
            }
            nodeId_20[12] = point12.getPointId();//需修改

            Point3D point13 = p2.getMidPoints(p6);//需重构修改
            if (point13 == null) {
                point13 = Point3D.getMiddlePoint3D(p2, p6);
                int point13Id = nodeVList.size() + 1;
                point13.setPointId(point13Id);
                nodeVList.add(point13);
                point13.setEndPoints(p2, p6);
                p2.addMidPoints(point13, p6);
                p6.addMidPoints(point13, p2);
            }
            nodeId_20[13] = point13.getPointId();//需修改

            Point3D point14 = p3.getMidPoints(p7);
            if (point14 == null) {
                point14 = Point3D.getMiddlePoint3D(p3, p7);
                int point14Id = nodeVList.size() + 1;
                point14.setPointId(point14Id);
                nodeVList.add(point14);
                point14.setEndPoints(p3, p7);
                p3.addMidPoints(point14, p7);
                p7.addMidPoints(point14, p3);
            }
            nodeId_20[14] = point14.getPointId();//需修改

            Point3D point15 = p4.getMidPoints(p8);
            if (point15 == null) {
                point15 = Point3D.getMiddlePoint3D(p4, p8);
                int point15Id = nodeVList.size() + 1;
                point15.setPointId(point15Id);
                nodeVList.add(point15);
                point15.setEndPoints(p4, p8);
                p4.addMidPoints(point15, p8);
                p8.addMidPoints(point15, p4);
            }
            nodeId_20[15] = point15.getPointId();//需修改

            Point3D point16 = p5.getMidPoints(p6);
            if (point16 == null) {
                point16 = Point3D.getMiddlePoint3D(p5, p6);
                int point16Id = nodeVList.size() + 1;
                point16.setPointId(point16Id);
                nodeVList.add(point16);
                point16.setEndPoints(p5, p6);
                p5.addMidPoints(point16, p6);
                p6.addMidPoints(point16, p5);
            }
            nodeId_20[16] = point16.getPointId();//需修改

            Point3D point17 = p6.getMidPoints(p7);
            if (point17 == null) {
                point17 = Point3D.getMiddlePoint3D(p6, p7);
                int point17Id = nodeVList.size() + 1;
                point17.setPointId(point17Id);
                nodeVList.add(point17);
                point17.setEndPoints(p6, p7);
                p6.addMidPoints(point17, p7);
                p7.addMidPoints(point17, p6);
            }
            nodeId_20[17] = point17.getPointId();//需修改

            Point3D point18 = p7.getMidPoints(p8);
            if (point18 == null) {
                point18 = Point3D.getMiddlePoint3D(p7, p8);
                int point18Id = nodeVList.size() + 1;
                point18.setPointId(point18Id);
                nodeVList.add(point18);
                point18.setEndPoints(p7, p8);
                p7.addMidPoints(point18, p8);
                p8.addMidPoints(point18, p7);
            }
            nodeId_20[18] = point18.getPointId();//需修改

            Point3D point19 = p8.getMidPoints(p5);
            if (point19 == null) {
                point19 = Point3D.getMiddlePoint3D(p8, p5);
                int point19Id = nodeVList.size() + 1;
                point19.setPointId(point19Id);
                nodeVList.add(point19);
                point19.setEndPoints(p8, p5);
                p8.addMidPoints(point19, p5);
                p5.addMidPoints(point19, p8);
            }
            nodeId_20[19] = point19.getPointId();//需修改

            //在该单元所有节点编号列表nodeId_20初始化完成后，构建单元添加到eleQuad_20中
            eleQuad_20[i] = new EleQuad_20(i + 1, nodeId_20);
        }

        //所有二十节点单元生成之后，将nodeVList转化为nodeV
        nodeV = new Point3D[nodeVList.size()];
        nodeV = nodeVList.toArray(nodeV);

        //更新nodeVNum数值
        nodeVNum = nodeV.length;

    }

    public void setTempInitial(double t) {
        this.tempInitial = new double[nodePNum];
        for (int i = 0; i < nodePNum - 1; i++) {
            this.tempInitial[i] = t;
        }
    }

    public void setTempInitial(String formulation) {
        this.tempInitial = new double[nodePNum];
//        Formulation form = new Formulation(formulation);
//        for (int i = 0; i < nodePNum - 1; i++) {
//            this.tempInitial[i] = form.getValue(nodeP[i].getX(), nodeP[i].getY(), nodeP[i].getZ());
//        }
        for (int i = 0; i < nodePNum - 1; i++) {
            this.tempInitial[i] = 0;
        }
    }

    public double[] getTempInitial() {
        return this.tempInitial;
    }

    public BoundaryPEle[] getPressBoundary() {
        return boundPressEle;
    }

    public EleQuad_8[] getEleQuad_8() {
        return eleQuad_8;
    }

    public EleQuad_20[] getEleQuad_20() {
        return eleQuad_20;
    }

    /**
     * @param eleQuad_8 the eleQuad_8 to set
     */
    public void setEleQuad_8(EleQuad_8[] eleQuad_8) {
        try {
            this.eleQuad_8 = eleQuad_8;
            this.elementNum = eleQuad_8.length;
        } catch (NullPointerException ex) {
            System.out.println("没有网格单元数据！");
            JOptionPane.showMessageDialog(null, "没有网格单元数据！");
        }
    }

    /**
     * @return the elementNum
     */
    public int getElementNum() {
        return elementNum;
    }

    /**
     * @return the node
     */
    public Point3D[] getNodeP() {
        return nodeP;
    }

    public Point3D[] getNode() {
        return nodeP;//实际上nodeP就是直接读进来的节点数据
    }

    public int getNodeNum() {
        return nodePNum;
    }

    public Point3D[] getNodeV() {
        return nodeV;
    }

    /**
     * @param node the node to set
     */
    public void setNode(Point3D[] node) {
        this.nodeP = node;
        this.nodePNum = node.length;
        if (scale != 1) {
            for (int i = 0; i <= nodePNum - 1; i++) {
                nodeP[i] = new Point3D(node[i].getPointId(),
                        node[i].getX() / scale,
                        node[i].getY() / scale, node[i].getZ() / scale);
            }
        }
    }

    /**
     * @return the nodeNum
     */
    public int getNodePNum() {
        return nodePNum;
    }

    public int getNodeVNum() {
        return nodeVNum;
    }

    /**
     * @return the nodeIdSet
     */
    public int[][] getNodeIdSet() {
        return nodeIdSet;
    }

    /**
     * @param nodeIdSet the nodeIdSet to set
     */
    public void setNodeIdSet(int[][] nodeIdSet) {
        this.nodeIdSet = nodeIdSet;
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
     * @return the materialList
     */
    public ArrayList<Material> getMaterialList() {
        return materialList;
    }

    public Material[] getMaterialArray() {
        Material[] materials = new Material[materialList.size()];
        materials = materialList.toArray(materials);
        return materials;
    }

    /**
     * @param materialList the materialList to set
     */
    public void setMaterialArr(Material[] materials) {
        try {
            for (int i = 0; i < materials.length; i++) {
                this.materialList.add(i, materials[i]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "没有材料信息数据！错误位置：Calc"
                    + "ulateInfoBag:setMaterialArr(Material[] materials)");
        }
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
        Boundary[] boundaryArr = new Boundary[boundary.size()];
        //一次存储边界名称，边界条件施加的节点集，变量，变量值
        //其中变量情况如下：
        //   0  ,    1,       2,      3,      4
        //{"压强", "速度u", "速度v", "速度w", "流量"}
        for (int i = 0; i <= boundary.size() - 1; i++) {
            boundaryArr[i] = boundary.get(i);
        }
        return boundaryArr;
    }

    /**
     * @param boundary the boundary to set
     */
    public void setBoundary(ArrayList<Boundary> boundary) {
        ArrayList<BoundaryPEle> boundPList = new ArrayList<>();
        ArrayList<BoundaryVNode> boundVList = new ArrayList<>();
        ArrayList<BoundaryVNode> boundVList88 = new ArrayList<>();
        ArrayList<BoundaryTEle> boundTList = new ArrayList<>();
        for (int i = 0; i <= boundary.size() - 1; i++) {
            if ((boundary.get(i).getVarName()).equals("压强")) {
                //如果变量值为压强，进行边界条件转化
                ArrayList<BoundaryPEle> childBoundaryList
                        = transPressBoundary(boundary.get(i));

                for (int j = 0; j <= childBoundaryList.size() - 1; j++) {
                    boundPList.add(childBoundaryList.get(j));
                }

            } else if (boundary.get(i).getVarName().equals("速度")) {
                //如果变量为速度u
                boundVList = transVeloBoundary(boundary.get(i), boundVList);
                boundVList88 = transVeloBoundary88(boundary.get(i), boundVList88);
            } else if (boundary.get(i).getVarName().equals("流量")) {

            } else if (boundary.get(i).getVarName().equals("温度")) {
                //
                ArrayList<BoundaryTEle> childBoundaryList
                        = transTempBoundary(boundary.get(i));
                for (int j = 0; j <= childBoundaryList.size() - 1; j++) {
                    int index = containsLocal(boundTList, childBoundaryList.get(j));
                    if (index == -1) {
                        //判断childBoundaryList.get(j)中的单元是否已经在边界单元链表boundTList中,index是如果存在的话，元素在boundTList中的索引号，其值为-1表示不存在
                        boundTList.add(childBoundaryList.get(j));
                    } else {
                        addSurface(boundTList.get(index), childBoundaryList.get(j));
                        //如果单元已经在boundTList中，则只需把面添加到单元中即可
                    }
                }
            } else if (boundary.get(i).getVarName().equals("热流密度")) {

            }
        }
        boundPressEle = new BoundaryPEle[boundPList.size()];
        for (int i = 0; i <= boundPList.size() - 1; i++) {
            boundPressEle[i] = boundPList.get(i);
        }
        boundVNode = new BoundaryVNode[boundVList.size()];
        boundVNode = boundVList.toArray(boundVNode);
        boundVNode88 = new BoundaryVNode[boundVList88.size()];
        boundVNode88 = boundVList88.toArray(getVeloBoundary88());
        boundTempEle = new BoundaryTEle[boundTList.size()];
        boundTempEle = boundTList.toArray(getTempBoundary());
        this.boundary = boundary;

    }

    private void addSurface(BoundaryTEle container, BoundaryTEle ele) {
        //将ele中的surface添加到container的surface中
        int[] surface1 = container.getSurface();
        int[] surface2 = ele.getSurface();
        int totalSurfaceNum = surface1.length + surface2.length;
        int[] result = new int[totalSurfaceNum];
        for (int i = 0; i < totalSurfaceNum; i++) {
            if (i < surface1.length) {
                result[i] = surface1[i];
            } else {
                result[i] = surface2[i - surface1.length];
            }
        }
        container.setSurface(result);
    }

    private int containsLocal(ArrayList<BoundaryTEle> container, BoundaryTEle ele) {
        int index = -1;
        for (int i = 0; i <= container.size() - 1; i++) {
            if (container.get(i).getElementId() == ele.getElementId()) {
                index = i;
            }
        }
        return index;
    }

    private ArrayList<BoundaryVNode> transVeloBoundary(Boundary object,
            ArrayList<BoundaryVNode> bVeloNode) {
        ArrayList<BoundaryVNode> boundaryVNode = bVeloNode;
        int dataSetId = object.getNodeSetId();
        double u = object.getU();
        double v = object.getV();
        double w = object.getW();
        if (elementNum == 0) {//不需要删除，耗用速度不大，可以防止程
            //序内部调用该方法时还没有初始化相关参数
            JOptionPane.showMessageDialog(null, "单元集合为空！");
        }
        if (nodePNum == 0) {
            JOptionPane.showMessageDialog(null, "没有节点数据！");
        }

        int[] nodeSet = nodeIdSet[dataSetId];
        ArrayList<Integer> nodeSetList = new ArrayList<>();

        //根绝节点集和边界节点信息判断新插入的速度节点哪些位于边界上
        for (int i = nodePNum; i < nodeVNum; i++) {
            if (containsInt(nodeSet, nodeV[i].getEndPoints()[0].getPointId())
                    && containsInt(nodeSet, nodeV[i].getEndPoints()[1].getPointId())) {
                nodeSetList.add(nodeV[i].getPointId());
            }
        }

        //将位于边界上的速度插入点加到nodeSet中去
        int[] nodeSetNew = new int[nodeSet.length + nodeSetList.size()];
        System.arraycopy(nodeSet, 0, nodeSetNew, 0, nodeSet.length);
        for (int i = 0; i < nodeSetList.size(); i++) {
            nodeSetNew[i + nodeSet.length] = nodeSetList.get(i);
        }
        nodeSet = new int[nodeSetNew.length];
        System.arraycopy(nodeSetNew, 0, nodeSet, 0, nodeSetNew.length);

        //设置速度边界
        for (int i = 0; i <= nodeSet.length - 1; i++) {
            //判断当前节点是否已经存在在边界节点boundaryVNode中
            int index = -1;
            for (int j = 0; j <= boundaryVNode.size() - 1; j++) {
                if (boundaryVNode.get(j).getNodeId() == nodeSet[i]) {
                    index = j;
                    break;
                }
            }
            if (index != -1) {
                boundaryVNode.get(index).setU(u);
                boundaryVNode.get(index).setV(v);
                boundaryVNode.get(index).setW(w);
            } else {
                BoundaryVNode bvn = new BoundaryVNode(nodeSet[i], u, v, w);
                boundaryVNode.add(bvn);
            }
        }

        return boundaryVNode;
    }

    private ArrayList<BoundaryVNode> transVeloBoundary88(Boundary object,
            ArrayList<BoundaryVNode> bVeloNode) {
        ArrayList<BoundaryVNode> boundaryVNode = bVeloNode;
        int dataSetId = object.getNodeSetId();
        double u = object.getU();
        double v = object.getV();
        double w = object.getW();
        if (elementNum == 0) {//不需要删除，耗用速度不大，可以防止程
            //序内部调用该方法时还没有初始化相关参数
            JOptionPane.showMessageDialog(null, "单元集合为空！");
        }
        if (nodePNum == 0) {
            JOptionPane.showMessageDialog(null, "没有节点数据！");
        }

        int[] nodeSet = nodeIdSet[dataSetId];

        //设置速度边界
        for (int i = 0; i <= nodeSet.length - 1; i++) {
            //判断当前节点是否已经存在在边界节点boundaryVNode中
            int index = -1;
            for (int j = 0; j <= boundaryVNode.size() - 1; j++) {
                if (boundaryVNode.get(j).getNodeId() == nodeSet[i]) {
                    index = j;
                    break;
                }
            }
            if (index != -1) {
                boundaryVNode.get(index).setU(u);
                boundaryVNode.get(index).setV(v);
                boundaryVNode.get(index).setW(w);
            } else {
                BoundaryVNode bvn = new BoundaryVNode(nodeSet[i], u, v, w);
                boundaryVNode.add(bvn);
            }
        }

        return boundaryVNode;
    }

    private boolean containsInt(int[] array, int ele) {
        boolean result = false;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == ele) {
                result = true;
            }
        }
        return result;
    }

    private ArrayList<BoundaryPEle> transPressBoundary(Boundary object) {
        //根据Boundary生成BoundaryPEle
        ArrayList<BoundaryPEle> boundaryPressure = new ArrayList<>();        
        int dataSetId = object.getNodeSetId();
        double value = object.getValue();

        if (elementNum == 0) {
            JOptionPane.showMessageDialog(null, "单元集合为空！");
        }
        if (nodePNum == 0) {
            JOptionPane.showMessageDialog(null, "没有节点数据！");
        }
        int[] nodeSet = nodeIdSet[dataSetId];
        //nodeSet为添加压力边界条件的节点集
        for (int i = 0; i <= elementNum - 1; i++) {//对所有压力单元进行循环
            int[] eleNodeList = eleQuad_8[i].getNodeList();
            //eleNodeList为当前单元中的八个节点编号
            ArrayList<Integer> index = new ArrayList<>();//位于压力边界上的点在单元中的索引号的链表
            ArrayList<Integer> boundarySurface = new ArrayList<>();//位于压力边界上的面的链表
            for (int j = 0; j <= nodeSet.length - 1; j++) {
                //判断当前单元的哪些节点在边界上，在边界上的节点在单元中的局部编号加入index数组
                if (eleNodeList[0] == nodeSet[j]) {
                    index.add(0);
                } else if (eleNodeList[1] == nodeSet[j]) {
                    index.add(1);
                } else if (eleNodeList[2] == nodeSet[j]) {
                    index.add(2);
                } else if (eleNodeList[3] == nodeSet[j]) {
                    index.add(3);
                } else if (eleNodeList[4] == nodeSet[j]) {
                    index.add(4);
                } else if (eleNodeList[5] == nodeSet[j]) {
                    index.add(5);
                } else if (eleNodeList[6] == nodeSet[j]) {
                    index.add(6);
                } else if (eleNodeList[7] == nodeSet[j]) {
                    index.add(7);
                }
                
            }
            if (index.size() >= 4) {
                if (index.contains(0) && index.contains(3) && index.contains(7)
                        && index.contains(4)) {
                    boundarySurface.add(0);
                }
                if (index.contains(1) && index.contains(2) && index.contains(6)
                        && index.contains(5)) {
                    boundarySurface.add(1);
                }
                if (index.contains(0) && index.contains(1) && index.contains(5)
                        && index.contains(4)) {
                    boundarySurface.add(2);
                }
                if (index.contains(3) && index.contains(2) && index.contains(6)
                        && index.contains(7)) {
                    boundarySurface.add(3);
                }
                if (index.contains(0) && index.contains(1) && index.contains(2)
                        && index.contains(3)) {
                    boundarySurface.add(4);
                }
                if (index.contains(4) && index.contains(5) && index.contains(6)
                        && index.contains(7)) {
                    boundarySurface.add(5);
                }
            }
            if (boundarySurface.size() >= 1) {
                for (int k = 0; k <= boundarySurface.size() - 1; k++) {
                    int surface = boundarySurface.get(k);
                    double[] cosValue = new double[3];
                    if (surface == 0) {
                        cosValue = getCosByFourPoint3D(nodeP[eleNodeList[0] - 1],
                                nodeP[eleNodeList[4] - 1],
                                nodeP[eleNodeList[7] - 1],
                                nodeP[eleNodeList[3] - 1]);
                        //点的顺序不能换，影响向量方向0473
                    } else if (surface == 1) {
                        cosValue = getCosByFourPoint3D(nodeP[eleNodeList[1] - 1],
                                nodeP[eleNodeList[2] - 1],
                                nodeP[eleNodeList[6] - 1],
                                nodeP[eleNodeList[5] - 1]);
                        //点的顺序不能换，影响向量方向4567
                    } else if (surface == 2) {
                        cosValue = getCosByFourPoint3D(nodeP[eleNodeList[0] - 1],
                                nodeP[eleNodeList[1] - 1],
                                nodeP[eleNodeList[5] - 1],
                                nodeP[eleNodeList[4] - 1]);
                        //点的顺序不能换，影响向量方向0154
                    } else if (surface == 3) {
                        cosValue = getCosByFourPoint3D(nodeP[eleNodeList[3] - 1],
                                nodeP[eleNodeList[7] - 1],
                                nodeP[eleNodeList[6] - 1],
                                nodeP[eleNodeList[2] - 1]);
                        //点的顺序不能换，影响向量方向2376
                    } else if (surface == 4) {
                        cosValue = getCosByFourPoint3D(nodeP[eleNodeList[0] - 1],
                                nodeP[eleNodeList[3] - 1],
                                nodeP[eleNodeList[2] - 1],
                                nodeP[eleNodeList[1] - 1]);
                        //点的顺序不能换，影响向量方向0473
                    } else if (surface == 5) {
                        cosValue = getCosByFourPoint3D(nodeP[eleNodeList[4] - 1],
                                nodeP[eleNodeList[5] - 1],
                                nodeP[eleNodeList[6] - 1],
                                nodeP[eleNodeList[7] - 1]);
                        //点的顺序不能换，影响向量方向1265
                    }

                    BoundaryPEle boundaryEle = new BoundaryPEle(i + 1,
                            surface, cosValue[0], cosValue[1], cosValue[2],
                            value, value, value, value);
                    boundaryPressure.add(boundaryEle);
                }
            }
        }

        return boundaryPressure;

    }

    private ArrayList<BoundaryTEle> transTempBoundary(Boundary object) {
        ArrayList<BoundaryTEle> boundaryT = new ArrayList<>();
        int dataSetId = object.getNodeSetId();
        double value = object.getValue();

        if (elementNum == 0) {
            JOptionPane.showMessageDialog(null, "单元集合为空！");
        }
        if (nodePNum == 0) {
            JOptionPane.showMessageDialog(null, "没有节点数据！");
        }
        int[] nodeSet = nodeIdSet[dataSetId];
        //nodeSet为添加压力边界条件的节点集
        for (int i = 0; i <= elementNum - 1; i++) {
            int[] eleNodeList = eleQuad_8[i].getNodeList();
            //eleNodeList为当前单元中的八个节点编号
            ArrayList<Integer> index = new ArrayList<>();
            ArrayList<Integer> boundarySurface = new ArrayList<>();
            for (int j = 0; j <= nodeSet.length - 1; j++) {
                //判断当前单元的哪些节点在边界上，在边界上的节点在单元中的局部编号加入index数组
                if (eleNodeList[0] == nodeSet[j]) {
                    index.add(0);
                } else if (eleNodeList[1] == nodeSet[j]) {
                    index.add(1);
                } else if (eleNodeList[2] == nodeSet[j]) {
                    index.add(2);
                } else if (eleNodeList[3] == nodeSet[j]) {
                    index.add(3);
                } else if (eleNodeList[4] == nodeSet[j]) {
                    index.add(4);
                } else if (eleNodeList[5] == nodeSet[j]) {
                    index.add(5);
                } else if (eleNodeList[6] == nodeSet[j]) {
                    index.add(6);
                } else if (eleNodeList[7] == nodeSet[j]) {
                    index.add(7);
                }
            }
            if (index.size() >= 4) {
                //面的编号顺序规则：(0)xmin->(1)xmax;(2)ymin->(3)ymax;(4)zmin->(5)zmax
                //面内节点编号顺序：
                /**
                 * 如果是xy面则为：(xmin,ymin),(xmax,ymin),(xmax,ymax),(xmin,ymax)
                 * 如果是yz面则为：(ymin,zmin),(ymax,zmin),(ymax,zmax),(ymin,zmax)
                 * 如果是xz面则为：(xmin,zmin),(xmax,zmin),(xmax,zmax),(xmin,zmax)
                 */
                if (index.contains(0) && index.contains(3) && index.contains(7)
                        && index.contains(4)) {
                    boundarySurface.add(0);
                }
                if (index.contains(1) && index.contains(2) && index.contains(6)
                        && index.contains(5)) {
                    boundarySurface.add(1);
                }
                if (index.contains(0) && index.contains(1) && index.contains(5)
                        && index.contains(4)) {
                    boundarySurface.add(2);
                }
                if (index.contains(3) && index.contains(2) && index.contains(6)
                        && index.contains(7)) {
                    boundarySurface.add(3);
                }
                if (index.contains(0) && index.contains(1) && index.contains(2)
                        && index.contains(3)) {
                    boundarySurface.add(4);
                }
                if (index.contains(4) && index.contains(5) && index.contains(6)
                        && index.contains(7)) {
                    boundarySurface.add(5);
                }
            }
            if (boundarySurface.size() > 0) {
                int[] surface = new int[boundarySurface.size()];
                for (int j = 0; j <= surface.length - 1; j++) {
                    surface[j] = boundarySurface.get(j);
                }

                BoundaryTEle boundaryEle = new BoundaryTEle(i + 1,
                        surface,
                        value, value, value, value);
                boundaryT.add(boundaryEle);
            }

        }

        return boundaryT;

    }

    private double[] getCosByFourPoint3D(Point3D node1, Point3D node2,
            Point3D node3, Point3D node4) {
        double[] cosValue = new double[3];
        double x1 = node1.getX();
        double x2 = node2.getX();
        double x3 = node3.getX();
        double x4 = node4.getX();
        double y1 = node1.getY();
        double y2 = node2.getY();
        double y3 = node3.getY();
        double y4 = node4.getY();
        double z1 = node1.getZ();
        double z2 = node2.getZ();
        double z3 = node3.getZ();
        double z4 = node4.getZ();
        //向量12
        double vector12_x = x2 - x1;
        double vector12_y = y2 - y1;
        double vector12_z = z2 - z1;
        //向量23
        double vector23_x = x3 - x2;
        double vector23_y = y3 - y2;
        double vector23_z = z3 - z2;
        //向量34
        double vector34_x = x4 - x3;
        double vector34_y = y4 - y3;
        double vector34_z = z4 - z3;
        //向量41
        double vector41_x = x1 - x4;
        double vector41_y = y1 - y4;
        double vector41_z = z1 - z4;
        //向量12×向量23,是123点确定的面的一个法向量，不是单位向量
        double vectorN1_x = (vector12_y * vector23_z) - (vector23_y * vector12_z);
        double vectorN1_y = (vector23_x * vector12_z) - (vector12_x * vector23_z);
        double vectorN1_z = (vector12_x * vector23_y) - (vector23_x * vector12_y);
        double cosx1 = vectorN1_x / sqrt(pow(vectorN1_x, 2)
                + pow(vectorN1_y, 2) + pow(vectorN1_z, 2));
        double cosy1 = vectorN1_y / sqrt(pow(vectorN1_x, 2)
                + pow(vectorN1_y, 2) + pow(vectorN1_z, 2));
        double cosz1 = vectorN1_z / sqrt(pow(vectorN1_x, 2)
                + pow(vectorN1_y, 2) + pow(vectorN1_z, 2));

        //向量23×向量34,是234点确定的面的一个法向量，不是单位向量
        double vectorN2_x = (vector23_y * vector34_z) - (vector34_y * vector23_z);
        double vectorN2_y = (vector34_x * vector23_z) - (vector23_x * vector34_z);
        double vectorN2_z = (vector23_x * vector34_y) - (vector34_x * vector23_y);
        double cosx2 = vectorN2_x / sqrt(pow(vectorN2_x, 2)
                + pow(vectorN2_y, 2) + pow(vectorN2_z, 2));
        double cosy2 = vectorN2_y / sqrt(pow(vectorN2_x, 2)
                + pow(vectorN2_y, 2) + pow(vectorN2_z, 2));
        double cosz2 = vectorN2_z / sqrt(pow(vectorN2_x, 2)
                + pow(vectorN2_y, 2) + pow(vectorN2_z, 2));

        //向量34×向量41,是341点确定的面的一个法向量，不是单位向量
        double vectorN3_x = (vector34_y * vector41_z) - (vector41_y * vector34_z);
        double vectorN3_y = (vector41_x * vector34_z) - (vector34_x * vector41_z);
        double vectorN3_z = (vector34_x * vector41_y) - (vector41_x * vector34_y);
        double cosx3 = vectorN3_x / sqrt(pow(vectorN3_x, 2)
                + pow(vectorN3_y, 2) + pow(vectorN3_z, 2));
        double cosy3 = vectorN3_y / sqrt(pow(vectorN3_x, 2)
                + pow(vectorN3_y, 2) + pow(vectorN3_z, 2));
        double cosz3 = vectorN3_z / sqrt(pow(vectorN3_x, 2)
                + pow(vectorN3_y, 2) + pow(vectorN3_z, 2));

        //向量41×向量12,是412点确定的面的一个法向量，不是单位向量
        double vectorN4_x = (vector41_y * vector12_z) - (vector12_y * vector41_z);
        double vectorN4_y = (vector12_x * vector41_z) - (vector41_x * vector12_z);
        double vectorN4_z = (vector41_x * vector12_y) - (vector12_x * vector41_y);
        double cosx4 = vectorN4_x / sqrt(pow(vectorN4_x, 2)
                + pow(vectorN4_y, 2) + pow(vectorN4_z, 2));
        double cosy4 = vectorN4_y / sqrt(pow(vectorN4_x, 2)
                + pow(vectorN4_y, 2) + pow(vectorN4_z, 2));
        double cosz4 = vectorN4_z / sqrt(pow(vectorN4_x, 2)
                + pow(vectorN4_y, 2) + pow(vectorN4_z, 2));

        cosValue[0] = (cosx1 + cosx2 + cosx3 + cosx4) / 4.0;
        cosValue[1] = (cosy1 + cosy2 + cosy3 + cosy4) / 4.0;
        cosValue[2] = (cosz1 + cosz2 + cosz3 + cosz4) / 4.0;
        return cosValue;
    }

    /**
     * @return the gaussPointNum
     */
    public int getGaussPointNum() {
        return gaussPointNum;
    }

    /**
     * @param gaussPointNum the gaussPointNum to set
     */
    public void setGaussPointNum(int gaussPointNum) {
        this.gaussPointNum = gaussPointNum;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return the bVNode
     */
    public BoundaryVNode[] getVeloBoundary() {
        return boundVNode;
    }

    /**
     * @return the boundTempEle
     */
    public BoundaryTEle[] getTempBoundary() {
        return boundTempEle;
    }

    /**
     * @return the boundVNode88
     */
    public BoundaryVNode[] getVeloBoundary88() {
        return boundVNode88;
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
