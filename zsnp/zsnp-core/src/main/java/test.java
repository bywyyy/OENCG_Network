import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args){
        System.out.println("hello");
        Map<Integer, Integer> proposal = new HashMap<>();
        proposal.put(1, 88);
        proposal.put(2, 12);
        Map<Integer, Integer> proposal2 = new HashMap<>();
        proposal2.put(1, 88);
        proposal2.put(2, 12);
        Map<Map, Float> qtable = new HashMap<>();
        qtable.put(proposal, 1.1f);
        qtable.put(proposal2, 1.1f);
        System.out.println(qtable);
        for (Map key:qtable.keySet()){
            System.out.println(key);
        }
        System.out.println(qtable.get(proposal));
    }
}
