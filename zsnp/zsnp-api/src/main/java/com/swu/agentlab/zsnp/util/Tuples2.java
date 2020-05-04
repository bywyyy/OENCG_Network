package com.swu.agentlab.zsnp.util;

public class Tuples2<T> {

    private T f;

    private T t;

    public Tuples2(T f, T t){
        this.f = f;
        this.t = t;
    }

    public T get1(){
        return this.f;
    }

    public T get2(){
        return this.t;
    }

}
