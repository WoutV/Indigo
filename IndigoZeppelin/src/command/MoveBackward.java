package command;

import zeppelin.MotorController;

public class MoveBackward extends Command {

	public MoveBackward(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		MotorController.getInstance().moveDistanceBackward(amount);
	}
	
	
}
