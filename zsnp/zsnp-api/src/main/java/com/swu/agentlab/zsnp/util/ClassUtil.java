package com.swu.agentlab.zsnp.util;

import com.sun.deploy.Environment;

import java.io.*;

/**
 * 用来加载类
 * 通常从*.class文件加载类
 * @author JJ.Wu
 * @Date 2018/1/26
 */
public class ClassUtil {

    /**
     *从一个类文件的路径加载一个类
     * @param filePath 类文件的路径
     * @return 类对象
     * @throws ClassNotFoundException
     */
    public static Class loadClassFromFilePath(String filePath) throws ClassNotFoundException {
        ClassLoader loader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                File file = new File(filePath);
                return ClassUtil.loadClassFormFile(file);
            }
        };
        return loader.loadClass(filePath);
    }

    /**
     * 从一个文件（通常为类文件*.class文件）加载一个类
     * @param file 文件（*.class）
     * @return 类对象
     * @throws ClassNotFoundException
     */
    public static Class loadClassFormFile(File file) throws ClassNotFoundException {
        ClassLoader loader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                try {
                    InputStream is = new FileInputStream(file);
                    byte[] b = new byte[is.available()];
                    is.read(b);
                    is.close();
                    Class c = defineClass(null, b, 0, b.length);
                    return c;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        return loader.loadClass("");
    }

}
