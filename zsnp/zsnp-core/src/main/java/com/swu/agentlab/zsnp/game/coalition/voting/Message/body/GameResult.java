package com.swu.agentlab.zsnp.game.coalition.voting.Message.body;

import com.swu.agentlab.zsnp.entity.message.result.Result;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class GameResult extends Result implements Serializable {

    private Map<PlayerInfo, Double> results;

    public GameResult(Map<PlayerInfo, Double> results) {
        this.results = results;
    }
}
