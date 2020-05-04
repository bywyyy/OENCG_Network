package com.swu.agentlab.zsnp.entity.message.body;

import com.swu.agentlab.zsnp.entity.message.result.Result;
import com.swu.agentlab.zsnp.entity.room.Statue;
import lombok.Data;

/**
 * @author JJ.Wu
 */
@Data
public class GameEnd extends Body {

    private Result result;

    private String cont;

    private Statue statue;

    public GameEnd(){
        this.bodyType = BodyType.GAME_END;
    }

    public GameEnd(String cont, Statue statue) {
        this.bodyType = BodyType.GAME_END;
        this.cont = cont;
        this.statue = statue;
    }
}
