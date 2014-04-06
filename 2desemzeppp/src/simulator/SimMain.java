package simulator;

import map.Map;

public class SimMain {
	public static void main(String[] args) {
		Simulator sim = new Simulator();
		Map map = new Map("/shapesDemo.csv");
		sim.setMap(map);
		
		//no rabbit
		SimConnNoRabbit simconn = new SimConnNoRabbit(sim);
		sim.setSimConnNoRabbit(simconn);
		Thread conn = new Thread(simconn);
		conn.start();
		
//		SimConnection conn = new SimConnection(sim);
//		sim.setSimConn(conn);
//		Thread connThread = new Thread(conn);
//		connThread.start();
		
	}
}
