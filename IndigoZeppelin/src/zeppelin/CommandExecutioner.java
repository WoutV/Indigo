package zeppelin;

import java.util.HashMap;

public class CommandExecutioner {

	private MotorController mc;
	
	private HashMap<Double,Double[]> distanceMap = new HashMap<Double,Double[]>();
	
	
	private static CommandExecutioner ce = new CommandExecutioner();
	
	private CommandExecutioner() {
		this.mc = MotorController.getInstance();
		initDistanceMap();
	}
	
	
	private void initDistanceMap() {
		Double[] v = {1400.0,325.0};
		distanceMap.put(0.5, v);
		Double[] f = {1100.0,320.0};
		distanceMap.put(1.0, f);
		
	}
	
	private void initTurnMap(){
		
	}


	public static CommandExecutioner getInstance() {
		return ce;
	}

	public void moveDistanceForward(double amount) {
		System.out.println("it works derp");
		mc.setCommandIsBeingExecuted(true);
		mc.moveForward();
		try {
			Thread.sleep((long) amount);
		} catch (InterruptedException e) {
			System.out.println("How do i sleep?");
		}
		mc.stopHorizontalMovement();
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
		
	}


	public void moveDistanceBackward(double amount) {
		mc.setCommandIsBeingExecuted(true);
		//TODO schrijf de methode hier
		
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
		mc.moveToHeight(dest);
		
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
		
	}


	public void turnDegreesLeft(double amount) {
		mc.setCommandIsBeingExecuted(true);
		//TODO schrijf de methode hier
		
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
		
	}


	public void turnDegreesRight(double amount) {
		mc.setCommandIsBeingExecuted(true);
		//TODO schrijf de methode hier
		
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
	}
	
	
	
}
