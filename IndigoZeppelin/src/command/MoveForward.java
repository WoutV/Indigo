package command;

import zeppelin.MotorController;

public class MoveForward extends Command {

	public MoveForward(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		MotorController.getInstance().moveDistanceForward(amount);
	}

	
}
