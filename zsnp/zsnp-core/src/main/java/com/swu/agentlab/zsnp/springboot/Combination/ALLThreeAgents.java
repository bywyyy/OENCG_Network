package com.swu.agentlab.zsnp.springboot.Combination;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.Arrays;

public class ALLThreeAgents {
    /** 3*2*1 = 6 种 可重复，排列*/
    static public Integer[][] all_agent_indexs() {
        Integer agentTypeSum = 3;
        Integer PermutationNum = 6;
        Integer agentSelectedNum = 3;
        Integer[] base = new Integer[agentTypeSum];
        for (int i = 0; i < agentTypeSum; i++) {
            base[i] = i;
        }

        Integer[][] all_agentsIndex = new Integer[PermutationNum][agentSelectedNum];
        int m =0;
        for (int i = 0; i < agentTypeSum; i++) {
            for (int j = 0; j < agentTypeSum; j++) {
                if (i != j) {
                    for (int h = 0; h < agentTypeSum; h++) {
                        if (h != i && h != j) {
                            all_agentsIndex[m][0] = i;
                            all_agentsIndex[m][1] = j;
                            all_agentsIndex[m][2] = h;
                            m++;
                        }
                    }
                }
            }
        }
        return all_agentsIndex;
    }
//    public static void main(String[] args){
//        Integer[][] agents = all_agent_indexs();
//        for (int i = 0; i < agents.length; i++){
//            System.out.println(Arrays.toString(agents[i]));
//        }
//    }
}
