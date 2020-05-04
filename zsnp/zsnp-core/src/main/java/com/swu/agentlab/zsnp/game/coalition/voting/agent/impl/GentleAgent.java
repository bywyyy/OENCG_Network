package com.swu.agentlab.zsnp.game.coalition.voting.agent.impl;

import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Response;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;

import java.util.Map;

public class GentleAgent extends VotingAgent {

    @Override
    public void init(Game game, Infos own, Map<Integer, Infos> opponents) {

    }

    @Override
    public void receiveCommunicate(int proposer, String communicateFree,String communicateType) {

    }

    @Override
    public void receiveProposal(int proposer, Map<Integer, Integer> proposal) {

    }

    @Override
    public void receiveResponse(int proposer, boolean agree) {

    }

    /*@Override
    public Proposal generateProposal() {
        Proposal proposal = generateRandomProposal();
        return proposal;
    }

    @Override
    public Response generateResponse() {
        return generateRandomRespose();
    }*/

    @Override
    public Map<Integer, Integer> propose() {
        return null;
    }

    @Override
    public boolean response() {
        return false;
    }

    @Override
    public Communicate Communicate() {
        return null;
    }
}
