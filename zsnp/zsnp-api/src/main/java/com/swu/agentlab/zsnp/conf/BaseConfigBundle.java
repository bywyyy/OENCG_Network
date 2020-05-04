package com.swu.agentlab.zsnp.conf;

import org.springframework.util.StringUtils;

import java.io.*;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public abstract class BaseConfigBundle {

    protected ResourceBundle commonResource;

    protected ResourceBundle personalResource;

    private String personalResourcePath;

    private String commonResourcePath;

    public BaseConfigBundle(String personalResourcePath, String commonResourcePath){
        this.personalResourcePath = personalResourcePath;
        this.commonResourcePath = commonResourcePath;
        InputStream personalIs = null;
        try {
            personalIs = new FileInputStream(this.personalResourcePath);
            personalResource = new PropertyResourceBundle(new BufferedReader(new InputStreamReader(personalIs , "UTF-8")));
        } catch (IOException e) {
        }
        commonResource = ResourceBundle.getBundle(this.commonResourcePath, Locale.US);
    }

    public String formatString(String key, Object... args){
        String msg = null;
        String pattern;
        try{
            pattern = personalResource.getString(key);
        }catch (MissingResourceException | NullPointerException e){
            pattern = commonResource.getString(key);
        }
        if(!StringUtils.isEmpty(pattern)){
            msg = MessageFormat.format(pattern, args);
        }
        return msg;
    }

    public String getString(String key){
        return this.formatString(key, null);
    }

}
