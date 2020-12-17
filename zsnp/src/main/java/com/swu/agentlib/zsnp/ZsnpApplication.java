
package com.swu.agentlib.zsnp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.Socket;

//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;

@SpringBootApplication
public class ZsnpApplication {
	//服务器入口
	public static void main(String[] args) {

	    System.out.println("abc");
        SpringApplication.run(ZsnpApplication.class, args);
	}
}
