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

	public void moveDistanceForward(double distance) {
		mc.setCommandIsBeingExecuted(true);
		mc.moveForward();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("How do i sleep?");
			
		}
		mc.stopHorizontalMovement();

		mc.setCommandIsBeingExecuted(false);
	}


	public void moveDistanceBackward(double amount) {
		// TODO Auto-generated method stub
		
	}


	public void moveDownward(double amount) {
		// TODO Auto-generated method stub
		
	}


	public void moveUpward(double amount) {
		// TODO Auto-generated method stub
		
	}


	public void turnDegreesLeft(double amount) {
		// TODO Auto-generated method stub
		
	}


	public void turnDegreesRight(double amount) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
