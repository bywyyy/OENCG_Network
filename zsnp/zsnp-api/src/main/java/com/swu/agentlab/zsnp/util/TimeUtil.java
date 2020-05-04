package com.swu.agentlab.zsnp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String getHm(){
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        // new Date()为获取当前系统时间
        return df.format(new Date());
    }

    /**
     * 时间格式hh:mm:ss
     * @return
     */
    public static String getHms(){
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        // new Date()为获取当前系统时间
        return df.format(new Date());
    }

    /**
     * 时间格式HHmmssSSS
     * @return
     */
    public static String getHmsSSS(){
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("HHmmssSSS");
        // new Date()为获取当前系统时间
        return df.format(new Date());
    }

    public static String getYmd(){
        /* 设置日期格式 */
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //new Date()为获取当前系统时间
        return df.format(new Date());
    }

    public static String getYmdhms(){
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
        // new Date()为获取当前系统时间
        return df.format(new Date());
    }

    public static String getYmdhm(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");
        return df.format(new Date());
    }

    /**
     * 当前时间比给定时间过去了多少分钟
     * @param hms hh:mm:ss
     * @return
     */
    public static int getMinutesPast(String hms){
        String currTime = TimeUtil.getHms();
        return TimeUtil.getMinutesDuration(hms, currTime);
    }

    public static int getMinutesDuration(String sHms, String dHms){
        String[] currT = dHms.split(":");
        String currH = currT[0];
        String currM = currT[1];
        String pastH = sHms.split(":")[0];
        String pastM = sHms.split(":")[1];
        int durH = Integer.parseInt(currH) - Integer.parseInt(pastH);
        int durM = Integer.parseInt(currM) - Integer.parseInt(pastM);
        return durH*60+durM;
    }

    public static void main(String[] args){
        System.out.println(getHms());
        System.out.println(getYmd());
        System.out.println(getYmdhms());
        System.out.println(getHm());
    }


}
