/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Administrator
 */
public class Formulation {

    String formString;
    double[] independentValue;
    String[] independentValueName;
    int independentValueNum;

    public Formulation(String formString) {
        //处理公式，注释中假设公式为：f(x,y,z)=2*x^2+6*y*z
        this.formString = formString.trim();
        int equalSignLocation = formString.indexOf("=");
        String leftString = formString.substring(0, equalSignLocation);//等号左边字符串,即f(x,y,z)
        int location1 = leftString.indexOf("(");
        int location2 = leftString.indexOf(")");
        String leftSubString = leftString.substring(location1, location2);
        independentValueName = leftSubString.split(",");
        independentValueNum = independentValueName.length;
        for (int i = 0; i < independentValueNum - 1; i++) {
            independentValueName[i] = independentValueName[i].trim();
        }
        this.formString = formString.substring(equalSignLocation, formString.length());
    }

    public double getValue(double... x) {
        int numOfX = x.length;
        double result=0;
        for (int i = 0; i <= independentValueNum - 1; i++) {
            formString.replace(independentValueName[i], String.valueOf(x[i]));
        }
        //result=execute(formString);
        return result;
    }
}
