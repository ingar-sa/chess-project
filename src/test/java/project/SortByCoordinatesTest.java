package project;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SortByCoordinatesTest {

    private boolean compareCoordinates(int[] actual, int[] expected) {
        return (actual[0] == expected[0] && actual[1] == expected[1]) ? true : false;
    }
    
    @Test
    @DisplayName("Test that the comparator works as expected")
    public void compareTest() {

        ArrayList<int[]> actualKeys = new ArrayList<int[]>(Arrays.asList(new int[]{2, 3}, new int[]{2, 2}, new int[]{1, 3}, new int[]{1, 3}));
        ArrayList<int[]> expectedKeys = new ArrayList<int[]>(Arrays.asList(new int[]{1, 3}, new int[]{1, 3}, new int[]{2, 2}, new int[]{2, 3}));

        Collections.sort(actualKeys, new SortByCoordinates());

        //Tests that it sorts as expected 
        for (int index = 0; index < actualKeys.size(); index++) {
            assertTrue(compareCoordinates(expectedKeys.get(index), actualKeys.get(index)));
        }
    }

}


