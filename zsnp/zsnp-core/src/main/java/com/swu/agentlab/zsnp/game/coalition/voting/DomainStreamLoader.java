package com.swu.agentlab.zsnp.game.coalition.voting;

import com.swu.agentlab.zsnp.util.ResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DomainStreamLoader {

    private static String basePath = "domain-repository";

    private static String internalPath = "/domain-repository";

    private boolean externalResourceExist;

    private String stagePath="";
    private String[] requiredFiles = {
            "form",
            "talents",
            "coalitions",
            "knowledge",
    };

    public DomainStreamLoader(String stagePath) {
        externalResourceExist = true;
        this.stagePath=stagePath;
        for(int i = 0; i < requiredFiles.length; i++){
            File file = new File(basePath+"/" +stagePath+"/"+ requiredFiles[i] + ".xml");
            if(!file.exists()){
                externalResourceExist = false;
                System.out.println("exist");
                break;
            }
        }
    }

    public InputStream getForm(){
        return this.getInputStream("form");
    }

    public InputStream getCoalitions(){
        return this.getInputStream("coalitions");
    }

    public InputStream getKnowledge(){
        return this.getInputStream("knowledge");
    }

    public InputStream getTalents(){
        return this.getInputStream("talents");
    }

    private InputStream getInputStream(String fileName){
        InputStream is = null;
        if(externalResourceExist){
            File file = new File(basePath+"/"+fileName + ".xml");
            try {
                is = ResourceLoader.getFileAsStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            is = DomainStreamLoader.class.getResourceAsStream(internalPath + "/"+ stagePath+"/"  + fileName+".xml");
        }
        return is;
    }
}
