package command;


public class MoveUp extends Command {

	public MoveUp(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		ce.moveVertically(amount);
	}
	
	@Override
	public String toString() {
		return " - Move " + super.amount + "cm upwards!";
	}

}
