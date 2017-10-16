package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yk
 */
public class DrawPoint extends JFrame {

    private final Dimension size = new Dimension(600, 400);
    private final Action lineAction = new LineAction();
    private final Action ellipseAction = new EllipseAction();
    private final Action ellipseFillAction = new EllipseFillAction();
    private final Action rectangleAction = new RectangleAction();
    private final Action rectangleFillAction = new RectangleFillAction();
    private final Action polygonAction = new PolygonAction();
    private static final BrushFactory brushFactory = new BrushFactory();
    private static Brush brush;

    public DrawPoint() {
        this.setTitle("画线");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(500, 400);
        draw();
    }

    public final static JComponent canvas = new JComponent() {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (brush != null) {
                brush.doPaint(g);
            }
        }
    };

    void draw() {
        attachListener();
        doLay();
    }

    private void attachListener() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void doLay() {
        Container container = this.getContentPane();
        container.add(canvas, BorderLayout.CENTER);
        JPanel buttonsPane = new JPanel(new GridLayout(2, 3));
        buttonsPane.add(new JButton(lineAction));
        buttonsPane.add(new JButton(ellipseAction));
        buttonsPane.add(new JButton(rectangleAction));
        buttonsPane.add(new JButton(polygonAction));
        buttonsPane.add(Box.createHorizontalBox());
        buttonsPane.add(new JButton(ellipseFillAction));
        buttonsPane.add(new JButton(rectangleFillAction));
        container.add(buttonsPane, BorderLayout.SOUTH);
        this.pack();
    }

    private static final class RectangleAction extends AbstractAction {

        private RectangleAction() {
            super("空心矩形");
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            brush = brushFactory.getBrush(BrushFactory.RECTANGLE_BRUSH);
            canvas.repaint();
        }
    }

    private static final class RectangleFillAction extends AbstractAction {

        private RectangleFillAction() {
            super("实心矩形");
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            brush = brushFactory.getBrush(BrushFactory.RECTANGLE_FILL_BRUSH);
            canvas.repaint();
        }
    }

    private static final class EllipseAction extends AbstractAction {

        private EllipseAction() {
            super("空心椭圆");
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            brush = brushFactory.getBrush(BrushFactory.ELLIPSE_BRUSH);
            canvas.repaint();
        }
    }

    private static final class EllipseFillAction extends AbstractAction {

        private EllipseFillAction() {
            super("实心椭圆");
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            brush = brushFactory.getBrush(BrushFactory.ELLIPSE_FILL_BRUSH);
            canvas.repaint();
        }
    }

    private static final class PolygonAction extends AbstractAction {

        private PolygonAction() {
            super("多边形");
        }

        public void actionPerformed(ActionEvent e) {
            brush = brushFactory.getBrush(BrushFactory.POLYGON_BRUSH);
            canvas.repaint();
        }
    }

    private static final class LineAction extends AbstractAction {

        private LineAction() {
            super("直线");
        }

        public void actionPerformed(ActionEvent e) {
            brush = brushFactory.getBrush(BrushFactory.LINE_BRUSH);
            canvas.repaint();
        }
    }

}
