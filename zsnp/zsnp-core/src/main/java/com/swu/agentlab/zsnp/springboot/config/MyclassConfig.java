package com.swu.agentlab.zsnp.springboot.config;

import com.swu.agentlab.zsnp.entity.player.admin.RemoteAdmin;
import com.swu.agentlab.zsnp.service.admin.AdminService;
import com.swu.agentlab.zsnp.springboot.Myclass.Launch;
import com.swu.agentlab.zsnp.springboot.Myclass.WebBridgeToClient;
import com.swu.agentlab.zsnp.springboot.Myclass.WebVotingArena;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.io.IOException;

@Configuration
public class MyclassConfig {

    @Bean
    public WebBridgeToClient webBridgeToClient() {
        return new WebBridgeToClient();
    }

    @Bean
    public Launch launch(){
        return new Launch();
    }

    @Bean
    public WebVotingArena webVotingArena(){
        return new WebVotingArena();
    }



    @Bean
    public ServerEndpointExporter serverEndpointExporter()
    {
        return new ServerEndpointExporter();
    }


}
