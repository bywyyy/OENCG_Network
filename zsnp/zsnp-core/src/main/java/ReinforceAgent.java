import calculate.Calculate_prosocial_aspiration;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;

import java.util.*;

import static calculate.FindMerge.PowerIndex;
import static calculate.FindMerge.find_merge_agent;

/**
 *随机agent实例
 *首先利用自身的初始化方法（init）将游戏的一些信息保存为全局变量，以便实现自己策略的时候使用
 *当接收到玩家“提案”（receiveProposal）或者“对提案的响应”（recieveResponse）之后，将接收到的信息保存为全局变量，以便实现自己策略的时候使用
 *制定自己的提案策略在propose中
 *制定自己的响应策略在response方法中
 *注意：这个示例agent是一个随机策略Agent，并没有用到对手的信息以及玩家发送的提案和响应的相关信息，
 *          它只是一个示例。制定自己的策略的时候可能会用到这些信息。
 * @author JJ.Wu
 * @date 2018/1/19
 */
public class ReinforceAgent extends VotingAgent {

    private Game game;

    private Infos own;

    private Map<Integer, Infos> opponents;

    private Random rand;

    private Map<Integer, Integer> lastProposal;

    private Map<Integer, Boolean> responses = new HashMap<>();

    private Integer aspiration;

    private Float epsilon = 0.5f;
    private Float gamma = 0.5f;


    private Map<Integer, Integer> players_map;
    private Integer quota;

    private String index_type = "Shapley";

    private String communicateFree = "Hello";

    private Set<Integer> players_set = new HashSet<>();

    /** Q 相关变量*/
    private Integer q_r_count = 0;
    private Float q_temp_r = 0.0f;
    private Map<Integer, Integer> old_action = new HashMap<>();
    private Map<Map, Float> q_table = new HashMap<>();



    /**
     * 接收初始的一些消息，包括：游戏信息（公共信息）、自己的信息、对手的信息
     * 该策略中，将这些信息保存为全局变量保存到全局变量，以便稍后使用
     * @param game 游戏的信息（公共信息）
     * @param own 自己的信息
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

        this.quota = game.getMajority();
        this.players_map = new HashMap<>();
        players_map.put(own.getNum(), own.getWeight());
        players_set.add(own.getNum());
        for (Integer p : opponents.keySet()){
            players_map.put(p, opponents.get(p).getWeight());
            players_set.add(p);
        }
    }

    /**
     * 接收玩家发送的联盟提案（也会收到自己提出的提议），
     * 每当有玩家发送联盟提议，都可以通过这个方法获取提议
     * 该策略中，将接收到的提案保存为全局变量，以便实现自己策略的时候参考使用
     * @param proposer 发送提案的玩家的编号
     * @param proposal 提案的内容，包括联盟中每一方的利益分配
     */
    @Override
    public void receiveProposal(int proposer, Map<Integer, Integer> proposal) {
        /**
         * 将玩家发送的提案保存为全局变量
         */

        /** 先判断responses是否都agree,是的话round重新计数
         * 然后再重新初始化responses*/

        if (!responses.isEmpty()){
            Integer all_agree_flag = 0;
            for (Integer m:responses.keySet()){
                if (responses.get(m) == false){
                    all_agree_flag += 1;
                }
            }
            if (all_agree_flag == 0){
                if (lastProposal.get(own.getNum()) != null){
                    q_temp_r += (float)Math.pow(gamma, q_r_count)*lastProposal.get(own.getNum());
                }
            }

        }

        /***/
        if (proposer == own.getNum()){
            //tocheck:null?
            if (!old_action.isEmpty()){
                //need to check
                q_table.put(old_action, q_temp_r);
                System.out.println("q_table.put(old_action, q_temp_r)::::-----" + old_action +q_temp_r);
            }
            old_action = proposal;
            q_temp_r = 0f;
            q_r_count = 0;
        }
        else {
            q_r_count += 1;
        }



        /**重新初始化responses*/
        this.lastProposal = proposal;
        responses = new HashMap<>();
        responses.put(proposer, true);


    }

    /**
     *接收玩家的响应
     * 该策略中，将接收到的响应保存为全局变量，以便实现自己策略的时候参考使用
     * @param responser 响应方的编号
     * @param agree 是否同意进入该联盟
     */
    @Override
    public void receiveResponse(int responser, boolean agree) {
        this.responses.put(responser, agree);

    }

    /**
     * 指定自己的提案策略，分为三步：
     *      1，定义一个Set对象，并将自己的编号添加进Set对象中（因为联盟提案中必须包括提案方自身。
     *          如提案中没有包括提案方自身，会产生运行时异常：非法联盟异常（InvalidCoalitionException））
     *      2，确定你的盟友，并将盟友们的编号添加进Set对象中，
     *          (注意：联盟形成的必要条件是联盟中各方的权重之和大于或者等于majority(majority在game变量中可以获取到)，majority=game.getMajority())
     *          然后通过全局变量game的方法：getRewards，获取到联盟的总资金（game.getRewards(Set对象)）
     *          如果获取到的总资金为0，则表示无法形成该联盟；否则联盟可以形成。
     *      3，为联盟中各方分配资金（包括自己），
     *          将资金放入到一个Map对象中
     *          如，玩家自身编号为1，想与0号玩家结盟；资金分配方案为：自身（1）分60；0号玩家分40。
     *          则可以这样：
     *              map.put(1, 60);
     *              map.put(0, 40);
     *          (注意：资金分配时，各玩家分配到的资金总和必须小于或者等于联盟的总资金(在第2步中通过getRewards方法获得到的总资金)
     *          ，否则会产生运行时异常：非法联盟异常(InvalidCoalitionException))
     * @return Map对象，即联盟资金分配方案
     */
    @Override
    public Map<Integer, Integer> propose() {

        if (Math.random() <= epsilon & !q_table.isEmpty()){
            // need to check 是不是可以中间return
            //find the max value
            Float max_q = 0f;
            for (Map action:q_table.keySet()){
                if (q_table.get(action) >= max_q){
                    max_q = q_table.get(action);
                }
            }
            //find all the actions which have the max value
            ArrayList<Map<Integer, Integer>> maps = new ArrayList<>();
            for (Map action:q_table.keySet()){
                if (q_table.get(action) == max_q){
                    maps.add(action);
                }
            }
            //randomly choose one action
            Map<Integer, Integer> ac = new HashMap<>();
            if(maps.size() >= 1){
                Random random = new Random();
                int random_set_index = random.nextInt(maps.size());
                ac = maps.get(random_set_index);
            }
            else {
                System.out.println("Error <<randomly choose the highest-payoff coalition set>> error!!!!");
            }

            return ac;
        }


        /** 初始化*/
        Map<Integer, Integer> map = new HashMap<>();
        ArrayList<Set<Integer>> sets = new ArrayList<>();

        /**a. choose coalition sets*/
        for (Coalition coa:game.getCoalitions()){
            if (coa.getPartyNums().contains(own.getNum())){
                sets.add(coa.getPartyNums());
            }
        }

        /** b. randomly choose one set*/
        Set<Integer> set = new HashSet<>();
        if(sets.size() >= 1){
            Random random = new Random();
            int random_set_index = random.nextInt(sets.size());
            set = sets.get(random_set_index);
        }
        else {
            System.out.println("Error <<randomly choose the highest-payoff coalition set>> error!!!!");
        }
        System.out.println("DEBUG:: set is" + set);

        /** c. calculate the resources division*/
        /** c1 . calculate the power*/

        Map<Integer, Float> weight_map = new HashMap<>();
        for (Integer player : players_set){
            weight_map.put(player, PowerIndex(players_map, quota, player, index_type));
        }

        /**c2. give the offer*/
        float sum_weight = 0.0f;
        for (Integer p : set){
            sum_weight += weight_map.get(p);
        }
        for (Integer p : set){
            map.put(p, (int)Math.floor( (weight_map.get(p)/sum_weight)*game.getRewards(set) ));
        }


        /**
         *返回map对象
         */
        return map;
    }

    /**
     * 制定自己的响应策略
     * 该策略，返回随机同意或者不同意
     * @return 同意（true）；不同意（false）
     */
    @Override
    public boolean response() {
        if(lastProposal.get(own.getNum()) != null){
            if (lastProposal.get(own.getNum()) >= aspiration){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }

    }

    @Override
    public void receiveCommunicate(int proposer, String communicateFree,String communicateType) {

    }
    @Override
    public Communicate Communicate() {
        Communicate communicate = new Communicate();
        communicate.setPlayerInfo(this.getPlayerInfo());

//        平台中的自由语言模块
        communicate.setCommunicateFree(communicateFree);
//        平台中的固定语言模块
        communicate.setCommunicateType("Let's play as follows");
//        平台中的表情模块
//        不高兴 1  正常 3  高兴 5
        communicate.setEmotion("3");
        return communicate;
    }

}
