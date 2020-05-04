package com.swu.agentlab.zsnp.entity.player.human;

import com.swu.agentlab.zsnp.entity.communicator.Communicator;
import com.swu.agentlab.zsnp.entity.communicator.MySocket;
import com.swu.agentlab.zsnp.entity.communicator.pipeline.Pipe;
import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.message.Message;
import com.swu.agentlab.zsnp.entity.message.body.*;
import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.message.result.Result;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.entity.player.RemotePlayer;
import com.swu.agentlab.zsnp.entity.player.PlayerType;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.game.RoleInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.*;
import com.swu.agentlab.zsnp.game.coalition.voting.Party;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Toallyz;
import com.swu.agentlab.zsnp.gui.config.GUIBundle;
import com.swu.agentlab.zsnp.gui.dialog.CountdownDialog;
import com.swu.agentlab.zsnp.gui.game.coalition.voting.FrameCloseHandler;
import com.swu.agentlab.zsnp.gui.game.coalition.voting.VotingArena;
import com.swu.agentlab.zsnp.gui.player.Arena;
import com.swu.agentlab.zsnp.springboot.Myclass.WebVotingArena;
import com.swu.agentlab.zsnp.springboot.ZsnpApplication;
import com.swu.agentlab.zsnp.springboot.entity.*;
import com.swu.agentlab.zsnp.util.Timer;
import lombok.Data;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.Session;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Data
public class RemoteHuman extends RemotePlayer implements FrameCloseHandler {

    private static Logger log = Logger.getLogger(RemoteHuman.class);

    private GUIBundle guiBundle;

    private Arena arena = null;

    private WebVotingArena webVotingArena=null;

    private Map<String,Offer> offerHistory = new HashMap<>();

    private Map<String, Double>  allPayoff = new HashMap<>();

    private int sessionNum = 0;

    private String humanOrAgent = "human";

    private String nowEmotion = "1";


    public RemoteHuman(RemotePlayer remotePlayer, RoomInfo roomInfo, Domain domain, RoleInfo roleInfo){
        super(remotePlayer.getId(),
                remotePlayer.getName(),
                remotePlayer.getDescription(),
                remotePlayer.isConnected(),
                remotePlayer.getCommunicator(),
                remotePlayer.getAddr(),
                remotePlayer.getReceiver(),
                remotePlayer.getLaunch(),
                PlayerType.REMOTE_HUMAN);
        this.setDomain(domain);
//        Arena arena = null;
        if(domain instanceof VotingDomain){

            PartyInfo partyInfo = (PartyInfo) roleInfo;
            /**
             *包含的是某个谈判者的各项信息，权重等等
             */
            Party party = new Party(partyInfo.getPartyNum(), partyInfo.getRoleName(), partyInfo.getResource());

            /**
             * 给party赋值，将衰减值附给party
             */
            party.setTalent(partyInfo.getTalent());
            /**
             * party代表谈判的角色，直接赋值给remoteHuman，根据remoteHuman我们可以知道它代表的role
             */
            this.setRole(party);
            /**
             * \创建谈判页面的客户端，将客户端信息进行提交
             */
            arena = new VotingArena( this, this);
            /**
             * 初始化谈判页面，显示谈判页面
             */
            arena.init(this.generatePlayerInfo(), roomInfo, domain);
        }/*else if(domain instanceof TripleDomain){
            PartyInfo partyInfo = (PartyInfo) roleInfo;
            Party party = new Party(partyInfo.getPartyNum(), partyInfo.getRoleName(), partyInfo.getResource());
            party.setTalent(partyInfo.getTalent());
            this.setRole(party);
            //arena = new TripleArena(this, this);
            arena.init(this.generatePlayerInfo(), roomInfo, domain);
        }*/
        /**
         * role 中还包含着页面的信息，就是谈判页面是哪个，根据role可以找到谈判页面
         */
        this.getRole().setArena(arena);
        /**
         * 退出的处理器是页面
         */
        this.setExitHandler(arena);
        /**
         * 得到arena.poperties全部关键字信息，通过guiBundle 提取关键字信息
         */
        this.guiBundle = GUIBundle.getInstance("arena");
        //this.getRole().setSender(this);
    }

    /**
     * 另一种声明remoteHuman的方式
     * @param name
     * @param description
     * @param communicator
     */
    public RemoteHuman(String name, String description, Communicator communicator){
        this.setType(PlayerType.REMOTE_HUMAN);
        this.setName(name);
        this.setDescription(description);
        this.communicator = communicator;
        this.communicator.setReceiver(this);
    }

    /**
     * receiveMessage接收处理页面传过来的message信息
     * @param message
     */
    @Override
    public void receiveMessage(Message message) {
        //log.info(message);
        Body cont = message.getBody();
        switch (cont.getBodyType()){
            case COMMUNICATE_BODY:
                /**
                 * 语言信息的处理逻辑
                 */
                CommunicateBody communicateBody = (CommunicateBody) cont;
//                String guiBundleName = "communicate"+((Communicate)communicateBody.getOffer()).getCommunicateNum();
                this.nowEmotion = ((Communicate)communicateBody.getOffer()).getEmotion();
                String communicateLan =  ((Communicate)communicateBody.getOffer()).getCommunicateFree()+"."
                        +((Communicate)communicateBody.getOffer()).getCommunicateType();
                System.out.println("remoteHuman 语言模块");
                if(webVotingArena!=null) {
                    webVotingArena.sendCommunicateMessageToWeb(new CommunicateMessage(communicateLan,
                            ((Communicate) communicateBody.getOffer()).getPlayerInfo().getName(),
                            ((Communicate) communicateBody.getOffer()).getPlayerInfo().getRoleInfo().getRoleName(),
                            ((PartyInfo) ((Communicate) communicateBody.getOffer()).getPlayerInfo().getRoleInfo()).getPartyNum(), message.getTime(), "CommunicateMessage", ((Communicate) communicateBody.getOffer()).getEmotion()), this.getId());
                }
                this.getRole().receiveOffer(communicateBody.getOffer());
                break;
            case LOGIN_RESPONSE:
                /**
                 * 有登录信息的时候将服务器信息传送到客户端
                 */
                LoginResponse loginRes = (LoginResponse) cont;
                this.getRole().getArena().printServerMessage(message.getTime(), loginRes.getInfo());
                break;
            case SERVER_BODY:

                this.getRole().getArena().printServerMessage(message.getTime(), ((ServerBody) message.getBody()).getCont());
                if(webVotingArena != null) {
                    webVotingArena.sendServerMessageToWeb(new ServerMessage(((ServerBody) message.getBody()).getCont(), message.getTime(), "SERVERMESSAGE"), this.getId());
                }
                break;
            case PLAYER_LOGIN:
                /**
                 * 玩家登录的时候，把玩家信息更新到客户端，将服务器信息显示到页面
                 */
                PlayerLogin playerLogin = (PlayerLogin) cont;
                this.getRole().getArena().update(playerLogin.getPlayerInfo());
                this.getRole().getArena().printServerMessage(message.getTime(), playerLogin.getCont());
                if(webVotingArena != null) {
                    webVotingArena.sendServerMessageToWeb(new ServerMessage(playerLogin.getCont(), message.getTime(), "SERVERMESSAGE"), this.getId());
                }
                break;
            case PLAYER_DISCONNECT:
                //玩家非正常退出
                /**
                 * 玩家非正常状态的退出处理
                 */

                PlayerDisconnect playerDisconnect = (PlayerDisconnect) cont;
                this.getRole().speak(false);
                this.getRole().getArena().printServerMessage(message.getTime(), playerDisconnect.getHint());
                this.getRole().getArena().update(playerDisconnect.getStatue());
                int sec = playerDisconnect.getTimeOut();
                CountdownDialog dialog = new CountdownDialog(this.getRole().getArena().getFrame(), "Exit this room.", sec);
                Timer.Countdown(sec, restTime -> {
                    dialog.setSeconds(restTime);
                }, ()->{
                    dialog.dispose();
                    if(this.communicator instanceof MySocket){
                        System.exit(0);
                    }else if(this.communicator instanceof Pipe){
                        this.getRole().getArena().getFrame().dispose();
                        try {
                            ((Pipe) this.communicator).getPos().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case PLAYER_LOGOUT:
                //玩家正常退出
                /**
                 * 玩家正常退出的处理
                 */
                PlayerLogout playerLogout = (PlayerLogout) cont;
                this.getRole().speak(false);
                this.getRole().getArena().printServerMessage(message.getTime(), playerLogout.getHint());
                this.getRole().getArena().update(playerLogout.getStatue());
                break;
            case GAME_BEGIN:
                // 游戏开始
                /**
                 * 游戏开始gameBeagin的信息里包含应该哪个玩家发言，对发言玩家和不发言玩家作出不同的处理
                 */
                GameBegin gameBegin = (GameBegin) cont;
                this.getRole().getArena().update(gameBegin.getRoomStatue());
                if (gameBegin.getSpeakers().contains(this.getId())) {
                    this.getRole().speak(true);
                    if (humanOrAgent.equals("human")) {
                        webVotingArena.sendButtonMessageToWeb(new ButtonMessage("TRUE", "FALSE", "FALSE", "TRUE", "BUTTONMESSAGE"), this.getId());
                    }
                    this.getRole().getArena().printServerMessage(message.getTime(), guiBundle.getString("round_one_proposer_hint"));
                    if (webVotingArena != null) {
                        webVotingArena.sendServerMessageToWeb(new ServerMessage(guiBundle.getString("round_one_proposer_hint"), message.getTime(), "SERVERMESSAGE"), this.getId());
                    }
                } else {

                    this.getRole().speak(false);
                    if (humanOrAgent.equals("human")) {
                        webVotingArena.sendButtonMessageToWeb(new ButtonMessage("FALSE", "FALSE", "FALSE", "FALSE", "BUTTONMESSAGE"), this.getId());
                    }
                    this.getRole().getArena().printServerMessage(message.getTime(), gameBegin.getCont());
                    if (webVotingArena != null) {
                        webVotingArena.sendServerMessageToWeb(new ServerMessage(gameBegin.getCont(), message.getTime(), "SERVERMESSAGE"), this.getId());
                    }
                }
                break;
            case COUNTER_BODY:
                /**
                 * 这部分是处理客户端提交的message(CPUMTER_BODY)信息，提出方案，同意方案，拒绝方案，三类
                 */
//                webVotingArena.send();
                CounterBody counterBody = (CounterBody) cont;
                //log.info(counterBody);
                /**
                 * speakers 获取接下来要提出方案的speaker
                 */
                Set<String> speakers = counterBody.getNextSpeakers();
                /**
                 * 如果speaker不为空，那么执行下面语句
                 */
                if(!speakers.isEmpty()){
                    String hint = counterBody.getHint();
                    System.out.println( counterBody.getHint());
                    if (speakers.contains(this.getId())) {
                        this.getRole().speak(true);
                        if (humanOrAgent.equals("human")) {
                            webVotingArena.sendButtonMessageToWeb(new ButtonMessage("TRUE", "FALSE", "FALSE", "TRUE", "BUTTONMESSAGE"), this.getId());
                        }
                        this.getRole().getArena().printServerMessage(message.getTime(), hint + "\n" + guiBundle.getString("proposer_hint"));
                        if (webVotingArena != null) {
                            webVotingArena.sendServerMessageToWeb(new ServerMessage(hint + "\n" + guiBundle.getString("proposer_hint"), message.getTime(), "SERVERMESSAGE"), this.getId());
                        }
                    } else {
                        this.getRole().speak(false);
                        if (webVotingArena != null) {
                            if (humanOrAgent.equals("human")) {
                                webVotingArena.sendButtonMessageToWeb(new ButtonMessage("FALSE", "FALSE", "FALSE", "FALSE", "BUTTONMESSAGE"), this.getId());
                            }
                        }
                        this.getRole().getArena().printServerMessage(message.getTime(), hint);
                        if (webVotingArena != null) {
                            webVotingArena.sendServerMessageToWeb(new ServerMessage(hint, message.getTime(), "SERVERMESSAGE"), this.getId());
                        }
                    }
                }
                /**
                 * 调用客户端的printOffer将Offer打印到客户端中
                 */
                if (webVotingArena != null) {
                    if (counterBody.getOffer() instanceof Proposal) {
                        webVotingArena.sendProposalMessageToWeb(new OfferMessage(counterBody.getOffer(), message.getTime(), "OFFERMESSAGE",
                                counterBody.getSessionNum()), this.getId(), this.nowEmotion);
                        for (PlayerInfo playerInfo : ((Proposal) counterBody.getOffer()).getAlliesz().keySet()) {
                            //lbl_sender.setText(playerInfo.getName()+"("+playerInfo.getRoleInfo().getRoleName()+")");
                            if (this.getId().equals(playerInfo.getId()) && this.getHumanOrAgent().equals("human")) {
                                webVotingArena.sendButtonMessageToWeb(new ButtonMessage("FALSE", "TRUE", "TRUE", "FLASE", "BUTTONMESSAGE"), this.getId());
                            }

                        }
                        this.sessionNum = counterBody.getSessionNum();
/**
 * 记录回合proposal
 */
                        if(((Party)this.getRole()).getPartyNum()==0){

                            Map<PlayerInfo,Integer>  proposerz = ((Proposal)counterBody.getOffer()).getProposerz();
                            Map<PlayerInfo, Toallyz> alliesz = ((Proposal)counterBody.getOffer()).getAlliesz();
                            Map<Integer,Integer> proposer =  new HashMap<Integer, Integer>();
                            int playerNum=0;
                            int playerSize = proposerz.size()+alliesz.size();
                            PlayerInfo proposerInfo = null;
                            while(playerNum < 5)
                            {
                                boolean notReward=false;
                                for(PlayerInfo playerInfo:proposerz.keySet()){
                                    proposerInfo = playerInfo;
                                    if(((PartyInfo)playerInfo.getRoleInfo()).getPartyNum() == playerNum){
                                        proposer.put(playerNum,proposerz.get(playerInfo));
                                        playerNum++;
                                        notReward = true;
                                    }
                                }
                                for(PlayerInfo playerInfo:alliesz.keySet()){
                                    if(((PartyInfo)playerInfo.getRoleInfo()).getPartyNum()==playerNum){
                                        proposer.put(playerNum,alliesz.get(playerInfo).getReward());
                                        playerNum++;
                                        notReward=true;
                                    }
                                }
                                if(notReward==false){
                                    proposer.put(playerNum,-1);
                                    playerNum++;
                                }
                            }
                            ArrayList<ArrayList<String>> alldata = new ArrayList<ArrayList<String>>();
                            ArrayList<String> result = new ArrayList();
                            String goal="";
                            for(Integer partyNum:proposer.keySet()){
                                if(proposer.get(partyNum)==-1){
                                    goal = goal + ":-";
                                }else{
                                    goal = goal + ":" + proposer.get(partyNum);
                                }
                            }
                            result.add("Stage"+String.valueOf(counterBody.getSessionNum())+":Round"+String.valueOf(((Proposal) counterBody.getOffer()).getSessionRoundNum())+":"+proposerInfo.getRoleInfo().getRoleName()+goal);
                            alldata.add(result);
//                alldata.add(new ArrayList<String>(Arrays.asList("1", "11", "111")));
//                //添加一行
//                alldata.add(new ArrayList<String>(Arrays.asList("2","22","222")));
//                //添加一行
//                alldata.add(new ArrayList<String>(Arrays.asList("3","33","333")));
//                //添加一行 //保存成csv文件

                            long l = System.currentTimeMillis();
                            //new日期对象
                            Date date = new Date(l);
                            //转换提日期输出格式
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

//                    String nyr = dateFormat.format(date);


                            String nyr = "5players-a";


                            Array2CSV(alldata,"log/human-human/"+nyr+".csv");

                        }







                    } else if (counterBody.getOffer() instanceof SessionResult) {
                        webVotingArena.sendSessionResultMessageToWeb(new OfferMessage(counterBody.getOffer(), message.getTime(), "SessionResultMessage",
                                counterBody.getSessionNum()), this.getId());
                        SessionResult sessionResult = (SessionResult) counterBody.getOffer();
                        for (String name : sessionResult.getResults().keySet()) {
                            if (allPayoff.get(name) == null) {
                                allPayoff.put(name, Double.parseDouble(sessionResult.getResults().get(name)));
                            } else {
                                Double payoff = allPayoff.get(name);
                                payoff = payoff + Double.parseDouble(sessionResult.getResults().get(name));
                                allPayoff.put(name, payoff);
                            }
                        }
                        webVotingArena.sendAveragePayoff(allPayoff, this.sessionNum, this.getId(), "AveragePayoff");

                        if(((Party)this.getRole()).getPartyNum()==0){
                            ArrayList<ArrayList<String>> alldata = new ArrayList<ArrayList<String>>();
                            ArrayList<String> result = new ArrayList();
                            result.add("success");
                            alldata.add(result);
                            long l = System.currentTimeMillis();
                            //new日期对象
                            Date date = new Date(l);
                            //转换提日期输出格式
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    String nyr = dateFormat.format(date);
                            String nyr = "5players-a";

                            Array2CSV(alldata,"log/human-human/"+nyr+".csv");
                        }



                    } else if (counterBody.getOffer() instanceof Response) {
                        webVotingArena.sendResponseMessageToWeb(new OfferMessage(counterBody.getOffer(), message.getTime(), "ResponseMessage",
                                counterBody.getSessionNum()), this.getId(), this.nowEmotion);
                        if (((Response) counterBody.getOffer()).getPlayerInfo().getId().equals(this.getId()) && this.getHumanOrAgent().equals("human")) {
                            webVotingArena.sendButtonMessageToWeb(new ButtonMessage("FALSE", "FALSE", "FALSE", "FALSE", "BUTTONMESSAGE"), this.getId());
                        }
                    } else if (counterBody.getOffer() instanceof SessionEnd) {
                        System.out.println(counterBody.getOffer());
                        webVotingArena.sendSessionEndMessageToWeb(new OfferMessage(counterBody.getOffer(), message.getTime(), "SessionEndMessage",
                                counterBody.getSessionNum()), this.getId());
                    } else if (counterBody.getOffer() instanceof Offer) {
                        webVotingArena.sendOfferMessageToWeb(new OfferMessage(counterBody.getOffer(), message.getTime(), "OFFERMESSAGE",
                                counterBody.getSessionNum()), this.getId());
                    }
                }




                this.getRole().getArena().printOffer(counterBody.getOffer(), message.getTime());
                /**
                 * 调用party 中的receiveOffer函数
                 */
                this.getRole().receiveOffer(counterBody.getOffer());
                break;
            case GAME_END:
                //游戏结束
                /**
                 * 游戏结束的处理方式
                 */
                GameEnd gameEnd = (GameEnd) cont;
                this.getRole().getArena().update(gameEnd.getStatue());
                this.getRole().getArena().printServerMessage(message.getTime(), gameEnd.getCont());
                this.getRole().getArena().printOffer(gameEnd.getResult(), message.getTime());
                this.getRole().receiveOffer(gameEnd.getResult());
                Map<PlayerInfo, Double> results = ((GameResult)gameEnd.getResult()).getResults();

                System.out.println("游戏结束获取Session");
                System.out.println(this.getSession());
                if(((Party)this.getRole()).getPartyNum() == 0 && this.getSession() != null){
                    System.out.println("游戏结束进入发送给前端的程序");
                    List<BatchResultMessage> batchResultMessages = new ArrayList<BatchResultMessage>();
                    for (PlayerInfo playerInfo : results.keySet()) {
                        BatchResultMessage batchResultMessage = new BatchResultMessage();
                        String playerName = playerInfo.getName();
                        String roleName = playerInfo.getRoleInfo().getRoleName();
                        batchResultMessage.setPlayerName(playerName);
                        batchResultMessage.setRoleName(roleName);
                        batchResultMessage.setPayOff(results.get(playerInfo));
                        batchResultMessage.setPartyNum(((PartyInfo)playerInfo.getRoleInfo()).getPartyNum());
                        batchResultMessages.add(batchResultMessage);
                    }
                    try {
                        this.getSession().getBasicRemote().sendText(BatchResultMessages.jsonStr(batchResultMessages));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if(webVotingArena!=null){
                    webVotingArena.sendGameEndMessage(((GameResult)gameEnd.getResult()).getResults(),gameEnd.getCont(),"GameEnd",this.getId());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 存储日志用到的函数
     *
     */
    public static void Array2CSV(ArrayList<ArrayList<String>> data, String path) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path,true), "UTF-8"));
            for (int i = 0; i < data.size(); i++) {
                ArrayList<String> onerow = data.get(i);
                for (int j = 0; j < onerow.size(); j++) {
                    out.write(DelQuota(onerow.get(j)));
                    out.write(",");
                }
                out.newLine();
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String DelQuota(String str) {
        String result = str;
//        String[] strQuota = {"~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "`","'", ",", ".", "/", ":", "/,", "<", ">", "?"};
//        for (int i = 0; i < strQuota.length; i++) {
//            if (result.indexOf(strQuota[i]) > -1) result = result.replace(strQuota[i], "");
//        }
        return result;
    }













    @Override
    public void sendMessage(Message message) {
        communicator.sendMessage(message);
    }

    @Override
    public void onClose(Frame frame) {
        if(this.getCommunicator() instanceof MySocket){
            System.exit(0);
        }else if(this.getCommunicator() instanceof Pipe){
            Pipe pipe = (Pipe) this.getCommunicator();
            if(pipe != null){
                try {
                    pipe.getPos().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            frame.dispose();
        }
    }
}
