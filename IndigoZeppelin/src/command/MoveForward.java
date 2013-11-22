package command;

import zeppelin.CommandExecutioner;
import zeppelin.MotorController;

public class MoveForward extends Command {

	public MoveForward(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		ce.moveDistanceForward(amount);
	}

	@Override
	public String toString() {
		return " - Move " + super.amount + "cm forwards!";
	}
	
}
