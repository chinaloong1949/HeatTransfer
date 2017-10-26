/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heattransfer;

import GUI.GBC;
import common.FileChooser;
import common.FileOperate;
import common.MailTo;
import common.OptimizationData;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class Optimization extends JFrame {

    JPanel contentPanel = new JPanel();
    File workingDirectory = new File("D:\\Users\\2017\\dissertation\\chapter3\\AoCao");
    File modelFolder;
    File createModelFile;
    File meshFolder;
    File createMeshFile;
    File solveFolder;
    File createSolveFile;
    File resultFolder;
    File dataProcessFile;
    File resultFile;

    OptimizationData data = new OptimizationData();
    File dataFile;

    public Optimization(int x, int y, int width, int height) {

        this.setLayout(new BorderLayout());
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("文件");

        JMenuItem chooseWorkingDirectoryMenuItem = new JMenuItem("设置工作目录");
        JMenuItem openDataMenuItem = new JMenuItem("打开");
        JMenuItem saveDataMenuItem = new JMenuItem("保存");

        fileMenu.add(openDataMenuItem);
        fileMenu.add(saveDataMenuItem);
        fileMenu.add(chooseWorkingDirectoryMenuItem);

        JMenu runMenu = new JMenu("运行");
        JMenuItem runItem = new JMenuItem("开始计算");

        JMenu helpMenu = new JMenu("帮助");
        JMenuItem aboutMeshItem = new JMenuItem("关于网格设置");
        JMenuItem aboutSolveItem = new JMenuItem("关于计算设置");
        JMenuItem contactItem = new JMenuItem("联系作者");

        helpMenu.add(aboutMeshItem);
        helpMenu.add(aboutSolveItem);
        helpMenu.add(contactItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

//        this.getContentPane().add(getGridBagPanes(2, null));
        contentPanel.setLayout(new BorderLayout());//一定要加布局管理器，不然出错，JPanel添加布局管理器之后类似于JFrame的getContentPane()
        contentPanel.add(getGridBagPanes(1, null));
        this.getContentPane().add(menuBar, BorderLayout.NORTH);
        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        this.setTitle("流动换热优化-v0.1-2017");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(x, y);
        this.setSize(width, height);

        //this.pack();
        this.setVisible(true);

        chooseWorkingDirectoryMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooser fc = new FileChooser("设置工作目录", FileChooser.SELECT_DIRECTORY, "C:");
                workingDirectory = fc.getFile();
                changePanel(1, null);
            }
        });

        saveDataMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooser fc = new FileChooser("保存当前设置到", FileChooser.SAVE_TO_FILE, workingDirectory.getAbsolutePath());
                fc.setFileType(new String[]{"xml", "txt"});
                dataFile = fc.getFile();
                if (dataFile != null) {
                    data.saveDataToFile(dataFile);
                }
            }
        });
        openDataMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooser fc = new FileChooser("打开设置文件", FileChooser.OPEN_FILE, workingDirectory.getAbsolutePath());
                fc.setFileType(new String[]{"xml", "txt"});
                dataFile = fc.getFile();
                if (dataFile != null) {
                    data = OptimizationData.readDataFromFile(dataFile);
                }
            }
        });

        runItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optimization(data);
            }
        });

        aboutMeshItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutMesh();
            }
        });
        aboutSolveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutSolve();
            }
        });

        contactItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new MailTo("540673597@qq.com");
            }
        });
    }

    public JPanel getGridBagPanes(int tab, Object object) {
        //上侧的工具选择面板  
        if (object != null) {
            this.data = (OptimizationData) object;
        }
        JPanel jPanel;
        jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        switch (tab) {
            case 1:
                System.out.println("1");
                JPanel firstPanel = new JPanel();
                firstPanel.setLayout(new BorderLayout());
                firstPanel.setBackground(Color.green);
                JPanel secondPanel = new JPanel(new BorderLayout());
                secondPanel.setBackground(Color.YELLOW);
                JPanel thirdPanel = new JPanel(new BorderLayout());
                thirdPanel.setBackground(Color.LIGHT_GRAY);
                JPanel forthPanel = new JPanel(new BorderLayout());
                forthPanel.setBackground(Color.CYAN);

                JButton jButtonFir_1_0 = new JButton("确定");
                JButton jButtonFir_1_1 = new JButton("继续");
                JButton jButtonFir_2_0 = new JButton("编辑");
                JButton jButtonFir_3_0 = new JButton("编辑");
                JButton jButtonFir_4_0 = new JButton("编辑");
                secondPanel.add(jButtonFir_2_0, BorderLayout.SOUTH);
                thirdPanel.add(jButtonFir_3_0, BorderLayout.SOUTH);
                forthPanel.add(jButtonFir_4_0, BorderLayout.SOUTH);
                JPanel buttonPanelFir = new JPanel(new GridLayout(1, 0));
                buttonPanelFir.add(jButtonFir_1_0);
                buttonPanelFir.add(jButtonFir_1_1);
                firstPanel.add(buttonPanelFir, BorderLayout.SOUTH);

                JPanel editPanel1 = new JPanel(new GridBagLayout());
                JLabel jLabelFir_1_0 = new JLabel("模型设置");
                jLabelFir_1_0.setFont(new Font("楷体", Font.BOLD, 20));
                JLabel jLabelFir_1_1 = new JLabel("选择模型存储文件夹：");
                JTextField jTextFieldFir_1_1;
                jTextFieldFir_1_1 = new JTextField(new File(workingDirectory, "model").getAbsolutePath());
                if (data.getModelFolder() != null) {
                    jTextFieldFir_1_1.setText(data.getModelFolder().getAbsolutePath());
                }

                JButton jButtonFir_1_10 = new JButton("打开");
                JLabel jLabelFir_1_2 = new JLabel("模型控制参数：");
                JTable jTableFir_1_0 = new JTable(1, 3);
                JLabel jLabelFir_1_3 = new JLabel("建模文件编辑：");
                JLabel jLabelFir_1_4 = new JLabel("");
                JTextArea jTextAreaFir_1 = new JTextArea();
                //jTextAreaFir_1.setFont(new Font("宋体",Font.BOLD,14));
                JScrollPane jScrollPaneFir1 = new JScrollPane(jTextAreaFir_1);

                JButton jButtonFir_1_11 = new JButton("打开");
                JButton jButtonFir_1_12 = new JButton("新建");

                Object[] title = {"模型参数", "单位", "备注"};
                addRow(0, jTableFir_1_0, 0, title);
                if (data.getModelVar().length != 0) {
                    String[] modelVar = data.getModelVar();
                    String[] modelUnit = data.getModelUnit();
                    String[] modelDescription = data.getModelDiscription();
                    for (int i = 0; i < data.getModelVar().length; i++) {
                        Object[] modelTableData = {modelVar[i], modelUnit[i], modelDescription[i]};
                        addRow(0, jTableFir_1_0, i + 1, modelTableData);
                    }
                }

                editPanel1.add(jLabelFir_1_0, new GBC(0, 0).setIpad(80, 10).setWeight(100, 0));//模型设置
                editPanel1.add(jLabelFir_1_1, new GBC(0, 1).setIpad(80, 10).setWeight(100, 0));//选择模型存储文件夹
                editPanel1.add(jTextFieldFir_1_1, new GBC(0, 2, 3, 1).setIpad(60, 10).setWeight(100, 0));
                editPanel1.add(jButtonFir_1_10, new GBC(3, 2).setIpad(20, 10).setWeight(0, 0));
                editPanel1.add(jLabelFir_1_2, new GBC(0, 3).setIpad(80, 10).setWeight(100, 0));//模型控制参数
                editPanel1.add(jTableFir_1_0, new GBC(0, 4, 4, 1).setIpad(80, 30).setWeight(100, 0));
                editPanel1.add(jLabelFir_1_3, new GBC(0, 5).setIpad(30, 10).setWeight(0, 0));//建模文件编辑
                editPanel1.add(jLabelFir_1_4, new GBC(1, 5).setIpad(10, 10).setWeight(100, 0));
                editPanel1.add(jButtonFir_1_11, new GBC(2, 5).setIpad(10, 10).setWeight(0, 0));
                editPanel1.add(jButtonFir_1_12, new GBC(3, 5).setIpad(10, 10).setWeight(0, 0));
                editPanel1.add(jScrollPaneFir1, new GBC(0, 6, 4, 1).setIpad(80, 10).setWeight(100, 100));

                firstPanel.add(editPanel1, BorderLayout.CENTER);

                jPanel.add(firstPanel, new GBC(0, 0).
                        setFill(GBC.BOTH).setIpad(500, 400).setWeight(100, 100));

                jPanel.add(secondPanel, new GBC(1, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));

                jPanel.add(thirdPanel, new GBC(2, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));

                jPanel.add(forthPanel, new GBC(3, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));
                jButtonFir_1_0.addActionListener(new ActionListener() {//确定按钮
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        data.setModelFolder(new File(jTextFieldFir_1_1.getText()));
                        int varCount = jTableFir_1_0.getRowCount();
                        String modelVar[] = new String[varCount];
                        String modelUnit[] = new String[varCount];
                        String modelDiscription[] = new String[varCount];
                        for (int i = 1; i < varCount; i++) {//从第二行开始读，第一行是title
                            modelVar[i - 1] = (String) jTableFir_1_0.getValueAt(i, 0);
                            modelUnit[i - 1] = (String) jTableFir_1_0.getValueAt(i, 1);
                            modelDiscription[i - 1] = (String) jTableFir_1_0.getValueAt(i, 2);
                        }
                        data.setModelVar(modelVar);
                        data.setModelUnit(modelUnit);
                        data.setModelDiscription(modelDiscription);
                        data.setCreateModelFile(createModelFile);
                        data.saveModelData();
                    }
                });
                jButtonFir_1_1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(2, null);
                    }
                });
                jButtonFir_2_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(2, null);
                    }
                });
                jButtonFir_3_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(3, null);
                    }
                });
                jButtonFir_4_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(4, null);
                    }
                });
                jButtonFir_1_10.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FileChooser fc = new FileChooser("选择保存模型文件夹", FileChooser.SELECT_DIRECTORY, workingDirectory.getAbsolutePath());
                        modelFolder = fc.getFile();
                        //System.out.println("modelFolder=" + modelFolder.getAbsolutePath());
                        if (modelFolder.equals(null)) {
                            modelFolder = new File("C:");
                        }
                        jTextFieldFir_1_1.setText(modelFolder.getAbsolutePath());
                        if (!modelFolder.exists()) {
                            modelFolder.mkdirs();
                        }
                    }
                });
                jTableFir_1_0.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //表格三的鼠标事件处理
                        if (e.getButton() == MouseEvent.BUTTON3) {

                        } else if (e.getButton() == MouseEvent.BUTTON1) {
                            int row = jTableFir_1_0.getSelectedRow();
                            int column = jTableFir_1_0.getSelectedColumn();
                            dealClickOnCell(3, jTableFir_1_0, row, column);
                        } else {
                        }
                    }
                });
                jTableFir_1_0.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            updateTable(1, jTableFir_1_0);
                        }
                    }
                });
                jButtonFir_1_12.addActionListener(new ActionListener() {//新建按钮
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //模型创建脚本存放在工作目录中，不在模型存储文件夹中
                        FileChooser fc = new FileChooser(FileChooser.NEW_FILE, workingDirectory.getAbsolutePath());
                        String fileFilter[] = {"vbs", "py", "txt"};
                        fc.setFileType(fileFilter);
                        fc.setDefaultFileName("createModel.vbs");
                        createModelFile = fc.getFile();
                        if (createModelFile != null) {
                            jLabelFir_1_4.setText(createModelFile.getAbsolutePath());
                            //设置j
                            jTextAreaFir_1.setText("");
                        }
                    }
                });
                jButtonFir_1_11.addActionListener(new ActionListener() {//打开按钮
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //模型创建脚本存放在工作目录中，不在模型存储文件夹中
                        FileChooser fc = new FileChooser(FileChooser.OPEN_FILE, workingDirectory.getAbsolutePath());
                        String fileFilter[] = {"vbs", "py", "txt"};
                        fc.setFileType(fileFilter);
                        fc.setDefaultFileName("createModel.vbs");
                        createModelFile = fc.getFile();
                        if (createModelFile != null) {
                            jLabelFir_1_4.setText(createModelFile.getAbsolutePath());
                            //设置jTextAreaFir_1
                            jTextAreaFir_1.setText("");
                            String content[] = new FileOperate().readFromFileStringArray(createModelFile);
                            for (int i = 0; i < content.length; i++) {
                                jTextAreaFir_1.append(content[i] + "\n");
                            }
                        }
                    }
                });
                break;
            case 2:
                System.out.println("2");
                JPanel firstPanel2 = new JPanel(new BorderLayout());
                firstPanel2.setLayout(new BorderLayout());
                firstPanel2.setBackground(Color.green);
                JPanel secondPanel2 = new JPanel(new BorderLayout());
                secondPanel2.setBackground(Color.YELLOW);
                JPanel thirdPanel2 = new JPanel(new BorderLayout());
                thirdPanel2.setBackground(Color.LIGHT_GRAY);
                JPanel forthPanel2 = new JPanel(new BorderLayout());
                forthPanel2.setBackground(Color.CYAN);
                JLabel jLabel1_0 = new JLabel("模型设置");
                jLabel1_0.setFont(new Font("楷体", Font.BOLD, 20));
//
                JButton jButtonSec_1_0 = new JButton("编辑");
                JButton jButtonSec_2_0 = new JButton("返回");
                JButton jButtonSec_2_1 = new JButton("确定");
                JButton jButtonSec_2_2 = new JButton("继续");
                JButton jButtonSec_3_0 = new JButton("编辑");
                JButton jButtonSec_4_0 = new JButton("编辑");

                JPanel editPanel2 = new JPanel(new GridBagLayout());
                JLabel jLabelSec_2_0 = new JLabel("网格设置");
                jLabelSec_2_0.setFont(new Font("楷体", Font.BOLD, 20));
                JLabel jLabelSec_2_1 = new JLabel("选择网格存储文件夹：");
                JTextField jTextFieldSec_2_1;
                jTextFieldSec_2_1 = new JTextField(new File(workingDirectory, "mesh").getAbsolutePath());

                JButton jButtonSec_2_10 = new JButton("打开");
                JLabel jLabelSec_2_2 = new JLabel("网格控制：");
                JTable jTableSec_2_0 = new JTable(1, 3);
                JLabel jLabelSec_2_3 = new JLabel("网格划分文件编辑：");
                JLabel jLabelSec_2_4 = new JLabel("");
                JTextArea jTextAreaSec_2 = new JTextArea();
                //jTextAreaFir_1.setFont(new Font("宋体",Font.BOLD,14));
                JScrollPane jScrollPaneSec1 = new JScrollPane(jTextAreaSec_2);

                JButton jButtonSec_2_11 = new JButton("打开");
                JButton jButtonSec_2_12 = new JButton("新建");

                Object[] title2 = {"模型参数", "单位", "备注"};
                addRow(0, jTableSec_2_0, 0, title2);

                editPanel2.add(jLabelSec_2_0, new GBC(0, 0).setIpad(80, 10).setWeight(100, 0));//模型设置
                editPanel2.add(jLabelSec_2_1, new GBC(0, 1).setIpad(80, 10).setWeight(100, 0));//选择模型存储文件夹
                editPanel2.add(jTextFieldSec_2_1, new GBC(0, 2, 3, 1).setIpad(60, 10).setWeight(100, 0));
                editPanel2.add(jButtonSec_2_10, new GBC(3, 2).setIpad(20, 10).setWeight(0, 0));
                editPanel2.add(jLabelSec_2_2, new GBC(0, 3).setIpad(80, 10).setWeight(100, 0));//模型控制参数
                editPanel2.add(jTableSec_2_0, new GBC(0, 4, 4, 1).setIpad(80, 30).setWeight(100, 0));
                editPanel2.add(jLabelSec_2_3, new GBC(0, 5).setIpad(30, 10).setWeight(0, 0));//建模文件编辑
                editPanel2.add(jLabelSec_2_4, new GBC(1, 5).setIpad(10, 10).setWeight(100, 0));
                editPanel2.add(jButtonSec_2_11, new GBC(2, 5).setIpad(10, 10).setWeight(0, 0));
                editPanel2.add(jButtonSec_2_12, new GBC(3, 5).setIpad(10, 10).setWeight(0, 0));
                editPanel2.add(jScrollPaneSec1, new GBC(0, 6, 4, 1).setIpad(80, 10).setWeight(100, 100));

                secondPanel2.add(editPanel2, BorderLayout.CENTER);

                firstPanel2.add(jButtonSec_1_0, BorderLayout.SOUTH);
                thirdPanel2.add(jButtonSec_3_0, BorderLayout.SOUTH);
                forthPanel2.add(jButtonSec_4_0, BorderLayout.SOUTH);
                JPanel boxPanelSec = new JPanel(new GridLayout(1, 0));
                boxPanelSec.add(jButtonSec_2_0);
                boxPanelSec.add(jButtonSec_2_1);
                boxPanelSec.add(jButtonSec_2_2);
                secondPanel2.add(boxPanelSec, BorderLayout.SOUTH);

                jPanel.add(firstPanel2, new GBC(0, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));

                jPanel.add(secondPanel2, new GBC(1, 0).
                        setFill(GBC.BOTH).setIpad(500, 400).setWeight(100, 100));

                jPanel.add(thirdPanel2, new GBC(2, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));

                jPanel.add(forthPanel2, new GBC(3, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));
                jButtonSec_1_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(1, null);
                    }
                });
                jButtonSec_2_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(1, null);
                    }
                });
                jButtonSec_2_1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        data.setMeshFolder(new File(jTextFieldSec_2_1.getText()));
                        data.setCreateMeshFile(createMeshFile);
                        saveData(2, data);
                    }
                });
                jButtonSec_2_2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(3, null);
                    }
                });
                jButtonSec_3_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(3, null);
                    }
                });
                jButtonSec_4_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(4, null);
                    }
                });
                jButtonSec_2_12.addActionListener(new ActionListener() {//新建按钮
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //模型创建脚本存放在工作目录中，不在模型存储文件夹中
                        FileChooser fc = new FileChooser(FileChooser.NEW_FILE, workingDirectory.getAbsolutePath());
                        String fileFilter[] = {"rpl", "py", "txt"};
                        fc.setFileType(fileFilter);
                        fc.setDefaultFileName("createMesh.rpl");
                        createMeshFile = fc.getFile();
                        if (createMeshFile != null) {
                            jLabelSec_2_4.setText(createMeshFile.getAbsolutePath());
                            //设置j
                            jTextAreaSec_2.setText("");
                        }
                    }
                });
                jButtonSec_2_11.addActionListener(new ActionListener() {//打开按钮
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //模型创建脚本存放在工作目录中，不在模型存储文件夹中
                        FileChooser fc = new FileChooser(FileChooser.OPEN_FILE, workingDirectory.getAbsolutePath());
                        String fileFilter[] = {"rpl", "py", "txt"};
                        fc.setFileType(fileFilter);
                        fc.setDefaultFileName("createMesh.rpl");
                        createMeshFile = fc.getFile();
                        if (createMeshFile != null) {
                            jLabelSec_2_4.setText(createMeshFile.getAbsolutePath());
                            //设置jTextAreaFir_1
                            jTextAreaSec_2.setText("");
                            String content[] = new FileOperate().readFromFileStringArray(createMeshFile);
                            for (int i = 0; i < content.length; i++) {
                                jTextAreaSec_2.append(content[i] + "\n");
                            }
                        }
                    }
                });

                break;
            case 3:
                JPanel firstPanel3 = new JPanel(new BorderLayout());
                firstPanel3.setBackground(Color.green);
                JPanel secondPanel3 = new JPanel(new BorderLayout());
                secondPanel3.setBackground(Color.YELLOW);
                JPanel thirdPanel3 = new JPanel(new BorderLayout());
                thirdPanel3.setBackground(Color.LIGHT_GRAY);
                JPanel forthPanel3 = new JPanel(new BorderLayout());
                forthPanel3.setBackground(Color.CYAN);
                JLabel jLabel3_0 = new JLabel("模型设置");
                jLabel3_0.setFont(new Font("楷体", Font.BOLD, 20));
//
                JButton jButtonThi_1_0 = new JButton("编辑");
                JButton jButtonThi_2_0 = new JButton("编辑");
                JButton jButtonThi_3_0 = new JButton("返回");
                JButton jButtonThi_3_1 = new JButton("确定");
                JButton jButtonThi_3_2 = new JButton("继续");
                JButton jButtonThi_4_0 = new JButton("编辑");

                JPanel editPanel3 = new JPanel(new GridBagLayout());
                JLabel jLabelThi_3_0 = new JLabel("计算设置");
                jLabelThi_3_0.setFont(new Font("楷体", Font.BOLD, 20));
                JLabel jLabelThi_3_1 = new JLabel("选择计算结果存储文件夹：");
                JTextField jTextFieldThi_3_1;
                jTextFieldThi_3_1 = new JTextField(new File(workingDirectory, "solve").getAbsolutePath());

                JButton jButtonThi_3_10 = new JButton("打开");

                JPanel calControlPanel = new JPanel(new GridLayout(0, 1));
                JPanel matCtrlPanel = new JPanel(new BorderLayout());
                matCtrlPanel.add(new JLabel("材料控制"), BorderLayout.NORTH);
                JPanel matCtrl = new JPanel(new FlowLayout());
                ButtonGroup matTypeCtrlButtonGroup = new ButtonGroup();
                JCheckBox useFluentMatHub = new JCheckBox("使用Fluent材料库");
                JCheckBox useUserMatHub = new JCheckBox("使用用户定义材料库", true);
                JCheckBox useNoMatHub = new JCheckBox("不使用任何材料库");
                JPanel userDefinedMatHub = new JPanel(new GridLayout(1, 0));

                JLabel userDefinedMatHubLabel = new JLabel("材料库名称：");
                userDefinedMatHub.add(userDefinedMatHubLabel);
                JTextField userDefinedMatHubTextField = new JTextField();
                JButton userDefinedMatHubButton = new JButton("选择材料库");
                userDefinedMatHub.add(userDefinedMatHubTextField);
                userDefinedMatHub.add(userDefinedMatHubButton);

                matTypeCtrlButtonGroup.add(useFluentMatHub);
                matTypeCtrlButtonGroup.add(useUserMatHub);
                matTypeCtrlButtonGroup.add(useNoMatHub);
                matCtrl.add(useFluentMatHub);
                matCtrl.add(useUserMatHub);
                matCtrl.add(useNoMatHub);
                matCtrlPanel.add(matCtrl, BorderLayout.CENTER);
                matCtrlPanel.add(userDefinedMatHub, BorderLayout.SOUTH);

                calControlPanel.add(matCtrlPanel);

                JLabel jLabelThi_3_3 = new JLabel("计算设置文件编辑：");
                JLabel jLabelThi_3_4 = new JLabel("");
                JTextArea jTextAreaThi_3 = new JTextArea();
                //jTextAreaFir_1.setFont(new Font("宋体",Font.BOLD,14));
                JScrollPane jScrollPaneThi1 = new JScrollPane(jTextAreaThi_3);

                JButton jButtonThi_3_11 = new JButton("打开");
                JButton jButtonThi_3_12 = new JButton("新建");

                editPanel3.add(jLabelThi_3_0, new GBC(0, 0).setIpad(80, 10).setWeight(100, 0));//计算设置
                editPanel3.add(jLabelThi_3_1, new GBC(0, 1).setIpad(80, 10).setWeight(100, 0));//选择计算存储文件夹
                editPanel3.add(jTextFieldThi_3_1, new GBC(0, 2, 3, 1).setIpad(60, 10).setWeight(100, 0));
                editPanel3.add(jButtonThi_3_10, new GBC(3, 2).setIpad(20, 10).setWeight(0, 0));

                editPanel3.add(calControlPanel, new GBC(0, 3, 4, 1).setIpad(80, 30).setWeight(100, 0));
                editPanel3.add(jLabelThi_3_3, new GBC(0, 4).setIpad(30, 10).setWeight(0, 0));//建模文件编辑
                editPanel3.add(jLabelThi_3_4, new GBC(1, 4).setIpad(10, 10).setWeight(100, 0));
                editPanel3.add(jButtonThi_3_11, new GBC(2, 4).setIpad(10, 10).setWeight(0, 0));
                editPanel3.add(jButtonThi_3_12, new GBC(3, 4).setIpad(10, 10).setWeight(0, 0));
                editPanel3.add(jScrollPaneThi1, new GBC(0, 5, 4, 1).setIpad(80, 10).setWeight(100, 100));

                thirdPanel3.add(editPanel3, BorderLayout.CENTER);

                firstPanel3.add(jButtonThi_1_0, BorderLayout.SOUTH);
                secondPanel3.add(jButtonThi_2_0, BorderLayout.SOUTH);
                forthPanel3.add(jButtonThi_4_0, BorderLayout.SOUTH);
                JPanel buttonPanelThi_3_0 = new JPanel(new GridLayout(1, 0));
                buttonPanelThi_3_0.add(jButtonThi_3_0);
                buttonPanelThi_3_0.add(jButtonThi_3_1);
                buttonPanelThi_3_0.add(jButtonThi_3_2);

                thirdPanel3.add(buttonPanelThi_3_0, BorderLayout.SOUTH);
                jPanel.add(firstPanel3, new GBC(0, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));

                jPanel.add(secondPanel3, new GBC(1, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));

                jPanel.add(thirdPanel3, new GBC(2, 0).
                        setFill(GBC.BOTH).setIpad(500, 400).setWeight(100, 100));

                jPanel.add(forthPanel3, new GBC(3, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));
                jButtonThi_1_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(1, null);
                    }
                });
                jButtonThi_2_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(2, null);
                    }
                });
                jButtonThi_3_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(2, null);
                    }
                });
                jButtonThi_3_1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        data.setSolveFolder(new File(jTextFieldThi_3_1.getText()));
                        data.setCreateSolveFile(new File(jLabelThi_3_4.getText()));

                    }
                });
                jButtonThi_3_2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(4, null);
                    }
                });
                jButtonThi_4_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(4, null);
                    }
                });
                jButtonThi_3_12.addActionListener(new ActionListener() {//新建按钮
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //模型创建脚本存放在工作目录中，不在模型存储文件夹中
                        FileChooser fc = new FileChooser(FileChooser.NEW_FILE, workingDirectory.getAbsolutePath());
                        String fileFilter[] = {"jou", "py", "txt"};
                        fc.setFileType(fileFilter);
                        fc.setDefaultFileName("createSolve.jou");
                        createSolveFile = fc.getFile();
                        if (createSolveFile != null) {
                            jLabelThi_3_4.setText(createSolveFile.getAbsolutePath());
                            //设置j
                            jTextAreaThi_3.setText("");
                        }
                    }
                });
                jButtonThi_3_11.addActionListener(new ActionListener() {//打开按钮
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //模型创建脚本存放在工作目录中，不在模型存储文件夹中
                        FileChooser fc = new FileChooser(FileChooser.OPEN_FILE, workingDirectory.getAbsolutePath());
                        String fileFilter[] = {"jou", "py", "txt"};
                        fc.setFileType(fileFilter);
                        fc.setDefaultFileName("createSolve.jou");
                        createSolveFile = fc.getFile();
                        if (createSolveFile != null) {
                            jLabelThi_3_4.setText(createSolveFile.getAbsolutePath());
                            //设置jTextAreaFir_1
                            jTextAreaThi_3.setText("");
                            String content[] = new FileOperate().readFromFileStringArray(createSolveFile);
                            for (int i = 0; i < content.length; i++) {
                                jTextAreaThi_3.append(content[i] + "\n");
                            }
                        }
                    }
                });

                useFluentMatHub.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (useFluentMatHub.isSelected()) {
                            userDefinedMatHub.removeAll();
                            matCtrl.revalidate();
                        }
                    }
                });
                useUserMatHub.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (useUserMatHub.isSelected()) {
                            userDefinedMatHub.add(userDefinedMatHubLabel);
                            userDefinedMatHub.add(userDefinedMatHubTextField);
                            userDefinedMatHub.add(userDefinedMatHubButton);
                            matCtrl.revalidate();
                        }
                    }
                });
                useNoMatHub.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        if (useUserMatHub.isSelected()) {
                            userDefinedMatHub.removeAll();
                            matCtrl.revalidate();
                        }
                    }
                });

                userDefinedMatHubButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        FileChooser fc = new FileChooser(FileChooser.OPEN_FILE, workingDirectory.getAbsolutePath());
                        data.setMaterialDataBaseFile(fc.getFile());
                        userDefinedMatHubTextField.setText(data.getMaterialDataBaseFile().getAbsolutePath());
                    }
                });

                break;
            case 4:
                JPanel firstPanel4 = new JPanel(new BorderLayout());
                firstPanel4.setBackground(Color.green);
                JPanel secondPanel4 = new JPanel(new BorderLayout());
                secondPanel4.setBackground(Color.YELLOW);
                JPanel thirdPanel4 = new JPanel(new BorderLayout());
                thirdPanel4.setBackground(Color.LIGHT_GRAY);
                JPanel forthPanel4 = new JPanel(new BorderLayout());
                forthPanel4.setBackground(Color.CYAN);

                JButton jButtonFor_1_0 = new JButton("编辑");
                JButton jButtonFor_2_0 = new JButton("编辑");
                JButton jButtonFor_3_0 = new JButton("编辑");

                JButton jButtonFor_4_0 = new JButton("确定");
                JButton jButtonFor_4_1 = new JButton("返回");

                JPanel editPanel4 = new JPanel(new GridBagLayout());
                JLabel jLabelFor_4_0 = new JLabel("数据处理设置");
                jLabelFor_4_0.setFont(new Font("楷体", Font.BOLD, 20));
                JLabel jLabelFor_4_1 = new JLabel("选择数据处理结果保存文件夹：");
                JTextField jTextFieldFor_4_1;
                jTextFieldFor_4_1 = new JTextField(new File(workingDirectory, "result").getAbsolutePath());

                JButton jButtonFor_4_10 = new JButton("打开");
                JLabel jLabelFor_4_2 = new JLabel("网格控制：");
                JTable jTableFor_4_0 = new JTable(1, 3);
                JLabel caseTypeLabel = new JLabel("优化类型：");
                String caseTypeName[] = new String[3];
                caseTypeName[0] = (OptimizationData.OptimizationType.Diff_Model).toString();
                caseTypeName[1] = (OptimizationData.OptimizationType.Diff_Mesh).toString();
                caseTypeName[2] = (OptimizationData.OptimizationType.Diff_Solve).toString();

                JCheckBox batSolveCheckBox = new JCheckBox("批量计算", false);

                JComboBox caseTypeCombo = new JComboBox(caseTypeName);

                JLabel jLabelFor_4_3 = new JLabel("网格划分文件编辑：");
                JLabel jLabelFor_4_4 = new JLabel("");
                JTextArea jTextAreaFor_4 = new JTextArea();
                //jTextAreaFir_1.setFont(new Font("宋体",Font.BOLD,14));
                JScrollPane jScrollPaneFor1 = new JScrollPane(jTextAreaFor_4);

                JButton jButtonFor_4_11 = new JButton("打开");
                JButton jButtonFor_4_12 = new JButton("新建");

                Object[] title4 = {"模型参数", "单位", "备注"};
                addRow(0, jTableFor_4_0, 0, title4);

                editPanel4.add(jLabelFor_4_0, new GBC(0, 0).setIpad(80, 10).setWeight(100, 0));//模型设置
                editPanel4.add(jLabelFor_4_1, new GBC(0, 1).setIpad(80, 10).setWeight(100, 0));//选择模型存储文件夹
                editPanel4.add(jTextFieldFor_4_1, new GBC(0, 2, 3, 1).setIpad(60, 10).setWeight(100, 0));
                editPanel4.add(jButtonFor_4_10, new GBC(3, 2).setIpad(20, 7).setWeight(0, 0));
                editPanel4.add(caseTypeLabel, new GBC(0, 3).setIpad(20, 7).setWeight(0, 0));
                editPanel4.add(caseTypeCombo, new GBC(1, 3).setIpad(20, 7).setWeight(100, 0));
                editPanel4.add(batSolveCheckBox, new GBC(2, 3).setIpad(20, 10).setWeight(0, 0));
                editPanel4.add(jLabelFor_4_2, new GBC(0, 4).setIpad(80, 10).setWeight(100, 0));//模型控制参数
                editPanel4.add(jTableFor_4_0, new GBC(0, 5, 4, 1).setIpad(80, 30).setWeight(100, 0));
                editPanel4.add(jLabelFor_4_3, new GBC(0, 6).setIpad(30, 10).setWeight(0, 0));//建模文件编辑
                editPanel4.add(jLabelFor_4_4, new GBC(1, 6).setIpad(10, 10).setWeight(100, 0));
                editPanel4.add(jButtonFor_4_11, new GBC(2, 6).setIpad(10, 10).setWeight(0, 0));
                editPanel4.add(jButtonFor_4_12, new GBC(3, 6).setIpad(10, 10).setWeight(0, 0));
                editPanel4.add(jScrollPaneFor1, new GBC(0, 7, 4, 1).setIpad(80, 10).setWeight(100, 100));

                forthPanel4.add(editPanel4, BorderLayout.CENTER);

                firstPanel4.add(jButtonFor_1_0, BorderLayout.SOUTH);
                secondPanel4.add(jButtonFor_2_0, BorderLayout.SOUTH);
                thirdPanel4.add(jButtonFor_3_0, BorderLayout.SOUTH);
                JPanel buttonPanelFor_4_0 = new JPanel(new GridLayout(1, 0));
                buttonPanelFor_4_0.add(jButtonFor_4_1);
                buttonPanelFor_4_0.add(jButtonFor_4_0);

                forthPanel4.add(buttonPanelFor_4_0, BorderLayout.SOUTH);

                jPanel.add(firstPanel4, new GBC(0, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));

                jPanel.add(secondPanel4, new GBC(1, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));

                jPanel.add(thirdPanel4, new GBC(2, 0).
                        setFill(GBC.BOTH).setIpad(50, 400).setWeight(0, 100));

                jPanel.add(forthPanel4, new GBC(3, 0).
                        setFill(GBC.BOTH).setIpad(500, 400).setWeight(100, 100));
                jButtonFor_1_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(1, null);
                    }
                });
                jButtonFor_2_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(2, null);
                    }
                });
                jButtonFor_3_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(3, null);
                    }
                });
                jButtonFor_4_0.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        changePanel(3, null);
                    }
                });
                jButtonFor_4_1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //保存面板四中的数据
                        data.setResultFolder(new File(jTextFieldFor_4_1.getText()));
                        data.setDataProcessFile(dataProcessFile);
                        data.setOptimizationType((String) caseTypeCombo.getSelectedItem());
                        data.setBatchSolve(batSolveCheckBox.isSelected());
                    }
                });
                jButtonFor_4_12.addActionListener(new ActionListener() {//新建按钮
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //模型创建脚本存放在工作目录中，不在模型存储文件夹中
                        FileChooser fc = new FileChooser(FileChooser.NEW_FILE, workingDirectory.getAbsolutePath());
                        String fileFilter[] = {"vbs", "py", "txt"};
                        fc.setFileType(fileFilter);
                        fc.setDefaultFileName("dataProcess.vbs");
                        dataProcessFile = fc.getFile();
                        if (dataProcessFile != null) {
                            jLabelFor_4_4.setText(dataProcessFile.getAbsolutePath());
                            //设置j
                            jTextAreaFor_4.setText("");
                        }
                    }
                });
                jButtonFor_4_11.addActionListener(new ActionListener() {//打开按钮
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //模型创建脚本存放在工作目录中，不在模型存储文件夹中
                        FileChooser fc = new FileChooser(FileChooser.OPEN_FILE, workingDirectory.getAbsolutePath());
                        String fileFilter[] = {"vbs", "py", "txt"};
                        fc.setFileType(fileFilter);
                        fc.setDefaultFileName("dataProcess.vbs");
                        dataProcessFile = fc.getFile();
                        if (dataProcessFile != null) {
                            jLabelFor_4_4.setText(dataProcessFile.getAbsolutePath());
                            //设置jTextAreaFir_1
                            jTextAreaFor_4.setText("");
                            String content[] = new FileOperate().readFromFileStringArray(dataProcessFile);
                            for (int i = 0; i < content.length; i++) {
                                jTextAreaFor_4.append(content[i] + "\n");
                            }
                        }
                    }
                });
                caseTypeCombo.addActionListener(new ActionListener() {//优化类型下拉列表
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (caseTypeCombo.getSelectedItem().equals("Diff_Model")) {
                            data.setOptimizationType(OptimizationData.OptimizationType.Diff_Model);
                        } else if (caseTypeCombo.getSelectedItem().equals("Diff_Mesh")) {
                            data.setOptimizationType(OptimizationData.OptimizationType.Diff_Mesh);
                        } else if (caseTypeCombo.getSelectedItem().equals("Diff_Solve")) {
                            data.setOptimizationType(OptimizationData.OptimizationType.Diff_Solve);
                        }
                    }
                });
                break;
            default:
                break;
        }
        jPanel.revalidate();
        return jPanel;
    }

    private void changePanel(int tab, Object object) {
        contentPanel.removeAll();
        contentPanel.add(getGridBagPanes(tab, object));
        contentPanel.revalidate();
//        this.getContentPane().removeAll();
//        this.getContentPane().add(getGridBagPanes(tab, object));
        this.revalidate();
    }

    private void dealClickOnCell(int tab, JTable table, int rowNum, int colNum) {
        if (tab == 1) {//点击的是模型面板中的表格
            if (table.getSelectedRow() == rowNum) {

            }
        }
    }

    private void updateTable(int tab, JTable table) {
        //当按下enter键后，如果最后一行有数据，就表格增加一行
        int rowCount = table.getRowCount();
        System.out.println("total rows=" + rowCount);
        String lastVar = (String) table.getValueAt(rowCount - 1, 0);
        if (!lastVar.trim().equals("")) {//如果最后一行第一列不为空
            System.out.println("add Row");
            DefaultTableModel dtm = (DefaultTableModel) table.getModel();
            Object[] nullRow = new Object[1];
            nullRow[0] = "";
            dtm.addRow(nullRow);
        }
    }

    private void aboutMesh() {
        JFrame meshFrame = new JFrame();
        meshFrame.setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        JTextArea infoTextField = new JTextArea();
        infoTextField.setEditable(false);
        String info = "网格各部分的part名注意和计算设置文件中一致，示例文件中的Part共有back, front, up, down和structure五个固体壁面部分。\n";
        info += "进出口part名为inlet, outlet且进出口两个面为周期性对称边界。";
        infoTextField.setText(info);
        JScrollPane scrollPane = new JScrollPane(infoTextField);
        contentPanel.add(scrollPane);
        meshFrame.add(contentPanel);
        meshFrame.setSize(900, 600);
        meshFrame.setVisible(true);
        meshFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void aboutSolve() {
        JFrame meshFrame = new JFrame();
        meshFrame.setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel(new BorderLayout());
        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setText("网格计算文件以“%”开头的行为注释行\n");
        infoTextArea.append("首先读入网格文件并进行网格检查，命令如下：\n");
        infoTextArea.append("/file read-case D:/Users/2017/paper3/toyashen/calculate/mesh/case2.msh	 \n"
                + "/mesh check	 \n"
                + "/mesh scale 0.000001 0.000001 0.000001     \n"
                + "%注意上面的网格文件路径，如果需要批量代替，将网格文件路径改为“$meshPath$”,然后由软件自动设置！\n\n");

        infoTextArea.append("打开能量方程，命令如下：\n");
        infoTextArea.append("/define models energy yes no no no yes \n");

        infoTextArea.append("\n");
        infoTextArea.append("设置牛顿流体(以水为例，包含密度1000，比热容4100，导热系数0.7，粘度系数0.001四个参数)，具体的参数值在外部materialProperties.txt中设置，如果是固定值，也可以在下面命令中直接替代相应属性名：\n");
        infoTextArea.append("/define materials change-create air materialName yes constant  density yes constant  heatCapacity yes constant  thermalConductivity yes constant  viscosity no no no\n");
        infoTextArea.append("也可以使用fluent自带的液体水(water-liquid)工质，命令如下：\n");
        infoTextArea.append("/define materials copy fluid water-liquid\n");

        infoTextArea.append("\n");
        infoTextArea.append("Cell Zone Conditions设置：\n");
        infoTextArea.append("(cx-gui-do cx-activate-item \"NavigationPane*Frame1*PushButton8(Cell Zone Conditions)\")	 \n"
                + "(cx-gui-do cx-set-list-selections \"Boundary Conditions*Frame1*Table1*Frame1*List1(Zone)\" '( 0))	 \n"
                + "选择Zone列表中第一个Zone,Zone的名字是ICEM划分网格时设置的域名字，即，建议划分网格时设置为fluid"
                + "(cx-gui-do cx-activate-item \"Boundary Conditions*Frame1*Table1*Frame1*List1(Zone)\")	 \n"
                + "(cx-gui-do cx-activate-item \"Boundary Conditions*Frame1*Table1*Frame2*Table2*Frame4*Table4*ButtonBox1*PushButton1(Edit)\")	 \n"
                + "(cx-gui-do cx-set-list-selections \"fluid-11-1*Frame3*Table3*Frame1*Table1*DropDownList1(Material Name)\" '( water))\n"
                + "(cx-gui-do cx-activate-item \"fluid-11-1*Frame3*Table3*Frame1*Table1*DropDownList1(Material Name)\")\n"
                + "(cx-gui-do cx-set-text-entry \"fluid-11-1*TextEntry1(Zone Name)\" \"fluid\")	 \n"
                + "(cx-gui-do cx-activate-item \"fluid-11-1*PanelButtons*PushButton1(OK)\")	 \n");

        infoTextArea.append("(cx-gui-do cx-set-text-entry \"fluid-12-1*TextEntry1(Zone Name)\" \"fluid\")	 \n");
        infoTextArea.append("(cx-gui-do cx-activate-item \"fluid-12-1*PanelButtons*PushButton1(OK)\")	 \n");
        infoTextArea.append("中的“fluid-12-1”是与网格文件中流体域有关的一个编号，注意更改,如果ICEM划分网格时设置了材料体“fluid”，那么此处可以由软件自动识别编号\n");

        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        meshFrame.add(contentPanel);
        meshFrame.setSize(900, 600);
        meshFrame.setVisible(true);
        meshFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     *
     * @param tab tab==0表示每次写入一行保证table后有一个空行
     * @param table 写入的table变量名
     * @param rowNum 写入的数据所在行号 写入的数据
     */
    private void addRow(int tab, JTable table, int rowNum, Object[] data) {
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
    }

    /**
     *
     * @param tab tab==1表示从模型开始初始化，tab==2表示从网格开始初始化，tab==3表示从计算开始初始化
     */
    private void initSolve(int tab) {
        if (tab == 1) {

        } else if (tab == 2) {

        } else if (tab == 3) {
            data.checkData(3);//检测数据是否齐全
        }
    }

    private void optimization(OptimizationData data) {
        if (data.getOptimizationType().equals(OptimizationData.OptimizationType.Diff_Model)) {
            initSolve(1);
        } else if (data.getOptimizationType().equals(OptimizationData.OptimizationType.Diff_Mesh)) {
            initSolve(2);
        } else if (data.getOptimizationType().equals(OptimizationData.OptimizationType.Diff_Solve)) {
            initSolve(3);
            solve3(data);
        }
    }

    /**
     * 该求解方法： 有多个固定的网格文件 材料参数可变 边界条件可变
     *
     * @param data
     */
    private void solve3(OptimizationData data) {
        String caseName = "";
        if (data.isBatchSolve()) {//如果是批量求解，则直接从material.csv和boundaryConditions.csv中读取计算文件设置信息，并替换
            //首先获得所有网格文件
            ArrayList<File> meshFiles = data.getMeshFiles();

            for (File meshFile : meshFiles) {
                caseName += meshFile.getName().replace(".msh", "");//将网格文件名去除后缀后追加到算例名之后
                //生成jou文件，首先获得jou文件的范例文件
                File solveFile = data.getCreateSolveFile();
                //赋值求解范例文件到新的临时文件，暂命名为tempSolveFile
                File tempSolveFile = new File("solve\\tempSolveFile.jou");
                FileOperate.copy(solveFile, tempSolveFile);
                //替换$meshFolder$
                replaceStrInFile(tempSolveFile, "$meshFile$", meshFile.getAbsoluteFile().toString());
                dealMatAndBCSets(data, tempSolveFile, caseName);

            }
        }

    }

    private void solve2(OptimizationData data) {

    }

    private void solve1(OptimizationData data) {

    }

    private void dealMatAndBCSets(OptimizationData data, File solveFile, String caseName) {
        //读取material.csv文件
        FileOperate fo = new FileOperate();
        String[] materials = fo.readFromFileStringArray(new File("materials.csv"));
        String[] matProNames = materials[0].split(",");
        for (int i = 1; i < materials.length; i++) {//针对每个材料创建一个求解文件
            if (data.getFluidType().equals(OptimizationData.FluidType.UserDefinedDataBase)) {
                String dataBaseName = data.getMaterialDataBaseFile().toString();
                replaceStrInFile(solveFile, "$userDefinedDataBaseName$", dataBaseName);
            }
            String[] properties = materials[i].split(",");
            int numOfMatProperties = properties.length;
            for (int propertiesIndex = 0; propertiesIndex < numOfMatProperties; propertiesIndex++) {
                if (propertiesIndex == 0) {
                    caseName = caseName + "_" + properties[0];  //caseName加上材料名
                }
                replaceStrInFile(solveFile, "$" + matProNames[i] + "$", properties[propertiesIndex]);
            }
            String boundaryConditions[] = fo.readFromFileStringArray(new File("boundaryConditions.csv"));
            String bCNames[] = boundaryConditions[0].split(",");
            //针对每种边界条件创建一种求解文件
            int numOfBCLength = bCNames.length;//就是一个边界条件里面有多少变量需要设置
            for (int j = 1; j < boundaryConditions.length; j++) {
                for (int bCIndex = 0; bCIndex < numOfBCLength; bCIndex++) {

                    String boundaryCondition[] = boundaryConditions[j].split(",");
                    if (bCIndex == 0) {
                        caseName = caseName + "_" + boundaryCondition[0];//caseName加上边界条件名
                    }
                    replaceStrInFile(solveFile, "$" + bCNames[bCIndex] + "$", boundaryCondition[bCIndex]);
                }
                caseName = caseName + ".jou";
                solveFile.renameTo(new File(data.getSolveFolder(), caseName));

                //调用Fluent运行
            }
        }
    }

    private void replaceStrInFile(File file, String str1, String str2) {
        FileOperate fo = new FileOperate(file);
        String lines[] = fo.readFromFileStringArray(file);
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll(str1, str2);
        }
        fo.writeToFileStrings(lines, false);
    }

}
