/*
 * 本例参考《计算流体力学有限元方法及其编程详解·毕超》第三章
 * 考虑三维模型，形函数为八节点一次六面体单元
 */
package simple;

import MATERIAL.Material;
import common.CalculateInfoBag;
import common.FileChooser;
import common.FileOperate;
import common.Matrix;
import common.GaussPoint;
import element.BoundaryPEle;
import element.BoundaryVNode;
import element.EleQuad_20;
import element.EleQuad_8;
import element.Fai_3D_20node;
import element.Fai_3D_8node;
import element.Part;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import point.Point2D;
import point.Point3D;

/**
 *
 * @author yk
 */
public class Example3_1_1 extends JFrame {

    private EleQuad_8[] eleQuad_8;
    private EleQuad_20[] eleQuad_20;
    private Part[] parts;//存储网格单元所属的部分的材料等其他属性标记号
    private int elementNum;
    private Point3D[] node;
    private double miu;
    private double rou;
    private int nodeVNum;
    private int nodePNum;
    private int[][] nodeIdSet;
    private BoundaryVNode[] bounVNode;//边界条件名称，节点集，变量名称，变量值
    private BoundaryPEle[] bounPreEle;
    private int analysisType;
    private double initialTime;
    private double timeSteps;
    private double totalTime;
    private int gaussPointNum;
    private String command;
    private CalculateInfoBag dataBag = null;
    double[] pLast;
    double[][] vLast;//节点的速度，其中vLast[i][0]为i节点x方向速度，vLast[i][1]
    //为y方向速度，vLast[i][2]为z方向速度
    private File file;//计算结果保存的文件夹路径
    private int maxIterNum;
    private double residual;
    private Material[] materials;

    public Example3_1_1(CalculateInfoBag initData) {
        this.dataBag = initData;
        this.setTitle("三维六面体单元计算");
        this.setSize(600, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel mainPanel = new JPanel(new FlowLayout());
        JButton calculateButton = new JButton("开始计算");
        JButton printButton = new JButton("数据信息");
        JButton printWholeMatrixToFileButton = new JButton("输出整体矩阵");
        mainPanel.add(calculateButton);
        mainPanel.add(printButton);
        this.add(mainPanel);
        calculateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (analysisType == 2) {
                    new Temp3D(dataBag);
                } else {
                    String defaultFilePath = System.getProperty("usr.dir");
                    file = new FileChooser("保存结果至", 3, defaultFilePath).getFile();
                    if (file.exists()) {
                        File[] files = file.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            files[i].delete();
                        }
                    }
                    readData();
                    calculate();
                }
            }
        });

        printButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                printInfo();
            }
        });
    }

    private void readData() {
        eleQuad_8 = dataBag.getEleQuad_8();
        eleQuad_20 = dataBag.getEleQuad_20();
        parts = dataBag.getParts();
        node = dataBag.getNodeV();
        nodeIdSet = dataBag.getNodeIdSet();
        nodeVNum = dataBag.getNodeVNum();
        nodePNum = dataBag.getNodePNum();
        elementNum = dataBag.getElementNum();
        materials = dataBag.getMaterialArray();
        bounPreEle = dataBag.getPressBoundary();
        bounVNode = dataBag.getVeloBoundary();
        gaussPointNum = dataBag.getGaussPointNum();
        command = dataBag.getCommand();
        analysisType = dataBag.getAnalysisType();
        initialTime = dataBag.getAnalysisType_initialTime();
        timeSteps = dataBag.getAnalysisType_timeSteps();
        totalTime = dataBag.getAnalysisType_totalTime();
        pLast = new double[nodePNum];
        vLast = new double[nodeVNum][3];
        maxIterNum = dataBag.getMaxIterNum();
        residual = dataBag.getMaxErr();
    }

    private void printInfo() {
        printInitInfo();
        printNodeIdSet();
    }

    private void calculate() {

//        Matrix B0 = new Matrix(nodePNum, nodeVNum);
//        Matrix B1 = new Matrix(nodePNum, nodeVNum);
//        Matrix B2 = new Matrix(nodePNum, nodeVNum);
//        Matrix D00 = new Matrix(nodeVNum, nodeVNum);
//        Matrix D01 = new Matrix(nodeVNum, nodeVNum);
//        Matrix D02 = new Matrix(nodeVNum, nodeVNum);
//        Matrix D10 = new Matrix(nodeVNum, nodeVNum);
//        Matrix D11 = new Matrix(nodeVNum, nodeVNum);
//        Matrix D12 = new Matrix(nodeVNum, nodeVNum);
//        Matrix D20 = new Matrix(nodeVNum, nodeVNum);
//        Matrix D21 = new Matrix(nodeVNum, nodeVNum);
//        Matrix D22 = new Matrix(nodeVNum, nodeVNum);
//        Matrix C0 = new Matrix(nodeVNum, nodePNum);
//        Matrix C1 = new Matrix(nodeVNum, nodePNum);
//        Matrix C2 = new Matrix(nodeVNum, nodePNum);
//        Matrix F0 = new Matrix(nodeVNum, 1);
//        Matrix F1 = new Matrix(nodeVNum, 1);
//        Matrix F2 = new Matrix(nodeVNum, 1);

        Matrix[][] matrixSet;

        double[][] b0_arr;//单元矩阵的数据
        double[][] b1_arr;
        double[][] b2_arr;
        double[][] c0_arr;
        double[][] c1_arr;
        double[][] c2_arr;
        double[][] d00_arr;
        double[][] d01_arr;
        double[][] d02_arr;
        double[][] d10_arr;
        double[][] d11_arr;
        double[][] d12_arr;
        double[][] d20_arr;
        double[][] d21_arr;
        double[][] d22_arr;
        double[][] f0_arr;
        double[][] f1_arr;
        double[][] f2_arr;

        //合成整体矩阵
        double[] p = new double[nodePNum];
        double[][] v = new double[nodeVNum][3];

        for (int iter = 0; iter < maxIterNum; iter++) {

            double[][] B0_arr = new double[nodePNum][nodeVNum];//整体矩阵的数据
            double[][] B1_arr = new double[nodePNum][nodeVNum];
            double[][] B2_arr = new double[nodePNum][nodeVNum];
            double[][] D00_arr = new double[nodeVNum][nodeVNum];
            double[][] D01_arr = new double[nodeVNum][nodeVNum];
            double[][] D02_arr = new double[nodeVNum][nodeVNum];
            double[][] D10_arr = new double[nodeVNum][nodeVNum];
            double[][] D11_arr = new double[nodeVNum][nodeVNum];
            double[][] D12_arr = new double[nodeVNum][nodeVNum];
            double[][] D20_arr = new double[nodeVNum][nodeVNum];
            double[][] D21_arr = new double[nodeVNum][nodeVNum];
            double[][] D22_arr = new double[nodeVNum][nodeVNum];
            double[][] C0_arr = new double[nodeVNum][nodePNum];
            double[][] C1_arr = new double[nodeVNum][nodePNum];
            double[][] C2_arr = new double[nodeVNum][nodePNum];
            double[][] F0_arr = new double[nodeVNum][1];
            double[][] F1_arr = new double[nodeVNum][1];
            double[][] F2_arr = new double[nodeVNum][1];
            
            for (int i = 0; i <= elementNum - 1; i++) {
                System.out.println("当前计算‘单元编号/总单元数’为" + (i + 1)
                        + "/" + elementNum + "；");
                matrixSet = getElementMatrix(i);//单元系数矩阵组

                b0_arr = matrixSet[3][0].toArray();
                b1_arr = matrixSet[3][1].toArray();
                b2_arr = matrixSet[3][2].toArray();
                c0_arr = matrixSet[0][3].toArray();
                c1_arr = matrixSet[1][3].toArray();
                c2_arr = matrixSet[2][3].toArray();
                d00_arr = matrixSet[0][0].toArray();
                d01_arr = matrixSet[0][1].toArray();
                d02_arr = matrixSet[0][2].toArray();
                d10_arr = matrixSet[1][0].toArray();
                d11_arr = matrixSet[1][1].toArray();
                d12_arr = matrixSet[1][2].toArray();
                d20_arr = matrixSet[2][0].toArray();
                d21_arr = matrixSet[2][1].toArray();
                d22_arr = matrixSet[2][2].toArray();

                //由各个单元矩阵子块组合整体系数矩阵的各个子块
                for (int m = 0; m <= 7; m++) {
                    for (int n = 0; n <= 19; n++) {
                        int mm = eleQuad_8[i].getNodeList()[m] - 1;//mm是节点的数组索引号
                        int nn = eleQuad_20[i].getNodeList()[n] - 1;//nn是节点的数组索引号
                        B0_arr[mm][nn] = B0_arr[mm][nn] + b0_arr[m][n];
                        B1_arr[mm][nn] = B1_arr[mm][nn] + b1_arr[m][n];
                        B2_arr[mm][nn] = B2_arr[mm][nn] + b2_arr[m][n];
                    }
                }

                for (int m = 0; m <= 19; m++) {
                    for (int n = 0; n <= 19; n++) {
                        int mm = eleQuad_20[i].getNodeList()[m] - 1;//mm是节点的数组索引号
                        int nn = eleQuad_20[i].getNodeList()[n] - 1;//nn是节点的数组索引号
                        D00_arr[mm][nn] = D00_arr[mm][nn] + d00_arr[m][n];
                        D01_arr[mm][nn] = D01_arr[mm][nn] + d01_arr[m][n];
                        D02_arr[mm][nn] = D02_arr[mm][nn] + d02_arr[m][n];
                        D10_arr[mm][nn] = D10_arr[mm][nn] + d10_arr[m][n];
                        D11_arr[mm][nn] = D11_arr[mm][nn] + d11_arr[m][n];
                        D12_arr[mm][nn] = D12_arr[mm][nn] + d12_arr[m][n];
                        D20_arr[mm][nn] = D20_arr[mm][nn] + d20_arr[m][n];
                        D21_arr[mm][nn] = D21_arr[mm][nn] + d21_arr[m][n];
                        D22_arr[mm][nn] = D22_arr[mm][nn] + d22_arr[m][n];
                    }
                }
                for (int m = 0; m <= 19; m++) {
                    for (int n = 0; n <= 7; n++) {
                        int mm = eleQuad_20[i].getNodeList()[m] - 1;//mm是节点的数组索引号
                        int nn = eleQuad_8[i].getNodeList()[n] - 1;//nn是节点的数组索引号
                        C0_arr[mm][nn] = C0_arr[mm][nn] + c0_arr[m][n];
                        C1_arr[mm][nn] = C1_arr[mm][nn] + c1_arr[m][n];
                        C2_arr[mm][nn] = C2_arr[mm][nn] + c2_arr[m][n];
                    }
                }

            }

            //当且仅当当前单元为压力边界单元时，进行对F子块求和。
            for (int i = 0; i <= bounPreEle.length - 1; i++) {
                Matrix[] F = getF(bounPreEle[i]);
                f0_arr = F[0].toArray();
                f1_arr = F[1].toArray();
                f2_arr = F[2].toArray();
                for (int m = 0; m <= 19; m++) {
                    int mm = eleQuad_20[bounPreEle[i].getElementId() - 1].getNodeList()[m] - 1;//mm是节点的数组索引号
                    F0_arr[mm][0] = F0_arr[mm][0] + f0_arr[m][0];
                    F1_arr[mm][0] = F1_arr[mm][0] + f1_arr[m][0];
                    F2_arr[mm][0] = F2_arr[mm][0] + f2_arr[m][0];
                }
            }

            //C子块和F子块取负值
            for (int i = 0; i <= C0_arr.length - 1; i++) {
                for (int j = 0; j <= C0_arr[0].length - 1; j++) {
                    C0_arr[i][j] = -1.0 * C0_arr[i][j];
                    C1_arr[i][j] = -1.0 * C1_arr[i][j];
                    C2_arr[i][j] = -1.0 * C2_arr[i][j];
                }
            }
            for (int i = 0; i <= F0_arr.length - 1; i++) {
                F0_arr[i][0] = -F0_arr[i][0];
                F1_arr[i][0] = -F1_arr[i][0];
                F2_arr[i][0] = -F2_arr[i][0];
            }

            //至此，计算出了整体矩阵的各个子块，下面是求解整体矩阵
            double[][] wholeArr = new double[(nodeVNum * 3 + nodePNum)][(nodeVNum
                    * 3 + nodePNum + 1)];
            for (int i = 0; i <= nodeVNum - 1; i++) {//整体矩阵第一行
                if (i <= nodeVNum - 1) {
                    for (int j = 0; j <= nodeVNum - 1; j++) {
                        wholeArr[i][j] = D00_arr[i][j];
                    }
                    for (int j = nodeVNum; j <= 2 * nodeVNum - 1; j++) {
                        wholeArr[i][j] = D01_arr[i][j - nodeVNum];
                    }
                    for (int j = 2 * nodeVNum; j <= 3 * nodeVNum - 1; j++) {
                        wholeArr[i][j] = D02_arr[i][j - 2 * nodeVNum];
                    }
                    for (int j = 3 * nodeVNum; j <= (3 * nodeVNum + nodePNum - 1); j++) {
                        wholeArr[i][j] = C0_arr[i][j - 3 * nodeVNum];
                    }
                }
            }
            for (int i = nodeVNum; i <= 2 * nodeVNum - 1; i++) {//整体矩阵第二行
                for (int j = 0; j <= nodeVNum - 1; j++) {
                    wholeArr[i][j] = D10_arr[i - nodeVNum][j];
                }
                for (int j = nodeVNum; j <= 2 * nodeVNum - 1; j++) {
                    wholeArr[i][j] = D11_arr[i - nodeVNum][j - nodeVNum];
                }
                for (int j = 2 * nodeVNum; j <= 3 * nodeVNum - 1; j++) {
                    wholeArr[i][j] = D12_arr[i - nodeVNum][j - 2 * nodeVNum];
                }
                for (int j = 3 * nodeVNum; j <= 3 * nodeVNum + nodePNum - 1; j++) {
                    wholeArr[i][j] = C1_arr[i - nodeVNum][j - 3 * nodeVNum];
                }
            }
            for (int i = 2 * nodeVNum; i <= 3 * nodeVNum - 1; i++) {
                for (int j = 0; j <= nodeVNum - 1; j++) {
                    wholeArr[i][j] = D20_arr[i - 2 * nodeVNum][j];
                }
                for (int j = nodeVNum; j <= 2 * nodeVNum - 1; j++) {
                    wholeArr[i][j] = D21_arr[i - 2 * nodeVNum][j - nodeVNum];
                }
                for (int j = 2 * nodeVNum; j <= 3 * nodeVNum - 1; j++) {
                    wholeArr[i][j] = D22_arr[i - 2 * nodeVNum][j - 2 * nodeVNum];
                }
                for (int j = 3 * nodeVNum; j <= 3 * nodeVNum + nodePNum - 1; j++) {
                    wholeArr[i][j] = C2_arr[i - 2 * nodeVNum][j - 3 * nodeVNum];
                }
            }
            for (int i = 3 * nodeVNum; i <= 3 * nodeVNum + nodePNum - 1; i++) {
                for (int j = 0; j <= nodeVNum - 1; j++) {
                    wholeArr[i][j] = B0_arr[i - 3 * nodeVNum][j];
                }
                for (int j = nodeVNum; j <= 2 * nodeVNum - 1; j++) {
                    wholeArr[i][j] = B1_arr[i - 3 * nodeVNum][j - nodeVNum];
                }
                for (int j = 2 * nodeVNum; j <= 3 * nodeVNum - 1; j++) {
                    wholeArr[i][j] = B2_arr[i - 3 * nodeVNum][j - 2 * nodeVNum];
                }
                for (int j = 3 * nodeVNum; j <= 3 * nodeVNum + nodePNum - 1; j++) {
                    wholeArr[i][j] = 0;
                }
            }
            for (int i = 0; i <= nodeVNum - 1; i++) {//增广矩阵的最后一列
                wholeArr[i][3 * nodeVNum + nodePNum] = F0_arr[i][0];
            }
            for (int i = nodeVNum; i <= 2 * nodeVNum - 1; i++) {
                wholeArr[i][3 * nodeVNum + nodePNum] = F1_arr[i - nodeVNum][0];
            }
            for (int i = 2 * nodeVNum; i <= 3 * nodeVNum - 1; i++) {
                wholeArr[i][3 * nodeVNum + nodePNum] = F2_arr[i - 2 * nodeVNum][0];
            }
            for (int i = 3 * nodeVNum; i <= 3 * nodeVNum + nodePNum - 1; i++) {
                wholeArr[i][3 * nodeVNum + nodePNum] = 0;
            }

            //下面，添加第一类边界条件，及边界节点的已知速度值
            System.out.println("开始添加边界条件<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                    + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            Matrix whole_matrix = new Matrix(wholeArr);
            whole_matrix.writeToFile(new File(file, "wholeMatrix.txt"));

            addBoundaryCondition(wholeArr, command);
            whole_matrix = new Matrix(wholeArr);
            whole_matrix.writeToFile(new File(file, "整体矩阵修正后.txt"));
            //System.out.println("whole_matrix:");
            //whole_matrix.printMatrix();
            System.out.println("边界条件添加完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                    + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println("开始求解整体矩阵<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                    + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            double[] result = whole_matrix.getSolution();
            System.out.println("整体矩阵求解完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                    + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            System.out.println(
                    "计算结果如下：");

            for (int i = 0; i <= nodeVNum - 1; i++) {
                System.out.println("u[" + (i + 1) + "]=" + result[i]);
                v[i][0] = result[i];
            }
            for (int i = nodeVNum; i <= 2 * nodeVNum - 1; i++) {
                System.out.println("v[" + (i - nodeVNum + 1) + "]=" + result[i]);
                v[i - nodeVNum][1] = result[i];
            }
            for (int i = 2 * nodeVNum; i <= 3 * nodeVNum - 1; i++) {
                System.out.println("w[" + (i - 2 * nodeVNum + 1) + "]=" + result[i]);
                v[i - 2 * nodeVNum][2] = result[i];
            }
            for (int i = 3 * nodeVNum; i <= 3 * nodeVNum + nodePNum - 1; i++) {
                System.out.println("p[" + (i - 3 * nodeVNum + 1) + "]=" + result[i]);
                p[i - 3 * nodeVNum] = result[i];
            }
            if (delta(v, vLast) < residual && delta(p, pLast) < residual) {

                break;
            } else {
//                vLast = v;//这样赋值是将v的地址传递给vLast,以后当v改变时，vLast也会随之改变，此处不能这样用
//                pLast = p;
                for (int vl = 0; vl < v.length; vl++) {
                    System.arraycopy(v[vl], 0, vLast[vl], 0, 3);
                }

                System.arraycopy(p, 0, pLast, 0, p.length);

            }

            Matrix resultMatrix = new Matrix(v);
            double[] nodeID = new double[nodeVNum];
//            for (int i = 0; i < nodeVNum; i++) {
//                nodeID[i] = i + 1;
//            }
            for (int i = 0; i < nodeVNum; i++) {
                nodeID[i] = node[i].getZ();
            }
            resultMatrix = resultMatrix.addColumn(0, nodeID);
            for (int i = 0; i < nodeVNum; i++) {
                nodeID[i] = node[i].getY();
            }
            resultMatrix = resultMatrix.addColumn(0, nodeID);
            for (int i = 0; i < nodeVNum; i++) {
                nodeID[i] = node[i].getX();
            }
            resultMatrix = resultMatrix.addColumn(0, nodeID);

            resultMatrix = resultMatrix.addColumn(resultMatrix.getColumns(), p);
            File file1 = new File(file, "Example3_1_1_result_" + iter + ".txt");
            FileOperate fileOperate = new FileOperate(file1);
            fileOperate.writeToFileString("VARIABLES = \"X\",\"Y\",\"Z\",\"u\",\"v\",\"w\",\"p\"", false);
//            fileOperate.writeToFileInt(gaussPointNum, true);
            fileOperate.writeToFileString("\n\n", true);
            resultMatrix.writeToFile(file1, true);
        }

    }

    private double delta(double[] f, double[] fLast) {
        double delta = 0;
        for (int i = 0; i < f.length; i++) {
//            if (f[i]==NaN){
//                
//            }
            if (abs(f[i] - fLast[i]) > delta) {
                delta = abs(f[i] - fLast[i]);
            }
        }
        return delta;
    }

    private double delta(double[][] f, double[][] fLast) {
        double delta = 0;
        for (int i = 0; i < f.length; i++) {
            for (int j = 0; j < f[0].length; j++) {
                if (abs(f[i][j] - fLast[i][j]) > delta) {
                    delta = abs(f[i][j] - fLast[i][j]);
                }
            }
        }
        return delta;
    }

    private Matrix[][] getElementMatrix(int elementIndex) {
        Matrix[][] matrix = new Matrix[4][4];
        Matrix b1_matrix = new Matrix(8, 20);//创建一个8行8列元素全为0的矩阵
        Matrix b2_matrix = new Matrix(8, 20);
        Matrix b3_matrix = new Matrix(8, 20);
        Matrix c1_matrix = new Matrix(20, 8);
        Matrix c2_matrix = new Matrix(20, 8);
        Matrix c3_matrix = new Matrix(20, 8);
        Matrix d11_matrix = new Matrix(20, 20);
        Matrix d12_matrix = new Matrix(20, 20);
        Matrix d13_matrix = new Matrix(20, 20);
        Matrix d21_matrix = new Matrix(20, 20);
        Matrix d22_matrix = new Matrix(20, 20);
        Matrix d23_matrix = new Matrix(20, 20);
        Matrix d31_matrix = new Matrix(20, 20);
        Matrix d32_matrix = new Matrix(20, 20);
        Matrix d33_matrix = new Matrix(20, 20);
        Matrix q1_matrix = new Matrix(20, 20);
        Matrix q2_matrix = new Matrix(20, 20);
        Matrix q3_matrix = new Matrix(20, 20);
        double[] u_i_e = new double[20];
        double[] v_i_e = new double[20];
        double[] w_i_e = new double[20];

        int pid = eleQuad_8[elementIndex].getPid();
        int MID = parts[pid - 1].getMID();
        miu = materials[MID - 1].getMiu();
        rou = materials[MID - 1].getDensity();
        //取得当前单元的八个节点的信息
        Point3D[] point8 = new Point3D[8];
        Point3D[] point20 = new Point3D[20];
        System.out.println("该计算单元的8个节点信息为(节点编号，x坐标，y坐标,z坐"
                + "标)：-------------------------------------------------- ");
        for (int i = 0; i <= 7; i++) {
            point8[i] = node[eleQuad_8[elementIndex].getNodeList()[i] - 1];
            point8[i].printPoint3D();

        }

        for (int i = 0; i <= 19; i++) {
            point20[i] = node[eleQuad_20[elementIndex].getNodeList()[i] - 1];
            u_i_e[i] = vLast[point20[i].getPointId() - 1][0];
            v_i_e[i] = vLast[point20[i].getPointId() - 1][1];
            w_i_e[i] = vLast[point20[i].getPointId() - 1][2];
        }

        System.out.println("================================================="
                + "=========================================================");

        //取得高斯积分点
        Point2D[] gaussPointList = new GaussPoint(gaussPointNum).getPointList();

        //循环叠加求矩阵B
        int bounPID = -1;//-1表示没有进行过边界单元判断，-2表示不是边界单元，其他大
        //于等于0的值表示是边界单元，且边界单元编号为bounPID
        for (int i = 0; i <= gaussPointNum - 1; i++) {
            for (int j = 0; j <= gaussPointNum - 1; j++) {
                for (int k = 0; k <= gaussPointNum - 1; k++) {
                    Matrix fyp_matrix_81;
                    Matrix fyp_matrix_18;
                    double[] fyp_kesi;
                    double[] fyp_ita;
                    double[] fyp_zeta;
                    Matrix fyv_120;
                    Matrix fyv_201;
                    double[] fyv_kesi;
                    double[] fyv_ita;
                    double[] fyv_zeta;
                    Matrix tempMatrix1;
                    Matrix tempMatrix2;
                    Matrix tempMatrix3;
                    Matrix tempMatrix;
                    Matrix jacobiMatrix_33;
                    double detJ;
                    double kesi;
                    double ita;
                    double zeta;

                    kesi = gaussPointList[i].getX();
                    ita = gaussPointList[j].getX();
                    zeta = gaussPointList[k].getX();
                    Fai_3D_8node fai_3D_8node = new Fai_3D_8node(point8, kesi, ita, zeta);

                    fyp_matrix_81 = fai_3D_8node.getN_iMatrix(false);//列向量
                    fyp_matrix_18 = fai_3D_8node.getN_iMatrix(true);//行向量

                    fyp_kesi = fai_3D_8node.getFai_kesi();
                    fyp_ita = fai_3D_8node.getFai_ita();
                    fyp_zeta = fai_3D_8node.getFai_zeta();

                    Fai_3D_20node fai_3D_20node = new Fai_3D_20node(point20, kesi, ita, zeta);

                    fyv_201 = fai_3D_20node.getN_iMatrix(false);
                    fyv_120 = fai_3D_20node.getN_iMatrix(true);
                    fyv_kesi = fai_3D_20node.getFai_kesi();
                    fyv_ita = fai_3D_20node.getFai_ita();
                    fyv_zeta = fai_3D_20node.getFai_zeta();
                    //至此，速度和压力插值函数的形函数及相关导数都已获得

                    //下面，求取速度插值函数对x,y,z的导数
                    double[][] arr320 = new double[3][20];
                    for (int kin = 0; kin <= 19; kin++) {
                        arr320[0][kin] = fyv_kesi[kin];
                        arr320[1][kin] = fyv_ita[kin];
                        arr320[2][kin] = fyv_zeta[kin];
                    }
                    Matrix tempMatrix_320;
                    tempMatrix_320 = new Matrix(arr320);
                    jacobiMatrix_33 = fai_3D_20node.getJacobiMatrix();
                    jacobiMatrix_33 = fai_3D_8node.getJacobiMatrix();
                    //fai_3D_20node.getJacobiMatrix()和fai_3D_8node.getJacobiMatrix()相等，无需重复计算
                    tempMatrix_320 = Matrix.cross(jacobiMatrix_33.inverseMatrix(),
                            tempMatrix_320);
                    double[] tempArr4 = new double[20];
                    double[] tempArr5 = new double[20];
                    double[] tempArr6 = new double[20];
                    for (int kin = 0; kin <= 19; kin++) {
                        tempArr4[kin] = tempMatrix_320.toArray()[0][kin];
                        tempArr5[kin] = tempMatrix_320.toArray()[1][kin];
                        tempArr6[kin] = tempMatrix_320.toArray()[2][kin];
                    }
                    Matrix fyv_x_120, fyv_x_201;
                    Matrix fyv_y_120, fyv_y_201;
                    Matrix fyv_z_120, fyv_z_201;
                    fyv_x_120 = new Matrix(true, tempArr4);
                    fyv_y_120 = new Matrix(true, tempArr5);
                    fyv_z_120 = new Matrix(true, tempArr6);
                    fyv_x_201 = new Matrix(false, tempArr4);
                    fyv_y_201 = new Matrix(false, tempArr5);
                    fyv_z_201 = new Matrix(false, tempArr6);

                    detJ = jacobiMatrix_33.getDetValue();
                    detJ = detJ * gaussPointList[i].getY() * gaussPointList[j]
                            .getY() * gaussPointList[k].getY();
//*************************************组合B单元子块***********************
                    Matrix tempMatrix_820;
                    tempMatrix_820 = Matrix.cross(fyp_matrix_81, fyv_x_120);
                    tempMatrix_820 = tempMatrix_820.multiply(detJ);
                    b1_matrix = Matrix.plus(b1_matrix, tempMatrix_820);

                    tempMatrix_820 = Matrix.cross(fyp_matrix_81, fyv_y_120);
                    tempMatrix_820 = tempMatrix_820.multiply(detJ);
                    b2_matrix = Matrix.plus(tempMatrix_820, b2_matrix);

                    tempMatrix_820 = Matrix.cross(fyp_matrix_81, fyv_z_120);
                    tempMatrix_820 = tempMatrix_820.multiply(detJ);
                    b3_matrix = Matrix.plus(tempMatrix_820, b3_matrix);
//*************************************组合C单元子块***********************
                    tempMatrix_820 = Matrix.cross(fyv_x_201, fyp_matrix_18);
                    tempMatrix_820 = tempMatrix_820.multiply(detJ);
                    c1_matrix = Matrix.plus(tempMatrix_820, c1_matrix);

                    tempMatrix_820 = Matrix.cross(fyv_y_201, fyp_matrix_18);
                    tempMatrix_820 = tempMatrix_820.multiply(detJ);
                    c2_matrix = Matrix.plus(tempMatrix_820, c2_matrix);

                    tempMatrix_820 = Matrix.cross(fyv_z_201, fyp_matrix_18);
                    tempMatrix_820 = tempMatrix_820.multiply(detJ);
                    c3_matrix = Matrix.plus(tempMatrix_820, c3_matrix);
//*************************************组合D单元子块***********************

                    tempMatrix1 = Matrix.cross(fyv_x_201, fyv_x_120);
                    tempMatrix2 = Matrix.cross(fyv_y_201, fyv_y_120);
                    tempMatrix3 = Matrix.cross(fyv_z_201, fyv_z_120);
                    tempMatrix = tempMatrix1;//.multiply(2);
                    tempMatrix = Matrix.plus(tempMatrix, tempMatrix2);
                    tempMatrix = Matrix.plus(tempMatrix, tempMatrix3);
                    tempMatrix = tempMatrix.multiply(detJ * miu);
                    d11_matrix = Matrix.plus(tempMatrix, d11_matrix);

                    tempMatrix = tempMatrix2.multiply(2);
                    tempMatrix = Matrix.plus(tempMatrix1, tempMatrix);
                    tempMatrix = Matrix.plus(tempMatrix3, tempMatrix);
                    tempMatrix = tempMatrix.multiply(detJ * miu);
                    d22_matrix = Matrix.plus(tempMatrix, d22_matrix);

                    tempMatrix = tempMatrix3.multiply(2);
                    tempMatrix = Matrix.plus(tempMatrix1, tempMatrix);
                    tempMatrix = Matrix.plus(tempMatrix, tempMatrix2);
                    tempMatrix = tempMatrix.multiply(detJ * miu);
                    d33_matrix = Matrix.plus(tempMatrix, d33_matrix);

                    tempMatrix = Matrix.cross(fyv_x_201, fyv_y_120);//此处应该可以调换
                    //为Matrix.cross(fy_yt,fy_x);
                    tempMatrix = tempMatrix.multiply(detJ * miu);
                    d12_matrix = Matrix.plus(tempMatrix, d12_matrix);

                    tempMatrix = Matrix.cross(fyv_x_201, fyv_z_120);
                    tempMatrix = tempMatrix.multiply(detJ * miu);
                    d13_matrix = Matrix.plus(tempMatrix, d13_matrix);

                    tempMatrix = Matrix.cross(fyv_y_201, fyv_x_120);
                    tempMatrix = tempMatrix.multiply(detJ * miu);
                    d21_matrix = Matrix.plus(tempMatrix, d21_matrix);

                    tempMatrix = Matrix.cross(fyv_y_201, fyv_z_120);
                    tempMatrix = tempMatrix.multiply(detJ * miu);
                    d23_matrix = Matrix.plus(tempMatrix, d23_matrix);

                    tempMatrix = Matrix.cross(fyv_z_201, fyv_x_120);
                    tempMatrix = tempMatrix.multiply(detJ * miu);
                    d31_matrix = Matrix.plus(tempMatrix, d31_matrix);

                    tempMatrix = Matrix.cross(fyv_z_201, fyv_y_120);
                    tempMatrix = tempMatrix.multiply(detJ * miu);
                    d32_matrix = Matrix.plus(tempMatrix, d32_matrix);

                    tempMatrix = Matrix.cross(fyv_201, fyv_120);
                    tempMatrix1 = Matrix.cross(tempMatrix, new Matrix(false, u_i_e));
                    tempMatrix1 = Matrix.cross(tempMatrix1, fyv_x_120);
                    tempMatrix1 = tempMatrix1.multiply(rou * detJ);
                    q1_matrix = Matrix.plus(q1_matrix, tempMatrix1);

                    tempMatrix1 = Matrix.cross(tempMatrix, new Matrix(false, v_i_e));
                    tempMatrix1 = Matrix.cross(tempMatrix1, fyv_y_120);
                    tempMatrix1 = tempMatrix1.multiply(rou * detJ);
                    q2_matrix = Matrix.plus(q2_matrix, tempMatrix1);

                    tempMatrix1 = Matrix.cross(tempMatrix, new Matrix(false, w_i_e));
                    tempMatrix1 = Matrix.cross(tempMatrix1, fyv_z_120);
                    tempMatrix1 = tempMatrix1.multiply(rou * detJ);
                    q3_matrix = Matrix.plus(q3_matrix, tempMatrix1);

                }
            }
        }

        q1_matrix = Matrix.plus(q1_matrix, q2_matrix);
        q1_matrix = Matrix.plus(q1_matrix, q3_matrix);
//        d11_matrix = Matrix.plus(d11_matrix, q1_matrix);
//        d22_matrix = Matrix.plus(d22_matrix, q1_matrix);
//        d33_matrix = Matrix.plus(d33_matrix, q1_matrix);
        matrix[0][0] = d11_matrix;
        matrix[0][1] = d12_matrix;
        matrix[0][2] = d13_matrix;
        matrix[0][3] = c1_matrix;
        matrix[1][0] = d21_matrix;
        matrix[1][1] = d22_matrix;
        matrix[1][2] = d23_matrix;
        matrix[1][3] = c2_matrix;
        matrix[2][0] = d31_matrix;
        matrix[2][1] = d32_matrix;
        matrix[2][2] = d33_matrix;
        matrix[2][3] = c3_matrix;
        matrix[3][0] = b1_matrix;
        matrix[3][1] = b2_matrix;
        matrix[3][2] = b3_matrix;
        matrix[3][3] = new Matrix(8, 1);
        return matrix;

    }

    private Matrix[] getF(BoundaryPEle bp) {
        Matrix[] matrixSet = new Matrix[3];
        Matrix f1_matrix = new Matrix(20, 1);
        Matrix f2_matrix = new Matrix(20, 1);
        Matrix f3_matrix = new Matrix(20, 1);

        //取得当前单元的八个节点的信息
        Point3D[] point8 = new Point3D[8];
        Point3D[] point20 = new Point3D[20];
        for (int i = 0; i <= 7; i++) {
            point8[i] = node[eleQuad_8[bp.getElementId() - 1]
                    .getNodeList()[i] - 1];
            point8[i].printPoint3D();
        }

        for (int i = 0; i <= 19; i++) {
            point20[i] = node[eleQuad_20[bp.getElementId() - 1]
                    .getNodeList()[i] - 1];
        }
        //取得高斯积分点
        Point2D[] gaussPointList = new GaussPoint(gaussPointNum)
                .getPointList();

        int p_surface = bp.getBoundarySurface();

        //createSurfaceFP(bp);
        if (p_surface == 0) {
            for (int i = 0; i <= gaussPointList.length - 1; i++) {
                for (int j = 0; j <= gaussPointList.length - 1; j++) {
                    double ita = gaussPointList[i].getX();
                    double zeta = gaussPointList[j].getX();
                    Fai_3D_8node fai_3D_8node = new Fai_3D_8node(point8,
                            -1, ita, zeta);
                    Fai_3D_20node fai_3D_20node = new Fai_3D_20node(point20,
                            -1, ita, zeta);
                    Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);//行向量
                    Matrix fyv_201 = fai_3D_20node.getN_iMatrix(false);
                    double[] parr = new double[8];
                    parr[0] = bp.getP1();
                    parr[1] = 0;
                    parr[2] = 0;
                    parr[3] = bp.getP2();
                    parr[4] = bp.getP4();
                    parr[5] = 0;
                    parr[6] = 0;
                    parr[7] = bp.getP3();
                    Matrix p_matrix_81 = new Matrix(false, parr);
                    double tempDouble = Matrix.cross(fyp_18, p_matrix_81)
                            .toArray()[0][0];
                    double area = getTetraArea(point8[0],
                            point8[3], point8[7], point8[4]);
                    double tempDouble1 = tempDouble * bp.getCos_theta_x()
                            * gaussPointList[i].getY()
                            * gaussPointList[j].getY();
                    tempDouble1 = tempDouble1 * area / 4;
                    Matrix tempMatrix_201;
                    tempMatrix_201 = fyv_201.multiply(tempDouble1);
                    f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                    double tempDouble2 = tempDouble * bp.getCos_theta_y()
                            * gaussPointList[i].getY()
                            * gaussPointList[j].getY();
                    tempDouble2 = tempDouble2 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble2);
                    f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                    double tempDouble3 = tempDouble * bp.getCos_theta_z()
                            * gaussPointList[i].getY()
                            * gaussPointList[j].getY();
                    tempDouble3 = tempDouble3 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble3);
                    f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                }
            }
        } else if (p_surface == 1) {
            for (int i = 0; i <= gaussPointList.length - 1; i++) {
                for (int j = 0; j <= gaussPointList.length - 1; j++) {
                    double ita = gaussPointList[i].getX();
                    double zeta = gaussPointList[j].getX();
                    Fai_3D_8node fai_3D_8node = new Fai_3D_8node(point8,
                            1, ita, zeta);
                    Fai_3D_20node fai_3D_20node = new Fai_3D_20node(point20,
                            1, ita, zeta);
                    Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);//行向量
                    Matrix fyv_201 = fai_3D_20node.getN_iMatrix(false);

                    double[] parr = new double[8];
                    parr[0] = 0;
                    parr[1] = bp.getP1();
                    parr[2] = bp.getP2();
                    parr[3] = 0;
                    parr[4] = 0;
                    parr[5] = bp.getP4();
                    parr[6] = bp.getP3();
                    parr[7] = 0;

                    Matrix p_matrix_81 = new Matrix(false, parr);
                    double tempDouble = Matrix.cross(fyp_18, p_matrix_81)
                            .toArray()[0][0];
                    double area = getTetraArea(point8[1],
                            point8[2], point8[6], point8[5]);
                    double tempDouble1 = tempDouble * bp.getCos_theta_x()
                            * gaussPointList[i].getY()
                            * gaussPointList[j].getY();
                    tempDouble1 = tempDouble1 * area / 4;
                    Matrix tempMatrix_201;
                    tempMatrix_201 = fyv_201.multiply(tempDouble1);
                    f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                    double tempDouble2 = tempDouble * bp.getCos_theta_y()
                            * gaussPointList[i].getY()
                            * gaussPointList[j].getY();
                    tempDouble2 = tempDouble2 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble2);
                    f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                    double tempDouble3 = tempDouble * bp.getCos_theta_z()
                            * gaussPointList[i].getY()
                            * gaussPointList[j].getY();
                    tempDouble3 = tempDouble3 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble3);
                    f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                }
            }
        } else if (p_surface == 2) {
            for (int i = 0; i <= gaussPointList.length - 1; i++) {
                for (int k = 0; k <= gaussPointList.length - 1; k++) {
                    double kesi = gaussPointList[i].getX();
                    double zeta = gaussPointList[k].getX();
                    Fai_3D_8node fai_3D_8node = new Fai_3D_8node(point8,
                            kesi, -1, zeta);
                    Fai_3D_20node fai_3D_20node = new Fai_3D_20node(point20,
                            kesi, -1, zeta);
                    Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);//行向量
                    Matrix fyv_201 = fai_3D_20node.getN_iMatrix(false);

                    double[] parr = new double[8];
                    parr[0] = bp.getP1();
                    parr[1] = bp.getP2();
                    parr[2] = 0;
                    parr[3] = 0;
                    parr[4] = bp.getP4();
                    parr[5] = bp.getP3();
                    parr[6] = 0;
                    parr[7] = 0;

                    Matrix p_matrix_81 = new Matrix(false, parr);
                    double tempDouble = Matrix.cross(fyp_18, p_matrix_81)
                            .toArray()[0][0];
                    double area = getTetraArea(point8[0],
                            point8[1], point8[5], point8[4]);
                    double tempDouble1 = tempDouble * bp.getCos_theta_x()
                            * gaussPointList[i].getY()
                            * gaussPointList[k].getY();
                    tempDouble1 = tempDouble1 * area / 4;
                    Matrix tempMatrix_201;
                    tempMatrix_201 = fyv_201.multiply(tempDouble1);
                    f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                    double tempDouble2 = tempDouble * bp.getCos_theta_y()
                            * gaussPointList[i].getY()
                            * gaussPointList[k].getY();
                    tempDouble2 = tempDouble2 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble2);
                    f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                    double tempDouble3 = tempDouble * bp.getCos_theta_z()
                            * gaussPointList[i].getY()
                            * gaussPointList[k].getY();
                    tempDouble3 = tempDouble3 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble3);
                    f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                }
            }
        } else if (p_surface == 3) {
            for (int i = 0; i <= gaussPointList.length - 1; i++) {
                for (int k = 0; k <= gaussPointList.length - 1; k++) {
                    double kesi = gaussPointList[i].getX();
                    double zeta = gaussPointList[k].getX();
                    Fai_3D_8node fai_3D_8node = new Fai_3D_8node(point8,
                            kesi, 1, zeta);
                    Fai_3D_20node fai_3D_20node = new Fai_3D_20node(point20,
                            kesi, 1, zeta);
                    Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);//行向量
                    Matrix fyv_201 = fai_3D_20node.getN_iMatrix(false);

                    double[] parr = new double[8];
                    parr[0] = 0;
                    parr[1] = 0;
                    parr[2] = bp.getP2();
                    parr[3] = bp.getP1();
                    parr[4] = 0;
                    parr[5] = 0;
                    parr[6] = bp.getP3();
                    parr[7] = bp.getP4();

                    Matrix p_matrix_81 = new Matrix(false, parr);
                    double tempDouble = Matrix.cross(fyp_18, p_matrix_81)
                            .toArray()[0][0];
                    double area = getTetraArea(point8[2],
                            point8[3], point8[7], point8[6]);
                    double tempDouble1 = tempDouble * bp.getCos_theta_x()
                            * gaussPointList[i].getY()
                            * gaussPointList[k].getY();
                    tempDouble1 = tempDouble1 * area / 4;
                    Matrix tempMatrix_201;
                    tempMatrix_201 = fyv_201.multiply(tempDouble1);
                    f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                    double tempDouble2 = tempDouble * bp.getCos_theta_y()
                            * gaussPointList[i].getY()
                            * gaussPointList[k].getY();
                    tempDouble2 = tempDouble2 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble2);
                    f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                    double tempDouble3 = tempDouble * bp.getCos_theta_z()
                            * gaussPointList[i].getY()
                            * gaussPointList[k].getY();
                    tempDouble3 = tempDouble3 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble3);
                    f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                }
            }
        } else if (p_surface == 4) {
            for (int j = 0; j <= gaussPointList.length - 1; j++) {
                for (int k = 0; k <= gaussPointList.length - 1; k++) {
                    double kesi = gaussPointList[j].getX();
                    double ita = gaussPointList[k].getX();
                    Fai_3D_8node fai_3D_8node = new Fai_3D_8node(point8,
                            kesi, ita, -1);
                    Fai_3D_20node fai_3D_20node = new Fai_3D_20node(point20,
                            kesi, ita, -1);
                    Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);//行向量
                    Matrix fyv_201 = fai_3D_20node.getN_iMatrix(false);

                    double[] parr = new double[8];
                    parr[0] = bp.getP1();
                    parr[1] = bp.getP2();
                    parr[2] = bp.getP3();
                    parr[3] = bp.getP4();
                    parr[4] = 0;
                    parr[5] = 0;
                    parr[6] = 0;
                    parr[7] = 0;

                    Matrix p_matrix_81 = new Matrix(false, parr);
                    double tempDouble = Matrix.cross(fyp_18, p_matrix_81)
                            .toArray()[0][0];
                    double area = getTetraArea(point8[0],
                            point8[1], point8[2], point8[3]);

                    double tempDouble1 = tempDouble * bp.getCos_theta_x()
                            * gaussPointList[j].getY()
                            * gaussPointList[k].getY();
                    tempDouble1 = tempDouble1 * area / 4;
                    Matrix tempMatrix_201;
                    tempMatrix_201 = fyv_201.multiply(tempDouble1);
                    f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                    double tempDouble2 = tempDouble * bp.getCos_theta_y()
                            * gaussPointList[j].getY()
                            * gaussPointList[k].getY();
                    tempDouble2 = tempDouble2 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble2);
                    f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                    double tempDouble3 = tempDouble * bp.getCos_theta_z()
                            * gaussPointList[j].getY()
                            * gaussPointList[k].getY();
                    tempDouble3 = tempDouble3 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble3);
                    f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                }
            }
        } else if (p_surface == 5) {
            for (int j = 0; j <= gaussPointList.length - 1; j++) {
                for (int k = 0; k <= gaussPointList.length - 1; k++) {
                    double kesi = gaussPointList[j].getX();
                    double ita = gaussPointList[k].getX();
                    Fai_3D_8node fai_3D_8node = new Fai_3D_8node(point8,
                            kesi, ita, 1);
                    Fai_3D_20node fai_3D_20node = new Fai_3D_20node(point20,
                            kesi, ita, 1);
                    Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);//行向量
                    Matrix fyv_201 = fai_3D_20node.getN_iMatrix(false);

                    double[] parr = new double[8];

                    parr[0] = 0;
                    parr[1] = 0;
                    parr[2] = 0;
                    parr[3] = 0;
                    parr[4] = bp.getP1();
                    parr[5] = bp.getP2();
                    parr[6] = bp.getP3();
                    parr[7] = bp.getP4();

                    Matrix p_matrix_81 = new Matrix(false, parr);
                    double tempDouble = Matrix.cross(fyp_18, p_matrix_81)
                            .toArray()[0][0];
                    double area = getTetraArea(point8[4],
                            point8[5], point8[6], point8[7]);
                    double tempDouble1 = tempDouble * bp.getCos_theta_x()
                            * gaussPointList[j].getY()
                            * gaussPointList[k].getY();
                    tempDouble1 = tempDouble1 * area / 4;
                    Matrix tempMatrix_201;
                    tempMatrix_201 = fyv_201.multiply(tempDouble1);
                    f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                    double tempDouble2 = tempDouble * bp.getCos_theta_y()
                            * gaussPointList[j].getY()
                            * gaussPointList[k].getY();
                    tempDouble2 = tempDouble2 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble2);
                    f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                    double tempDouble3 = tempDouble * bp.getCos_theta_z()
                            * gaussPointList[j].getY()
                            * gaussPointList[k].getY();
                    tempDouble3 = tempDouble3 * area / 4;
                    tempMatrix_201 = fyv_201.multiply(tempDouble3);
                    f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "边界编号无效！Example3_1_1:700");
        }
        matrixSet[0] = f1_matrix;
        matrixSet[1] = f2_matrix;
        matrixSet[2] = f3_matrix;

        return matrixSet;
    }

    public void printNodeIdSet() {
        int n = nodeIdSet.length;
        for (int i = 0; i <= n - 1; i++) {
            System.out.println("节点集" + (i + 1) + "的节点编号如下：");
            int m = nodeIdSet[i].length;
            int indexOfRowData = 0;
            for (int j = 0; j <= m - 1; j++) {
                if (indexOfRowData <= 6) {
                    System.out.print(nodeIdSet[i][j] + "     ");
                    indexOfRowData++;
                } else {
                    System.out.println(nodeIdSet[i][j]);
                    indexOfRowData = 0;
                }
            }
            System.out.println("");
            System.out.println("=============================================="
                    + "======================================================="
                    + "======");
        }

    }

    private void addBoundaryCondition(double[][] matrix, String command) {
        if (command.equals("对角线归一代入法")) {
            for (int i = 0; i <= bounVNode.length - 1; i++) {
                //有多少个速度边界条件就循环多少次
                int nodeId = bounVNode[i].getNodeId();
                double u = bounVNode[i].getU();
                double v = bounVNode[i].getV();
                double w = bounVNode[i].getW();
                int ii = nodeId - 1;
                for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
                    //右端向量=右端向量-稀疏矩阵第ii列乘以边界节点的变量值
                    matrix[k][3 * nodeVNum + nodePNum] = matrix[k][3
                            * nodeVNum + nodePNum] - u * matrix[k][ii];
                }
                for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
                    //将已知u_i=u_i*对应的系数矩阵的行全部置零
                    matrix[ii][k] = 0;
                }
                for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
                    //将已知u_i=u_i*对应的列全部置零
                    matrix[k][ii] = 0;
                }
                matrix[ii][ii] = 1;
                matrix[ii][3 * nodeVNum + nodePNum] = u;

                ii = nodeVNum + nodeId - 1;
                for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
                    //右端向量=右端向量-稀疏矩阵第ii列乘以边界节点的变量值
                    matrix[k][3 * nodeVNum + nodePNum] = matrix[k][3
                            * nodeVNum + nodePNum] - v * matrix[k][ii];
                }
                for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
                    //将已知u_i=u_i*对应的系数矩阵的行全部置零
                    matrix[ii][k] = 0;
                }
                for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
                    //将已知u_i=u_i*对应的列全部置零
                    matrix[k][ii] = 0;
                }
                matrix[ii][ii] = 1;
                matrix[ii][3 * nodeVNum + nodePNum] = v;

                ii = (2 * nodeVNum) + nodeId - 1;
                for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
                    //右端向量=右端向量-稀疏矩阵第ii列乘以边界节点的变量值
                    matrix[k][3 * nodeVNum + nodePNum] = matrix[k][3
                            * nodeVNum + nodePNum] - w * matrix[k][ii];
                }
                for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
                    //将已知u_i=u_i*对应的系数矩阵的行全部置零
                    matrix[ii][k] = 0;
                }
                for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
                    //将已知u_i=u_i*对应的列全部置零
                    matrix[k][ii] = 0;
                }
                matrix[ii][ii] = 1;
                matrix[ii][3 * nodeVNum + nodePNum] = w;
            }
        } else if (command.equals("乘大数代入法")) {

        }

    }

    private double getTetraArea(Point3D point1, Point3D point2,
            Point3D point3, Point3D point4) {
        double area;
        double x1 = point1.getX();
        double y1 = point1.getY();
        double z1 = point1.getZ();
        double x2 = point2.getX();
        double y2 = point2.getY();
        double z2 = point2.getZ();
        double x3 = point3.getX();
        double y3 = point3.getY();
        double z3 = point3.getZ();
        double x4 = point4.getX();
        double y4 = point4.getY();
        double z4 = point4.getZ();

        double l_12 = sqrt(pow((x1 - x2), 2) + pow((y1 - y2), 2) + pow((z1 - z2), 2));
        double l_23 = sqrt(pow((x2 - x3), 2) + pow((y2 - y3), 2) + pow((z2 - z3), 2));
        double l_34 = sqrt(pow((x3 - x4), 2) + pow((y3 - y4), 2) + pow((z3 - z4), 2));
        double l_41 = sqrt(pow((x4 - x1), 2) + pow((y4 - y1), 2) + pow((z4 - z1), 2));
        double l_13 = sqrt(pow((x1 - x3), 2) + pow((y1 - y3), 2) + pow((z1 - z3), 2));
        double p = (l_12 + l_23 + l_13) / 2;
        area = sqrt(p * (p - l_12) * (p - l_23) * (p - l_13));
        p = (l_34 + l_41 + l_13) / 2;
        area = area + sqrt(p * (p - l_34) * (p - l_41) * (p - l_13));
        return area;
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
        double cosx1 = vectorN1_x / sqrt(pow(vectorN1_x, 2) + pow(vectorN1_y, 2) + pow(vectorN1_z, 2));
        double cosy1 = vectorN1_y / sqrt(pow(vectorN1_x, 2) + pow(vectorN1_y, 2) + pow(vectorN1_z, 2));
        double cosz1 = vectorN1_z / sqrt(pow(vectorN1_x, 2) + pow(vectorN1_y, 2) + pow(vectorN1_z, 2));

        //向量23×向量34,是234点确定的面的一个法向量，不是单位向量
        double vectorN2_x = (vector23_y * vector34_z) - (vector34_y * vector23_z);
        double vectorN2_y = (vector34_x * vector23_z) - (vector23_x * vector34_z);
        double vectorN2_z = (vector23_x * vector34_y) - (vector34_x * vector23_y);
        double cosx2 = vectorN2_x / sqrt(pow(vectorN2_x, 2) + pow(vectorN2_y, 2) + pow(vectorN2_z, 2));
        double cosy2 = vectorN2_y / sqrt(pow(vectorN2_x, 2) + pow(vectorN2_y, 2) + pow(vectorN2_z, 2));
        double cosz2 = vectorN2_z / sqrt(pow(vectorN2_x, 2) + pow(vectorN2_y, 2) + pow(vectorN2_z, 2));

        //向量34×向量41,是341点确定的面的一个法向量，不是单位向量
        double vectorN3_x = (vector34_y * vector41_z) - (vector41_y * vector34_z);
        double vectorN3_y = (vector41_x * vector34_z) - (vector34_x * vector41_z);
        double vectorN3_z = (vector34_x * vector41_y) - (vector41_x * vector34_y);
        double cosx3 = vectorN3_x / sqrt(pow(vectorN3_x, 2) + pow(vectorN3_y, 2) + pow(vectorN3_z, 2));
        double cosy3 = vectorN3_y / sqrt(pow(vectorN3_x, 2) + pow(vectorN3_y, 2) + pow(vectorN3_z, 2));
        double cosz3 = vectorN3_z / sqrt(pow(vectorN3_x, 2) + pow(vectorN3_y, 2) + pow(vectorN3_z, 2));

        //向量41×向量12,是412点确定的面的一个法向量，不是单位向量
        double vectorN4_x = (vector41_y * vector12_z) - (vector12_y * vector41_z);
        double vectorN4_y = (vector12_x * vector41_z) - (vector41_x * vector12_z);
        double vectorN4_z = (vector41_x * vector12_y) - (vector12_x * vector41_y);
        double cosx4 = vectorN4_x / sqrt(pow(vectorN4_x, 2) + pow(vectorN4_y, 2) + pow(vectorN4_z, 2));
        double cosy4 = vectorN4_y / sqrt(pow(vectorN4_x, 2) + pow(vectorN4_y, 2) + pow(vectorN4_z, 2));
        double cosz4 = vectorN4_z / sqrt(pow(vectorN4_x, 2) + pow(vectorN4_y, 2) + pow(vectorN4_z, 2));

        cosValue[0] = (cosx1 + cosx2 + cosx3 + cosx4) / 4.0;
        cosValue[1] = (cosy1 + cosy2 + cosy3 + cosy4) / 4.0;
        cosValue[2] = (cosz1 + cosz2 + cosz3 + cosz4) / 4.0;
        return cosValue;
    }

    private void printInitInfo() {
        System.out.println("以下是节点信息：<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Point3D.printTitle();
        for (int i = 0; i <= nodeVNum - 1; i++) {
            node[i].printPoint3D();
        }
        System.out.println("节点信息输出结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("");
        System.out.println("以下是单元信息：<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        EleQuad_8.printTitle();
        for (int i = 0; i <= elementNum - 1; i++) {
            eleQuad_8[i].printElement();
        }
        System.out.println("速度单元信息输出结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("");

    }

}
