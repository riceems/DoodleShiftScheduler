package archive;

import net.sourceforge.jswarm_pso.Swarm;
import java.util.ArrayList;

/**
 * Created by muhammadsaadshamim on 4/28/15.
 */
public class PSORunner {


    public static int numShifts;
    private int numStudents, minShiftsRequired, maxShiftsAllowed;
    private ArrayList<ArrayList<Integer>> schedule;

    public PSORunner(int nStudents, int numShifts, int minShiftsRequired, int maxShiftsAllowed, ArrayList<ArrayList<Integer>> schedule) {
        numStudents = nStudents;
        this.numShifts = numShifts;
        this.schedule = schedule;
        this.minShiftsRequired = minShiftsRequired;
        this.maxShiftsAllowed = maxShiftsAllowed;

    }

    public void runOptimization(){
        System.out.println("Begin Optimization\n");

        Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES*10,
                new MyParticle(),
                new MyFitnessFunction(numStudents, minShiftsRequired, maxShiftsAllowed, schedule));

        // Set position (and velocity) constraints. I.e.: where to look for solutions
        swarm.setInertia(.9);
        swarm.setGlobalIncrement(.9);
        swarm.setParticleIncrement(.9);

        swarm.setMaxPosition(numStudents);
        swarm.setMinPosition(0);
        swarm.setMaxMinVelocity(.9);

        int numberOfIterations = 100000;

        // Optimize (and time it)
        for( int i = 0; i < numberOfIterations; i++ ) {
            swarm.evolve();
            if( i % 1000 == 0 ) System.out.println("Iteration: " + i + "\tParticle: " + swarm.getParticle(swarm.getBestParticleIndex()).toString());
        }

        // Print en results
        System.out.println(swarm.toStringStats());
        System.out.println("End Optimization");

    }
}
