package com.swu.agentlab.zsnp.game.coalition.voting;

import com.swu.agentlab.zsnp.entity.domain.Domain;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * @author JJ.Wu
 */
@Data
public class VotingDomain extends Domain implements Serializable, Cloneable {

    private Set<PartyInfo> parties;

    private int majority;

    private Set<Coalition> coalitions;

    private boolean[][] knowledges;

    private int maxSession;

    private int maxRound;

    private double discountFactor;

    private Map<String, Integer> configure = new HashMap<>();

    private boolean newConfigure = false;
    public VotingDomain(){
    }



    public static VotingDomain loadVotingDomain(String stagePath){
        VotingDomain domain = new VotingDomain();
        Set parties = new HashSet();
        Set coalitions = new HashSet();
        InputStream formStream = null;
        InputStream talentsStream = null;
        InputStream coalitionStream = null;
        InputStream knowledgeStream = null;
        DomainStreamLoader domainStreamLoader = new DomainStreamLoader(stagePath);
        try {
            SAXReader reader = new SAXReader();
            //读取parties
            //Document formDoc = reader.read(new File("D:\\IDEA\\projects\\zsnp\\zsnp-core\\src\\main\\java\\com\\swu\\agentlab\\zsnp\\game\\coalition\\voting\\domain-repository\\form.xml"));
            //Document formDoc = reader.read(new InputStreamReader(VotingDomain.class.getResourceAsStream("domain-repository/form.xml")));
            formStream = domainStreamLoader.getForm();
            Document formDoc = reader.read(new InputStreamReader(formStream));
            domain.setName(formDoc.getRootElement().attributeValue("name"));
            Node partiesNode = formDoc.selectSingleNode("//parties[1]");
            List<Node> partyNodes = partiesNode.selectNodes("//party");
            Random rand = new Random();
            talentsStream = domainStreamLoader.getTalents();
            Document talentsDoc = reader.read(new InputStreamReader(talentsStream));
            for(Node item: partyNodes){
                Element partyEle = (Element) item;
                PartyInfo partyInfo = new PartyInfo(partyEle.attributeValue("name"), Integer.parseInt(partyEle.attributeValue("id")), Integer.parseInt(partyEle.attributeValue("resource")), 0.0,0.0);
                //加载每个party的Talent
                Party party = new Party(partyInfo);
                //party.loadTalent("domain-repository/talents.xml");
                party.loadTalent(talentsDoc);
                double tal = party.getTalent();
                double talpublish = party.getTalpublish();
                //int des = rand.nextInt(Math.abs((int)tal));
                //partyInfo.setTalent(party.getTalent());
                partyInfo.setTalent(tal);
                partyInfo.setTalpublish(talpublish);
                parties.add(partyInfo);
            }
            domain.setParties(parties);
            domain.setAmountRoles(parties.size());
            //读取majority
            Node majorityNode = formDoc.selectSingleNode("//requirement/majority");
            Element majorityEle = (Element) majorityNode;
            domain.setMajority(Integer.parseInt(majorityEle.attributeValue("resource")));
            Element sessionEle = (Element) formDoc.selectSingleNode("//game/session");
            int maxSession = 3;
            if(sessionEle != null){
                String sessionNum = sessionEle.attributeValue("max-num");
                if(!StringUtils.isEmpty(sessionNum)){
                    maxSession = Integer.parseInt(sessionNum);
                }
            }
            domain.setMaxSession(maxSession);
            Element roundEle = (Element) formDoc.selectSingleNode("//game/round");
            int maxRound = 9999;
            if(roundEle != null){
                String roundNum = roundEle.attributeValue("max-num");
                if(!StringUtils.isEmpty(roundNum)){
                    maxRound = Integer.parseInt(roundNum);
                }
            }
            domain.setMaxRound(maxRound);
            Element discountEle = (Element) formDoc.selectSingleNode("//game/round/discount");
            double discount = 1.0d;
            if(discountEle != null){
                String discountNum = discountEle.getStringValue();
                if(!StringUtils.isEmpty(discountNum)){
                    discount = Double.parseDouble(discountNum);
                    if(discount < 0.0d && discount > 1.0d){
                        discount = 1.0d;
                    }
                }
            }
            domain.setDiscountFactor(discount);
            //读取coalitions
            //Document coalitionDoc = reader.read(new File("D:\\IDEA\\projects\\zsnp\\zsnp-core\\src\\main\\java\\com\\swu\\agentlab\\zsnp\\game\\coalition\\voting\\domain-repository\\coalitions.xml"));
            //Document coalitionDoc = reader.read(new InputStreamReader(VotingDomain.class.getResourceAsStream("domain-repository/coalitions.xml")));
            coalitionStream = domainStreamLoader.getCoalitions();
            System.out.println(coalitionStream);
            Document coalitionDoc = reader.read(new InputStreamReader(coalitionStream));
            List<Node> coalitionNodes = coalitionDoc.selectNodes("//coalition");
            for(Node item: coalitionNodes){
                Element coalitionEle = (Element) item;
                String[] str_ids = coalitionEle.attributeValue("ids").split(",");
                Set<Integer> ids = new HashSet<>();
                for(int i = 0; i<str_ids.length; i++){
                    ids.add(Integer.parseInt(str_ids[i].trim()));
                }
                Coalition coalition = new Coalition(ids, Integer.parseInt(coalitionEle.attributeValue("resources")), Integer.parseInt(coalitionEle.attributeValue("reward")),Integer.parseInt(coalitionEle.attributeValue("rewardPublish")));
                coalitions.add(coalition);
            }
            domain.setCoalitions(coalitions);
            //读取knowledges
            boolean[][] knowledges = new boolean[parties.size()][parties.size()];
            //Document knowledgesDoc = reader.read(VotingDomain.class.getResourceAsStream("domain-repository/knowledge.xml"));
            knowledgeStream = domainStreamLoader.getKnowledge();
            Document knowledgesDoc = reader.read(knowledgeStream);
            for(int i = 0; i<knowledges.length; i++){
                for(int j = 0; j<knowledges[i].length; j++){
                    //Document knowledgesDoc = reader.read(new File("D:\\IDEA\\projects\\zsnp\\zsnp-core\\src\\main\\java\\com\\swu\\agentlab\\zsnp\\game\\coalition\\voting\\domain-repository\\knowledge.xml"));
                    //System.out.println(VotingDomain.class.getResource("src/main/java/com/swu/agentlab/zsnp/game/coalition/voting/domain-repository/knowledge.xml"));
                    //System.out.println(VotingDomain.class.getResourceAsStream("/com/swu/agentlab/zsnp/game/coalition/voting/domain-repository/knowledge.xml"));
                    //System.out.println(VotingDomain.class.getResourceAsStream("domain-repository/form.xml"));
                    String xpath = "/knowledge-map/knowledge[@id="+i+"]/party[@id="+j+"]";
                    String knowlege = knowledgesDoc.getRootElement().selectSingleNode(xpath).getStringValue();
                    if("true".equals(knowlege)){
                        knowledges[i][j] = true;
                    }else if("false".equals(knowlege)){
                        knowledges[i][j] = false;
                    }
                }
            }
            domain.setKnowledges(knowledges);
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            try {
                if (formStream != null) {
                    formStream.close();
                }
                if(coalitionStream != null){
                    coalitionStream.close();
                }
                if(talentsStream != null){
                    talentsStream.close();
                }
                if(knowledgeStream != null){
                    knowledgeStream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return domain;
    }

    public PartyInfo getByPartyName(String partyName){
        PartyInfo partyInfo = null;
        for(PartyInfo item: parties){
            if(partyName.equals(item.getRoleName())){
                partyInfo = item;
                return partyInfo;
            }
        }
        return partyInfo;
    }

    /**
     * 根据Paryt的Num获取Party
     * @param partyNum
     * @return
     */
    public PartyInfo getPartyByNum(int partyNum){
        PartyInfo partyInfo = null;
        for(PartyInfo item: parties){
            if(item.getPartyNum() == partyNum){
                return item;
            }
        }
        return partyInfo;
    }


    /**
     * 根据partyNum 集合获取Coalition
     * @param partyNums 集合
     * @return
     */
    public Coalition getByPartyNums(Set<Integer> partyNums){
        Coalition coalition = null;
        for(Coalition item: this.getCoalitions()){
            if(partyNums.equals(item.getPartyNums())){
                coalition = item;
                return coalition;
            }
        }
        return coalition;
    }

    public static void main(String[] args) {
        VotingDomain domain = VotingDomain.loadVotingDomain("3Players_weight1-2-2");
    }

}
