package edu.rice.rems;

import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by muhammadsaadshamim on 7/26/15.
 */
public class Solver {

    private static Random generator = new Random();

    public static int[] bruteForce(int[][] availabilities)
    {

        int n = maxNumPossibility(availabilities);


        return new int[0];
    }

    public static int[] iterativeImprovement(int[][] availabilities) {

        int n = maxNumPossibility(availabilities);

        int[] solution = generateState(availabilities, generator.nextInt(n));

        // number tries allowed where value not improved i.e. not total tries, but stagnant ones
        int numTries = 0;
        int maxNumTries = 1000;

        int score = getScore(solution);

        while(numTries < maxNumTries){
            if(numTries > maxNumTries/2){
                int[][] neighbors = generateComplexNeighbors(solution);
                int[] scores = getScore(neighbors);

                int index = Ints.indexOf(scores,Ints.max(scores));

            }

        }



        return solution;
    }
}
