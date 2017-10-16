/*
 * To change this license header, choose License Headers in Project Properties.
 * 这是一个标准绘制二维曲线的类
 * 调用构造方法创建一个该类的实例,传入数据有1，点信息；2，曲线标题，3，曲线名称，4，x轴标题，5，y轴标题
 * 通过调用.getCurvePanel()方法可以得到曲线面板，通过.getCurveFrame()方法可以得到曲线Frame
 */
package Curve;

import point.Point2D;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import static java.lang.Math.max;
import static java.lang.Math.min;
import java.text.NumberFormat;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author yk
 */
public class CurveFrame extends JFrame {

    private JPanel curvePanel;
    private JInternalFrame curveInternalFrame;
    private Point2D[] point = null;
    private int pointNum;
    private int width;
    private int height;
    private double maxX;
    private double minX;
    private double maxY;
    private double minY;
    private String title = "title";
    private String series = "series";
    private String xTitle = "xTitle";
    private String yTitle = "yTitle";

    public CurveFrame(Point2D[] point, String title, String series, String xTitle, String yTitle) {
        if (point != null) {
            this.point = point;
            this.pointNum = point.length;
            maxX = point[0].getX();
            minX = point[0].getX();
            maxY = point[0].getY();
            minY = point[0].getY();
            for (int i = 1; i <= pointNum - 1; i++) {
                maxX = max(maxX, point[i].getX());
                minX = min(minX, point[i].getX());
                maxY = max(maxY, point[i].getY());
                minY = min(minY, point[i].getY());
            }
            this.title = title;
            this.series = series;
            this.xTitle = xTitle;
            this.yTitle = yTitle;

        } else {

        }
    }

    public JPanel getCurvePanel() {
        JPanel jPanel = new JPanel(new BorderLayout());
        if (point != null) {
            DefaultXYDataset lineDataSet = new DefaultXYDataset();
            double[][] data = new double[2][pointNum];
            for (int i = 0; i <= pointNum - 1; i++) {
                data[0][i] = point[i].getX();
                data[1][i] = point[i].getY();
            }

            lineDataSet.addSeries(series, data);

            String[] x = new String[pointNum];

            /*for (int i = 0; i <= pointNum - 1; i++) {
             Number num = -point[i].getY();
             lineDataSet.addValue(num, series, point[i].getX());
             }*/
            //CategoryDataset dataset = DatasetUtilities.createCategoryDataset(yValue, xValue, data);
            StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
            standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));//标题字体
            standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));//图例字体
            standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));//轴向字体
            ChartFactory.setChartTheme(standardChartTheme);
            JFreeChart chart = ChartFactory.createXYLineChart(title,//报表题目
                    xTitle,//x轴标题
                    yTitle,//y轴标题
                    lineDataSet,//数据集
                    //dataset,
                    PlotOrientation.VERTICAL,//图标方向
                    true,//显示图例 
                    true,//不用生成工具
                    false);//不生成URL地址

            XYPlot plot = chart.getXYPlot();

            plot.setBackgroundPaint(Color.white);
            plot.setDomainGridlinesVisible(true);  //设置背景网格线是否可见
            plot.setDomainGridlinePaint(Color.BLACK); //设置背景网格线颜色
            plot.setRangeGridlinePaint(Color.GRAY);
            plot.setNoDataMessage("没有数据");//没有数据时显示的文字说明。 
            // 数据轴属性部分
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            NumberFormat numFormat = NumberFormat.getInstance();
            numFormat.setMaximumFractionDigits(1);
            numFormat.setMaximumIntegerDigits(2);
            //rangeAxis.setTickUnit(new NumberTickUnit(1, numFormat));
            //rangeAxis.setAutoRangeIncludesZero(true); //自动生成
            rangeAxis.setUpperMargin(0.20);
            rangeAxis.setLabelAngle(Math.PI / 2.0);
            rangeAxis.setAutoRange(true);

            //
            ValueAxis categoryAxis = plot.getDomainAxis();
            //categoryAxis.setCategoryLabelPositionOffset(20);
            categoryAxis.setTickMarkInsideLength(0.1f);
            categoryAxis.setAutoRange(true);

            // 数据渲染部分 主要是对折线做操作
            XYItemRenderer renderer = plot.getRenderer();
            renderer.setBaseItemLabelsVisible(false);
            //renderer.setBaseShapesVisible(true);//设置数据点可见
            renderer.setSeriesPaint(0, Color.RED);    //设置折线的颜色
            //renderer.setBaseShapesFilled(true);

            renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
                    ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
            renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
            renderer.setBaseItemLabelFont(new Font("Dialog", 1, 14));  //设置提示折点数据形状
            plot.setRenderer(renderer);

            //区域渲染部分
            double lowpress = -4.5;
            double uperpress = -8;   //设定正常血糖值的范围
            IntervalMarker inter = new IntervalMarker(lowpress, uperpress);
            inter.setLabelOffsetType(LengthAdjustmentType.EXPAND); //  范围调整——扩张
            inter.setPaint(Color.LIGHT_GRAY);// 域顏色  

            inter.setLabelFont(new Font("SansSerif", 41, 14));
            inter.setLabelPaint(Color.RED);
            inter.setLabel("正常血糖值范围");    //设定区域说明文字
            plot.addRangeMarker(inter, Layer.BACKGROUND);  //添加mark到图形   BACKGROUND使得数据折线在区域的前端

            ChartPanel chartPanel = new ChartPanel(chart);
            jPanel.add(chartPanel, BorderLayout.CENTER);
            System.out.println("绘制曲线图完成！");
        } else {
            jPanel.add(new JLabel("没有数据！"));
        }
        return jPanel;
    }

    public JFrame getCurveFrame() {
        this.add(getCurvePanel());
        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        return this;
    }

    public void setSeries(String series) {
        this.series = series;
    }

}
