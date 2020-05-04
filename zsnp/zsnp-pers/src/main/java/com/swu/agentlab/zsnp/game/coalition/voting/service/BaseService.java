package com.swu.agentlab.zsnp.game.coalition.voting.service;

public interface BaseService<T> {

    void insert (T t);

    T selectById(String id);

}
