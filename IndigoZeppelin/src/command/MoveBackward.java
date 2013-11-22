package command;

import zeppelin.MotorController;

public class MoveBackward extends Command {

	public MoveBackward(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		ce.moveDistanceBackward(amount);
	}

	@Override
	public String toString() {
		return " - Move " + super.amount + "cm backwards!";
	}
	
	
}
