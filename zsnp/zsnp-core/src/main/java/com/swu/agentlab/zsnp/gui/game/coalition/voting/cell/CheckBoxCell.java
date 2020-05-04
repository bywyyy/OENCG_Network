package com.swu.agentlab.zsnp.gui.game.coalition.voting.cell;

import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;

public class CheckBoxCell extends DefaultCellEditor implements TableCellRenderer {

    private final Logger log = Logger.getLogger(CheckBoxCell.class);

    private JCheckBox checkBox;

    private CheckBoxCellHandler cellHandler;

    private RowInfo rowInfo;

    public CheckBoxCell(CheckBoxCellHandler cellHandler){
        super(new JCheckBox());
        this.checkBox = (JCheckBox) this.getComponent();
        this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        this.checkBox.setBackground(Color.white);
        this.cellHandler = cellHandler;
        this.rowInfo = new RowInfo();
        this.checkBox.addActionListener(e -> {
            rowInfo.setSelected(checkBox.isSelected());
            cellHandler.handleRowInfo(rowInfo);
            //log.info(checkBox.isSelected());
        });
    }

    /**
     * 每次点击，该单元格，就会调用它
     * @param table
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @return
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if(value == null){
            //this.checkBox.setSelected(true);
            //this.checkBox.setEnabled(false);
            return null;
        }else{
            Check check = (Check) value;
            this.checkBox.setEnabled(check.enable);
            this.checkBox.setSelected(check.checked);
        }
        //获取操作行的信息 RowInfo
        String partyName = (String)table.getValueAt(row, 0);
        int resource = Integer.parseInt(table.getValueAt(row,2).toString().trim());
        Rewards reward = (Rewards) table.getValueAt(row, 4);
        rowInfo.setRowIndex(row);
        rowInfo.setPartyName(partyName);
        rowInfo.setResource(resource);
        rowInfo.setReward(reward.thisReward);
        return this.checkBox;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.checkBox.setBackground(Color.white);
        if(value == null){
            //this.checkBox.setSelected(true);
            //this.checkBox.setEnabled(false);
            return null;
        }else{
            Check check = (Check) value;
            this.checkBox.setEnabled(check.enable);
            this.checkBox.setSelected(check.checked);
        }
        return this.checkBox;
    }

    @Override
    public Object getCellEditorValue() {
        return new Check(this.checkBox.isEnabled(), this.checkBox.isSelected());
    }

    /**
     * enable, checked
     */
    public static class Check{
        /**
         * 是否可以被更改选中状态
         */
       public boolean enable;

        /**
         * 是否被选中
         */
       public boolean checked;

       public Check(boolean enable, boolean checked){
           this.enable = enable;
           this.checked = checked;
       }
    }
}
