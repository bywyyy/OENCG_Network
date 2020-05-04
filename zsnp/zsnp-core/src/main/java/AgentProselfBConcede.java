import calculate.Calculate_proself_aspiration;
import calculate.Index_Banzhaf;
import calculate.Index_DP;
import calculate.Index_SS;
import com.swu.agentlab.zsnp.entity.player.PlayerInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.PartyInfo;
import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;

import java.util.*;

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
public class AgentProselfBConcede extends VotingAgent {

    private Game game;

    private Infos own;

    private Map<Integer, Infos> opponents;

    private Random rand;

    private Map<Integer, Integer> lastProposal;

    private Map<Integer, Boolean> responses;

    private Integer proposer;

    private int[] betray_record;

    private Integer aspiration;

    private Integer round_count;

    private String signal;
    private Integer signalType;


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
        this.betray_record = new int[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            betray_record[i] = 0;
        }


        this.aspiration = Calculate_proself_aspiration.proself_aspiration(game, own, opponents);
        this.round_count = 0;
        this.responses = new HashMap<>();

    }

    @Override
    public void receiveCommunicate(int proposer, String communicateFree, String communicateType) {

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

        /** 先判断responses是否都agree,是的话round重新计数
         * 然后再重新初始化responses*/
        Integer all_agree_flag = 0;
        for (Integer m:responses.keySet()){
            if (responses.get(m) == false){
                all_agree_flag += 1;
            }
        }
        if (all_agree_flag == 0){
            round_count = 0;
        }
        round_count += 1;

        /**重新初始化responses*/
        responses = new HashMap<>();
        responses.put(proposer, true);

        this.proposer = proposer;


        System.out.print("\r\n" +own.getNum() + "@@@接收提议的相关信息：：：*****：：：提议的人："+ proposer);
        for (Integer m:lastProposal.keySet()){
            System.out.print("\r\n" +own.getNum() + "@@@接收提议的相关信息：：：*****：：：包括的成员："+ m + "分的资金：" + lastProposal.get(m));
        }



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

        for (Integer m:responses.keySet()){
            System.out.print("\r\n" +own.getNum() + "@@@接收是否同意的相关信息：：：*****：：：包括的成员："+ m + "是否同意：" + responses.get(m));
        }
        if (proposer == own.getNum()){
            if (!agree) {
                betray_record[responser] += 1;
            }
        }


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

        float[] norm_banzhaf_power_index = Index_Banzhaf.banzhaf_index(game, own, opponents);
        float[] norm_SSIndex_power_index = Index_SS.ss_index(game, own, opponents);
        float[] norm_DPIndex_power_index = Index_DP.dp_index(game, own, opponents);

        /**三种index，看自己用哪种index的权重最大*/
        float[][] index_three_types = new float[3][game.getAmountOfPlayers()];
        index_three_types[0] = norm_banzhaf_power_index;
        index_three_types[1] = norm_SSIndex_power_index;
        index_three_types[2] = norm_DPIndex_power_index;

        int max_index_type = 0;
        for (int i = 0; i < index_three_types.length; i++){
            if (index_three_types[i][own.getNum()] > index_three_types[max_index_type][own.getNum()]){
                max_index_type = i;
            }
        }
        System.out.print("\r\n" + "哪种index定义标准化后的自身的权重最大：" + max_index_type + "\r\n");


        /**
         * 申明并初始化一个Map对象，作为这个方法的返回值
         */
        Map<Integer, Integer> map = new HashMap<>();
        /**
         * 1，实例化一个Set对象，将自己的编号添加到该Set对象中
         *      一定要将自身的编号添加到Set对象中，否则会报运行时错误
         */
        /**
         * 2，确定你的盟友，并将盟友编号添加到Set中，并确定联盟的总资金
         */


        //search the member who should be punished
        int betray_member = -1;
        int max_betray_count = 0;
        for (int i = 0; i < betray_record.length; i++){
            if (betray_record[i] > max_betray_count){
                betray_member = i;
                max_betray_count = betray_record[i];
            }
        }

        /** signal - add*/
        Set<PlayerInfo> playerInfos = this.getPlayerInfos();

        String name = "";

        for(PlayerInfo playerInfo:playerInfos){

            if( ((PartyInfo)playerInfo.getRoleInfo()).getPartyNum() == betray_member ){
                name = ((PartyInfo)playerInfo.getRoleInfo()).getRoleName();
            }

        }

        if (betray_member == -1){
            signal = "I want as many as resources, the minimal winning coalition is good for us";
            signalType = 1;
        }
        else {
            signal = "Since you didn't ally with me,"+ name +","+ "I will ally with others";
            signalType = 2;
        }
        /** signal - add*/


        /** A. choose all sets that are ok*/
        ArrayList<Set<Integer>> sets = new ArrayList<>();

        if (betray_member != -1) {
            /**a0. calculate the max reward can obtained*/
            float self_max_reward = 0;
            for (Coalition coa : game.getCoalitions()) {
                if ((coa.getPartyNums().contains(own.getNum())) & (!coa.getPartyNums().contains(betray_member))) {
                    int coa_rewards = coa.getRewards();
                    float sum_weight = 0;
                    for (Integer num : coa.getPartyNums()) {
                        sum_weight += index_three_types[max_index_type][num];
                    }
                    float self_reward = coa_rewards * (index_three_types[max_index_type][own.getNum()] / sum_weight);

                    if (self_reward > self_max_reward) {
                        self_max_reward = self_reward;
                    }
                }
            }
            /** a1. choose all sets that are okay*/
            for (Coalition coa:game.getCoalitions()){
                if ((coa.getPartyNums().contains(own.getNum())) & (!coa.getPartyNums().contains(betray_member))){
                    int coa_rewards = coa.getRewards();
                    float sum_weight = 0;
                    for (Integer num: coa.getPartyNums()){
                        sum_weight += index_three_types[max_index_type][num];
                    }
                    float self_reward = coa_rewards*(index_three_types[max_index_type][own.getNum()]/sum_weight);
                    if (self_reward == self_max_reward){
                        sets.add(coa.getPartyNums());
                    }
                }
            }

        }

        if (sets.size() == 0){
            /**a0. calculate the max reward can obtained*/
            float self_max_reward = 0;
            for (Coalition coa:game.getCoalitions()){
                if (coa.getPartyNums().contains(own.getNum())){
                    int coa_rewards = coa.getRewards();
                    float sum_weight = 0;
                    for (Integer num: coa.getPartyNums()){
                        sum_weight += index_three_types[max_index_type][num];
                    }
                    float self_reward = coa_rewards*(index_three_types[max_index_type][own.getNum()]/sum_weight);

                    if (self_reward > self_max_reward){
                        self_max_reward = self_reward;
                    }
                }
            }
            /** a1. choose all sets that are ok*/
            for (Coalition coa:game.getCoalitions()){
                if (coa.getPartyNums().contains(own.getNum())){
                    int coa_rewards = coa.getRewards();
                    float sum_weight = 0;
                    for (Integer num: coa.getPartyNums()){
                        sum_weight += index_three_types[max_index_type][num];
                    }
                    float self_reward = coa_rewards*(index_three_types[max_index_type][own.getNum()]/sum_weight);
                    if (self_reward == self_max_reward){
                        sets.add(coa.getPartyNums());
                    }
                }
            }
        }
        else {
            betray_record[betray_member] -= 1;
        }

        /** B. randomly choose one set*/
        Set<Integer> set = new HashSet<>();
        if(sets.size() >= 1){
            Random random = new Random();
            int random_set_index = random.nextInt(sets.size());
            set = sets.get(random_set_index);
        }
        else {
            System.out.println("Error <<randomly choose the highest-payoff coalition set>> error!!!!");
        }
        /** C. calculate the chosen set's set_sum_weight*/
        float set_sum_weight = 0;
        for (Integer num: set){
            set_sum_weight += index_three_types[max_index_type][num];
        }





        /**
         * 3，分配总资金
         */

        for (Integer num: set){
            map.put( num, (int)Math.floor( game.getRewards(set)*(index_three_types[max_index_type][num]/set_sum_weight) ) );
            System.out.print("\r\n"+"选择的联盟以及资金分配--成员："+ num + "资金：" + (int)Math.floor( game.getRewards(set)*(index_three_types[max_index_type][num]/set_sum_weight) ));
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

        System.out.print("\r\n" + "round_count" + round_count + "\r\n");

        if (lastProposal.get(own.getNum()) != null){
            if (round_count <= 15){
                if (lastProposal.get(own.getNum()) >= aspiration) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                if ( lastProposal.get(own.getNum()) >= (3.0f/4.0f)*aspiration ) {
                    System.out.print("\r\n" + aspiration + "\r\n");
                    return true;
                }
                else {
                    return false;
                }
            }

        }
        else {
            return false;
        }

    }

    @Override
    public Communicate Communicate() {
        Communicate communicate = new Communicate();

        communicate.setPlayerInfo(this.getPlayerInfo());


        if (signalType == 1){
            communicate.setCommunicateType("Let's always play as follows");
            communicate.setEmotion("5");
        }
        if (signalType == 2) {
            communicate.setCommunicateType("Let's play as follows");
            communicate.setEmotion("1");
        }

        communicate.setCommunicateFree(signal);

        return communicate;
    }

}
