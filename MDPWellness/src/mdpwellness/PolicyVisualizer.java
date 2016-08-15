/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdpwellness;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.Range;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author munna
 */
public class PolicyVisualizer extends ApplicationFrame{
    
    DefaultCategoryDataset nutritionDataSet = new DefaultCategoryDataset();
    DefaultCategoryDataset exerciseDataSet  = new DefaultCategoryDataset();

    public PolicyVisualizer(String title) {
        super(title);
        
    }
    
    public void addData(Map<Integer,String>  policy)
    {
        JPanel jpanel = createDemoPanel(policy);
        jpanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(jpanel);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
    }
    
    private JFreeChart createChart()
    {
        NumberAxis nutritionAxis = new NumberAxis("Calories/Day");
        nutritionAxis.setLowerBound(0);
        nutritionAxis.setUpperBound(ActionSet.maxCalories);
        nutritionAxis.setAutoRange(false);
		
		CategoryPlot categoryplot = new CategoryPlot(   nutritionDataSet, 
                                                                new CategoryAxis("Weight(KG)"),
                                                                nutritionAxis,
                                                                new BarRenderer()
                                                            );
                CategoryAxis xaxis = categoryplot.getDomainAxis();
                xaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
                
                LegendItemCollection legend = new LegendItemCollection();
                LegendItem nutritionLegend = new LegendItem("Calories/Day");
                LegendItem exerciseLegend  = new LegendItem("Physical Activity Level");
                nutritionLegend.setFillPaint(Color.BLUE);
                exerciseLegend.setFillPaint(Color.RED);
                
                legend.add(nutritionLegend);
                legend.add(exerciseLegend);
                
                categoryplot.setFixedLegendItems(legend);
                JFreeChart jfreechart = new JFreeChart("Policy Visualizer",categoryplot);
                categoryplot.setDataset(1,exerciseDataSet);
                categoryplot.mapDatasetToRangeAxis(1,1);
                 
                NumberAxis exerciseAxis  = new NumberAxis("Physical Activity Level");
                exerciseAxis.setLowerBound(0);
                exerciseAxis.setUpperBound(ActionSet.maxPA);
                exerciseAxis.setAutoRange(false);
                
                categoryplot.setRangeAxis(1, exerciseAxis);
                BarRenderer barrenderer1 = new BarRenderer();
		categoryplot.setRenderer(1, barrenderer1);
                
               
               
//		BarRenderer stackedbarrenderer = (BarRenderer)categoryplot.getRenderer();
//		stackedbarrenderer.setDrawBarOutline(true);
//		stackedbarrenderer.setBaseItemLabelsVisible(false);
//		stackedbarrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		
                ChartUtilities.applyCurrentTheme(jfreechart);
                
                return jfreechart;
                
                
    }
    
    private void createDataset(Map<Integer,String> policy)
	{
            for(int weight = (int)BodyParams.initialWeight ; weight > (int) BodyParams.targetWeight; weight -- )
            {
                String actionName = policy.get(weight);
                if(actionName == null)
                {
                    continue;
                }
                 String tokens[] = actionName.split("-");
                double calories = Double.valueOf(tokens[0]);
                double pa = Double.valueOf(tokens[1]);

//                calories = calories/ActionSet.maxCalories;
//                pa       = pa/ActionSet.maxPA;
//                
                nutritionDataSet.addValue(calories, "Nutrition", ""+ weight);
                nutritionDataSet.addValue(null,"Dummy 1",""+weight);
                exerciseDataSet.addValue(null,"Dummy 2",""+weight);
                exerciseDataSet.addValue(pa, "PA", ""+weight);
                
                
            }
	}
    
    public JPanel createDemoPanel(Map<Integer,String>  policy)
    {
        createDataset(policy);
        JFreeChart jfreechart = createChart();
        return new ChartPanel(jfreechart);
    }
    
}
