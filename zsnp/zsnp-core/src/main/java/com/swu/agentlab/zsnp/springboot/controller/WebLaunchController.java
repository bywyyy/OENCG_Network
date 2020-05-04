package com.swu.agentlab.zsnp.springboot.controller;


import com.swu.agentlab.zsnp.springboot.Myclass.Launch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

/**
 * 测试控制器
 *
 * @author: @我没有三颗心脏
 * @create: 2018-05-08-下午 16:46
 */
@Controller
@RequestMapping(value = "")
public class WebLaunchController {


    @Autowired
    Launch launch;


//    @RequestMapping(value = "/roomInfos")
//    public String roomInfos(){
//        Set<RoomInfo> roominfos = webBridgeToClient.roomInfos();
//        System.out.println("this is controller");
//        System.out.print(roominfos);
//        return "index";
//    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public String login(@RequestParam("name")String name, @RequestParam("roomId") String roomId,
                        @RequestParam("desc") String desc,@RequestParam("humanORagent") String humanORagent,@RequestParam("playerId") String playerId,
                        @RequestParam("agentPath") String agentPath){
        if(!agentPath.equals("0")) {
//            String[] agentpath = agentPath.split("\\.");//分割出来的字符数组
//            System.out.println(agentpath[0]);
            launch.btn_ok(playerId, name, desc, roomId, agentPath, humanORagent);
        }else{
            launch.btn_ok(playerId, name, desc, roomId, null, humanORagent);
        }
        return "redirect:/main?playerId="+playerId+"&roomId="+roomId;
    }







    @RequestMapping(value = "/mulitAgent",method = RequestMethod.POST)
    public String mulitAgent(@RequestParam("agentPath") String agentPath,@RequestParam("maxStage") String maxStage,
                             @RequestParam("maxRound") String maxRound){
//        launch.mulitAgent(agentPath1,agentPath2,agentPath3,maxStage,maxRound);'
        System.out.println(agentPath);
        String[] agent =  agentPath.split(",");
        System.out.println(agent[0]);
        System.out.println(agent[1]);
        System.out.println(agent[2]);

        return "redirect:/batchCompetition?agentPath1="+agent[0]+"&agentPath2="+agent[1]+"&agentPath3="+agent[2]+"&maxStage="+maxStage+"&maxRound="+maxRound;
    }

    @RequestMapping(value = "/batchCompetition")
    public String bathCompetition(){
        return "batch_competition";
    }


    @RequestMapping(value = "/main")
    public String main(){
        return "main-v3.0";
    }
}