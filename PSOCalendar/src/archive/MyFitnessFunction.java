package archive;

import net.sourceforge.jswarm_pso.FitnessFunction;

import java.util.ArrayList;

/**
 * Created by muhammadsaadshamim on 4/28/15.
 */
public class MyFitnessFunction extends FitnessFunction {

    private int numStudents;
    private int minShiftsRequired = 0;
    private int maxShiftsAllowed = 7;

    private ArrayList<ArrayList<Integer>> schedule;

    /* penalty defaults */
    private int penaltyMinimumNotMet = 25;
    private int penaltyDoubleShift = 25;
    private int penaltyMaximumExceeded = 50;
    private int penaltyTripleShift = 70;


    public MyFitnessFunction(int numStudents, int minShiftsRequired, int maxShiftsAllowed, ArrayList<ArrayList<Integer>> schedule){
        super();
        this.numStudents = numStudents;
        this.minShiftsRequired = minShiftsRequired;
        this.maxShiftsAllowed = maxShiftsAllowed;
        this.schedule = schedule;
        setMaximize(false);
    }

    @Override
    public double evaluate(double[] position) {
        // integer index of student
        for(int i = 0; i < position.length; i++){
            position[i] = Math.floor(position[i]);
        }

        int score = calculateDoubleShiftPenalties(position);
        score += calculateTripleShiftPenalties(position);
        score += calculateNumberOfShiftPenalties(position);
        score += calculateUnwantedDates(position);
        return score;
    }

    private int calculateUnwantedDates(double[] x) {
        int score = 0;
        for(int i = 0; i < x.length; i++){
            score += schedule.get((int)x[i]).get(i);
        }
        return score;
    }

    private int calculateDoubleShiftPenalties(double[] x) {
        int val = 0;
        for(int i = 0; i < x.length-1; i++){
            if(x[i] == x[i+1])
                val += penaltyDoubleShift;
        }
        return val;
    }

    private int calculateTripleShiftPenalties(double[] x) {
        int val = 0;
        for(int i = 0; i < x.length-1; i++){
            if(x[i] == x[i+1] && x[i] == x[i+1])
                val += penaltyTripleShift;
        }
        return val;
    }

    private int calculateNumberOfShiftPenalties(double[] x) {
        int score = 0;
        int[] students = new int[numStudents];

        for(int i = 0; i < x.length; i++){
            students[(int)x[i]]++;
        }

        for(int i = 0; i < students.length; i++){
            if(students[i] < minShiftsRequired)
                score += penaltyMinimumNotMet;
            else if(students[i] > maxShiftsAllowed)
                score += penaltyMaximumExceeded;
        }

        return score;
    }
}
