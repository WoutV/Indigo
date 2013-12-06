package command;


public class MoveDown extends Command {

	public MoveDown(double amount) {
		super(amount);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		ce.moveVertically(-1*amount);
	}
	
	@Override
	public String toString() {
		return " - Move " + super.amount + "cm downwards!";
	}

	

}
