/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package joggle;

import common.openfvm.MshElement;
import common.openfvm.MshFace;
import common.openfvm.OctNode;
import common.Parameter;
import common.openfvm.BcdSurface;
import common.openfvm.BcdType;
import common.openfvm.BcdVolume;
import common.openfvm.MatMaterial;
import common.openfvm.MatTherm;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.System.exit;
import javax.swing.JOptionPane;
import point.Point3D;

/**
 *
 * @author Administrator
 */
public class ReadFileofOpenFVM {

    private File file;
    private String fileName = "tutorial01";
    private File parFile;
    private File mshFile;
    private File bcdFile;
    private File mtlFile;
    private Parameter parameter;
    private Point3D[] nodes;
    private MshFace[] patches;
    private MshElement[] elements;
    private MshFace[] faces;
    private int[] nod_correlation;
    private int[] ele_correlation;
    BcdSurface[] bcsurfaces = null;
    BcdVolume[] bcvolumes = null;

    int n;

    int inull;

    int tcode;
    int nbpar;

    int nphi = 6;//位于globals.h中

    int nbfaces = 0;
    int nbpatches = 0;
    int nbelements = 0;
    int nbbcsurfaces = 0;
    int nbbcvolumes = 0;

    MatMaterial material;

    public ReadFileofOpenFVM(File file) {
        this.file = file;
        parameter = new Parameter();
        setDefaults();
        fileName = file.getName();
        //判断以文件夹命名的计算文件是否存在和齐全
        File[] files = file.listFiles();
        boolean flag1 = false;//标记fileName+".par"文件是否存在
        boolean flag2 = false;//同上，标记".msh"文件是否存在
        boolean flag3 = false;//同上，标记".bcd"文件是否存在
        boolean flag4 = false;//同上，标记".mtl"文件是否存在
        for (int i = 0; i <= files.length - 1; i++) {
            if (files[i].getName().equals(fileName + ".par")) {
                flag1 = true;
            }
            if (files[i].getName().equals(fileName + ".msh")) {
                flag2 = true;
            }
            if (files[i].getName().equals(fileName + ".bcd")) {
                flag3 = true;
            }
            if (files[i].getName().equals(fileName + ".mtl")) {
                flag4 = true;
            }
        }
        if (!(flag1 && flag2 && flag3 && flag4)) {
            JOptionPane.showMessageDialog(null, "计算文件不全！",
                    "错误", JOptionPane.ERROR_MESSAGE);
            Thread.currentThread().interrupt();//结束当前进程
        }

        if (!Thread.currentThread().isInterrupted()) {
            this.parFile = new File(file, fileName + ".par");
            this.mshFile = new File(file, fileName + ".msh");
            this.bcdFile = new File(file, fileName + ".bcd");
            this.mtlFile = new File(file, fileName + ".mtl");
            readParFile();
            readMshFile();
            readBcdFile();
            readMtlFile();
        }

    }

    private void readBcdFile() {
        int i, j, n;
        int inull;
        int tcode;
        int nbbcd;

        try {
            InputStreamReader fr = new InputStreamReader(
                    new FileInputStream(this.mshFile), "GBK");
            BufferedReader in = new BufferedReader(fr);
            String line = null;
            String object[];
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("$Boundary")) {
                    object = line.split(" ", 2);
                    object[1] = object[1].trim();
                    object = object[1].split(" ", 2);
                    inull = Integer.parseInt(object[0]);
                    object[1] = object[1].trim();
                    object = object[1].split(" ", 2);
                    nbbcd = Integer.parseInt(object[0]);

                    line = in.readLine().trim();
                    for (i = 0; i < nbbcd; i++) {
                        object = line.split(" ", 2);
                        tcode = Integer.parseInt(object[0]);
                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        n = Integer.parseInt(object[0]);
                        switch (tcode) {
                            case 10000:
                                //Empty - physical surfaces
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }
                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.EMPTY);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10020:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }
                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.CYCLIC);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10040:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }
                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.PERMEABLE);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10050:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }
                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.OPEN);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10100:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.INLET);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10110:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.PRESSUREINLET);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10150:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.OUTLET);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10170:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.WALL);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10180:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.ADIABATICWALL);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10190:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.MOVINGWALL);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10200:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.SURFACE);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 10250:
                                if (bcvolumes == null) {
                                    bcvolumes = new BcdVolume[nbbcvolumes + n];
                                } else {
                                    BcdVolume[] tmpBcd = bcvolumes.clone();
                                    bcvolumes = new BcdVolume[nbbcvolumes + n];
                                    System.arraycopy(tmpBcd, 0, bcvolumes, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcvolumes[nbbcvolumes + j].setBc(BcdType.VOLUME);
                                    getEntryVolume(in, j);
                                }
                                nbbcvolumes = nbbcvolumes + n;
                                break;
                            case 11000:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.EMPTY);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 11040:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.CONSTRAINT);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 11050:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.CONSTRAINTU);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 11060:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.CONSTRAINTV);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 11070:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    for (int ii = 0; ii < tmpBcd.length; ii++) {
                                        bcsurfaces[ii] = tmpBcd[ii];
                                    }
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.CONSTRAINTW);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 11080:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.PRESSURE);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 11200:
                                if (bcsurfaces == null) {
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                } else {
                                    BcdSurface[] tmpBcd = bcsurfaces.clone();
                                    bcsurfaces = new BcdSurface[nbbcsurfaces + n];
                                    System.arraycopy(tmpBcd, 0, bcsurfaces, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcsurfaces[nbbcsurfaces + j].setBc(BcdType.SURFACE);
                                    getEntrySurface(in, j);
                                }
                                nbbcsurfaces = nbbcsurfaces + n;
                                break;
                            case 11250:
                                if (bcvolumes == null) {
                                    bcvolumes = new BcdVolume[nbbcvolumes + n];
                                } else {
                                    BcdVolume[] tmpBcd = bcvolumes.clone();
                                    bcvolumes = new BcdVolume[nbbcvolumes + n];
                                    System.arraycopy(tmpBcd, 0, bcvolumes, 0, tmpBcd.length);
                                }

                                for (j = 0; j < n; j++) {
                                    bcvolumes[nbbcvolumes + j].setBc(BcdType.VOLUME);
                                    getEntryVolume(in, j);
                                }
                                nbbcvolumes = nbbcvolumes + n;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEntrySurface(BufferedReader in, int j) throws Exception {
        int ival;

        String gs;

        String line = in.readLine().trim();
        String[] object = line.split(" ", 2);
        ival = Integer.parseInt(object[0]);

        bcsurfaces[nbbcsurfaces + j].setPhysreg(ival);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        gs = object[0];
        bcsurfaces[nbbcsurfaces + j].setFu(gs);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        gs = object[0];
        bcsurfaces[nbbcsurfaces + j].setFv(gs);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        gs = object[0];
        bcsurfaces[nbbcsurfaces + j].setFw(gs);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        gs = object[0];
        bcsurfaces[nbbcsurfaces + j].setFp(gs);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        gs = object[0];
        bcsurfaces[nbbcsurfaces + j].setfT(gs);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        gs = object[0];
        bcsurfaces[nbbcsurfaces + j].setFs(gs);
    }

    private void getEntryVolume(BufferedReader in, int j) throws IOException {
        int ival;
        String gs;
        String line = in.readLine().trim();
        String[] object = line.split(" ", 2);
        ival = Integer.parseInt(object[0]);
        bcvolumes[nbbcvolumes + j].setPhysreg(ival);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        bcvolumes[nbbcvolumes + j].setFu(object[0]);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        bcvolumes[nbbcvolumes + j].setFv(object[0]);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        bcvolumes[nbbcvolumes + j].setFw(object[0]);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        bcvolumes[nbbcvolumes + j].setFp(object[0]);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        bcvolumes[nbbcvolumes + j].setfT(object[0]);

        object[1] = object[1].trim();
        object = object[1].split(" ", 2);
        bcvolumes[nbbcvolumes + j].setFs(object[0]);

    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void readParFile() {

        try {
            InputStreamReader fr = new InputStreamReader(
                    new FileInputStream(this.parFile), "GBK");
            BufferedReader in = new BufferedReader(fr);
            String line = null;
            while ((line = in.readLine()) != null) {
                if (line.trim().equals("")) {
                    //表示空行，忽略
                } else if (line.startsWith("$Parameter")) {
                    //此处从line中分离出"$Parameter"后的两个数字，赋值给inull和nbpar
                    String[] object = line.split(" ", 2);
                    object[1] = object[1].trim();
                    object = object[1].split(" ", 2);
                    inull = Integer.parseInt(object[0]);
                    object[1] = object[1].trim();
                    object = object[1].split(" ", 2);
                    nbpar = Integer.parseInt(object[0]);
                    for (int i = 0; i <= nbpar - 1; i++) {
                        //将line中的前两个数字分离出来分别赋值给tcode和n
                        line = in.readLine();
                        line = line.trim();
                        object = line.split(" ", 2);
                        tcode = Integer.parseInt(object[0]);
                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        n = Integer.parseInt(object[0]);
                        float[] tmpFloat6 = new float[6];
                        float[] tmpFloat3 = new float[3];
                        float tmpfloat;
                        int[] tmpInt6 = new int[6];
                        switch (tcode) {

                            case 30001:
                                break;
                            case 30003:
                                break;
                            case 30005:
                                //对流插值格式
                                line = in.readLine();
                                line = line.trim();
                                object = line.split(" ", 2);
                                tmpInt6[0] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[1] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[2] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[3] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[4] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[5] = Integer.parseInt(object[0]);

                                parameter.setScheme(tmpInt6);
                                break;
                            case 30009:
                                break;
                            case 30010:
                                break;
                            case 30020:
                                //binary output
                                line = in.readLine();
                                line = line.trim();
                                int tempInt = Integer.parseInt(
                                        (line.split(" ", 2))[0]);
                                parameter.setWbinary(tempInt);
                                if (tempInt == 1) {
                                    System.out.println("结果保存为二进制文件\\n");
                                } else {
                                    System.out.println("结果保存为文本文件\\n");
                                }
                                break;
                            case 30030:
                                break;
                            case 30031:
                                break;
                            case 30040:
                                //calculate nth variable
                                line = in.readLine();
                                tmpInt6 = new int[6];
                                line = line.trim();
                                object = line.split(" ", 2);
                                tmpInt6[0] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[1] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[2] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[3] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[4] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[5] = Integer.parseInt(object[0]);
                                parameter.setCalc(tmpInt6);
                                break;
                            case 30090:
                                break;
                            case 30100:
                                //steady state
                                line = in.readLine();
                                line = line.trim();
                                object = line.split(" ", 2);
                                tempInt = Integer.parseInt(object[0]);
                                parameter.setSteady(tempInt);
                                if (tempInt == 1) {
                                    System.out.println("Calculate until steady state is reached.\\n");
                                } else {
                                    System.out.println("Stop simulation at end time.\\n");
                                }
                                System.out.println("");
                                break;
                            case 30105:
                                // Convergence criterion for steady state    
                                line = in.readLine();
                                line = line.trim();
                                object = line.split(" ", 2);
                                tmpFloat6[0] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[1] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[2] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[3] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[4] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[5] = Float.parseFloat(object[0]);

                                parameter.setFtol(tmpFloat6);

                                break;
                            case 30200:
                                // Adjust time interval

                                line = in.readLine().trim();
                                tempInt = Integer.parseInt(line.split(" ")[0]);
                                parameter.setAdjdt(tempInt);

                                if (tempInt == 1) {
                                    System.out.println("Adjust time intervals.\n");
                                } else {
                                    System.out.println("Do not adjust time intervals.\n");
                                }

                                break;
                            case 30201:
                                line = in.readLine();
                                line = line.trim();
                                tmpfloat = Float.parseFloat(line.split(" ")[0]);
                                parameter.setMaxCp(tmpfloat);
                                System.out.println("Maximum Courant number: \t\t\t"
                                        + tmpfloat);
                                break;
                            case 30400:
                                //number of saves
                                line = in.readLine();
                                line = line.trim();
                                tempInt = Integer.parseInt(line.split(" ")[0]);
                                parameter.setNsav(tempInt);
                                System.out.println("保存次数：" + tempInt);
                                System.out.println("");
                                break;
                            case 30440:

                                break;
                            case 30450:
                                //write face scalars
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                tmpInt6[0] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[1] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[2] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[3] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[4] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[5] = Integer.parseInt(object[0]);

                                parameter.setFsav(tmpInt6);

                                System.out.println("Variable: \\t\\t\\t\\t\\t[ u v w p T s]\\n");
                                System.out.println("Save scalars on face: \\t\\t\\t\\t[");
                                for (int j = 0; j < nphi; j++) {
                                    System.out.print(parameter.getFsav()[j] + " ");
                                }
                                System.out.println("]\n");
                                break;
                            case 30455:
                                //write face factors
                                line = in.readLine().trim();
                                tempInt = Integer.parseInt(line.split(" ")[0]);
                                parameter.setFvec(tempInt);
                                if (tempInt == 1) {
                                    System.out.println("Save face - vector magnitude: \\t\\t\\t[yes]\\n");
                                } else {
                                    System.out.println("Save face - vector magnitude: \t\t\t[no]\n");
                                }
                                break;
                            case 30460:
                                //write element scalars(u v w p T s)

                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                tmpInt6[0] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[1] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[2] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[3] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[4] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[5] = Integer.parseInt(object[0]);

                                parameter.setCsav(tmpInt6);
                                break;
                            case 30465:
                                //Write vorticity (x y z)
                                line = in.readLine();
                                line = line.trim();
                                tempInt = Integer.parseInt(line.split(" ")[0]);
                                parameter.setCvec(tempInt);
                                if (tempInt == 1) {
                                    System.out.println("Save cell center - vector: \t\t\t[yes]");
                                } else {
                                    System.out.println("Save cell center - vector: \t\t\t[no]");
                                }
                                break;

                            case 30470:

                                // Write vorticity (x y z)
                                line = in.readLine().trim();
                                int[] tmpInt3 = new int[3];
                                object = line.split(" ", 2);
                                tmpInt3[0] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt3[1] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt3[2] = Integer.parseInt(object[0]);

                                parameter.setVortex(tmpInt3);

                                System.out.println("Axis: \t\t\t\t\t\t[ x y z]");
                                System.out.println("Save vorticity: \t\t\t\t[");
                                for (int j = 0; j < 3; j++) {
                                    System.out.print(tmpInt3[j] + " ");
                                }
                                System.out.println("]");

                                break;

                            case 30475:
                                // Write stream function (xy)
                                line = in.readLine().trim();
                                tempInt = Integer.parseInt(line.split(" ")[0]);
                                parameter.setStreamf(tempInt);
                                if (tempInt == 1) {
                                    System.out.println("Save stream function in xy plane\n");
                                } else {
                                    System.out.println("Do not save stream function in xy plane\n");
                                }

                                break;

                            case 30485:
                                // Probe (u v w p T s)
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                tmpInt6[0] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[1] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[2] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[3] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[4] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[5] = Integer.parseInt(object[0]);

                                parameter.setProbe(tmpInt6);
                                System.out.println("Variable: \t\t\t\t\t[ u v w p T s]");
                                System.out.print("Probe options: \t\t\t\t\t[");
                                for (int j = 0; j < nphi; j++) {
                                    System.out.print(tmpInt6[j]);
                                }
                                System.out.println("]");

                                break;

                            case 30500:

                                break;

                            case 30550:
                                // Maximum number of non-othorgonal corrections
                                line = in.readLine().trim();
                                tempInt = Integer.parseInt(line.split(" ")[0]);
                                parameter.setNorthocor(tempInt);

                                System.out.println("Maximum number of "
                                        + "non-orthogonal corrections: \t"
                                        + tempInt);
                                break;

                            case 30551:
                                // Orthogonal factor
                                line = in.readLine().trim();
                                tmpfloat = Float.parseFloat(line.split(" ")[0]);
                                parameter.setOrthof(tmpfloat);

                                System.out.println("Orthogonal factor: \t\t\t\t"
                                        + tmpfloat);
                                break;

                            case 30600:
                                // Convergence criterion (matrix solution) 
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                tmpFloat6[0] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[1] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[2] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[3] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[4] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat6[5] = Float.parseFloat(object[0]);

                                parameter.setMtol(tmpFloat6);

                                System.out.println("Variable: \t\t\t\t\t[ u v w p T s]");
                                System.out.print("Matrix solution tolerance: \t\t\t[");
                                for (int j = 0; j < nphi; j++) {
                                    System.out.println(tmpFloat6[j] + " ");
                                }
                                System.out.println("]");
                                break;

                            case 30601:
                                // Maximum number of iterations (matrix solution)
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                tmpInt6[0] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[1] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[2] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[3] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[4] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[5] = Integer.parseInt(object[0]);

                                parameter.setMiter(tmpInt6);

                                System.out.println("Variable: \t\t\t\t\t[ u v w p T s]");
                                System.out.print("Matrix solution iterations: \t\t\t[");
                                for (int j = 0; j < nphi; j++) {
                                    System.out.println(tmpInt6[j] + " ");
                                }
                                System.out.println("]");
                                break;

                            case 30650:
                                // Matrix solver (u v w p T s) (0-NONE, 1-JACOBI, 2-SOR, 3-QMR, 4-GMRES, 5-CG, 6-CGN, 7-CGS, 8-BICG, 9-BICGS)
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                tmpInt6[0] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[1] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[2] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[3] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[4] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[5] = Integer.parseInt(object[0]);

                                parameter.setMsolver(tmpInt6);

                                System.out.println("Variable: \t\t\t\t\t[ u v w p T s]");
                                System.out.println("Solver: c) \t\t\t\t\t[");
                                for (int j = 0; j < nphi; j++) {
                                    System.out.println(tmpInt6[j] + " ");
                                }
                                System.out.println("]\n");
                                break;

                            case 30651:

                                // Matrix preconditioner (0-NONE, 1-JACOBI, 2-SOR, 3-ILU)
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                tmpInt6[0] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[1] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[2] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[3] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[4] = Integer.parseInt(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpInt6[5] = Integer.parseInt(object[0]);
                                parameter.setMprecond(tmpInt6);

                                System.out.println("Variable: \t\t\t\t\t[ u v w p T s]");
                                System.out.print("Pre-conditioner: d) \t\t\t\t[");
                                for (int j = 0; j < nphi; j++) {
                                    System.out.println(tmpInt6[j] + " ");
                                }
                                System.out.println("]\n");
                                break;

                            case 30800:
                                // Interface scheme factor - CICSAM
                                line = in.readLine().trim();
                                tmpfloat = Float.parseFloat(line.split(" ")[0]);
                                parameter.setKq(tmpfloat);

                                System.out.println("Interface scheme factor: \t\t\t"
                                        + tmpfloat);
                                break;

                            case 30900:

                                // Maximum number of CICSAM corrections
                                line = in.readLine().trim();
                                tempInt = Integer.parseInt(line.split(" ")[0]);
                                parameter.setNcicsamcor(tempInt);

                                System.out.println("Number of CICSAM corrections: \t\t\t"
                                        + tempInt);
                                break;

                            case 32000:

                                // Start time
                                line = in.readLine().trim();
                                tmpfloat = Float.parseFloat(line.split(" ")[0]);
                                parameter.setT0(tmpfloat);

                                System.out.println("Start time: \t\t\t\t\t"
                                        + tmpfloat);
                                break;

                            case 32001:

                                // End time
                                // End time
                                line = in.readLine().trim();
                                tmpfloat = Float.parseFloat(line.split(" ")[0]);
                                parameter.setT1(tmpfloat);

                                System.out.println("End time: \t\t\t\t\t"
                                        + tmpfloat);
                                break;

                            case 32002:

                                // Time interval
                                line = in.readLine().trim();
                                tmpfloat = Float.parseFloat(line.split(" ")[0]);
                                parameter.setDt(tmpfloat);
                                System.out.println("Time interval: \t\t\t\t\t"
                                        + tmpfloat);
                                break;

                            case 33050:

                                // Simulate filling
                                break;

                            case 33051:

                                // Completely filled percentage
                                break;

                            case 34000:
                                // Gravity vector
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                tmpFloat3[0] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                tmpFloat3[1] = Float.parseFloat(object[0]);

                                object[1] = object[1].trim();
                                object = object[1].split(" ");
                                tmpFloat3[2] = Float.parseFloat(object[0]);

                                parameter.setG(tmpFloat3);

                                System.out.println("Gravity vector (x): \t\t\t\t%+.3E m/s^2\n"
                                        + tmpFloat3[0]);
                                System.out.println("Gravity vector (x): \t\t\t\t%+.3E m/s^2\n"
                                        + tmpFloat3[1]);
                                System.out.println("Gravity vector (x): \t\t\t\t%+.3E m/s^2\n"
                                        + tmpFloat3[2]);

                                break;

                            case 36000:

                                // Interior BCs
                                break;

                            default:
                                System.out.println("\\nError: Unknown parameter code(" + tcode + ").");
                                exit(0);
                                break;
                        }
                    }
                }

            }
            in.close();
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }

    }

    private void readMshFile() {

        int nnod;
        int nele;

        int eindex;
        int maxindex;
        int etype;

        int ntags;
        int physreg, elemreg;

        int npartitions;
        int partition;

        int ival = 0;

        float fval;

        try {
            InputStreamReader fr = new InputStreamReader(
                    new FileInputStream(this.mshFile), "GBK");
            BufferedReader in = new BufferedReader(fr);
            String line = null;
            maxindex = 0;
            String object[];
            int tmpInt;
            double tmpDouble3[] = new double[3];
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("$Nodes")) {
                    line = in.readLine().trim();
                    nnod = Integer.parseInt(line.split(" ")[0]);
                    nodes = new Point3D[nnod];
                    nod_correlation = new int[nnod * 2];
                    for (int i = 0; i < nnod; i++) {
                        line = in.readLine().trim();
                        object = line.split(" ", 2);
                        tmpInt = Integer.parseInt(object[0]);

                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        tmpDouble3[0] = Double.parseDouble(object[0]);

                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        tmpDouble3[1] = Double.parseDouble(object[0]);

                        object[1] = object[1].trim();
                        object = object[1].split(" ");
                        tmpDouble3[2] = Double.parseDouble(object[0]);

                        nodes[i] = new Point3D(tmpInt, tmpDouble3[0],
                                tmpDouble3[1], tmpDouble3[2]);
                        if (tmpInt > nod_correlation.length) {
                            int tempArr[];
                            tempArr = nod_correlation.clone();
                            nod_correlation = new int[tmpInt + 1];
                            for (int j = 0; j <= nod_correlation.length - 1; j++) {
                                nod_correlation[j] = tempArr[j];
                            }
                            nod_correlation[tmpInt] = i;
                        } else {
                            nod_correlation[tmpInt] = i;//表示网格文件中的id为tmpInt的点的坐标存储在数组node的第i个位置
                        }
                    }
                }
                if (line.startsWith("$Elements")) {
                    line = in.readLine().trim();
                    nele = Integer.parseInt(line.split(" ")[0]);
                    patches = new MshFace[nele];
                    elements = new MshElement[nele];
                    for (int i = 0; i < nele; i++) {
                        patches[i] = new MshFace();
                        elements[i] = new MshElement();
                    }

                    for (int i = 0; i < nele; i++) {
                        line = in.readLine().trim();
                        object = line.split(" ", 2);
                        eindex = Integer.parseInt(object[0]);

                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        etype = Integer.parseInt((object[0]));

                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        ntags = Integer.parseInt(object[0]);//该值目前只能为3

                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        physreg = Integer.parseInt(object[0]);

                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        elemreg = Integer.parseInt(object[0]);

                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        partition = Integer.parseInt(object[0]);

                        if (etype == 2 || etype == 3) {

                            if (etype == 2) {
                                ival = 3;

                            } else if (etype == 3) {
                                ival = 4;
                            }

                            patches[nbpatches].setNbnodes(ival);
                            //下面求出给面中心点的坐标，并存入cface中
                            patches[nbpatches].setCface(new Point3D(-1, 0, 0, 0));

                            patches[nbpatches].setNodeSize(patches[nbpatches].getNbnodes());

                            for (int j = 0; j < patches[nbpatches].getNbnodes(); j++) {
                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                ival = Integer.parseInt(object[0]);

                                patches[nbpatches].setNode(j, nod_correlation[ival]);

                                patches[nbpatches].setCface(new Point3D(-1,
                                        nodes[patches[nbpatches].getNode(j)].getX()
                                        + patches[nbpatches].getCface().getX(),
                                        nodes[patches[nbpatches].getNode(j)].getY()
                                        + patches[nbpatches].getCface().getY(),
                                        nodes[patches[nbpatches].getNode(j)].getZ()
                                        + patches[nbpatches].getCface().getZ()));
                            }
                            int itemp = patches[nbpatches].getNbnodes();
                            patches[nbpatches].setCface(new Point3D(-1,
                                    patches[nbpatches].getCface().getX() / itemp,
                                    patches[nbpatches].getCface().getY() / itemp,
                                    patches[nbpatches].getCface().getZ() / itemp));
                            //至此，中心点坐标求出，并已经存入cface中
                            patches[nbpatches].setPhysreg(physreg);
                            patches[nbpatches].setElemreg(elemreg);
                            patches[nbpatches].setElement(-1);
                            patches[nbpatches].setPair(-1);
                            patches[nbpatches].setBc(-1);
                            if (etype == 2) {
                                patches[nbpatches].setType(1);//三角形
                            } else if (etype == 3) {
                                patches[nbpatches].setType(2);//四边形
                            }
                            patches[nbpatches].setIndex(eindex);

                            if (nbelements == 0) {
                                maxindex = eindex;
                            }
                            nbpatches++;
                        }
                        if (etype >= 4 && etype <= 6) {
                            //立体单元
                            if (etype == 4) {
                                ival = 4;
                            } else if (etype == 5) {
                                ival = 8;
                            } else if (etype == 6) {
                                ival = 6;
                            }

                            elements[nbelements].setNbnodes(ival);
                            elements[nbelements].setCelement(new Point3D(-1, 0, 0, 0));

                            elements[nbelements].setNodeSize(ival);
                            for (int j = 0; j < elements[nbelements].getNbnodes(); j++) {
                                object[1] = object[1].trim();
                                object = object[1].split(" ", 2);
                                ival = Integer.parseInt(object[0]);

                                elements[nbelements].setNode(j, nod_correlation[ival]);

                                elements[nbelements].setCelement(new Point3D(-1,
                                        nodes[elements[nbelements].getNode(j)].getX()
                                        + elements[nbelements].getCelement().getX(),
                                        nodes[elements[nbelements].getNode(j)].getY()
                                        + elements[nbelements].getCelement().getY(),
                                        nodes[elements[nbelements].getNode(j)].getZ()
                                        + elements[nbelements].getCelement().getZ()
                                ));
                            }
                            int itemp = elements[nbelements].getNbnodes();
                            elements[nbelements].setCelement(new Point3D(-1,
                                    elements[nbelements].getCelement().getX() / itemp,
                                    elements[nbelements].getCelement().getY() / itemp,
                                    elements[nbelements].getCelement().getZ() / itemp
                            ));
                            elements[nbelements].setPhysreg(physreg);
                            elements[nbelements].setElemreg(elemreg);
                            if (etype == 4) {
                                elements[nbelements].setType(3);//tetrahedron
                            } else if (etype == 5) {
                                elements[nbelements].setType(4);//hexahedron
                            } else if (etype == 6) {
                                elements[nbelements].setType(5);//prism
                            }
                            elements[nbelements].setIndex(eindex);
                            nbelements++;

                        }

                        if (etype >= 7) {
                            JOptionPane.showMessageDialog(null, "未知单元类型！");
                        }

                    }
                }
            }
            in.close();
            for (int i = 0; i < nbpatches; i++) {
                if (patches[i].getIndex() > maxindex) {
                    patches[i].setBc(3);//PROCESSOR
                }
            }
            MshCreateFaces();
            MshAddBoundaryFaces();
            MshConnectFaces();
//            MshRemoveBoundaryFaces();
//            MshConnectFaces();
//            MshInteriorFaces();
//            MshCalcPropFaces();
            if (nbelements == 0) {
                System.out.println("Error: No solid elements.");
                exit(0);
            }

//            MshCalcPropMesh();
//            MshGetElementTypes();
//            MshCorrectNonOrthogonality();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void readMtlFile() {
        int i, n;

        int inull;

        int tcode;
        int nbmat;

        String gs;
        try {
            InputStreamReader fr = new InputStreamReader(
                    new FileInputStream(this.mshFile), "GBK");
            BufferedReader in = new BufferedReader(fr);
            String line = null;
            String object[];
            while ((line = in.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("$Material")) {
                    object = line.split(" ", 2);

                    object[1] = object[1].trim();
                    object = object[1].split(" ", 2);
                    inull = Integer.parseInt(object[0]);

                    object[1] = object[1].trim();
                    object = object[1].split(" ", 2);
                    nbmat = Integer.parseInt(object[0]);

                    for (i = 0; i < nbmat; i++) {
                        line = in.readLine().trim();
                        object = line.split(" ", 2);
                        tcode = Integer.parseInt(object[0]);

                        object[1] = object[1].trim();
                        object = object[1].split(" ", 2);
                        n = Integer.parseInt(object[0]);
                        switch (tcode) {
                            case 20012://可压缩性
                                line = in.readLine().trim();
                                double psi0 = Double.parseDouble(line);
                                material.setPsi(0, psi0);
                                break;
                            case 20015://密度
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.setDens(0, object[0]);
                                break;
                            case 20021://粘度
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.setVisc(0, object[0]);
                                break;
                            case 20022://比热
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.getTherm()[0].setSpheat(object[0]);
                                break;
                            case 20023://热导率
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.getTherm()[0].setThcond(object[0]);
                                break;
                            case 20050://弹性模量
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.getMech()[0].setElastmod(object[0]);
                                break;
                            case 20051://泊松比
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.getMech()[0].setPoisson(
                                        Double.parseDouble(object[0]));
                                break;
                            case 21012://可压缩性
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.setPsi(1, Double.parseDouble(object[0]));
                                break;
                            case 21015:
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.setDens(1, object[0]);
                                break;
                            case 21021:
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.setVisc(1, object[0]);
                                break;
                            case 21022:
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.getTherm()[1].setSpheat(object[0]);
                                break;
                            case 21023:
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.getTherm()[1].setThcond(object[0]);
                                break;
                            case 21050:
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.getMech()[1].setElastmod(object[0]);
                                break;
                            case 21051:
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.getMech()[1].setPoisson(Double.parseDouble(object[0]));
                                break;
                            case 26050:
                                //constant surface tension(fluid0/fluid1)
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.setTens(Double.parseDouble(object[0]));
                                break;
                            case 26060:
                                //热导率（边界）
                                line = in.readLine().trim();
                                object = line.split(" ", 2);
                                material.setBthcond(Double.parseDouble(object[0]));
                                break;
                            default:
                                break;
                        }
                    }
                } else if (line.startsWith("$EndFile")) {
                    return;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void MshAddBoundaryFaces() {
        nbfaces = nbfaces + nbpatches;//等号右边的nbfaces是faces数组目前实际有效的长度
        //下面将faces重新定义长度为新的nbfaces,需要将其前面有效数据拷贝到新的faces数组中
        MshFace[] tmpMshFace = faces.clone();//首先复制旧的faces数组
        faces = new MshFace[nbfaces];//新建长度为nbfaces的数组
        for (int i = 0; i < nbfaces - nbpatches; i++) {//将旧的faces数组中的有效数据放入新的faces数组中
            faces[i] = tmpMshFace[i];
        }
        //至此，faces数组重新定义长度完成

        for (int i = nbfaces - nbpatches; i < nbfaces; i++) {
            faces[i] = patches[i - nbfaces + nbpatches];
        }
    }

    private void MshConnectFaces() {
        double[] min = new double[3];
        double[] max = new double[3];

        int nb;
        int nbv1, nbv2;
        int face, anotherFace;
        double dist;

        Point3D[] tab;
        //Find pairs of all faces
        //Boundary faces have no pair:-1
        //Create an octree with face cfaces

        if (nbfaces == 0) {
            System.out.println("没有网格数据！");
        }
        tab = new Point3D[nbfaces];
        min[0] = faces[0].getCface().getX();
        min[1] = faces[0].getCface().getY();
        min[2] = faces[0].getCface().getZ();

        max[0] = faces[0].getCface().getX();
        max[1] = faces[1].getCface().getY();
        max[2] = faces[2].getCface().getZ();

        for (int i = 0; i < nbfaces; i++) {
            tab[i] = new Point3D(-1,
                    faces[i].getCface().getX(),
                    faces[i].getCface().getY(),
                    faces[i].getCface().getZ());

            min[0] = Math.min(min[0], faces[i].getCface().getX());
            min[1] = Math.min(min[1], faces[i].getCface().getY());
            min[2] = Math.min(min[2], faces[i].getCface().getZ());

            max[0] = Math.max(max[0], faces[i].getCface().getX());
            max[1] = Math.max(max[1], faces[i].getCface().getY());
            max[2] = Math.max(max[2], faces[i].getCface().getZ());
        }
        //至此，min中存储的是所有面中心点坐标的最小值，max中存储的是相应的最大值
        //tab存储的是所有面中心点的坐标值
        OctCreateOctree(min, max, tab, nbfaces);

//        for (int i = 0; i < nbleafs; i++) {
//
//        }

    }

    private void OctCreateOctree(double[] min, double[] max,
            Point3D[] tab, int nbentities) {
        int nbleafs = 0;
        int nbpointers = 0;
        double EPSILON = 1e-6;

        min[0] = min[0] - EPSILON;
        max[0] = min[0] + EPSILON;
        min[1] = min[1] - EPSILON;
        max[1] = max[1] + EPSILON;
        min[2] = min[2] - EPSILON;
        max[2] = max[2] + EPSILON;

        OctNode[] root = new OctNode[1];

        root[0].setEntitiesSize(nbentities);

        root[0].setXmin(min[0]);
        root[0].setXmax(max[0]);
        root[0].setYmin(min[1]);
        root[0].setYmax(max[1]);
        root[0].setZmin(min[2]);
        root[0].setZmax(max[2]);

        root[0].setXmid((min[0] + max[0]) / 2.0);
        root[0].setYmid((min[1] + max[1]) / 2.0);
        root[0].setZmid((min[2] + max[2]) / 2.0);

        for (int i = 0; i < nbentities; i++) {
            root[0].setEntities(i, i);
        }

        OctCreateRecursive(root, tab, nbentities);
    }

    private void OctCreateRecursive(OctNode[] node, Point3D[] tab, int nbentities) {
        int[] ok = new int[8];

        int numberOfTheNode;

        double xm;
        double xp;
        double ym;
        double yp;
        double zm;
        double zp;

        for (int i = 0; i < 8; i++) {
            node[0].getNodes(i).setEntitiesSize(node[0].getNbentities());
            node[0].getNodes(i).setNbentities(0);
        }
    }

    public void MshCreateFaces() {
        int element;
        Point3D d;
        faces = new MshFace[nbelements * 8];//???为什么是单元数的8倍

        for (int i = 0; i < nbelements; i++) {
            element = i;
            if (elements[element].getType() == 1) {//type==1是TRIANGLE
                elements[element].setNbfaces(1);

                elements[element].setFaceSize(elements[element].getNbfaces());
                elements[element].setFace(0, nbfaces);
                faces[nbfaces].setType(1);//TRIANGLE
                faces[nbfaces].setNbnodes(3);

                faces[nbfaces].setNodeSize(faces[nbfaces].getNbnodes());

                faces[nbfaces].setNode(0, elements[element].getNode(0));
                faces[nbfaces].setNode(1, elements[element].getNode(1));
                faces[nbfaces].setNode(2, elements[element].getNode(2));

                //Calculate area
                faces[nbfaces].setAj(getTriArea(nodes[faces[nbfaces].getNode(0)],
                        nodes[faces[nbfaces].getNode(1)],
                        nodes[faces[nbfaces].getNode(2)]));
            }
            if (elements[element].getType() == 2) {//QUADRANGLE

                elements[element].setNbfaces(1);

                elements[element].setFaceSize(elements[element].getNbfaces());

                elements[element].setFace(0, nbfaces);

                faces[nbfaces].setType(2);//QUADRANGLE四边形
                faces[nbfaces].setNbnodes(4);//一个面有四个点

                faces[nbfaces].setNodeSize(faces[nbfaces].getNbnodes());

                faces[nbfaces].setNode(0, elements[element].getNode(0));
                faces[nbfaces].setNode(1, elements[element].getNode(1));
                faces[nbfaces].setNode(2, elements[element].getNode(2));
                faces[nbfaces].setNode(3, elements[element].getNode(3));

                //Calculate area
                faces[nbfaces].setAj(getQuadArea(nodes[faces[nbfaces].getNode(0)],
                        nodes[faces[nbfaces].getNode(1)],
                        nodes[faces[nbfaces].getNode(2)],
                        nodes[faces[nbfaces].getNode(3)]));

                //Calculate centroid
            }

            if (elements[element].getType() == 3) {//TETRAHEDRON

            }
            if (elements[element].getType() == 4) {//HEXAHEDRON
                elements[element].setNbfaces(6);

                elements[element].setFaceSize(6);
                for (int j = 0; j < 6; j++) {
                    elements[element].setFace(j, nbfaces);//单元中第j个面在faces数组中的位置是nbfaces
                    faces[nbfaces] = new MshFace();
                    faces[nbfaces].setType(2);//QUADRANGLE四边形
                    faces[nbfaces].setNbnodes(4);
                    switch (j) {
                        case 0:
                            faces[nbfaces].setNodeSize(4);

                            faces[nbfaces].setNode(0, elements[element].getNode(0));
                            faces[nbfaces].setNode(1, elements[element].getNode(1));
                            faces[nbfaces].setNode(2, elements[element].getNode(2));
                            faces[nbfaces].setNode(3, elements[element].getNode(3));
                            break;
                        case 1:
                            faces[nbfaces].setNodeSize(4);

                            faces[nbfaces].setNode(0, elements[element].getNode(7));
                            faces[nbfaces].setNode(1, elements[element].getNode(6));
                            faces[nbfaces].setNode(2, elements[element].getNode(5));
                            faces[nbfaces].setNode(3, elements[element].getNode(4));
                            break;
                        case 2:
                            faces[nbfaces].setNodeSize(4);

                            faces[nbfaces].setNode(0, elements[element].getNode(5));
                            faces[nbfaces].setNode(1, elements[element].getNode(6));
                            faces[nbfaces].setNode(2, elements[element].getNode(2));
                            faces[nbfaces].setNode(3, elements[element].getNode(1));
                            break;
                        case 3:
                            faces[nbfaces].setNodeSize(4);

                            faces[nbfaces].setNode(0, elements[element].getNode(3));
                            faces[nbfaces].setNode(1, elements[element].getNode(7));
                            faces[nbfaces].setNode(2, elements[element].getNode(4));
                            faces[nbfaces].setNode(3, elements[element].getNode(0));
                            break;
                        case 4:
                            faces[nbfaces].setNodeSize(4);

                            faces[nbfaces].setNode(0, elements[element].getNode(0));
                            faces[nbfaces].setNode(1, elements[element].getNode(4));
                            faces[nbfaces].setNode(2, elements[element].getNode(5));
                            faces[nbfaces].setNode(3, elements[element].getNode(1));
                            break;
                        case 5:
                            faces[nbfaces].setNodeSize(4);

                            faces[nbfaces].setNode(0, elements[element].getNode(7));
                            faces[nbfaces].setNode(1, elements[element].getNode(3));
                            faces[nbfaces].setNode(2, elements[element].getNode(2));
                            faces[nbfaces].setNode(3, elements[element].getNode(6));
                            break;

                    }
                    faces[nbfaces].setAj(getQuadArea(nodes[faces[nbfaces].getNode(0)],
                            nodes[faces[nbfaces].getNode(1)],
                            nodes[faces[nbfaces].getNode(2)],
                            nodes[faces[nbfaces].getNode(3)]));

                    faces[nbfaces].setCface(getQuadCentroid(nodes[faces[nbfaces].getNode(0)],
                            nodes[faces[nbfaces].getNode(1)],
                            nodes[faces[nbfaces].getNode(2)],
                            nodes[faces[nbfaces].getNode(3)]));

                    //Calculate normal of the face
                    faces[nbfaces].setN(getNormal(nodes[faces[nbfaces].getNode(0)],
                            nodes[faces[nbfaces].getNode(1)],
                            nodes[faces[nbfaces].getNode(2)]));

                    d = getSubVector(faces[nbfaces].getCface(),
                            elements[element].getCelement());

                    //Normal should point to neighbour
                    //Flip normal if necessary
                    if (getDotVectorVector(faces[nbfaces].getN(), d) < 0.0) {
                        faces[nbfaces].setN(new Point3D(-1,
                                faces[nbfaces].getN().getX() * (-1),
                                faces[nbfaces].getN().getY() * (-1),
                                faces[nbfaces].getN().getZ() * (-1)));
                    }
                    faces[nbfaces].setElement(element);
                    faces[nbfaces].setPhysreg(-1);
                    faces[nbfaces].setElemreg(-1);
                    faces[nbfaces].setPair(-1);
                    faces[nbfaces].setBc(-1);

                    nbfaces++;
                }
            }
        }
    }

    public double getQuadArea(Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
        double area = 0;
        area = area + getTriArea(p1, p2, p3);
        area = area + getTriArea(p1, p3, p4);
        return area;
    }

    public double getTriArea(Point3D p1, Point3D p2, Point3D p3) {
        double area = 0;
        double x1 = p1.getX();
        double y1 = p1.getY();
        double z1 = p1.getZ();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double z2 = p2.getZ();
        double x3 = p3.getX();
        double y3 = p3.getY();
        double z3 = p3.getZ();

        double l_12 = sqrt(pow((x1 - x2), 2) + pow((y1 - y2), 2) + pow((z1 - z2), 2));
        double l_23 = sqrt(pow((x2 - x3), 2) + pow((y2 - y3), 2) + pow((z2 - z3), 2));
        double l_13 = sqrt(pow((x1 - x3), 2) + pow((y1 - y3), 2) + pow((z1 - z3), 2));

        double p = (l_12 + l_23 + l_13) / 2;
        area = sqrt(p * (p - l_12) * (p - l_23) * (p - l_13));

        return area;
    }

    public Point3D getQuadCentroid(Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
        Point3D rv;
        double x = (p1.getX() + p2.getX() + p3.getX() + p4.getX()) / 4.0;
        double y = (p1.getY() + p2.getY() + p3.getY() + p4.getY()) / 4.0;
        double z = (p1.getZ() + p2.getZ() + p3.getZ() + p4.getZ()) / 4.0;

        rv = new Point3D(-1, x, y, z);
        return rv;
    }

    public Point3D getNormal(Point3D p1, Point3D p2, Point3D p3) {
        Point3D rv;

        Point3D v1;
        Point3D v2;

        v1 = getSubVector(p2, p1);
        v2 = getSubVector(p3, p1);

        rv = getCrossVectorVector(v1, v2);

        rv = getNormalizeVector(rv);

        return rv;
    }

    public Point3D getSubVector(Point3D p2, Point3D p1) {
        Point3D rv;

        double x = p2.getX() - p1.getX();
        double y = p2.getY() - p1.getY();
        double z = p2.getZ() - p1.getZ();

        rv = new Point3D(-1, x, y, z);
        return rv;
    }

    public Point3D getCrossVectorVector(Point3D v1, Point3D v2) {
        Point3D result;
        double x = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
        double y = v1.getZ() * v2.getX() - v1.getX() * v2.getZ();
        double z = v1.getX() * v2.getY() - v1.getY() * v2.getX();

        result = new Point3D(-1, x, y, z);
        return result;
    }

    public Point3D getNormalizeVector(Point3D vector) {
        Point3D result;
        double x, y, z;
        double length = GeoMagVector(vector);
        double min_normal_length = 0.000000000001;
        if (length < min_normal_length) {
            x = 1.0;
            y = 0.0;
            z = 0.0;
        } else {
            double factor = 1.0 / length;
            x = vector.getX() * factor;
            y = vector.getY() * factor;
            z = vector.getZ() * factor;
        }

        result = new Point3D(-1, x, y, z);
        return result;
    }

    public double getDotVectorVector(Point3D n1, Point3D n2) {
        double result;
        result = n1.getX() * n2.getX() + n1.getY() * n2.getY()
                + n1.getZ() * n2.getZ();
        return result;
    }

    public double GeoMagVector(Point3D vector) {
        double result;
        result = pow(vector.getX(), 2) + pow(vector.getY(), 2)
                + pow(vector.getZ(), 2);
        result = sqrt(result);
        return result;
    }

    private void setDefaults() {
        parameter.setUlength("m");
        parameter.setUmass("kg");
        parameter.setUtime("s");
        parameter.setUenergy("J");
        parameter.setUtemperature("K");

        parameter.setInertia(1);

        parameter.setEf(new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f});

        parameter.setDfactor(1.0f);

        parameter.setSt(1.0f);

        parameter.setTimemethod(new int[]{1, 1, 1, 1, 1, 1});

        parameter.setScheme(new int[]{1, 1, 1, 1, 1, 1});

        parameter.setSteady(0);

        parameter.setFtol(new float[]{1E-6f, 1E-6f, 1E-6f, 1E-6f, 1E-6f, 1E-6f});

        parameter.setWbinary(0);
        parameter.setNsav(1);

        parameter.setCalc(new int[]{0, 0, 0, 0, 0, 0});

        parameter.setSavflux(0);

        parameter.setFsav(new int[]{0, 0, 0, 0, 0, 0});

        parameter.setCsav(new int[]{0, 0, 0, 0, 0, 0});

        parameter.setProbe(new int[]{0, 0, 0, 0, 0, 0});

        parameter.setSmooth(1);

        parameter.setVortex(new int[]{0, 0, 0});

        parameter.setStreamf(0);

        parameter.setFvec(0);
        parameter.setCvec(0);

        parameter.setKq(2.0f);
        parameter.setNcicsamcor(2);

        parameter.setG(new float[]{0.0f, 0.0f, 0.0f});

        parameter.setMsolver(new int[]{8, 8, 8, 8, 8, 3});

        parameter.setMprecond(new int[]{3, 3, 3, 3, 3, 3});

        parameter.setNorthocor(0);
        parameter.setOrthof(0.0f);

        parameter.setMtol(new float[]{1E-8f, 1E-8f, 1E-8f, 1E-8f, 1E-8f, 1E-8f});

        parameter.setMiter(new int[]{500, 500, 500, 500, 500, 500});

        parameter.setRestart(10000);
        parameter.setAdjdt(0);
        parameter.setMaxCp(0.25f);

        parameter.setT0(0.0f);
        parameter.setT1(0.001f);
        parameter.setDt(0.001f);

        parameter.setFill(0);
        parameter.setPf(99.5f);

        parameter.setIntbcphysreg(-1);
    }

    public Parameter getPar() {
        return this.parameter;
    }
}
