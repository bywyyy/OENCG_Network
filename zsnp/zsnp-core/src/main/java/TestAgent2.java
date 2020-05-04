import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.impl.RandomAgent;

import java.util.Map;
import java.util.Random;

public class TestAgent2 extends VotingAgent {

    private RandomAgent randomAgent;

    private Random rand;

    @Override
    public void init(Game game, Infos own, Map<Integer, Infos> opponents) {
        this.randomAgent = new RandomAgent();
        this.randomAgent.init(game, own, opponents);
        rand = new Random();
    }

    @Override
    public void receiveCommunicate(int proposer, String communicateFree,String communicateType) {

    }

    @Override
    public void receiveProposal(int proposer, Map<Integer, Integer> proposal) {

    }

    @Override
    public void receiveResponse(int responder, boolean agree) {

    }

    @Override
    public Map<Integer, Integer> propose() {
        return randomAgent.propose();
    }

    @Override
    public boolean response() {
        return rand.nextInt(20)>18;
        //return false;
    }

    @Override
    public Communicate Communicate() {
        Communicate communicate = new Communicate();
        communicate.setPlayerInfo(this.getPlayerInfo());

//        平台中的自由语言模块
        communicate.setCommunicateType("hi");
//        平台中的固定语言模块
        communicate.setCommunicateFree("hei");
//        平台中的表情模块
//        不高兴 1  正常 3  高兴 5
        communicate.setEmotion("1");
        return communicate;
    }
}
