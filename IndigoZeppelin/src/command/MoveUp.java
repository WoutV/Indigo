package command;

import zeppelin.MotorController;

public class MoveUp extends Command {

	public MoveUp(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		MotorController.getInstance().moveUpward(amount);
	}

}
