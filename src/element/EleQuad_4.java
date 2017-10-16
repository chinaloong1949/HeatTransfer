/*
 * 平面四边形单元，每个边有两个节点，共四个节点
 */
package element;

import java.text.NumberFormat;

/**
 *
 * @author Administrator
 */
public class EleQuad_4 {

    private int elementId = 0;
    private int[] nodeId = new int[4];
    private int boundarySide = -1;
    private double cosx;
    private double cosy;
    private double p1;
    private double p2;
    private boolean isBoundaryEle = false;

    public EleQuad_4(int ElementId, int[] nodeId) {
        this.elementId = ElementId;
        this.nodeId = nodeId;
    }

    public EleQuad_4(int[] nodeId) {
        this.nodeId = nodeId;
    }

    public int[] getNodeList() {
        return this.nodeId;
    }

    public int getElementId() {
        return this.elementId;
    }

    public int getBoundarySide() {
        return this.boundarySide;
    }

    public double[] getInfo() {
        double[] info = new double[4];
        info[0] = this.cosx;
        info[1] = this.cosy;
        info[2] = this.p1;
        info[3] = this.p2;
        return info;
    }

    public boolean isBoundaryElement() {
        return isBoundaryEle;
    }

    public void setInfo(int side, double cosx, double cosy, double p1, double p2) {
        this.boundarySide = side;
        this.cosx = cosx;
        this.cosy = cosy;
        this.p1 = p1;
        this.p2 = p2;
        this.isBoundaryEle = true;
    }

    public static void printTitle() {
        System.out.println("单元编号    包含节点号（node1, node2, node3, node4）  "
                + "是否边界单元   边界边   第一点处压力值   第二点处压力值");
    }

    public void printElement() {

        System.out.print(String.format("%7d", elementId));

        for (int i = 0; i <= 3; i++) {
            System.out.print(String.format("%10d", nodeId[i]));
        }
        if (!isBoundaryEle) {
            System.out.print(String.format("          否     "));
        }
        if (isBoundaryEle) {
            System.out.print(String.format("          是     "));
            System.out.print(String.format("%6d", this.boundarySide));
            System.out.print("      ");
            System.out.print(String.format("%10.4f", p1) + "      ");
            System.out.print(String.format("%10.4f", p1));
        }
        System.out.println("");
    }
}
