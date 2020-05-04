package com.swu.agentlab.zsnp.gui.game.coalition.voting.cell;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class SliderFieldCell extends DefaultCellEditor implements TableCellRenderer {

    private final Logger log = Logger.getLogger(SliderFieldPanel.class);

    private SliderFieldPanel sliderField;

    private SliderFieldCellHandler sliderFieldCellHandler;

    private RowInfo rowInfo;

    public SliderFieldCell(SliderFieldCellHandler sliderFieldCellHandler){
        super(new JTextField());
        JTextField field = (JTextField) this.getComponent();
        JSlider slider = new JSlider();
        this.sliderField = new SliderFieldPanel(slider, field);
        this.setClickCountToStart(1);
        this.sliderFieldCellHandler = sliderFieldCellHandler;
        rowInfo = new RowInfo();
        slider.addChangeListener(e -> {
            if(rowInfo.getPartyName() == null){
                return;
            }
            rowInfo.setReward(this.sliderField.getValue());
            sliderFieldCellHandler.rewardCellChanged(rowInfo);
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //获取操作行的信息 RowInfo
        String partyName = (String)table.getValueAt(row, 0);
        rowInfo.setPartyName(partyName);
        rowInfo.setRowIndex(row);
        if(value == null){
            return this.sliderField;
        }else{
            Rewards rewards = (Rewards) value;
            this.sliderField.setValue(rewards);
            if(rewards.maxRewards>0){
                this.sliderField.setEnable(true);
            }else{
                this.sliderField.setEnable(false);
            }
            return this.sliderField;
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value == null){
            return this.sliderField;
        }else{
            Rewards rewards = (Rewards) value;
            this.sliderField.setValue(rewards);
            return this.sliderField;
        }
    }

    @Override
    public Object getCellEditorValue() {
        Rewards rewards = new Rewards(this.sliderField.getMaxValue(), this.sliderField.getValue());
        return rewards;
    }

    public void setMaxValue(int maxValue){
        sliderField.setMaxValue(maxValue);
        sliderField.setEnable(true);
    }


}
