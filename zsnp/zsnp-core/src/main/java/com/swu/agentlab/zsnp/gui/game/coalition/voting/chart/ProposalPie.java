package com.swu.agentlab.zsnp.gui.game.coalition.voting.chart;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import org.jfree.chart.ChartPanel;
import org.jfree.data.general.DefaultPieDataset;

import java.util.HashSet;
import java.util.Set;

public class ProposalPie {

    private PieChart chart;

    private DefaultPieDataset dataset;

    private VotingDomain domain;

    private Proposal proposal;

    private GUIBundle guiBundle;

    public ProposalPie(VotingDomain domain){
        this.guiBundle = GUIBundle.getInstance("arena");
        this.domain = domain;
        this.chart = new PieChart(guiBundle.getString("pie_chart_title"));
        this.dataset = chart.getData();
    }

    public ChartPanel getPieChartPanel(){
        return chart.getChartPanel();
    }

    public void setProposal(Proposal proposal){
        this.proposal = proposal;
        Set<String> set = new HashSet<>();
        for(PlayerInfo item: proposal.getProposerz().keySet()){
            this.dataset.setValue(item.getRoleInfo().getRoleName(), proposal.getProposerz().get(item));
            set.add(item.getRoleInfo().getRoleName());
        }
        for(PlayerInfo item: proposal.getAlliesz().keySet()){
            this.dataset.setValue(item.getRoleInfo().getRoleName(), proposal.getAlliesz().get(item).getReward());
            set.add(item.getRoleInfo().getRoleName());
        }
        for(PartyInfo item: domain.getParties()){
            if(!set.contains(item.getRoleName())){
                this.dataset.setValue(item.getRoleName(), 0.0001);
            }
        }
    }

    public static void updateByOffer(ProposalPie pie, Offer offer){
        if(offer instanceof Proposal){
            pie.setProposal((Proposal) offer);
        }
    }
}
