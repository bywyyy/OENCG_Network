package calculate;

import com.swu.agentlab.zsnp.game.coalition.voting.agent.Game;
import com.swu.agentlab.zsnp.game.coalition.voting.agent.Infos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Index_SS {

    public static ArrayList<ArrayList<Integer>> Permutation(int[] A,int n)
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

    static public float[] ss_index(Game game, Infos own, Map<Integer, Infos> opponents){
        /** code ::: shapley-shubik index */


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
        for (int i = 0; i < game.getAmountOfPlayers(); i++ ){
            important_count[i] = 0;
        }

        //players is each's id; all_orderings_count calculated
        int[] players = new int[game.getAmountOfPlayers()];
        int all_orderings_count = 1;
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            players[i] = i;
            all_orderings_count *= (i + 1);
        }


        //求组合数
        ArrayList<ArrayList<Integer>> res = Permutation(players,game.getAmountOfPlayers());

        //arrayList 转化为 数组
        int[][] all_orderings=new int[all_orderings_count][game.getAmountOfPlayers()];
        for(int i=0;i<all_orderings_count;i++){
            for (int j = 0; j < game.getAmountOfPlayers(); j++){
                all_orderings[i][j] = res.get(i).get(j) ;
            }
        }

//        for(int i=0;i<all_orderings_count;i++){
//            System.out.println("debug::" + Arrays.toString(all_orderings[i]));
//        }


        /** in turn ， let it be every-player , calculate it's important count */
        for (int temp_num = 0; temp_num < game.getAmountOfPlayers(); temp_num++){
            for (int i = 0; i < all_orderings.length; i++){
                int accumulative_weight = 0;
                for (int j = 0; j < all_orderings[i].length; j++){
                    if (all_orderings[i][j] != temp_num){
                        accumulative_weight += weight_list[all_orderings[i][j]];
                    }
                    else {
                        break;
                    }
                }
                if ((accumulative_weight < game.getMajority()) & ((accumulative_weight + weight_list[temp_num]) >= game.getMajority())){
                    important_count[temp_num] += 1;
//                    System.out.println("debugdebug输出：：："+ temp_num + "::" + important_count[temp_num]);
                }
            }
        }


        /**calculate the power index*/
        float[] power_index = new float[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            power_index[i] = (1.0f/(float)all_orderings.length)*important_count[i];
        }

        /** calculate the norm power index */
        float[] norm_SSIndex_power_index = new float[game.getAmountOfPlayers()];
        for (int i = 0; i < game.getAmountOfPlayers(); i++){
            norm_SSIndex_power_index[i] = 0.0f;
        }

        float temp_power_index_sum = 0;
        for (int i = 0; i < power_index.length; i++){
            temp_power_index_sum += power_index[i];
        }
        for (int i = 0; i < power_index.length; i++){
            norm_SSIndex_power_index[i] = power_index[i]/temp_power_index_sum;
        }

        for (int i = 0; i < game.getAmountOfPlayers(); i++) {
            System.out.print("\r\n" + "norm_SSIndex_power_index:" + norm_SSIndex_power_index[i] + "\r\n");
        }



        return norm_SSIndex_power_index;
    }
}
