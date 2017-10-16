///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package simple;
//
//import common.CalculateInfoBag;
//
///**
// *
// * @author Administrator
// */
//public class Solid2D {
//
//    int all_freedom;
//    int all_elem;
//    int all_node;
//    int[] address;
//    Element[] elem;
//    Point3D[] node;
//    int[][] freedom_ID;
//
//    public Solid2D(CalculateInfoBag info) {
//        
//        readInAllModelData(info);
//        prepareForSolution();
//        calculation();
//    }
//    
//    private void readInAllModelData(CalculateInfoBag info){
//        all_node=info.getNodePNum();
//        
//    }
//
//    private void calculation() {
//        double[] gk = new double[address[all_freedom]];
//        double[] gp = new double[all_freedom];
//        boolean ldlt;
//        int i;
//        int n;
//        gk = create_solid_gk(gk);
//        gp = create_solid_gp(gp);
//        ldlt = false;
//        solve(address, gk, gp, ldlt);
//
//    }
//
//    private double[] create_solid_gk(double[] gk) {
//        int i;
//        int j;
//        int l;
//        int freeD1;
//        int freeD2;
//        int addressD1D2;
//        int j1;
//        int l1;
//        int N_In_E;
//        int enf;
//        double[][] ek = new double[24][24];
//        for (i = 0; i < address[address[all_freedom]]; i++) {
//            gk[i] = 0;
//        }
//
//        int ISW = 2;
//        for (i = 0; i < all_elem; i++) {
//            int current_elem = i;
//            for (j = 0; j < 24; j++) {
//                for (l = 0; l < 24; l++) {
//                    ek[l][j] = 0;
//                }
//            }
//            int elem_class = elem[current_elem].getElemClass();
//            ek = element_dispatcher(ek, elem_class, ISW, current_elem);
//            N_In_E = elem[current_elem].getNodeNumber();
//            enf = 2;
//            if (elem_class == 30) {
//                elem_class = 3;
//            }
//            for (j = 0; j < N_In_E; j++) {
//                for (j1 = 0; j1 < 3; j1++) {
//                    freeD1 = freedom_ID[j1][elem[j + 2][i]];
//                    if (freeD1 > 0) {
//                        for (l = 0; l < N_In_E; l++) {
//                            for (l1 = 0; l1 < 3; l1++) {
//                                freeD2 = freedom_ID[l1][elem[l + 2][i]];
//                                if (freeD2 > 0 && freeD2 <= freeD1) {
//                                    addressD1D2 = address[freeD1] - freeD1 + freeD2;
//                                    gk[addressD1D2] = gk[addressD1D2] + ek[enf * (l - 1) + l1][enf * (j - 1) + j1];
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return gk;
//    }
//
//    private double[][] element_dispather(double[][] ek, int elem_class, int ISW, int current_elem) {
//        switch (elem_class) {
//            case 1://三节点三角形单元
//                ek = elem_plane01(ek, ISW, current_elem);
//                break;
//            case 2://六节点三角形单元
//                ek = elem_plane02(ek, ISW, current_elem);
//                break;
//            case 3://十节点三角形单元
//                ek = elem_plane03(ek, ISW, current_elem);
//                break;
//            case 4://四节点四边形单元
//                ek = elem_plane04(ek, ISW, current_elem);
//                break;
//            case 5://十节点四边形单元
//                ek = elem_plane05(ek, ISW, current_elem);
//                break;
//            default:
//                break;
//        }
//        return ek;
//    }
//
//    private double[][] elem_plane01(double[][] ek, int ISW, int current_elem) {
//        switch (ISW) {
//            case 1:
//                ek = makePlane01k(ek, current_elem);
//                break;
//            case 2:
//                ek = plane01_equiLoad01(ek, current_elem);
//                break;
//            case 3:
//                ek = plane01_equiLoad02(ek, current_elem);
//                break;
//            case 4:
//                ek = getPlane01Stress(ek, current_elem);
//                break;
//            case 5:
//                ek = getPlane01SM(ek, current_elem);
//                break;
//            case 6:
//                ek = getPlane01RH(ek, current_elem);
//                break;
//            default:
//                break;
//        }
//        return ek;
//    }
//
//    //程序5.6 计算三节点三角形单刚子程序
//    private double[][] makeplane01k(double[][] ek, int current_elem) {
//        int i;
//        int j;
//        int m;
//        double[] x = new double[3];
//        double[] y = new double[3];
//        double[] a = new double[3];
//        double[] b = new double[3];
//        double[] c = new double[3];
//        double si;
//        double delta;
//        int mate_num;
//        double factor;
//        int iType;
//        double emod;
//        double possion;
//        double thick;
//        i = current_elem;
//        x[0] = coor[elem[i].getNodeList(0)].getX();
//        y[0] = coor[elem[i].getNodeList(0)].getY();
//        x[1] = coor[elem[i].getNodeList(1)].getX();
//        y[1] = coor[elem[i].getNodeList(1)].getY();
//        x[2] = coor[elem[i].getNodeList(2)].getX();
//        y[2] = coor[elem[i].getNodeList(2)].getY();
//        delta = (x[0] * y[1] + x[1] * y[2] + x[2] * y[0]);
//        delta = delta - (y[0] * x[1] + y[1] * x[2] + y[2] * x[0]);
//        delta = delta * 0.5;
//        //计算中间参数
//        for (i = 0; i < 3; i++) {
//            if (i == 0) {
//                j = 1;
//                m = 2;
//            } else if (i == 1) {
//                j = 2;
//                m = 0;
//            } else if (i == 2) {
//                j = 0;
//                m = 1;
//            }
//            a[i]=x[j]*y[m]-x[m]*y[j];
//            b[i]=y[j]-y[m];
//            c[i]=x[m]-x[j];
//        }
//        //确定材料参数
//        
//        return ek;
//    }
//}
