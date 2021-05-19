package Simulation;


import Simulation.RandomDistribution.UniformDistribution;
import Simulation.RandomDistribution.RandomDistribution;

/**
 *	Machine in a factory
 *	@author Joel Karel
 *	@version %I%, %G%
 */
public class Machine implements CProcess,ProductAcceptor {
	/** Product that is being handled  */
	private Product product;
	/** Eventlist that will manage events */
	private final CEventList eventlist;
	/** Queue from which the machine has to take products */
	private Queue queue;
	/** Sink to dump products */
	private ProductAcceptor sink;
	/** Status of the machine (b=busy, i=idle) */
	private char status;
	/** Machine name */
	private final String name;
	/** Random Distribution */
	private RandomDistribution rnd;
	/** Processing times (in case pre-specified) */
	private double[] processingTimes;
	/** Processing time iterator */
	private int procCnt;

	/**
	*	Constructor
	*        Service times are exponentially distributed with mean 30
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*/
	public Machine(Queue q, ProductAcceptor s, CEventList e, String n) {
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		rnd = new UniformDistribution(30);
		queue.askProduct(this);
	}

	/**
	*	Constructor
	*        Service times are exponentially distributed with specified mean
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*   @param rnd	Mean processing time
	*/
	public Machine(Queue q, ProductAcceptor s, CEventList e, String n, RandomDistribution rnd) {
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		this.rnd = rnd;
		queue.askProduct(this);
	}
	
	/**
	*	Constructor
	*        Service times are pre-specified
	*	@param q	Queue from which the machine has to take products
	*	@param s	Where to send the completed products
	*	@param e	Eventlist that will manage events
	*	@param n	The name of the machine
	*        @param st	service times
	*/
	public Machine(Queue q, ProductAcceptor s, CEventList e, String n, double[] st) {
		status='i';
		queue=q;
		sink=s;
		eventlist=e;
		name=n;
		rnd = new UniformDistribution(-1);
		processingTimes=st;
		procCnt=0;
		queue.askProduct(this);
	}

	/**
	*	Method to have this object execute an event
	*	@param type	The type of the event that has to be executed
	*	@param tme	The current time
	*/
	public void execute(int type, double tme) {
		// show arrival
		//System.out.println("Product finished at time = " + tme+"   "+name);
		// Remove product from system
		product.stamp(tme,"Production complete",name);
		sink.giveProduct(product);
		product=null;
		// set machine status to idle
		status='i';
		// Ask the queue for products
		queue.askProduct(this);
	}
	
	/**
	*	Let the machine accept a product and let it start handling it
	*	@param p	The product that is offered
	*	@return	true if the product is accepted and started, false in all other cases
	*/
        @Override
	public boolean giveProduct(Product p) {
		// Only accept something if the machine is idle
		if(status=='i') {
			// accept the product
			product=p;
			// mark starting time
			product.stamp(eventlist.getTime(),"Production started",name);
			// start production
			startProduction();
			// Flag that the product has arrived
			return true;
		}
		// Flag that the product has been rejected
		else return false;
	}

	@Override
	public int size() {
		return 0;
	}

	/**
	*	Starting routine for the production
	*	Start the handling of the current product with an exponentionally distributed processingtime with average 30
	*	This time is placed in the eventlist
	*/
	private void startProduction() {
		// generate duration
		if(rnd.getMean()>0) {
			double duration = drawRandomExponential();
			// Create a new event in the eventlist
			double tme = eventlist.getTime();
			eventlist.add(this,0,tme+duration); //target,type,time
			// set status to busy
			status='b';
		} else{
			if(processingTimes.length>procCnt) {
				eventlist.add(this,0,eventlist.getTime()+processingTimes[procCnt]); //target,type,time
				// set status to busy
				status='b';
				procCnt++;
			} else {
				eventlist.stop();
			}
		}
	}


	public double drawRandomExponential() {
		return rnd.executeRandom();
	}
}