/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public class JImagePane extends JPanel {

    /**
     * 居中
     */
    public static final String CENTRE = "Centre";
    /**
     * 平铺
     */
    public static final String TILED = "Tiled";
    /**
     * 拉伸
     */
    public static final String SCALED = "Scaled";

    /**
     * 背景图片
     */
    private Image backgroundImage;

    /**
     * 背景图片显示模式
     */
    private String imageDisplayMode;

    /**
     * 背景图片显示模式索引
     */
    private int modeIndex;

    /**
     * 构造一个具有指定背景图片和指定显示模式的JImagePane
     *
     * @param image
     * @param modeName
     */
    public JImagePane(Image image, String modeName) {
        super();
        setBackgroundImage(image);
        setImageDisplayMode(modeName);
    }

    /**
     *
     * @param imageFile
     */
    public JImagePane(String imageFile) {
        super();
        Image image = new ImageIcon(JImagePane.class.getResource(imageFile)).getImage();
        setBackgroundImage(image);
        setImageDisplayMode(CENTRE);
    }

    /**
     *
     * @param imageFile 图片文件名
     * @param modeName 显示模式，模式名称，取值仅限于JImagePane.TILED JImagePane.SCALED
     * JImagePane.CENTRE
     */
    public JImagePane(String imageFile, String modeName) {
        super();
        Image image = new ImageIcon(imageFile).getImage();
        setBackgroundImage(image);
        setImageDisplayMode(CENTRE);
        setImageDisplayMode(modeName);
    }

    /**
     *
     * @param image 图片文件名
     */
    public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
        this.repaint();
    }

    /**
     * 获取背景图片
     *
     * @return 背景图片
     */
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * 设置图片显示模式
     *
     * @param modeName 模式名称，取值仅限于JImagePane.TILED JImagePane.SCALED
     * JImagePane.CENTRE
     */
    public void setImageDisplayMode(String modeName) {
        modeName = modeName.trim();
        if (modeName != null) {
            //居中
            if (modeName.equalsIgnoreCase(CENTRE)) {
                this.imageDisplayMode = CENTRE;
                modeIndex = 0;
            } //平铺
            else if (modeName.equalsIgnoreCase(TILED)) {
                this.imageDisplayMode = TILED;
                modeIndex = 1;
            } //缩放
            else if (modeName.equalsIgnoreCase(SCALED)) {
                this.imageDisplayMode = SCALED;
                modeIndex = 2;
            }

            this.repaint();
        }
    }

    /**
     * 获取背景图片显示模式
     *
     * @return 显示模式
     */
    public String getImageDisplayMode() {
        return imageDisplayMode;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //如果设置了背景图片则显示
        if (backgroundImage != null) {
            int width = this.getWidth();
            int height = this.getHeight();
            int imageWidth = backgroundImage.getWidth(this);
            int imageHeight = backgroundImage.getHeight(this);

            switch (modeIndex) {
                //居中
                case 0: {
                    int x = (width - imageWidth) / 2;
                    int y = (height - imageHeight) / 2;
                    g.drawImage(backgroundImage, x, y, this);
                    break;
                }
                //平铺
                case 1: {
                    for (int ix = 0; ix < width; ix += imageWidth) {
                        for (int iy = 0; iy < height; iy += imageHeight) {
                            g.drawImage(backgroundImage, ix, iy, this);
                        }
                    }
                    break;
                }
                case 2: {
                    g.drawImage(backgroundImage, 0, 0, width, height, this);
                }
            }
        }
    }
}
