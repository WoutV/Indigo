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
			SimConnNoRabbit simconn = new SimConnNoRabbit(sim);
			sim.setSimConnNoRabbit(simconn);
			Thread conn = new Thread(simconn);
			conn.start();
		}
		return sim;
	}
	
	public static void main(String[] args) {
		makeOwnZeppSim(new Map("/shapesDemo.csv"),false,false);
		
	}
}
