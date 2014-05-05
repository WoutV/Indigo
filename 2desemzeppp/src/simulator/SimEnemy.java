package simulator;

import java.util.Random;

import map.Map;

/**
 * The SimEnemy: Sim for an enemy zeppelin.
 * The Sim needs a target. It moves slowly towards this target.
 */
public class SimEnemy implements Runnable {
	private double Constant;
	
	private SimEnemyConn simconn;
	private SimEnemyConnNoRabbit simconn2;

	/**
	 * Set this SimEnemy to use a SimEnemyConn using a Rabbit server.
	 * @param sims
	 */
	public void setSimConn(SimEnemyConn sims){
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

	private Map map;
	private boolean running;
	
	//all in cm!!
	private double xPos=200,yPos=200;
	private double xTarget,yTarget;
	
	private int tabletTarget;
	
	private Thread thread;
	
	private Random random = new Random();

	/**
	 * Creates a new SimEnemy using the given speed constant, wait duration (in ms),
	 * and x and y starting coordinates (in cm).
	 */
	public SimEnemy(double constant, int wait, double xPos, double yPos, Map map){
		Constant = constant;
		this.wait = wait;
		this.xPos = xPos;
		this.yPos = yPos;
		this.map = map;
		thread = new Thread(this);
	}

	/**
	 * Creates a new SimEnemy using default speed constant and default wait duration and
	 * default starting coordinates.
	 */
	public SimEnemy(Map map){
		this(40,500,200,200,map);
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
			sendTarget();
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private void sendTarget() {
		String key = "enemy.info.target";
		String info = (int) (xTarget) + "," + (int) (yTarget); 
		if(simconn != null)
			simconn.sendTransfer(info, key);
		if(simconn2 != null)
			simconn2.sendTransfer(info, key);
	}
	
	/**
	 * Receives a new command. Only available when the Sim is at a target (not running).
	 * It makes the Sim move towards the new target.
	 * 
	 * @param 	x
	 * 			X-coordinate of the new target (in cm)
	 * @param 	y
	 * 			Y-coordinate of the new target (in cm)
	 * @param 	tablet
	 * 			No. of the tablet at the new target. 0 if land-target without tablet.
	 */
	public void receiveCommand(int x, int y, int tablet) {
		if(!running) {
			tabletTarget = tablet;
			receiveTarget(x,y);
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
			
			if(tabletTarget != 0) {
				String command = SImQR.decodeQR(tabletTarget);
				
			}
			
		}
	}
	

	private void parseTabletString(String tabletString){
		if(tabletString.contains("position")){
			String[] array = tabletString.split(":");
			String[] position = array[1].split(",");
			receiveTarget(Integer.parseInt(position[0]), Integer.parseInt(position[0]));
		}
		else if(tabletString.contains("tablet")){
			String[] array = tabletString.split(":");
			double[] tabletLoc = map.getTablet(Integer.parseInt(array[1]));
			tabletTarget = Integer.parseInt(array[1]);
			receiveTarget((int)tabletLoc[0], (int)tabletLoc[1]);
		}
	}
	private void sendLoc() {
		String key = "enemy.info.location";
		String info = (int) (xPos*10) + "," + (int) (yPos*10); 
		if(simconn != null)
			simconn.sendTransfer(info, key);
		if(simconn2 != null)
			simconn2.sendTransfer(info, key);
	}
	
}

