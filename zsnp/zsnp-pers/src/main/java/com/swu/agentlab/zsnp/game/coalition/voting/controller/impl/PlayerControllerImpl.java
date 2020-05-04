package com.swu.agentlab.zsnp.game.coalition.voting.controller.impl;

import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.controller.BaseController;
import com.swu.agentlab.zsnp.game.coalition.voting.controller.PlayerController;
import com.swu.agentlab.zsnp.game.coalition.voting.entity.player.Player;
import com.swu.agentlab.zsnp.game.coalition.voting.service.PlayerService;
import com.swu.agentlab.zsnp.game.coalition.voting.service.impl.PlayerServiceImpl;

public class PlayerControllerImpl implements BaseController<PlayerInfo>, PlayerController {

    private PlayerService service;

    public PlayerControllerImpl(){
        this.service = new PlayerServiceImpl();
    }

    @Override
    public void add(PlayerInfo playerInfo) {
        Player player = new Player();
        player.setId(playerInfo.getId());
        player.setName(playerInfo.getName());
        player.setDescription(playerInfo.getDescription());
        player.setPartyNum(((PartyInfo)playerInfo.getRoleInfo()).getPartyNum());
        service.insert(player);
    }

    @Override
    public PlayerInfo getById(String id) {
        return null;
    }
}
