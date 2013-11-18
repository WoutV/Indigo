package command;

import zeppelin.MotorController;

public class TurnLeft extends Command {



	public TurnLeft(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		MotorController.getInstance().turnDegreesLeft(amount);
	}

}
