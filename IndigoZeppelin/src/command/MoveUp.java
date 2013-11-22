package command;

import zeppelin.MotorController;

public class MoveUp extends Command {

	public MoveUp(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		ce.moveUpward(amount);
	}
	
	@Override
	public String toString() {
		return " - Move " + super.amount + "cm upwards!";
	}

}
