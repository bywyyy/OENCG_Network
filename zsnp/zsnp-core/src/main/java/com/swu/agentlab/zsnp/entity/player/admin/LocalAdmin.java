package com.swu.agentlab.zsnp.entity.player.admin;

import com.swu.agentlab.zsnp.controller.room.BaseRoomController;
import com.swu.agentlab.zsnp.controller.room.RoomController;
import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.entity.protocol.Protocol;
import com.swu.agentlab.zsnp.entity.room.Room;
import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.entity.room.Statue;
import com.swu.agentlab.zsnp.game.coalition.voting.room.MessageManager;
import com.swu.agentlab.zsnp.gui.admin.AdminForm;
import com.swu.agentlab.zsnp.gui.admin.RoomHandler;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class LocalAdmin extends Admin implements RoomHandler {

    private final Logger log = Logger.getLogger(LocalAdmin.class);

    private BaseRoomController roomController;

    private Set<RoomInfo> roomInfos = new HashSet<>();

    public LocalAdmin(BaseRoomController roomController) {
        this.roomController = roomController;
        //adminForm = new AdminForm(this);
        //this.roomInfos = roomController.getAllRoomInfo();
        //this.adminForm.init(roomInfos);
    }

    /**
     * 创建也给房间
     * @param name 房间名
     * @param description 房间描述
     * @param domainClass 域的全类名(com.swu.agentlab.zsnp.XxxDoamin)
     * @param protocolClass 协议的全类名(com.swu.agentlab.zsnp.XxxProtocol)
     * @param messageManagerClass 消息管理器的全类名(com.swu.agentlab.XxxMessageManager)
     */
    @Override
    public Room createRoom(String name, String description, String domainClass, String protocolClass, String messageManagerClass,int maxStage,int maxRound,String stagePath) {
        Room room = null;
        try {
            Domain domain = (Domain) Class.forName(domainClass).newInstance();
            Protocol protocol = (Protocol) Class.forName(protocolClass).newInstance();
            MessageManager messageManager = (MessageManager) Class.forName(messageManagerClass).newInstance();
            room =  roomController.createRoom(name, description, domain, protocol, messageManager,maxStage,maxRound,stagePath);
            //this.adminForm.appendRoom(room.generateInfo());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return room;
    }

    @Override
    public void deleteRoom(String roomId) {
        log.info("deleteRoom");
        roomController.deleteRoom(roomId);
        adminForm.removeRoom(roomId);
    }

    @Override
    public void deleteRoomData(String roomId) {
        log.info("deleteRoomData");
    }

    @Override
    public void selectRoomByStatue(Statue statue) {
        log.info("selectRoomByStatue: "+statue);
        this.roomInfos = roomController.selectRoomsByStatue(statue);
        adminForm.updateAllRooms(this.roomInfos);
    }
}
