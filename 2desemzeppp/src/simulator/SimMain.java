package simulator;

public class SimMain {
	public static void main(String[] args) {
		Simulator sim = new Simulator();
		SimConnection conn = new SimConnection(sim);
		sim.setSimConn(conn);
		Thread connThread = new Thread(conn);
		connThread.start();
		
	}
}
