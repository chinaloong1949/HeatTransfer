/*
 * 该类用到了DrawCurve类
 */
package MATERIAL;

import Curve.CurveFrame;
import common.FileOperate;
import common.XStreamUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import point.Point2D;

/**
 *
 * @author yk
 */
public class MaterialFrame extends JPanel {

    private JPopupMenu popupMenu = null;
    private ArrayList<Material> materialList = new ArrayList<>();

    private JTable table1;
    private JTable table2;
    private JTable table3;
    private MaterialFrame materialFrame = null;
    private File materialsDir;

    JInternalFrame leftUpFrame = new JInternalFrame();
    JInternalFrame leftDownFrame = null;
    JInternalFrame rightUpFrame = new JInternalFrame();
    JInternalFrame rightDownFrame = new JInternalFrame();

    public MaterialFrame(String fileName) {
        File file = new File(fileName);
        new MaterialFrame(file);
    }

    public MaterialFrame(File file) {
        if (file != null) {
            this.materialsDir = file;
        } else {
            this.materialsDir = new File("materialXML");
        }
        initFrame();
    }

    public MaterialFrame() {
        //构造右键菜单
        materialsDir = new File("materialXML");
        initFrame();
    }

    private void initFrame() {
        initPopupMenu();
        initMaterialList();

        //this.setFrameIcon(imageIcon);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel mainSplitPane = getMainSplitPane();
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        Box buttonBox = Box.createHorizontalBox();
        JButton saveButton = new JButton("保存");
        JButton resetButton = new JButton("重置");
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(saveButton);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(resetButton);
        buttonBox.add(Box.createHorizontalGlue());
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonBox, BorderLayout.SOUTH);
        this.setVisible(true);
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveTable();
            }
        });

        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resetMaterialFrame();
            }
        });
    }

    private void saveTable() {
        //获得table1中的材料列表，分别存储各个材料到"材料名.xml"文件中
        int rows = table1.getRowCount();
        Material material;
        String materialFile;
        String materialName;
        String materialDes;
        int row;
        try {
            row = table1.getSelectedRows()[0];
        } catch (Exception e) {
            return;
        }
        materialFile = (String) table1.getValueAt(row, 2);
        materialName = (String) table1.getValueAt(row, 1);
        materialDes = (String) table1.getValueAt(row, 4);
        System.out.println("material=" + materialFile);
        int id = -1;
        //如果当前编辑的材料已经存在于材料库中，保持其材料编号不变，更新其他材料属性
        File fileCurrent = new File(materialsDir, materialFile);
        if (fileCurrent.exists()) {
            FileOperate fo = new FileOperate(fileCurrent);
            String materialXml = fo.readFromFileStringWhole(fileCurrent);
            id = XStreamUtil.MaterialFromXML(materialXml).getMaterilId();
        } else {
            //如果当前编辑的材料是新增加的材料
            //此处不会是新增的材料，因为在添加材料是已经创建了材料对应的文件，
            //见addNewMat(String materialName,String fileName)
        }
        material = new Material(id, materialName);
        material.setSource(materialFile);
        material.setDescribe(materialDes);
        int table3Row = table3.getRowCount();
        for (int i = 1; i < table3Row; i++) {
            String item = (String) table3.getValueAt(i, 1);
            switch (item) {
                case "密度": {
                    String ss = String.valueOf(table3.getValueAt(i, 2));
                    ss = ss.trim();
                    material.setDensity(Double.parseDouble(ss));
                    break;
                }
                case "杨氏模量": {
                    String ss = String.valueOf(table3.getValueAt(i, 2));
                    ss = ss.trim();
                    material.setYangShiMoLiang(Double.parseDouble(ss));
                    break;
                }
                case "泊松比": {
                    String ss = String.valueOf(table3.getValueAt(i, 2));
                    ss = ss.trim();
                    material.setBoSongBi(Double.parseDouble(ss));
                    break;
                }
                case "体积模量": {
                    String ss = String.valueOf(table3.getValueAt(i, 2));
                    ss = ss.trim();
                    material.setTiJiMoLiang(Double.parseDouble(ss));
                    break;
                }
                case "剪切模量": {
                    String ss = String.valueOf(table3.getValueAt(i, 2));
                    ss = ss.trim();
                    material.setJianQieMoLiang(Double.parseDouble(ss));
                    break;
                }
                case "屈服强度": {
                    String ss = String.valueOf(table3.getValueAt(i, 2));
                    ss = ss.trim();
                    material.setQuFuQiangDu(Double.parseDouble(ss));
                    break;
                }
                case "粘度":{
                    String ss=String.valueOf(table3.getValueAt(i, 2));
                    ss=ss.trim();
                    material.setMiu(Double.parseDouble(ss));
                }
                case "导热系数": {
                    String ss = String.valueOf(table3.getValueAt(i, 2));
                    ss = ss.trim();
                    material.setDaorexishu(Double.parseDouble(ss));
                }
            }
        }
        saveMaterial(material, materialFile);//保存当前编辑的材料
    }

    private void saveMaterial(Material material, String fileName) {
        File file = new File(materialsDir, fileName);
        saveMaterial(material, file);
    }

    private void saveMaterial(Material material, File materialFile) {
        String materialXml = XStreamUtil.materialToXML(material);
        FileOperate fo = new FileOperate(materialFile);
        fo.writeToFileString(materialXml, false);//false表示覆盖已有文件
    }

    private void resetMaterialFrame() {
//        System.out.println("resetMaterialFrame!");
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BorderLayout());
//        JPanel mainSplitPane = getMainSplitPane();
//        this.removeAll();
//        mainPanel.add(mainSplitPane, BorderLayout.CENTER);
//        
//        Box buttonBox = Box.createHorizontalBox();
//        JButton saveButton = new JButton("保存");
//        JButton resetButton = new JButton("重置");
//        buttonBox.add(Box.createHorizontalGlue());
//        buttonBox.add(saveButton);
//        buttonBox.add(Box.createHorizontalGlue());
//        buttonBox.add(resetButton);
//        buttonBox.add(Box.createHorizontalGlue());
//        this.setLayout(new BorderLayout());
//        this.add(mainPanel, BorderLayout.CENTER);
//        this.add(buttonBox, BorderLayout.SOUTH);
//        this.setVisible(true);
//        saveButton.addActionListener(new ActionListener() {
//            
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                saveTable();
//            }
//        });
//        
//        resetButton.addActionListener(new ActionListener() {
//            
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                resetMaterialFrame();
//            }
//        });
    }

    public Material[] getMaterials() {
        Material[] materials = new Material[materialList.size()];

        for (int i = 0; i < materials.length; i++) {
            materials[i] = materialList.get(i);
        }
        return materials;
    }

    private void initPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem newMatItem = new JMenuItem("新建材料");
        JMenuItem refreshItem = new JMenuItem("重新加载材料库");
        JMenuItem close11Item = new JMenuItem("关闭大纲示意窗口");
        JMenuItem close12Item = new JMenuItem("关闭属性表窗口");
        JMenuItem close21Item = new JMenuItem("关闭属性大纲窗口");
        JMenuItem close22Item = new JMenuItem("关闭属性曲线图窗口");
        JMenuItem saveItem = new JMenuItem("保存");
        JMenuItem resetItem = new JMenuItem("重置材料库");
        popupMenu.add(newMatItem);
        popupMenu.add(refreshItem);
        popupMenu.addSeparator();
        popupMenu.add(close12Item);
        popupMenu.add(close22Item);
        popupMenu.add(close21Item);
        popupMenu.add(close11Item);
        popupMenu.addSeparator();
        popupMenu.add(saveItem);
        popupMenu.add(resetItem);
        newMatItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void initMaterialList() {

        //从XML文件中读取材料信息
        if (!materialsDir.exists()) {
            //创建文件夹
            materialsDir.mkdirs();
        } else if (materialsDir.listFiles().length == 0) {
            //没有相关材料文件，此处不执行操作           

        } else {
            //读入材料文件，添加材料到材料链表里
            File[] xmlFiles = materialsDir.listFiles();
            for (File readFile : xmlFiles) {
                if (readFile.getName().endsWith(".xml")) {
                    InputStream in;
                    try {
                        in = new FileInputStream(readFile);
                        Scanner inScan = new Scanner(in);
                        String inXml = "";
                        while (inScan.hasNextLine()) {
                            inXml = inXml + inScan.nextLine();
                        }
                        in.close();
                        materialList.add(XStreamUtil.MaterialFromXML(inXml));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private JPanel getMainSplitPane() {
        JPanel jPanel = new JPanel();

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JScrollPane leftUpScrollPane;
        JScrollPane leftDownScrollPane;
        JScrollPane rightUpScrollPane;
        JScrollPane rightDownScrollPane;

        Material[] materials = new Material[materialList.size()];
        materials = materialList.toArray(materials);
        table1 = getMaterialNameTable(materials);
        table1.setAutoResizeMode(2);
        //添加右键操作菜单
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Point p = e.getPoint();
                    popupMenu.show(leftUpFrame, p.x, p.y);
                    popupMenu.setVisible(true);
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    popupMenu.setVisible(false);
                    int row = table1.getSelectedRow();
                    int column = table1.getSelectedColumn();
                    System.out.println("row=" + row);
                    System.out.println("column=" + column);
                    Material selectMat = dealClickOnCell(1, table1, row, column);
                    changeInnerFrame(selectMat);
                } else {
                    popupMenu.setVisible(false);
                }
            }
        });
        leftUpScrollPane = new JScrollPane(table1);
        leftDownScrollPane = new JScrollPane(getMaterialPropertyTable(null));
        rightUpScrollPane = new JScrollPane(getMaterialLifeTable(null));
        rightDownScrollPane = new JScrollPane(getMaterialPropertyChart(null));

        leftUpFrame.setTitle("大纲示意表：工程材料库");
        leftUpFrame.setClosable(true);
        //leftUpFrame.setIconifiable(true);//可最小化
        //leftUpFrame.setMaximizable(true);
        leftUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        leftUpFrame.setLayout(new BorderLayout());
        leftUpFrame.add(leftUpScrollPane, BorderLayout.CENTER);
        leftUpFrame.setVisible(true);
        //leftUpFrame.setPreferredSize(new Dimension(400, 600));

        if (leftDownFrame == null) {//初始化时的leftDownFrame操作
            leftDownFrame = new JInternalFrame();
            leftDownFrame.setTitle("属性大纲：Null Material");
            leftDownFrame.setClosable(true);
            //leftDownFrame.setIconifiable(true);//可最小化
            //leftDownFrame.setMaximizable(true);
            leftDownFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            leftDownFrame.setLayout(new BorderLayout());
            leftDownFrame.add(leftDownScrollPane, BorderLayout.CENTER);
            leftDownFrame.setVisible(true);
        }

        rightUpFrame.setTitle("属性表：应力-寿命参数");
        rightUpFrame.setClosable(true);//可关闭
        //rightUpFrame.setIconifiable(true);//可最小化
        //rightUpFrame.setMaximizable(true);//可最大化
        rightUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rightUpFrame.setLayout(new BorderLayout());
        rightUpFrame.add(rightUpScrollPane, BorderLayout.CENTER);
        rightUpFrame.setVisible(true);

        rightDownFrame.setTitle("属性曲线图：应力-寿命参数");
        //rightDownFrame.setIconifiable(true);
        rightDownFrame.setClosable(true);
        //rightDownFrame.setMaximizable(true);
        rightDownFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rightDownFrame.setLayout(new BorderLayout());
        rightDownFrame.add(rightDownScrollPane, BorderLayout.CENTER);
        rightDownFrame.setVisible(true);

        leftUpFrame.setBorder(BorderFactory.createLineBorder(Color.RED));
        leftDownFrame.setBorder(BorderFactory.createLineBorder(Color.RED));
        rightUpFrame.setBorder(BorderFactory.createLineBorder(Color.RED));
        rightDownFrame.setBorder(BorderFactory.createLineBorder(Color.RED));

        leftSplitPane.setDividerSize(2);
        leftSplitPane.add(leftUpFrame);
        leftSplitPane.add(leftDownFrame);
        leftSplitPane.setDividerLocation(300);
        leftSplitPane.setContinuousLayout(true);
        rightSplitPane.setDividerSize(2);
        rightSplitPane.add(rightUpFrame);
        rightSplitPane.add(rightDownFrame);
        rightSplitPane.setDividerLocation(300);
        rightSplitPane.setContinuousLayout(true);

        mainSplitPane.setDividerSize(7);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.add(leftSplitPane);
        mainSplitPane.add(rightSplitPane);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setDividerLocation(400);
        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.add(mainSplitPane);

        jPanel.setLayout(new BorderLayout());
        jPanel.add(mainSplitPane, BorderLayout.CENTER);

        return jPanel;

    }

    private void changeInnerFrame(Material material) {
        if (material != null) {
            leftDownFrame.setTitle("属性大纲：" + material.getName());
        } else {
            leftDownFrame.setTitle("属性大纲：新材料编辑中……");
        }

        leftDownFrame.setClosable(true);
        //leftDownFrame.setIconifiable(true);//可最小化
        //leftDownFrame.setMaximizable(true);
        leftDownFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTable jPanel = getMaterialPropertyTable(material);
        JScrollPane leftDownScrollPane = new JScrollPane(jPanel);
        leftDownFrame.getContentPane().removeAll();

        leftDownFrame.getContentPane().add(leftDownScrollPane, BorderLayout.CENTER);
        leftDownFrame.setVisible(true);
        leftDownFrame.repaint();
        leftDownFrame.revalidate();
    }

    private JTable getMaterialNameTable(Material[] materials) {

        JTable table = new JTable(1, 6);
        Object[] title = {1, "工程数据内容", "源", "添加", "描述", "操作"};

        addRow(1, table, 0, title);//标题行是纯文本，故tab==1；
        Object[] title1 = {2, "材料"};

        addRow(3, table, 1, title1);
//        Object[] material_0 = {3, "结构钢", "Structuralsteel.xml", "零摄氏度时的疲劳数据，平均应力取自1998，ASME BPV code，第八部分第二节表5-110.1"};
//        addRow(0, table, 2, material_0);
        if (materials.length == 0) {
            Object[] material_2 = {3, "点击添加新材料"};
            addRow(3, table, 2, material_2);
        } else {
            for (int i = 0; i <= materials.length - 1; i++) {
                Material tempM = materials[i];
                String name = tempM.getName();
                String source = tempM.getSource();
                String describe = tempM.getDescribe();
                boolean isSelected = tempM.isSelected();
                String[] action = new String[]{"保存", "删除"};
                JComboBox comboBox = new JComboBox(action);
                comboBox.setSelectedIndex(0);
                table.getColumnModel().getColumn(5).setCellEditor(
                        new DefaultCellEditor(comboBox));
//                comboBox.setModel(new DefaultComboBoxModel(action));
                Object[] material_1 = {i + 3, name, source, isSelected,
                    describe};
                addRow(2, table, i + 2, material_1);
            }
            Object[] material_2 = {(materials.length + 2), "点击添加新材料"};
            addRow(3, table, materials.length + 2, material_2);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
//            TableColumn firstColumn = table.getColumnModel().getColumn(0);
//            firstColumn.setPreferredWidth(30);
//            TableColumn secondColumn = table.getColumnModel().getColumn(1);
//            secondColumn.setPreferredWidth(50);
//            TableColumn thirdColumn = table.getColumnModel().getColumn(2);
//            thirdColumn.setPreferredWidth(80);
            table.setAutoscrolls(true);
            table.setColumnSelectionAllowed(true);
            table.setEnabled(true);
            table.setCellSelectionEnabled(true);

        }
        //new TableSetting().setColor(table, 2, 2, Color.yellow);

        table.repaint();
        table.updateUI();
        return table;
    }

    private JTable getMaterialPropertyTable(Material material) {

        table3 = new JTable(1, 4);
        if (material == null) {
            material = new Material(-1, "null material");
        }
        Object[] title = {1, "属性", "值", "单位"};
        addRow(1, table3, 0, title);
        int dataNum = 8;

        Object[][] data = new Object[dataNum][4];

        for (int i = 0; i <= dataNum - 1; i++) {
            data[i][0] = i + 2;
        }

        data[0][1] = "密度";
        data[0][2] = material.getDensity();
        data[0][3] = "kg m^-3";

        data[1][1] = "杨氏模量";
        double temp = material.getYangShiMoLiang();
        data[1][2] = temp;
        data[1][3] = "Pa";

        data[2][1] = "泊松比";
        data[2][2] = material.getBoSongBi();
        data[2][3] = "";

        data[3][1] = "体积模量";
        data[3][2] = material.getTiJiMoLiang();
        data[3][3] = "Pa";

        data[4][1] = "剪切模量";
        data[4][2] = material.getJianQieMoLiang();
        data[4][3] = "Pa";

        data[5][1] = "屈服强度";
        data[5][2] = material.getQuFuQiangDu();
        data[5][3] = "Pa";

        data[6][1] = "粘度";
        data[6][2] = material.getMiu();
        data[6][3] = "Pa *s";

        data[7][1] = "导热系数";
        data[7][2] = material.getDaorexishu();
        data[7][3] = "W/(m *K)";
        
        for (int i = 0; i <= dataNum - 1; i++) {
            addRow(1, table3, i + 1, data[i]);
        }

        table3.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableColumn firstColumn = table3.getColumnModel().getColumn(0);
        firstColumn.setPreferredWidth(30);
        TableColumn secondColumn = table3.getColumnModel().getColumn(1);
        secondColumn.setPreferredWidth(50);
        TableColumn thirdColumn = table3.getColumnModel().getColumn(2);
        thirdColumn.setPreferredWidth(80);
        table3.setEnabled(true);
        table3.setAutoCreateColumnsFromModel(true);
        table3.setAutoscrolls(true);
        table3.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                //表格三的鼠标事件处理
                if (e.getButton() == MouseEvent.BUTTON3) {
//                    Point p = e.getPoint();
//                    popupMenu.show(leftUpFrame, p.x, p.y);
//                    popupMenu.setVisible(true);
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    int row = table1.getSelectedRow();
                    int column = table1.getSelectedColumn();
                    dealClickOnCell(3, table3, row, column);
                    //System.out.println(table3.getValueAt(1, 2));
                } else {
                }
            }
        });

        return table3;
    }

    private JTable getMaterialLifeTable(Material material) {
        JTable table = new JTable(1, 7);
        Object[] title = {1, "强度系数", "强度指数", "延展系数",
            "延展指数", "周期强度系数", "周期应力硬化指数"};
        addRow(1, table, 0, title);
        Object[] data = {2, 9.2E+8, -0.106, 0.213, -0.47, 1E+9, 0.2};
        addRow(1, table, 1, data);
        return table;
    }

    private JPanel getMaterialPropertyChart(Material material) {
        JPanel panel = new JPanel();
        double[] dataX = {};
        double[] dataY = {};
        Point2D[] point = new Point2D[2];
        point[0] = new Point2D(0, 0);
        point[1] = new Point2D(100, 100);
        CurveFrame drawCurve = new CurveFrame(point, "", "材料寿命",
                "应力", "寿命");
        JPanel curvePanel = drawCurve.getCurvePanel();
        panel.setLayout(new BorderLayout());
        panel.add(curvePanel, BorderLayout.CENTER);
        return panel;
    }

    private void addNewMat(String materialName, String fileName) {
        //将新材料添加到materialList中
        int id = materialList.size();
        Material material = new Material(id, materialName);
        material.setSource(fileName);
        materialList.add(material);

        //将该材料写入材料库文件夹materialsDir中
        String materialXML = XStreamUtil.materialToXML(material);
        FileOperate fo = new FileOperate(new File(materialsDir, fileName));
        fo.writeToFileString(materialXML, false);//false表示覆盖已有文件
    }

    private Material dealClickOnCell(int tab, JTable table,
            int row, int column) {
        Material material = null;
        if (tab == 1) {//表示单击事件是左上方窗口触发的
            System.out.println("comein 464MaterialFrame:");
            String tempStr = "点击添加新材料";
            int rowNum = table.getRowCount();
            if (column == 1) {
                if (table.getValueAt(row, column).equals(tempStr)) {
                    //如果点击的是“点击添加新材料”，将该单元置空，并进入编辑状态
                    table.setEditingRow(row);
                    table.setValueAt("", row, column);
                    table.editCellAt(row, column);//该语句不能去掉，是单元保持编辑状态

                    return null;
                }
            }
            if (!table.getValueAt(rowNum - 1, 1).
                    equals(tempStr)) {
                //判断倒数第一行第二列是不是“点击添加新材料”，如果不是
                //表明上次编辑的是该单元，如果该单元为空，提示“材料名不能为空”。
                //如果不为空，表明新建材料正确，自动更新源文件名，在表格后面添加新行。
                if (((String) table.getValueAt(rowNum - 1, 1))
                        .trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "材料名不能为空，"
                            + "请重新编写！");
                    table.setValueAt(tempStr, table.getRowCount() - 1, 1);
                } else {

                    String source = table.getValueAt(rowNum - 1, 1) + ".xml";
                    String[] action = {"保存", "删除"};
                    JComboBox comboBox = new JComboBox(action);
                    table.getColumnModel().getColumn(5).setCellEditor(
                            new DefaultCellEditor(comboBox));
                    table.setValueAt(source, rowNum - 1, 2);
                    addNewMat((String) table.getValueAt(rowNum - 1, 1), source);
                    Object[] material_2 = {(rowNum + 1), "点击添加新材料"};
                    addRow(1, table, rowNum, material_2);

                }
            }
            if (row == rowNum - 1) {
                //如果是倒数第一行，则表明当前选择行没有材料信息，返回空
                return null;
            } else {
                String fileName = ((String) table.getValueAt(row, 2))
                        .trim();
                File file = new File(materialsDir, fileName);

                if (file.exists()) {
                    InputStream is = null;
                    try {
                        is = new FileInputStream(file);
                        Scanner inSca = new Scanner(is);
                        String inStr = "";
                        while (inSca.hasNextLine()) {
                            inStr = inStr + inSca.nextLine();
                        }
                        material = XStreamUtil.MaterialFromXML(inStr);

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MaterialFrame.class.getName())
                                .log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            is.close();
                        } catch (IOException ex) {
                            Logger.getLogger(MaterialFrame.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }//表格1的单击时间处理完成
        else if (tab == 2) {

        } else if (tab == 3) {
            //对表格三的鼠标点击处理
        }
        return material;
    }

    /**
     * 
     * @param tab   tab==0表示每次写入一行保证table后有一个空行
     * @param table 写入的table变量名
     * @param rowNum    写入的数据所在的行号
     * @param data 写入的数据
     */
    private void addRow(int tab, JTable table, int rowNum, Object[] data) {
        //tab==0表示每次写入数据后保证table后有一个空行

        if (tab == 0 || tab == 1) {
            //纯文本表格添加方式
            if (table.getRowCount() > rowNum) {
                for (int i = 0; i <= data.length - 1; i++) {
                    table.setValueAt(data[i], rowNum, i);
                }
                if (tab == 0) {
                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    Object[] nullRow = new Object[1];
                    nullRow[0] = "";
                    dtm.addRow(nullRow);
                }
            } else {
                DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                Object[] nullRow = new Object[1];
                nullRow[0] = "";
                dtm.addRow(nullRow);
                addRow(tab, table, rowNum, data);
            }
        } else if (tab == 2 || tab == 3) {
            //针对table1的设置
            if (table.getRowCount() > rowNum) {
                for (int i = 0; i <= data.length - 1; i++) {
                    if (i != 6) {
                        table.setValueAt(data[i], rowNum, i);
                    }
                }
                if (tab == 2) {
                    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                    Object[] nullRow = new Object[1];
                    nullRow[0] = "";
                    dtm.addRow(nullRow);
                }
            } else {
                DefaultTableModel dtm = (DefaultTableModel) table.getModel();
                Object[] nullRow = new Object[1];
                nullRow[0] = "";
                dtm.addRow(nullRow);
                addRow(tab, table, rowNum, data);
            }
        }

    }

}
