package calculate;
import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Calculate_prosocial_aspiration {
    static public Integer prosocial_aspiration(Game game, Infos own, Map<Integer, Infos> opponents){

        /** 三种index的计算，调用calculate包里的函数*/
        float[] norm_banzhaf_power_index = Index_Banzhaf.banzhaf_index(game, own, opponents);
        float[] norm_SSIndex_power_index = Index_SS.ss_index(game, own, opponents);
        float[] norm_DPIndex_power_index = Index_DP.dp_index(game, own, opponents);


        /**三种index，看自己用哪种index的权重最小*/
        float[][] index_three_types = new float[3][game.getAmountOfPlayers()];
        index_three_types[0] = norm_banzhaf_power_index;
        index_three_types[1] = norm_SSIndex_power_index;
        index_three_types[2] = norm_DPIndex_power_index;

        int min_index_type = 0;
        for (int i = 0; i < index_three_types.length; i++){
            if (index_three_types[i][own.getNum()] < index_three_types[min_index_type][own.getNum()]){
                min_index_type = i;
            }
        }
        System.out.print("\r\n" + "哪种index定义标准化后的自身的权重最小：" + min_index_type + "\r\n");


        /**
         * 确定你的盟友，并将盟友编号添加到Set中，并确定联盟的总资金
         */


        Set<Integer> set = new HashSet<>();

        for (Coalition coa:game.getCoalitions()){
            if (coa.getPartyNums().size() == game.getAmountOfPlayers()){
                set = coa.getPartyNums();
            }
        }

        Integer prosocial_aspiration = (int)Math.floor( game.getRewards(set)*(index_three_types[min_index_type][own.getNum()]) );

        return prosocial_aspiration;
    }
}
