package command;

import zeppelin.CommandExecutioner;
import zeppelin.MotorController;

public abstract class Command {

	protected CommandExecutioner ce = CommandExecutioner.getInstance();

	protected double amount;

	public Command(double amount) {
		this.amount = amount;
	}

	public abstract void execute();

	public abstract String toString();
}
