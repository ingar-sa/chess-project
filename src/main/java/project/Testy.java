package project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Testy {

    public void test() {
        ArrayList<Integer> yo = new ArrayList<Integer>();
        yo.add(3);
        yo.add(3);
        yo.add(1);
        yo.add(1);
        yo.add(100000);
        yo.add(100000); 
        System.out.println(yo.size());

        Set<Integer> set = new HashSet<Integer>(yo);
        System.out.println(set.size()); 
    }
    
    public static void main(String[] args) {
        Testy t = new Testy();
        t.test();
        
    }
}
