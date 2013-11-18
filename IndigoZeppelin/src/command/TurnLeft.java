package command;

import zeppelin.MotorController;

public class TurnLeft implements Command {
	
	private double amount;

	public TurnLeft(double amount) {
		this.amount = amount;
	}

	@Override
	public void execute() {
		MotorController.getInstance().turnDegreesLeft(amount);
	}

}
