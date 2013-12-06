package zeppelin;

import java.util.HashMap;

public class CommandExecutioner {

	private MotorController mc;
	
	/**
	 * Destance forward: distance (cm) -> [time to move forward (ms),backward in percent of time forward
	 * (0.5=50 percent)]
	 */
	private HashMap<Double,Double[]> distanceMap = new HashMap<Double,Double[]>();
	
	/**
	 * angle -> [time (ms),opposite direction (percent),amount of runs, time between runs]
	 */
	private HashMap<Double,Double[]> turnMap = new HashMap<Double,Double[]>();

	private static CommandExecutioner ce = new CommandExecutioner();
	
	private CommandExecutioner() {
		this.mc = MotorController.getInstance();
		initDistanceMap();
		initTurnMap();
	}
	
	
	private void initDistanceMap() {
		Double[] v = {1400.0,3.25};
		Double[] f = {1100.0,3.20};
		distanceMap.put(100.0, v);
		distanceMap.put(50.0, f);
	}
	
	private void initTurnMap(){
		Double[] v= {1000.0,0.65,2.0,15.0};
		Double[] f= {1000.0,0.60,1.0,20.0};
		turnMap.put(180.0, v);
		turnMap.put(90.0, f);
	}


	public static CommandExecutioner getInstance() {
		return ce;
	}

	public void moveDistanceForward(double amount) {
		System.out.println("it works derp");
		mc.setCommandIsBeingExecuted(true);
		while(amount >= 100) {
			mc.moveForward();
			try {
				Thread.sleep(distanceMap.get(100.0)[0].longValue());
				mc.moveBackward();
				Thread.sleep((long)(distanceMap.get(100.0)[0]*distanceMap.get(100.0)[1]));
				mc.stopHorizontalMovement();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			amount-=100;
		}
		while(amount >= 50) {
			mc.moveForward();
			try {
				Thread.sleep(distanceMap.get(50.0)[0].longValue());
				mc.moveBackward();
				Thread.sleep((long)(distanceMap.get(50.0)[0]*distanceMap.get(50.0)[1]));
				mc.stopHorizontalMovement();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			amount-=50;
		}
		mc.stopHorizontalMovement();
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
		
	}


	public void moveDistanceBackward(double amount) {
		mc.setCommandIsBeingExecuted(true);
		turnDegreesLeft(180.0);
		moveDistanceForward(amount);
		turnDegreesLeft(180.0);
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
	}

	/**
	 * Moves the zeppelin vertically by the specified amount (in cm)
	 * @param amount
	 */
	public void moveVertically(double amount) {
		mc.setCommandIsBeingExecuted(true);
		double currentHeight = Main.getInstance().getDistanceSensor().getHeight();
		double dest = currentHeight + amount;
		if(dest<15)
			dest=15;
		if(dest>250)
			dest=250;
		mc.moveToHeight(dest);
		
		while(Math.abs(dest-Main.getInstance().getDistanceSensor().getHeight()) > 5) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				
			}
		}
		
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
		
	}


	public void turnDegreesLeft(double amount) {
		mc.setCommandIsBeingExecuted(true);
		if(Math.abs(amount-90.0)<=0.001) {
			mc.turnLeft();
			try {
				Thread.sleep(turnMap.get(90.0)[0].longValue());
				mc.stopHorizontalMovement();
				mc.turnRight();
				Thread.sleep((long)(turnMap.get(90.0)[0]*turnMap.get(90.0)[1]));
				mc.stopHorizontalMovement();
				Thread.sleep(turnMap.get(90.0)[3].longValue());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(Math.abs(amount-180.0)<=0.001) {
			for(int i=0;i<2;i++) {
				mc.turnLeft();
				try {
					Thread.sleep(turnMap.get(180.0)[0].longValue());
					mc.stopHorizontalMovement();
					mc.turnRight();
					Thread.sleep((long)(turnMap.get(180.0)[0]*turnMap.get(180.0)[1]));
					mc.stopHorizontalMovement();
					Thread.sleep(turnMap.get(180.0)[3].longValue());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
		
	}


	public void turnDegreesRight(double amount) {
		mc.setCommandIsBeingExecuted(true);
		if(Math.abs(amount-90.0)<=0.001) {
			mc.turnRight();
			try {
				Thread.sleep(Long.parseLong(turnMap.get(90.0)[0]+""));
				mc.stopHorizontalMovement();
				mc.turnLeft();
				Thread.sleep(Long.parseLong(turnMap.get(90.0)[0]*turnMap.get(90.0)[1]+""));
				mc.stopHorizontalMovement();
				Thread.sleep(Long.parseLong(turnMap.get(90.0)[3]+""));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(Math.abs(amount-180.0)<=0.001) {
			turnDegreesLeft(180.0);
		}
		
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
	}
	
	
	
}
