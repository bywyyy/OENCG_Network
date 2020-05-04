package com.swu.agentlab.zsnp.game.coalition.voting.game;

import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Comment;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JJ.Wu
 */
@Data
public class Toallyz implements Serializable {

    private int reward;

    private Comment comment;

    public Toallyz() {
    }

    public Toallyz(int reward, Comment comment) {
        this.reward = reward;
        this.comment = comment;
    }
}
