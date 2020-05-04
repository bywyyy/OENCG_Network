package com.swu.agentlab.zsnp.gui.game.coalition.voting.cell;

import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * @author JJ.Wu
 */
@Data
public class SliderFieldPanel extends JPanel {

    private JSlider slider;

    private JLabel lbl_minValue;

    private JLabel lbl_maxValue;

    private JTextField field;

    private int maxValue;

    private int value;

    public SliderFieldPanel(JSlider slider, JTextField field) {
        this.lbl_minValue = new JLabel("0");
        this.lbl_minValue.setForeground(Color.gray);
        this.slider = slider;
        this.lbl_maxValue = new JLabel("0");
        this.lbl_maxValue.setForeground(Color.gray);
        this.field = field;
        this.slider.setPreferredSize(new Dimension(95, 28));
        this.slider.setOpaque(false);
        this.slider.setForeground(Color.gray);
        this.slider.addChangeListener(e -> {
            this.value = this.slider.getValue();
            try{
                this.field.setText(this.value+"");
            }catch (IllegalStateException e1){
                //e1.printStackTrace();
            }
        });
        this.field.setPreferredSize(new Dimension(40,20));
        this.field.setOpaque(true);
        this.field.setBorder(BorderFactory.createLineBorder(Color.gray));
        this.field.addCaretListener(e -> {
            String str = this.field.getText();
            if(!"".equals(str)&&isInteger(str)){
                if(inRange(Integer.parseInt(str))){
                    value = Integer.parseInt(str);
                    this.slider.setValue(value);
                }else{
                }
            }
        });
        FlowLayout layout = new FlowLayout();
        layout.setHgap(5);
        layout.setVgap(0);
        this.setLayout(layout);
        this.add(lbl_minValue);
        this.add(slider);
        this.add(lbl_maxValue);
        this.add(field);
        this.setPreferredSize(new Dimension(200,28));
        this.setForeground(Color.gray);
        this.setOpaque(false);
        this.setMaxValue(0);
        this.setEnable(false);
    }

    public void setValue(Rewards rewards){
        this.maxValue = rewards.maxRewards;
        this.slider.setMaximum(this.maxValue);
        this.lbl_maxValue.setText(maxValue+"");
        value = rewards.thisReward;
        this.slider.setValue(value);
        this.field.setText(value+"");
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int value){
        this.value = value;
        this.slider.setValue(value);
    }

    public void setEnable(boolean enable){
        this.slider.setEnabled(enable);
        this.field.setEnabled(enable);
        if(enable){
            this.lbl_minValue.setForeground(Color.black);
            this.lbl_maxValue.setForeground(Color.black);
            this.field.setBorder(BorderFactory.createLineBorder(Color.black));
            this.field.setForeground(Color.black);
        }else{
            this.lbl_minValue.setForeground(Color.gray);
            this.lbl_maxValue.setForeground(Color.gray);
            this.field.setBorder(BorderFactory.createLineBorder(Color.gray));
            this.field.setForeground(Color.gray);
        }
    }

    private boolean isInteger(String str){
        char[] chars = str.toCharArray();
        for(Character item: chars){
            if(!Character.isDigit(item)){
                return false;
            }
        }
        return true;
    }

    private boolean inRange(int num){
        return num>=0&&num<=maxValue;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setContentPane(new SliderFieldPanel(new JSlider(), new JTextField()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
