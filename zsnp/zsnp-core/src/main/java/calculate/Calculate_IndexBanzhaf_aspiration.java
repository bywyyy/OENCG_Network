package calculate;
import com.swu.agentlab.zsnp.game.coalition.voting.Coalition;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;

import java.util.Map;

public class Calculate_IndexBanzhaf_aspiration {
    static public Integer banzhaf_aspiration(Game game, Infos own, Map<Integer, Infos> opponents){
        /**调用函数计算index*/
        float[] power_index = Index_Banzhaf.banzhaf_index(game, own, opponents);


        float sum_weight = 0;
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            sum_weight += power_index[i];
        }
        Integer banzhaf_aspiration = 0;
        for (Coalition coa:game.getCoalitions()){
            if (coa.getPartyNums().size() == game.getAmountOfPlayers()){
                banzhaf_aspiration = (int)Math.floor(game.getRewards(coa.getPartyNums())*(power_index[own.getNum()]/sum_weight));
            }
        }

        System.out.print("\r\n" + "期望：" + banzhaf_aspiration);

        return banzhaf_aspiration;
    }
}
