package zeppelin;

public class CommandParser {

	private MotorController mc;
	public CommandParser(MotorController instance) {
		this.mc=instance;
	}

	public void moveDistanceForward(double distance) {
		
		mc.moveForward();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.out.println("How do i sleep?");
			
		}
		mc.stopHorizontalMovement();

		
	}
	
	
	
}
