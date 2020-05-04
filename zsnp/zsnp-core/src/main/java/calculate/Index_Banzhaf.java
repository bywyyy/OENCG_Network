package calculate;

import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;

import java.util.Map;

public class Index_Banzhaf {
    static public float[] banzhaf_index(Game game, Infos own, Map<Integer, Infos> opponents){
        /** code ::: Banzhaf index */


        /** search for all the persons weight*/
        int[] weight_list = new int[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++ ){
            weight_list[i] = 0;
        }
        weight_list[own.getNum()] = own.getWeight();
        for (Integer key : opponents.keySet()) {
            weight_list[key] = opponents.get(key).getWeight();
        }

        /** search for all the persons important count*/

        int[] important_count = new int[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            important_count[i] = 0;
        }

        for (Coalition coa:game.getCoalitions()) {
            /** temp_num in turn ， let it be every-player , calculate it's important count */
            for (int temp_num = 0; temp_num < game.getAmountOfPlayers(); temp_num++){
                if (coa.getPartyNums().contains(temp_num)){
                    if ((coa.getResources() - weight_list[temp_num]) < game.getMajority()){
                        important_count[temp_num] += 1;
                    }
                }
            }
        }

        /**calculate the power index*/
        float[] power_index = new float[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            power_index[i] = (1.0f/4.0f)*important_count[i];
        }

        /** calculate the norm power index */
        float[] norm_banzhaf_power_index = new float[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            norm_banzhaf_power_index[i] = 0.0f;
        }

        float temp_power_index_sum = 0;
        for (int i = 0; i < power_index.length; i++){
            temp_power_index_sum += power_index[i];
        }
        for (int i = 0; i < power_index.length; i++){
            norm_banzhaf_power_index[i] = power_index[i]/temp_power_index_sum;
        }

        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            System.out.print("\r\n"+"norm_banzhaf_power_index:"+norm_banzhaf_power_index[i]+"\r\n");
        }

        return norm_banzhaf_power_index;
    }
}
