/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author munna
 */
public class BodyWeightVisualizer extends ApplicationFrame{

    private YIntervalSeriesCollection collection = new YIntervalSeriesCollection();
    
    public BodyWeightVisualizer(String title) {
        super(title);
        RefineryUtilities.centerFrameOnScreen(this);
	JPanel jpanel = createDemoPanel();
        jpanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(jpanel);
        this.pack();
        this.setVisible(true);
    }
    
    private JFreeChart createChart(XYDataset xydataset)
    {
        JFreeChart jfreechart = ChartFactory.createXYLineChart("Body Weight", "Time", "Weight", xydataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();
        xyplot.setDomainZeroBaselineVisible(true);
        xyplot.setRangeZeroBaselineVisible(true);
        xyplot.getDomainAxis().setLowerMargin(0.0D);
        xyplot.getDomainAxis().setUpperMargin(0.0D);
        xyplot.setDomainPannable(true);
        xyplot.setRangePannable(true);
        DeviationRenderer deviationrenderer = new DeviationRenderer(true, false);
        deviationrenderer.setSeriesStroke(0, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesStroke(0, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesFillPaint(0, new Color(255, 200, 200));
        xyplot.setRenderer(deviationrenderer);
//        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)xyplot.getRenderer();
//        xylineandshaperenderer.setLegendLine(new java.awt.geom.Rectangle2D.Double(-4D, -3D, 8D, 6D));
        return jfreechart;
    }
    
    public JPanel createDemoPanel()
    {
        JFreeChart jfreechart = createChart(collection);
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setMouseWheelEnabled(true);
        return chartpanel;
    }
    
    public void addSeries(YIntervalSeries series)
    {
        this.collection.addSeries(series);
        
    }
    
    
    
}
