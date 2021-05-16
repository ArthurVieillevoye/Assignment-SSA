package Simulation;

import Simulation.RandomDistribution.UniformDistribution;
import Simulation.RandomDistribution.RandomDistribution;

import java.util.ArrayList;

/**
 *	A source of products
 *	This class implements CProcess so that it can execute events.
 *	By continuously creating new events, the source keeps busy.
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Source implements CProcess {
	/** Eventlist that will be requested to construct events */
	private CEventList list;
	/** Queue that buffers products for the machine */
	private ArrayList<ProductAcceptor> queue = new ArrayList<>();
	/** Name of the source */
	private String name;
	/** Random Distribution */
	private RandomDistribution rnd;
	/** Interarrival times (in case pre-specified) */
	private double[] interarrivalTimes;
	/** Interarrival time iterator */
	private int interArrCnt;

	/**
	*	Constructor, creates objects
	*        Interarrival times are exponentially distributed with mean 33
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*/
	public Source(ProductAcceptor q,CEventList l,String n) {
		list = l;
		queue.add(q);
		name = n;
		this.rnd = new UniformDistribution(35);
		// put first event in list for initialization
		list.add(this,0,drawRandomExponential()); //target,type,time
	}

	/**
	*	Constructor, creates objects
	*        Interarrival times are exponentially distributed with specified mean
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*	@param m	Mean arrival time
	*/
	public Source(ProductAcceptor q,CEventList l,String n,double m) {
		list = l;
		queue.add(q);
		name = n;
		this.rnd = new UniformDistribution(m);
		// put first event in list for initialization
		list.add(this,0,drawRandomExponential()); //target,type,time
	}
	public Source(CEventList l, String n, RandomDistribution rnd) {
		list = l;
		name = n;
		this.rnd = rnd;
		// put first event in list for initialization
		list.add(this,0,drawRandomExponential()); //target,type,time
	}

	public void add(ProductAcceptor q){
		queue.add(q);
	}

	/**
	*	Constructor, creates objects
	*        Interarrival times are prespecified
	*	@param q	The receiver of the products
	*	@param l	The eventlist that is requested to construct events
	*	@param n	Name of object
	*	@param ia	interarrival times
	*/
	public Source(ProductAcceptor q,CEventList l,String n,double[] ia) {
		list = l;
		queue.add(q);
		name = n;
		this.rnd = new UniformDistribution(-1);
		interarrivalTimes=ia;
		interArrCnt=0;
		// put first event in list for initialization
		list.add(this,0,interarrivalTimes[0]); //target,type,time
	}
	
        @Override
	public void execute(int type, double tme) {
		// show arrival
		System.out.println("Arrival at time = " + tme + "   "+ name);
		// give arrived product to queue
		Product p = new Product();
		p.stamp(tme,"Creation",name);
		int min = queue.get(0).size();
		ProductAcceptor assigne = queue.get(0);
		for(ProductAcceptor qu: queue){
			if(qu.size() < min){
				assigne = qu;
				min = qu.size();
			}
		}
		assigne.giveProduct(p);
		// generate duration
		if(rnd.getMean()>0) {
			double duration = drawRandomExponential();
			// Create a new event in the eventlist
			list.add(this,0,tme+duration); //target,type,time
		} else {
			interArrCnt++;
			if(interarrivalTimes.length>interArrCnt) {
				list.add(this,0,tme+interarrivalTimes[interArrCnt]); //target,type,time
			} else {
				list.stop();
			}
		}
	}

	public double drawRandomExponential() {
		return rnd.executeRandom();
	}
}