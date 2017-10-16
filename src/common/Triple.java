/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

public class Triple {

    public int row, column;
    public double value;

    public Triple(int row, int column, double value) {
        this.row = row;
        this.column = column;
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
