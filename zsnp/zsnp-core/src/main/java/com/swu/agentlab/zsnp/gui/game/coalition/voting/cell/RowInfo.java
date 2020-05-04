package com.swu.agentlab.zsnp.gui.game.coalition.voting.cell;

import lombok.Data;

/**
 * 点击的行信息：
 * 行index，角色名，是否选中
 * @author JJ.Wu
 */
@Data
public class RowInfo {

    private int rowIndex;

    private String partyName;

    private int resource;

    private boolean isSelected;

    private int reward;

    public RowInfo() {
    }

    /**
     *
     * @param rowIndex
     * @param partyName
     * @param isSelected
     */
    public RowInfo(int rowIndex, String partyName, boolean isSelected, int reward) {
        this.rowIndex = rowIndex;
        this.partyName = partyName;
        this.isSelected = isSelected;
        this.reward = reward;
    }
}
