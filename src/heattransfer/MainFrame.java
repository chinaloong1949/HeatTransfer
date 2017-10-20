/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heattransfer;

import Curve.DisplayUI;
import GUI.JImagePane;
import GUI.SuperSet;
import GUI.TimePanel;
import MATERIAL.Material;
import MATERIAL.MaterialFrame;
import common.Boundary;
import common.CalculateInfoBag;
import common.FileChooser;
import common.FileOperate;
import common.MailTo;
import common.SetInfo;
import joggle.ReadKeyFile;

import common.XStreamUtil;
import element.EleQuad_8;
import element.Part;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import point.Point3D;
import simple.CompareTool;
import simple.OpenFVM;
//import simple.Solid2D;
import simple.Temp3D;
import simple.ThreadDispatch;

/**
 *
 * @author yk
 */
public class MainFrame extends JFrame {

    private JPanel centerPanel = new JPanel();
    private JSplitPane contentPanel;
    private JSplitPane leftPanel;
    private JPanel rightPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JPanel bottomPanel = new JPanel();
    private JPanel leftBottomPanel = new JPanel();
    private JPanel jPanel1 = new JPanel();
    private JPanel timePanel;
    private JLabel timeLabel;
    private JTree tree;
    private JPanel displayUI = null;
    private SetInfo setInfo;

    public File k_file = null;
    private Part[] parts;
    private EleQuad_8[] eleQuad_8;
    private int elementNum = 0;
    private Point3D[] node;
    private int nodeNum = 0;
    private int[][] nodeIdSet = null;
    private int analysisType = 0;//0定常；1非定常
    private double analysisType_totalTime = 0.1;
    private int analysisType_timeSteps = 100;
    private double analysisType_initialTime = 0;
    private Material[] materialArr = null;//材料库中拥有的材料
    private String material = "水";//计算选择的材料
    private double referPressure = 101325;
    private int maxIterNum;
    private double maxerr;
    private ArrayList<Boundary> boundary = new ArrayList<>();
    private MaterialFrame materialFrame = null;

    DefaultMutableTreeNode node4;
    DefaultMutableTreeNode node2_1;

    String splitChar = ",";
    private int gaussPointNum = 4;
    private String command = "对角线归一代入法";

    private int currentPart = 0;//当前正在操作的part索引号

    @SuppressWarnings("Convert2Lambda")
    public MainFrame(SetInfo setInfo, int xLocation, int yLocation, int width, int height) {

        this.setInfo = setInfo;
        if (setInfo == null) {
            this.setInfo = new SetInfo();
        }
        initData(0);
        File file = new File("title.txt");
        String lineText = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileInputStream, "GBK");
            BufferedReader bufferedReader = new BufferedReader(reader);

            lineText = bufferedReader.readLine();
            System.out.println("软件名为：" + lineText);

        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setTitle(lineText);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("文件");
        JMenu operateMenu = new JMenu("操作");
        JMenu searchMenu = new JMenu("查询");
        JMenu setMenu = new JMenu("设置");
        JMenu toolMenu = new JMenu("工具");
        JMenu helpMenu = new JMenu("帮助");

        JMenuItem openItem = new JMenuItem("打开");
        JMenuItem saveItem = new JMenuItem("保存计算设置");
        JMenuItem openSetInfoItem = new JMenuItem("读取计算设置");
        JMenuItem newItem = new JMenuItem("新建APDL文件");
        JMenuItem exportMeshItem = new JMenuItem("export mesh to tecplot");
        JMenuItem exitItem = new JMenuItem("退出");

        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(openItem);
        fileMenu.add(openSetInfoItem);
        fileMenu.add(exportMeshItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenuItem setItem0 = new JMenuItem("文件分割符设置……");
        JMenuItem setItem1 = new JMenuItem("输出到Excel表格……");
        JMenuItem setItem2 = new JMenuItem("插入边界条件……");
        JMenuItem setItem3 = new JMenuItem("开始求解");
        JMenuItem setItem4 = new JMenuItem("命令窗口");
        JMenuItem setItem8 = new JMenuItem("流动换热分析");
        JMenuItem setItem5 = new JMenuItem("Solid2D求解");
        JMenuItem setItem6 = new JMenuItem("固体温度场计算");
        JMenuItem setItem7 = new JMenuItem("结构参数优化");

        operateMenu.add(setItem0);
        operateMenu.add(setItem1);
        operateMenu.add(setItem2);
        operateMenu.addSeparator();
        operateMenu.add(setItem3);
        operateMenu.addSeparator();
        operateMenu.add(setItem4);
        operateMenu.addSeparator();
        operateMenu.add(setItem5);
        operateMenu.addSeparator();
        operateMenu.add(setItem8);
        operateMenu.add(setItem6);
        operateMenu.add(setItem7);

        JMenuItem searchMenu0 = new JMenuItem("设计变量取值建议范围列表");
        searchMenu.add(searchMenu0);

        JMenuItem setSuperItem = new JMenuItem("高级设置");

        setMenu.add(setSuperItem);

        JMenuItem compareItem = new JMenuItem("txt对比工具");

        toolMenu.add(compareItem);

        JMenuItem helpItem = new JMenuItem("联系作者");

        helpMenu.add(helpItem);

        menuBar.add(fileMenu);
        menuBar.add(operateMenu);
        menuBar.add(searchMenu);
        menuBar.add(setMenu);
        menuBar.add(toolMenu);
        menuBar.add(helpMenu);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        topPanel.setLayout(new BorderLayout());
        topPanel.add(menuBar, BorderLayout.WEST);

        //现在centerP下放置一个JScrollPane,放置中间面板需要容纳内容过多显示不全
        //scrollPane下放置一个普通面板，因为scrollPane不能设置布局
        contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        //contentPanel.setLayout(new BorderLayout());
        //contentPanel分为左右两部分，左侧上方放置一个树状结构，左侧下方放置一个成员面板，右侧是另外一个响应面板
        rightPanel = getContentPanel(0, null);

        leftPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        JScrollPane leftBottomPane;
        leftBottomPanel.setLayout(new BorderLayout());
        leftBottomPane = new JScrollPane(leftBottomPanel);
        JScrollPane treeScrollPane = new JScrollPane(initTree());
        treeScrollPane.setPreferredSize(new Dimension(250, 600));
        leftPanel.add(treeScrollPane);
        leftPanel.add(leftBottomPane);

        contentPanel.add(leftPanel);
        leftPanel.setDividerLocation(0.7);
        leftPanel.setOneTouchExpandable(true);

        contentPanel.add(rightPanel);
        contentPanel.setDividerLocation(230);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(contentPanel, BorderLayout.CENTER);

        bottomPanel = getBottomPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (width != 0 && height != 0) {
            screenSize.setSize(width, height);
        } else {
            if (screenSize.getWidth() > 1366) {
                screenSize.setSize(1366, screenSize.getHeight());
            }
            if (screenSize.getHeight() > 768) {
                screenSize.setSize(screenSize.getWidth(), 768);
            }
        }
        this.setLocation(xLocation, yLocation);
        this.setSize(screenSize);

        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (boundary != null && boundary.size() >= 1) {
            for (int i = 0; i <= boundary.size() - 1; i++) {
                String nodeName = boundary.get(i).getBoundaryName();
                addNodeToTree(getTree(), node2_1, nodeName, 0);
                changePanel(14, nodeName);
            }
        }

        openItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openFile(1);
            }
        });

        openSetInfoItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File file = openFile(1);
                readSetInfo(file);
            }
        });

        saveItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File file = saveData(1);
                saveSetInfoToFile(file);
            }
        });

        newItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String nodeName = addNodeToTree(getTree(), node4, 1);
                changePanel(13, nodeName);
            }
        });

        exportMeshItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportMesh(0);
            }
        });

        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setItem0.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setSuper("splitChar", null);

            }
        });

        setItem1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveToExcel();
            }
        });

        setItem2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String nodeName = addNodeToTree(getTree(), node2_1, 0);
                changePanel(14, nodeName);
            }
        });

        setItem3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                calculate(0, null);
            }
        });

        setItem4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openFVM(0, null);
            }
        });

        setItem5.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                calculate(1, null);
            }
        });

        setItem6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculate(2, null);//温度场计算
            }
        });

        setItem7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanel(20, null);
            }
        });

        setItem8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePanel(-1, null);
            }
        });

        setSuperItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                superSet("gaussPointNum", null);
            }

        });

        compareItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new CompareTool(null);
            }
        });

        helpItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new MailTo("540673597@qq.com");
            }
        });

    }

    private JPanel getBottomPanel() {
        timePanel = new JPanel();
        JPanel bottomP = new JPanel();
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();
        Box box1 = Box.createHorizontalBox();
        jPanel1.add(new JLabel("Copyright @2014 XJTU, All Rights Reserved"));
        jPanel2.add(new JLabel("      "));
        timeLabel = new JLabel("时间");
        timePanel.setLayout(new BorderLayout());
        timePanel.add(timeLabel, BorderLayout.CENTER);
        jPanel3.add(timePanel);
        box1.add(jPanel1);
        box1.add(Box.createHorizontalGlue());
        box1.add(jPanel2);
        box1.add(jPanel3);
        box1.add(Box.createHorizontalGlue());
        bottomP.add(box1);

        TimePanel timePanel = new TimePanel(this);
        Thread thread1 = new Thread(timePanel);
        thread1.start();
        return bottomP;
    }

    public void replaceTimePanel() {
        timePanel.remove(timeLabel);
        Date now;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar cc = Calendar.getInstance();
        now = cc.getTime();
        String dateStr = sdf.format(now);
        //System.out.println("time=" + dateStr);
        timeLabel = new JLabel(dateStr);
        timePanel.add(timeLabel);
        timeLabel.revalidate();//这句不能省略
    }

    private File openFile(int tab) {
        File file = null;
        switch (tab) {
            case 0:
                String[] fileType0 = {"k"};
                file = new FileChooser("选择数据文件", 1, fileType0).getFile();
                break;
            case 1:

                String[] fileType1 = {"xml", "html"};
                file = new FileChooser("选择设置文件", 1, fileType1).getFile();
                break;
            case 2:
                String[] fileType2 = {"dat", "txt"};
                file = new FileChooser("选择数据文件", 1, fileType2).getFile();

                break;
            case 3:
                String[] fileType3 = {"tmp", "txt"};
                file = new FileChooser("选择数据文件", 1, fileType3).getFile();

                break;
            case 4:
                String[] fileType4 = {"dat", "txt"};
                file = new FileChooser("选择数据文件", 1, fileType4).getFile();

                break;
            default:
                JOptionPane.showMessageDialog(null, "输入标志常量错误，当前为" + tab);
                break;
        }
        return file;

    }

    private void exportMesh(int tab) {
        File file = new FileChooser("export tecplot grd file(dat)", 2, new String[]{".dat"}).getFile();
        if (file.exists()) {
            System.out.println(file.getAbsoluteFile() + " already exists, overwrite it!");
        } else {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            FileOperate fo = new FileOperate(file);
            String line;
            line = "TITLE = \"grd from " + k_file.getAbsolutePath() + "\"\n";
            fo.writeToFileString(line, false);//first time to write choose 'false', which means overwrite if file exists.
            line = "VARIABLES = \"X\", \"Y\", \"Z\"\n";
            fo.writeToFileString(line, true);
            line = "ZONE NODES = " + nodeNum + ", ELEMENTS = " + elementNum
                    + ", DATAPACKING = POINT, ZONETYPE = FEBRICK\n";
            fo.writeToFileString(line, true);

            for (int i = 0; i < nodeNum; i++) {
                line = "";
                line = line + node[i].getX() + " ";
                line = line + node[i].getY() + " ";
                line = line + node[i].getZ() + "\n";
                fo.writeToFileString(line, true);
            }
            for (int i = 0; i < elementNum; i++) {
                line = "";
                int[] nodeList = eleQuad_8[i].getNodeList();
                for (int j = 0; j < 8; j++) {
                    line = line + nodeList[j] + " ";
                }
                line = line + "\n";
                fo.writeToFileString(line, true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private File saveData(int tab) {
        File file = null;
        switch (tab) {
            case 0:
                String[] fileType0 = {"res", "txt"};
                FileChooser fc0 = new FileChooser("保存文件至", 2, fileType0, "STRESS.RES");

                file = fc0.getFile();
                break;
            case 1:
                String[] fileType1 = {"xml", "html"};
                file = new FileChooser("保存文件至", 2, fileType1, "set.xml").getFile();
                break;
            case 2:
                String[] fileType2 = {"pos", "txt"};
                file = new FileChooser("保存文件至", 2, fileType2, "DISP.POS").getFile();
                break;
            case 3:
                String[] fileType3 = {"pos", "txt"};
                file = new FileChooser("保存文件至", 2, fileType3, "SSTR.POS").getFile();
                break;
            default:
                JOptionPane.showMessageDialog(null, "保存标志量错误，当前为" + tab);
        }
        return file;
    }

    private void initData(int tab) {
        switch (tab) {
            case 0:
                analysisType = setInfo.getAnalysisType();
                analysisType_initialTime = setInfo.getAnalysisType_initialTime();
                analysisType_timeSteps = setInfo.getAnalysisType_timeSteps();
                analysisType_totalTime = setInfo.getAnalysisType_totalTime();
                materialFrame = new MaterialFrame(setInfo.getMaterialInitFile());
                material = setInfo.getMaterial();
                referPressure = setInfo.getReferPressure();
                maxIterNum = setInfo.getMaxIterNum();
                maxerr = setInfo.getMaxErr();
                parts = setInfo.getParts();
                Boundary[] boundaryArr = setInfo.getBoundary();
                if (boundaryArr != null) {
                    for (int i = 0; i <= boundaryArr.length - 1; i++) {
                        boundary.add(boundaryArr[i]);
                    }
                }
                if (setInfo.getK_file() != null) {
                    k_file = setInfo.getK_file();
                    try {
                        readDataFromkFile(k_file);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "关键字文件"
                                + k_file.getPath() + "不存在，请检查设置！");
                    }
                }

                break;
            case 1:

                break;
            case 2:

                break;

            case 3:

                break;
            case 4:

                break;
            default:
                break;
        }
    }

    private JTree initTree() {

        DefaultMutableTreeNode one = new DefaultMutableTreeNode("流场数值模拟");
        tree = new JTree(one);
        tree.setBorder(new javax.swing.border.MatteBorder(null));
        //tree.setPreferredSize(new Dimension(200, 300));
        tree.setAutoscrolls(true);
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("网格数据");
        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("计算设置");
        DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("计算结果");
        node4 = new DefaultMutableTreeNode("文件编辑");

        DefaultMutableTreeNode node1_0 = new DefaultMutableTreeNode("读入k文件");
        DefaultMutableTreeNode node1_1 = new DefaultMutableTreeNode("网格统计数据");
        DefaultMutableTreeNode node1_2 = new DefaultMutableTreeNode("节点信息");
        DefaultMutableTreeNode node1_3 = new DefaultMutableTreeNode("单元信息");
        DefaultMutableTreeNode node1_4 = new DefaultMutableTreeNode("节点集");

        DefaultMutableTreeNode node2_0 = new DefaultMutableTreeNode("计算类型");
        node2_1 = new DefaultMutableTreeNode("流动区域设置");
        DefaultMutableTreeNode node2_2 = new DefaultMutableTreeNode("工程材料库");
        DefaultMutableTreeNode node2_3 = new DefaultMutableTreeNode("边界条件");
        DefaultMutableTreeNode node2_4 = new DefaultMutableTreeNode("求解控制");
        DefaultMutableTreeNode node2_5 = new DefaultMutableTreeNode("计算设置信息");

        DefaultMutableTreeNode node3_0 = new DefaultMutableTreeNode("图形显示");
        DefaultMutableTreeNode node3_1 = new DefaultMutableTreeNode("节点变量值");

        DefaultMutableTreeNode node2_1_0 = new DefaultMutableTreeNode("默认流动区域");

        node1.add(node1_0);
        node1.add(node1_1);
        node1.add(node1_2);
        node1.add(node1_3);
        node1.add(node1_4);

        node2_1.add(node2_1_0);

        node2.add(node2_0);
        node2.add(node2_1);
        node2.add(node2_2);
        node2.add(node2_3);
        node2.add(node2_4);

        node3.add(node3_0);
        node3.add(node3_1);

        one.add(node1);
        one.add(node2);
        one.add(node3);
        one.add(node4);

        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {

                    if (tree.getLastSelectedPathComponent().equals(one)) {
                        changePanel(0, null);
                    } else if (tree.getLastSelectedPathComponent().equals(node1)) {
                        //changePanel(1, null);
                    } else if (tree.getLastSelectedPathComponent().equals(node1_0)) {
                        changePanel(2, null);
                    } else if (tree.getLastSelectedPathComponent().equals(node1_1)) {
                        changePanel(3, null);
                    } else if (tree.getLastSelectedPathComponent().equals(node1_2)) {
                        changePanel(4, null);//节点信息
                    } else if (tree.getLastSelectedPathComponent().equals(node1_3)) {
                        changePanel(5, null);//单元信息
                    } else if (tree.getLastSelectedPathComponent().equals(node1_4)) {
                        changePanel(6, null);//节点集
                    } else if (tree.getLastSelectedPathComponent().equals(node2)) {
                        //changePanel(7, null);
                    } else if (tree.getLastSelectedPathComponent().equals(node2_0)) {
                        changePanel(8, analysisType);//计算整体设置
                    } else if (tree.getLastSelectedPathComponent().equals(node2_1)) {
                        changePanel(9, null);//流动区域设置
                    } else if (tree.getLastSelectedPathComponent().equals(node2_1_0)) {
                        changePanel(-901, null);//工程材料库

                    } else if (tree.getLastSelectedPathComponent().equals(node2_2)) {
                        changePanel(10, null);//工程材料库
                    } else if (tree.getLastSelectedPathComponent().equals(node2_3)) {
                        changePanel(11, null);//边界条件
                    } else if (tree.getLastSelectedPathComponent().equals(node4)) {
                        //addNodeToTree(tree, node4);
                        changePanel(12, null);
                    }//changePanel(13)被菜单file-->newItem使用了
                    else//如果该节点是node4的子节点 
                    if (node4.isNodeChild((DefaultMutableTreeNode) tree.getLastSelectedPathComponent())) {
                        DefaultMutableTreeNode node
                                = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                        String nodeName = (String) node.getUserObject();
                        changePanel(13, nodeName);
                    } else//如果该节点是node2_1的子节点
                    if (node2_1.isNodeChild((DefaultMutableTreeNode) tree.getLastSelectedPathComponent())) {
                        DefaultMutableTreeNode node
                                = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                        String nodeName = (String) node.getUserObject();
                        changePanel(14, nodeName);
                    } else if (tree.getLastSelectedPathComponent().equals(node2_4)) {
                        changePanel(15, null);//求解控制
                    } else if (tree.getLastSelectedPathComponent().equals(node2_5)) {
                        changePanel(16, null);
                    } else if (tree.getLastSelectedPathComponent().equals(node3)) {
                        changePanel(17, null);
                    } else if (tree.getLastSelectedPathComponent().equals(node3_0)) {
                        changePanel(18, null);
                    } else if (tree.getLastSelectedPathComponent().equals(node3_1)) {
                        changePanel(19, null);
                    }
                } catch (Exception ex) {
                    System.out.println("选择了空节点");
                }
            }

        }
        );

        tree.expandRow(0);
        tree.expandRow(1);
        tree.expandRow(2);
        tree.expandRow(3);
        tree.expandRow(4);
        tree.expandRow(5);
        tree.expandRow(6);
        tree.expandRow(7);
        tree.expandRow(8);
        tree.expandRow(9);
        tree.expandRow(10);
        return tree;
    }

    public JTree getTree() {
        return this.tree;
    }

    private String addNodeToTree(JTree tree,
            DefaultMutableTreeNode parentNode, int tab) {
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        String nodeName = null;
        if (tab == 0) {
            while (nodeName == null) {
                nodeName = JOptionPane.showInputDialog(null,
                        "请输入边界条件名称", "新建边界条件",
                        JOptionPane.PLAIN_MESSAGE);
                if (node2_1.getChildCount() >= 2) {
                    for (int i = 0; i <= node2_1.getChildCount() - 1; i++) {
                        String childName = node2_1.getChildAt(i).toString();
                        System.out.println("538MainFrame:childName=" + childName);
                        if (childName.equals(nodeName)) {

                            JOptionPane.showMessageDialog(null, "边界条件名称"
                                    + "已存在，"
                                    + "请重新输入！");
                            nodeName = null;
                        }
                    }
                }
            }
        } else if (tab == 1) {
            nodeName = JOptionPane.showInputDialog(null,
                    "请输入文件名称", "新建APDL", JOptionPane.PLAIN_MESSAGE);
        }

        while (nodeName.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "输入为空！"
                    + "输入内容前后的空格自动忽略！");
            if (tab == 0) {
                nodeName = JOptionPane.showInputDialog(null,
                        "请输入边界条件名称", "新建边界条件",
                        JOptionPane.PLAIN_MESSAGE);
            } else if (tab == 1) {
                nodeName = JOptionPane.showInputDialog(null,
                        "请输入文件名称", "新建APDL", JOptionPane.PLAIN_MESSAGE);
            }
        }

        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(nodeName);
        treeModel.insertNodeInto(childNode, parentNode, 0);
        return nodeName;
    }

    private void addNodeToTree(JTree tree,
            DefaultMutableTreeNode parentNode, String nodeName, int tab) {

        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();

        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(nodeName);
        treeModel.insertNodeInto(childNode, parentNode, 0);
    }

    public void changePanel(int tab, Object object) {

        switch (tab) {

            default:
                if (tab == -1) {//表示从其他模块切回流场计算模块
                    new MainFrame(null, this.getX(), this.getY(),
                            this.getWidth(), this.getHeight());
                    this.dispose();
                } else if (tab <= 19) {
                    contentPanel.remove(rightPanel);
                    rightPanel = getContentPanel(tab, object);
                    contentPanel.add(rightPanel);
                    contentPanel.revalidate();//不加此条语句，则
                    //只有在第二次单机时才会更新rightPanel

                    leftBottomPanel.removeAll();
                    leftBottomPanel.add(getleftBottomPanel(tab, object), BorderLayout.CENTER);
                    leftPanel.revalidate();

                } else {
                    centerPanel.removeAll();
                    centerPanel.add(getContentPanel(tab, object), BorderLayout.CENTER);
                    centerPanel.revalidate();
                }

                break;
        }
    }

    private JPanel getleftBottomPanel(int tab, Object object) {
        JPanel jPanel = new JPanel(new BorderLayout());
        switch (tab) {
            case 0:
                JTextArea textArea0_0 = new JTextArea("");
                textArea0_0.setLineWrap(true);
                jPanel.add(textArea0_0, BorderLayout.CENTER);
                break;
            case 1:
                break;
            case 2:
                JTextArea textArea2_0 = new JTextArea("k文件格式同LS-DYNA软件，"
                        + "具体参考k文件关键字手册！");
                textArea2_0.setLineWrap(true);
                jPanel.add(textArea2_0, BorderLayout.CENTER);
                break;
            case 3:
                JTextArea textArea3_0 = new JTextArea("");
                textArea3_0.setLineWrap(true);
                jPanel.add(textArea3_0, BorderLayout.CENTER);
                break;
            case 4:
                JTextArea textArea4_0 = new JTextArea("");
                textArea4_0.setLineWrap(true);
                jPanel.add(textArea4_0, BorderLayout.CENTER);
                break;
            case 5:
                JTextArea textArea5_0 = new JTextArea("");
                textArea5_0.setLineWrap(true);
                jPanel.add(textArea5_0, BorderLayout.CENTER);
                break;
            case 6:
                JTextArea textArea6_0 = new JTextArea("");

                textArea6_0.setLineWrap(true);
                jPanel.add(textArea6_0, BorderLayout.CENTER);
                break;
            case 7:
                JTextArea textArea7_0 = new JTextArea("请等待计算完成");
                textArea7_0.setBackground(Color.red);
                textArea7_0.setLineWrap(true);
                jPanel.add(textArea7_0, BorderLayout.CENTER);
                break;
            case 8:
                JTextArea textArea8_0 = null;
                if ((int) object == 0) {
                    textArea8_0 = new JTextArea("定常计算");
                } else if ((int) object == 1) {
                    textArea8_0 = new JTextArea("非定常计算");
                } else if ((int) object == 2) {
                    textArea8_0 = new JTextArea("定常计算");
                } else if ((int) object == 3) {
                    textArea8_0 = new JTextArea("非定常计算");
                }
                textArea8_0.setLineWrap(true);
                jPanel.add(textArea8_0, BorderLayout.CENTER);
                break;
            case 9:
                JTextArea textArea9_0 = new JTextArea("");
                textArea9_0.append("");
                textArea9_0.setLineWrap(true);
                jPanel.add(textArea9_0, BorderLayout.CENTER);
                break;
            case 10:
                JTextArea textArea10_0 = new JTextArea("");
                textArea10_0.setLineWrap(true);
                jPanel.add(textArea10_0, BorderLayout.CENTER);
                break;
            case 11:
                JTextArea textArea11_0 = new JTextArea("");
                textArea11_0.setLineWrap(true);
                jPanel.add(textArea11_0, BorderLayout.CENTER);
            default:
                break;
        }
        return jPanel;
    }

    @SuppressWarnings("Convert2Lambda")
    private JPanel getContentPanel(int tab, Object object) {
        jPanel1 = new JPanel();
        jPanel1.setLayout(new BorderLayout());

        switch (tab) {
            case 0:
                JPanel jPanel0_0 = new JImagePane("水流.jpg", JImagePane.SCALED);
                jPanel0_0.setFont(new Font("宋体", Font.BOLD, 55));

                int location = contentPanel.getDividerLocation();

                jPanel1.add(jPanel0_0, BorderLayout.CENTER);
                contentPanel.setDividerLocation(location);

                break;
            case 1:

                JPanel jPanel1_0 = new JPanel(new BorderLayout());
                location = contentPanel.getDividerLocation();
                jPanel1.add(jPanel1_0, BorderLayout.CENTER);

                contentPanel.setDividerLocation(location);
                break;
            case 2:

                JPanel jPanel2_0 = new JPanel(new BorderLayout());
                JLabel jLabel2_0 = new JLabel("请选择k文件：");

                JTextField textField2_0 = new JTextField("");

                textField2_0.setPreferredSize(new Dimension(100, 18));
                textField2_0.setMaximumSize(new Dimension(300, 20));
                JButton button2_0 = new JButton("浏览");
                JButton button2_1 = new JButton("读取数据");
                button2_1.setEnabled(false);

                Box box2_0 = Box.createHorizontalBox();
                box2_0.add(jLabel2_0);
                box2_0.add(textField2_0);
                box2_0.add(Box.createHorizontalStrut(10));
                box2_0.add(button2_0);
                box2_0.add(Box.createHorizontalStrut(10));
                box2_0.add(button2_1);

                jPanel2_0.add(box2_0);

                if (k_file != null) {
                    textField2_0.setText(k_file.getAbsolutePath());
                    button2_1.setEnabled(true);
                }

                jPanel1.add(jPanel2_0, BorderLayout.CENTER);
                button2_0.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            k_file = openFile(0);
                            textField2_0.setText(k_file.getAbsolutePath());
                            button2_1.setEnabled(true);
                        } catch (NullPointerException ex) {

                        }
                    }
                });

                button2_1.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        readDataFromkFile(k_file);
                    }
                });

                break;
            case 3:
                JPanel jPanel3_0 = new JPanel(new BorderLayout());
                JTextArea textArea3_0 = new JTextArea("没有网格数据，"
                        + "请检查输入k文件或者重新读取数据！");

                if (nodeNum != 0 || elementNum != 0) {
                    textArea3_0.setText("网格节点总数为：" + nodeNum);
                    textArea3_0.append("\r\n单元总数为：" + elementNum);
                    textArea3_0.append("\r\n节点集数量为：" + nodeIdSet.length);
                }
                jPanel3_0.add(textArea3_0);
                jPanel1.add(jPanel3_0, BorderLayout.CENTER);
                break;
            case 4:
                JPanel jPanel4_0 = new JPanel(new BorderLayout());
                JTable table4_0 = new JTable(100, 5);
                JScrollPane scrollPane4_0 = new JScrollPane(table4_0);
                Object[] title4_0 = {"数组索引号", "节点编号", "x坐标",
                    "y坐标", "z坐标"};
                addRow(table4_0, 0, title4_0);
                for (int i = 0; i <= nodeNum - 1; i++) {
                    Object[] data = {i, node[i].getPointId(), node[i].getX(),
                        node[i].getY(), node[i].getZ()};
                    addRow(table4_0, i + 1, data);
                }

                jPanel4_0.add(scrollPane4_0, BorderLayout.CENTER);
                jPanel1.add(jPanel4_0, BorderLayout.CENTER);
                break;
            case 5:
                JPanel jPanel5_0 = new JPanel(new BorderLayout());
                JTable table5_0 = new JTable(100, 10);
                JScrollPane scrollPane5_0 = new JScrollPane(table5_0);
                Object[] title5_0 = {"单元索引号", "单元编号", "节点1", "节点2",
                    "节点3", "节点4", "节点5", "节点6", "节点7", "节点8"};
                addRow(table5_0, 0, title5_0);
                for (int i = 0; i <= elementNum - 1; i++) {
                    int[] node_5 = eleQuad_8[i].getNodeList();
                    Object[] data = {i, eleQuad_8[i].getElementId(),
                        node_5[0], node_5[1], node_5[2], node_5[3], node_5[4],
                        node_5[5], node_5[6], node_5[7]};
                    addRow(table5_0, i + 1, data);
                }
                jPanel5_0.add(scrollPane5_0, BorderLayout.CENTER);
                jPanel1.add(jPanel5_0, BorderLayout.CENTER);
                break;
            case 6:

                if (nodeIdSet != null) {
                    int n6_0 = nodeIdSet.length;
                    JTabbedPane tabbedPane6_0 = new JTabbedPane(JTabbedPane.TOP);
                    JPanel jPanel6_0 = new JPanel(new BorderLayout());
                    for (int i = 0; i <= n6_0 - 1; i++) {
                        String panelName6_0 = "节点集" + (i + 1);

                        JTextArea tempTextArea6_0 = new JTextArea();
                        tempTextArea6_0.setAutoscrolls(true);
                        tempTextArea6_0.setText("");
                        for (int j = 0; j <= nodeIdSet[i].length - 1; j++) {
                            if ((j) % 8 == 0) {
                                tempTextArea6_0.append("\r\n");
                            }
                            tempTextArea6_0.append(String.format("%20d",
                                    nodeIdSet[i][j]));
                        }
                        JScrollPane tempPanel6_0 = new JScrollPane(tempTextArea6_0);
                        tabbedPane6_0.add(panelName6_0, tempPanel6_0);
                    }

                    jPanel6_0.add(tabbedPane6_0, BorderLayout.CENTER);

                    location = contentPanel.getDividerLocation();

                    jPanel1.add(jPanel6_0, BorderLayout.CENTER);
                    contentPanel.setDividerLocation(location);
                } else {

                    jPanel1.add(new JTextArea("没有节点集"), BorderLayout.CENTER);

                }

                break;
            case 7:

                break;
            case 8:
                JPanel jPanel8_0 = new JPanel(new FlowLayout());

                JComboBox jComboBox8_0 = new JComboBox();
                jComboBox8_0.setFont(new java.awt.Font("宋体", 0, 18)); // NOI18N
                jComboBox8_0.setModel(
                        new DefaultComboBoxModel(new String[]{"定常", "非定常",
                    "温度场分析"}));
                jComboBox8_0.setMaximumSize(new Dimension(200, 23));
                JLabel jLabel8_0 = new JLabel();
                jLabel8_0.setFont(new java.awt.Font("宋体", 0, 18)); // NOI18N
                jLabel8_0.setText("分析类型：");
                JLabel jLabel8_1 = new JLabel("计算总时间：");
                JTextField textField8_1 = new JTextField(
                        String.valueOf(analysisType_totalTime));
                JLabel jLabel8_2 = new JLabel("计算时间步数：");
                JTextField textField8_2 = new JTextField(
                        String.valueOf(analysisType_timeSteps));
                JLabel jLabel8_3 = new JLabel("计算初始时刻：");
                JTextField textField8_3 = new JTextField(
                        String.valueOf(analysisType_initialTime));
                Box box8_0 = Box.createHorizontalBox();
                Box box8_1 = Box.createVerticalBox();
                if ((int) object == 0) {
                    jComboBox8_0.setSelectedIndex(0);
                    box8_0.add(Box.createHorizontalGlue());
                    box8_0.add(jLabel8_0);
                    box8_0.add(jComboBox8_0);
                    box8_0.add(Box.createHorizontalGlue());
                    box8_0.add(Box.createHorizontalGlue());
                    box8_1.add(box8_0);
                } else if ((int) object == 1) {
                    jComboBox8_0.setSelectedIndex(1);
                    box8_0.add(Box.createHorizontalGlue());
                    box8_0.add(jLabel8_0);
                    box8_0.add(jComboBox8_0);
                    box8_0.add(Box.createHorizontalGlue());
                    box8_0.add(Box.createHorizontalGlue());
                    box8_1.add(box8_0);
                    box8_1.add(jLabel8_1);
                    box8_1.add(textField8_1);
                    box8_1.add(jLabel8_2);
                    box8_1.add(textField8_2);
                    box8_1.add(jLabel8_3);
                    box8_1.add(textField8_3);
                } else if ((int) object == 2) {
                    jComboBox8_0.setSelectedIndex(2);
                    box8_0.add(Box.createHorizontalGlue());
                    box8_0.add(jLabel8_0);
                    box8_0.add(jComboBox8_0);
                    box8_0.add(Box.createHorizontalGlue());
                    box8_0.add(Box.createHorizontalGlue());
                    box8_1.add(box8_0);
                    box8_1.add(jLabel8_1);
                    box8_1.add(textField8_1);
                    box8_1.add(jLabel8_2);
                    box8_1.add(textField8_2);
                    box8_1.add(jLabel8_3);
                    box8_1.add(textField8_3);
                }

                JButton button8_0 = new JButton("确定");
                JButton button8_1 = new JButton("应用");
                JButton button8_2 = new JButton("取消");
                Box buttonBox8_0 = Box.createHorizontalBox();
                buttonBox8_0.add(Box.createHorizontalGlue());
                buttonBox8_0.add(button8_0);
                buttonBox8_0.add(Box.createHorizontalGlue());
                buttonBox8_0.add(button8_1);
                buttonBox8_0.add(Box.createHorizontalGlue());
                buttonBox8_0.add(button8_2);
                buttonBox8_0.add(Box.createHorizontalGlue());

                jPanel8_0.add(box8_1);
                jPanel1.add(jPanel8_0, BorderLayout.CENTER);
                jPanel1.add(buttonBox8_0, BorderLayout.SOUTH);

                jComboBox8_0.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (jComboBox8_0.getSelectedIndex() == 0) {
                            analysisType = 0;
                            changePanel(8, 0);
                        } else if (jComboBox8_0.getSelectedIndex() == 1) {
                            analysisType = 1;
                            changePanel(8, 1);
                        } else if (jComboBox8_0.getSelectedIndex() == 2) {
                            analysisType = 2;
                            changePanel(8, 2);
                        }
                    }
                });

                button8_0.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        analysisType_totalTime = Double.parseDouble(
                                textField8_1.getText());
                        analysisType_timeSteps = Integer.parseInt(
                                textField8_2.getText());
                        analysisType_initialTime = Double.parseDouble(
                                textField8_3.getText());
                        changePanel(9, null);

                    }
                });
                button8_1.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        analysisType_totalTime = Double.parseDouble(
                                textField8_1.getText());
                        analysisType_timeSteps = Integer.parseInt(
                                textField8_2.getText());
                        analysisType_initialTime = Double.parseDouble(
                                textField8_3.getText());
                    }
                });
                button8_2.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                break;
            case 9:
                JPanel jPanel9_0 = new JPanel(new FlowLayout());

//                JScrollPane scrollPane9_0 = new JScrollPane();
                JPanel jPanel9_0_0 = new JPanel();//位置和类型面板
                JPanel jPanel9_0_1 = new JPanel();//材料选择面板
                JPanel jPanel9_0_2 = new JPanel();//区域模型面板
                jPanel9_0_0.setPreferredSize(new Dimension(1000, 80));
                jPanel9_0_1.setPreferredSize(new Dimension(1000, 80));
                jPanel9_0_2.setPreferredSize(new Dimension(1000, 80));

                JLabel jLabel9_0 = new JLabel("流体选择：");
                jLabel9_0.setMinimumSize(new Dimension(200, 20));
                jLabel9_0.setFont(new java.awt.Font("宋体", 0, 18));
                JComboBox jComboBox9_0 = new JComboBox();

                //读取材料库中的材料,此处相当于只是读取
                if (materialFrame == null) {
                    materialArr = null;
                } else {
                    materialArr = materialFrame.getMaterials();
                }
                if (materialArr == null || materialArr.length == 0) {
                    Material material = new Material(-1, "材料库中没有材料", 0);
                    materialArr = new Material[1];
                    materialArr[0] = material;
                }
                String[] materialName = new String[materialArr.length];
                for (int i = 0; i < materialArr.length; i++) {
                    materialName[i] = materialArr[i].getName();
                }
                jComboBox9_0.setModel(new DefaultComboBoxModel(materialName));
                jComboBox9_0.setFont(new java.awt.Font("宋体", 0, 18));
                jComboBox9_0.setMinimumSize(new Dimension(200, 20));
                Box box9_0 = Box.createVerticalBox();
                box9_0.add(jLabel9_0);
                box9_0.add(jComboBox9_0);
                jPanel9_0_1.add(box9_0);
                jComboBox9_0.setSelectedItem(material);

                JLabel jLabel9_1 = new JLabel("参考压力：");
                jLabel9_1.setMinimumSize(new Dimension(200, 20));
                jLabel9_1.setFont(new java.awt.Font("宋体", 0, 18));
                JTextField textField9_0 = new JTextField(
                        String.valueOf(referPressure));
                textField9_0.setFont(new java.awt.Font("宋体", 0, 18));
                textField9_0.setMinimumSize(new Dimension(200, 20));
                Box box9_1 = Box.createVerticalBox();
                box9_1.add(jLabel9_1);
                box9_1.add(textField9_0);
                jPanel9_0_2.add(box9_1);

                jPanel9_0.add(jPanel9_0_0);
                jPanel9_0.add(jPanel9_0_1);
                jPanel9_0.add(jPanel9_0_2);

                JPanel jPanel9_1 = new JPanel(new FlowLayout());

                JTabbedPane tabbedPane9_0 = new JTabbedPane(JTabbedPane.LEFT);
                tabbedPane9_0.add("基本设置", jPanel9_0);
                tabbedPane9_0.add("流体模型", jPanel9_1);
                //tabbedPane9_0.add("初始设置", jPanel9_2);

                JButton button9_0 = new JButton("确定");
                JButton button9_1 = new JButton("应用");
                JButton button9_2 = new JButton("重置");
                Box buttonBox9_0 = Box.createHorizontalBox();
                buttonBox9_0.add(Box.createHorizontalGlue());
                buttonBox9_0.add(button9_0);
                buttonBox9_0.add(Box.createHorizontalGlue());
                buttonBox9_0.add(button9_1);
                buttonBox9_0.add(Box.createHorizontalGlue());
                buttonBox9_0.add(button9_2);
                buttonBox9_0.add(Box.createHorizontalGlue());

                location = contentPanel.getDividerLocation();
                jPanel1.add(tabbedPane9_0, BorderLayout.CENTER);
                jPanel1.add(buttonBox9_0, BorderLayout.SOUTH);
                contentPanel.setDividerLocation(location);

                button9_0.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        material = jComboBox9_0.getSelectedItem().toString();
                        System.out.println("1078MainFrame:material=" + material);
                        int mid = 0;
                        for (int i = 0; i < materialArr.length; i++) {
                            if (material.equals(materialArr[i].getName())) {
                                mid = i + 1;//材料编号=材料数组索引号+1
                            }
                        }
                        parts[currentPart].setMID(mid);
                        referPressure = Double.parseDouble(textField9_0.getText());
                    }
                });
                button9_1.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        material = jComboBox9_0.getSelectedItem().toString();
                        int mid = 0;
                        for (int i = 0; i < materialArr.length; i++) {
                            if (material.equals(materialArr[i].getName())) {
                                mid = i + 1;//材料编号=材料数组索引号+1
                            }
                        }
                        parts[currentPart].setMID(mid);
                        System.out.println("1087MainFrame:material=" + material);
                        referPressure = Double.parseDouble(textField9_0.getText());
                    }
                });

                break;
            case 10:
                if (materialFrame == null) {
                    materialFrame = new MaterialFrame();
                }
                location = contentPanel.getDividerLocation();
                jPanel1.add(materialFrame, BorderLayout.CENTER);
                contentPanel.setDividerLocation(location);
                break;
            case 11:
                JPanel jPanel11_0 = new JPanel(new FlowLayout());
                JButton jButton11_0 = new JButton("添加进口边界条件");
                JButton jButton11_1 = new JButton("添加出口边界条件");
                JButton jButton11_2 = new JButton("添加指定节点约束条件");
                jPanel11_0.add(jButton11_0);
                jPanel11_0.add(jButton11_1);
                jPanel11_0.add(jButton11_2);
                jPanel1.add(jPanel11_0, BorderLayout.CENTER);
                jButton11_0.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String nodeName = addNodeToTree(getTree(), node2_1, 0);
                        changePanel(14, nodeName);
                    }
                });
                jButton11_1.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String nodeName = addNodeToTree(getTree(), node2_1, 0);
                        changePanel(14, nodeName);
                    }
                });
                jButton11_2.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String nodeName = addNodeToTree(getTree(), node2_1, 0);
                        changePanel(14, nodeName);
                    }
                });
                break;
            case 12:

                break;
            case 13:
                JTabbedPane tabbedPane13_0 = new JTabbedPane(JTabbedPane.TOP);
                String fileName13_0 = (String) object;
                JTextArea textArea13_0 = new JTextArea("");
                JPanel jPanel13_1 = new JPanel(new BorderLayout());
                jPanel13_1.add(textArea13_0, BorderLayout.CENTER);
                tabbedPane13_0.add(fileName13_0, jPanel13_1);

                Box box13_0 = Box.createHorizontalBox();
                JButton button13_0 = new JButton("取消");
                JButton button13_1 = new JButton("保存草稿");
                box13_0.add(Box.createHorizontalGlue());
                box13_0.add(button13_1);
                box13_0.add(Box.createHorizontalGlue());
                box13_0.add(button13_0);
                box13_0.add(Box.createHorizontalGlue());

                jPanel1.add(tabbedPane13_0, BorderLayout.CENTER);
                jPanel1.add(box13_0, BorderLayout.SOUTH);
                break;
            case 14:
                JTabbedPane tabbedPane14_0 = new JTabbedPane(JTabbedPane.TOP);
                String boundaryName14_0 = (String) object;
                int selectedNodeSet = 0;
                String selectedValueName = "压强";
                double value = 0;
                double u = 0;
                double v = 0;
                double w = 0;

                if (!boundary.isEmpty()) {
                    for (int i = 0; i <= boundary.size() - 1; i++) {
                        Boundary tempO = boundary.get(i);
                        if (tempO.getBoundaryName().equals(boundaryName14_0)) {
                            //如果该边界已经存在
                            selectedNodeSet = tempO.getNodeSetId();
                            selectedValueName = tempO.getVarName();
                            value = tempO.getValue();
                            u = tempO.getU();
                            v = tempO.getV();
                            w = tempO.getW();
                        }
                    }
                }

                JPanel jPanel14_0 = new JPanel(new BorderLayout());
                JLabel jLabel14_0 = new JLabel("边界名称：");
                JTextField textField14_0 = new JTextField(boundaryName14_0);
                textField14_0.setEditable(false);
                textField14_0.setMaximumSize(new Dimension(2000, 20));
                JLabel jLabel14_1 = new JLabel("边界条件施加的节点集：");
                JComboBox comboBox14_1 = new JComboBox();
                String[] nodeSetList = null;
                if (nodeIdSet == null) {
                    nodeSetList = new String[]{"节点集不存在", "指定节点"};

                } else {
                    nodeSetList = new String[nodeIdSet.length + 1];
                    for (int i = 0; i <= nodeSetList.length - 1; i++) {
                        nodeSetList[i] = "节点集" + (i + 1);
                    }
                    nodeSetList[nodeIdSet.length] = "指定节点";
                }

                comboBox14_1.setModel(new DefaultComboBoxModel(nodeSetList));
                comboBox14_1.setSelectedIndex(selectedNodeSet);
                comboBox14_1.setMaximumSize(new Dimension(2000, 20));
                JLabel jLabel14_2 = new JLabel("变量：");

                JComboBox comboBox14_2 = new JComboBox();
                String[] varList = new String[]{"压强", "速度", "流量", "温度",
                    "热流密度"};
                comboBox14_2.setMaximumSize(new Dimension(2000, 20));
                comboBox14_2.setModel(new DefaultComboBoxModel(varList));
                comboBox14_2.setSelectedItem(selectedValueName);
                JLabel jLabel14_3 = new JLabel("变量值");
                JTextField textField14_3 = new JTextField();
                textField14_3.setText(String.valueOf(value));
                textField14_3.setMaximumSize(new Dimension(2000, 20));
                JLabel jLabel14_3_0 = new JLabel("u:");
                JLabel jLabel14_3_1 = new JLabel("v:");
                JLabel jLabel14_3_2 = new JLabel("w:");
                JTextField textField14_3_0 = new JTextField(String.valueOf(u));
                JTextField textField14_3_1 = new JTextField(String.valueOf(v));
                JTextField textField14_3_2 = new JTextField(String.valueOf(w));
                Box box14_3_0 = Box.createHorizontalBox();
                Box box14_3_1 = Box.createHorizontalBox();
                Box box14_3_2 = Box.createHorizontalBox();
                box14_3_0.add(Box.createHorizontalGlue());
                box14_3_0.add(jLabel14_3_0);
                box14_3_0.add(textField14_3_0);
                box14_3_0.add(Box.createHorizontalGlue());
                box14_3_1.add(Box.createHorizontalGlue());
                box14_3_1.add(jLabel14_3_1);
                box14_3_1.add(textField14_3_1);
                box14_3_1.add(Box.createHorizontalGlue());
                box14_3_2.add(Box.createHorizontalGlue());
                box14_3_2.add(jLabel14_3_2);
                box14_3_2.add(textField14_3_2);
                box14_3_2.add(Box.createHorizontalGlue());
                Box box14_3_3 = Box.createVerticalBox();
                box14_3_3.add(Box.createVerticalGlue());
                box14_3_3.add(box14_3_0);
                box14_3_3.add(Box.createVerticalStrut(10));
                box14_3_3.add(box14_3_1);
                box14_3_3.add(Box.createVerticalStrut(10));
                box14_3_3.add(box14_3_2);
                box14_3_3.add(Box.createVerticalGlue());

                JPanel jPanel14_1 = new JPanel(new BorderLayout());
                JPanel jPanel14_2 = new JPanel(new BorderLayout());
                JPanel jPanel14_3 = new JPanel(new BorderLayout());
                JPanel jPanel14_4 = new JPanel(new BorderLayout());
                Box box14_1_1 = Box.createVerticalBox();
                box14_1_1.add(Box.createVerticalGlue());
                box14_1_1.add(jLabel14_0);
                box14_1_1.add(textField14_0);
                box14_1_1.add(Box.createVerticalGlue());
                jPanel14_1.add(box14_1_1, BorderLayout.CENTER);
                Box box14_1_2 = Box.createVerticalBox();
                box14_1_2.add(Box.createVerticalGlue());
                box14_1_2.add(jLabel14_1);
                box14_1_2.add(comboBox14_1);
                JTextField textField14_1_0 = new JTextField();
                textField14_1_0.setVisible(false);
                box14_1_2.add(textField14_1_0);
                box14_1_2.add(Box.createVerticalGlue());
                jPanel14_2.add(box14_1_2, BorderLayout.CENTER);
                Box box14_1_3 = Box.createVerticalBox();
                box14_1_3.add(Box.createVerticalGlue());
                box14_1_3.add(jLabel14_2);
                box14_1_3.add(comboBox14_2);
                box14_1_3.add(Box.createVerticalGlue());
                jPanel14_3.add(box14_1_3, BorderLayout.CENTER);
                Box box14_1_4 = Box.createVerticalBox();
                box14_1_4.add(Box.createVerticalGlue());
                box14_1_4.add(jLabel14_3);
                if (selectedValueName.equals("速度")) {
                    box14_1_4.add(box14_3_3);
                } else {
                    box14_1_4.add(textField14_3);
                }
                box14_1_4.add(Box.createVerticalGlue());
                jPanel14_4.add(box14_1_4, BorderLayout.CENTER);

                Box box14_1 = Box.createVerticalBox();
                box14_1.add(Box.createVerticalGlue());
                box14_1.add(jPanel14_1);
                box14_1.add(Box.createVerticalStrut(10));
                box14_1.add(jPanel14_2);
                box14_1.add(Box.createVerticalStrut(10));
                box14_1.add(jPanel14_3);
                box14_1.add(Box.createVerticalStrut(10));
                box14_1.add(jPanel14_4);
                box14_1.add(Box.createVerticalGlue());

                jPanel14_0.add(box14_1, BorderLayout.CENTER);

                tabbedPane14_0.add(boundaryName14_0, jPanel14_0);

                Box box14_0 = Box.createHorizontalBox();
                JButton button14_0 = new JButton("保存");
                JButton button14_1 = new JButton("取消");
                box14_0.add(Box.createHorizontalGlue());
                box14_0.add(button14_0);
                box14_0.add(Box.createHorizontalGlue());
                box14_0.add(button14_1);
                box14_0.add(Box.createHorizontalGlue());
                jPanel1.add(tabbedPane14_0, BorderLayout.CENTER);
                jPanel1.add(box14_0, BorderLayout.SOUTH);

                button14_0.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int nodeset = comboBox14_1.getSelectedIndex();
                        String valueName = comboBox14_2.getSelectedItem()
                                .toString();
                        Boundary info;
                        if (comboBox14_2.getSelectedItem().equals("速度")) {
                            info = new Boundary(boundaryName14_0, nodeset,
                                    valueName, Double
                                            .parseDouble(textField14_3_0.getText()),
                                    Double.parseDouble(textField14_3_1
                                            .getText()),
                                    Double.parseDouble(textField14_3_2
                                            .getText()));
                        } else {
                            info = new Boundary(boundaryName14_0, nodeset,
                                    valueName, Double
                                            .parseDouble(textField14_3
                                                    .getText()));
                        }
                        if (boundary.isEmpty()) {
                            //如果集合boundary为空，直接添加
                            boundary.add(info);
                        } else {
                            //如果boundary不为空，需要判断当前边界条件是否已经添加
                            boolean exist = false;
                            for (int i = 0; i <= boundary.size() - 1; i++) {
                                Boundary temp = boundary.get(i);
                                if ((temp.getBoundaryName())
                                        .equals(boundaryName14_0)) {
                                    //说明当前边界条件已存在，更新之。
                                    temp.setNodeSetId(comboBox14_1
                                            .getSelectedIndex());
                                    temp.setVarName(comboBox14_2
                                            .getSelectedItem().toString());
                                    temp.setValue(Double
                                            .parseDouble(textField14_3
                                                    .getText()));
                                    temp.setU(Double.
                                            parseDouble(textField14_3_0
                                                    .getText()));
                                    temp.setV(Double.
                                            parseDouble(textField14_3_1
                                                    .getText()));
                                    temp.setW(Double.
                                            parseDouble(textField14_3_2
                                                    .getText()));
                                    boundary.remove(i);
                                    boundary.add(temp);
                                    exist = true;
                                }
                            }
                            if (!exist) {
                                //如果边界条件不存在于boundary集合中，添加之。

                                boundary.add(info);
                            }

                        }
                    }
                });

                comboBox14_1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (comboBox14_1.getSelectedItem().equals("指定节点")) {
                            textField14_1_0.setVisible(true);
                        } else {
                            textField14_1_0.setVisible(false);
                        }
                    }
                });
                comboBox14_2.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (comboBox14_2.getSelectedItem().equals("速度")) {
                            box14_1_4.removeAll();
                            box14_1_4.add(jLabel14_3);
                            box14_1_4.add(box14_3_3);
                            box14_1_4.repaint();
                        } else {
                            box14_1_4.removeAll();
                            box14_1_4.add(jLabel14_3);
                            box14_1_4.add(textField14_3);
                            box14_1_4.repaint();
                        }
                    }
                });
                break;
            case 15://求解控制
                JPanel jPanel15_0 = new JPanel(new BorderLayout());

                Box box15_0 = Box.createVerticalBox();
                JLabel jLabel15_0 = new JLabel("对流项求解格式：");
                JComboBox comboBox15_0 = new JComboBox();
                comboBox15_0.setMaximumSize(new Dimension(2000, 20));
                JLabel jLabel15_1 = new JLabel("非定常项求解格式：");
                JComboBox comboBox15_1 = new JComboBox();
                comboBox15_1.setMaximumSize(new Dimension(2000, 20));
                JLabel jLabel15_2 = new JLabel("迭代控制：");
                JLabel jLabel15_3 = new JLabel("最大迭代步数：");
                JTextField textField15_3 = new JTextField(String.valueOf(maxIterNum));
                textField15_3.setMaximumSize(new Dimension(2000, 20));
                JLabel jLabel15_4 = new JLabel("计算停止需要达到的残差小于：");
                JTextField textField15_4 = new JTextField(String.valueOf(maxerr));
                textField15_4.setMaximumSize(new Dimension(2000, 20));

                box15_0.add(Box.createVerticalGlue());
                box15_0.add(jLabel15_0);
                box15_0.add(comboBox15_0);
                box15_0.add(jLabel15_1);
                box15_0.add(comboBox15_1);
                box15_0.add(Box.createVerticalStrut(10));
                box15_0.add(jLabel15_2);
                box15_0.add(jLabel15_3);
                box15_0.add(textField15_3);
                box15_0.add(jLabel15_4);
                box15_0.add(textField15_4);
                box15_0.add(Box.createVerticalGlue());

                jPanel15_0.add(box15_0, BorderLayout.CENTER);

                JButton button15_0 = new JButton("保存");
                JButton button15_1 = new JButton("应用");
                JButton button15_2 = new JButton("取消");

                Box box15_1 = Box.createHorizontalBox();
                box15_1.add(Box.createHorizontalGlue());
                box15_1.add(button15_0);
                box15_1.add(Box.createHorizontalGlue());
                box15_1.add(button15_1);
                box15_1.add(Box.createHorizontalGlue());
                box15_1.add(button15_2);
                box15_1.add(Box.createHorizontalGlue());
                jPanel1.add(jPanel15_0, BorderLayout.CENTER);
                jPanel1.add(box15_1, BorderLayout.SOUTH);
                button15_0.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        maxIterNum = Integer.parseInt(textField15_3.getText());
                        maxerr = Double.parseDouble(textField15_4.getText());
                    }
                });

                button15_1.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        maxIterNum = Integer.parseInt(textField15_3.getText());
                        maxerr = Double.parseDouble(textField15_4.getText());
                    }
                });
                break;
            case 16:
                break;
            case 17:
                break;
            case 18:
                if (displayUI == null) {
                    displayUI = new DisplayUI();
                }
                jPanel1.add(displayUI, BorderLayout.CENTER);
                break;
            case 19:
                break;
            case 20://结构优化
                JPanel jPanel20_0 = new JPanel(new BorderLayout());
                JPanel jPanel20_1 = new JPanel(new BorderLayout());
                JPanel jPanel20_2 = new JPanel(new BorderLayout());
//                JPanel jPanel20_3 = new JPanel();
//
//                JLabel jLabel20_0 = new JLabel("几何模型设置");
//                JLabel JLabel20_1 = new JLabel("选择模型保存文件夹");
//                JButton jButton20_0 = new JButton("打开");
//                JButton jButton20_1 = new JButton("编辑SolidWorks模型创建宏文件");
//                JButton jButton20_2 = new JButton("编辑几何模型尺寸参数");
//
//                jPanel20_3.setLayout(new GridBagLayout());//第一个tabbedPane中的容器面板
//                JPanel jPanel20_4 = new JPanel();
//                jPanel20_4.setBackground(Color.red);
//                JPanel jPanel20_5 = new JPanel();
//                jPanel20_5.setBackground(Color.yellow);
//
//                JPanel jPanel20_6 = new JPanel();
//                jPanel20_6.setBackground(Color.blue);
//                jPanel20_3.add(jPanel20_4,
//                        new GBC(0, 0, 82, 1).setFill(GBC.BOTH).setIpad(70, 90));
//                jPanel20_3.add(jPanel20_5,
//                        new GBC(82, 0, 6, 1).setFill(GBC.BOTH));
//                jPanel20_3.add(jPanel20_6,
//                        new GBC(88, 0, 6, 1).setFill(GBC.BOTH));
//                jPanel20_3.add(jPanel20_5,
//                        new GBC(94, 0, 6, 100).setFill(GBC.BOTH));
//                jPanel20_0.add(jPanel20_3);
                new Optimization(this.getX(), this.getY(), this.getWidth(), this.getHeight());
                this.dispose();
                break;
            default:
                break;

        }

        return jPanel1;
    }

    private void readDataFromkFile(File file) {
        ReadKeyFile keyFile = new ReadKeyFile(file);
        eleQuad_8 = keyFile.getElementSolidListFromIndex0_ansys();//带不带ansys影响单元内节点编号顺序，但是貌似不影响计算结果

        node = keyFile.getPoint3DListFromIndex0();
        nodeIdSet = keyFile.getNodeSet();
        parts = keyFile.getParts();
        nodeNum = node.length;
        elementNum = eleQuad_8.length;
        JOptionPane.showMessageDialog(null, "数据读取完成！");
    }

    private void calculate(int tab, Object object) {
        CalculateInfoBag info = new CalculateInfoBag();
        info.setAnalysisType(analysisType);
        System.out.println("分析类型：" + analysisType + "(定常)");
        info.setAnalysisType_initialTime(analysisType_initialTime);
        info.setAnalysisType_timeSteps(analysisType_timeSteps);
        info.setAnalysisType_totalTime(analysisType_totalTime);
        info.setMaxIterNum(maxIterNum);
        info.setMaxErr(maxerr);
        System.out.println("单元信息打包...");
        info.setEleQuad_8(eleQuad_8);
        System.out.println("单元信息打包完成");
        info.setMaterial(material);//选择的材料
        materialArr = materialFrame.getMaterials();
        info.setMaterialArr(materialArr);
        System.out.println("节点信息打包...");
        if (node == null) {
            JOptionPane.showMessageDialog(null, "没有网格信息！");
            return;
        }
        info.setNode(node);
        System.out.println("节点信息打包完成");
        info.setParts(parts);
        System.out.println("节点集信息打包...");
        info.setNodeIdSet(nodeIdSet);
        System.out.println("节点集信息打包完成");
        info.setReferPressure(referPressure);
        info.setGaussPointNum(getGaussPointNum());
        info.setCommand(command);
        System.out.println("生成速度高阶单元...");
        info.initCalculateInfoBag();
        System.out.println("速度高阶单元生成完毕");
        System.out.println("边界条件打包...");
        info.setBoundary(boundary);
        System.out.println("边界条件打包完成");
        //info.writeDataToFile(new File("D:\\workspace\\MATLAB\\chapter3 - 副本"));
        switch (tab) {
            case 0:
                ThreadDispatch td1 = new ThreadDispatch("thread_1", "FluidFlow", info);
                td1.start();
//                new Example3_1_1(info);
                break;
            case 1:
                JOptionPane.showMessageDialog(null, "该模块被注释掉了！");
//                new Solid2D(info);
                break;
            case 2:
                info.setTempInitial(273.0);
                new Temp3D(info);
            default:
                break;
        }

    }

    private void openFVM(int tab, Object object) {
        CalculateInfoBag info = new CalculateInfoBag("OpenFVM");
        new OpenFVM(info);
    }

    private void saveSetInfoToFile(File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            Boundary[] boundaryArr = new Boundary[boundary.size()];
            boundaryArr = boundary.toArray(boundaryArr);

            SetInfo setInfo = new SetInfo(k_file, (analysisType),
                    analysisType_initialTime, analysisType_timeSteps,
                    analysisType_totalTime, referPressure, boundaryArr);
            setInfo.setMaterial(material);
            setInfo.setMaxErr(maxerr);
            setInfo.setMaxIterNum(maxIterNum);
            setInfo.setParts(parts);
            String setInfoXML = XStreamUtil.SetInfoToXML(setInfo);
            os.write(setInfoXML.getBytes("GBK"));
            os.flush();
            os.close();
        } catch (Exception ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readSetInfo(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            String inxml = "";
            Scanner inSca = new Scanner(is, "GBK");
            while (inSca.hasNext()) {
                inxml = inxml + inSca.nextLine();
            }

            SetInfo setInfo = XStreamUtil.SetInfoFromXML(inxml);
            int width = this.getWidth();
            int height = this.getHeight();
            new MainFrame(setInfo, this.getX(), this.getY(), width, height);
            this.dispose();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).
                    log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveToExcel() {

    }

    private void superSet(String tab, Object object) {
        new SuperSet(tab, this);
    }

    public void setSuper(String value, Object object) {
        switch (value) {
            case "gaussPointNum":
                this.gaussPointNum = (Integer) object;
                break;
            case "splitChar":
                this.splitChar = (String) object;
                break;
            case "command":
                this.command = (String) object;
            default:
                break;
        }
    }

    private void addRow(JTable table, int rowNum, Object[] data) {
        System.out.println("begin table rows=" + table.getRowCount());
        if (table.getRowCount() > rowNum) {
            for (int i = 0; i <= data.length - 1; i++) {
                table.setValueAt(data[i], rowNum, i);
            }
        } else {
            DefaultTableModel dtm = (DefaultTableModel) table.getModel();
            Object[] nullRow = new Object[1];
            nullRow[0] = "";
            dtm.addRow(nullRow);
            addRow(table, rowNum, data);
        }
    }

    private void stop() {

    }

    /**
     * @return the gaussPointNum
     */
    public int getGaussPointNum() {
        return gaussPointNum;
    }

    /**
     * @return the commmand
     */
    public String getCommand() {
        return command;
    }

}
