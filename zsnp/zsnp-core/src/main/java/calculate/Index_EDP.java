package calculate;

import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Index_EDP {
    static public int[] edp_index(Game game, Infos own, Map<Integer, Infos> opponents) {
        /** code ::: EDP index */

        int playerNumber = game.getAmountOfPlayers();

        int minplayerNumber = playerNumber;
        for (Coalition coa : game.getCoalitions()) {
            if (coa.getPartyNums().size() < minplayerNumber && coa.getPartyNums().contains(own.getNum())) {
                minplayerNumber = coa.getPartyNums().size();
            }
        }


        int[] temp_list = new int[playerNumber];
        for (int i = 0; i < playerNumber; i++) {
            temp_list[i] = 0;
        }

        Set<Coalition> minCoalitions = new HashSet<>();
        for (Coalition coa : game.getCoalitions()) {
            if (coa.getPartyNums().size() == minplayerNumber && coa.getPartyNums().contains(own.getNum())) {
                minCoalitions.add(coa);
            }

        }

        for (Coalition coa : minCoalitions) {
            float weightSum = coa.getResources();

            for (int n = 0; n < playerNumber; n++)
                if (coa.getPartyNums().contains(n)) {
                    if (n == own.getNum()) {
                        temp_list[n] += coa.getRewards() * own.getWeight() / weightSum;
                    } else {
                        temp_list[n] += coa.getRewards() * opponents.get(n).getWeight() / weightSum;
                    }
                }

        }

        float minCoalitionNumber = minCoalitions.size();
        /** calculate the norm power index */
        int[] norm_DPIndex_power_index = new int[playerNumber];
        for (int i = 0; i < playerNumber; i++) {
            norm_DPIndex_power_index[i] = 0;
        }

        for (int i = 0; i < playerNumber; i++) {
            norm_DPIndex_power_index[i] = (int)Math.floor(temp_list[i] / minCoalitionNumber);
        }

        for (int i = 0; i < playerNumber; i++) {
            System.out.print("\r\n" + "norm_DPIndex_power_index:" + norm_DPIndex_power_index[i] + "\r\n");
        }

        return norm_DPIndex_power_index;
    }
}
