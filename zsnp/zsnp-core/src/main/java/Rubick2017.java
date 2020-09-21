//import calculate.Calculate_proself_aspiration;
//import calculate.Index_Banzhaf;
//import calculate.Index_DP;
//import calculate.Index_SS;
//import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
//import com.swu.agentlab.zsnp.game.coalition.voting.Message.body.Communicate;
//import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
//import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;
//import com.swu.agentlab.zsnp.game.coalition.voting.agent.VotingAgent;
//
//import java.util.*;
//
///**
// * ANAC2017RubickAgent实例
// * 首先利用自身的初始化方法（init）将游戏的一些信息保存为全局变量，以便实现自己策略的时候使用
// * 当接收到玩家“提案”（receiveProposal）或者“对提案的响应”（recieveResponse）之后，将接收到的信息保存为全局变量，以便实现自己策略的时候使用
// * 制定自己的提案策略在propose中
// * 制定自己的响应策略在response方法中
// * 注意：这个示例agent是一个随机策略Agent，并没有用到对手的信息以及玩家发送的提案和响应的相关信息，
// * 它只是一个示例。制定自己的策略的时候可能会用到这些信息。
// */
//public class Rubick2017 extends VotingAgent {
//
//    private Game game;
//    private Infos own;
//    private Map<Integer, Infos> opponents;
//    private Random rand;
//    private Map<Integer, Integer> lastProposal;
//    private Map<Integer, Boolean> responses;
//
//    private Integer aspiration;
////    private Integer round = 0;
//
//    //    private Bid lastReceivedBid = null;
//    private StandardInfoList history;
//    private LinkedList<String> parties = new LinkedList<>();
//    private LinkedList<LinkedList<Double>> histOpp0 = new LinkedList<>();
//    private LinkedList<LinkedList<Double>> histOpp1 = new LinkedList<>();
//    private boolean isHistoryAnalyzed = false;
//    private int numberOfReceivedOffer = 0;
//    private LinkedList<Integer> profileOrder = new LinkedList<>();
//    private String[] opponentNames = new String[2];
//    private double[] acceptanceLimits = {0.0, 0.0};
//    private double maxReceivedBidutil = 0.0;
//    private int lastpartyID = 0;
//    private SortedOutcomeSpace sos = null;
//    private LinkedList<Map<Integer, Integer>> bestAcceptedBids = new LinkedList<>();
//    private double threshold = 0;
//
//    protected LinkedHashMap<Integer, Integer> frequentValuesList0 = new LinkedHashMap<Integer, Integer>();
//    protected LinkedHashMap<Integer, Integer> frequentValuesList1 = new LinkedHashMap<Integer, Integer>();
//    protected LinkedHashMap<Integer, Integer> frequentValuesList2 = new LinkedHashMap<Integer, Integer>();
//
//    ArrayList<Integer> opp0bag = new ArrayList<Integer>();
//    ArrayList<Integer> opp1bag = new ArrayList<Integer>();
//    ArrayList<Integer> opp2bag = new ArrayList<Integer>();
//
//
//    /**
//     * 接收初始的一些消息，包括：游戏信息（公共信息）、自己的信息、对手的信息
//     * 该策略中，将这些信息保存为全局变量保存到全局变量，以便稍后使用
//     *
//     * @param game      游戏的信息（公共信息）
//     * @param own       自己的信息
//     * @param opponents 对手的信息，为一个映射类，
//     */
//    @Override
//    public void init(Game game, Infos own, Map<Integer, Infos> opponents) {
//        /**
//         * 将游戏信息、自己的信息、和对手的信息
//         */
//        this.game = game;
//        this.own = own;
//        this.opponents = opponents;
//        /**
//         * （示例用的，不是必须的对象）
//         * 随机对象
//         */
//        this.rand = new Random();
//
////        this.aspiration = Calculate_proself_aspiration.proself_aspiration(game, own, opponents);
////        this.asp = Calculate_proself_aspiration.proself_aspiration(game, own, opponents);
//
//
//        //======================================================
//        sos = new SortedOutcomeSpace(utilitySpace);//allbids
//        history = (StandardInfoList) getData().get();
//
////        sortPartyProfiles(getPartyId().toString());
////        threshold = getUtilitySpace().getReservationValue();
//        threshold = Calculate_proself_aspiration.proself_aspiration(game, own, opponents);
//        maxReceivedBidutil = threshold; // to see the logic
//
//    }
//
//    @Override
//    public void receiveCommunicate(int proposer, String communicateFree, String communicateType) {
//
//    }
//
//    /**
//     * 接收玩家发送的联盟提案（也会收到自己提出的提议），
//     * 每当有玩家发送联盟提议，都可以通过这个方法获取提议
//     * 该策略中，将接收到的提案保存为全局变量，以便实现自己策略的时候参考使用
//     *
//     * @param proposer 发送提案的玩家的编号
//     * @param proposal 提案的内容，包括联盟中每一方的利益分配
//     */
//    @Override
//    public void receiveProposal(int proposer, Map<Integer, Integer> proposal) {
//        /**
//         * 将玩家发送的提案保存为全局变量
//         */
//        this.lastProposal = proposal;  //lastReceivedBid
//        this.responses = new HashMap<>();
//        this.responses.put(proposer, true);
//
//
//        System.out.print("\r\n" + own.getNum() + "@@@接收提议的相关信息：：：*****：：：提议的人：" + proposer);
//        for (Integer m : lastProposal.keySet()) {
//            System.out.print("\r\n" + own.getNum() + "@@@接收提议的相关信息：：：*****：：：包括的成员：" + m + "分的资金：" + lastProposal.get(m));
//        }
//
//
//        //===============
//
//        if (maxReceivedBidutil < lastProposal.get(own.getNum()))
//            maxReceivedBidutil = lastProposal.get(own.getNum()) * 0.95;
//
//        numberOfReceivedOffer++;
//
//        lastpartyID = proposer;
//
//        BidResolver(lastProposal, proposer);
//
//        if (parties.size() == 3 && !history.isEmpty() && isHistoryAnalyzed == false) {
//            // System.out.println("about to analyze: ");
//            analyzeHistory();
//        }
//    }
//
//
//    /**
//     * 接收玩家的响应
//     * 该策略中，将接收到的响应保存为全局变量，以便实现自己策略的时候参考使用
//     *
//     * @param responser 响应方的编号
//     * @param agree     是否同意进入该联盟
//     */
//    @Override
//    public void receiveResponse(int responser, boolean agree) {
//        this.responses.put(responser, agree);
//
//        boolean coalition = true;
//        for (Integer m : responses.keySet()) {
//            System.out.print("\r\n" + own.getNum() + "@@@接收是否同意的相关信息：：：*****：：：包括的成员：" + m + "是否同意：" + responses.get(m));
//
//            if (!responses.get(m))
//                coalition = false;
//        }
//
//        if (coalition && acceptedBid.get(own.getNum()) != null) {
//
//            Map<Integer, Integer> acceptedBid = lastProposal;
//
//            if (bestAcceptedBids.isEmpty()) {
//                bestAcceptedBids.add(acceptedBid);
//            } else if (!bestAcceptedBids.contains(acceptedBid)) {
//                int size = bestAcceptedBids.size();
//                for (int i = 0; i < size; i++) {
//                    if (acceptedBid.get(own.getNum()) > bestAcceptedBids.get(i).get(own.getNum())) {
//                        bestAcceptedBids.add(i, acceptedBid); // collect best accepted Bids.
//                        break;
//                    } else if (i == bestAcceptedBids.size() - 1) { // if new accepted bid has the least util in the list.
//                        bestAcceptedBids.add(acceptedBid);
//                    }
//                }
//            }
//        }
//
//    }
//
//    /**
//     * 指定自己的提案策略，分为三步：
//     * 1，定义一个Set对象，并将自己的编号添加进Set对象中（因为联盟提案中必须包括提案方自身。
//     * 如提案中没有包括提案方自身，会产生运行时异常：非法联盟异常（InvalidCoalitionException））
//     * 2，确定你的盟友，并将盟友们的编号添加进Set对象中，
//     * (注意：联盟形成的必要条件是联盟中各方的权重之和大于或者等于majority(majority在game变量中可以获取到)，majority=game.getMajority())
//     * 然后通过全局变量game的方法：getRewards，获取到联盟的总资金（game.getRewards(Set对象)）
//     * 如果获取到的总资金为0，则表示无法形成该联盟；否则联盟可以形成。
//     * 3，为联盟中各方分配资金（包括自己），
//     * 将资金放入到一个Map对象中
//     * 如，玩家自身编号为1，想与0号玩家结盟；资金分配方案为：自身（1）分60；0号玩家分40。
//     * 则可以这样：
//     * map.put(1, 60);
//     * map.put(0, 40);
//     * (注意：资金分配时，各玩家分配到的资金总和必须小于或者等于联盟的总资金(在第2步中通过getRewards方法获得到的总资金)
//     * ，否则会产生运行时异常：非法联盟异常(InvalidCoalitionException))
//     *
//     * @return Map对象，即联盟资金分配方案
//     */
//    @Override
//    public Map<Integer, Integer> propose() {
//        double decisiveUtil = checkAcceptance();
//
//        Map<Integer, Integer> bid = generateBid(decisiveUtil);
//        return new Offer(getPartyId(), bid);
//
//
//        int proRound = super.roundNum;
//        int ts1 = (int) (game.getMaxRoundNum() / 3) + 1;
//        int ts2 = (int) (game.getMaxRoundNum() / 3) + 2;
//        int ts3 = (int) (game.getMaxRoundNum() / 3) + 3;
//        int ts4 = (int) (game.getMaxRoundNum() / 3 * 2) + 1;
//        int ts5 = (int) (game.getMaxRoundNum() / 3 * 2) + 2;
//        int ts6 = (int) (game.getMaxRoundNum() / 3 * 2) + 3;
//
//        if (proRound <= 3 && aspiration == asp) {
//            aspiration += 20;
//        } else if (proRound == ts1 || proRound == ts2 || proRound == ts3 || proRound == ts4 || proRound == ts5 || proRound == ts6) {
//            aspiration -= 10;
//        }
//
//        float[] norm_banzhaf_power_index = Index_Banzhaf.banzhaf_index(game, own, opponents);
//        float[] norm_SSIndex_power_index = Index_SS.ss_index(game, own, opponents);
//        float[] norm_DPIndex_power_index = Index_DP.dp_index(game, own, opponents);
//
//        /**三种index，看自己用哪种index的权重最大*/
//        float[][] index_three_types = new float[3][game.getAmountOfPlayers()];
//        index_three_types[0] = norm_banzhaf_power_index;
//        index_three_types[1] = norm_SSIndex_power_index;
//        index_three_types[2] = norm_DPIndex_power_index;
//
//        int max_index_type = 0;
//        int min_index_type = 0;
//        for (int i = 0; i < index_three_types.length; i++) {
//            if (index_three_types[i][own.getNum()] > index_three_types[max_index_type][own.getNum()]) {
//                max_index_type = i;
//            } else if (index_three_types[i][own.getNum()] < index_three_types[min_index_type][own.getNum()]) {
//                min_index_type = i;
//            }
//        }
//
//        int mid_index_type = 3 - max_index_type - min_index_type;
//        System.out.print("\r\n" + "哪种index定义标准化后的自身的权重最大：" + max_index_type + "\r\n");
//
//
//        /**
//         * 申明并初始化一个Map对象，作为这个方法的返回值
//         */
//
//        /**
//         * 1，实例化一个Set对象，将自己的编号添加到该Set对象中
//         *      一定要将自身的编号添加到Set对象中，否则会报运行时错误
//         */
//        /**
//         * 2，确定你的盟友，并将盟友编号添加到Set中，并确定联盟的总资金
//         */
//
//        /**a0. calculate the max reward can obtained*/
//        float self_max_reward = 0;
//        float self_mid_reward = 0;
//        float self_min_reward = 0;
//        for (Coalition coa : game.getCoalitions()) {
//            if (coa.getPartyNums().contains(own.getNum())) {
//                int coa_rewards = coa.getRewards();
//                float sum_weight_max = 0;
//                float sum_weight_mid = 0;
//                float sum_weight_min = 0;
//
//                for (Integer num : coa.getPartyNums()) {
//                    sum_weight_max += index_three_types[max_index_type][num];
//                    sum_weight_mid += index_three_types[mid_index_type][num];
//                    sum_weight_min += index_three_types[min_index_type][num];
//                }
//                float self_reward_max = coa_rewards * (index_three_types[max_index_type][own.getNum()] / sum_weight_max);
//                float self_reward_mid = coa_rewards * (index_three_types[max_index_type][own.getNum()] / sum_weight_mid);
//                float self_reward_min = coa_rewards * (index_three_types[max_index_type][own.getNum()] / sum_weight_min);
//
//                if (self_reward_max > self_max_reward) {
//                    self_max_reward = self_reward_max;
//                }
//                if (self_reward_mid > self_mid_reward) {
//                    self_mid_reward = self_reward_mid;
//                }
//                if (self_reward_min > self_min_reward) {
//                    self_min_reward = self_reward_min;
//                }
//            }
//        }
//
//        //2.17 add code , randomly choose the highest-payoff coalition set
//        /** a. choose all sets that are ok*/
//        ArrayList<Set<Integer>> sets_max = new ArrayList<>();
//        ArrayList<Set<Integer>> sets_mid = new ArrayList<>();
//        ArrayList<Set<Integer>> sets_min = new ArrayList<>();
//        for (Coalition coa : game.getCoalitions()) {
//            if (coa.getPartyNums().contains(own.getNum())) {
//                int coa_rewards = coa.getRewards();
//                float sum_weight_max = 0;
//                float sum_weight_mid = 0;
//                float sum_weight_min = 0;
//                for (Integer num : coa.getPartyNums()) {
//                    sum_weight_max += index_three_types[max_index_type][num];
//                    sum_weight_mid += index_three_types[mid_index_type][num];
//                    sum_weight_min += index_three_types[min_index_type][num];
//                }
//                float self_reward_max = coa_rewards * (index_three_types[max_index_type][own.getNum()] / sum_weight_max);
//                float self_reward_mid = coa_rewards * (index_three_types[max_index_type][own.getNum()] / sum_weight_mid);
//                float self_reward_min = coa_rewards * (index_three_types[max_index_type][own.getNum()] / sum_weight_min);
//
//                if (self_reward_max == self_max_reward) {
//                    sets_max.add(coa.getPartyNums());
//                }
//                if (self_reward_mid == self_mid_reward) {
//                    sets_mid.add(coa.getPartyNums());
//                }
//                if (self_reward_min == self_min_reward) {
//                    sets_min.add(coa.getPartyNums());
//                }
//            }
//        }
//
//        Map<Integer, Integer> map = new HashMap<>();
//
//        if (super.roundNum < game.getMaxRoundNum() / 3) {
//            Set<Integer> set = new HashSet<>();
//            if (sets_max.size() >= 1) {
//                Random random = new Random();
//                int random_set_index = random.nextInt(sets_max.size());
//                set = sets_max.get(random_set_index);
//            } else {
//                System.out.println("Error <<randomly choose the highest-payoff coalition set>> error!!!!");
//            }
//            /** c. calculate the chosen set's set_sum_weight*/
//            float set_sum_weight = 0;
//            for (Integer num : set) {
//                set_sum_weight += index_three_types[max_index_type][num];
//            }
//
//            /**
//             * 3，分配总资金
//             */
//            for (Integer num : set) {
//                map.put(num, (int) Math.floor(game.getRewards(set) * (index_three_types[max_index_type][num] / set_sum_weight)));
//                System.out.print("\r\n" + "选择的联盟以及资金分配--成员：" + num + "资金：" + (int) Math.floor(game.getRewards(set) * (index_three_types[max_index_type][num] / set_sum_weight)));
//            }
//        }
////        this.aspiration = (int)Math.floor( game.getRewards(set)*(index_three_types[max_index_type][own.getNum()]/set_sum_weight) );
//        /**
//         *返回map对象
//         */
//        return map;
//    }
//
//
//    /**
//     * 制定自己的响应策略
//     * 该策略，返回随机同意或者不同意
//     *
//     * @return 同意（true）；不同意（false）
//     */
//    @Override
//    public boolean response() {
//        double decisiveUtil = checkAcceptance();
//
//        if (lastProposal.get(own.getNum()) != null) {
//            if (decisiveUtil == -1) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//
//    }
//
//
//    private int takeTheChance(double maxReceived) {
//
//        int pow = 1;
//        double chance = rand.nextDouble();
//        // System.out.println("chance: " + chance);
//        if (chance > 0.95 + 0.05 * maxReceived)
//            pow = 2;
//        else if (chance > 0.93 + 0.07 * maxReceived)
//            pow = 3;
//        else
//            pow = 10;
//
//        return pow;
//    }
//
//
//    private double checkAcceptance() {
//        double time = super.roundNum / game.getMaxRoundNum();
//
//        int pow = takeTheChance(maxReceivedBidutil);
//        double targetutil = 1 - (Math.pow(time, pow) * Math.abs(rand.nextGaussian() / 3));
//
//        if (numberOfReceivedOffer < 2)
//            return 1;
//        else if (!history.isEmpty()) {
//            double upperLimit = maxReceivedBidutil;
//
//            // pick the highest as the upper limit
//            for (double du : acceptanceLimits) {
//                if (upperLimit < du)
//                    upperLimit = du;
//            }
//            upperLimit = 0.90 * upperLimit;
//            pow = takeTheChance(upperLimit);
//            targetutil = 1 - (Math.pow(time, pow) * Math.abs(rand.nextGaussian() / 3));
//            targetutil = upperLimit + (1 - upperLimit) * targetutil;
//
//        } else {
//
//            if (maxReceivedBidutil < 0.8)
//                maxReceivedBidutil = 0.8;
//
//            targetutil = maxReceivedBidutil + (1 - maxReceivedBidutil) * targetutil;
//
//        }
//
//        if (lastProposal.get(own.getNum()) > targetutil || time > 0.999)
//            return -1; // Accept
//        else
//            return targetutil;
//    }
//
//    private Map<Integer, Integer> generateBid(double targetutil) {
//        Map<Integer, Integer> bid = null;
//        double time = super.roundNum / game.getMaxRoundNum();
//
//        if (time > 0.995 && !bestAcceptedBids.isEmpty()) {
//            // game has almost ended. Offer one from best accepted bids
//            int s = bestAcceptedBids.size();
//
//            if (s > 3)
//                s = 3;
//
//            // pick from top 3
//            int ind = rand.nextInt(s);
//            bid = bestAcceptedBids.get(ind);
//        } else {
//
//            // find candidate bids in range target utility and 1
//            if (opp0bag.size() > 0 && opp1bag.size() > 0)
//                bid = searchCandidateBids(targetutil);
//
//            if (bid == null)
//                bid = sos.getBidNearUtility(targetutil).getBid();
//        }
//
//        System.out.flush();
//        return bid;
//
//    }
//
//    public Map<Integer, Integer> searchCandidateBids(double targetutil) {
//        double bu = 0.0;
//        int valu = 0;
//        // search for maximum match
//        LinkedList<Integer> intersection = new LinkedList<>();
//        LinkedList<Map<Integer, Integer>> candidateBids = new LinkedList<>();
//
//        for (BidDetails bd : sos.getAllOutcomes()) {
//            bu = getUtility(bd.getBid());
//
//            if (bu >= targetutil) {
//                int score = 0;
//                for (int isn = 0; isn < bd.getBid().getIssues().size(); isn++) {
//                    valu = bd.getBid().getValue(isn + 1);
//
//                    if (valu == opp0bag.get(isn))
//                        score++;
//
//                    if (valu == opp1bag.get(isn))
//                        score++;
//                }
//
//                intersection.add(score);
//                candidateBids.add(bd.getBid());
//
//            } else
//                break;
//        }
//
//        int max = -1;
//        for (int i = 0; i < intersection.size(); i++) {
//            if (max < intersection.get(i))
//                max = i; // if find find higher score, make it max.
//        }
//
//        if (candidateBids.size() > 1) {
//            return candidateBids.get(max);
//        }
//
//        return null;
//    }
//
//    public void BidResolver(Map<Integer, Integer> proposal, int proposer) {
//        int valu = 0;
//        if (proposer == 0 && proposer != own.getNum()) {
//            valu = proposal.get(proposer);
//            if (frequentValuesList0.containsKey(valu)) {
//                int prevAmount = frequentValuesList0.get(valu);
//                frequentValuesList0.put(valu, prevAmount + 1);
//            } else {
//                frequentValuesList0.put(valu, 1);
//            }
//
//
//        } else if (proposer == 1 && proposer != own.getNum()) {
//            if (frequentValuesList1.containsKey(valu)) {
//                int prevAmount = frequentValuesList1.get(valu);
//                frequentValuesList1.put(valu, prevAmount + 1);
//            } else {
//                frequentValuesList1.put(valu, 1);
//            }
//        } else if (proposer == 2 && proposer != own.getNum()) {
//            if (frequentValuesList2.containsKey(valu)) {
//                int prevAmount = frequentValuesList2.get(valu);
//                frequentValuesList2.put(valu, prevAmount + 1);
//            } else {
//                frequentValuesList2.put(valu, 1);
//            }
//        }
//
//        if (numberOfReceivedOffer > 2)
//            extractOpponentPreferences();
//    }
//
//    public void printFreqs(int opid) {
//
//        System.out.println("opid : " + opid);
//        if (opid == 0) {
//            for (int val : frequentValuesList0.keySet()) {
//                System.out.println("freq0: is: value :" + val + " amount: " + frequentValuesList0.get(val));
//            }
//        } else {
//            for (int val : frequentValuesList1.keySet()) {
//                System.out.println("freq1: is:  value :" + val + " amount: " + frequentValuesList1.get(val));
//            }
//        }
//        System.out.println("\n");
//
//    }
//
//    //为收到的提议计算一个平均值
//    public void extractOpponentPreferences() {
//        // find the best intersection
//        ArrayList<Integer> opp0priors = new ArrayList<Integer>();
//        ArrayList<Integer> opp1priors = new ArrayList<Integer>();
//        ArrayList<Integer> opp2priors = new ArrayList<Integer>();
//
//        opp0bag = new ArrayList<Integer>();
//        opp1bag = new ArrayList<Integer>();
//        opp2bag = new ArrayList<Integer>();
//
//        double meanEvalValues0 = 0.0;
//        double meanEvalValues1 = 0.0;
//        double meanEvalValues2 = 0.0;
//
//        double sum = 0.0;//提议总价值
//        int offernum = 0;//收到提议次数
//        for (int val : frequentValuesList0.keySet()) {
//            sum += val * frequentValuesList0.get(val); // find the average
//            offernum += frequentValuesList0.get(val);
//            // eval value of that issue
//        }
//        meanEvalValues0 = (sum / offernum);
//
//        sum = 0.0;
//        offernum = 0;
//        for (int val : frequentValuesList1.keySet()) {
//            sum += val * frequentValuesList1.get(val); // find the average
//            offernum += frequentValuesList1.get(val);
//            // eval value of that issue
//        }
//        meanEvalValues1 = (sum / offernum);
//
//        sum = 0.0;
//        offernum = 0;
//        for (int val : frequentValuesList2.keySet()) {
//            sum += val * frequentValuesList2.get(val); // find the average
//            offernum += frequentValuesList2.get(val);
//            // eval value of that issue
//        }
//        meanEvalValues2 = (sum / offernum);
//
//        // select ones with over average
//        for (int val : frequentValuesList0.keySet()) {
//            if (frequentValuesList0.get(val) >= meanEvalValues0) {
//                opp0priors.add(val);
//            }
//        }
//        opp0bag.add(opp0priors.get(rand.nextInt(opp0priors.size())));
//        opp0priors = new ArrayList<Integer>();
//
//        for (int val : frequentValuesList1.keySet()) {
//            if (frequentValuesList1.get(val) >= meanEvalValues1) {
//                opp1priors.add(val);
//            }
//        }
//        opp1bag.add(opp1priors.get(rand.nextInt(opp1priors.size())));
//        opp1priors = new ArrayList<Integer>();
//
//        for (int val : frequentValuesList2.keySet()) {
//            if (frequentValuesList2.get(val) >= meanEvalValues2) {
//                opp2priors.add(val);
//            }
//        }
//        opp1bag.add(opp2priors.get(rand.nextInt(opp2priors.size())));
//        opp2priors = new ArrayList<Integer>();
//
//    }
//
//
//    private void analyzeHistory() {
//        isHistoryAnalyzed = true;
//
//        for (int h = 0; h <= history.size() - 1; h++) { // from older to recent
//            // history
//
//            LinkedList<Double> utilsOp1 = new LinkedList<>();
//            LinkedList<Double> utilsOp2 = new LinkedList<>();
//
//            StandardInfo info = history.get(h);
//
//            boolean historyMatch = true;
//
//            int cnt = 0;
//            for (Tuple<String, Double> offered : info.getUtilities()) {
//
//                String partyname = getPartyName(offered.get1());
//                Double util = offered.get2();
//
//                if (cnt < 3 && !partyname.equals(parties.get(cnt))) {
//                    historyMatch = false;
//                    break;
//                } else {
//                    // check if there's a confusion
//
//                    if (partyname.equals(opponentNames[0])) {
//                        utilsOp1.add(util);
//                        if (util > acceptanceLimits[0])
//                            acceptanceLimits[0] = util;
//
//                    } else if (partyname.equals(opponentNames[1])) {
//                        utilsOp2.add(util);
//                        if (util > acceptanceLimits[1])
//                            acceptanceLimits[1] = util;
//                    }
//
//                }
//                cnt++;
//            }
//
//        }
//
//    }
//
//
//    @Override
//    public Communicate Communicate() {
//        Communicate communicate = new Communicate();
//        communicate.setPlayerInfo(this.getPlayerInfo());
//
////        平台中的自由语言模块
//        communicate.setCommunicateType("hi");
////        平台中的固定语言模块
//        communicate.setCommunicateFree("hei");
////        平台中的表情模块
////        不高兴 1  正常 3  高兴 5
//        communicate.setEmotion("1");
//        return communicate;
//    }
//
//}
