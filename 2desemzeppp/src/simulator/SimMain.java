package simulator;

import map.Map;

/**
 * Initialises the Sim and its SimConn.
 */
public class SimMain {
	
	/**
	 * Creates a Sim for the own zeppelin
	 * 
	 * @param 	map
	 * 			Map of the field
	 * @param 	rabbit
	 * 			True if using a rabbit server
	 * @param 	wind
	 * 			True to put random wind on.
	 */
	public static Simulator makeOwnZeppSim(Map map,boolean rabbit,boolean wind) {
		Simulator sim = new Simulator(map);
		if(rabbit) {
			SimConnection conn = new SimConnection(sim);
			sim.setSimConn(conn);
			Thread connThread = new Thread(conn);
			connThread.start();
		}
		if(!rabbit) {
			simNoRabbitMaker simnorabbitmaker = new simNoRabbitMaker(sim);
			Thread simMakerThread = new Thread(simnorabbitmaker);
			simMakerThread.start();
		}
		return sim;
	}
	
	/**
	 * Creates a new SimEnemy.
	 * 
	 * @param 	rabbit
	 * 			True if using a rabbit server
	 * @param 	constant
	 * 			Constant for the Sim. 0 for default.
	 * @param 	wait
	 * 			Waiting duration between steps (in ms). 0 for default.
	 * @param 	x
	 * 			Starting x-coordinate (in cm). <0 for default
	 * @param 	y
	 * 			Starting y-coordinate (in cm). <0 for default
	 */
	public static SimEnemy makeSimEnemy(boolean rabbit, double constant, int wait, double x, double y) {
		if(constant == 0)
			constant = 2;
		if(wait == 0)
			wait = 500;
		if(x < 0)
			x = 200;
		if(y < 0)
			y = 200;
		SimEnemy simenemy = new SimEnemy(constant,wait,x,y);
		
		if(rabbit) {
			
		}
		if(!rabbit) {
			simEnemyNoRabbitMaker simnorabbitmaker = new simEnemyNoRabbitMaker(simenemy);
			Thread simMakerThread = new Thread(simnorabbitmaker);
			simMakerThread.start();
		}
		
		return simenemy;
	}
	
	public static void main(String[] args) {
		makeOwnZeppSim(new Map("/shapesDemo.csv"),false,false);
		
	}
	
	public static class simNoRabbitMaker implements Runnable {
		private Simulator sim;
		
		public simNoRabbitMaker(Simulator sim) {
			this.sim = sim;
		}
		
		public void run() {
			SimConnNoRabbit simconn = new SimConnNoRabbit(sim);
			sim.setSimConnNoRabbit(simconn);
			Thread conn = new Thread(simconn);
			conn.start();
		}
	}
	
	public static class simEnemyNoRabbitMaker implements Runnable {
		private SimEnemy sim;
		
		public simEnemyNoRabbitMaker(SimEnemy sim) {
			this.sim = sim;
		}
		
		public void run() {
			SimEnemyConnNoRabbit simconn = new SimEnemyConnNoRabbit();
			sim.setSimConnNoRabbit(simconn);
		}
	}
}
