package com.swu.agentlab.zsnp.game.coalition.voting;

import com.swu.agentlab.zsnp.game.coalition.voting.entity.player.Player;
import com.swu.agentlab.zsnp.util.IdUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:game/coalition/voting/conf/applicationContext.xml");
        SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
        SqlSession session = factory.openSession();
        log.info(session.selectOne("playerDao.getPlayerById", "1234567891234567"));
        //log.info(session.selectOne("com.swu.agentlab.zsnp.game.coalition.voting.dao.player.PlayerDao.getPlayerById", "1234567891234567"));
        /*Player player = new Player();
        player.setId(IdUtil.generate16HexId());
        player.setRoomId(IdUtil.generate16HexId());
        player.setName("王五");
        player.setPartyNum(3);
        player.setDescription("学号");*/
        //session.insert("playerDao.insertPlayer", player);
        //log.info(session.selectOne(session.selectOne("roomDao.selectRoomById", "123")));
    }

}
