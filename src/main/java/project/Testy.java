package project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import project.Board.Tile;
import project.Pieces.Pawn;

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
        Tile tile = new Tile(2, 2);
        Pawn pawn = new Pawn("hei", 'w');
        tile.setPiece(pawn);

        Pawn pawn1 = ((Pawn)tile.getPiece());
        pawn1 = new Pawn("testy", 'b');

        
        
    }
}
