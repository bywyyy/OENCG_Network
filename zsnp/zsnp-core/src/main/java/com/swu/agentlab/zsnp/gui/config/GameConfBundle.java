package com.swu.agentlab.zsnp.gui.config;

import com.swu.agentlab.zsnp.conf.BaseConfigBundle;

import java.util.HashMap;
import java.util.Map;

public class GameConfBundle extends BaseConfigBundle {

    private static String basePersonalResourcePath = "./config/";

    private static String baseCommonResourcePath = "conf.";

    private static volatile Map<String, GameConfBundle> gameConfBundleMap = new HashMap<>();

    private GameConfBundle(String personalResourcePath, String commonResourcePath) {
        super(personalResourcePath, commonResourcePath);
    }

    public static GameConfBundle getInstance(String gameConf){
        GameConfBundle gameConfBundle = gameConfBundleMap.get(gameConf);
        if(gameConfBundle == null){
            gameConfBundle = new GameConfBundle(
                    basePersonalResourcePath + gameConf + ".properties",
                    baseCommonResourcePath + gameConf
            );
            gameConfBundleMap.put(gameConf, gameConfBundle);
        }
        return gameConfBundle;
    }
}
