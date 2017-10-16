/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class SparseMatrix {

    private int[] rpos;//各行第一个非零元的序号表，其值为data的索引号
    private int rowNum;//矩阵行数
    private int columnNum;//矩阵列数
    private int dataNum;//矩阵非零元素的个数
    private ArrayList<Triple> dataList = new ArrayList<>();
    private IndexList[] column;

    public SparseMatrix(int rowNum, int columnNum) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        rpos = new int[rowNum + 1];
        column = new IndexList[columnNum];
    }

    public void setValueFromIndex1(int row, int column, double value) {
        setValue(row - 1, column - 1, value);
    }

    public void setValue(int row, int column, double value) {

        int is = isExisted(row, column);
        if (is != -1) {//如果row,column对应的元素已有，则修改其值
//            dataList.get(is).setValue(value);
            if (abs(value) > 1e-15) {
                //设置的值不为0
                dataList.get(is).setValue(value);
            } else {
                //设置的值为0
                dataList.remove(is);
                for (int i = row + 1; i <= getRowNum(); i++) {
                    rpos[i]--;
                }
                dataNum = dataNum - 1;
            }
        } else {//对应的元素不存在
            if (abs(value) < 1e-15) {//该元素本来就不存在，设置的值又是0，因此不进行任何操作
                return;
            }
            if (rpos[row] < rpos[row + 1]) {//插入的这一行已经存在数据，需要判断插入元素在这一行的具体位置
                int temp = getRpos()[row + 1];
                int temp1 = 0;
                for (int j = rpos[row]; j < temp; j++) {
                    if (dataList.get(j).column > column) {
                        dataList.add(j, new Triple(row, column, value));
                        temp1++;
                        break;
                    }
                }
                if (temp1 == 0) {//这种情况下，所插入元素应该是插入行的最后一个
                    dataList.add(getRpos()[row + 1], new Triple(row, column, value));
                }
                for (int i = row + 1; i <= getRowNum(); i++) {
                    rpos[i]++;
                }
            } else {//插入的这一行不存在数据，直接将该元素插入这一行所在的位置即可
                dataList.add(rpos[row], new Triple(row, column, value));
                for (int i = row + 1; i <= getRowNum(); i++) {//第row行以后所有行的序号标+1
                    rpos[i]++;
                }
            }
            dataNum++;
        }

    }

    public void setValue(int index, double value) {
        //该方法只能用来改变本来就存在的非零元素的值
        //改变对应位置上的Triple的value，保持row和column不变
        this.dataList.get(index).value = value;
    }

    public double getValueAtFromIndex1(int row, int column) {
        return getValueAt(row - 1, column - 1);
    }

    public double getValueAt(int row, int column) {
        int index = -1;
        for (int i = getRpos()[row]; i < getRpos()[row + 1]; i++) {
            if (dataList.get(i).column == column) {
                index = i;
                break;
            }
        }
        return getValueAt(index);
    }

    public double getValueAt(int index) {
        if (index == -1) {//表示是矩阵的0元素
            return 0.0;
        }
        return dataList.get(index).value;
    }

    public int getIndexOfElement(int row, int column) {
        return isExisted(row, column);
    }

    public int isExisted(int row, int column) {
        //如果该元素存在，返回他在dataList中的索引号；如果不存在，返回-1

        for (int i = getRpos()[row]; i < rpos[row + 1]; i++) {//这种情况下，不用判断triple.row==row
            Triple triple = dataList.get(i);
            if (triple.column == column) {
                return i;
            }
        }
        return -1;
    }

    public static SparseMatrix plus(SparseMatrix matrix1, SparseMatrix matrix2) {
        if (matrix1.getRowNum() != matrix2.getRowNum() || matrix1.getColumnNum() != matrix2.getColumnNum()) {
            System.out.println("进行相加的两个矩阵形状不同，matrix1为"
                    + matrix1.getRowNum() + "," + matrix1.getColumnNum() + ";matrix2为"
                    + matrix2.getRowNum() + "," + matrix2.getColumnNum());
            return null;
        }
        SparseMatrix result = new SparseMatrix(matrix1.getRowNum(), matrix1.getColumnNum());
        for (int i = 0; i < matrix1.getRowNum(); i++) {//
            int start1 = matrix1.getRpos()[i];
            int end1 = matrix1.getRpos()[i + 1];
            int start2 = matrix2.getRpos()[i];
            int end2 = matrix2.getRpos()[i + 1];
            for (int j = start1; j < end1; j++) {
                result.setValue(i, j, matrix1.getValueAt(i, j) + matrix2.getValueAt(i, j));

            }

        }
        return result;
    }

    public double[] getSolution(String command) {
        double[] result = new double[getRowNum()];
        switch (command) {
            case "AsFull":
                result = toFullMatrix().getSolution();
                break;
            case "":
                break;

        }

        return result;
    }

    public Matrix toFullMatrix() {
        Matrix matrix = new Matrix(getRowNum(), getColumnNum());
        Triple[] data = getData();
        for (Triple triple : data) {
            matrix.setValue(triple.row, triple.column, triple.value);
        }
        return matrix;
    }

    public SparseMatrix tranpose() {
        SparseMatrix result = new SparseMatrix(getColumnNum(), getRowNum());
        Triple[] data = this.getData();
        /* 
         采用此算法时引用两个辅助数组num[],cpot[], 
         num[col]表示矩阵M中第col列中非零元的个数，
         cpot[col]指示M中第col列的第一个非零元在b.data中的恰当位置。
         （即指M中每一列的第一个非零元在B中表示为第几个非零元）
         */
        result.dataNum = this.getDataNum();
        int[] num = new int[getColumnNum()];
        int[] cpot = new int[getColumnNum()];
        if (result.getDataNum() != 0) {
            //java数组默认为0，不需要额外初始化
//            for (int col = 0; col < getColumnNum(); col++) {
//                num[col] = 0;
//            }
            for (int t = 0; t < getDataNum(); t++) {
                num[data[t].column]++;
            }
            cpot[0] = 0;
            for (int col = 1; col < getColumnNum(); col++) {
                cpot[col] = cpot[col - 1] + num[col - 1];
            }
            for (int i = 0; i < cpot.length; i++) {
                result.rpos[i] = cpot[i];
            }
            Triple tri;
            Triple[] resultData = new Triple[getDataNum()];
            for (int i = 0; i < getDataNum(); i++) {
                int col = data[i].column;
                int q = cpot[col];
                resultData[q] = new Triple(data[i].column, data[i].row, data[i].value);
                cpot[col]++;
            }
            for (int i = 0; i < getDataNum(); i++) {
                result.dataList.add(resultData[i]);
            }
        }
        return result;
    }

    public SparseMatrix reorder() {
        SparseMatrix result = new SparseMatrix(getRowNum(), getColumnNum());
        Triple[] triple = this.getData();
        for (int i = 0; i < getDataNum(); i++) {
            result.setValue(triple[i].row, triple[i].column, triple[i].value);
        }
        return result;
    }

    public Triple[] getData() {
        Triple[] triple = new Triple[dataList.size()];
        triple = dataList.toArray(triple);
        return triple;
    }

    public void writeToFile(String fileName) {
        File file = new File(fileName);
        writeToFile(file);
    }

    public void writeToFile(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Triple[] data = getData();
            for (Triple triple : data) {
                fos.flush();
                String temp = String.format("%10d%10d%20.2f", triple.row, triple.column, triple.value);
                while (temp.length() < 20) {
                    temp = temp + " ";
                }
                fos.write((temp).getBytes("UTF-8"));
                fos.write("\r\n".getBytes("UTF-8"));
            }
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Matrix.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeToFile(String fileName, boolean append) {
        File file = new File(fileName);
        writeToFile(file, append);
    }

    public void writeToFile(File file, boolean append) {
        if (append) {
            if (file.exists()) {
                try {
                    FileOutputStream fos = new FileOutputStream(file, true);
                    Triple[] data = getData();
                    for (Triple triple : data) {
                        fos.flush();
                        String temp = String.format("%20d%20d%7.2f", triple.row, triple.column, triple.value);
                        while (temp.length() < 20) {
                            temp = temp + " ";
                        }
                        fos.write((temp).getBytes("UTF-8"));
                        fos.write("\r\n".getBytes("UTF-8"));
                    }
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                writeToFile(file);
            }
        } else {
            writeToFile(file);
        }
    }

    public void printSparseMatrix(int tab) {
        //tab==0下标从0开始，tab==1,下表从1开始
        System.out.println("行号       列号         值");
        System.out.println("----------------------------------");
        for (int i = 0; i < getDataNum(); i++) {

            System.out.println((dataList.get(i).row + tab) + "       "
                    + (dataList.get(i).column + tab) + "      "
                    + dataList.get(i).value);
        }
        System.out.println("==================================");
        System.out.println("矩阵共有" + getRowNum() + "行，" + getColumnNum() + "列，" + getDataNum() + "个非零元素。");

    }

    /**
     * @return the dataNum
     */
    public int getDataNum() {
        return dataNum;
    }

    /**
     * @return the rpos
     */
    public int[] getRpos() {
        return rpos;
    }

    /**
     * @return the rowNum
     */
    public int getRowNum() {
        return rowNum;
    }

    /**
     * @return the columnNum
     */
    public int getColumnNum() {
        return columnNum;
    }
}
