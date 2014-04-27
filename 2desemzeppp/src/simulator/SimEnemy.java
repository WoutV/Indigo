package simulator;

import java.util.Random;

import map.Map;

/**
 * The SimEnemy: Sim for an enemy zeppelin.
 * The Sim needs a Map and target. It moves slowly towards this target.
 */
public class SimEnemy {
	private double Constant;
	
	private SimConnection simconn;
	private SimConnNoRabbit simconn2;

	/**
	 * Set this SimEnemy to use a SimConnection using a Rabbit server.
	 * @param sims
	 */
	public void setSimConn(SimConnection sims){
		simconn = sims;
	}
	
	/**
	 * Set this SimEnemy to use a connection not using a Rabbit server.
	 * @param conn
	 */
	public void setSimConnNoRabbit(SimConnNoRabbit conn) {
		simconn2 = conn;
	}
	/**
	 * The time between moves (in ms).
	 */
	private long wait = 500;
	
	private boolean wind;
	
	private Map map;

	//boardsize in cm
	private int boardSize = 400;

	private boolean running;
	
	//all in cm!!
	private double xPos=200,yPos=200;
	private double xTarget,yTarget;
	
	private Random random = new Random();

	/**
	 * Creates a new SimEnemy using the given map, speed constant and wait duration (in ms).
	 */
	public SimEnemy(Map map, double constant, int wait){
		this.map = map;
		Constant = constant;
		this.wait = wait;
	}

	/**
	 * Creates a new SimEnemy using the given map, default speed constant and default wait duration.
	 */
	public SimEnemy(Map map){
		this(map,40,500);
	}

	/**
	 * Set wind (unreliability) on.
	 */
	public void windOn() {
		wind = true;
	}

	/**
	 * Set wind (unreliability) off.
	 */
	public void windOff() {
		wind = false;
	}
		
	/**
	 * Creates a random wind effect: between -2 cm and 2 cm.
	 */
	private void wind() {
		//-2cm -> 2cm
		int xExtra = random.nextInt(400);
		xExtra -= 200;
		int yExtra = random.nextInt(400);
		yExtra -= 200;
		xPos = xPos + xExtra/100.0;
		yPos = yPos + yExtra/100.0;
	}
	
	/**
	 * Sends a target to the SimEnemy (in cm) and makes it run.
	 * 
	 * @param x
	 * @param y
	 */
	public void receiveTarget(int x, int y) {
		xTarget = x;
		yTarget = y;
		running = true;
		run();
	}
	
	public void run() {
		System.out.println("running now");
		while(running) {
			try {
				Thread.sleep(wait);
			}
			catch (Exception exc) {
				
			}
			
			if(xPos < xTarget-10) {
				xPos = xPos + Constant;
			}
			if(xPos > xTarget+10) {
				xPos = xPos - Constant;
			}
			if(yPos < yTarget-10) {
				yPos = yPos + Constant;
			}
			if(yPos > yTarget+10) {
				yPos = yPos + Constant;
			}
			
			if(Math.abs(xPos-xTarget) < 10 && Math.abs(yPos-yTarget) < 10) {
				running = false;
			}
			
			sendLoc();
		}
	}
	
	private void sendLoc() {
		String key = "enemy.loc.loc";
		String info = (int) (xPos*10) + "," + (int) (yPos*10); 
		if(simconn != null)
			simconn.sendTransfer(info, key);
		if(simconn2 != null)
			simconn2.sendTransfer(info, key);
	}
	
}

