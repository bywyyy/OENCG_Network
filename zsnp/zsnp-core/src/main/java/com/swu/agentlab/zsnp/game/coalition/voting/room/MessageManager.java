package com.swu.agentlab.zsnp.game.coalition.voting.room;

import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.*;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.entity.room.BaseMessageManager;
import com.swu.agentlab.zsnp.entity.room.Statue;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.*;
import com.swu.agentlab.zsnp.game.coalition.voting.Party;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingProtocol;
import com.swu.agentlab.zsnp.game.coalition.voting.controller.impl.GameRecController;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Round;
import com.swu.agentlab.zsnp.util.TimeUtil;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

public class MessageManager extends BaseMessageManager {

    private final Logger log = Logger.getLogger(MessageManager.class);

    @Setter
    private VotingProtocol protocol;

    private int[] talents;

    private List<double[]> results;

    //private CounterController counterController;

    //private PlayerController playerController;

    private GameRecController gameRecController;

    private MessageBundle messageBundle;

    public MessageManager() {
        /**
         *
         */
        gameRecController = new GameRecController();
        results = new ArrayList<>();
        messageBundle = MessageBundle.getInstance();
    }

    /*public MessageManager(VotingProtocol protocol) {
        this.protocol = protocol;
        //playerController = new PlayerControllerImpl();
        //counterController = new CounterController();
    }*/

    @Override
    public void receiveMessage(Message message) {
    }


    /**
     *
     * @param player
     */
    @Override
    public void sendLoginResponse(Player player) {

        /*Party party = ((Party) player.getRole()).clone();
        party.loadTalent();*/
        //String cont = "Hi, "+player.getName()+", you are the "+this.room.getAmountOfPlayers()+"th player of this room.";

        /**
         *message 中的字段
         */

        String cont = messageBundle.formatMsg("self_login", player.getName(), this.room.getAmountOfPlayers());

        /**
         * 这个domain到底是什么意思
         */
        VotingDomain votingDomain = (VotingDomain) SerializationUtils.clone((Serializable) this.room.getDomain());
        System.out.println("____________________________");
        System.out.println(votingDomain);
        System.out.println("____________________________");
        /**
         * partyNum是什么意思？？？？？
         */
        int partyNum = ((Party) player.getRole()).getPartyNum();
        System.out.println("____________________________");
        System.out.println(partyNum);
        System.out.println("____________________________");

        boolean[][] knowledges = votingDomain.getKnowledges();
        System.out.println("____________________________");
        System.out.println(knowledges);
        System.out.println("____________________________");

        System.out.println("____________________________");
        System.out.println(knowledges.length);
        System.out.println("____________________________");


        /**
         *  当前party 对其他人可不可见
         */
        for(int i = 0; i<knowledges.length; i++){
            if(!knowledges[partyNum][i]){
                votingDomain.getPartyByNum(i).setTalent(0);
            }
        }

        /**
         * 生成loginResponse,将所有的信息都赋值进来
         */

        LoginResponse loginRes = new LoginResponse(true, this.room.generateInfo(),
                votingDomain, player.getRole().generateRoleInfo(),cont);
        /**
         * 将loginResponse，发送给服务器进行处理
         */
        player.sendMessage(new Message(null,null,null, loginRes, TimeUtil.getHms()));
        /*sleep();
        if(room.getDomain() instanceof VotingDomain){
            VotingDomain votingDomain = (VotingDomain) room.getDomain();
            if(room.getAmountOfPlayers() == 1){
                for(PartyInfo item:votingDomain.getParties()){
                    if(item.getPartyNum() == ((Party)player.getRole()).getPartyNum()){
                        item.setTalent(0.0);
                        break;
                    }
                }
            }
        }*/
    }

    /**
     *通知其他玩家有新的玩家进入房间
     * @param player 刚刚进入房间的玩家
     */
    @Override
    public void notifyLoginMessage(Player player) {
        //String cont = "The "+ room.getAmountOfPlayers()+"th player, "+player.getName()+"("+player.getRole().getRoleName()+") entered this room.";
        String cont = messageBundle.formatMsg("other_login", room.getAmountOfPlayers(), player.getName(), player.getRole().getRoleName());
        PlayerLogin playerLogin = new PlayerLogin(player.generatePlayerInfo(), cont);
        Message message = new Message(null, null, null, playerLogin, null);
        this.sendMessage(room.getPlayers(), message);
    }

    /**
     *当游戏开始调用这个
     */
    @Override
    public void notifyGameStart() {
        /**
         * 如果Domain是votingDomain类型的
         */
        if(this.room.getDomain() instanceof VotingDomain){
            VotingDomain votingDomain = (VotingDomain) room.getDomain();
            int majority = votingDomain.getMajority();
            /**
             *
             */
            gameRecController.setRoomInfo(this.room.getId(), this.room.getName(), majority);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //playerController.add(player.generatePlayerInfo());
                    boolean[][] knowledge = votingDomain.getKnowledges();
                    String[] roleNames = new String[knowledge.length];
                    int[] resources = new int[knowledge.length];
                    double[] talents = new double[knowledge.length];
                    for(int i = 0; i<knowledge.length; i++){
                        roleNames[i] = votingDomain.getPartyByNum(i).getRoleName();
                        resources[i] = votingDomain.getPartyByNum(i).getResource();
                        talents[i] = votingDomain.getPartyByNum(i).getTalent();
                    }
                    gameRecController.addKnowledges(roleNames, knowledge, resources, talents);
                    for(Player item: room.getPlayers()){
                        /**
                         * gameRecController???是干什么的
                         */
                        gameRecController.addPlayer(item);
                    }
                }
            }).start();
        }


        /**
         * 生成game_begin的消息
         */
        String cont;
        //cont = "Game begins.";
        cont = messageBundle.formatMsg("game_begin");
        /**
         * 得到当前房间的状态
         */
        Statue statue = room.getStatue();
        /**
         * 生成gameBegin的消息
         */
        GameBegin begin = new GameBegin(cont, new HashSet(), statue);
        Message message = new Message(null, null, null, begin, null);
        /**
         * 把gamebegin的消息传送给各个player
         */
        this.sendMessage(room.getPlayers(), message);

        //sleep();
        Set<String> speakers = new HashSet<>();
        SessionBegin sessionBegin = generateSessionBegin();
        /**
         * speakers设置为第一个进入房间的玩家
         */
        speakers.add(room.getPlayerIds().getFirst());
        StringBuilder builder = new StringBuilder();

        for(String speaker: speakers){
            Player player = room.getPlayers().get(speaker);
            if(!"".equals(builder.toString())){
                builder.append(", ");
            }
            builder.append(player.getName()+"(");
            builder.append(player.getRole().getRoleName()+")");
        }
        System.out.println("------------------------builder-----------------------------");
        System.out.println(builder);
        System.out.println("------------------------builder-----------------------------");

        //cont = "Current proposer is "+builder.toString()+".";
        cont = messageBundle.formatMsg("current_proposer", builder.toString());
        /**
         * 传送counterBody到remoteHuman，让相应的客户端可以提出方案
         */
        CounterBody counterBody = new CounterBody(room.getId(), speakers,sessionBegin);
        counterBody.setHint(cont);
        //message.setBody(counterBody);
        Message message1 = new Message(null, null, null, counterBody, TimeUtil.getHms());
        /**
         * 将这个信息传送给所有的player
         */
        this.sendMessage(room.getPlayers(), message1);
    }

    /**
     *
     * @param counterBody
     */
    @Override
    public synchronized void receiveCounterBody(CounterBody counterBody) {
        //VotingProtocol protocol = (VotingProtocol) room.getProtocol();
        protocol.receiveCounterBody(counterBody);
        counterBody.getOffer().setRoundNum(counterBody.getRoundNum());
        String time = TimeUtil.getHms();
        counterBody.getOffer().setTime(time);
        //添加到数据库
        new Thread(new Runnable() {
            @Override
            public void run() {
                //counterController.add(counterBody);
                gameRecController.addCounterBody(counterBody);
            }
        }).start();
        /*if(counterBody.getOffer() instanceof Proposal){
            counterBody.getOffer().setRoundNum(counterBody.getRoundNum());
            String time = TimeUtil.getHms();
            counterBody.getOffer().setTime(time);
        }*/
        if(counterBody.getOffer() instanceof Proposal){
            CounterBody counterBody3 = SerializationUtils.clone(counterBody);
            counterBody3.setNextSpeakers(new HashSet<>());
            ((Proposal)counterBody3.getOffer()).setSessionRoundNum(protocol.getSessionRoundNum());
            this.sendMessage(room.getPlayers(), new Message(null, null, null, counterBody3, time));
        }
        //sleep();
        if(protocol.isRoundEnd()){
            /**
             * 保存协商记录对象到文件------------------------------------------------
             */
            new Thread(()->{
                gameRecController.save();
            }).start();
            for(Response item: protocol.getRound().getResponses()){
                CounterBody counterBody4 = new CounterBody(room.getId(), new HashSet<>(), item);
                Message message = new Message(null, null, null, counterBody4, item.getTime());
                sendMessage(room.getPlayers(), message);
            }
            if(!protocol.isSessionEnd()){
                Set nextSpeakers = new HashSet();
                CounterBody counterBody2 = new CounterBody(room.getId(), nextSpeakers, null);
                //nextSpeakers.addAll(room.getProtocol().nextSpeakers());
                String hint = generateHint(nextSpeakers);
                counterBody2.setNextSpeakers(nextSpeakers);
                //counterBody2.setHint("Proposal failed. "+hint+", propose your coalition.");
                counterBody2.setHint(messageBundle.formatMsg("proposal_failed", hint));
                Message message = new Message(null, null, null, counterBody2, TimeUtil.getHms());
                sendMessage(room.getPlayers(), message);
                protocol.setRound(new Round());
                protocol.setRoundEnd(false);
            }else{
                //CounterBody counterBody1 = SerializationUtils.clone(counterBody);
                if(protocol.isSessionEnd()){
                    SessionEnd sessionEnd = generateSessionEnd();
                    //sleep();
                    CounterBody sessionEndBody = new CounterBody(room.getId(), new HashSet<String>(), sessionEnd);
                    sessionEndBody.setHint(sessionEnd.getHint());
            /*counterBody.setHint(sessionEnd.getHint());
            counterBody.setNextSpeakers(nextSpeakers);
            counterBody.setOffer(sessionEnd);*/
                    //this.sendMessage(room.getPlayers(), new Message(null, null, null, sessionEndBody, TimeUtil.getHm()));
                    //sleep();
                    double[] sessionUtility = generateSessionUtilities(sessionEnd);
                    results.add(sessionUtility);
                    this.sendSessionResult(sessionEnd);
                    //sleep();
                    if(!protocol.isGameEnd()){
                        //session结束，整个游戏没结束
                        Set<String> nextSpeakers = new HashSet<>();
                        //nextSpeakers.addAll(room.getProtocol().nextSpeakers());
                        protocol.setSessionNum(protocol.getSessionNum()+1);
                        protocol.setSessionRoundNum(0);
                        SessionBegin sessionBegin = generateSessionBegin();
                        CounterBody sessionBeginBody = new CounterBody(room.getId(),  nextSpeakers, sessionBegin);
                        String hint = generateHint(nextSpeakers);
                        //sessionBeginBody.setHint(sessionBegin.getHint()+"\nProposer is "+hint);
                        sessionBeginBody.setHint(sessionBegin.getHint() + messageBundle.formatMsg("session_proposer", hint));
                /*counterBody.setNextSpeakers(nextSpeakers);
                counterBody.setHint(sessionBegin.getHint()+"\nThe next speaker is "+builder.toString());
                counterBody.setOffer(sessionBegin);*/
                        this.sendMessage(room.getPlayers(), new Message(null, null, null, sessionBeginBody, TimeUtil.getHm()));
                    }else{
                        //游戏结束
                        room.setStatue(Statue.GAME_END);
                        //String hint = "Game over.\nPlease exit this room.";
                        String hint = messageBundle.formatMsg("game_over");
                        GameEnd gameEnd = new GameEnd(hint, room.getStatue());
                        GameResult gameResult = generateGameResult();
                        gameEnd.setResult(gameResult);
                        this.sendMessage(room.getPlayers(), new Message(null, null, null, gameEnd, TimeUtil.getHm()));
                        /**
                         * 保存协商记录对象到文件
                         */
                        new Thread(()->{
                            gameRecController.save(1);
                        }).start();
                    }
                    protocol.setSessionEnd(false);
                    protocol.setRoundEnd(false);
                    protocol.setRound(new Round());
                }
            }
            if(!protocol.isGameEnd()){
                Set nextSpeakers = new HashSet();
                nextSpeakers.addAll(protocol.nextSpeakers());
                String hint = generateHint(nextSpeakers);
                CounterBody counterBody1 = new CounterBody(room.getId(), nextSpeakers, null);
                counterBody1.setHint(hint);
                Message message = new Message(null, null, null, counterBody1, TimeUtil.getHms());
                sendMessage(room.getPlayers(), message);
            }
            //log.info(counterBody);
        }/*else{
            counterBody.setNextSpeakers(new HashSet<>());
        }*/

    }

    @Override
    public synchronized void receiveCommunicateBody(CommunicateBody communicateBody) {
        //VotingProtocol protocol = (VotingProtocol) room.getProtocol();
        //VotingProtocol protocol = (VotingProtocol) room.getProtocol();
        protocol.receiveCommunicateBody(communicateBody);

        communicateBody.getOffer().setRoundNum(communicateBody.getRoundNum());
        String time = TimeUtil.getHms();
        communicateBody.getOffer().setTime(time);

        if(communicateBody.getOffer() instanceof Communicate){
            this.sendMessage(room.getPlayers(), new Message(null, null, null, communicateBody,null));
        }
    }


    /**
     *
     * @param nextSpeakers
     * @return
     */
    private String generateHint(Set nextSpeakers){
        StringBuilder builder = new StringBuilder();
        for(Object speaker: nextSpeakers){
            if(!"".equals(builder.toString())){
                builder.append(", ");
            }
            Player player = room.getPlayers().get(speaker.toString());
            builder.append(player.getName()+"(");
            builder.append(player.getRole().getRoleName()+")");
        }
        String cont = messageBundle.formatMsg("next_proposer", builder.toString());
        return cont;
    }

    /**
     *
     * @return
     */
    private SessionEnd generateSessionEnd(){
        SessionEnd sessionEnd = new SessionEnd();
        boolean agree = protocol.isAgree();
        sessionEnd.setAgree(agree);
        int sessionNum = protocol.getSessionNum();
        sessionEnd.setSessionNum(sessionNum);
        Proposal proposal = protocol.getRound().getProposal();
        VotingDomain domain = protocol.getDomain();
        for(PlayerInfo playerInfo: proposal.getProposerz().keySet()){
            double res = 0.0d;
            if(agree){
                //res = proposal.getProposerz().get(playerInfo) * Math.pow(domain.getDiscountFactor(), (protocol.getSessionRoundNum() - 1)/domain.getMaxRound());
                res = proposal.getProposerz().get(playerInfo);
            }
            sessionEnd.getRewards().put(playerInfo, res);
        }
        for(PlayerInfo playerInfo: proposal.getAlliesz().keySet()){
            double res = 0.0d;
            if(agree){
                //res = proposal.getAlliesz().get(playerInfo).getReward() * Math.pow(domain.getDiscountFactor(), (protocol.getSessionRoundNum() - 1)/domain.getMaxRound());
                res = proposal.getAlliesz().get(playerInfo).getReward();
            }
            sessionEnd.getRewards().put(playerInfo, res);
        }
        Set<PlayerInfo> playerInfos = sessionEnd.getRewards().keySet();
        for(Player player: room.getPlayers()){
            PlayerInfo playerInfo = player.generatePlayerInfo();
            if(!playerInfos.contains(playerInfo)){
                sessionEnd.getRewards().put(playerInfo, 0d);
            }
        }
        //sessionEnd.setHint("Session "+sessionEnd.getSessionNum()+" ended.");
        sessionEnd.setHint(messageBundle.formatMsg("session_end", sessionEnd.getSessionNum()));
        return sessionEnd;
    }

    /**
     *
     * @return
     */
    private SessionBegin generateSessionBegin(){
        SessionBegin sessionBegin = new SessionBegin();
        sessionBegin.setSessionNum(protocol.getSessionNum());
        //sessionBegin.setHint("Session "+sessionBegin.getSessionNum()+" begins.");
        sessionBegin.setHint(messageBundle.formatMsg("session_begin", sessionBegin.getSessionNum()));
        return sessionBegin;
    }

    private void sleep(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param player 退出房间的玩家
     */
    @Override
    public void notifyLoginoutMessage(Player player) {
        PlayerLogout playerLogout = new PlayerLogout();
        playerLogout.setPlayerInfo(player.generatePlayerInfo());
        playerLogout.setStatue(room.getStatue());
        if(room.getStatue() == Statue.PRE_GAME||room.getStatue() == Statue.GAME_END){
            //playerLogout.setHint("Player, "+player.getName()+"("+player.getRole().getRoleName()+") exit this room.");
            playerLogout.setHint(messageBundle.formatMsg("player_exit", player.getName(), player.getRole().getRoleName()));
        }else{
            //playerLogout.setHint("Player, "+player.getName()+"("+player.getRole().getRoleName()+")exit this room. \nGame paused, wait for reconnection. ");
            playerLogout.setHint(messageBundle.formatMsg("player_exit_game_pause", player.getName(), player.getRole().getRoleName()));
        }
        this.sendMessage(room.getPlayers(), new Message(null, null, null, playerLogout, TimeUtil.getHm()));
    }

    /**
     *
     * @param player
     */
    @Override
    public void notifyDisconnectMessage(Player player) {
        PlayerDisconnect playerDisconnect = new PlayerDisconnect();
        playerDisconnect.setPlayerInfo(player.generatePlayerInfo());
        playerDisconnect.setStatue(room.getStatue());
        playerDisconnect.setTimeOut(8);
        //playerDisconnect.setHint("Player, "+player.getName()+"("+player.getRole()+") exited this room abnormally. this game has been terminated.\nYou will exit this room in "+playerDisconnect.getTimeOut()+" seconds.");
        playerDisconnect.setHint(messageBundle.formatMsg("player_exit_abnormally", player.getName(), player.getRole()));
        this.sendMessage(room.getPlayers(), new Message(null, null, null, playerDisconnect, TimeUtil.getHms()));
    }

    /**
     *
     * @param sessionEnd
     */
    private void sendSessionResult(SessionEnd sessionEnd){
        VotingDomain votingDomain = ((VotingDomain) this.room.getDomain());
        boolean[][] knowledges = votingDomain.getKnowledges();
        Map<PlayerInfo, Double> rewards = sessionEnd.getRewards();
        for(Player item: room.getPlayers()){
            Map<String, String> results = new HashMap<>();
            Party party = (Party) item.getRole();
            int partyNum = party.getPartyNum();
            for(PlayerInfo item2: rewards.keySet()){
                PartyInfo partyInfo = (PartyInfo) item2.getRoleInfo();
                String reward;
                /*if(rewards.get(item2) == 0){
                    reward = "0";
                }else{
                    reward = "?";
                }*/
                if(knowledges[partyNum][partyInfo.getPartyNum()]){
                    double talent = ((VotingDomain) this.room.getDomain()).getPartyByNum(partyInfo.getPartyNum()).getTalent();
                    double discountedReward = rewards.get(item2) * Math.pow(votingDomain.getDiscountFactor(), ((double)protocol.getSessionRoundNum())/protocol.getMaxRound());
                    reward = String.valueOf(discountedReward + talent);
                }else{
                    //reward = rewards.get(item2);
                    reward = "?";
                }
                String key = item2.getName()+"("+item2.getRoleInfo().getRoleName()+")";
                results.put(key, reward);
            }
            SessionResult sessionResult = new SessionResult(results);
            sessionResult.setAgree(sessionEnd.isAgree());
            item.sendMessage(new Message(null, null, null, new CounterBody(room.getId(), new HashSet<>(),sessionResult), TimeUtil.getHms()));
            //log.info(sessionResult);
        }
    }

    /**
     *
     * @param sessionEnd
     * @return
     */
    private double[] generateSessionUtilities(SessionEnd sessionEnd){
        VotingDomain votingDomain = (VotingDomain) this.room.getDomain();
        double[] results = new double[votingDomain.getAmountRoles()];
        Map<PlayerInfo, Double> rewards = sessionEnd.getRewards();
        for(PlayerInfo item: rewards.keySet()){
            double reward = rewards.get(item);
            PartyInfo partyInfo = (PartyInfo) item.getRoleInfo();
            int partyNum = partyInfo.getPartyNum();
            /*if("zero".equals(votingDomain.getFailedUtilityMode())){
                if(reward == 0){
                    results[partyNum] = reward;
                }
            }else{

            }*/
            /*if(reward == 0){
                results[partyNum] = reward;
            }else{
            }*/
            double talent = votingDomain.getPartyByNum(partyNum).getTalent();
            double discountedReward = reward * Math.pow(votingDomain.getDiscountFactor(), ((double)protocol.getSessionRoundNum())/protocol.getMaxRound());
            results[partyNum] = discountedReward + talent;
        }
        return results;
    }

    /**
     *
     * @return
     */
    private GameResult generateGameResult(){
        VotingDomain votingDomain = (VotingDomain) room.getDomain();
        Map<PlayerInfo, Double> results = new HashMap<>();
        double[] utility = new double[votingDomain.getAmountRoles()];
        for(double[] item: this.results){
            for(int i = 0; i<item.length; i++){
                utility[i] += item[i];
            }
        }
        for(Player item: room.getPlayers()){
            int partyNum = ((Party)item.getRole()).getPartyNum();
            results.put(item.generatePlayerInfo(), utility[partyNum]);
        }
        return new GameResult(results);
    }
}
