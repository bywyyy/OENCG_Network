package com.swu.agentlab.zsnp.game.coalition.voting.xmlrec;

import com.swu.agentlab.zsnp.util.TimeUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameRec {

    private Document doc;

    private int sessionNum;

    private int roundNum;

    private Element sessionElement;

    private Element roundElement;

    private Element proposalElement;

    public GameRec(){
        doc = new DOMDocument();
        this.addGame();
    }

    private void addGame(){
        Element element = new DOMElement("game");
        doc.setRootElement(element);
    }

    public void setRoomInfo(String id, String name, int majority){
        Element element = doc.getRootElement();
        element.addAttribute("id", id);
        element.addAttribute("name", name);
        element.addAttribute("majority", majority+"");
    }

    public void addPartyKnowledge(int partyNum, String roleName, boolean[] knows, int resource, double talent){
        Element element = doc.getRootElement();
        Element knowEle = new DOMElement("knowledge");
        knowEle.addAttribute("party-num", partyNum+"");
        knowEle.addAttribute("role-name", roleName);
        knowEle.addAttribute("resource", resource+"");
        knowEle.addAttribute("talent", talent+"");
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<knows.length; i++){
            if(!"".equals(builder.toString())){
                builder.append(", ");
            }
            builder.append(knows[i]);
        }
        knowEle.addText(builder.toString());
        element.add(knowEle);
    }

    public void addPlayer(String name, String description, String roleName, String type, String path){
        Element playerEle = new DOMElement("player");
        playerEle.addAttribute("name", name);
        playerEle.addAttribute("desc", description);
        playerEle.addAttribute("role-name", roleName);
        playerEle.addAttribute("type", type);
        playerEle.addAttribute("path", path);
        doc.getRootElement().add(playerEle);
    }

    private Element addSession(int sessionNum){
        Element session = new DOMElement("session");
        session.addAttribute("num", sessionNum+"");
        Element game = doc.getRootElement();
        game.add(session);
        return session;
    }

    private Element addRound(int sessionNum, int roundNum) {
        Element sessionElement;
        if(sessionNum == this.sessionNum){
            sessionElement = this.sessionElement;
        }else{
            Node session = doc.selectSingleNode("/game/session[@num=" + sessionNum + "]");
            if(session == null){
                session = this.addSession(sessionNum);
            }
            sessionElement = (Element) session;
        }
        Element round = new DOMElement("round");
        round.addAttribute("num", roundNum+"");
        sessionElement.add(round);
        return round;
    }

    public void addProposal(int sessionNum, int roundNum, String name, String roleName, int reward){
        Element proposalElement;
        Element sessionElement;
        Element roundElement;
        if(sessionNum == this.sessionNum){
            sessionElement = this.sessionElement;
            if(roundNum == this.roundNum){
                roundElement = this.roundElement;
            }else{
                roundElement = (Element) sessionElement.selectSingleNode("/round[@num="+roundNum+"]");
                if(roundElement == null){
                    roundElement = addRound(sessionNum, roundNum);
                }
                this.roundNum = roundNum;
                this.roundElement = roundElement;
            }
        }else{
            sessionElement = (Element) doc.selectSingleNode("/game/session[@num = "+sessionNum+"]");
            if(sessionElement == null){
                sessionElement = addSession(sessionNum);
            }
            roundElement = (Element) sessionElement.selectSingleNode("/round[@num="+roundNum+"]");
            if(roundElement == null){
                roundElement = addRound(sessionNum, roundNum);
            }
            this.sessionNum= sessionNum;
            this.roundNum = roundNum;
            this.sessionElement = sessionElement;
            this.roundElement = roundElement;
        }
        /*String xpath = "/game/session[@num = "+sessionNum+"]/round[@num="+roundNum+"]";
        Node round = doc.selectSingleNode(xpath);
        Element roundEle = (Element) round;
        if(roundEle == null){
            roundEle = addRound(sessionNum, roundNum);
        }*/
        Element proposal = new DOMElement("proposal");
        proposal.addAttribute("name", name);
        proposal.addAttribute("role-name", roleName+"");
        proposal.addAttribute("reward", reward+"");
        roundElement.add(proposal);
        this.proposalElement = proposal;
    }

    public void addAlly(int sessionNum, int roundNum, String allyName, String roleName, int reward){
        Element proposalElement;
        if(sessionNum == this.sessionNum && roundNum == this.roundNum){
            proposalElement = this.proposalElement;
        }else{
            String xpath = "/game/session[@num = "+sessionNum+"]/round[@num="+roundNum+"]/proposal";
            Node proposal = doc.selectSingleNode(xpath);
            proposalElement = (Element) proposal;
        }
        Element ally = new DOMElement("ally");
        ally.addAttribute("name", allyName);
        ally.addAttribute("role-name", roleName);
        ally.addAttribute("reward", reward+"");
        proposalElement.add(ally);
    }

    public synchronized void addResponse(int sessionNum, int roundNum, String name, String roleName, boolean agree){
        Element roundElement;
        if(this.sessionNum == sessionNum&& this.roundNum == roundNum){
            roundElement = this.roundElement;
        }else{
            String xpath = "/game/session[@num = "+sessionNum+"]/round[@num="+roundNum+"]";
            Node round = this.doc.selectSingleNode(xpath);
            roundElement = (Element) round;
            if(roundElement == null){
                roundElement = addRound(sessionNum, roundNum);
            }
            this.sessionNum = sessionNum;
            this.roundNum = roundNum;
            this.roundElement = roundElement;
        }
        Element response = new DOMElement("response");
        response.addAttribute("name", name);
        response.addAttribute("role-name", roleName);
        response.addAttribute("agree", agree+"");
        roundElement.add(response);
    }

    public String toXML(){
        return doc.asXML();
    }

    public void save(){
        String name = doc.getRootElement().attributeValue("name");
        String dir = "log/"+TimeUtil.getYmd();
        File file = new File(dir);
        if(!file.exists()){
            file.mkdirs();
        }
        String appendex = TimeUtil.getHmsSSS();
        try {
            File out = new File(dir+"/"+appendex+"_"+name+".xml");
            FileWriter fileWriter = new FileWriter(out);
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(System.out, format);
            writer.setWriter(fileWriter);
            writer.write(doc);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GameRec gameRec = new GameRec();
        gameRec.setRoomInfo("12356", "room1",4);
        Task task = new Task(gameRec);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Thread thread1 = new Thread(task);
                    thread1.start();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameRec.save();
    }

    private static class Task implements Runnable{

        private GameRec gameRec;

        public Task(GameRec gameRec) {
            this.gameRec = gameRec;
        }

        @Override
        public void run() {
            for(int i = 1; i<=3; i++){
                //gameRec.addSession(i);
                for(int j = 1; j<=3; j++){
                    //gameRec.addRound(i, j);
                    gameRec.addProposal(i, j, "Lily", "Party1", 34);
                    gameRec.addAlly(i, j, "Jack", "Party3", 33);
                    gameRec.addAlly(i, j, "Ken", "Party2", 33);
                    gameRec.addResponse(i, j, "Ken", "Party2", true);
                    gameRec.addResponse(i, j, "Jack", "Party3", false);
                }
            }
        }
    }
}
