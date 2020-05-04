package com.swu.agentlab.zsnp.game.coalition.voting.agent;

import com.swu.agentlab.zsnp.entity.communicator.Communicator;
import com.swu.agentlab.zsnp.entity.communicator.Sendable;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.Agent;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Proposal;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Response;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.SessionBegin;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Toallyz;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;

import java.util.*;

public abstract class VotingAgent extends Agent {

    private Offer lastReceiveOffer;

    private Offer myOffer;

    private Communicate communicate;

    private VotingDomain votingDomain;
    @Getter
    private PlayerInfo playerInfo;

    private PartyInfo partyInfo;

    private double talent;
    @Getter
    private Set<PlayerInfo> playerInfos;

    /**
     * 新加的语言模块
     */


    protected int roundNum;         //当前轮数


    //public VotingAgent(){}

    /*public VotingAgent(String roomId, Sendable sender, VotingDomain votingDomain, PlayerInfo playerInfo, Set<PlayerInfo> playerInfos) {
        super(roomId, sender);
        this.votingDomain = votingDomain;
        this.playerInfo = playerInfo;
        this.talent = ((PartyInfo) playerInfo.getRoleInfo()).getTalent();
        ((PartyInfo) playerInfo.getRoleInfo()).setTalent(0.0);
        this.playerInfos = playerInfos;
    }*/

    public final void init(String roomId, Sendable sender, VotingDomain votingDomain, PlayerInfo playerInfo, Set<PlayerInfo> playerInfos) {
        super.init(roomId, sender);
        this.votingDomain = votingDomain;
        this.playerInfo = playerInfo;
        this.talent = ((PartyInfo) playerInfo.getRoleInfo()).getTalent();
        ((PartyInfo) playerInfo.getRoleInfo()).setTalent(0.0);
        this.playerInfos = playerInfos;

        this.partyInfo = (PartyInfo) this.playerInfo.getRoleInfo();
        int majority = votingDomain.getMajority();
        Set<Coalition> coalitions = votingDomain.getCoalitions();
        Infos own = new Infos(partyInfo.getPartyNum(), partyInfo.getResource(), this.talent);
        Game game = new Game(votingDomain.getAmountRoles(), majority, votingDomain.getMaxRound(), votingDomain.getDiscountFactor(), coalitions);
        Map<Integer, Infos> opponents = new HashMap<>();
        for(PartyInfo item: votingDomain.getParties()){
            if(item.getPartyNum()!=partyInfo.getPartyNum()){
                Infos infos = new Infos(item.getPartyNum(), item.getResource(), item.getTalent());
                opponents.put(item.getPartyNum(), infos);
            }
        }
        this.init(game, own, opponents);
    }

    /**
     * 接收初始的一些消息，包括：自己的信息、对手的信息
     * @param game 游戏的信息（公共信息）
     * @param own 自己的信息
     * @param opponents 对手的信息
     */
    public abstract void init(Game game, Infos own, Map<Integer, Infos> opponents);

    @Override
    public final void receiveOffer(Offer offer) {
        this.lastReceiveOffer = offer;

        if(offer instanceof Proposal){
            Proposal proposal = (Proposal) offer;
            int proposer = 0;
            Map<Integer, Integer> proposalOffer = new HashMap<>();
            for(PlayerInfo item: proposal.getProposerz().keySet()){
                proposer = ((PartyInfo)item.getRoleInfo()).getPartyNum();
                proposalOffer.put(proposer, proposal.getProposerz().get(item));
            }
            for(PlayerInfo item: proposal.getAlliesz().keySet()){
                proposalOffer.put(((PartyInfo)item.getRoleInfo()).getPartyNum(), proposal.getAlliesz().get(item).getReward());
            }
            this.roundNum = proposal.getSessionRoundNum();
            receiveProposal(proposer, proposalOffer);
        }else if(offer instanceof Response){
            Response response = (Response) offer;
            int responser = ((PartyInfo)response.getPlayerInfo().getRoleInfo()).getPartyNum();
            boolean agree = response.isAgree();
            receiveResponse(responser, agree);
        }else if(offer instanceof SessionBegin){
            this.roundNum = 1;
        }else if(offer instanceof Communicate){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Communicate communicate = (Communicate)offer;
            PartyInfo partyInfo = (PartyInfo) communicate.getPlayerInfo().getRoleInfo();
            receiveCommunicate(partyInfo.getPartyNum(),communicate.getCommunicateFree(),communicate.getCommunicateType());
        }
    }



    /**
     *  接收玩家发送的语言（也会收到自己提出的语言）
     * @param proposer 发送语言的玩家的编号
     * @param communicateFree 发送的语句信息
     * @param communicateType
     */
    public abstract void receiveCommunicate(int proposer,String communicateFree,String communicateType);

    /**
     * 接收玩家发送的提议（也会收到自己提出的提议）
     * 每当有玩家发送联盟提议，都可以通过这个方法获取提议
     * @param proposer 发送提议的玩家的编号
     * @param proposal 提议的内容，包括联盟中每一方的利益分配
     */
    public abstract void receiveProposal(int proposer, Map<Integer, Integer> proposal);

    /**
     * 接收玩家发送的响应（也会收到自己提出的响应）
     * @param responder 发送响应的玩家编号
     * @param agree 是否同意进入该联盟
     */
    public abstract void receiveResponse(int responder, boolean agree);

    @Override
    protected final Offer generateOffer() {
        return myOffer;
    }


    public final void setRole(type type){
        switch (type){
            case PROPOSER:
                myOffer = this.generateProposal();
                communicate = this.Communicate();
                break;
            case RESPONSER:
                myOffer = this.generateResponse();
                break;
            default:
                break;
        }
    }

    @Override
    protected final Communicate generateCommunicate() {
//        生成
        communicate =this.Communicate();
        return communicate;
    }

    private final Proposal generateProposal(){
        Map<Integer, Integer> map = this.propose();
        if(!map.containsKey(partyInfo.getPartyNum())){
            throw new InvalidCoalitionException("联盟结构中必须包含自己");
        }
        int sum = 0;
        for(Integer item: map.values()){
            sum+=item;
        }
        int max = this.getRewards(map.keySet());
        if(sum>max){
            throw new InvalidCoalitionException("收益总和大于合法值");
        }
        Proposal proposal = new Proposal();
        proposal.setProposerz(new HashMap<PlayerInfo, Integer>());
        proposal.setAlliesz(new HashMap<PlayerInfo, Toallyz>());
        for(Integer item: map.keySet()){
            if(item == partyInfo.getPartyNum()){
                Map<PlayerInfo, Integer> map1 = proposal.getProposerz();
                map1.put(this.playerInfo, map.get(item));
                proposal.setProposerz(map1);
            }else{
                Map<PlayerInfo, Toallyz> map1 = proposal.getAlliesz();
                Toallyz toallyz = new Toallyz();
                toallyz.setReward(map.get(item));
                map1.put(getByPartyNum(item), toallyz);
            }
        }
        return proposal;
    };

    private final Response generateResponse(){
        return new Response(playerInfo, this.response(), null);
    };

    /**
     * 提出你的联盟提议
     * @return 联盟利益的分配方案
     */
    public abstract Map<Integer, Integer> propose();

    /**
     * 对联盟提案做出响应
     * @return 是否同意进入该联盟
     */
    public abstract boolean response();

    /**
     * 生成联盟提议中的附加语言信息
     * @return
     */
    public abstract Communicate Communicate();


    private final int getRewards(Set<Integer> nums){
        Set<Coalition> coalitions = votingDomain.getCoalitions();
        int rewards = 0;
        for(Coalition item: coalitions){
            if(item.getPartyNums().equals(nums)){
                rewards = item.getRewards();
            }
        }
        return rewards;
    }

    private final Proposal generateRandomProposal(){
        Proposal proposal = new Proposal();
        Map proposerz = new HashMap();
        Map alliesz = new HashMap();
        proposal.setProposerz(proposerz);
        proposal.setAlliesz(alliesz);
        Set<Coalition> coalitions = votingDomain.getCoalitions();
        Coalition selectedCoalition = null;
        int selectIndex = new Random().nextInt(coalitions.size());
        int i = 0;
        int myPartyNum = ((PartyInfo)playerInfo.getRoleInfo()).getPartyNum();
        while (selectedCoalition == null){
            for(Coalition item: coalitions){
                if(i >= selectIndex && item.getPartyNums().contains(myPartyNum)){
                    selectedCoalition = item;
                    break;
                }
                i++;
            }
        }
        int rewards = selectedCoalition.getRewards();
        int allies = selectedCoalition.getPartyNums().size() - 1;
        int avgReward = (int)(rewards/(allies+1));
        for(int item: selectedCoalition.getPartyNums()){
            if(item == ((PartyInfo)playerInfo.getRoleInfo()).getPartyNum()){
                //allies中去除掉自己
                continue;
            }
            PlayerInfo playerInfo = getByPartyNum(item);
            Toallyz toallyz = new Toallyz();
            toallyz.setReward(avgReward);
            alliesz.put(playerInfo, toallyz);
        }
        proposerz.put(playerInfo, rewards - avgReward*allies);
        return proposal;
    }

    private final Response generateRandomRespose(){
        Response response = new Response();
        response.setPlayerInfo(playerInfo);
        boolean b = new Random().nextBoolean();
        response.setAgree(b);
        return response;
    }



    private final PlayerInfo getByPartyNum(int partyNum){
        PlayerInfo playerInfo = null;
        for(PlayerInfo item: playerInfos){
            PartyInfo partyInfo = (PartyInfo) item.getRoleInfo();
            if(partyNum == partyInfo.getPartyNum()){
                playerInfo = item;
                break;
            }
        }
        return playerInfo;
    }

    public enum type{

        PROPOSER,

        RESPONSER,

    }

    private class InvalidCoalitionException extends RuntimeException{
        public InvalidCoalitionException(String message) {
            super(message);
        }
    }

    /**
     * 计算经过衰减因子作用后的净收益
     * @param reward 毛收益
     * @param talent 开销
     * @param discount 衰减因子
     * @param passedRound 当前轮数
     * @param maxRound 最大总轮数
     * @return 衰减计算后的净收益
     */
    protected double getDiscountedUtility(int reward, double talent, double discount, int passedRound, int maxRound){
        double discountUtility  = reward * Math.pow(discount, ((double)(passedRound))/maxRound) + talent;
        return discountUtility;
    }
}
