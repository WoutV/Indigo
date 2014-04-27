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

	
	//all in cm!!
	private double xPos=200,yPos=200;
	
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
	 * Creates a random wind effect: between -2 cm and 2 cm, alpha between -10 and 10.
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
	
	
}

