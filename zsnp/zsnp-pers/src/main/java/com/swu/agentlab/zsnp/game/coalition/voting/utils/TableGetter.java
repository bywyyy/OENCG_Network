package com.swu.agentlab.zsnp.game.coalition.voting.utils;

public class TableGetter {

    public static String getTableName(Object o){
        String className = o.getClass().getSimpleName();
        return "t_"+className.toLowerCase();
    }

}
