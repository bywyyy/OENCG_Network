package com.swu.agentlab.zsnp.gui.game.coalition.voting.cell;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonCell extends DefaultCellEditor implements TableCellRenderer {

    private JButton button;

    public ButtonCell(){
        super(new JCheckBox());
        this.button = new JButton("-");
        this.button.setForeground(Color.lightGray);
        this.button.setFont(new Font("微软雅黑",1,16));
        this.button.setOpaque(false);
        this.button.setBorderPainted(false);
        this.button.setBackground(Color.white);
        this.setClickCountToStart(1);
        this.button.addActionListener(e -> {

        });
    }

    @Override
    public Object getCellEditorValue() {
        return this.button.getText();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if(value == null){
            this.button.setText("-");
        }else{
            String icon = value.toString();
            this.button.setText(icon);
        }
        if("+".equals(this.button.getText())){
            this.button.setForeground(Color.black);
            this.button.setFont(new Font("微软雅黑",1,16));
            this.button.setEnabled(true);
        }else{
            this.button.setForeground(Color.lightGray);
            this.button.setFont(new Font("微软雅黑",1,16));
            this.button.setEnabled(false);
        }
        return this.button;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value == null){
            this.button.setText("-");
        }else{
            String icon = value.toString();
            this.button.setText(icon);
        }
        return this.button;
    }


}
