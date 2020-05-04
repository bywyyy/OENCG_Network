package com.swu.agentlab.zsnp.gui.config;

import com.swu.agentlab.zsnp.conf.BaseConfigBundle;

import java.util.HashMap;
import java.util.Map;

public class GUIBundle extends BaseConfigBundle {

    private static String basePersonalResourcePath = "./config/gui/";

    private static String baseCommonResourcePath = "conf.gui.";

    private static volatile Map<String, GUIBundle> guiBundleMap = new HashMap<>();

    public static GUIBundle getInstance(String gui){
        GUIBundle bundle = guiBundleMap.get(gui);
        if(bundle == null){
            bundle = new GUIBundle(basePersonalResourcePath + gui + ".properties", baseCommonResourcePath + gui);
            guiBundleMap.put(gui, bundle);
        }
        return bundle;
    }

    private GUIBundle(String personalResourcePath, String commonResourcePath) {
        super(personalResourcePath, commonResourcePath);
    }
}
