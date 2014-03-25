package simulator;

public class SimMain {
	public static void main(String[] args) {
		Sim sim = new Sim();
		SimConn conn = new SimConn(sim);
		sim.setSimConn(conn);
		Thread connThread = new Thread(conn);
		connThread.start();
		
	}
}
