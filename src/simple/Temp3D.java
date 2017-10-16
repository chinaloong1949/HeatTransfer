/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import common.CalculateInfoBag;
import common.GaussPoint;
import common.Matrix;
import element.BoundaryTEle;
import element.EleQuad_8;
import static java.lang.Math.sqrt;
import point.Point3D;
import element.FormB;
import element.FormM_A;
import element.FormN;
import static java.lang.Math.pow;
import javax.swing.JOptionPane;
import point.Point2D;

/**
 *
 * @author yk
 */
public class Temp3D {

    private double initialTime;
    private double totalTime;
    private int timeSteps;
    private EleQuad_8[] element;
    private Point3D[] node;
    private int elementNum;
    private int nodeNum;
    private BoundaryTEle[] boundTEle;
    private double namuda = 46.0;//导热系数,铁的导热系数为45左右，水的为0.5左右    
    private double h = 120;//表面换热系数

    double tEnvironment;//环境温度
    private double[] tLast = new double[nodeNum];
    private double[] tnow = new double[nodeNum];
    private int gaussPointNum;

    public Temp3D(CalculateInfoBag dataBag) {
        initData(dataBag);
        calculate();
    }

    private void initData(CalculateInfoBag dataBag) {
        initialTime = dataBag.getAnalysisType_initialTime();
        totalTime = dataBag.getAnalysisType_totalTime();
        timeSteps = dataBag.getAnalysisType_timeSteps();
        element = dataBag.getEleQuad_8();
        elementNum = element.length;
        node = dataBag.getNodeP();
        nodeNum = node.length;
        boundTEle = dataBag.getTempBoundary();
        tLast = dataBag.getTempInitial();
        tEnvironment = 200;
        gaussPointNum = dataBag.getGaussPointNum();

    }

    private void calculate() {
        double presentTime;
        double deltaTime = totalTime / timeSteps;
        double[][] result = new double[timeSteps + 1][nodeNum];
        double[][] gk = new double[nodeNum][nodeNum];
        double[][] gc = new double[nodeNum][nodeNum];
        double[] pb = new double[nodeNum];

        result[0] = tLast;
        for (int i = 0; i <= timeSteps - 1; i++) {
            //writeT_nFile(tLast, i);//将当前时刻的温度值输出到相应文件中
            Matrix TLast = new Matrix(false, tLast);
            presentTime = initialTime + deltaTime * (i + 1);
            gk = createGK();//得到当前时刻的温度项系数矩阵
            gc = createGC();//得到当前时刻的温度导数项系数矩阵
            pb = createPB();//得到当前时刻的右端温度载荷矩阵
            Matrix GK = new Matrix(gk);
            Matrix GC = new Matrix(gc);
            Matrix PB = new Matrix(false, pb);
            GK.writeToFile("GK.txt");
            GC.writeToFile("GC.txt");
            PB.writeToFile("PB.txt");
            GC = GC.multiply(1 / deltaTime);
            GK = Matrix.plus(GK, GC);
            PB = Matrix.plus(PB, Matrix.cross(GC, TLast));
            tLast = Matrix.getSolution(GK, PB);
            result[i + 1] = tLast;
        }
        new Matrix(false, tLast).writeToFile("Temp3D_tLast.txt");
        new Matrix(result).writeToFile("Temp3D_result_1GaussPoint.txt");
    }

    private double[][] createGK() {
        //计算导热总刚
        double[][] gkwhole = new double[nodeNum][nodeNum];
        double[][] gk;// = new double[8][8];

        for (int i = 0; i <= elementNum - 1; i++) {
            gk = element_dispatcher(0, i);
            new Matrix(gk).writeToFile("gk+" + i + ".txt");
            int[] nodeList = element[i].getNodeList();
            for (int m = 0; m <= 7; m++) {
                for (int n = 0; n <= 7; n++) {
                    int mm = nodeList[m] - 1;
                    int nn = nodeList[n] - 1;
                    gkwhole[mm][nn] = gkwhole[mm][nn] + gk[m][n];
                }
            }

        }
        return gkwhole;
    }

    private double[][] createGC() {
        //计算蓄热总刚
        double[][] gcwhole = new double[nodeNum][nodeNum];
        double[][] gc = new double[8][8];
        for (int i = 0; i <= elementNum - 1; i++) {
            gc = element_dispatcher(1, i);
            int[] nodeList = element[i].getNodeList();
            for (int m = 0; m <= 7; m++) {
                for (int n = 0; n <= 7; n++) {
                    int mm = nodeList[m] - 1;
                    int nn = nodeList[n] - 1;
                    gcwhole[mm][nn] = gcwhole[mm][nn] + gc[m][n];
                }
            }
        }
        return gcwhole;
    }

    private double[] createPB() {
        double[] pbwhole = new double[nodeNum];
        double[][] pb;// = new double[8][1];//8行1列
        for (int i = 0; i <= elementNum - 1; i++) {
            pb = element_dispatcher(2, i);
            int[] nodeList = element[i].getNodeList();
            for (int m = 0; m <= 7; m++) {
                int mm = nodeList[m] - 1;
                pbwhole[mm] = pbwhole[mm] + pb[m][0];
            }
        }
        return pbwhole;
    }

    private double[][] element_dispatcher(int isw, int index) {
        //index为节点或者单元索引号
        double[][] result = new double[8][8];//相当于随便赋了个初值，不然程序报错
        int elementClass = 1;

        switch (elementClass) {
            case 1:
                result = elemThermalSolid20(isw, index);
                break;
            default:
                break;
        }
        return result;

    }

    private double[][] elemThermalSolid20(int isw, int elementIndex) {
        double[][] result = null;
        switch (isw) {
            case 0:
                result = makeThermalSolid20EK(elementIndex);
                break;
            case 1:
                result = makeThermalSolid20EC(elementIndex);
                break;
            case 2:
                result = makeThermalSolid20EP(elementIndex);
                break;
            default:
                break;

        }
        return result;
    }

    //对应程序7.22
    private double[][] makeThermalSolid20EK(int elementIndex) {

        double[][] ek = new double[8][8];

        //取出8个节点的坐标
        double[] x = new double[8];
        double[] y = new double[8];
        double[] z = new double[8];
        //<editor-fold defaultstate="collapsed" desc="取出8个节点坐标">
        Point3D[] point8 = new Point3D[8];
        int[] nodeList = element[elementIndex].getNodeList();
        for (int i = 0; i <= 7; i++) {
            point8[i] = node[nodeList[i] - 1];
            x[i] = point8[i].getX();
            y[i] = point8[i].getY();
            z[i] = point8[i].getZ();
        }
        //</editor-fold>

        //求出8个高斯点处的导热系数
        double[] namuda = get01_GIP_ThermalCond();

        Point2D[] gaussPointList = new GaussPoint(gaussPointNum).getPointList();

        //对8个高斯点循环，求出导热单刚
        for (int i = 0; i < gaussPointNum; i++) {
            for (int j = 0; j < gaussPointNum; j++) {
                for (int k = 0; k < gaussPointNum; k++) {
                    double kesi = gaussPointList[i].getX();
                    double ita = gaussPointList[j].getX();
                    double zeta = gaussPointList[k].getX();
                    FormB formB = new FormB(kesi, ita, zeta, x, y, z);
                    double[][] b = formB.getB();
                    double detJ = formB.getDetJ();
                    double mid01 = namuda[i] * gaussPointList[i].getY() * gaussPointList[j].getY()
                            * gaussPointList[k].getY() * detJ;
                    for (int ii = 0; ii <= 7; ii++) {
                        for (int jj = 0; jj <= 7; jj++) {
                            for (int kk = 0; kk <= 2; kk++) {
                                ek[ii][jj] = ek[ii][jj] + b[kk][ii] * b[kk][jj] * mid01;
                            }
                        }
                    }
                }
            }
        }

        //若当前单元是边界贡献矩阵，叠加边界贡献矩阵
        boolean isBoundaryEle = false;
        int bpIndex = 0;
        for (int i = 0; i <= boundTEle.length - 1; i++) {
            if (boundTEle[i].getElementId() == (elementIndex + 1)) {
                isBoundaryEle = true;
                bpIndex = i;
                break;
            }
        }
        if (isBoundaryEle) {
            double[][] eh = makeThermalSolid20EH(bpIndex);
            for (int ii = 0; ii <= 7; ii++) {
                for (int jj = 0; jj <= 7; jj++) {
                    ek[ii][jj] = ek[ii][jj] + eh[ii][jj];
                }
            }
        }

        return ek;

    }

    private double[][] makeThermalSolid20EH(int bpIndex) {
        double[][] eh = new double[8][8];
        //确定当前单元位于边界上的表面总数
        BoundaryTEle btp = boundTEle[bpIndex];

        int[] surface = btp.getSurface();
        for (int i = 0; i <= surface.length - 1; i++) {
            double[][] lk = new double[4][4];
            lk = createSurfaceLK(btp, i);
            //将获得的面上的lk贡献矩阵叠加到单元贡献矩阵中

            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;
            if (surface[i] == 0) {
                a = 0;
                b = 3;
                c = 7;
                d = 4;
            } else if (surface[i] == 1) {
                a = 1;
                b = 2;
                c = 6;
                d = 5;
            } else if (surface[i] == 2) {
                //1,2,6,5
                a = 0;
                b = 1;
                c = 5;
                d = 4;
            } else if (surface[i] == 3) {
                a = 3;
                b = 2;
                c = 6;
                d = 7;
            } else if (surface[i] == 4) {
                a = 0;
                b = 1;
                c = 2;
                d = 3;
            } else if (surface[i] == 5) {
                a = 4;
                b = 5;
                c = 6;
                d = 7;
            }
            eh[a][a] = eh[a][a] + lk[0][0];
            eh[a][b] = eh[a][b] + lk[0][1];
            eh[a][c] = eh[a][c] + lk[0][2];
            eh[a][d] = eh[a][d] + lk[0][3];
            eh[b][a] = eh[b][a] + lk[1][0];
            eh[b][b] = eh[b][b] + lk[1][1];
            eh[b][c] = eh[b][c] + lk[1][2];
            eh[b][d] = eh[b][d] + lk[1][3];
            eh[c][a] = eh[c][a] + lk[2][0];
            eh[c][b] = eh[c][b] + lk[2][1];
            eh[c][c] = eh[c][c] + lk[2][2];
            eh[c][d] = eh[c][d] + lk[2][3];
            eh[d][a] = eh[d][a] + lk[3][0];
            eh[d][b] = eh[d][b] + lk[3][1];
            eh[d][c] = eh[d][c] + lk[3][2];
            eh[d][d] = eh[d][d] + lk[3][3];
        }
        return eh;
    }

    private double[][] createSurfaceLK(BoundaryTEle btp, int surfaceIndex) {

        double[][] lk = new double[4][4];

        //取出面上四个点的坐标
        double[] x = new double[4];
        double[] y = new double[4];
        double[] z = new double[4];
        //<editor-fold defaultstate="collapsed" desc="取出面上四个点的坐标">

        Point3D[] point4 = new Point3D[4];
        Point3D[] point8 = new Point3D[8];
        int[] nodeList = element[btp.getElementId() - 1].getNodeList();
        for (int i = 0; i < 8; i++) {
            point8[i] = node[ nodeList[i] - 1];
        }

        if (btp.getSurface()[surfaceIndex] == 0) {
            point4[0] = point8[0];
            point4[1] = point8[3];
            point4[2] = point8[7];
            point4[3] = point8[4];
        } else if (btp.getSurface()[surfaceIndex] == 1) {
            point4[0] = point8[1];
            point4[1] = point8[2];
            point4[2] = point8[6];
            point4[3] = point8[5];
        } else if (btp.getSurface()[surfaceIndex] == 2) {
            point4[0] = point8[0];
            point4[1] = point8[1];
            point4[2] = point8[5];
            point4[3] = point8[4];
        } else if (btp.getSurface()[surfaceIndex] == 3) {
            point4[0] = point8[3];
            point4[1] = point8[2];
            point4[2] = point8[6];
            point4[3] = point8[7];
        } else if (btp.getSurface()[surfaceIndex] == 4) {
            point4[0] = point8[0];
            point4[1] = point8[1];
            point4[2] = point8[2];
            point4[3] = point8[3];
        } else if (btp.getSurface()[surfaceIndex] == 5) {
            point4[0] = point8[4];
            point4[1] = point8[5];
            point4[2] = point8[6];
            point4[3] = point8[7];
        } else {
            JOptionPane.showMessageDialog(null, "边界面序号错误！");
        }
        for (int j = 0; j <= 3; j++) {
            x[j] = point4[j].getX();
            y[j] = point4[j].getY();
            z[j] = point4[j].getZ();
        }
        //</editor-fold>

        Point2D[] gaussPointList = new GaussPoint(gaussPointNum)
                .getPointList();

        //对四个高斯点循环，求出一个边界面上的边界贡献矩阵
        for (int i = 0; i < gaussPointNum; i++) {
            for (int j = 0; j < gaussPointNum; j++) {
                double kesi = gaussPointList[i].getX();
                double ita = gaussPointList[j].getX();
                FormM_A formM_A = new FormM_A(kesi, ita, x, y, z);
                double[] fy = formM_A.getFyp();
                double area = formM_A.getArea();
                area = area * gaussPointList[i].getY() * gaussPointList[j].getY();
                for (int ii = 0; ii <= 3; ii++) {
                    for (int jj = 0; jj <= 3; jj++) {
                        lk[ii][jj] = lk[ii][jj] + fy[ii] * fy[jj] * area;
                    }
                }
            }
        }

        //确定换热系数
        double h = getSurface_h();

        lk = new Matrix(lk).multiply(h).toArray();

        return lk;
    }

    private double[] get01_GIP_ThermalCond() {
        //根据上一步结果求出材料的导热系数作为当前导热系数
        double[] namuda1 = new double[8];
        for (int i = 0; i <= 7; i++) {
            namuda1[i] = this.namuda;
        }
        return namuda1;
    }

    private double getSurface_h() {
        return h;
    }

    private double[][] makeThermalSolid20EC(int elementIndex) {

        double[][] ec = new double[8][8];
        //取出8个节点的坐标
        double[] x = new double[8];
        double[] y = new double[8];
        double[] z = new double[8];
        //<editor-fold defaultstate="collapsed" desc="取出8个节点坐标">
        Point3D[] point8 = new Point3D[8];
        int[] nodeList = element[elementIndex].getNodeList();
        for (int i = 0; i <= 7; i++) {
            point8[i] = node[nodeList[i] - 1];
            x[i] = point8[i].getX();
            y[i] = point8[i].getY();
            z[i] = point8[i].getZ();
        }
        //</editor-fold>

        Point2D[] gaussPointList = new GaussPoint(gaussPointNum).getPointList();

        //求出8个高斯点处的密度比热之积
        double[] densSpecHeat = get01_GIP_DensSpecHeat();
        for (int i = 0; i < gaussPointNum; i++) {
            for (int j = 0; j < gaussPointNum; j++) {
                for (int k = 0; k < gaussPointNum; k++) {
                    double kesi = gaussPointList[i].getX();
                    double ita = gaussPointList[j].getX();
                    double zeta = gaussPointList[k].getX();

                    FormN formN = new FormN(kesi, ita, zeta, x, y, z);
                    double[] n_arr = formN.getN();
                    double detJ = formN.getDetJ();
                    double mid01 = densSpecHeat[i] * gaussPointList[i].getY()
                            * gaussPointList[j].getY() * gaussPointList[k].getY() * detJ;
                    for (int ii = 0; ii <= 7; ii++) {
                        for (int jj = 0; jj <= 7; jj++) {
                            ec[ii][jj] = ec[ii][jj] + n_arr[ii] * n_arr[jj] * mid01;
                        }
                    }
                }
            }
        }

        return ec;
    }

    private double[] get01_GIP_DensSpecHeat() {
        double[] result = new double[8];
        //铁的密度是7900kg/m^3,比热容是45J/(kgK)
        for (int i = 0; i <= 7; i++) {
            result[i] = 7900 * 45;
        }
        return result;
    }

    private double[][] makeThermalSolid20EP(int elementIndex) {
        double[][] ep = new double[8][1];

        //若当前单元是边界贡献矩阵，叠加边界贡献矩阵
        boolean isBoundaryEle = false;
        int bpIndex = 0;
        for (int i = 0; i <= boundTEle.length - 1; i++) {
            if (boundTEle[i].getElementId() == (elementIndex + 1)) {
                isBoundaryEle = true;
                bpIndex = i;
                break;
            }
        }

        if (isBoundaryEle) {
            BoundaryTEle btp = boundTEle[bpIndex];
            for (int i = 0; i <= btp.getSurface().length - 1; i++) {
                double[] fp = createSurfaceFP(btp, i);
                int[] surface = btp.getSurface();
                int a = 0;
                int b = 0;
                int c = 0;
                int d = 0;
                if (surface[i] == 0) {
                    a = 0;
                    b = 3;
                    c = 7;
                    d = 4;
                } else if (surface[i] == 1) {
                    a = 1;
                    b = 2;
                    c = 6;
                    d = 5;
                } else if (surface[i] == 2) {
                    //1,2,6,5
                    a = 0;
                    b = 1;
                    c = 5;
                    d = 4;
                } else if (surface[i] == 3) {
                    a = 3;
                    b = 2;
                    c = 6;
                    d = 7;
                } else if (surface[i] == 4) {
                    a = 0;
                    b = 1;
                    c = 2;
                    d = 3;
                } else if (surface[i] == 5) {
                    a = 4;
                    b = 5;
                    c = 6;
                    d = 7;
                }
                ep[a][0] = ep[a][0] + fp[0];
                ep[b][0] = ep[b][0] + fp[1];
                ep[c][0] = ep[c][0] + fp[2];
                ep[d][0] = ep[d][0] + fp[3];
            }
        }
        return ep;
    }

    private double[] createSurfaceFP(BoundaryTEle btp, int surfaceIndex) {

        double[] fp = new double[4];
        //取出边界面上的节点载荷
        //取出面上四个点的坐标
        double[] x = new double[4];
        double[] y = new double[4];
        double[] z = new double[4];
        //<editor-fold defaultstate="collapsed" desc="取出面上四个点的坐标">

        Point3D[] point4 = new Point3D[4];
        Point3D[] point8 = new Point3D[8];
        int[] nodeList = element[btp.getElementId() - 1].getNodeList();
        for (int i = 0; i < 8; i++) {
            point8[i] = node[nodeList[i] - 1];
        }

        if (btp.getSurface()[surfaceIndex] == 0) {
            point4[0] = point8[0];
            point4[1] = point8[3];
            point4[2] = point8[7];
            point4[3] = point8[4];
        } else if (btp.getSurface()[surfaceIndex] == 1) {
            point4[0] = point8[1];
            point4[1] = point8[2];
            point4[2] = point8[6];
            point4[3] = point8[5];
        } else if (btp.getSurface()[surfaceIndex] == 2) {
            point4[0] = point8[0];
            point4[1] = point8[1];
            point4[2] = point8[5];
            point4[3] = point8[4];
        } else if (btp.getSurface()[surfaceIndex] == 3) {
            //4,3,7,8->3,2,6,7
            point4[0] = point8[3];
            point4[1] = point8[2];
            point4[2] = point8[6];
            point4[3] = point8[7];
        } else if (btp.getSurface()[surfaceIndex] == 4) {
            //1,4,8,5->0,3,7,4
            point4[0] = point8[0];
            point4[1] = point8[1];
            point4[2] = point8[2];
            point4[3] = point8[3];
        } else if (btp.getSurface()[surfaceIndex] == 5) {
            //2,3,7,6->1,2,6,5
            point4[0] = point8[4];
            point4[1] = point8[5];
            point4[2] = point8[6];
            point4[3] = point8[7];
        } else {
            JOptionPane.showMessageDialog(null, "边界面序号错误！");
        }
        for (int j = 0; j <= 3; j++) {
            x[j] = point4[j].getX();
            y[j] = point4[j].getY();
            z[j] = point4[j].getZ();
        }
        //</editor-fold>

        Point2D[] gaussPointList = new GaussPoint(gaussPointNum)
                .getPointList();

        for (int i = 0; i < gaussPointNum; i++) {
            for (int j = 0; j < gaussPointNum; j++) {
                double kesi = gaussPointList[i].getX();
                double ita = gaussPointList[j].getX();
                FormM_A formM_A = new FormM_A(kesi, ita, x, y, z);
                double[] m = formM_A.getFyp();
                double a = formM_A.getArea();//面积分中的a相当于体积分中的detJ
                for (int ii = 0; ii <= 3; ii++) {
                    fp[ii] = fp[ii] + m[ii] * a * gaussPointList[i].getY()
                            * gaussPointList[j].getY();
                }
            }
        }

        //确定环境温度
        double tf = getSurface_Tf();
        //确定换热系数
        double h_in = getSurface_h();

        for (int ii = 0; ii <= 3; ii++) {
            fp[ii] = tf * h_in * fp[ii];
        }
        return fp;
    }

    private double getSurface_Tf() {
        return 373.0;
    }

}
