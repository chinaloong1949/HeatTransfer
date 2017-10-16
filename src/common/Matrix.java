/*
 * 矩阵类
 * 1.如果矩阵式方阵，可以调用getDetValue方法求行列式......已验证
 * 2.如果是增广矩阵，可以调用getSolutionMatrix方法得到线性代数方程组的解,参考计算方法教程凌永祥，陈明逵第二章....已验证
 * 3.可以调用replaceColumn()方法替换矩阵中的某一列
 * 4.可以调用switchRow(int,int)方法交换矩阵中的两行
 * 5.可以调用Matrix类的静态方法corss(Matrix,Matrix)计算矩阵叉乘
 * 6.可以调用multiply(double)方法计算矩阵乘以一个数
 * 7.可以调用plus(Matrix,Matrix)方法计算矩阵加法
 * 8.可以调用transpose()方法求矩阵的转置
 * 9.通过equal(Matrix)判断两个矩阵是否相等
 *10.可以调用norm_1()求得矩阵的1-范数，同理可以调用norm_2(),norm_infinity()求得矩阵的2-范数和无穷大-范数；
 *11.可以调用adjointMatrix()求该矩阵的伴随矩阵，调用inverseMatrix()求该矩阵的逆矩阵.....已验证
 *12.可以调用getEigenValue()方法求矩阵的特征值……未完成
 *13.可以调用deleteRow()/deleteColumn()/deleteRowColumn()来删除矩阵的某一行某一列
 */
package common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author yk
 */
public class Matrix {

    private int rowNum = 1;
    private int columnNum = 1;
    private double[][] array;
    private boolean isSquare = false;

    public Matrix(boolean isHorizontal, double[] array) {
        if (isHorizontal) {
            columnNum = array.length;
            rowNum = 1;
            this.array = new double[1][columnNum];
            for (int i = 0; i <= columnNum - 1; i++) {
                this.array[0][i] = array[i];
            }
        } else {
            rowNum = array.length;
            columnNum = 1;
            this.array = new double[rowNum][1];
            for (int i = 0; i <= rowNum - 1; i++) {
                this.array[i][0] = array[i];
            }
        }
    }

    public Matrix(double[][] array) {
        this.array = array;
        rowNum = array.length;
        columnNum = array[0].length;//第一行有多少个元素，对矩阵来说就是列数
        if (rowNum == columnNum) {
            isSquare = true;
        }
    }

    public Matrix(int rowNum, int columnNum) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        array = new double[rowNum][columnNum];
        for (int i = 0; i <= rowNum - 1; i++) {
            for (int j = 0; j <= columnNum - 1; j++) {
                array[i][j] = 0;
            }
        }

    }

    public void setValue(int row, int column, double value) {
        array[row][column] = value;
    }

    public double getValue(int row, int column) {
        return array[row][column];
    }

    public boolean isSquareMatrix() {
        if (rowNum == columnNum) {
            isSquare = true;
        }
        return isSquare;
    }

    public double getDetValue() {
        double value = 0;
        if (isSquare) {
            value = new Determinant(array).getValue();
        }
        return value;
    }

    public static double[] getSolution(Matrix k, Matrix b) {
        double[] barr = b.toArrayColumn(0);
        return k.addColumn(k.getColumns(), barr).getSolution();
    }

    public static double[] getSolutionJacobi(Matrix k, Matrix b, int maxIterNum, double residual) {
        //该方法要求系数矩阵所有对角线元素不为0
        double[] barr = b.toArrayColumn(0);
        for (int i = 0; i < k.getRows(); i++) {
            if (k.getValueAt(i, i) == 0) {
                System.out.println("对角元素存在为0的情况，不能使用Jacobi迭代法");
            }
        }
        return k.addColumn(k.getColumns(), barr).getSolutionJacobi(maxIterNum, residual);
    }

    public static double[] getSolutionGauss_Seidel(Matrix k, Matrix b, int maxIterNum, double residual) {
        /* 
         * 该方法要求系数矩阵所有对角线元素不为0
         * 有些时候Jacobi迭代收敛，Gauss-Seidel迭代不一定收敛
         * 采用Gauss-Seidel迭代的收敛速度要比Jacobi迭代快很多
         */
        double[] barr = b.toArrayColumn(0);
        for (int i = 0; i < k.getRows(); i++) {
            if (k.getValueAt(i, i) == 0) {
                System.out.println("对角元素存在为0的情况，不能使用Jacobi迭代法");
            }
        }
        return k.addColumn(k.getColumns(), barr).getSolutionGauss_Seidel(maxIterNum, residual);
    }

    public double[] getSolution() {
        double[] solution = new double[rowNum];
        if (columnNum != (rowNum + 1)) {
            System.out.println("不是增广矩阵");
        } else {
            System.out.println("高斯列主消元法开始求解！");
            //判断系数矩阵是不是奇异矩阵，奇异矩阵的充要条件是矩阵可逆
            double[][] tempArr = new double[rowNum][rowNum];
            for (int i = 0; i <= rowNum - 1; i++) {
                for (int j = 0; j <= rowNum - 1; j++) {
                    tempArr[i][j] = array[i][j];
                }
            }
            /*if (new Matrix(tempArr).getDetValue() == 0) {
             System.out.println("系数矩阵不是奇异矩阵，高斯列主消元法无法进行"
             + "下面将在a[k][k]为0时停止。");
             }*/

            for (int k = 1; k <= rowNum - 1; k++) {//k控制用第几行作为消去的参考
                double tempDouble = abs(array[k - 1][k - 1]);
                int numOfSwitchLine = 0;
                for (int i = k; i <= rowNum - 1; i++) {
                    if (abs(array[i][k - 1]) > tempDouble) {
                        tempDouble = abs(array[i][k - 1]);
                        numOfSwitchLine = i;
                    }
                }
                if (numOfSwitchLine != 0) {
                    //交换第i行和第k-1行
                    array = new Matrix(array).
                            switchRow(numOfSwitchLine, k - 1).toArray();
                }
                for (int i = k; i <= rowNum - 1; i++) {//i控制当前消去第几行
                    System.out.println("  当前进行第" + k + "/" + rowNum
                            + "次消元;该次消行进度为" + (i - k + 1)
                            + "/" + (rowNum - k));
                    if (array[k - 1][k - 1] == 0) {
                        System.out.println("a[" + k + "][" + k + "]=0");
                        System.out.println("列主元为零，请检查设置！");
                    }
                    array[i][k - 1] = array[i][k - 1] / array[k - 1][k - 1];
                    for (int j = k; j <= columnNum - 1; j++) {
                        //j控制消去第i行的第j项
                        array[i][j] = array[i][j] - array[k - 1][j]
                                * array[i][k - 1];
                    }
                }
            }
            System.out.println("消元结束，开始回代！~~~~~~~~~~~~~~~~~~~~~~~~~~");
            solution[rowNum - 1] = array[rowNum - 1][columnNum - 1]
                    / array[rowNum - 1][rowNum - 1];
            for (int k = rowNum - 2; k >= 0; k--) {
                double sum = array[k][columnNum - 1];
                for (int j = k + 1; j <= rowNum - 1; j++) {
                    sum = sum - array[k][j] * solution[j];
                }
                solution[k] = sum / array[k][k];
            }
        }

        return solution;
    }

    public double[] getSolutionJacobi(int iterNum, double residual) {
        double[] solution = new double[rowNum];
        double[] solution_old = new double[rowNum];
        if (columnNum != (rowNum + 1)) {
            System.out.println("不是增广矩阵");
        } else {
            /* 
             * 判断迭代收敛性
             * 
             */
            for (int i = 0; i < rowNum; i++) {
                solution_old[i] = 1;
            }
            int iterNowNum = 0;
            do {
                iterNowNum++;
                for (int i = 0; i < rowNum; i++) {
                    //计算sigema(alpha_i_j,x_j)
                    double sigema = 0;
                    for (int j = 0; j < rowNum; j++) {
                        if (j != i) {
                            sigema = sigema + array[i][j] * solution_old[j];
                        }
                    }
                    solution[i] = (this.array[rowNum - 1][rowNum] - sigema) / array[i][i];
                }
            } while (delta(solution, solution_old) > residual && iterNowNum < iterNum);
        }
        return solution;
    }

    public double[] getSolutionGauss_Seidel(int iterNum, double residual) {
        double[] solution = new double[rowNum];
        double[] solution_old = new double[rowNum];
        if (columnNum != (rowNum + 1)) {
            System.out.println("不是增广矩阵");
        } else {
            for (int i = 0; i < rowNum; i++) {
                solution_old[i] = 1;
            }
            int iterNowNum = 0;
            do {
                iterNowNum++;
                for (int i = 0; i < rowNum; i++) {
                    //计算sigema(alpha_i_j,x_j)
                    double sigema = 0;
                    for (int j = 0; j < rowNum; j++) {
                        if (j < i) {
                            sigema = sigema + array[i][j] * solution[j];
                        } else if (j > i) {
                            sigema = sigema + array[i][j] * solution_old[j];
                        }
                    }
                    solution[i] = (this.array[rowNum - 1][rowNum] - sigema) / array[i][i];
                }
            } while (delta(solution, solution_old) > residual && iterNowNum < iterNum);
        }
        return solution;
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

    public Matrix replaceColumn(int numOfColumn, double[] column) {
        //列数从0开始，将该矩阵的第numOfColumn列换成column；
        double[][] copyOfArray = array;
        if (rowNum == column.length) {//判断替换的长度是否等于原矩阵的行数
            for (int i = 0; i <= rowNum - 1; i++) {
                copyOfArray[i][numOfColumn] = column[i];
            }
        }
        return new Matrix(copyOfArray);
    }

    public Matrix replaceRow(int numOfRow, double[] row) {
        //行数从0开始，将该矩阵的第numOfRow行换成row；
        double[][] copyOfArray = array;
        if (columnNum == row.length) {
            for (int i = 0; i <= columnNum - 1; i++) {
                copyOfArray[i][numOfRow] = row[i];
            }
        }
        return new Matrix(copyOfArray);
    }

    public static Matrix cross(Matrix matrix1, Matrix matrix2) {
        //矩阵matrix1左乘matrix2,返回结果矩阵
        Matrix matrix = null;
        double[][] array1 = matrix1.toArray();
        double[][] array2 = matrix2.toArray();
        if (matrix1.columnNum != matrix2.rowNum) {
            JOptionPane.showMessageDialog(null, "矩阵无法叉乘");
        } else {
            //两个矩阵满足叉乘条件，进行叉乘
            double[][] innerArray = new double[matrix1.rowNum][matrix2.columnNum];

            for (int i = 0; i <= matrix1.rowNum - 1; i++) {//i表示结果矩阵的行
                for (int j = 0; j <= matrix2.columnNum - 1; j++) {//j表示结果矩阵的列
                    innerArray[i][j] = 0;
                    for (int k = 0; k <= matrix1.columnNum - 1; k++) {
                        innerArray[i][j] = array1[i][k] * array2[k][j] + innerArray[i][j];
                    }
                }
            }
            matrix = new Matrix(innerArray);
        }

        return matrix;
    }

    public Matrix multiply(double num) {
        double[][] innerArray = new double[rowNum][columnNum];
        for (int i = 0; i <= rowNum - 1; i++) {
            for (int j = 0; j <= columnNum - 1; j++) {
                innerArray[i][j] = array[i][j] * num;
            }
        }
        return new Matrix(innerArray);
    }

    public static Matrix plus(Matrix matrix1, Matrix matrix2) {
        double[][] innerArray = new double[matrix1.getRows()][matrix1.getColumns()];
        double[][] array1 = matrix1.toArray();
        double[][] array2 = matrix2.toArray();
        if ((matrix1.getRows() != matrix2.getRows()) || (matrix1.getColumns() != matrix2.getColumns())) {
            JOptionPane.showMessageDialog(null, "用于进行矩阵加法操作的两个矩阵不是同型矩阵！");
        } else {
            for (int i = 0; i <= matrix1.getRows() - 1; i++) {
                for (int j = 0; j <= matrix1.getColumns() - 1; j++) {
                    innerArray[i][j] = array1[i][j] + array2[i][j];
                }
            }

        }
        Matrix matrix = new Matrix(innerArray);
        return matrix;
    }

    public static Matrix minus(Matrix matrix1, Matrix matrix2) {
        double[][] innerArray = new double[matrix1.getRows()][matrix1.getColumns()];
        double[][] array1 = matrix1.toArray();
        double[][] array2 = matrix2.toArray();
        if ((matrix1.getRows() != matrix2.getRows()) || (matrix1.getColumns() != matrix2.getColumns())) {
            JOptionPane.showMessageDialog(null, "用于进行矩阵减法操作的两个矩阵不是同型矩阵！");
        } else {
            for (int i = 0; i <= matrix1.getRows(); i++) {
                for (int j = 0; j <= matrix1.getColumns(); j++) {
                    innerArray[i][j] = array1[i][j] - array2[i][j];
                }
            }

        }
        Matrix matrix = new Matrix(innerArray);
        return matrix;
    }

    public Matrix transpose() {
        //矩阵转置
        Matrix matrix;

        double[][] innerArray = new double[columnNum][rowNum];
        for (int i = 0; i <= rowNum - 1; i++) {
            for (int j = 0; j <= columnNum - 1; j++) {
                innerArray[j][i] = array[i][j];
            }
        }
        matrix = new Matrix(innerArray);
        return matrix;
    }

    public Matrix switchRow(int row1, int row2) {
        Matrix matrix = new Matrix(array);
        for (int i = 0; i <= columnNum - 1; i++) {
            double tempDouble = array[row1][i];
            array[row1][i] = array[row2][i];
            array[row2][i] = tempDouble;
        }
        return matrix;
    }

    public double norm_1() {
        //求当前矩阵的1-范数，定义参考《计算方法教程·凌永祥、陈明逵》第55页
        double norm_1 = 0;
        for (int j = 0; j <= columnNum - 1; j++) {

            double sum = 0;
            for (int i = 0; i <= rowNum - 1; i++) {
                sum = sum + abs(array[i][j]);
            }
            if (sum > norm_1) {
                norm_1 = sum;
            }
        }
        return norm_1;
    }

    public double norm_2() {
        //求当前矩阵的2-范数，定义参考《计算方法教程·凌永祥、陈明逵》第55页
        double norm_2;
        Matrix a_temp = this.transpose();
        a_temp = Matrix.cross(a_temp, this);

        double[] eigenvalue = a_temp.getEigenValue();
        double maxEigenvalue = eigenvalue[0];
        for (int i = 1; i <= rowNum - 1; i++) {
            if (eigenvalue[i] > maxEigenvalue) {
                maxEigenvalue = eigenvalue[i];
            }
        }
        norm_2 = sqrt(maxEigenvalue);
        return norm_2;
    }

    public double norm_infinity() {
        //求当前矩阵的无穷大-范数，定义参考《计算方法教程·凌永祥、陈明逵》第55页
        double norm_infinity = 0;
        for (int i = 0; i <= rowNum - 1; i++) {

            double sum = 0;
            for (int j = 0; j <= columnNum - 1; j++) {
                sum = sum + abs(array[i][j]);
            }
            if (sum > norm_infinity) {
                norm_infinity = sum;
            }
        }
        return norm_infinity;
    }

    public double[] getEigenValue() {
        double[] eigenvalue = new double[rowNum];
        if (rowNum != columnNum) {
            JOptionPane.showMessageDialog(null, "给定矩阵不是方阵，无法求取特征值");
        } else {
            double[][] innerArray = new double[rowNum][columnNum];
            Matrix matrix = new Matrix(innerArray);

        }
        return eigenvalue;
    }

    public Matrix adjointMatrix() {
        double[][] innerArray = new double[rowNum][columnNum];
        if (!isSquare) {
            JOptionPane.showMessageDialog(null, "所求矩阵不是方阵，无法求取伴随矩阵");
        } else {
            for (int i = 0; i <= rowNum - 1; i++) {
                for (int j = 0; j <= columnNum - 1; j++) {
                    if ((i + j) % 2 != 0) {
                        innerArray[i][j] = -(new Determinant(this.algebraicMatrix(j, i).toArray()).getValue());
                    } else {
                        innerArray[i][j] = new Determinant(this.algebraicMatrix(j, i).toArray()).getValue();
                    }

                }
            }
        }
        Matrix matrix = new Matrix(innerArray);
        return matrix;
    }

    public Matrix inverseMatrix() {
        double detOfArray = this.getDetValue();
        if (detOfArray == 0) {
            this.printMatrix();
            JOptionPane.showMessageDialog(null, "矩阵不可逆");
        }

        Matrix matrix = this.adjointMatrix().multiply(1 / detOfArray);
        return matrix;
    }

    public Matrix algebraicMatrix(int numOfRow, int numOfColumn) {
        //该代数余子矩阵不是代数余子式，代数余子式为该矩阵的行列式的值乘以-1的（i+j）次方
        Matrix matrix;
        double[][] tempArray = new double[rowNum - 1][columnNum - 1];
        for (int i = 0; i <= rowNum - 2; i++) {
            for (int j = 0; j <= columnNum - 2; j++) {
                if ((i < numOfRow) && (j < numOfColumn)) {
                    tempArray[i][j] = array[i][j];
                } else if ((i < numOfRow) && (j >= numOfColumn)) {
                    tempArray[i][j] = array[i][j + 1];
                } else if ((i >= numOfRow) && (j < numOfColumn)) {
                    tempArray[i][j] = array[i + 1][j];
                } else {
                    tempArray[i][j] = array[i + 1][j + 1];
                }
            }
        }
        matrix = new Matrix(tempArray);
        return matrix;
    }

    public boolean equal(Matrix matrix1) {
        //如果两个矩阵对应位置上的元素相等，就认为两个矩阵相等
        boolean isequal = true;
        double[][] array1 = matrix1.toArray();
        if ((matrix1.getRows() != rowNum) || (matrix1.getColumns() != columnNum)) {
            isequal = false;
        } else {
            for (int i = 0; i <= rowNum - 1; i++) {
                for (int j = 0; j <= columnNum - 1; j++) {
                    if (array1[i][j] != array[i][j]) {
                        isequal = false;
                    }
                }
            }
        }
        return isequal;
    }

    public Matrix deleteRow(int rowNum) {
        double[][] tempArr = new double[rowNum - 1][columnNum];
        for (int i = 0; i < rowNum - 1; i++) {
            for (int j = 0; j < columnNum; j++) {
                if (i < rowNum) {
                    tempArr[i][j] = array[i][j];
                } else {
                    tempArr[i][j] = array[i + 1][j];
                }
            }
        }
        return new Matrix(tempArr);
    }

    public Matrix addRow(int rowNum, double[] rowData) {
        double[][] tempArr = new double[rowNum + 1][columnNum];
        if (rowData.length >= columnNum) {
            if (rowData.length > columnNum) {
                System.out.println("添加数据过程中，数据长度大于列数，添加前"
                        + columnNum + "个数据到矩阵第" + rowNum + "中！");
            }
            for (int i = 0; i < rowNum + 1; i++) {
                if (i < rowNum) {
                    System.arraycopy(array[i], 0, tempArr[i], 0, columnNum);
                } else if (i == rowNum) {
                    System.arraycopy(rowData, 0, tempArr[i], 0, columnNum);
                } else {
                    System.arraycopy(array[i - 1], 0, tempArr[i], 0, columnNum);
                }
            }
        } else {
            System.out.println("添加数据过程中，数据长度小于列数，不足数据补零处理！"
                    + "共补了" + (columnNum - rowData.length)
                    + "个零，具体行号：" + rowNum);
            for (int i = 0; i < rowNum + 1; i++) {
                if (i < rowNum) {
                    System.arraycopy(array[i], 0, tempArr[i], 0, columnNum);
                } else if (i == rowNum) {
                    for (int j = 0; j < columnNum; j++) {
                        if (j < rowData.length) {
                            tempArr[i][j] = rowData[j];
                        } else {
                            tempArr[i][j] = 0;
                        }
                    }
                } else {
                    System.arraycopy(array[i - 1], 0, tempArr[i], 0, columnNum);
                }
            }
        }
        return new Matrix(tempArr);
    }

    public Matrix addColumn(int column, double[] columnData) {
        double[][] tempArr = new double[rowNum][columnNum + 1];
        if (columnData.length >= rowNum) {
            if (columnData.length > rowNum) {
                System.out.println("添加数据过程中，数据长度大于行数，添加前"
                        + rowNum + "个数据到矩阵第" + column + "中！");
            }
            for (int i = 0; i < rowNum; i++) {
                for (int j = 0; j < columnNum + 1; j++) {
                    if (j < column) {
                        tempArr[i][j] = array[i][j];
                    } else if (j == column) {
                        tempArr[i][j] = columnData[i];
                    } else {
                        tempArr[i][j] = array[i][j - 1];
                    }
                }
            }
        } else {
            System.out.println("添加数据过程中，数据长度小于列数，不足数据补零处理！"
                    + "共补了" + (rowNum - columnData.length)
                    + "个零，具体行号：" + rowNum);
            for (int i = 0; i < rowNum; i++) {
                for (int j = 0; j < columnNum + 1; j++) {
                    if (j < column) {
                        tempArr[i][j] = array[i][j];
                    } else if (j == column) {
                        if (i < columnData.length) {
                            tempArr[i][j] = columnData[i];
                        } else {
                            tempArr[i][j] = 0;
                        }
                    } else {
                        tempArr[i][j] = array[i][j - 1];
                    }
                }
            }
        }
        return new Matrix(tempArr);
    }

    public Matrix deleteColumn(int column) {
        double[][] tempArr = new double[rowNum][columnNum - 1];
        for (int i = 0; i < rowNum; i++) {
            for (int j = 0; j < columnNum - 1; j++) {
                if (j < column) {
                    tempArr[i][j] = array[i][j];
                } else {
                    tempArr[i][j] = array[i][j - 1];
                }
            }
        }
        return new Matrix(tempArr);
    }

    public Matrix deleteRowColumn(int row, int column) {
        double[][] tempArr = new double[rowNum - 1][columnNum - 1];
        for (int i = 0; i < rowNum - 1; i++) {
            if (i < row) {
                for (int j = 0; j < columnNum - 1; j++) {
                    if (j < column) {
                        tempArr[i][j] = array[i][j];
                    } else {
                        tempArr[i][j] = array[i][j + 1];
                    }
                }
            } else {
                for (int j = 0; j < columnNum - 1; j++) {
                    if (j < column) {
                        tempArr[i][j] = array[i + 1][j];
                    } else {
                        tempArr[i][j] = array[i + 1][j + 1];
                    }
                }
            }
        }
        return new Matrix(tempArr);
    }

    public int getRows() {
        return this.rowNum;
    }

    public int getColumns() {
        return this.columnNum;
    }

    public double[][] toArray() {
        return array;
    }

    public double[] toArrayRow(int numOfRow) {
        double[] tempArr = new double[columnNum];
        if (numOfRow < rowNum) {
            System.arraycopy(array[numOfRow], 0, tempArr, 0, columnNum);
        } else {
            System.out.println("矩阵不存在第" + numOfRow + "行，矩阵行"
                    + "数索引范围：0.." + (rowNum - 1));
        }
        return tempArr;
    }

    public double[] toArrayColumn(int numOfColumn) {
        double[] tempArr = new double[rowNum];
        if (numOfColumn < columnNum) {
            for (int i = 0; i < rowNum; i++) {
                tempArr[i] = array[i][numOfColumn];
            }
        } else {
            System.out.println("矩阵不存在第" + numOfColumn + "列，矩阵行"
                    + "数索引范围：0.." + (columnNum - 1));
        }
        return tempArr;
    }

    public double getValueAt(int i, int j) {
        return this.array[i][j];
    }

    public void printMatrix() {
        System.out.println("-------------------------------");
        for (int i = 0; i <= rowNum - 1; i++) {
            for (int j = 0; j <= columnNum - 1; j++) {
                String temp = String.format("%7.2f", array[i][j]);
                while (temp.length() < 20) {
                    temp = temp + " ";
                }
                System.out.print(temp);
            }
            System.out.println("");
        }
        System.out.println("=================================");
    }

    public void writeToFile(String fileName) {
        File file = new File(fileName);
        writeToFile(file);
    }

    public void writeToFile(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);

            for (int i = 0; i <= rowNum - 1; i++) {
                for (int j = 0; j <= columnNum - 1; j++) {
                    fos.flush();
                    String temp = String.format("%7.2f", array[i][j]);
                    while (temp.length() < 20) {
                        temp = temp + " ";
                    }
                    fos.write((temp).getBytes("UTF-8"));
                }
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
                    for (int i = 0; i <= rowNum - 1; i++) {
                        for (int j = 0; j <= columnNum - 1; j++) {
                            fos.flush();
                            String temp = String.format("%7.2f", array[i][j]);
                            while (temp.length() < 20) {
                                temp = temp + " ";
                            }
                            fos.write((temp).getBytes("UTF-8"));
                        }
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

}
