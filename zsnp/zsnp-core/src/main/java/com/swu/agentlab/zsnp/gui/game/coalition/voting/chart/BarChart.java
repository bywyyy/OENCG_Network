package com.swu.agentlab.zsnp.gui.game.coalition.voting.chart;

import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import lombok.Setter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import java.awt.*;

public class BarChart {

    private ChartPanel frame1;

    private DefaultCategoryDataset dataset;

    public DefaultCategoryDataset getDataset() {
        return dataset;
    }

    private GUIBundle guiBundle;

    public  BarChart(String title, String xLabel, String yLabel){
        this.guiBundle = GUIBundle.getInstance("arena");
        this.dataset = getDataSet();
        JFreeChart chart = ChartFactory.createBarChart3D(
                title, // 图表标题
                xLabel, // 目录轴的显示标签
                yLabel, // 数值轴的显示标签
                dataset, // 数据集
                PlotOrientation.VERTICAL, // 图表方向：水平、垂直
                false,           // 是否显示图例(对于简单的柱状图必须是false)
                false,          // 是否生成工具
                false           // 是否生成URL链接
        );
        //从这里开始
        CategoryPlot plot=chart.getCategoryPlot();//获取图表区域对象
        plot.setNoDataMessage(guiBundle.getString("chart_no_data_hint"));
        CategoryAxis domainAxis=plot.getDomainAxis();         //水平底部列表
        domainAxis.setLabelFont(new Font("微软雅黑",Font.PLAIN,10));         //水平底部标题
        domainAxis.setTickLabelFont(new Font("微软雅黑",Font.PLAIN,10));  //垂直标题
        domainAxis.setUpperMargin(0.10);
        domainAxis.setLowerMargin(0.10);
        /*plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);*/
        ValueAxis rangeAxis=plot.getRangeAxis();//获取柱状
        rangeAxis.setLabelFont(new Font("微软雅黑",Font.PLAIN,10));
        //chart.getLegend().setItemFont(new Font("微软雅黑", Font.PLAIN, 10));
        chart.getTitle().setFont(new Font("微软雅黑",Font.PLAIN,12));//设置标题字体
        //CategoryItemRenderer renderer = plot.getRenderer();
        BarRenderer3D renderer = new BarRenderer3D();
        renderer.setBaseItemLabelGenerator( new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelFont(new Font("微软雅黑", Font.PLAIN, 10));
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));
        renderer.setBaseNegativeItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));
        renderer.setItemLabelAnchorOffset(10);
        //renderer.setBaseOutlinePaint(Color.orange);
        renderer.setDrawBarOutline(true);
        renderer.setSeriesPaint(0, Color.ORANGE);
        plot.setRenderer(renderer);
        //renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));
        //到这里结束，虽然代码有点多，但只为一个目的，解决汉字乱码问题

        frame1=new ChartPanel(chart,true);        //这里也可以用chartFrame,可以直接生成一个独立的Frame
        frame1.setPreferredSize(new Dimension(270, 205));
    }
    private DefaultCategoryDataset getDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        //CategoryDataset dataset = new DefaultCategoryDataset();
        /*dataset.addValue(-10, "北京", "苹果");
        dataset.addValue(100, "北京", "梨子");
        dataset.addValue(0, "北京", "葡萄");*/
        //dataset.clear();
        /*dataset.addValue(200, "北京", "梨子");
        dataset.addValue(200, "上海", "梨子");
        dataset.addValue(200, "广州", "梨子");
        dataset.addValue(300, "北京", "葡萄");
        dataset.addValue(300, "上海", "葡萄");
        dataset.addValue(300, "广州", "葡萄");
        dataset.addValue(400, "北京", "香蕉");
        dataset.addValue(400, "上海", "香蕉");
        dataset.addValue(400, "广州", "香蕉");
        dataset.addValue(500, "北京", "荔枝");
        dataset.addValue(500, "上海", "荔枝");
        dataset.addValue(500, "广州", "荔枝");
        dataset.addValue(600, "北京", "鸭梨");
        dataset.addValue(600, "上海", "鸭梨");
        dataset.addValue(600, "广州", "鸭梨");*/
        return dataset;
    }
    public ChartPanel getChartPanel(){
        return frame1;
    }

    public void setXLabel(String label){
        frame1.getChart().getCategoryPlot().getDomainAxis().setLabel(label);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        BarChart barChart = new BarChart("Fruit", "Category", "Amount");
        barChart.setXLabel("Hello");
        //barChart.getChartPanel().getChart().getCategoryPlot().getDomainAxis().setLabel("HELLO");
        frame.setContentPane(barChart.getChartPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
