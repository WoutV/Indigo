package command;

public class MoveForward extends Command {

	public MoveForward(double amount) {
		super(amount);
	}

	@Override
	public void execute() {
		System.out.println("commandexecute");
		ce.moveDistanceForward(amount);
	}

	@Override
	public String toString() {
		return " - Move " + super.amount + "cm forwards!";
	}
	
}
