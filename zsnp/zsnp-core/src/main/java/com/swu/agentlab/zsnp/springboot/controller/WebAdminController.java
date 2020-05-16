package com.swu.agentlab.zsnp.springboot.controller;

import com.swu.agentlab.zsnp.entity.room.RoomInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.DomainStreamLoader;
import com.swu.agentlab.zsnp.springboot.Myclass.WebBridgeToClient;
import com.swu.agentlab.zsnp.springboot.Myclass.WebVotingArena;
import com.swu.agentlab.zsnp.springboot.entity.DeleteRoomResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
@RequestMapping(value = "")
public class WebAdminController {

    @Autowired
    WebBridgeToClient webBridgeToClient;

    @Autowired
    WebVotingArena webVotingArena;

    public WebAdminController() {
    }

    @RequestMapping(value = "/")
    public String index() {
        return "create_roomNew";
    }

    @RequestMapping(value = "/test2")
    public String test2() {
        return "test2";
    }



    @RequestMapping(value = "/start_player")
    public String start_player(){
        String playerId = webBridgeToClient.start_player();
        return "redirect:/launch?playerId="+playerId;
    }

    @RequestMapping(value = "/launch")
    public String main(){
        return "start_playerNew1";
    }
    @RequestMapping(value = "/test123")
    public String test123(){
//        webVotingArena.send();
        return "test";
    }

    @RequestMapping(value = "/design_configure")
    public String design_configure(){
//        webVotingArena.send();
        return "design_configure";
    }

    @ResponseBody
    @RequestMapping(value = "/deleteRoom")
    public DeleteRoomResult deleteRoom(HttpServletRequest request){
        String roomId= request.getParameter("roomId");
        String result =  webBridgeToClient.deleteRoom(roomId);
        DeleteRoomResult deleteRoomResult = new DeleteRoomResult();
        deleteRoomResult.setResult(result);
        return deleteRoomResult;
    }


    @ResponseBody
    @RequestMapping(value = "/createRoom")
    public Set<RoomInfo> createRoom(HttpServletRequest request) {
        String stagePath= request.getParameter("stagePath");
        webBridgeToClient.createRoom(stagePath);
        Set<RoomInfo> roominfos = webBridgeToClient.getRoomInfos();
       return roominfos;

    }

    @ResponseBody
    @RequestMapping(value = "/stagePath")
    public List<String> stagePath() throws IOException {
        File file = ResourceUtils.getFile("zsnp-core/src/main/resources/domain-repository");
        List<String> stagePath=new ArrayList<>();
        System.out.println(file);
        System.out.println(file.getName());
        File[] files = file.listFiles();

        assert files != null;
        for (File file1 : files) {
            if (file1.isDirectory()) {
                System.out.println(file1.getName());
                stagePath.add(file1.getName());
            }
        }
        System.out.println(stagePath);
        return stagePath;
    }


/*
    @ResponseBody
    @RequestMapping(value = "/createRoom")
    public void createRoom() {
        webBridgeToClient.createRoom();
    }
*/

    @ResponseBody
    @RequestMapping(value = "/getRoomInfos")
    public Set<RoomInfo> getRoomInfos(){

        Set<RoomInfo> roominfos = webBridgeToClient.getAllRoomInfos();

//        Iterator<RoomInfo> it = roominfos.iterator();
//        for(int i=0; i<roominfos.size(); i++){
//            RoomInfo roominfo = it.next();
//            String statue = roominfo.statue.toString();
//            if("GAME_END".equals(statue)){
//                it.remove();
//                i--;
//            }
//        }

        return roominfos;
    }
}
