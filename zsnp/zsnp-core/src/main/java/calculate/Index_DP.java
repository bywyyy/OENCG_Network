package calculate;

import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;

import java.util.Map;

public class Index_DP {
    static public float[] dp_index(Game game, Infos own, Map<Integer, Infos> opponents){
        /** code ::: DP index */


        int a = game.getAmountOfPlayers();
        for (Coalition coa:game.getCoalitions()) {
            if(coa.getPartyNums().size() < a){
                a = coa.getPartyNums().size();
            }
        }

        float b = 0.0f;
        int[] temp_list = new int[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            temp_list[i] = 0;
        }
        for (Coalition coa:game.getCoalitions()) {
            if (coa.getPartyNums().size() == a){
                b += 1;
                /** in turn ， let it be every-player , calculate it's important count */
                for (int i = 0; i < game.getAmountOfPlayers(); i++){
                    if(coa.getPartyNums().contains(i)){
                        temp_list[i] += 1;
                    }
                }
            }
        }

        /**calculate the power index*/
        float[] power_index = new float[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            power_index[i] = (1.0f/b)*((1.0f/b)*temp_list[i]);
        }

        /** calculate the norm power index */
        float[] norm_DPIndex_power_index = new float[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            norm_DPIndex_power_index[i] = 0.0f;
        }

        float temp_power_index_sum = 0;
        for (int i = 0; i < power_index.length; i++){
            temp_power_index_sum += power_index[i];
        }
        for (int i = 0; i < power_index.length; i++){
            norm_DPIndex_power_index[i] = power_index[i]/temp_power_index_sum;
        }

        for (int i = 0; i < game.getAmountOfPlayers(); i++) {
            System.out.print("\r\n" + "norm_DPIndex_power_index:" + norm_DPIndex_power_index[i] + "\r\n");
        }



        return norm_DPIndex_power_index;
    }
}
