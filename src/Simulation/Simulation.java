/**
 *	Example program for using eventlists
 *	@author Joel Karel
 *	@version %I%, %G%
 */

package Simulation;

import java.util.Arrays;

public class Simulation {

    public CEventList list;
    public Queue queue;
    public Source source;
    public Sink sink;
    public Machine mach;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        new Simulation().basesetting();
    }

    Machine [] machines = new Machine[8];
    Queue [] queues = new Queue[8];

    public void basesetting(){
        CEventList l = new CEventList();
        Sink si = new Sink("Sink 1");


        for (int i=0; i < 8 ;i++){
            queues[i] = new Queue();
        }
        Source s1 = new Source(l,"Regular",10,2);
        Source s2 = new Source(l,"GPU",10,2);
        // TODO Implement the poisson process

        // The regular computation severs
        for (int i=0; i < 6; i++){
            machines[i] = new Machine(queues[i],si,l,"Regular computation severs",24,0.8);
            s1.add(queues[i]);
        }

        for (int i=0; i < 2; i++){
            s1.add(queues[i+6]);
            s2.add(queues[i+6]);
            machines[i+6] = new Machine(queues[i+6],si,l,"GPUs",24,.8);
        }
        // The GPU severs



        l.start(40); // 2000 is maximum time
    }

    //TODO Implement the poisont distribution
    private double [] poissonDistri() {
        return null;
    }

}