package com.swu.agentlab.zsnp.game.coalition.voting.controller;

public interface BaseController<T> {

    void add(T t);

    T getById(String id);

}
