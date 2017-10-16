/*
 * 平面四边形单元，每个边有三个节点，共九个节点
 */
package element;

import point.Point2D;

/**
 *
 * @author Administrator
 */
public class EleQuad_9 {

    private int elementId = 0;
    private int[] nodeId = new int[9];
    private Point2D[] point = null;

    public EleQuad_9(int ElementId, int[] nodeId) {
        this.elementId = ElementId;
        this.nodeId = nodeId;
    }

    public EleQuad_9(int[] nodeId) {
        this.nodeId = nodeId;
    }

    public int[] getNodeList() {
        return this.nodeId;
    }

    public static void printTitle() {
        System.out.println("单元编号    ------包含节点号（node1, node2, node3, node4, node5, node6, node7, node8, node9）------"
                + "");
    }

    public void printElement() {
        System.out.print(String.format("%7d", elementId));

        for (int i = 0; i <= 8; i++) {
            System.out.print(String.format("%10d", nodeId[i]));
        }
        System.out.println("");
    }

}
