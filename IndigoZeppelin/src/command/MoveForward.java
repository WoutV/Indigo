package command;

import zeppelin.MotorController;

public class MoveForward extends Command {

	public MoveForward(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		MotorController.getInstance().setCommandIsBeingExecuted(true);
		MotorController.getInstance().moveDistanceForward(amount);
		MotorController.getInstance().setCommandIsBeingExecuted(false);
	}

	@Override
	public String toString() {
		return " - Move " + super.amount + "cm forwards!";
	}
	
}
