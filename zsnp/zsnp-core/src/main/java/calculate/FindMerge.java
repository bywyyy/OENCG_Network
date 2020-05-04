package calculate;

import com.swu.agentlab.zsnp.entity.player.server.ChooseYourAgents;

import java.util.*;


public class FindMerge {

/**player_map,KEY是每个player的ID，对应着每个人的weight;
* quota 是组成有效联盟的最小权重和；
* self player的id（整数），是指针对哪个agent去计算有效的merge；
* index_type 是指使用哪种index去进行power计算;
* return 返回能够有效合并的agent的id，（这个算法只考虑和一个player进行merge的情况）；没找到，就返回“-1”*/

    static public Integer find_merge_agent(Map<Integer, Integer> players_map, Integer quota, Integer self, String index_type){
        //初始化伙伴是-1（没有这样的伙伴），联合能获取的利益benefit是0
        Integer partner = -1;
        Float benefit = 0.0f;

        float power_self = PowerIndex(players_map, quota, self, index_type);
        for (Integer player:players_map.keySet()){
            if (player != self){
                float power_player = PowerIndex(players_map, quota, player, index_type);
                /**进行merge，重新形成Game,players_map改变；
                * 具体操作：重新定义一组map, 并且assign原来players_map中的值（除了self和当前player）；
                * id 为self 的字典中，weight填入self和player的weight之和；*/
                Map<Integer, Integer> players_map_new = new HashMap<>();
                for (Integer p:players_map.keySet()){
                    if (p != self & p != player){
                        players_map_new.put(p,players_map.get(p));
                    }
                }
                players_map_new.put(self, players_map.get(self)+players_map.get(player));
                float power_self_new = PowerIndex(players_map_new, quota, self, index_type);
                /**找到联合能达到更大利益的合作伙伴*/
                float temp_benefit = power_self_new - (power_self + power_player);
                if (temp_benefit > benefit){
                    partner = player;
                    benefit = temp_benefit;
                }
            }
        }
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println("选择的合作伙伴partner:"+ partner + "，两个合作之后增加的benefit:" + benefit);
        return partner;
    }

    public static float PowerIndex(Map<Integer, Integer> players_map, Integer quota, Integer player, String index_type){
        float index = 0.0f;
        if (index_type == "Shapley"){
            index = ShapleyPower(players_map, quota, player);
        }
        else if(index_type == "Banzhaf"){
            index = BanzhafPower(players_map, quota, player);
        }
        else if (index_type == "DP"){
            index = DPPower(players_map, quota, player);
        }
        else {
            System.out.println("error ：输入index种类不规范");
        }
        return index;
    }

    public static float DPPower(Map<Integer, Integer> players_map, Integer quota, Integer player){
        /** 调用函数，找到所有winning coalitions；初始化important_map*/
        Map<Integer, Integer> important_map = new HashMap<>();
        Set<Set<Integer>> winning_coalitions = GetAllWinningCoalitions(players_map,quota);
        //求players数组
        Set<Integer> players = new HashSet<>();
        for (Integer p:players_map.keySet()){
            players.add(p);
            important_map.put(p,0);
        }
        //找到最小联盟的size
        int min_size = players.size();
        for (Set coa:winning_coalitions) {
            if(coa.size() < min_size){
                min_size = coa.size();
            }
        }
        //计算important count
        for (Set coa:winning_coalitions) {
            if (coa.size() == min_size){
                /** for every-player , calculate it's important count */
                for (Integer p : players){
                    if (coa.contains(p)){
                        important_map.put(p, important_map.get(p)+1);
                    }
                }
            }
        }
        /**calculate the power index*/
        Integer sum_important = 0;
        for (Integer p : important_map.keySet()){
            sum_important += important_map.get(p);
        }
        Map<Integer, Float> weight_map = new HashMap<>();
        for (Integer p : important_map.keySet()){
            weight_map.put(p, (float)important_map.get(p)/(float)sum_important);
        }

        return weight_map.get(player);
    }

    public static float BanzhafPower(Map<Integer, Integer> players_map, Integer quota, Integer player){

        /** 调用函数，找到所有winning coalitions；初始化important_map*/
        Map<Integer, Integer> important_map = new HashMap<>();
        Set<Set<Integer>> winning_coalitions = GetAllWinningCoalitions(players_map,quota);
        //求players数组
        Set<Integer> players = new HashSet<>();
        for (Integer p:players_map.keySet()){
            players.add(p);
            important_map.put(p,0);
        }
        for (Set coa:winning_coalitions) {
            /** for every-player , calculate it's important count */
            Integer temp_sum = 0;
            for (Object p : coa){
                temp_sum += players_map.get(p);
            }
            for (Integer p : players){
                if (coa.contains(p)){
                    if ((temp_sum - players_map.get(p)) < quota){
                        important_map.put(p, important_map.get(p)+1);
                    }
                }
            }
        }
        /**calculate the power index*/
        Integer sum_important = 0;
        for (Integer p : important_map.keySet()){
            sum_important += important_map.get(p);
        }
        Map<Integer, Float> weight_map = new HashMap<>();
        for (Integer p : important_map.keySet()){
            weight_map.put(p, (float)important_map.get(p)/(float)sum_important);
        }

        return weight_map.get(player);
    }

    public static Set<Set<Integer>> GetAllWinningCoalitions(Map<Integer, Integer> players_map, Integer quota){
        Set<Set<Integer>> winning_coalitions = new HashSet<Set<Integer>>();
        //求players数组
        Integer PLAYERNUM = players_map.size();
        Integer[] players = new Integer[PLAYERNUM];
        int ii = 0;
        for (Integer p:players_map.keySet()){
            players[ii] = p;
            ii++;
        }
        for (int co_num = 2; co_num <= PLAYERNUM; co_num++){
            try{
                //调用Cmn 函数（之前写在server类里面的）
                ChooseYourAgents ca = new ChooseYourAgents(players, co_num);
                //如果可以组成winning coalition，就放到winning coalitions集合里面
                Object[][] c = ca.getResutl();
                for (int i = 0; i < c.length; i++) {
                    int temp_sum = 0;
                    Set<Integer> temp = new HashSet<>();
                    for(int j = 0; j < c[i].length; j++) {
                        temp_sum += players_map.get(c[i][j]);
                        temp.add((Integer) c[i][j]);
                    }
                    if (temp_sum >= quota){
                        winning_coalitions.add(temp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return winning_coalitions;
    }

    public static float ShapleyPower(Map<Integer, Integer> players_map, Integer quota, Integer player){
        /** search for the person's important count*/
        /** 初始化important_count*/
        Integer important_count = 0;
        Integer PLAYERNUM = players_map.size();

        // all_orderings_count calculated
        int all_orderings_count = 1;
        for (int i = 0; i < PLAYERNUM; i++){
            all_orderings_count *= (i + 1);
        }

        //求组合数
        int[] players = new int[PLAYERNUM];
        int ii = 0;
        for (Integer p:players_map.keySet()){
            players[ii] = p;
            ii++;
        }
        ArrayList<ArrayList<Integer>> res = Permutation(players,PLAYERNUM);
        //arrayList 转化为 数组
        int[][] all_orderings=new int[all_orderings_count][PLAYERNUM];
        for(int i=0; i<all_orderings_count; i++){
            for (int j = 0; j < PLAYERNUM; j++){
                all_orderings[i][j] = res.get(i).get(j) ;
            }
        }

        /** let it be this player , calculate it's important count*/
        for (int i = 0; i < all_orderings.length; i++){
            int accumulative_weight = 0;
            for (int j = 0; j < all_orderings[i].length; j++){
                if (all_orderings[i][j] != player){
                    accumulative_weight += players_map.get(all_orderings[i][j]);
                }
                else {
                    break;
                }
            }
            if ((accumulative_weight < quota) & ((accumulative_weight + players_map.get(player)) >= quota)){
                important_count += 1;
//                    System.out.println("debugdebug输出：：："+ temp_num + "::" + important_count[temp_num]);
            }
        }
        /**calculate the power index;
         * Shapley index 本身就是正则化的（不用做正则化操作）*/
        float power_index = (1.0f/(float)all_orderings.length)*important_count;

        return power_index;
    }



    public static ArrayList<ArrayList<Integer>> Permutation(int[] A, int n)
    {
        if(n==1)
        {
            ArrayList<Integer> t=new ArrayList<Integer>();
            ArrayList<ArrayList<Integer>> temp=new ArrayList<ArrayList<Integer>>();
            t.add(A[0]);
            temp.add(t);
            return temp;
        }
        else
        {
            ArrayList<ArrayList<Integer>> temp=Permutation(A,n-1);
            ArrayList<ArrayList<Integer>> res=new ArrayList<ArrayList<Integer>>();
            for(int i=0;i<temp.size();i++)
            {

                for(int j=0;j<n;j++)
                {
                    ArrayList<Integer> t=new ArrayList<Integer>(temp.get(i));
                    if(j<n-1)
                    {
                        t.add(j,A[n-1]);
                        res.add(t);
                    }
                    else
                    {
                        t.add(A[n-1]);
                        res.add(t);
                    }

                }
            }
            return res;
        }
    }

    public static int getRandom(int min, int max){
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }


    public static void main(String[] args) {
        for (int j = 0; j < 10 ; j++) {
            Map<Integer, Integer> players_map = new HashMap<>();
            for (int i = 1; i <= 3; i++) {
                players_map.put(i, getRandom(1, 3));
            }
            Set<Integer> players = new HashSet<>();
            Integer sum_weight = 0;
            Integer max_weight = 0;
            for (Integer p : players_map.keySet()) {
                players.add(p);
                sum_weight += players_map.get(p);
                if (players_map.get(p) > max_weight) {
                    max_weight = players_map.get(p);
                }
            }
            Integer quota = getRandom(max_weight + 1, sum_weight);
            System.out.println("#####################################");
            System.out.println("Game:" + players_map + "(quota:" + quota + ")");
            Set<Set<Integer>> winning_coalitions = GetAllWinningCoalitions(players_map, quota);
            System.out.println("winning_coalitions:" + winning_coalitions);
            System.out.println("---------------------------------------------");
//            String[] index_types = new String[]{"Shapley", "Banzhaf", "DP"};
            String[] index_types = new String[]{ "DP"};
            for (String index_type : index_types) {
                for (Integer p : players) {
                    System.out.println("玩家编号为：" + p + "，" + index_type + "index 为："
                            + PowerIndex(players_map, quota, p, index_type));
                }
                for (Integer p : players) {
                    System.out.println("玩家编号为：" + p + "，根据" + index_type + "index 算出可以" +
                            "达到较高有益合作的合作伙伴的编号是（-1表示不存在这样的伙伴）："
                            + find_merge_agent(players_map, quota, p, index_type));
                }
                System.out.println("---------------------------------------------");
            }
        }
    }



}
