package com.swu.agentlab.zsnp.game.coalition.voting.service.impl;

import com.swu.agentlab.zsnp.game.coalition.voting.service.BaseService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseServiceImpl<T> implements BaseService {

    private SqlSession sqlSession;

    public BaseServiceImpl(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:game/coalition/voting/conf/applicationContext.xml");
        SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
        this.sqlSession = factory.openSession();
    }

    @Override
    public void insert(Object o) {
        String table = o.getClass().getSimpleName();
        String mapper = table.toLowerCase()+"Dao.insert"+table;
        sqlSession.insert(mapper, o);
    }

    @Override
    public Object selectById(String id) {

        return null;
    }

    public String getTable(Object o){
        return o.getClass().getSimpleName();
    }
}
