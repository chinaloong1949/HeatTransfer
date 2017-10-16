/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author yk
 */
public class BrushFactory {

    static final int LINE_BRUSH = 0;
    static final int ELLIPSE_BRUSH = 1;
    static final int RECTANGLE_BRUSH = 2;
    static final int POLYGON_BRUSH = 3;
    static final int ELLIPSE_FILL_BRUSH = 4;
    static final int RECTANGLE_FILL_BRUSH = 5;
    static final Brush NO = new NONE();
    static final Brush LINE = new LineBrush();
    static final Brush ELLIPSE = new EllipseBrush();
    static final Brush ELLIPSE_FILL = new EllipseFillBrush();
    static final Brush RECTANGLE = new RectangleBrush();
    static final Brush RECTANGLE_FILL = new RectangleFillBrush();
    static final Brush POLYGON = new PolygonBrush();

    Brush getBrush(int brushIndex) {
        switch (brushIndex) {
            case LINE_BRUSH:
                return LINE;
            case ELLIPSE_BRUSH:
                return ELLIPSE;
            case ELLIPSE_FILL_BRUSH:
                return ELLIPSE_FILL;
            case RECTANGLE_BRUSH:
                return RECTANGLE;
            case RECTANGLE_FILL_BRUSH:
                return RECTANGLE_FILL;
            case POLYGON_BRUSH:
                return POLYGON;
            default:
                return NO;
        }
    }

    private static final class LineBrush implements Brush {

        @Override
        public void doPaint(Graphics g) {
            Graphics gg = g.create();
            gg.setColor(Color.red);
            gg.drawLine(70, 70, 200, 200);
            gg.dispose();
        }
    }

    private static final class EllipseBrush implements Brush {

        @Override
        public void doPaint(Graphics g) {
            Graphics gg = g.create();
            gg.setColor(Color.red);
            gg.drawOval(100, 100, 200, 50);
            gg.dispose();
        }
    }

    private static final class EllipseFillBrush implements Brush {

        @Override
        public void doPaint(Graphics g) {
            Graphics gg = g.create();
            gg.setColor(Color.red);
            gg.fillOval(100, 100, 200, 50);
            gg.dispose();
        }
    }

    private static final class RectangleBrush implements Brush {

        @Override
        public void doPaint(Graphics g) {
            Graphics gg = g.create();
            gg.setColor(Color.red);
            gg.drawRect(70, 70, 100, 100);
            gg.dispose();
        }
    }

    private static final class RectangleFillBrush implements Brush {

        @Override
        public void doPaint(Graphics g) {
            Graphics gg = g.create();
            gg.setColor(Color.red);
            gg.fillRect(70, 70, 200, 200);
            gg.dispose();
        }
    }

    private static final class PolygonBrush implements Brush {

        @Override
        public void doPaint(Graphics g) {
            Graphics gg = g.create();
            gg.setColor(Color.red);
            gg.drawPolygon(new int[]{48, 50, 244, 483, 310}, new int[]{36, 192, 281, 302, 77}, 5);
            gg.dispose();
        }
    }

    private static final class NONE implements Brush {

        @Override
        public void doPaint(Graphics g) {
            Graphics gg = g.create();
            gg.setColor(Color.red);
            gg.drawString("no brush selected!", 20, 30);
            gg.dispose();
        }
    }

}
