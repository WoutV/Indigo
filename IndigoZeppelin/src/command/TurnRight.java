package command;

import zeppelin.MotorController;

public class TurnRight extends Command {

	public TurnRight(double amount) {
		super(amount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		MotorController.getInstance().turnDegreesRight(amount);
	}

}
