/*
 * 本例参考《计算流体力学有限元方法及其编程详解·毕超》第三章
 * 考虑三维模型，形函数为八节点一次六面体单元
 */
package simple;

import MATERIAL.Material;
import common.Boundary;
import common.CalculateInfoBag;
import common.Matrix;
import common.GaussPoint;
import element.BoundaryPEle;
import element.EleQuad_20;
import element.EleQuad_8;
import element.Fai_3D_20node;
import element.Fai_3D_8node;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import point.Point2D;
import point.Point3D;

/**
 *
 * @author yk
 */
public class Example3_1_2 extends JFrame {

    private EleQuad_8[] eleQuad_8;
    private EleQuad_20[] eleQuad_20;
    private int elementNum;
    private Point3D[] node;
    private double miu = 1000;
    private double rou = 1000;
    private int nodeVNum;
    private int nodePNum;
    private int[][] nodeIdSet;
    private Boundary[] boundary;//边界条件名称，节点集，变量名称，变量值
    private BoundaryPEle[] bounPreEle;
    private int gaussPointNum;
    private String command;
    private CalculateInfoBag dataBag = null;
    private double[] u0;
    private double[] v0;
    private double[] w0;
    private double[] p0;

    public Example3_1_2(CalculateInfoBag initData) {
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
        readData();
        calculateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                calculate();
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
        node = dataBag.getNodeV();
        nodeIdSet = dataBag.getNodeIdSet();
        nodeVNum = dataBag.getNodeVNum();
        nodePNum = dataBag.getNodePNum();
        elementNum = dataBag.getElementNum();
        miu = 1000;
        rou = 1000;
        boundary = dataBag.getBoundary();
        gaussPointNum = dataBag.getGaussPointNum();
        command = dataBag.getCommand();
        bounPreEle = dataBag.getPressBoundary();
        //初始化u,v,w,p
        u0 = new double[nodeVNum];
        v0 = new double[nodeVNum];
        w0 = new double[nodeVNum];
        p0 = new double[nodePNum];
        for (int i = 0; i <= nodeVNum - 1; i++) {
            u0[i] = 0;
            v0[i] = 0;
            w0[i] = 0;
        }
        for (int i = 0; i <= nodePNum - 1; i++) {
            p0[i] = 0;
        }
        for (int i = 0; i <= boundary.length - 1; i++) {
            if (boundary[i].getVarName().equals("压强")) {
                int[] nodeSet = nodeIdSet[boundary[i].getNodeSetId() - 1];
                for (int j = 0; j <= nodeSet.length - 1; j++) {
                    p0[j] = boundary[i].getValue();
                }
            } else if (boundary[i].getVarName().equals("速度")) {
                int[] nodeSet = nodeIdSet[boundary[i].getNodeSetId() - 1];
                for (int j = 0; j <= nodeSet.length - 1; j++) {
                    u0[j] = boundary[i].getValue();
                }
//            } else if (boundary[i].getVarName() == 2) {
//                int[] nodeSet = nodeIdSet[boundary[i].getNodeSetId() - 1];
//                for (int j = 0; j <= nodeSet.length - 1; j++) {
//                    v0[j] = boundary[i].getValue();
//                }
//            } else if (boundary[i].getVarName() == 3) {
//                int[] nodeSet = nodeIdSet[boundary[i].getNodeSetId() - 1];
//                for (int j = 0; j <= nodeSet.length - 1; j++) {
//                    w0[j] = boundary[i].getValue();
//                }
            }
        }

    }

    private void printInfo() {
        printInitInfo();
        printNodeIdSet();
    }

    private void calculate() {
        //<editor-fold defaultstate="collapsed" desc="整体矩阵各子块B0等声明">
        Matrix B0 = new Matrix(nodePNum, nodeVNum);
        Matrix B1 = new Matrix(nodePNum, nodeVNum);
        Matrix B2 = new Matrix(nodePNum, nodeVNum);
        Matrix D00 = new Matrix(nodeVNum, nodeVNum);
        Matrix D01 = new Matrix(nodeVNum, nodeVNum);
        Matrix D02 = new Matrix(nodeVNum, nodeVNum);
        Matrix D10 = new Matrix(nodeVNum, nodeVNum);
        Matrix D11 = new Matrix(nodeVNum, nodeVNum);
        Matrix D12 = new Matrix(nodeVNum, nodeVNum);
        Matrix D20 = new Matrix(nodeVNum, nodeVNum);
        Matrix D21 = new Matrix(nodeVNum, nodeVNum);
        Matrix D22 = new Matrix(nodeVNum, nodeVNum);
        Matrix C0 = new Matrix(nodeVNum, nodePNum);
        Matrix C1 = new Matrix(nodeVNum, nodePNum);
        Matrix C2 = new Matrix(nodeVNum, nodePNum);
        Matrix F0 = new Matrix(nodeVNum, 1);
        Matrix F1 = new Matrix(nodeVNum, 1);
        Matrix F2 = new Matrix(nodeVNum, 1);
        Matrix G0 = new Matrix(nodeVNum, 1);
        Matrix G1 = new Matrix(nodeVNum, 1);
        Matrix G2 = new Matrix(nodeVNum, 1);
        //</editor-fold>

        B0.writeToFile("B0.txt");
        //<editor-fold defaultstate="collapsed" desc="B0_arr[][]等数组声明及赋值">
        double[][] B0_arr = B0.toArray();//整体矩阵的数据
        double[][] B1_arr = B1.toArray();
        double[][] B2_arr = B2.toArray();
        double[][] D00_arr = D00.toArray();
        double[][] D01_arr = D01.toArray();
        double[][] D02_arr = D02.toArray();
        double[][] D10_arr = D10.toArray();
        double[][] D11_arr = D11.toArray();
        double[][] D12_arr = D12.toArray();
        double[][] D20_arr = D20.toArray();
        double[][] D21_arr = D21.toArray();
        double[][] D22_arr = D22.toArray();
        double[][] C0_arr = C0.toArray();
        double[][] C1_arr = C1.toArray();
        double[][] C2_arr = C2.toArray();
        double[][] F0_arr = F0.toArray();
        double[][] F1_arr = F1.toArray();
        double[][] F2_arr = F2.toArray();
        double[][] G0_arr = G0.toArray();
        double[][] G1_arr = G1.toArray();
        double[][] G2_arr = G2.toArray();
        //</editor-fold>

        Matrix[][] matrixSet;

        //<editor-fold defaultstate="collapsed" desc="b0_arr[][]等数组声明">
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
        double[][] g0_arr;
        double[][] g1_arr;
        double[][] g2_arr;
        //</editor-fold>

        //合成整体矩阵
        for (int i = 0; i <= elementNum - 1; i++) {
            System.out.println("当前计算‘单元编号/总单元数’为" + (i + 1)
                    + "/" + elementNum + "；");
            matrixSet = getElementMatrix(i);//单元系数矩阵组
            
            //<editor-fold defaultstate="collapsed" desc="b0_arr数组赋值">
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
            f0_arr = matrixSet[0][4].toArray();
            f1_arr = matrixSet[1][4].toArray();
            f2_arr = matrixSet[2][4].toArray();
            g0_arr = matrixSet[0][5].toArray();
            g1_arr = matrixSet[1][5].toArray();
            g2_arr = matrixSet[2][5].toArray();
            //</editor-fold>
            
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
            for (int m = 0; m <= 19; m++) {
                int mm = eleQuad_20[i].getNodeList()[m] - 1;
                G0_arr[mm][1] = G0_arr[mm][1] + g0_arr[m][1];
                G1_arr[mm][1] = G1_arr[mm][1] + g1_arr[m][1];
                G2_arr[mm][1] = G2_arr[mm][1] + g2_arr[m][1];
            }
            //当且仅当当前单元（编号：i+1）为压力边界单元时，进行对F子块求和。
            //因为当当前单元不是边界单元时，fn_arr全部为0，所以求和不影响结果
            for (int m = 0; m <= 19; m++) {
                int mm = eleQuad_20[i].getNodeList()[m] - 1;//mm是节点的数组索引号
                F0_arr[mm][0] = F0_arr[mm][0] + f0_arr[m][0];
                F1_arr[mm][0] = F1_arr[mm][0] + f1_arr[m][0];
                F2_arr[mm][0] = F2_arr[mm][0] + f2_arr[m][0];

            }

        }
//        System.out.println("子块D00$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//        D00 = new Matrix(D00_arr);
//        D00.printMatrix();
//        System.out.println("子块D01$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//        D01 = new Matrix(D01_arr);
//        D01.printMatrix();

        //<editor-fold defaultstate="collapsed" desc="组合整体矩阵">
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
                for (int j = 3 * nodeVNum; j < (3 * nodeVNum + nodePNum - 1); j++) {
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
        //</editor-fold>

        //下面，添加第一类边界条件，及边界节点的已知速度值
        System.out.println("开始添加边界条件<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"
                + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Matrix whole_matrix = new Matrix(wholeArr);
        whole_matrix.writeToFile("wholeMatrix.txt");

//        addBoundaryCondition(wholeArr, command);
        whole_matrix = new Matrix(wholeArr);
        whole_matrix.writeToFile("整体矩阵修正后.txt");
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
        }
        for (int i = nodeVNum; i <= 2 * nodeVNum - 1; i++) {
            System.out.println("v[" + (i - nodeVNum + 1) + "]=" + result[i]);
        }
        for (int i = 2 * nodeVNum; i <= 3 * nodeVNum - 1; i++) {
            System.out.println("w[" + (i - 2 * nodeVNum + 1) + "]=" + result[i]);
        }
        for (int i = 3 * nodeVNum; i <= 3 * nodeVNum + nodePNum - 1; i++) {
            System.out.println("p[" + (i - 3 * nodeVNum + 1) + "]=" + result[i]);
        }

    }

    private Matrix[][] getElementMatrix(int elementIndex) {
        Matrix[][] matrix = new Matrix[4][6];
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
        Matrix f1_matrix = new Matrix(20, 1);
        Matrix f2_matrix = new Matrix(20, 1);
        Matrix f3_matrix = new Matrix(20, 1);
        Matrix g1_matrix = new Matrix(20, 1);
        Matrix g2_matrix = new Matrix(20, 1);
        Matrix g3_matrix = new Matrix(20, 1);

        //取得当前单元的八个节点的信息
        Point3D[] point8 = new Point3D[8];
        Point3D[] point20 = new Point3D[20];
        System.out.println("该计算单元的8个节点信息为(节点编号，x坐标，y坐标,z"
                + "坐标)：-------------------------------------------------- ");
        for (int i = 0; i <= 7; i++) {
            point8[i] = node[eleQuad_8[elementIndex].getNodeList()[i] - 1];
            point8[i].printPoint3D();
        }

        for (int i = 0; i <= 19; i++) {
            point20[i] = node[eleQuad_20[elementIndex].getNodeList()[i] - 1];
        }

        System.out.println("================================================="
                + "=========================================================");

        //取得高斯积分点
        Point2D[] gaussPointList = new GaussPoint(gaussPointNum).getPointList();

        //循环叠加求矩阵B
        Matrix fyp_matrix_81;
        Matrix fyp_matrix_18;
        Matrix fyv_matrix_201;
        Matrix fyv_matrix_120;
        double[] fyp_kesi;
        double[] fyp_ita;
        double[] fyp_zeta;
        double[] fyv_kesi;
        double[] fyv_ita;
        double[] fyv_zeta;
        Matrix tempMatrix1;
        Matrix tempMatrix2;
        Matrix tempMatrix3;
        Matrix tempMatrix;
        Matrix jacobiMatrix_33;
        double[] tempArr1 = new double[8];
        double[] tempArr2 = new double[8];
        double[] tempArr3 = new double[8];

        double detJ;
        double kesi;
        double ita;
        double zeta;

        int bounPID = -1;//-1表示没有进行过边界单元判断，-2表示不是边界单元，其他大
        //于等于0的值表示是边界单元，且边界单元编号为bounPID
        for (int i = 0; i <= gaussPointNum - 1; i++) {
            for (int j = 0; j <= gaussPointNum - 1; j++) {
                for (int k = 0; k <= gaussPointNum - 1; k++) {
                    //<editor-fold defaultstate="collapsed" desc="计算插值函数相关数据">
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

                    fyv_matrix_201 = fai_3D_20node.getN_iMatrix(false);//20行1列，列向量
                    fyv_matrix_120 = fai_3D_20node.getN_iMatrix(true);//20列1行，行向量

                    fyv_kesi = fai_3D_20node.getFai_kesi();
                    fyv_ita = fai_3D_20node.getFai_ita();
                    fyv_zeta = fai_3D_20node.getFai_zeta();
                    //至此，速度和压力插值函数的形函数及相关导数都已获得

                    //下面，求取压力插值函数对x,y,z的导数
                    double[][] arr = new double[3][8];
                    for (int kin = 0; kin <= 7; kin++) {
                        arr[0][kin] = fyp_kesi[kin];
                        arr[1][kin] = fyp_ita[kin];
                        arr[2][kin] = fyp_zeta[kin];
                    }
                    Matrix tempMatrix_38;
                    tempMatrix_38 = new Matrix(arr);//三行八列
                    jacobiMatrix_33 = fai_3D_8node.getJacobiMatrix();
                    tempMatrix_38 = Matrix.cross(jacobiMatrix_33.inverseMatrix(),
                            tempMatrix_38);//三行八列,第一行fai_x,第二行fai_y,第三行fai_z
                    for (int kin = 0; kin <= 7; kin++) {
                        tempArr1[kin] = tempMatrix_38.toArray()[0][kin];
                        tempArr2[kin] = tempMatrix_38.toArray()[1][kin];
                        tempArr3[kin] = tempMatrix_38.toArray()[2][kin];
                    }
                    Matrix fyp_x_18, fyp_x_81;
                    Matrix fyp_y_18, fyp_y_81;
                    Matrix fyp_z_18, fyp_z_81;
                    fyp_x_18 = new Matrix(true, tempArr1);//行向量
                    fyp_y_18 = new Matrix(true, tempArr2);
                    fyp_z_18 = new Matrix(true, tempArr3);
                    fyp_x_81 = new Matrix(false, tempArr1);//列向量
                    fyp_y_81 = new Matrix(false, tempArr2);
                    fyp_z_81 = new Matrix(false, tempArr3);

                    //下面，求取速度插值函数对x,y,z的导数
                    double[][] arr320 = new double[3][20];
                    for (int kin = 0; kin <= 19; kin++) {
                        arr320[0][kin] = fyv_kesi[kin];
                        arr320[1][kin] = fyv_ita[kin];
                        arr320[2][kin] = fyv_zeta[kin];
                    }
                    Matrix tempMatrix_320;
                    tempMatrix_320 = new Matrix(arr320);
                    //jacobiMatrix_33 = fai_3D_20node.getJacobiMatrix();
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
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="组合B">
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
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="组合C">
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
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="组合D">
//*************************************组合D单元子块*****************************
                    tempMatrix1 = Matrix.cross(fyv_x_201, fyv_x_120);
                    tempMatrix2 = Matrix.cross(fyv_y_201, fyv_y_120);
                    tempMatrix3 = Matrix.cross(fyv_z_201, fyv_z_120);
                    tempMatrix = tempMatrix1.multiply(2);
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
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="组合G">
//**********************************组合G单元子块********************************
                    double fyv_xfy = Matrix.cross(fyv_x_120, fyv_matrix_201)
                            .toArray()[0][0];
                    fyv_xfy = fyv_xfy * detJ * rou;
                    Matrix tempmatrix_201 = fyv_matrix_201.multiply(fyv_xfy);
                    g1_matrix = Matrix.plus(g1_matrix, tempmatrix_201);

                    double fyv_yfy = Matrix.cross(fyv_y_120, fyv_matrix_201)
                            .toArray()[0][0];
                    fyv_yfy = fyv_yfy * detJ * rou;
                    tempmatrix_201 = fyv_matrix_201.multiply(fyv_yfy);
                    g2_matrix = Matrix.plus(g2_matrix, tempmatrix_201);

                    double fyv_zfy = Matrix.cross(fyv_z_120, fyv_matrix_201)
                            .toArray()[0][0];
                    fyv_zfy = fyv_zfy * detJ * rou;
                    tempmatrix_201 = fyv_matrix_201.multiply(fyv_zfy);
                    g3_matrix = Matrix.plus(g3_matrix, tempmatrix_201);
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="组合F子块">
//****************************************组合F子块******************************

                    //判断边界面编号
                    if (bounPID == -1) {

                        for (int ii = 0; ii <= bounPreEle.length - 1; ii++) {
                            if (elementIndex == bounPreEle[ii].getElementId()) {
                                //如果该单元是边界单元
                                bounPID = ii;
                                break;
                            }
                        }
                        if (bounPID == -1) {
                            bounPID = -2;
                        }
                    } else if (bounPID == -2) {
                        //表明不是压力边界单元，不执行操作进入下一循环
                    }
                    //此处不能用else if，因为bounPID可能在上一个if语句中修改，
                    //这时两个if语句可能都要执行。
                    if (bounPID != -1 && bounPID != -2) {
                        //表明是压力边界单元，需要处理F子块
                        BoundaryPEle boundaryPEle = bounPreEle[bounPID];

//************************************组合F1************************************
                        if ((boundaryPEle.getBoundarySurface() == 0)
                                && (k == 0)) {//只在k=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, ita, -1);
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = boundaryPEle.getP1();
                            parr[1] = boundaryPEle.getP2();
                            parr[2] = boundaryPEle.getP3();
                            parr[3] = boundaryPEle.getP4();
                            parr[4] = 0;
                            parr[5] = 0;
                            parr[6] = 0;
                            parr[7] = 0;
                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_x() * gaussPointList[i].getY()
                                    * gaussPointList[j].getY();

                            tempDouble = tempDouble * getTetraArea(point8[0],
                                    point8[1], point8[2], point8[3]);
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fyv_matrix_201;
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                        } else if ((boundaryPEle.getBoundarySurface() == 1)
                                && (k == 0)) {//只在k=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, ita, 1);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];

                            parr[0] = 0;//不同边界面需要修改
                            parr[1] = 0;//不同边界面需要修改
                            parr[2] = 0;//不同边界面需要修改
                            parr[3] = 0;//不同边界面需要修改
                            parr[4] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[5] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[6] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[7] = boundaryPEle.getP4();//不同边界面需要修改
                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_x() * gaussPointList[i].getY()
                                    * gaussPointList[j].getY();

                            tempDouble = tempDouble * getTetraArea(point8[4],
                                    point8[5], point8[6], point8[7]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fyv_matrix_201;
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                        } else if ((boundaryPEle.getBoundarySurface() == 2)
                                && (j == 0)) {//只在j=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, -1, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];

                            parr[0] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[1] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[2] = 0;//不同边界面需要修改
                            parr[3] = 0;//不同边界面需要修改
                            parr[4] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[5] = boundaryPEle.getP4();//不同边界面需要修改
                            parr[6] = 0;//不同边界面需要修改
                            parr[7] = 0;//不同边界面需要修改
                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_x() * gaussPointList[i].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[0],
                                    point8[1], point8[5], point8[4]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                        } else if ((boundaryPEle.getBoundarySurface() == 3)
                                && (j == 0)) {//只在j=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, 1, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = 0;//不同边界面需要修改
                            parr[1] = 0;//不同边界面需要修改
                            parr[2] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[3] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[4] = 0;//不同边界面需要修改
                            parr[5] = 0;//不同边界面需要修改
                            parr[6] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[7] = boundaryPEle.getP4();//不同边界面需要修改

                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_x() * gaussPointList[i].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[2],
                                    point8[3], point8[6], point8[7]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                        } else if ((boundaryPEle.getBoundarySurface() == 4)
                                && (i == 0)) {//只在i=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, -1, ita, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[1] = 0;//不同边界面需要修改
                            parr[2] = 0;//不同边界面需要修改
                            parr[3] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[4] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[5] = 0;//不同边界面需要修改
                            parr[6] = 0;//不同边界面需要修改
                            parr[7] = boundaryPEle.getP4();//不同边界面需要修改

                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_x() * gaussPointList[j].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[0],
                                    point8[3], point8[4], point8[7]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                        } else if ((boundaryPEle.getBoundarySurface() == 5)
                                && (i == 0)) {//只在i=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, 1, ita, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = 0;//不同边界面需要修改
                            parr[1] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[2] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[3] = 0;//不同边界面需要修改
                            parr[4] = //不同边界面需要修改
                                    parr[5] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[6] = boundaryPEle.getP4();//不同边界面需要修改
                            parr[7] = 0;//不同边界面需要修改

                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_x() * gaussPointList[j].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[1],
                                    point8[2], point8[5], point8[6]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f1_matrix = Matrix.plus(f1_matrix, tempMatrix_201);
                        }

//**************************************组合F2**********************************
                        if ((boundaryPEle.getBoundarySurface() == 0)
                                && (k == 0)) {//只在k=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, ita, -1);
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = boundaryPEle.getP1();
                            parr[1] = boundaryPEle.getP2();
                            parr[2] = boundaryPEle.getP3();
                            parr[3] = boundaryPEle.getP4();
                            parr[4] = 0;
                            parr[5] = 0;
                            parr[6] = 0;
                            parr[7] = 0;
                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_y() * gaussPointList[i].getY()
                                    * gaussPointList[j].getY();

                            tempDouble = tempDouble * getTetraArea(point8[0],
                                    point8[1], point8[2], point8[3]);
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                        } else if ((boundaryPEle.getBoundarySurface() == 1)
                                && (k == 0)) {//只在k=0时计算一次) {
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, ita, 1);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];

                            parr[0] = 0;//不同边界面需要修改
                            parr[1] = 0;//不同边界面需要修改
                            parr[2] = 0;//不同边界面需要修改
                            parr[3] = 0;//不同边界面需要修改
                            parr[4] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[5] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[6] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[7] = boundaryPEle.getP4();//不同边界面需要修改
                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_y() * gaussPointList[i].getY()
                                    * gaussPointList[j].getY();

                            tempDouble = tempDouble * getTetraArea(point8[4],
                                    point8[5], point8[6], point8[7]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                        } else if (boundaryPEle.getBoundarySurface() == 2
                                && (j == 0)) {//只在j=0时计算一次) {
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, -1, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];

                            parr[0] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[1] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[2] = 0;//不同边界面需要修改
                            parr[3] = 0;//不同边界面需要修改
                            parr[4] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[5] = boundaryPEle.getP4();//不同边界面需要修改
                            parr[6] = 0;//不同边界面需要修改
                            parr[7] = 0;//不同边界面需要修改
                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_y() * gaussPointList[i].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[0],
                                    point8[1], point8[4], point8[5]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                        } else if (boundaryPEle.getBoundarySurface() == 3
                                && (j == 0)) {//只在j=0时计算一次) {
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, 1, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = 0;//不同边界面需要修改
                            parr[1] = 0;//不同边界面需要修改
                            parr[2] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[3] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[4] = 0;//不同边界面需要修改
                            parr[5] = 0;//不同边界面需要修改
                            parr[6] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[7] = boundaryPEle.getP4();//不同边界面需要修改

                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_y() * gaussPointList[i].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[2],
                                    point8[3], point8[6], point8[7]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                        } else if (boundaryPEle.getBoundarySurface() == 4
                                && (i == 0)) {//只在i=0时计算一次) {
                            fai_3D_8node = new Fai_3D_8node(point8, -1, ita, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[1] = 0;//不同边界面需要修改
                            parr[2] = 0;//不同边界面需要修改
                            parr[3] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[4] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[5] = 0;//不同边界面需要修改
                            parr[6] = 0;//不同边界面需要修改
                            parr[7] = boundaryPEle.getP4();//不同边界面需要修改

                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_y() * gaussPointList[j].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[0],
                                    point8[3], point8[4], point8[7]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                        } else if (boundaryPEle.getBoundarySurface() == 5
                                && (i == 0)) {//只在i=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, 1, ita, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = 0;//不同边界面需要修改
                            parr[1] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[2] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[3] = 0;//不同边界面需要修改
                            parr[4] = //不同边界面需要修改
                                    parr[5] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[6] = boundaryPEle.getP4();//不同边界面需要修改
                            parr[7] = 0;//不同边界面需要修改

                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_y() * gaussPointList[j].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[1],
                                    point8[2], point8[5], point8[6]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f2_matrix = Matrix.plus(f2_matrix, tempMatrix_201);
                        }

//*****************************************组合F3*******************************
                        if (boundaryPEle.getBoundarySurface() == 0
                                && (k == 0)) {//只在k=0时计算一次) {
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, ita, -1);
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = boundaryPEle.getP1();
                            parr[1] = boundaryPEle.getP2();
                            parr[2] = boundaryPEle.getP3();
                            parr[3] = boundaryPEle.getP4();
                            parr[4] = 0;
                            parr[5] = 0;
                            parr[6] = 0;
                            parr[7] = 0;
                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_z() * gaussPointList[i].getY()
                                    * gaussPointList[j].getY();

                            tempDouble = tempDouble * getTetraArea(point8[0],
                                    point8[1], point8[2], point8[3]);
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                        } else if (boundaryPEle.getBoundarySurface() == 1
                                && (k == 0)) {//只在k=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, ita, 1);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];

                            parr[0] = 0;//不同边界面需要修改
                            parr[1] = 0;//不同边界面需要修改
                            parr[2] = 0;//不同边界面需要修改
                            parr[3] = 0;//不同边界面需要修改
                            parr[4] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[5] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[6] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[7] = boundaryPEle.getP4();//不同边界面需要修改
                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_z() * gaussPointList[i].getY()
                                    * gaussPointList[j].getY();

                            tempDouble = tempDouble * getTetraArea(point8[4],
                                    point8[5], point8[6], point8[7]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                        } else if (boundaryPEle.getBoundarySurface() == 2
                                && (j == 0)) {//只在j=0时计算一次) {
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, -1, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];

                            parr[0] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[1] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[2] = 0;//不同边界面需要修改
                            parr[3] = 0;//不同边界面需要修改
                            parr[4] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[5] = boundaryPEle.getP4();//不同边界面需要修改
                            parr[6] = 0;//不同边界面需要修改
                            parr[7] = 0;//不同边界面需要修改
                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_z() * gaussPointList[i].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[0],
                                    point8[1], point8[4], point8[5]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                        } else if (boundaryPEle.getBoundarySurface() == 3
                                && (j == 0)) {//只在j=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, kesi, 1, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = 0;//不同边界面需要修改
                            parr[1] = 0;//不同边界面需要修改
                            parr[2] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[3] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[4] = 0;//不同边界面需要修改
                            parr[5] = 0;//不同边界面需要修改
                            parr[6] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[7] = boundaryPEle.getP4();//不同边界面需要修改

                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_z() * gaussPointList[i].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[2],
                                    point8[3], point8[6], point8[7]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                        } else if (boundaryPEle.getBoundarySurface() == 4
                                && (i == 0)) {//只在i=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, -1, ita, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[1] = 0;//不同边界面需要修改
                            parr[2] = 0;//不同边界面需要修改
                            parr[3] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[4] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[5] = 0;//不同边界面需要修改
                            parr[6] = 0;//不同边界面需要修改
                            parr[7] = boundaryPEle.getP4();//不同边界面需要修改

                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_z() * gaussPointList[j].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[0],
                                    point8[3], point8[4], point8[7]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                        } else if (boundaryPEle.getBoundarySurface() == 5
                                && (i == 0)) {//只在i=0时计算一次
                            fai_3D_8node = new Fai_3D_8node(point8, 1, ita, zeta);//不同边界面需要修改
                            Matrix fyp_18 = fai_3D_8node.getN_iMatrix(true);
                            double[] parr = new double[8];
                            parr[0] = 0;//不同边界面需要修改
                            parr[1] = boundaryPEle.getP1();//不同边界面需要修改
                            parr[2] = boundaryPEle.getP2();//不同边界面需要修改
                            parr[3] = 0;//不同边界面需要修改
                            parr[4] = //不同边界面需要修改
                                    parr[5] = boundaryPEle.getP3();//不同边界面需要修改
                            parr[6] = boundaryPEle.getP4();//不同边界面需要修改
                            parr[7] = 0;//不同边界面需要修改

                            Matrix p_matrix_81 = new Matrix(false, parr);
                            double tempDouble = Matrix.cross(fyp_18, p_matrix_81).toArray()[0][0];
                            tempDouble = tempDouble * boundaryPEle.
                                    getCos_theta_z() * gaussPointList[j].getY()
                                    * gaussPointList[k].getY();//最好修改下

                            tempDouble = tempDouble * getTetraArea(point8[1],
                                    point8[2], point8[5], point8[6]);//不同边界面需要修改
                            Matrix tempMatrix_201;
                            tempMatrix_201 = fai_3D_20node.getN_iMatrix(false);
                            tempMatrix_201 = tempMatrix_201.multiply(tempDouble);
                            f3_matrix = Matrix.plus(f3_matrix, tempMatrix_201);
                        }
                    }
                    //</editor-fold>
                }
            }
        }
        matrix[0][0] = d11_matrix;
        matrix[0][1] = d12_matrix;
        matrix[0][2] = d13_matrix;
        matrix[0][3] = c1_matrix;
        matrix[0][4] = f1_matrix;
        matrix[1][0] = d21_matrix;
        matrix[1][1] = d22_matrix;
        matrix[1][2] = d23_matrix;
        matrix[1][3] = c2_matrix;
        matrix[1][4] = f2_matrix;
        matrix[2][0] = d31_matrix;
        matrix[2][1] = d32_matrix;
        matrix[2][2] = d33_matrix;
        matrix[2][3] = c3_matrix;
        matrix[2][4] = f3_matrix;
        matrix[3][0] = b1_matrix;
        matrix[3][1] = b2_matrix;
        matrix[3][2] = b3_matrix;
        matrix[3][3] = new Matrix(8, 8);
        matrix[3][4] = new Matrix(8, 1);
        matrix[0][5] = g1_matrix;
        matrix[1][5] = g2_matrix;
        matrix[2][5] = g3_matrix;
        return matrix;
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

//    public void addBoundaryCondition(double[][] matrix, String command) {
//
//        if (command.equals("对角线归一代入法")) {
//            int tab = -1;
//            for (int i = 0; i <= boundary.length - 1; i++) {
//                //有多少个边界条件就循环多少次
//                if (boundary[i].getVarName() != 0) {
//                    //如果不是压力边界条件，进入此if语句
//                    int[] nodeId = nodeIdSet[boundary[i].getNodeSetId()];
//                    //得到该边界条件所施加的节点的编号集合
//                    if (boundary[i].getVarName() == 1) {
//                        tab = 1;//边界条件是x方向速度u
//                    } else if (boundary[i].getVarName() == 2) {
//                        tab = 2;//边界条件是y方向速度v
//                    } else if (boundary[i].getVarName() == 3) {
//                        tab = 3;//边界条件是z方向速度w
//                    }
//                    for (int j = 0; j <= nodeId.length - 1; j++) {
//                        int ii = ((tab - 1) * nodeVNum) + nodeId[j] - 1;
//                        for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
//                            //右端向量=右端向量-稀疏矩阵第ii列乘以边界节点的变量值
//                            matrix[k][3 * nodeVNum + nodePNum] = matrix[k][3
//                                    * nodeVNum + nodePNum] - boundary[i]
//                                    .getValue() * matrix[k][ii];
//                        }
//                        for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
//                            //将已知u_i=u_i*对应的系数矩阵的行全部置零
//                            matrix[ii][k] = 0;
//                        }
//                        for (int k = 0; k <= 3 * nodeVNum + nodePNum - 1; k++) {
//                            //将已知u_i=u_i*对应的列全部置零
//                            matrix[k][ii] = 0;
//                        }
//                        matrix[ii][ii] = 1;
//                        matrix[ii][3 * nodeVNum + nodePNum] = boundary[i]
//                                .getValue();
//                    }
//
//                }
//            }
//        } else if (command.equals("乘大数代入法")) {
//
//        }
//
////        for (int i = 0; i <= nodeId.length - 1; i++) {
////            int II = nodeId[i] * (tab + 1);
////            for (int j = 0; j <= 4 * nodeNum - 1; j++) {
////                matrix[j][4 * nodeNum] = matrix[j][4 * nodeNum] - matrix[j][II]
////                        * boundaryValue;
////            }
////            for (int j = 0; j <= 4 * nodeNum - 1; j++) {
////                //将已知u_i=u_i*对应的行全部置零
////                matrix[II][j] = 0;
////            }
////            for (int j = 0; j <= 4 * nodeNum - 1; j++) {
////                //将已知u_i=u_i*对应的列全部置零
////                matrix[j][II] = 0;
////            }
////            matrix[II][II] = 1;
////            matrix[II][4 * nodeNum] = boundaryValue;
////        }
//    }

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
