/*
 * 三维二次六面体二十节点单元
 * 通常作为六面体网格流域的速度插值单元使用
 */
package element;

/**
 *
 * @author Administrator
 */
public class EleQuad_20 {

    private int elementId = -1;
    private int[] nodeId = new int[20];

    public EleQuad_20(int elementId, int[] nodeId) {
        this.elementId = elementId;
        this.nodeId = nodeId;
    }

    public int getElementId() {
        return elementId;
    }

    public int[] getNodeList() {
        return nodeId;
    }

}
