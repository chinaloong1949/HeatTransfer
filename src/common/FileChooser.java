/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 该类用法：
 * tab的值看init()方法下面的介绍
 */
package common;

import java.io.File;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Administrator
 */
public class FileChooser extends JFrame {

    File file = null;
    String title = "设置保存文件路径名称";
    String fileType[] = null;
    private String defaultFileName = "default.txt";
    private String defaultFilePath = "C:";

    public FileChooser(String title, int tab, String[] fileType, String defaultFileName) {
        this.title = title;
        this.fileType = fileType;
        this.defaultFileName = defaultFileName;
        this.file = init(tab);
    }

    public FileChooser(String title, int tab) {
        this.title = title;
        this.file = init(tab);
    }

    public FileChooser(String title, int tab, String defaultFilePath) {
        this.defaultFilePath = defaultFilePath;
        this.title = title;
        this.file = init(tab);
    }

    public FileChooser(String title, int tab, String[] fileType) {
        this.title = title;
        this.fileType = fileType;
        this.file = init(tab);
    }

    public FileChooser(int tab, String[] fileType) {

        this.fileType = fileType;
        this.file = init(tab);
    }

    private File init(int tab) {
        //tab==1表示打开操作文件选择器,可以选择一个文件
        //tab==2表示储存操作文件选择器
        //tab==3表示选择文件夹操作
        //tab==4表示允许选择文件夹或者文件
        //tab==5表示打开操作文件选择器，允许同时选择多个文件
        File file1 = null;

        switch (tab) {
            case 1:
                JFileChooser fc1 = new JFileChooser(defaultFilePath);
                if (fileType != null) {
                    fc1.setAcceptAllFileFilterUsed(false);
                    fc1.addChoosableFileFilter(new FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            if (f.isDirectory()) {
                                return true;
                            }
                            String fileName = f.getName();
                            int index = fileName.lastIndexOf('.');
                            if (index > 0 && index < fileName.length() - 1) {
                                String extension = fileName.substring(index + 1).toLowerCase();
                                /*if (extension.equals("csv") || extension.equals("CSV")
                                 || extension.equals("txt") || extension.equals("TXT")
                                 || extension.equals("dat") || extension.equals("DAT")) {
                                 return true;
                                 }*/
                                if (Arrays.asList(fileType).contains(extension)) {
                                    //判断extension是否在fileType数组中
                                    return true;
                                }
                            }
                            return false;
                        }

                        @Override
                        public String getDescription() {
                            return getDescription_1(fileType);
                        }
                    }
                    );
                }
                int openResult1 = fc1.showOpenDialog(null);
                if (openResult1 == JFileChooser.APPROVE_OPTION) {
                    file1 = fc1.getSelectedFile();
                    System.out.println("58FileChooser:file1=" + file1);
                }
                break;
            case 2:
                JFileChooser fc2 = new JFileChooser(defaultFilePath);
                fc2.setDialogTitle(title);
                fc2.setSelectedFile(new File(defaultFileName));
                if (fileType != null) {
                    fc2.setAcceptAllFileFilterUsed(false);
                    fc2.setFileFilter(new FileNameExtensionFilter(getDescription_1(fileType), fileType));
                }
                int openResult2 = fc2.showSaveDialog(null);
                if (openResult2 == JFileChooser.APPROVE_OPTION) {
                    file1 = fc2.getSelectedFile();
                }
                break;
            case 3:
                JFileChooser fc3 = new JFileChooser(defaultFilePath);
                fc3.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                try {
                    int openResult3 = fc3.showOpenDialog(null);
                    if (openResult3 == JFileChooser.APPROVE_OPTION) {
                        file1 = fc3.getSelectedFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 4:

                break;
            case 5:
                JFileChooser fc5 = new JFileChooser(defaultFilePath);
                fc5.setMultiSelectionEnabled(true);
                if (fileType != null) {
                    fc5.setAcceptAllFileFilterUsed(false);
                    fc5.addChoosableFileFilter(new FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            if (f.isDirectory()) {
                                return true;
                            }
                            String fileName = f.getName();
                            int index = fileName.lastIndexOf('.');
                            if (index > 0 && index < fileName.length() - 1) {
                                String extension = fileName.substring(index + 1).toLowerCase();
                                /*if (extension.equals("csv") || extension.equals("CSV")
                                 || extension.equals("txt") || extension.equals("TXT")
                                 || extension.equals("dat") || extension.equals("DAT")) {
                                 return true;
                                 }*/
                                if (Arrays.asList(fileType).contains(extension)) {
                                    //判断extension是否在fileType数组中
                                    return true;
                                }
                            }
                            return false;
                        }

                        @Override
                        public String getDescription() {
                            return getDescription_1(fileType);
                        }
                    }
                    );
                }
                int openResult5 = fc5.showOpenDialog(null);
                if (openResult5 == JFileChooser.APPROVE_OPTION) {
                    file1 = fc5.getSelectedFile();
                    System.out.println("58FileChooser:file1=" + file1);
                }
                break;
            default:
                System.out.println("文件选择器出错，错误的tab指示值！");
        }
        return file1;
    }

    private String getDescription_1(String[] fileType) {
        //通过过滤类型数组获得描述字符串
        String description = "";
        for (String s : fileType) {
            description = description + "*." + s;
        }
        return description;
    }

    public void setDefaultFileName(String defaultFileName) {
        this.defaultFileName = defaultFileName;
    }

    public void setFileType(String[] fileType) {
        this.fileType = fileType;
    }

    public File getFile() {
        return file;
    }

}
