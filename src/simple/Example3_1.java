/*
 * 参见《计算流体力学有限元方法及其编程详解·毕超》第三章
 */
package simple;

import common.Matrix;
import common.GaussPoint;
import element.EleQuad_4;
import element.Fai_2D_4node;
import element.Fai_2D_9node;
import element.EleQuad_9;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import point.Point2D;

/**
 *
 * @author yk
 */
public class Example3_1 {

    private EleQuad_9[] eleQuad_9;
    private EleQuad_4[] eleQuad_4;
    private int elementNum;
    private Point2D[] nodeV;
    private double miu = 1000;

    public Example3_1() {
        generateMesh();
        Integer x;

        eleQuad_9 = readJMV("jmv.txt");
        eleQuad_4 = readJMP("jmp.txt");
        nodeV = readJXYV("jxyv.txt");
        //readJXYP("jxyp.txt");
        readJBV("jbv.txt");//将速度边界条件加入到节点信息中
        readJBP("jbp.txt");//将压力信息加入到单元信息中
        printInitInfo();
        int nd = 25;
        int nz = 81;
        if (eleQuad_9.length == eleQuad_4.length) {
            elementNum = eleQuad_9.length;
        } else {
            System.out.println("Error: 51_Example3_1, 四节点和九节点单元个数不相等！");
        }
        Matrix B0 = new Matrix(nd, nz);
        Matrix B1 = new Matrix(nd, nz);
        Matrix D00 = new Matrix(nz, nz);
        Matrix D01 = new Matrix(nz, nz);
        Matrix D10 = new Matrix(nz, nz);
        Matrix D11 = new Matrix(nz, nz);
        Matrix C1 = new Matrix(nz, nd);
        Matrix C2 = new Matrix(nz, nd);
        Matrix F1 = new Matrix(nz, 1);
        Matrix F2 = new Matrix(nz, 1);
        double[][] B0_arr = B0.toArray();
        double[][] B1_arr = B1.toArray();
        double[][] D00_arr = D00.toArray();
        double[][] D01_arr = D01.toArray();
        double[][] D10_arr = D10.toArray();
        double[][] D11_arr = D11.toArray();
        double[][] C0_arr = C1.toArray();
        double[][] C1_arr = C2.toArray();
        double[][] F0_arr = F1.toArray();
        double[][] F1_arr = F2.toArray();

        Matrix[] b = null;
        Matrix[] c = null;
        Matrix[][] d = null;
        Matrix[] f = null;

        double[][] b0_arr;
        double[][] b1_arr;
        double[][] c0_arr;
        double[][] c1_arr;
        double[][] d00_arr;
        double[][] d01_arr;
        double[][] d10_arr;
        double[][] d11_arr;

        //合成整体矩阵
        for (int i = 0; i <= elementNum - 1; i++) {
            b = getB(eleQuad_9[i]);//两个四行九列的矩阵
            c = getC(eleQuad_9[i]);//两个九行四列的矩阵
            d = getD(eleQuad_9[i]);//四个九行九列的矩阵
            if (eleQuad_4[i].isBoundaryElement()) {
                f = getF(eleQuad_4[i]);//两个九行一列的矩阵
                for (int m = 0; m <= 8; m++) {
                    int nodeId = eleQuad_9[i].getNodeList()[m]-1;
                    F0_arr[nodeId][0] = F0_arr[nodeId][0] + f[0].toArray()[m][0];
                    F1_arr[nodeId][0] = F1_arr[nodeId][0] + f[0].toArray()[m][0];
                }
            }
            b0_arr = b[0].toArray();
            b1_arr = b[1].toArray();
            c0_arr = c[0].toArray();
            c1_arr = c[1].toArray();
            d00_arr = d[0][0].toArray();
            d01_arr = d[0][1].toArray();
            d10_arr = d[1][0].toArray();
            d11_arr = d[1][1].toArray();

            for (int m = 0; m <= 3; m++) {
                for (int n = 0; n <= 8; n++) {
                    int mm = eleQuad_4[i].getNodeList()[m]-1;
                    int nn = eleQuad_9[i].getNodeList()[n]-1;
                    B0_arr[mm][nn] = B0_arr[mm][nn] + b0_arr[m][n];
                    B1_arr[mm][nn] = B1_arr[mm][nn] + b1_arr[m][n];
                }
            }

            for (int m = 0; m <= 8; m++) {
                for (int n = 0; n <= 3; n++) {
                    int mm = eleQuad_9[i].getNodeList()[m]-1;
                    int nn = eleQuad_4[i].getNodeList()[n]-1;
                    C0_arr[mm][nn] = C0_arr[mm][nn] + c0_arr[m][n];
                    C1_arr[mm][nn] = C1_arr[mm][nn] + c1_arr[m][n];
                }
            }

            for (int m = 0; m <= 8; m++) {
                for (int n = 0; n <= 8; n++) {
                    int mm = eleQuad_9[i].getNodeList()[m]-1;//mm是节点的编号
                    int nn = eleQuad_9[i].getNodeList()[n]-1;//nn是节点的编号
                    D00_arr[mm][nn] = D00_arr[mm][nn] + d00_arr[m][n];
                    D01_arr[mm][nn] = D01_arr[mm][nn] + d01_arr[m][n];
                    D10_arr[mm][nn] = D10_arr[mm][nn] + d10_arr[m][n];
                    D11_arr[mm][nn] = D11_arr[mm][nn] + d11_arr[m][n];
                }
            }

        }

        //至此，计算出了整体矩阵，下面是求解整体矩阵
        double[][] wholeArr = new double[187][188];
        for (int i = 0; i <= 186; i++) {
            if (i <= 80) {
                for (int j = 0; j <= 80; j++) {
                    wholeArr[i][j] = D00_arr[i][j];
                }
                for (int j = 81; j <= 161; j++) {
                    wholeArr[i][j] = D01_arr[i][j - 81];
                }
                for (int j = 162; j <= 186; j++) {
                    wholeArr[i][j] = -C0_arr[i][j - 162];
                }
            } else if (i <= 161) {
                for (int j = 0; j <= 80; j++) {
                    wholeArr[i][j] = D10_arr[i - 81][j];
                }
                for (int j = 81; j <= 161; j++) {
                    wholeArr[i][j] = D11_arr[i - 81][j - 81];
                }
                for (int j = 162; j <= 186; j++) {
                    wholeArr[i][j] = -C1_arr[i - 81][j - 162];
                }
            } else if (i <= 186) {
                for (int j = 0; j <= 80; j++) {
                    wholeArr[i][j] = B0_arr[i - 162][j];
                }
                for (int j = 81; j <= 161; j++) {
                    wholeArr[i][j] = B1_arr[i - 162][j - 81];
                }
                for (int j = 162; j <= 186; j++) {
                    wholeArr[i][j] = 0;
                }
            }
        }
        for (int i = 0; i <= 186; i++) {
            if (i <= 80) {
                wholeArr[i][187] = F0_arr[i][0];
            } else if (i <= 161) {
                wholeArr[i][187] = F1_arr[i - 81][0];
            } else {
                wholeArr[i][187] = 0;
            }
        }

        Matrix whole_matrix = new Matrix(wholeArr);
        //System.out.println("whole_matrix:");
        //whole_matrix.printMatrix();
        double[] result = whole_matrix.getSolution();
        System.out.println("计算结果如下：");
        for (int i = 0; i <= 186; i++) {
            System.out.println("result[" + (i + 1) + "]=" + result[i]);
        }
    }

    private Matrix[] getB(EleQuad_9 element) {
        Matrix[] b_matrix = new Matrix[2];
        Matrix b1_matrix = new Matrix(4, 9);//创建一个4行9列元素全为0的矩阵
        Matrix b2_matrix = new Matrix(4, 9);

        //取得当前单元的九个节点的信息
        Point2D[] point9 = new Point2D[9];
        for (int i = 0; i <= 8; i++) {
            point9[i] = nodeV[element.getNodeList()[i] - 1];
        }

        //取得高斯积分点
        int gaussPointNum = 6;
        Point2D[] gaussPointList = new GaussPoint(gaussPointNum).getPointList();

        //循环叠加求矩阵B
        Matrix fyp_matrix;
        double[] fy_kesi;
        double[] fy_ita;
        Matrix tempMatrix1;
        Matrix jacobiMatrix;
        double[] tempArr1 = new double[9];
        double[] tempArr2 = new double[9];
        double detJ;
        double kesi;
        double ita;
        Matrix fy_x;
        Matrix fy_y;
        for (int i = 0; i <= gaussPointNum - 1; i++) {
            for (int j = 0; j <= gaussPointNum - 1; j++) {
                kesi = gaussPointList[i].getX();
                ita = gaussPointList[j].getX();
                fyp_matrix = new Fai_2D_4node().getN_iMatrix(false, kesi, ita);
                Fai_2D_9node fai_9_node = new Fai_2D_9node(point9);
                fy_kesi = fai_9_node.getFai_kesi(kesi, ita);//行矩阵,9列
                fy_ita = fai_9_node.getFai_ita(kesi, ita);//行矩阵
                double[][] arr = new double[2][9];
                for (int k = 0; k <= 8; k++) {
                    arr[0][k] = fy_kesi[k];
                    arr[1][k] = fy_ita[k];
                }
                tempMatrix1 = new Matrix(arr);
                jacobiMatrix = fai_9_node.getJacobiMatrix(kesi, ita);
                tempMatrix1 = Matrix.cross(jacobiMatrix.inverseMatrix(), tempMatrix1);
                for (int k = 0; k <= 8; k++) {
                    tempArr1[k] = tempMatrix1.toArray()[0][k];
                    tempArr2[k] = tempMatrix1.toArray()[1][k];
                }
                fy_x = new Matrix(true, tempArr1);
                fy_y = new Matrix(true, tempArr2);
                detJ = jacobiMatrix.getDetValue();
                detJ = detJ * gaussPointList[i].getY() * gaussPointList[j].getY();
                tempMatrix1 = Matrix.cross(fyp_matrix, fy_x);
                tempMatrix1 = tempMatrix1.multiply(detJ);

                b1_matrix = Matrix.plus(b1_matrix, tempMatrix1);

                tempMatrix1 = Matrix.cross(fyp_matrix, fy_y);
                tempMatrix1 = tempMatrix1.multiply(detJ);
                b2_matrix = Matrix.plus(tempMatrix1, b2_matrix);
            }
        }
        b_matrix[0] = b1_matrix;
        b_matrix[1] = b2_matrix;
        return b_matrix;
    }

    private Matrix[] getC(EleQuad_9 element) {
        Matrix[] c_matrix = new Matrix[2];
        Matrix c1_matrix = new Matrix(9, 4);
        Matrix c2_matrix = new Matrix(9, 4);

        //取得当前单元的九个节点的信息
        Point2D[] point9 = new Point2D[9];
        for (int i = 0; i <= 8; i++) {
            point9[i] = nodeV[element.getNodeList()[i] - 1];
        }

        //取得高斯积分点
        int gaussPointNum = 6;
        Point2D[] gaussPointList = new GaussPoint(gaussPointNum).getPointList();

        //循环叠加求矩阵C
        Matrix fyp_matrix;
        Matrix tempMatrix1;
        double[] fy_kesi;//fai对kesi的偏导数
        double[] fy_ita;
        for (int i = 0; i <= gaussPointNum - 1; i++) {
            for (int j = 0; j <= gaussPointNum - 1; j++) {
                double kesi = gaussPointList[i].getX();
                double ita = gaussPointList[j].getX();
                fyp_matrix = new Fai_2D_4node().getN_iMatrix(true, kesi, ita);
                Fai_2D_9node fai_9_node = new Fai_2D_9node(point9);
                fy_kesi = fai_9_node.getFai_kesi(kesi, ita);//一行九列
                fy_ita = fai_9_node.getFai_ita(kesi, ita);
                Matrix jacobiMatrix = fai_9_node.getJacobiMatrix(kesi, ita);
                double detJ = jacobiMatrix.getDetValue();
                detJ = detJ * gaussPointList[i].getY() * gaussPointList[j].getY();
                double[][] tempArr = new double[2][9];
                for (int k = 0; k <= 8; k++) {
                    tempArr[0][k] = fy_kesi[k];
                    tempArr[1][k] = fy_ita[k];
                }
                tempMatrix1 = new Matrix(tempArr);
                tempMatrix1 = Matrix.cross(jacobiMatrix.inverseMatrix(), tempMatrix1);
                double[] tempArr1 = new double[9];
                double[] tempArr2 = new double[9];
                tempArr = tempMatrix1.toArray();
                for (int k = 0; k <= 8; k++) {
                    tempArr1[k] = tempArr[0][k];
                    tempArr2[k] = tempArr[1][k];
                }

                Matrix fy_x = new Matrix(false, tempArr1);
                Matrix fy_y = new Matrix(false, tempArr2);
                tempMatrix1 = Matrix.cross(fy_x, fyp_matrix);
                tempMatrix1 = tempMatrix1.multiply(detJ);
                c1_matrix = Matrix.plus(tempMatrix1, c1_matrix);

                tempMatrix1 = Matrix.cross(fy_y, fyp_matrix);
                tempMatrix1 = tempMatrix1.multiply(detJ);
                c2_matrix = Matrix.plus(tempMatrix1, c2_matrix);
            }
        }

        c_matrix[0] = c1_matrix;
        c_matrix[1] = c2_matrix;
        return c_matrix;
    }

    private Matrix[][] getD(EleQuad_9 element) {
        Matrix[][] d_matrix = new Matrix[2][2];
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                d_matrix[i][j] = new Matrix(9, 9);
            }
        }

        //取得当前单元的九个节点的信息
        Point2D[] point9 = new Point2D[9];
        for (int i = 0; i <= 8; i++) {
            point9[i] = nodeV[element.getNodeList()[i] - 1];
        }

        //取得高斯积分数据
        int gaussPointNum = 6;
        Point2D[] gaussPointList = new GaussPoint(gaussPointNum).getPointList();

        //叠加求取矩阵D
        for (int i = 0; i <= gaussPointNum - 1; i++) {
            for (int j = 0; j <= gaussPointNum - 1; j++) {
                double kesi = gaussPointList[i].getX();
                double ita = gaussPointList[j].getX();
                Fai_2D_9node fai_9_node = new Fai_2D_9node(point9);
                double[][] tempArr = new double[2][9];
                tempArr[0] = fai_9_node.getFai_kesi(kesi, ita);//tempArr[0]是一个数组
                tempArr[1] = fai_9_node.getFai_ita(kesi, ita);
                Matrix tempMatrix1 = new Matrix(tempArr);
                Matrix jacobiMatrix = fai_9_node.getJacobiMatrix(kesi, ita);
                tempMatrix1 = Matrix.cross(jacobiMatrix.inverseMatrix(), tempMatrix1);
                tempArr = tempMatrix1.toArray();
                Matrix fy_x = new Matrix(false, tempArr[0]);
                Matrix fy_y = new Matrix(false, tempArr[1]);

                tempMatrix1 = Matrix.cross(fy_x, fy_x.transpose());
                tempMatrix1 = tempMatrix1.multiply(2);
                Matrix tempMatrix2 = Matrix.cross(fy_y, fy_y.transpose());

                tempMatrix1 = Matrix.plus(tempMatrix1, tempMatrix2);
                double detJ = jacobiMatrix.getDetValue();
                detJ = detJ * gaussPointList[i].getY() * gaussPointList[i].getY();
                tempMatrix1 = tempMatrix1.multiply(detJ);
                d_matrix[0][0] = Matrix.plus(d_matrix[0][0], tempMatrix1);

                tempMatrix1 = Matrix.cross(fy_x, fy_y.transpose());
                tempMatrix1 = tempMatrix1.multiply(detJ);
                d_matrix[0][1] = Matrix.plus(d_matrix[0][1], tempMatrix1);

                tempMatrix1 = Matrix.cross(fy_y, fy_x.transpose());
                tempMatrix1 = tempMatrix1.multiply(detJ);
                d_matrix[1][0] = Matrix.plus(d_matrix[1][0], tempMatrix1);

                tempMatrix1 = Matrix.cross(fy_y, fy_y.transpose());
                tempMatrix1 = tempMatrix1.multiply(2);
                tempMatrix2 = Matrix.cross(fy_x, fy_x.transpose());
                tempMatrix2 = Matrix.plus(tempMatrix1, tempMatrix2);
                tempMatrix2 = tempMatrix2.multiply(detJ);
                d_matrix[1][1] = Matrix.plus(d_matrix[1][1], tempMatrix2);

            }
        }
        d_matrix[0][0] = d_matrix[0][0].multiply(miu);
        d_matrix[0][1] = d_matrix[0][1].multiply(miu);
        d_matrix[1][0] = d_matrix[1][0].multiply(miu);
        d_matrix[1][1] = d_matrix[1][1].multiply(miu);

        return d_matrix;
    }

    private Matrix[] getF(EleQuad_4 element_4) {
        Matrix[] f_matrix = new Matrix[2];
        f_matrix[0] = new Matrix(9, 1);
        f_matrix[1] = new Matrix(9, 1);

        //取得当前压力单元对应的速度单元的九个节点的信息
        Point2D[] point9 = new Point2D[9];
        for (int i = 0; i <= 8; i++) {
            point9[i] = nodeV[eleQuad_9[element_4.getElementId() - 1]
                    .getNodeList()[i] - 1];
        }

        //取得高斯积分数据
        int gaussPointNum = 6;
        Point2D[] gaussPointList = new GaussPoint(gaussPointNum).getPointList();

        int boundarySide = element_4.getBoundarySide();
        double[] eleInfo = element_4.getInfo();//分别是cosx, cosy, P1, P2

        Matrix p_matrix;
        Matrix fy_matrix;
        Matrix fyp_matrix;
        double lengthOfBoundarySide;
        if (boundarySide == 1) {
            for (int i = 0; i <= gaussPointNum - 1; i++) {
                double kesi = gaussPointList[i].getX();
                Fai_2D_9node fai_9_node = new Fai_2D_9node(point9);
                double[] tempArr0 = fai_9_node.getN_i(kesi, -1);
                Fai_2D_4node fai_4_node = new Fai_2D_4node();
                double[] tempArr1 = fai_4_node.getN_i(kesi, -1);
                double[] p_arr = new double[4];
                p_arr[0] = eleInfo[2];
                p_arr[1] = eleInfo[3];
                p_arr[2] = 0;
                p_arr[3] = 0;
                p_matrix = new Matrix(false, p_arr);//列矩阵
                fy_matrix = new Matrix(false, tempArr0);//列矩阵
                fyp_matrix = new Matrix(true, tempArr1);//行矩阵               
                lengthOfBoundarySide = sqrt(
                        pow((point9[2].getX() - point9[0].getX()), 2)
                        + pow((point9[2].getY() - point9[0].getY()), 2));
                Matrix tempMatrix = Matrix.cross(fyp_matrix, p_matrix);
                double tempDouble = tempMatrix.toArray()[0][0];
                double tempDouble1 = tempDouble * gaussPointList[i].getY() * lengthOfBoundarySide * eleInfo[0] / 2;
                tempMatrix = fy_matrix.multiply(tempDouble1);
                f_matrix[0].printMatrix();
                System.out.println("-----------");
                tempMatrix.printMatrix();
                f_matrix[0] = Matrix.plus(f_matrix[0], tempMatrix);
                double tempDouble2 = tempDouble * gaussPointList[i].getY() * lengthOfBoundarySide * eleInfo[1] / 2;
                tempMatrix = fy_matrix.multiply(tempDouble2);
                f_matrix[1] = Matrix.plus(f_matrix[1], tempMatrix);
            }
        } else if (boundarySide == 2) {
            for (int i = 0; i <= gaussPointNum - 1; i++) {
                double ita = gaussPointList[i].getX();
                Fai_2D_9node fai_9_node = new Fai_2D_9node(point9);
                double[] tempArr0 = fai_9_node.getN_i(1, ita);
                Fai_2D_4node fai_4_node = new Fai_2D_4node();
                double[] tempArr1 = fai_4_node.getN_i(1, ita);
                double[] p_arr = new double[4];
                p_arr[0] = 0;
                p_arr[1] = eleInfo[2];
                p_arr[2] = eleInfo[3];
                p_arr[3] = 0;
                p_matrix = new Matrix(false, p_arr);//列矩阵
                fy_matrix = new Matrix(false, tempArr0);//列矩阵
                fyp_matrix = new Matrix(true, tempArr1);//行矩阵               
                lengthOfBoundarySide = sqrt(
                        pow((point9[2].getX() - point9[8].getX()), 2)
                        + pow((point9[2].getY() - point9[8].getY()), 2));
                Matrix tempMatrix = Matrix.cross(fyp_matrix, p_matrix);
                double tempDouble = tempMatrix.toArray()[0][0];
                double tempDouble1 = tempDouble * gaussPointList[i].getY() * lengthOfBoundarySide * eleInfo[0] / 2;
                tempMatrix = fy_matrix.multiply(tempDouble1);
                f_matrix[0] = Matrix.plus(f_matrix[0], tempMatrix);
                double tempDouble2 = tempDouble * gaussPointList[i].getY() * lengthOfBoundarySide * eleInfo[1] / 2;
                tempMatrix = fy_matrix.multiply(tempDouble2);
                f_matrix[1] = Matrix.plus(f_matrix[1], tempMatrix);
            }
        } else if (boundarySide == 3) {
            for (int i = 0; i <= gaussPointNum - 1; i++) {
                double kesi = gaussPointList[i].getX();
                Fai_2D_9node fai_9_node = new Fai_2D_9node(point9);
                double[] tempArr0 = fai_9_node.getN_i(kesi, 1);
                Fai_2D_4node fai_4_node = new Fai_2D_4node();
                double[] tempArr1 = fai_4_node.getN_i(kesi, 1);
                double[] p_arr = new double[4];
                p_arr[0] = 0;
                p_arr[1] = 0;
                p_arr[2] = eleInfo[0];
                p_arr[3] = eleInfo[1];
                p_matrix = new Matrix(false, p_arr);//列矩阵
                fy_matrix = new Matrix(false, tempArr0);//列矩阵
                fyp_matrix = new Matrix(true, tempArr1);//行矩阵               
                lengthOfBoundarySide = sqrt(
                        pow((point9[6].getX() - point9[8].getX()), 2)
                        + pow((point9[6].getY() - point9[8].getY()), 2));
                Matrix tempMatrix = Matrix.cross(fyp_matrix, p_matrix);
                double tempDouble = tempMatrix.toArray()[0][0];
                double tempDouble1 = tempDouble * gaussPointList[i].getY() * lengthOfBoundarySide * eleInfo[0] / 2;
                tempMatrix = fy_matrix.multiply(tempDouble1);
                f_matrix[0] = Matrix.plus(f_matrix[0], tempMatrix);
                double tempDouble2 = tempDouble * gaussPointList[i].getY() * lengthOfBoundarySide * eleInfo[1] / 2;
                tempMatrix = fy_matrix.multiply(tempDouble2);
                f_matrix[1] = Matrix.plus(f_matrix[1], tempMatrix);
            }
        } else {
            for (int i = 0; i <= gaussPointNum - 1; i++) {
                double ita = gaussPointList[i].getX();
                Fai_2D_9node fai_9_node = new Fai_2D_9node(point9);
                double[] tempArr0 = fai_9_node.getN_i(-1, ita);
                Fai_2D_4node fai_4_node = new Fai_2D_4node();
                double[] tempArr1 = fai_4_node.getN_i(-1, ita);
                double[] p_arr = new double[4];
                p_arr[0] = eleInfo[3];
                p_arr[1] = 0;
                p_arr[2] = 0;
                p_arr[3] = eleInfo[2];
                p_matrix = new Matrix(false, p_arr);//列矩阵
                fy_matrix = new Matrix(false, tempArr0);//列矩阵
                fyp_matrix = new Matrix(true, tempArr1);//行矩阵               
                lengthOfBoundarySide = sqrt(
                        pow((point9[0].getX() - point9[6].getX()), 2)
                        + pow((point9[0].getY() - point9[6].getY()), 2));
                Matrix tempMatrix = Matrix.cross(fyp_matrix, p_matrix);
                double tempDouble = tempMatrix.toArray()[0][0];
                double tempDouble1 = tempDouble * gaussPointList[i].getY() * lengthOfBoundarySide * eleInfo[0] / 2;
                tempMatrix = fy_matrix.multiply(tempDouble1);
                f_matrix[0].printMatrix();
                System.out.println("-----------");
                tempMatrix.printMatrix();
                f_matrix[0] = Matrix.plus(f_matrix[0], tempMatrix);
                double tempDouble2 = tempDouble * gaussPointList[i].getY() * lengthOfBoundarySide * eleInfo[1] / 2;
                tempMatrix = fy_matrix.multiply(tempDouble2);
                f_matrix[1] = Matrix.plus(f_matrix[1], tempMatrix);
            }
        }

        return f_matrix;
    }

    private EleQuad_9[] readJMV(String fileName) {
        EleQuad_9[] eleQuadr_9 = null;
        File file = new File(fileName);
        ArrayList<EleQuad_9> eleQuad_9List = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            String lineText;
            String[] dataStr = new String[10];
            int elementId = 0;

            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            int num = 0;

            while ((lineText = reader.readLine()) != null) {
                dataStr = lineText.split(" ");
                elementId = Integer.parseInt(dataStr[0]);
                int[] pointIdArr = new int[9];//一个单元有九个节点，该声明不能提到循环外面
                for (int i = 0; i <= 8; i++) {
                    pointIdArr[i] = Integer.parseInt(dataStr[i + 1]);
                }
                EleQuad_9 ele = new EleQuad_9(elementId, pointIdArr);
                eleQuad_9List.add(num, ele);
            }
            int size = eleQuad_9List.size();
            eleQuadr_9 = new EleQuad_9[size];
            for (int i = 0; i <= size - 1; i++) {
                eleQuadr_9[i] = eleQuad_9List.get(size - 1 - i);

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Example3_1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {

        }

        return eleQuadr_9;
    }

    private EleQuad_4[] readJMP(String fileName) {
        EleQuad_4[] eleQuadr_4 = null;
        File file = new File(fileName);
        ArrayList<EleQuad_4> eleQuad_4List = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            String lineText;
            String[] dataStr = new String[10];
            int elementId = 0;

            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            int num = 0;

            while ((lineText = reader.readLine()) != null) {
                dataStr = lineText.split(" ");
                elementId = Integer.parseInt(dataStr[0]);
                int[] pointIdArr = new int[4];//一个单元有九个节点，该声明不能提到循环外面
                for (int i = 0; i <= 3; i++) {
                    pointIdArr[i] = Integer.parseInt(dataStr[i + 1]);
                }
                EleQuad_4 ele = new EleQuad_4(elementId, pointIdArr);
                eleQuad_4List.add(num, ele);
            }
            int size = eleQuad_4List.size();
            eleQuadr_4 = new EleQuad_4[size];
            for (int i = 0; i <= size - 1; i++) {
                eleQuadr_4[i] = eleQuad_4List.get(size - 1 - i);

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Example3_1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {

        }

        return eleQuadr_4;
    }

    private Point2D[] readJXYV(String fileName) {
        Point2D[] node = null;
        File file = new File(fileName);
        ArrayList<Point2D> point2DList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(file);
            String lineText;
            String[] dataStr = new String[10];
            int nodeId = 0;

            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            int num = 0;

            while ((lineText = reader.readLine()) != null) {
                dataStr = lineText.split(" ");
                nodeId = Integer.parseInt(dataStr[0]);
                double x;//节点的x坐标
                double y;
                x = Double.parseDouble(dataStr[1]);
                y = Double.parseDouble(dataStr[2]);
                point2DList.add(num, new Point2D(nodeId, x, y));
            }
            int size = point2DList.size();
            node = new Point2D[size];
            for (int i = 0; i <= size - 1; i++) {
                node[i] = point2DList.get(size - 1 - i);

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Example3_1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {

        }

        return node;
    }

    private void readJBV(String fileName) {
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String lineText;
            while ((lineText = reader.readLine()) != null) {
                String[] dataStr = lineText.split(" ");
                int nodeId = Integer.parseInt(dataStr[0]);
                double u = Double.parseDouble(dataStr[1]);
                double v = Double.parseDouble(dataStr[2]);
                nodeV[nodeId - 1].setVelocity(u, v);
            }
        } catch (Exception e) {

        }
    }

    private void readJBP(String fileName) {
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String lineText;
            while ((lineText = reader.readLine()) != null) {
                String[] dataStr = lineText.split(" ");
                int elementId = Integer.parseInt(dataStr[0]);
                int side = Integer.parseInt(dataStr[1]);
                double cosx = Double.parseDouble(dataStr[2]);
                double cosy = Double.parseDouble(dataStr[3]);
                double p1 = Double.parseDouble(dataStr[4]);
                double p2 = Double.parseDouble(dataStr[5]);
                eleQuad_4[elementId - 1].setInfo(side, cosx, cosy, p1, p2);
            }
        } catch (Exception e) {

        }
    }

    private void generateMesh() {

    }

    private void printInitInfo() {
        System.out.println("以下是节点信息：<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Point2D.printTitle();
        for (int i = 0; i <= nodeV.length - 1; i++) {
            nodeV[i].printPoint2D();
        }
        System.out.println("节点信息输出结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("");
        System.out.println("以下是速度单元信息：<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        EleQuad_9.printTitle();
        for (int i = 0; i <= eleQuad_9.length - 1; i++) {
            eleQuad_9[i].printElement();
        }
        System.out.println("速度单元信息输出结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("");
        System.out.println("以下是压力单元信息：<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        EleQuad_4.printTitle();
        for (int i = 0; i <= eleQuad_4.length - 1; i++) {
            eleQuad_4[i].printElement();
        }
        System.out.println("压力单元信息输出结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

}
