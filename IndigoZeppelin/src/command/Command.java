package command;

public abstract class Command {
	
	protected double amount;
	
	public Command(double amount) {
		this.amount = amount;
	}
	
	public abstract void execute();
	public abstract String toString();
}
