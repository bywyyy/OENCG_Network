
package com.swu.agentlab.zsnp.springboot;

import com.swu.agentlab.zsnp.entity.player.server.MyServer;
import com.swu.agentlab.zsnp.entity.player.server.Server;
import com.swu.agentlab.zsnp.springboot.Myclass.WebVotingArena;
import com.swu.agentlab.zsnp.springboot.controller.MyBatch;
import com.swu.agentlab.zsnp.springboot.controller.MyRoomWebSocket;
import com.swu.agentlab.zsnp.springboot.controller.MyWebSocket;
import lombok.Data;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


import java.lang.reflect.Method;

@Data
@SpringBootApplication
public class ZsnpApplication {

	public static MyServer server;
	//服务器入口
	public static void main(String[] args) {
		server = new MyServer(1234);
		server.start();
        SpringApplication springApplication = new SpringApplication(ZsnpApplication.class);
        ConfigurableApplicationContext configurableApplicationContext = springApplication.run(args);
        MyWebSocket.setApplicationContext(configurableApplicationContext);
		MyRoomWebSocket.setApplicationContext(configurableApplicationContext);
		MyBatch.setApplicationContext(configurableApplicationContext);
	}
}
