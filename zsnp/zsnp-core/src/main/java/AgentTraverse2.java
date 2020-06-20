import calculate.Calculate_prosocial_aspiration;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;

import java.util.*;

import static calculate.FindMerge.PowerIndex;

/**
 * 遍历agent实例，三个player收益和为100，遍历所有组合，随即提出offer。用来丰富offer信息，供网络学习
 * 首先利用自身的初始化方法（init）将游戏的一些信息保存为全局变量，以便实现自己策略的时候使用
 * 当接收到玩家“提案”（receiveProposal）或者“对提案的响应”（recieveResponse）之后，将接收到的信息保存为全局变量，以便实现自己策略的时候使用
 * 制定自己的提案策略在propose中
 * 制定自己的响应策略在response方法中
 * 注意：这个示例agent是一个随机策略Agent，并没有用到对手的信息以及玩家发送的提案和响应的相关信息，
 * 它只是一个示例。制定自己的策略的时候可能会用到这些信息。
 */
public class AgentTraverse2 extends VotingAgent {

    private Game game;

    private Infos own;

    private Map<Integer, Infos> opponents;

    private Random rand;

    private Map<Integer, Integer> lastProposal = new HashMap<>();

    private Map<Integer, Boolean> responses;

    private Integer aspiration;

    private Float alpha = 0.9f;
    private Float beta = 0.8f;

    private Integer merge_partner = -1;

    private Map<Integer, Integer> players_map;
    private Integer quota;

    private String index_type = "Shapley";

    private Set<Integer> players_set = new HashSet<>();

    private Map<Integer, Integer> merge_request;

    private List<Integer> history_achieved_bonus = new ArrayList<>();
    private Integer offer_price = 0;
    private List<int[]> offerListInit = new ArrayList<>();
    private Integer sum_round = 0;


    /**
     * 接收初始的一些消息，包括：游戏信息（公共信息）、自己的信息、对手的信息
     * 该策略中，将这些信息保存为全局变量保存到全局变量，以便稍后使用
     *
     * @param game      游戏的信息（公共信息）
     * @param own       自己的信息
     * @param opponents 对手的信息，为一个映射类，
     */
    @Override
    public void init(Game game, Infos own, Map<Integer, Infos> opponents) {
        /**
         * 将游戏信息、自己的信息、和对手的信息
         */
        this.game = game;
        this.own = own;
        this.opponents = opponents;
        /**
         * （示例用的，不是必须的对象）
         * 随机对象
         */
        this.rand = new Random();

        this.aspiration = Calculate_prosocial_aspiration.prosocial_aspiration(game, own, opponents);
        this.responses = new HashMap<>();

        this.quota = game.getMajority();
        players_map = new HashMap<>();
        players_map.put(own.getNum(), own.getWeight());
        players_set.add(own.getNum());
        for (Integer p : opponents.keySet()) {
            players_map.put(p, opponents.get(p).getWeight());
            players_set.add(p);
        }

        //offerlist初始化
        for (int player1 = 0; player1 < 100; player1 += 5) {
            for (int player2 = 0; player2 < 100 - player1 + 1; player2 += 5) {
                int player3 = 100 - player1 - player2;
                if (player2 == 100 || player3 == 100)
                    continue;
                int[] offer = {player1, player2, player3};
                if (offer[own.getNum()] == 0) //联盟不包括自己
                    continue;
                int majoSum = 0;//联盟中玩家的权重大于阈值
                for (int l = 0; l < 3; l++) {
                    if (offer[l] > 0)
                        majoSum += players_map.get(l);
                }

                if (majoSum >= quota)
                    offerListInit.add(offer);
            }
        }
    }

    /**
     * 接收玩家发送的联盟提案（也会收到自己提出的提议），
     * 每当有玩家发送联盟提议，都可以通过这个方法获取提议
     * 该策略中，将接收到的提案保存为全局变量，以便实现自己策略的时候参考使用
     *
     * @param proposer 发送提案的玩家的编号
     * @param proposal 提案的内容，包括联盟中每一方的利益分配
     */
    @Override
    public void receiveProposal(int proposer, Map<Integer, Integer> proposal) {
        /**
         * 将玩家发送的提案保存为全局变量
         */


        if (lastProposal.get(own.getNum()) != null) {
            offer_price = lastProposal.get(own.getNum());
        } else {
            offer_price = 0;
        }
        /** 先判断responses是否都agree,是的话round重新计数
         * 然后再重新初始化responses*/
        Integer all_agree_flag = 0;
        for (Integer m : responses.keySet()) {
            if (responses.get(m) == false) {
                all_agree_flag += 1;
            }
        }
        if (all_agree_flag == 0 & lastProposal.get(own.getNum()) != null) {
            history_achieved_bonus.add(lastProposal.get(own.getNum()));
        }


        /**重新初始化responses*/
        this.lastProposal = proposal;
        responses = new HashMap<>();
        responses.put(proposer, true);


        System.out.print("\r\n" + own.getNum() + "@@@接收提议的相关信息：：：*****：：：提议的人：" + proposer);
        for (Integer m : lastProposal.keySet()) {
            System.out.print("\r\n" + own.getNum() + "@@@接收提议的相关信息：：：*****：：：包括的成员：" + m + "分的资金：" + lastProposal.get(m));
        }

        sum_round += 1;
    }

    /**
     * 接收玩家的响应
     * 该策略中，将接收到的响应保存为全局变量，以便实现自己策略的时候参考使用
     *
     * @param responser 响应方的编号
     * @param agree     是否同意进入该联盟
     */
    @Override
    public void receiveResponse(int responser, boolean agree) {

        this.responses.put(responser, agree);

    }

    /**
     * 指定自己的提案策略，分为三步：
     * 1，定义一个Set对象，并将自己的编号添加进Set对象中（因为联盟提案中必须包括提案方自身。
     * 如提案中没有包括提案方自身，会产生运行时异常：非法联盟异常（InvalidCoalitionException））
     * 2，确定你的盟友，并将盟友们的编号添加进Set对象中，
     * (注意：联盟形成的必要条件是联盟中各方的权重之和大于或者等于majority(majority在game变量中可以获取到)，majority=game.getMajority())
     * 然后通过全局变量game的方法：getRewards，获取到联盟的总资金（game.getRewards(Set对象)）
     * 如果获取到的总资金为0，则表示无法形成该联盟；否则联盟可以形成。
     * 3，为联盟中各方分配资金（包括自己），
     * 将资金放入到一个Map对象中
     * 如，玩家自身编号为1，想与0号玩家结盟；资金分配方案为：自身（1）分60；0号玩家分40。
     * 则可以这样：
     * map.put(1, 60);
     * map.put(0, 40);
     * (注意：资金分配时，各玩家分配到的资金总和必须小于或者等于联盟的总资金(在第2步中通过getRewards方法获得到的总资金)
     * ，否则会产生运行时异常：非法联盟异常(InvalidCoalitionException))
     *
     * @return Map对象，即联盟资金分配方案
     */
    @Override
    public Map<Integer, Integer> propose() {
//        //更新 参数
//        if (sum_round >= 20){
//            alpha = 0.8f;
//            beta = 0.5f;
//        }
//        if (sum_round >= 30){
//            alpha = 0.9f;
//            beta = 0.3f;
//        }
//        if (sum_round >= 38){
//            alpha = 0.9f;
//            beta = 0.01f;
//        }

        //update "merge" flag
        Map<Integer, Integer> map = new HashMap<>();

        int offerListLen = offerListInit.size();
        Random random = new Random();
        int ran1 = random.nextInt(offerListLen);
        int[] offer = offerListInit.get(ran1);
        for (int l = 0; l < 3; l++) {
            if (offer[l] > 0)
                map.put(l, offer[l]);
        }

        //tips : can consider to use history achieved offer
        // (can update the newest or highest offer in history


        /**
         *返回map对象
         */
        return map;
    }

    /**
     * 制定自己的响应策略
     * 该策略，返回随机同意或者不同意
     *
     * @return 同意（true）；不同意（false）
     */
    @Override
    public boolean response() {

        /**a . 处理merge请求, update merge_partner*/
        // if <have merge_request>
        //tips : 设置一个achieved payoff record，记录下达成的offer
        // ( 可以把历史的都记下来，也可以记住一段回合区域的，也可以记住最高的，也可以记住最新的
        //if <have merge_partner> 如果更高就更换（可加概率）
        // else <no merge_partner> 高了，就接受 update merge_partner
        if (!merge_request.isEmpty()) {
            for (Integer key : merge_request.keySet()) {
                if (merge_request.get(key) >= Collections.max(history_achieved_bonus)) {
                    merge_partner = key;
                }
            }
        }


        /**b . 正常处理offer*/
        if (lastProposal.get(own.getNum()) != null) {
            if (merge_partner != -1 & Math.random() <= 0.9) {
                if (lastProposal.get(merge_partner) != null & lastProposal.get(own.getNum()) >= aspiration) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (lastProposal.get(own.getNum()) >= aspiration) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }

    }

    @Override
    public void receiveCommunicate(int proposer, String communicateFree, String communicateType) {
        merge_request = new HashMap<>();
        /** mame obtain */
        PlayerInfo playerInfo = this.getPlayerInfo();

        String name = "";

//        if( ((PartyInfo)playerInfo.getRoleInfo()).getPartyNum() == own.getNum() ){
//            name = ((PartyInfo)playerInfo.getRoleInfo()).getRoleName();
//        }

        name = ((PartyInfo) playerInfo.getRoleInfo()).getRoleName();


        if (communicateFree.contains("merge")
                & communicateFree.contains(name)
                & offer_price != 0) {
            merge_request.put(proposer, offer_price);
            System.out.println("DEBUG:: proposer is" + proposer);
            System.out.println("DEBUG:: name is" + name);
            System.out.println("DEBUG:: offer_price is" + offer_price);
        }
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
