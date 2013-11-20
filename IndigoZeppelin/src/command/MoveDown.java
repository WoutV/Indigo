package command;

import zeppelin.MotorController;

public class MoveDown extends Command {

	public MoveDown(double amount) {
		super(amount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		MotorController.getInstance().setCommandIsBeingExecuted(true);
		MotorController.getInstance().moveDownward(amount);
		MotorController.getInstance().setCommandIsBeingExecuted(false);
	}

	

}
