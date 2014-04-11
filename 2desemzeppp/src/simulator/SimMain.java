package simulator;

import map.Map;

/**
 * Initialises the Sim and its SimConn.
 */
public class SimMain {
	public static void main(String[] args) {
		Map map = new Map("/shapesDemo.csv");
		Simulator sim = new Simulator(map);
		
		//no rabbit
		SimConnNoRabbit simconn = new SimConnNoRabbit(sim);
		sim.setSimConnNoRabbit(simconn);
		Thread conn = new Thread(simconn);
		conn.start();
		
		
		//rabbit
//		SimConnection conn = new SimConnection(sim);
//		sim.setSimConn(conn);
//		Thread connThread = new Thread(conn);
//		connThread.start();
		
	}
}
