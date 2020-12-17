package com.swu.agentlab.zsnp.service.room;

import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.message.body.CommunicateBody;
import com.swu.agentlab.zsnp.entity.message.body.CounterBody;
import com.swu.agentlab.zsnp.entity.player.Player;
import com.swu.agentlab.zsnp.entity.player.PlayerSet;
import com.swu.agentlab.zsnp.entity.protocol.Protocol;
import com.swu.agentlab.zsnp.entity.room.BaseMessageManager;
import com.swu.agentlab.zsnp.entity.room.Room;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.entity.room.Statue;
import com.swu.agentlab.zsnp.game.Role;
import com.swu.agentlab.zsnp.game.coalition.voting.Party;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingDomain;
import com.swu.agentlab.zsnp.game.coalition.voting.VotingProtocol;
import com.swu.agentlab.zsnp.game.coalition.voting.room.MessageManager;
import com.swu.agentlab.zsnp.util.IdUtil;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class RoomService extends BaseRoomService {


    /**
     * @param name
     * @param description
     * @param domain
     * @param protocol
     * @return
     */


    @Override
    public Room createRoom(String name, String description, Domain domain, Protocol protocol,
                           BaseMessageManager messageManager, int maxStage, int maxRound, String stagePath) {
        Room room = new Room();
        room.setId(IdUtil.generate16HexId());
        room.setName(name);
        String des = "";
//        room.setDescription(des);
        PlayerSet players = new PlayerSet();
        room.setPlayers(players);
        room.setPlayerIds(new LinkedList<>());
        //r如果是VotingDomain这个domain
        if (domain instanceof VotingDomain) {
            domain = VotingDomain.loadVotingDomain(stagePath);
            VotingDomain votingDomain = (VotingDomain) domain;
            Set<PartyInfo> partyInfos = votingDomain.getParties();
            String roomNameOne = stagePath.substring(0, 1);
            if (partyInfos.size() == 3) {
                if (roomNameOne.equals("n")) {
                    des = "This is a three-players NTU repeated coalitional negotiation game in which parties can form bidding coalitions. \neg. the parliamentary government system.";
                } else {
                    des = "This is a three-players repeated coalitional negotiation game in which parties can form bidding coalitions. \neg. the parliamentary government system.";
                }
            } else {
                if (roomNameOne.equals("n")) {
                    des = "This is a five-players NTU repeated coalitional negotiation game in which parties can form bidding coalitions. \neg. the parliamentary government system.";
                } else {
                    des = "This is a five-players repeated coalitional negotiation game in which parties can form bidding coalitions. \neg. the parliamentary government system.";
                }
            }
            VotingProtocol votingProtocol = (VotingProtocol) protocol;
            votingProtocol.setPlayerIds(room.getPlayerIds());
            votingProtocol.setDomain(votingDomain);
            if (maxStage != -1) {
                votingProtocol.setSessionAmounts(maxStage);
                votingProtocol.setMaxRound(maxRound);
            } else {
                votingProtocol.setSessionAmounts(votingDomain.getMaxSession());
                votingProtocol.setMaxRound(votingDomain.getMaxRound());
            }
            ((MessageManager) messageManager).setProtocol(votingProtocol);
        }
        room.setDescription(des);
        room.setDomain(domain);
        room.setProtocol(protocol);
        room.setStatue(Statue.PRE_GAME);
        messageManager.setRoom(room);
        room.setMessageManager(messageManager);
        return room;
    }

    /**
     * @param room
     * @return
     */
    @Override
    public RoomInfo generateRoomInfo(Room room) {
        return room == null ? null : room.generateInfo();
    }

    /**
     * @param room
     * @param player
     */
    @Override
    public Role addPlayer(Room room, Player player) {
        synchronized (room) {
            Role role = null;
            if (room.getDomain() instanceof VotingDomain) {
                PartyInfo partyInfo = null;
                VotingDomain votingDomain = (VotingDomain) room.getDomain();
                partyInfo = votingDomain.getPartyByNum(room.getAmountOfPlayers());
                Party party = new Party(partyInfo);
                //party.loadTalent();
                player.setRole(party);

                Party p = party.clone();
                //p.loadTalent();
                role = p;
            }
            room.getPlayerIds().add(player.getId());
            room.setAmountOfPlayers(room.getAmountOfPlayers() + 1);
            /**
             * 更新房间信息，通过房间内的人数还有其他信息来更新房间信息
             */
            this.updateRoomStatue(room);
            //通知进入房间的那个玩家
            /**
             * MessageManager 中的sendLoginResponse将消息发送给当前的player
             */
            room.getMessageManager().sendLoginResponse(player);
            //通知已在房间内的玩家，新的玩家进入房间

            room.getMessageManager().notifyLoginMessage(player);
            //真正的添加玩家到房间内
            room.getPlayers().add(player);
            /**
             * 检查房间的状态,如果房间在游戏中，则调用notifyGameStart()向房间中各个agent提供游戏开始的信息
             */
            if (room.getStatue() == Statue.ON_GAME) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /**
                 *
                 */
                room.getMessageManager().notifyGameStart();
            }
            return role;
        }
    }

    @Override
    public synchronized void updateRoomStatue(Room room) {
        int amountOfRoles = room.getDomain().getAmountRoles();
        switch (room.getStatue()) {
            case PRE_GAME:
                if (room.getAmountOfPlayers() >= amountOfRoles) {
                    room.setStatue(Statue.ON_GAME);
                }
                break;
            case ON_GAME:
                if (room.getAmountOfPlayers() < amountOfRoles) {
                    room.setStatue(Statue.GAME_PAUSE);
                }
                break;
            case GAME_PAUSE:
                if (room.getAmountOfPlayers() >= amountOfRoles) {
                    room.setStatue(Statue.ON_GAME);
                } else if (room.getAmountOfPlayers() <= 0) {
                    room.setStatue(Statue.GAME_PAUSE);
                }
                break;
            case GAME_END:

                break;
            default:

                break;
        }
    }


    /**
     * @param room
     * @param counterBody
     */
    @Override
    public void receiveCounterBody(Room room, CounterBody counterBody) {
        room.getMessageManager().receiveCounterBody(counterBody);
    }

    @Override
    public void receiveCommunicateBody(Room room, CommunicateBody communicateBody) {
        room.getMessageManager().receiveCommunicateBody(communicateBody);
    }


    @Override
    public Set<String> getPlayerNames(Room room) {
        Set<String> names = new HashSet<>();
        for (Player player : room.getPlayers()) {
            names.add(player.getName());
        }
        return names;
    }

    @Override
    public void playerExit(Room room, Player player) {
        Statue statue = room.getStatue();
        if (statue == Statue.GAME_END) {
            //房间游戏已结束，玩家正常退出房间
            room.getPlayerIds().remove(player.getId());
            room.getPlayers().remove(player);
            room.getMessageManager().notifyLoginoutMessage(player);
        } else {
            //房间游戏还未开始、或正在游戏、或游戏暂停，玩家退出了房间，玩家异常退出房间
            room.getPlayerIds().remove(player.getId());
            room.getPlayers().remove(player);
            room.getMessageManager().notifyDisconnectMessage(player);
        }
        player = null;
        room.setAmountOfPlayers(room.getAmountOfPlayers() - 1);
        this.updateRoomStatue(room);
    }

    @Override
    public void deleteRoom(Room room) {
        //移除房间内的玩家
        room.getPlayerIds().clear();
        room.getPlayers().clear();
    }
}
