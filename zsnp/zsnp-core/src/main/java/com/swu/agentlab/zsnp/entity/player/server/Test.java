package com.swu.agentlab.zsnp.entity.player.server;

import com.swu.agentlab.zsnp.util.IdUtil;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Test {

    @FunctionalInterface
    private static interface A{

        int a = 1;

        void method1();

        default void method2(){

        };

        static void method3(){
            System.out.println(a);
        }

    }

    private static class C{

        private String str;

        static {
            System.out.println("static method of class C");
        }

        public C(){
        }

        public C(String str){
            this.str = str;
        }

        public String getStr(){
            return this.str;
        }

        @Override
        public boolean equals(Object o){
            return o instanceof C? ((C)o).getStr().equals(this.str):false;
        }

        @Override
        public int hashCode(){
            return this.str.hashCode();
        }

    }

    public static void main(String[] args) {
        new C();
    }

}
