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
public class Agent extends VotingAgent {

    private Game game;

    private Infos own;

    private Map<Integer, Infos> opponents;

    private Random rand;

    private Map<Integer, Integer> lastProposal;

    private Map<Integer, Boolean> responses;

    private Integer aspiration;

    private Float alpha = 1.0f;
    private Float beta = 1.0f;

    private Integer merge_partner = -1;

    private Map<Integer, Integer> players_map;
    private Integer quota;

    private String index_type = "Shapley";

    private String communicateFree;

    private Set<Integer> players_set = new HashSet<>();

    private Integer sum_round = 0;

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
        this.lastProposal = proposal;
        this.responses = new HashMap<>();
        this.responses.put(proposer, true);

        sum_round += 1;
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
//        if (sum_round >= 20){
//            beta = 0.5f;
//        }
//        if (sum_round >= 30){
//            beta = 0.3f;
//        }
//        if (sum_round >= 38){
//            beta = 0.01f;
//        }

        //calculate the merge_partner
        merge_partner = find_merge_agent(players_map, quota, own.getNum(), index_type);

        System.out.println("DEBUG:: merge_partner is" + merge_partner);

        /**a. choose coalition sets*/
        Map<Integer, Integer> map = new HashMap<>();
        ArrayList<Set<Integer>> sets = new ArrayList<>();

        if (merge_partner != -1 & Math.random() <= alpha){
            if (Math.random() <= beta){
                //find the mini size of fitness coalition
                Integer min_size = game.getAmountOfPlayers();
                for (Coalition coa:game.getCoalitions()){
                    if (coa.getPartyNums().contains(own.getNum()) & coa.getPartyNums().contains(merge_partner)){
                        int temp_size = coa.getPartyNums().size();
                        if (temp_size < min_size){
                            min_size = temp_size;
                        }
                    }
                }
                //put the mini-size (contains them) coalitions into sets
                for (Coalition coa:game.getCoalitions()){
                    if (coa.getPartyNums().contains(own.getNum()) &
                            coa.getPartyNums().contains(merge_partner) & coa.getPartyNums().size() == min_size){
                        sets.add(coa.getPartyNums());
                    }
                }
            }
            else {
                //find the grand coalition
                for (Coalition coa:game.getCoalitions()){
                    if (coa.getPartyNums().size() == game.getAmountOfPlayers()){
                        sets.add(coa.getPartyNums());
                    }
                }
            }

        }
        else {
            for (Coalition coa:game.getCoalitions()){
                if (coa.getPartyNums().contains(own.getNum())){
                    sets.add(coa.getPartyNums());
                }
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
        /** c1 . calculate the power (consider the merge operation*/
        // if the coalition contain merge partner
        //calculate the new power and divide payoff
        Map<Integer, Float> weight_map = new HashMap<>();
        for (Integer player : players_set){
            weight_map.put(player, PowerIndex(players_map, quota, player, index_type));
        }
        // change weight_map when they achieve the merge
        System.out.println("DEBUG:: weight_map before is" + weight_map);
        if (merge_partner != -1 & set.contains(merge_partner)){
            /**进行merge，重新形成Game,players_map改变；
             * 具体操作：重新定义一组map, 并且assign原来players_map中的值（除了self和当前player）；
             * id 为self 的字典中，weight填入self和player的weight之和；*/
            Map<Integer, Integer> players_map_new = new HashMap<>();
            for (Integer p:players_map.keySet()){
                if (p != own.getNum() & p != merge_partner){
                    players_map_new.put(p,players_map.get(p));
                }
            }
            players_map_new.put(own.getNum(), players_map.get(own.getNum())+players_map.get(merge_partner));
            System.out.println("DEBUG:: players_map_new  is" + players_map_new);
            //calculate the new power , change weight_map
            float power_self_new = PowerIndex(players_map_new, quota, own.getNum(), index_type);
            float add_benefit = power_self_new - (weight_map.get(own.getNum()) + weight_map.get(merge_partner));
            for (Integer p : players_set){
                if (p != own.getNum() & p != merge_partner){
                    weight_map.put(p, PowerIndex(players_map_new, quota, p, index_type));
                }
            }
            weight_map.put(own.getNum(), weight_map.get(own.getNum()) + (0.5f)*add_benefit);
            weight_map.put(merge_partner, weight_map.get(merge_partner) + (0.5f)*add_benefit);
        }

        System.out.println("DEBUG:: weight_map is" + weight_map);

        //tips : can consider to use history achieved offer
        // (can update the newest or highest offer in history

        /**c2. give the offer*/
        float sum_weight = 0.0f;
        for (Integer p : set){
            sum_weight += weight_map.get(p);
        }
        for (Integer p : set){
            map.put(p, (int)Math.floor( (weight_map.get(p)/sum_weight)*game.getRewards(set) ));
        }

        /** d. add merge signal */
        if (merge_partner != -1 & set.contains(merge_partner)){
            /** mame obtain */
            Set<PlayerInfo> playerInfos = this.getPlayerInfos();

            String name = "";

            for(PlayerInfo playerInfo:playerInfos){

                if( ((PartyInfo)playerInfo.getRoleInfo()).getPartyNum() == merge_partner ){
                    name = ((PartyInfo)playerInfo.getRoleInfo()).getRoleName();
                }

            }
            System.out.println("DEBUG:: name is" + name);
            communicateFree = "When we merge, " + name + ", we can get more benefits";
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
//            if (merge_partner != -1){
//                if (lastProposal.get(merge_partner) != null & lastProposal.get(own.getNum()) >= aspiration){
//                    return true;
//                }
//                else {
//                    return false;
//                }
//            }

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
