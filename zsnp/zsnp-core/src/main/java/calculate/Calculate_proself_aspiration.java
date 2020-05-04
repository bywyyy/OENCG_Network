package calculate;
import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Calculate_proself_aspiration {


    static public Integer proself_aspiration(Game game, Infos own, Map<Integer, Infos> opponents){


        /** 三种index的计算，调用calculate包里的函数*/
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

        /**
         * 1，实例化一个Set对象，将自己的编号添加到该Set对象中
         *      一定要将自身的编号添加到Set对象中，否则会报运行时错误
         */
        /**
         * 2，确定你的盟友，并将盟友编号添加到Set中，并确定联盟的总资金
         */


        float self_max_reward = 0;

        Set<Integer> set = new HashSet<>();
        float set_sum_weight = 0;




        for (Coalition coa:game.getCoalitions()){
            if (coa.getPartyNums().contains(own.getNum())){
                int coa_rewards = coa.getRewards();
                float sum_weight = 0;
                for (Integer num: coa.getPartyNums()){
                    sum_weight += index_three_types[max_index_type][num];
                    System.out.print("debug输出：：："+ num);
                }
                float self_reward = coa_rewards*(index_three_types[max_index_type][own.getNum()]/sum_weight);

                System.out.print("debug输出：：："+self_reward);
                if (self_reward > self_max_reward){
                    self_max_reward = self_reward;
                    set = coa.getPartyNums();
                    set_sum_weight = sum_weight;
                }
            }
        }


        Integer aspiration = (int)Math.floor( game.getRewards(set)*(index_three_types[max_index_type][own.getNum()]/set_sum_weight) );

        return aspiration;


    }


}
