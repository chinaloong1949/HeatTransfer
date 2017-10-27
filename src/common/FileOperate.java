/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class FileOperate {

    File file;

    /**
     * 仅仅写入文件需要传入fileName,从文件读取可以从方法传入文件或文件名，此时构造函数传入的fileName会被覆盖
     *
     * @param fileName
     */
    public FileOperate(String fileName) {
        file = new File(fileName);

    }

    /**
     *
     * @param file 仅仅写入文件需要传入fileName,从文件读取可以从方法传入文件或文件名，此时构造函数传入的file会被覆盖
     */
    public FileOperate(File file) {
        this.file = file;
    }

    public FileOperate() {
    }

    public void writeToFileString(String content, boolean append) {
        BufferedWriter out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append)));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean writeToFileStrings(String content[], boolean append) {
        BufferedWriter out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append)));
            for (String line : content) {
                out.write(line);
                out.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void writeToFileInt(int content, boolean append) {
        BufferedWriter out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append)));
            out.write(String.valueOf(content));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToFileDouble(double content, boolean append) {
        ObjectOutputStream oos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            oos = new ObjectOutputStream(new FileOutputStream(file, append));
            oos.writeDouble(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToFileFloat(float content, boolean append) {
        ObjectOutputStream oos = null;
        try {
            if (file.exists()) {
                file.createNewFile();
            }
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            oos = new ObjectOutputStream(new FileOutputStream(file, append));
            oos.writeFloat(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public double[][] readFromFileDoubleArray2(File file) {
        double[][] data;
        String[] lines = readFromFileStringArray(file);
        int rows = lines.length;
        lines[0] = lines[0].trim();
        int columns = lines[0].split("\\s{1,}").length;
        data = new double[rows][columns];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
            String[] lineEle = lines[i].split("\\s{1,}");
            for (int j = 0; j < lineEle.length; j++) {
                data[i][j] = Double.parseDouble(lineEle[j]);
            }
        }
        return data;
    }

    /**
     * 从文件中读入一行数据，作为字符串返回
     *
     * @param file
     * @return
     */
    public String readFromFileString(File file) {
        //从文件中读入一行数据，作为字符串返回
        BufferedReader reader = null;
        String content = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            content = reader.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 读取所有文件内容到一个字符串中，并返回
     *
     * @param file
     * @return
     */
    public String readFromFileStringWhole(File file) {
        //读取所有文件内容到一个字符串中，并返回
        BufferedReader reader = null;
        String content = "";
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                content = content + line;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 读入字符串后会丢掉文件中的换行符
     *
     * @param file
     * @return
     */
    public String[] readFromFileStringArray(File file) {
        BufferedReader reader = null;
        ArrayList<String> content = new ArrayList<>();
        try {
            String line;
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((line = reader.readLine()) != null) {
                content.add(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] result = new String[content.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = content.get(i);
        }
        return result;
    }

    public static void copy(File file, File toFile) {
        byte[] b = new byte[1024];
        int a;
        FileInputStream fis;
        FileOutputStream fos;
        try {
            if (file.isDirectory()) {
                String filepath = file.getAbsolutePath();
                filepath = filepath.replace("\\\\", "/");
                String toFilepath = toFile.getAbsolutePath();
                toFilepath = toFilepath.replace("\\\\", "/");
                int lastIndexOf = filepath.lastIndexOf("/");
                toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());
                File copy = new File(toFilepath);
                //复制文件夹
                if (!copy.exists()) {
                    copy.mkdir();
                }
                //遍历文件夹
                for (File f : file.listFiles()) {
                    copy(f, copy);
                }

            } else {
                if (toFile.isDirectory()) {
                    String filepath = file.getAbsolutePath();
                    filepath = filepath.replaceAll("\\\\", "/");
                    String toFilepath = toFile.getAbsolutePath();
                    toFilepath = toFilepath.replaceAll("\\\\", "/");
                    int lastIndexOf = filepath.lastIndexOf("/");
                    toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());

                    //写文件
                    File newFile = new File(toFilepath);
                    if (newFile.exists()) {
                        newFile.delete();
                        newFile.createNewFile();
                    }
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(newFile);
                    while ((a = fis.read(b)) != -1) {
                        fos.write(b, 0, a);
                    }
                    fos.close();
                    fis.close();
                } else {//两个文件都不是文件夹，就拷贝第一个文件的内容到第二个文件的内容
                    if (toFile.exists()) {
                        toFile.delete();
                    }
                    copy(file, toFile.getParentFile());
                    new File(toFile.getParent(), file.getName()).renameTo(toFile);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 在文件中查找包含给定字符串的行
     *
     * @param findingStr 查找的字符串
     * @return 返回包含查找字符串所在的行
     */
    public String[] findString(String findingStr) {
        String line = "";
        BufferedReader reader = null;
        ArrayList<String> content = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((line = reader.readLine()) != null) {
                if (line.contains(findingStr)) {
                    content.add(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] result = new String[content.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = content.get(i);
        }
        return result;
    }

}
