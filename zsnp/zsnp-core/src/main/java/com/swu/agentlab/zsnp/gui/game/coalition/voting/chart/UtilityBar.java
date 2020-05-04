package com.swu.agentlab.zsnp.gui.game.coalition.voting.chart;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.List;

public class UtilityBar {

    private BarChart barChart;

    private DefaultCategoryDataset dataset;

    private VotingDomain votingDomain;

    private GUIBundle guiBundle;

    public UtilityBar(VotingDomain votingDomain) {
        this.guiBundle = GUIBundle.getInstance("arena");
        this.votingDomain = votingDomain;
        this.barChart = new BarChart(guiBundle.getString("bar_chart_title"),
                guiBundle.getString("bar_chart_xAixs"),
                guiBundle.getString("bar_chart_yAixs"));
        this.dataset = this.barChart.getDataset();
    }

    public ChartPanel getBarChartPanel() {
        return barChart.getChartPanel();
    }

    public void setProposal(Proposal proposal){
        dataset.clear();
        List<String> parties = new ArrayList<>();
        String proposer = "";
        for(PlayerInfo item: proposal.getProposerz().keySet()){
            proposer = item.getRoleInfo().getRoleName();
            barChart.setXLabel("Proposer: "+proposer);
            PartyInfo partyInfo = (PartyInfo) item.getRoleInfo();
            int reward = proposal.getProposerz().get(item);
            Double talent = votingDomain.getPartyByNum(partyInfo.getPartyNum()).getTalent();
            if(talent != 0.0){
                double discountedU = reward * Math.pow(votingDomain.getDiscountFactor(), ((double)(proposal.getSessionRoundNum())/votingDomain.getMaxRound())) + talent;
                dataset.addValue(discountedU, proposer, proposer);
            }
            parties.add(proposer);
        }
        for(PlayerInfo item: proposal.getAlliesz().keySet()){
            PartyInfo partyInfo = (PartyInfo) item.getRoleInfo();
            int reward = proposal.getAlliesz().get(item).getReward();
            Double talent = votingDomain.getPartyByNum(partyInfo.getPartyNum()).getTalent();
            if(talent != 0.0){
                double discountedU = reward * Math.pow(votingDomain.getDiscountFactor(), ((double)(proposal.getSessionRoundNum())/votingDomain.getMaxRound())) + talent;
                dataset.addValue(discountedU, proposer, item.getRoleInfo().getRoleName());
            }
            parties.add(partyInfo.getRoleName());
        }
        for(PartyInfo item: votingDomain.getParties()){
            if(!parties.contains(item.getRoleName())){
                dataset.addValue(0, proposer, item.getRoleName());
            }
        }
    }

    public static void updateByOffer(UtilityBar utilityBar, Offer offer){
        if(offer instanceof Proposal){
            utilityBar.setProposal((Proposal) offer);
        }
    }
}
