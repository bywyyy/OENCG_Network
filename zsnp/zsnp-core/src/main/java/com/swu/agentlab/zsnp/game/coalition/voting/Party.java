package com.swu.agentlab.zsnp.game.coalition.voting;

import com.swu.agentlab.zsnp.entity.message.body.offer.Offer;
import com.swu.agentlab.zsnp.entity.player.Agent;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.Role;
import com.swu.agentlab.zsnp.game.RoleInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.*;
import com.swu.agentlab.zsnp.game.coalition.voting.game.Toallyz;
import com.swu.agentlab.zsnp.gui.game.coalition.voting.VotingArena;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;

/**
 * @author JJ.Wu
 */
@Data
public class Party extends Role {

    private int partyNum;

    private int resource;

    private Object lock;

    private int roundNum;

    /**
     * 对角色自身可见，对游戏中其他玩家来说都是0.0
     */
    private double talent;

    private double talpublish;

    public Party(PartyInfo partyInfo) {
        this.partyNum = partyInfo.getPartyNum();
        this.setRoleName(partyInfo.getRoleName());
        this.setResource(partyInfo.getResource());
        this.talent = partyInfo.getTalent();
        this.talpublish = partyInfo.getTalpublish();
    }

    public Party(int partyNum, String name, int resource) {
        this.partyNum = partyNum;
        this.setRoleName(name);
        this.resource = resource;
        this.talent = 0.0;
        this.talpublish = 0.0;
        this.lock = new Object();
    }

    public void loadTalent(String filePath) {
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(new InputStreamReader(VotingDomain.class.getResourceAsStream(filePath)));
            //Document doc = reader.read(new File("D:\\IDEA\\projects\\zsnp\\zsnp-core\\src\\main\\java\\com\\swu\\agentlab\\zsnp\\game\\coalition\\voting\\description\\talents.xml"));
            Node talentNode = doc.selectSingleNode("//talent[@id = " + this.partyNum + "]");
            Element talentEle = ((Element) talentNode);
            this.setTalent(Double.parseDouble(talentNode.getStringValue()));
            this.setTalpublish(Double.parseDouble(talentEle.attributeValue("talpublish")));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void loadTalent(InputStream is) {
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(new InputStreamReader(is));
            Node talentNode = doc.selectSingleNode("//talent[@id = " + this.partyNum + "]");
            Element talentEle = ((Element) talentNode);
            this.setTalent(Double.parseDouble(talentNode.getStringValue()));
            this.setTalpublish(Double.parseDouble(talentEle.attributeValue("talpublish")));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void loadTalent(Document doc) {
        Node talentNode = doc.selectSingleNode("//talent[@id = " + this.partyNum + "]");
        Element talentEle = ((Element) talentNode);
        String genMode = talentEle.attributeValue("gen-mode");
        double talpublish = Double.parseDouble(talentEle.attributeValue("talpublish"));
        double talent = Double.parseDouble(talentNode.getStringValue());
        if (genMode != null) {
            genMode = genMode.trim();
            if ("prototype".equals(genMode)) {
            }
            if ("random-base".equals(genMode)) {
                Random rand = new Random();
                double randNum = rand.nextInt(Math.abs((int) talent));
                talent = talent + randNum;
            }
        }
        this.setTalent(talent);
        this.setTalpublish(talpublish);
    }

    @Override
    public Party clone() {
        return new Party(this.partyNum, this.getRoleName(), this.resource);
    }

    @Override
    public String toString() {
        return this.getRoleName();
    }

    @Override
    public RoleInfo generateRoleInfo() {
        return new PartyInfo(this.getRoleName(), this.partyNum, this.resource, this.talent, this.talpublish);
    }

    @Override
    public Offer proposeOffer() {
        return null;
    }


    /**
     * 接收玩家提交的offer
     *
     * @param offer
     */
    @Override
    public void receiveOffer(Offer offer) {

        /*
        判断当前的player是不是agent，如果是agent调用agent的接受offer的函数
         */
        if (getArena().getAgent() != null) {
            Agent agent = getArena().getAgent();
            agent.receiveOffer(offer);
        }
        /*
        判断offer的类型是不是提交proposal，如果是进行下面的步骤
         */
        if (offer instanceof Proposal) {
            Proposal proposal = (Proposal) offer;
            /*
            提取处proposal中的其他玩家的分配情况
             */
            Map<PlayerInfo, Toallyz> allies = proposal.getAlliesz();
            for (PlayerInfo item : allies.keySet()) {
                /*

                 */
                if (((PartyInfo) item.getRoleInfo()).getPartyNum() == this.partyNum) {
                    ((VotingArena) this.getArena()).setAlly(true);
                    break;
                }

            }
            this.roundNum++;
            //this.getArena().printServerMessage(proposal.getTime(), "Round "+this.roundNum);
        } else if (offer instanceof Response) {

        } else if (offer instanceof Communicate) {

        } else if (offer instanceof SessionEnd) {
            this.roundNum = 0;
        } else if (offer instanceof SessionResult) {
            System.out.println(offer);
            this.roundNum = 0;
        }
    }

    @Override
    public void speak(boolean speakable) {
        //((VotingArena)this.getArena()).setAlly(false);
        ((VotingArena) this.getArena()).setProposer(speakable);
    }
}
