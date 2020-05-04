package com.swu.agentlab.zsnp.gui.player;

import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.gui.Room.RoomInfoPanel;
import com.swu.agentlab.zsnp.gui.Room.RoomSelectedListener;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class RoomsPanel extends JPanel implements RoomSelectedListener {

    private Set<RoomInfo> roomInfos;

    private RoomSelectedListener roomSelectedListener;

    public RoomsPanel(Set<RoomInfo> roomInfos, RoomSelectedListener roomSelectedListener){
        this.setBackground(Color.white);
        this.setOpaque(true);
        this.roomInfos = roomInfos;
        this.roomSelectedListener = roomSelectedListener;
        for(RoomInfo item: roomInfos){
            this.appendRoom(item);
        }
    }

    public void appendRoom(RoomInfo roomInfo){
        roomInfos.add(roomInfo);
        RoomInfoPanel roomInfoPanel = new RoomInfoPanel(roomInfo, this);
        this.add(roomInfoPanel);
        this.updateUI();
    }

    public void update(RoomInfo roomInfo) {
        Component[] components = this.getComponents();
        for(Component item: components){
            RoomInfoPanel panel = (RoomInfoPanel) item;
            if(panel.getRoomId().equals(roomInfo.getId())){
                panel.updateInfo(roomInfo);
                break;
            }
        }
    }

    public void setRoomInfos(Set<RoomInfo> roomInfos){
        this.roomInfos = roomInfos;
        this.removeAll();
        for(RoomInfo item: roomInfos){
            this.appendRoom(item);
        }
        this.updateUI();
    }

    public void removeRoom(String roomId){
        Component[] components = this.getComponents();
        for(Component item: components){
            RoomInfoPanel panel = (RoomInfoPanel) item;
            if(panel.getRoomId().equals(roomId)){
                this.remove(panel);
                updateUI();
                break;
            }
        }
    }

    public RoomInfo getById(String roomId){
        RoomInfo roomInfo = null;
        for(RoomInfo item: roomInfos){
            if(item.getId().equals(roomId)){
                roomInfo = item;
                break;
            }
        }
        return roomInfo;
    }

    @Override
    public void unselectedAllRoomPanel() {
        Component[] components = this.getComponents();
        for(Component item: components){
            RoomInfoPanel panel = (RoomInfoPanel) item;
            panel.setUnselected();
        }
        roomSelectedListener.unselectedAllRoomPanel();
    }

    @Override
    public void setSelectedRoomId(String roomId) {
        roomSelectedListener.setSelectedRoomId(roomId);
    }

}
