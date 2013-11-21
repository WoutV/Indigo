package command;

import zeppelin.MotorController;

public class MoveBackward extends Command {

	public MoveBackward(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		MotorController.getInstance().setCommandIsBeingExecuted(true);
		MotorController.getInstance().moveDistanceBackward(amount);
		MotorController.getInstance().setCommandIsBeingExecuted(false);
	}

	@Override
	public String toString() {
		return " - Move " + super.amount + "cm backwards!";
	}
	
	
}
