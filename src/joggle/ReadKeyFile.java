/*
 * 本例子参见《流体力学中的有限元方法》第四章第三小节
 * 本例考虑三维六面体单元
 */
package joggle;

import common.FileChooser;
import element.EleQuad_8;
import element.Part;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import point.Point3D;

/**
 *
 * @author yk
 */
public class ReadKeyFile {

    Point3D[] pointList;
    ArrayList<String> pointArrList = new ArrayList<>();
    EleQuad_8[] elementList_ansys;
    EleQuad_8[] elementList_normal;
    ArrayList<String> elementArrList = new ArrayList<>();
    String title = null;
    int DATABASE_FORMAT = 0;
    ArrayList<NodeSet> nodeSetList = new ArrayList<>();
    int nodeSetNum = 0;
    ArrayList<Part> partList = new ArrayList<>();
    File file;

    public ReadKeyFile() {
        this.file = new FileChooser("打开文件", 1).getFile();
        readin();
        setPointInfo();
        setElement_SolidInfo();
    }

    public ReadKeyFile(File file) {
        this.file = file;
        readin();
        setPointInfo();
        setElement_SolidInfo();
    }

    private void readin() {
        try {
            InputStreamReader fr = new InputStreamReader(new FileInputStream(file), "GBK");
            BufferedReader in = new BufferedReader(fr);
            String line;
            String tab = null;//对数据内容进行标记，方便读取处理
            int cardNum = 0;
            while ((line = in.readLine()) != null) {
                //System.out.println("45ReadKeyFile:  " + line);
                if (line.trim().equals("")) {
                    //表示空行，如连续回车

                } else if (line.charAt(0) == '$') {
                    //表示注释行，不执行任何操作
                } else if (line.charAt(0) == '*') {
                    //关键字行
                    //该if语句下将tab置成相应值，下次循环进入相应case块
                    line = line.trim();
                    tab = line.substring(1);
                    cardNum = 0;
                } else {
                    //排除是注释行和关键字行，则是数据行
                    switch (tab) {
                        case "KEYWORD":
                            //表示k文件，LS-DYNA关键字文件标记
                            break;
                        case "TITLE":
                            //标题
                            title = line.trim();
                            break;
                        case "DATABASE_FORMAT":
                            DATABASE_FORMAT = Integer.parseInt(line.trim());
                            break;
                        case "NODE":
                            //将该行作为一个字符串整体加入pointArrList中
                            pointArrList.add(line);
                            break;
                        case "SECTION_SOLID":
                            //SECTION_SOLID(line);
                            break;
                        case "ALE_MULTI_MATERIAL_GROUP":
                            //ALE_MULTI_MATERIAL_GROUP(line);
                            break;
                        case "CONTROL_ALE":
                            //CONTROL_ALE(line);
                            break;
                        case "INITIAL_VOLUME_FRACTION_GEOMETRY":
                            //INITIAL_VOLUME_FRACTION_GEOMETRY(line);
                            break;
                        case "SET_PART_LIST":
                            //SET_PART_LIST(line);
                            break;
                        case "SET_NODE_LIST":
                            cardNum = cardNum + 1;
                            SET_NODE_LIST(line, cardNum);
                            break;
                        case "SET_MULTI-MATERIAL_GROUP_LIST":
                            //SET_MULTI - MATERIAL_GROUP_LIST(line);
                            break;
                        case "CONSTRAINED_LAGRANGE_IN_SOLID":
                            //CONSTRAINED_LAGRANGE_IN_SOLID(line);
                            break;
                        case "ELEMENT_SOLID":
                            elementArrList.add(line);
                            break;
                        case "MAT_ELASTIC":
                            //MAT_ELASTIC(line);
                            break;
                        case "PART":
                            PART(in);
                            break;
                        default:
                            break;
                    }
                }

            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }

    }

    private void PART(BufferedReader in) throws IOException {
        String line;
        line = in.readLine().trim();
        while (line.startsWith("$")) {
            line = in.readLine().trim();
        }
        String[] ids = line.split("\\s+");
        Part part = new Part();
        for (int i = 0; i < ids.length; i++) {
            switch (i) {
                case 0:
                    part.setPID(Integer.parseInt(ids[i]));
                    break;
                case 1:
                    part.setSECID(Integer.parseInt(ids[i]));
                    break;
                case 2:
                    part.setMID(Integer.parseInt(ids[i]));
                    break;
                case 3:
                    part.setEOSID(Integer.parseInt(ids[i]));
                    break;
                case 4:
                    part.setHGID(Integer.parseInt(ids[i]));
                    break;
                case 5:
                    part.setGRAV(Integer.parseInt(ids[i]));
                    break;
                case 6:
                    part.setADPOPT(Integer.parseInt(ids[i]));
                    break;
                case 7:
                    part.setTMID(Integer.parseInt(ids[i]));
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Part定义异常，出错位置："
                            + "ReadKeyFile:PART(BufferedReader in)");
            }
        }
        partList.add(part);
    }

    private void setPointInfo() {
        //考虑到链表结构如果要根据索引查找元素效率低下，所以这里将其转化为数组结构。
        int nodeNum = pointArrList.size();
        pointList = new Point3D[nodeNum + 1];//节点索引号从1开始算起，
        //而Java语言默认是从0开始，所以数组大小加1,PointList[0]里不存点信息
        Iterator<String> it = pointArrList.iterator();
        String tempLine;
        int i = 0;
        int pointId;
        double x, y, z;
        while (it.hasNext()) {
            i++;
            tempLine = it.next();

            pointId = Integer.parseInt(tempLine.substring(0, 8).trim());
            x = Double.parseDouble(tempLine.substring(8, 24).trim());
            y = Double.parseDouble(tempLine.substring(24, 40).trim());
            z = Double.parseDouble(tempLine.substring(40, 56).trim());
            pointList[i] = new Point3D(pointId, x, y, z);
        }
        pointArrList.clear();//释放链表结构所占用的内存
    }

    private void setElement_SolidInfo() {
        Iterator<String> it;
//        int i = 0;
        String tempLine;

        ArrayList<EleQuad_8> eleQuad_8List_ansys = new ArrayList<>();
        ArrayList<EleQuad_8> eleQuad_8List_normal = new ArrayList<>();
        it = elementArrList.iterator();
        int EID, PID, node1, node2, node3, node4, node5, node6, node7, node8;
        EID = PID = node1 = node2 = node3 = node4 = node5 = node6 = node7 = node8 = 0;

        while (it.hasNext()) {

            tempLine = it.next().trim();
            String[] ss = tempLine.split("\\s+");
            if (ss.length == 10) {//如果一行有10个数据，则表示每一个单元信息是位于同一行
//                i++;
                EID = Integer.parseInt(ss[0]);
                PID = Integer.parseInt(ss[1]);
                node1 = Integer.parseInt(ss[2]);
                node2 = Integer.parseInt(ss[3]);
                node3 = Integer.parseInt(ss[4]);
                node4 = Integer.parseInt(ss[5]);
                node5 = Integer.parseInt(ss[6]);
                node6 = Integer.parseInt(ss[7]);
                node7 = Integer.parseInt(ss[8]);
                node8 = Integer.parseInt(ss[9]);

            } else if (ss.length == 2) {
                //如果一行只有两个数据，则表示只是EID和PID，赋值这两个变量后进入下一行
//                i++;
                EID = Integer.parseInt(ss[0]);
                PID = Integer.parseInt(ss[1]);
                continue;
            } else if (ss.length == 8) {//如果一行有8个数据，则表示是八个节点编号，EID和PID已经在上一行赋值了
                node1 = Integer.parseInt(ss[0]);
                node2 = Integer.parseInt(ss[1]);
                node3 = Integer.parseInt(ss[2]);
                node4 = Integer.parseInt(ss[3]);
                node5 = Integer.parseInt(ss[4]);
                node6 = Integer.parseInt(ss[5]);
                node7 = Integer.parseInt(ss[6]);
                node8 = Integer.parseInt(ss[7]);
            }
            int[] nodeId = new int[]{node1, node2, node3, node4,
                node5, node6, node7, node8};
            int[] nodeId_normal = new int[8];

            double minx = pointList[nodeId[0]].getX();
            double maxx = pointList[nodeId[0]].getX();
            for (int ii = 1; ii <= 7; ii++) {
                if (minx > pointList[nodeId[ii]].getX()) {
                    minx = pointList[nodeId[ii]].getX();
                }
                if (maxx < pointList[nodeId[ii]].getX()) {
                    maxx = pointList[nodeId[ii]].getX();
                }
            }
            double midx = (minx + maxx) / 2;
            double miny = pointList[nodeId[0]].getY();
            double maxy = pointList[nodeId[0]].getY();
            for (int ii = 1; ii <= 7; ii++) {
                if (miny > pointList[nodeId[ii]].getY()) {
                    miny = pointList[nodeId[ii]].getY();
                }
                if (maxy < pointList[nodeId[ii]].getY()) {
                    maxy = pointList[nodeId[ii]].getY();
                }
            }
            double midy = (miny + maxy) / 2;
            double minz = pointList[nodeId[0]].getZ();
            double maxz = pointList[nodeId[0]].getZ();
            for (int ii = 1; ii <= 7; ii++) {
                if (minz > pointList[nodeId[ii]].getZ()) {
                    minz = pointList[nodeId[ii]].getZ();
                }
                if (maxz < pointList[nodeId[ii]].getZ()) {
                    maxz = pointList[nodeId[ii]].getZ();
                }
            }
            double midz = (minz + maxz) / 2;
            for (int j = 0; j <= 7; j++) {
                if (pointList[nodeId[j]].getX() < midx
                        && pointList[nodeId[j]].getY() < midy
                        && pointList[nodeId[j]].getZ() < midz) {
                    nodeId_normal[0] = nodeId[j];
                } else if (pointList[nodeId[j]].getX() > midx
                        && pointList[nodeId[j]].getY() < midy
                        && pointList[nodeId[j]].getZ() < midz) {
                    nodeId_normal[1] = nodeId[j];
                } else if (pointList[nodeId[j]].getX() > midx
                        && pointList[nodeId[j]].getY() > midy
                        && pointList[nodeId[j]].getZ() < midz) {
                    nodeId_normal[2] = nodeId[j];
                } else if (pointList[nodeId[j]].getX() < midx
                        && pointList[nodeId[j]].getY() > midy
                        && pointList[nodeId[j]].getZ() < midz) {
                    nodeId_normal[3] = nodeId[j];
                } else if (pointList[nodeId[j]].getX() < midx
                        && pointList[nodeId[j]].getY() < midy
                        && pointList[nodeId[j]].getZ() > midz) {
                    nodeId_normal[4] = nodeId[j];
                } else if (pointList[nodeId[j]].getX() > midx
                        && pointList[nodeId[j]].getY() < midy
                        && pointList[nodeId[j]].getZ() > midz) {
                    nodeId_normal[5] = nodeId[j];
                } else if (pointList[nodeId[j]].getX() > midx
                        && pointList[nodeId[j]].getY() > midy
                        && pointList[nodeId[j]].getZ() > midz) {
                    nodeId_normal[6] = nodeId[j];
                } else if (pointList[nodeId[j]].getX() < midx
                        && pointList[nodeId[j]].getY() > midy
                        && pointList[nodeId[j]].getZ() > midz) {
                    nodeId_normal[7] = nodeId[j];
                } else {
                    System.out.println("Error in rearrange element's nodeList");
                    System.out.println("出错单元：" + EID);
                    System.out.println("出错单元各个节点信息：");
                    new EleQuad_8(EID, PID, nodeId).printElement();
                    System.out.println("出错节点：");
                    pointList[nodeId[j]].printPoint3D();
                    System.out.println("minX = " + minx + ", midX = " + midx + ", maxX = " + maxx);
                    System.out.println("minY = " + miny + ", midY = " + midy + ", maxY = " + maxy);
                    System.out.println("minZ = " + minz + ", midZ = " + midz + ", maxZ = " + maxz);
                }
            }
            eleQuad_8List_ansys.add(new EleQuad_8(EID, PID, nodeId));
            eleQuad_8List_normal.add(new EleQuad_8(EID, PID, nodeId_normal));

//            elementList_ansys[i] = new EleQuad_8(EID, PID, nodeId);
//            elementList_normal[i] = new EleQuad_8(EID, PID, nodeId_normal);
        }
        elementArrList.clear();
        int elementNum = eleQuad_8List_ansys.size();
        elementList_ansys = new EleQuad_8[elementNum];//elementList[0]不存单元
        elementList_normal = new EleQuad_8[elementNum];
        elementList_ansys = eleQuad_8List_ansys.toArray(elementList_ansys);
        elementList_normal = eleQuad_8List_normal.toArray(elementList_normal);
        eleQuad_8List_ansys.clear();
        eleQuad_8List_normal.clear();
    }

    private void SET_NODE_LIST(String line, int cardNum) {
        //该方法有readin()调用，不需要再构造方法中执行
        if (cardNum == 1) {
            nodeSetList.add(new NodeSet(nodeSetNum));
            nodeSetNum++;
        } else {
            int tempNum = nodeSetList.size();//当前链表的大小
            //因为点集一般少于10个，所以大量的查找链表数据可以接受
            NodeSet nodeSet1 = nodeSetList.get(tempNum - 1);
            //nodeSet即为line所包含的数据应存入的点集
            nodeSet1.addNodebyString(line);
        }
    }

    public int[][] getNodeSet() {
        int[][] nodeIdSet = new int[nodeSetList.size()][];
        Iterator<NodeSet> it;
        int i = 0;
        it = nodeSetList.iterator();
        while (it.hasNext()) {
            nodeIdSet[i] = it.next().getNodeIdSet();
            i++;
        }
        return nodeIdSet;
    }

    public Point3D[] getPoint3DListFromIndex0() {
        int pointNum = pointList.length - 1;
        Point3D[] resultList = new Point3D[pointNum];
        for (int i = 0; i <= pointNum - 1; i++) {
            resultList[i] = pointList[i + 1];
        }
        return resultList;
    }

    public Point3D[] getPoint3DListFromIndex1() {
        return pointList;
    }

    public EleQuad_8[] getElementSolidListFromIndex0_ansys() {

        return elementList_ansys;
    }

    public EleQuad_8[] getElementSolidListFromIndex0() {

        return elementList_normal;
    }

    public EleQuad_8[] getElementSolidListFromIndex1_ansys() {
        EleQuad_8[] resultList = new EleQuad_8[elementList_ansys.length + 1];
        resultList[0] = null;
        for (int i = 1; i < resultList.length; i++) {
            resultList[i] = elementList_ansys[i - 1];
        }
        return resultList;
    }

    public Part[] getParts() {
        Part[] parts = new Part[partList.size()];
        parts = partList.toArray(parts);
        return parts;
    }

    class NodeSet {

        int nodeSetId;
        ArrayList<Integer> point3DList = new ArrayList<>();

        public NodeSet(int i) {
            this.nodeSetId = i;
        }

        public void addNodebyString(String line) {
            int tempLineLength = line.trim().length();

            while (tempLineLength >= 1) {
                point3DList.add(Integer.parseInt(line.substring(0, 10).trim()));
                line = line.substring(10, line.length());
                tempLineLength = tempLineLength - 10;
            }
        }

        public int[] getNodeIdSet() {
            int[] result = new int[point3DList.size()];
            Iterator<Integer> it;
            it = point3DList.iterator();
            int index = 0;
            while (it.hasNext()) {
                result[index] = it.next();
                index++;
            }
            return result;
        }

    }

}
