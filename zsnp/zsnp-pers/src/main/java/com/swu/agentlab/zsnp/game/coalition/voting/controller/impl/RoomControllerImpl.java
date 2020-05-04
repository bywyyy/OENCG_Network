package com.swu.agentlab.zsnp.game.coalition.voting.controller.impl;

import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.controller.BaseController;
import com.swu.agentlab.zsnp.game.coalition.voting.controller.RoomController;
import com.swu.agentlab.zsnp.game.coalition.voting.entity.room.Room;
import com.swu.agentlab.zsnp.game.coalition.voting.service.RoomService;
import com.swu.agentlab.zsnp.game.coalition.voting.service.impl.RoomServiceImpl;

public class RoomControllerImpl implements BaseController<RoomInfo>, RoomController {

    private RoomService roomService;

    public RoomControllerImpl() {
        this.roomService = new RoomServiceImpl();
    }

    @Override
    public void add(RoomInfo roomInfo) {
        Room room  = new Room();
        room.setId(roomInfo.getId());
        room.setName(roomInfo.getName());
        room.setDomain_name(roomInfo.getDomainName());
        room.setProtocol_class(roomInfo.getDomainName());
        room.setDescription(roomInfo.getDescription());
        roomService.insert(room);

    }

    @Override
    public RoomInfo getById(String id) {
        return null;
    }
}
