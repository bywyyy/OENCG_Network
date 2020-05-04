package com.swu.agentlab.zsnp.entity.player;

import java.util.HashSet;

/**
 * @author JJ.Wu
 */
public class PlayerSet extends HashSet<Player> {

    /**
     * 根据玩家id在集合中查找玩家
     * @param id
     * @return
     */
    public Player get(String id){
        if(id==null||"".equals(id)){
            return null;
        }
        Player p = null;
        for(Player item: this){
            if(id.equals(item.getId())){
                p = item;
                break;
            }
        }
        return p;
    }


}
