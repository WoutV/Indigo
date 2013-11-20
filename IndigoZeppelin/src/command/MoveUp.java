package command;

import zeppelin.MotorController;

public class MoveUp extends Command {

	public MoveUp(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		MotorController.getInstance().setCommandIsBeingExecuted(true);
		MotorController.getInstance().moveUpward(amount);
		MotorController.getInstance().setCommandIsBeingExecuted(false);
	}

}
