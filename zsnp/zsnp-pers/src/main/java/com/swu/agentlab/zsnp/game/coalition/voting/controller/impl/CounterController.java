package com.swu.agentlab.zsnp.game.coalition.voting.controller.impl;

import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Response;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.controller.BaseController;
import com.swu.agentlab.zsnp.game.coalition.voting.controller.ProposalController;
import com.swu.agentlab.zsnp.game.coalition.voting.controller.ResponseController;
import com.swu.agentlab.zsnp.game.coalition.voting.entity.ally.Ally;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Toallyz;
import com.swu.agentlab.zsnp.game.coalition.voting.service.AllyService;
import com.swu.agentlab.zsnp.game.coalition.voting.service.ProposalService;
import com.swu.agentlab.zsnp.game.coalition.voting.service.ResponseService;
import com.swu.agentlab.zsnp.game.coalition.voting.service.impl.AllyServiceImpl;
import com.swu.agentlab.zsnp.game.coalition.voting.service.impl.ProposalServiceImpl;
import com.swu.agentlab.zsnp.game.coalition.voting.service.impl.ResponseServiceImpl;
import com.swu.agentlab.zsnp.util.IdUtil;
import com.swu.agentlab.zsnp.util.TimeUtil;

public class CounterController implements BaseController<CounterBody>, ProposalController, ResponseController {

    private ProposalService proposalService;

    private AllyService allyService;

    private ResponseService responseService;

    public CounterController() {
        this.proposalService = new ProposalServiceImpl();
        this.allyService = new AllyServiceImpl();
        this.responseService = new ResponseServiceImpl();
    }

    @Override
    public void add(CounterBody counterBody) {
        Offer offer = counterBody.getOffer();
        if(offer instanceof Proposal){
            Proposal proposal = (Proposal) offer;
            String coalitionId = IdUtil.generate16HexId();
            for(PlayerInfo item: proposal.getProposerz().keySet()){
                Ally ally = new Ally();
                ally.setCoalitionId(coalitionId);
                ally.setId(IdUtil.generate16HexId());
                ally.setPlayerId(item.getId());
                ally.setPartyNum(((PartyInfo)item.getRoleInfo()).getPartyNum());
                ally.setReward(proposal.getProposerz().get(item));
                allyService.insert(ally);
            }
            for(PlayerInfo item: proposal.getAlliesz().keySet()){
                Ally ally = new Ally();
                ally.setCoalitionId(coalitionId);
                ally.setId(IdUtil.generate16HexId());
                ally.setPlayerId(item.getId());
                ally.setPartyNum(((PartyInfo)item.getRoleInfo()).getPartyNum());
                Toallyz toallyz = proposal.getAlliesz().get(item);
                ally.setReward(toallyz.getReward());
                ally.setComment(toallyz.getComment() == null?"":String.valueOf(toallyz.getComment()));
                allyService.insert(ally);
            }
            com.swu.agentlab.zsnp.game.coalition.voting.entity.proposal.Proposal proposal1 = new com.swu.agentlab.zsnp.game.coalition.voting.entity.proposal.Proposal();
            proposal1.setId(IdUtil.generate16HexId());
            proposal1.setRoomId(counterBody.getRoomId());
            proposal1.setSessionNum(counterBody.getSessionNum());
            proposal1.setRoundNum(counterBody.getRoundNum());
            proposal1.setCoalitionId(coalitionId);
            proposal1.setTime(TimeUtil.getHms());
            for(PlayerInfo item: proposal.getProposerz().keySet()){
                proposal1.setPlayerId(item.getId());
            }
            proposalService.insert(proposal1);
        }else if(offer instanceof Response){
            Response response = (Response) offer;
            com.swu.agentlab.zsnp.game.coalition.voting.entity.response.Response response1 = new com.swu.agentlab.zsnp.game.coalition.voting.entity.response.Response();
            response1.setId(IdUtil.generate16HexId());
            response1.setRoomId(counterBody.getRoomId());
            response1.setPlayerId(response.getPlayerInfo().getId());
            response1.setSessionNum(counterBody.getSessionNum());
            response1.setRoundNum(counterBody.getRoundNum());
            response1.setAgree(response.isAgree());
            response1.setTime(TimeUtil.getHms());
            responseService.insert(response1);
        }
    }

    @Override
    public CounterBody getById(String id) {
        return null;
    }
}
