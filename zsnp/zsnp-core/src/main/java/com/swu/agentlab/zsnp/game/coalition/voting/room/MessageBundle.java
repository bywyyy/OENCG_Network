package com.swu.agentlab.zsnp.game.coalition.voting.room;

import com.swu.agentlab.zsnp.conf.BaseConfigBundle;
import com.swu.agentlab.zsnp.entity.message.Message;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

public class MessageBundle extends BaseConfigBundle {

    private static String personalResourcePath = "./config/server_message.properties";

    private static String commonResourcePath = "conf.server_message";

    private static volatile MessageBundle bundle = null;

    public static synchronized MessageBundle getInstance(){
        if(bundle == null){
            bundle = new MessageBundle(personalResourcePath, commonResourcePath);
        }
        return bundle;
    }

    private MessageBundle(String personalResourcePath, String commonResourcePath){
        super(personalResourcePath, commonResourcePath);
    }

    public String formatMsg(String key, Object... args){
        return super.formatString(key, args);
    }

}
