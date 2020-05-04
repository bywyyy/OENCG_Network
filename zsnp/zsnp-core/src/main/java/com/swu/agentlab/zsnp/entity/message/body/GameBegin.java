package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.room.Statue;
import lombok.Data;

import java.util.Set;

/**
 * 服务器提示内容、即将发言的玩家，房间的状态
 * @author JJ.Wu
 */
@Data
public class GameBegin extends Body{

    private String cont;

    private Set<String> speakers;

    private Statue roomStatue;

    public GameBegin(){}

    public GameBegin(String cont, Set speakers, Statue statue){
        this.setBodyType(BodyType.GAME_BEGIN);
        this.cont = cont;
        this.speakers = speakers;
        this.roomStatue = statue;
    }

}
