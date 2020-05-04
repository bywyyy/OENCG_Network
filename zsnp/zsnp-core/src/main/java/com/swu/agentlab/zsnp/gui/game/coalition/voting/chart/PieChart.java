package com.swu.agentlab.zsnp.gui.game.coalition.voting.chart;

import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import lombok.Getter;
import lombok.Setter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PieChart {

    private ChartPanel frame1;

    @Getter@Setter
    private DefaultPieDataset data;

    private String title;

    private GUIBundle guiBundle;

    public PieChart(String title){
        this.guiBundle = GUIBundle.getInstance("arena");
        data = getDataSet();
        JFreeChart chart = ChartFactory.createPieChart3D(title,data,true,false,false);
        //设置百分比
        PiePlot pieplot = (PiePlot) chart.getPlot();
        DecimalFormat df = new DecimalFormat("#");//获得一个DecimalFormat对象，主要是设置小数问题
        NumberFormat nf = NumberFormat.getNumberInstance();//获得一个NumberFormat对象
        StandardPieSectionLabelGenerator sp1 = new StandardPieSectionLabelGenerator("{0}  {1}", nf, df);//获得StandardPieSectionLabelGenerator对象
        pieplot.setLabelGenerator(sp1);//设置饼图显示百分比

        //没有数据的时候显示的内容
        pieplot.setNoDataMessage(guiBundle.getString("chart_no_data_hint"));
        pieplot.setCircular(false);
        pieplot.setLabelGap(0.02D);

        pieplot.setIgnoreNullValues(true);//设置不显示空值
        pieplot.setIgnoreZeroValues(true);//设置不显示负值
        frame1=new ChartPanel (chart,true);
        frame1.setPreferredSize(new Dimension(270, 205));
        //frame1.setBorder(BorderFactory.createLineBorder(Color.black));
        chart.getTitle().setFont(new Font("微软雅黑",Font.PLAIN,12));//设置标题字体
        PiePlot piePlot= (PiePlot) chart.getPlot();//获取图表区域对象
        piePlot.setLabelFont(new Font("微软雅黑",Font.PLAIN,10));//解决乱码
        chart.getLegend().setItemFont(new Font("微软雅黑",Font.PLAIN,10));
    }
    private DefaultPieDataset getDataSet() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        /*dataset.setValue("苹果",100);
        dataset.setValue("梨子",200);
        dataset.setValue("葡萄",300);
        dataset.setValue("香蕉",400);
        dataset.setValue("荔枝",0.0001);*/
        return dataset;
    }

    public ChartPanel getChartPanel(){
        return frame1;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Panel panel = new Panel();
        PieChart chart = new PieChart("Coalition Proposal");
        //panel.add(chart.getChartPanel());
        frame.setContentPane(chart.getChartPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        //frame.setBounds(0, 0, 300, 200);
        frame.setVisible(true);
        chart.data.setValue("苹果",100);
        chart.data.setValue("梨子",200);
        chart.data.setValue("葡萄",300);
        chart.data.setValue("香蕉",400);
        chart.data.setValue("荔枝",0.0001);
    }
}
