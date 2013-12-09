package command;


public class TurnLeft extends Command {



	public TurnLeft(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		ce.turnDegreesLeft(amount);
	}

	@Override
	public String toString() {
		return " - Turn " + super.amount + "degrees left!";
	}
}
