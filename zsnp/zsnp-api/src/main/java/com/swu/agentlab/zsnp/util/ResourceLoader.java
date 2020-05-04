package com.swu.agentlab.zsnp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceLoader {

    public static InputStream getFileAsStream(File file) throws FileNotFoundException {
        InputStream is = new FileInputStream(file);
        return is;
    }

    public static InputStream getStreamFromFilePath(String path) throws FileNotFoundException {
        File file = new File(path);
        return getFileAsStream(file);
    }

    public static void main(String[] args){
        File file = new File("zsnp-core/pom.xml");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.exists());
    }

}
