package command;

import zeppelin.MotorController;

public class TurnRight extends Command {

	public TurnRight(double amount) {
		super(amount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		ce.turnDegreesRight(amount);
	}
	
	@Override
	public String toString() {
		return " - Turn " + super.amount + "degrees right!";
	}

}
