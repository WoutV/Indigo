package simulator;

import java.util.Random;

/**
 * The SimEnemy: Sim for an enemy zeppelin.
 * The Sim needs a Map and target. It moves slowly towards this target.
 */
public class SimEnemy implements Runnable {
	private double Constant;
	
	private SimConnection simconn;
	private SimEnemyConnNoRabbit simconn2;

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
	public void setSimConnNoRabbit(SimEnemyConnNoRabbit conn) {
		simconn2 = conn;
	}
	/**
	 * The time between moves (in ms).
	 */
	private long wait = 500;
	
	private boolean wind;

	private boolean running;
	
	//all in cm!!
	private double xPos=200,yPos=200;
	private double xTarget,yTarget;
	
	private Thread thread;
	
	private Random random = new Random();

	/**
	 * Creates a new SimEnemy using the given speed constant, wait duration (in ms),
	 * and x and y starting coordinates (in cm).
	 */
	public SimEnemy(double constant, int wait, double xPos, double yPos){
		Constant = constant;
		this.wait = wait;
		this.xPos = xPos;
		this.yPos = yPos;
		
		thread = new Thread(this);
	}

	/**
	 * Creates a new SimEnemy using default speed constant and default wait duration and
	 * default starting coordinates.
	 */
	public SimEnemy(){
		this(40,500,200,200);
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
		if(!running) {
			xTarget = x;
			yTarget = y;
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void run() {
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
				yPos = yPos - Constant;
			}
			
			if(wind)
				wind();
			
			if(Math.abs(xPos-xTarget) <= 10 && Math.abs(yPos-yTarget) <= 10) {
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

