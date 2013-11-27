package zeppelin;

import java.util.HashMap;

public class CommandExecutioner {

	private MotorController mc;
	
	private HashMap<Double,Double> distanceMap = new HashMap<Double,Double>();
	
	private static CommandExecutioner ce = new CommandExecutioner();
	
	private CommandExecutioner() {
		this.mc = MotorController.getInstance();
		initDistanceMap();
	}
	
	
	private void initDistanceMap() {
		distanceMap.put(10.0, null);
		distanceMap.put(20.0, null);
		distanceMap.put(25.0, null);
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


	public void moveDownward(double amount) {
		mc.setCommandIsBeingExecuted(true);
		//TODO schrijf de methode hier
		
		mc.updateCommandList();
		mc.setCommandIsBeingExecuted(false);
	}


	public void moveUpward(double amount) {
		mc.setCommandIsBeingExecuted(true);
		//TODO schrijf de methode hier
		
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
