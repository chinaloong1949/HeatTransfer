/*
 * 六面体单元，每个边有两个节点，共八个节点
 */
package element;

import point.Point3D;

/**
 *
 * @author Administrator
 */
public class EleQuad_8 {

    private int elementId = 0;
    private int[] nodeId = new int[8];
    private int pid;

    public EleQuad_8(int elementId, int pid, int[] nodeId) {
        this.elementId = elementId;
        this.pid = pid;
        this.nodeId = nodeId;
    }

    public EleQuad_8(int ElementId, int[] nodeId) {
        this.elementId = ElementId;
        this.nodeId = nodeId;
    }

    public EleQuad_8(int[] nodeId) {
        this.nodeId = nodeId;
    }

    public int getElementId() {
        return this.elementId;
    }

    public int[] getNodeList() {
        return this.nodeId;
    }

    public static void printTitle() {
        System.out.println("单元编号    ------包含节点号（node1, node2, node3, node4, node5, node6, node7, node8）------"
                + "");
    }

    public void printElement() {
        System.out.print(String.format("%7d", elementId));

        for (int i = 0; i <= 7; i++) {
            System.out.print(String.format("%10d", nodeId[i]));
        }
        System.out.println("");
    }

    /**
     * @return the pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

}
