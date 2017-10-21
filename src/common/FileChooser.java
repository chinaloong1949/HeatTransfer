/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 该类用法：
 * tab的值看init()方法下面的介绍
 */
package common;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Administrator
 */
public class FileChooser extends JFileChooser {
    
    public static final int OPEN_FILE = 1;
    public static final int SELECT_MULTI_FILES = 5;
    public static final int SELECT_DIRECTORY = 3;
    public static final int SAVE_TO_FILE = 2;
    public static final int NEW_FILE = 4;
    File file = null;
    String title = "";
    String fileType[] = null;
    private String defaultFileName = "default.txt";
    private int tab = 1;

    /**
     *
     * @param title 标题
     * @param tab
     * 标记FileChooser的类型是用于打开文件还是保存文件等，可以取的值有FileChooser.OPEN_FILE,
 FileChooser.SELECT_DIRECTORY,FileChooser.NEW_FILE和FileChooser.SAVE_FILE
     * @param fileType 过滤指定后缀名的文件，如String[] fileType={"jpg","png"}会
     * 过滤出*.jpg和*.png的文件在文件选择窗口中
     * @param defaultFileName
     * 默认选择的文件名称，相对路径，默认值为"default.txt",仅tab==FileChooser.SAVE_FILE时有效
     */
    public FileChooser(String title, int tab, String[] fileType, String defaultFileName) {
        super.setDialogTitle(title);
        this.title = title;//这句用来标记title是否被调用者修改
        this.fileType = fileType;
        this.defaultFileName = defaultFileName;
        this.tab = tab;
    }

    /**
     *
     * @param tab
     * 标记FileChooser的类型是用于打开文件还是保存文件等，可以取的值有FileChooser.OPEN_FILE,
 FileChooser.SELECT_DIRECTORY,FileChooser.NEW_FILE和FileChooser.SAVE_FILE
     */
    public FileChooser(int tab) {
        this.tab = tab;
    }

    /**
     *
     * @param tab
     * 标记FileChooser的类型是用于打开文件还是保存文件等，可以取的值有FileChooser.OPEN_FILE,
 FileChooser.SELECT_DIRECTORY,FileChooser.NEW_FILE和FileChooser.SAVE_FILE
     * @param defaultFilePath FileChooser打开时所在的默认路径
     */
    public FileChooser(int tab, String defaultFilePath) {
        super(defaultFilePath);
        this.tab = tab;
    }

    /**
     * FileChooser继承自JFileChooser
     *
     * @param title FileChooser的标题
     * @param tab
     * 标记FileChooser的类型是用于打开文件还是保存文件等，可以取的值有FileChooser.OPEN_FILE,
 FileChooser.SELECT_DIRECTORY,FileChooser.NEW_FILE和FileChooser.SAVE_FILE
     */
    public FileChooser(String title, int tab) {
        super(new File("C:"));//设置默认路径
        this.title = title;//这句用来标记title是否被调用者修改
        super.setDialogTitle(title);
        this.tab = tab;
    }

    /**
     *
     * @param title 对话框的标题
     * @param tab 可以取的值有FileChooser.OPEN_FILE,
 FileChooser.SELECT_DIRECTORY,FileChooser.NEW_FILE和FileChooser.SAVE_FILE
     * @param defaultFilePath 对话框默认的路径
     */
    public FileChooser(String title, int tab, String defaultFilePath) {
        super(defaultFilePath);
        super.setDialogTitle(title);
        this.title = title;//这句用来标记title是否被调用者修改
        this.tab = tab;
    }

    /**
     *
     * @param title FileChooser的标题
     * @param tab
     * 标记FileChooser的类型是用于打开文件还是保存文件等，可以取的值有FileChooser.OPEN_FILE,
 FileChooser.SELECT_DIRECTORY,FileChooser.NEW_FILE和FileChooser.SAVE_FILE
     * @param fileType 过滤指定后缀名的文件，如String[] fileType={"jpg","png"}会
     * 过滤出*.jpg和*.png的文件在文件选择窗口中
     */
    public FileChooser(String title, int tab, String[] fileType) {
        super.setDialogTitle(title);
        this.title = title;//这句用来标记title是否被调用者修改
        this.fileType = fileType;
        this.tab = tab;
    }

    /**
     *
     * @param tab
     * 标记FileChooser的类型是用于打开文件还是保存文件等，可以取的值有FileChooser.OPEN_FILE,
 FileChooser.SELECT_DIRECTORY,FileChooser.NEW_FILE和FileChooser.SAVE_FILE
     * @param fileType 过滤指定后缀名的文件，如String[] fileType={"jpg","png"}会
     * 过滤出*.jpg和*.png的文件在文件选择窗口中
     */
    public FileChooser(int tab, String[] fileType) {
        
        this.fileType = fileType;
        this.tab = tab;
    }
    
    private File init(int tab) {
        //tab==1表示打开操作文件选择器,可以选择一个文件，不存在则提示
        //tab==2表示储存操作文件选择器
        //tab==3表示选择文件夹操作
        //tab==4新建文件选择器，如果不存在则新建，存在则提示
        //tab==5表示打开操作文件选择器，允许同时选择多个文件
        File file1 = null;
        
        switch (tab) {
            case 1:
                if (this.title.equals("")) {
                    this.title = "打开文件";
                }
                if (fileType != null) {
                    this.setAcceptAllFileFilterUsed(false);
                    this.addChoosableFileFilter(new FileNameExtensionFilter(
                            getDescription_1(fileType), fileType));
                }
                int openResult1 = this.showOpenDialog(null);
                if (openResult1 == JFileChooser.APPROVE_OPTION) {
                    file1 = this.getSelectedFile();
                    if (!file1.exists()) {
                        JOptionPane.showMessageDialog(null, "您选择的文件不存在，请确认后重试！");
                        file1 = null;
                    }
                    System.out.println("105FileChooser:file1=" + file1);
                }
                break;
            case 2:
                if (title.equals("")) {
                    this.title = "保存为";
                }
                this.setDialogTitle(title);
                this.setSelectedFile(new File(defaultFileName));
                if (fileType != null) {
                    this.setAcceptAllFileFilterUsed(true);
                    this.setFileFilter(new FileNameExtensionFilter(getDescription_1(fileType), fileType));
                }
                int openResult2 = this.showSaveDialog(null);
                if (openResult2 == JFileChooser.APPROVE_OPTION) {
                    file1 = this.getSelectedFile();
                    if (file1.exists()) {
                        int flagint = JOptionPane.showConfirmDialog(null, "您选择的文件已经存在，是否覆盖！", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (flagint == JOptionPane.YES_OPTION) {
                            file1.delete();
                            try {
                                file1.createNewFile();
                            } catch (IOException ex) {
                                Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (flagint == JOptionPane.NO_OPTION) {
                            file1 = null;
                        } else {
                            file1 = null;
                        }
                    } else {
                        try {
                            file1.createNewFile();
                        } catch (IOException ex) {
                            Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("105FileChooser:file1=" + file1);
                }
                
                break;
            case 3:
                if (this.title.equals("")) {
                    this.title = "请选择文件夹";
                }
                this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                try {
                    int openResult3 = this.showOpenDialog(null);
                    if (openResult3 == JFileChooser.APPROVE_OPTION) {
                        file1 = this.getSelectedFile();
                        if (!file1.exists()) {
                            JOptionPane.showMessageDialog(null, "您选择的文件夹不存在，请确认后重试！");
                            file1 = null;
                        }
                        System.out.println("105FileChooser:file1=" + file1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("file=" + file1);
                break;
            
            case 4:
                if (this.title.equals("")) {
                    this.title = "新建文件";
                }
                if (this.defaultFileName != null) {
                    this.setSelectedFile(new File(defaultFileName));
                }
                if (fileType != null) {
                    this.setAcceptAllFileFilterUsed(false);
                    this.addChoosableFileFilter(new FileNameExtensionFilter(
                            getDescription_1(fileType), fileType));
                }
                int openResult4 = this.showOpenDialog(null);
                if (openResult4 == JFileChooser.APPROVE_OPTION) {
                    file1 = this.getSelectedFile();
                    if (file1.exists()) {
                        int flagint = JOptionPane.showConfirmDialog(null, "您选择的文件已经存在，是否覆盖！", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
                        if (flagint == JOptionPane.YES_OPTION) {
                            file1.delete();
                            try {
                                file1.createNewFile();
                            } catch (IOException ex) {
                                Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (flagint == JOptionPane.NO_OPTION) {
                            file1 = null;
                        } else {
                            file1 = null;
                        }
                    } else {
                        try {
                            file1.createNewFile();
                        } catch (IOException ex) {
                            Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("105FileChooser:file1=" + file1);
                }
                break;
            case 5:
                this.setMultiSelectionEnabled(true);
                if (fileType != null) {
                    this.setAcceptAllFileFilterUsed(false);
                    this.addChoosableFileFilter(new FileNameExtensionFilter(
                            getDescription_1(fileType), fileType));
                }
                int openResult5 = this.showOpenDialog(null);
                if (openResult5 == JFileChooser.APPROVE_OPTION) {
                    file1 = this.getSelectedFile();
                    System.out.println("147FileChooser:file1=" + file1);
                }
                break;
            default:
                System.out.println("151FileChooser:文件选择器出错，错误的tab指示值！");
        }
        return file1;
    }
    
    private String getDescription_1(String[] fileType) {
        //通过过滤类型数组获得描述字符串
        String description = "";
        for (String s : fileType) {
            description = description + "*." + s + "; ";
        }
        return description;
    }
    
    public void setDefaultFileName(String defaultFileName) {
        this.defaultFileName = defaultFileName;
    }
    
    public void setFileType(String[] fileType) {
        this.fileType = fileType;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public File getFile() {
        file = init(this.tab);
        return file;
    }
    
}
