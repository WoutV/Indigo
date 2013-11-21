package command;

import zeppelin.MotorController;

public class TurnRight extends Command {

	public TurnRight(double amount) {
		super(amount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		MotorController.getInstance().setCommandIsBeingExecuted(true);
		MotorController.getInstance().turnDegreesRight(amount);
		MotorController.getInstance().setCommandIsBeingExecuted(false);
	}
	
	@Override
	public String toString() {
		return " - Turn " + super.amount + "degrees right!";
	}

}
