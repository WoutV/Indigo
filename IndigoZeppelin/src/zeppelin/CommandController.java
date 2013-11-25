package zeppelin;

import java.util.LinkedList;

import command.Command;


/**
 * Processed QR code, Wordt gebruikt in main? 
 * Bevat Commando's voor de zeppelin om uit te voeren. (ArrayList)?
 * @author Wout
 *
 */
public class CommandController implements Runnable {

	private boolean autoPilot;
	private LinkedList<Command> commandList;
	private boolean commandIsBeingExecuted;
	
	public CommandController(){
		commandList = new LinkedList<Command>();
	}
	
	@Override
	public void run() {
		while(true){
			
			while(autoPilot){
				
				if(commandList.isEmpty()){

				}else{
					System.out.println("enteredlecommand");
					commandList.pop().execute();
					while(commandIsBeingExecuted){};
				}
			}
		}
		
	}

	public boolean isCommandIsBeingExecuted() {
		return commandIsBeingExecuted;
	}

	public void setCommandIsBeingExecuted(boolean commandIsBeingExecuted) {
		this.commandIsBeingExecuted = commandIsBeingExecuted;
	}

	public boolean isAutoPilot() {
		return autoPilot;
	}

	public void setAutoPilot(boolean autoPilot) {
		this.autoPilot = autoPilot;
	}

	public LinkedList<command.Command> getCommandList() {
		return commandList;
	}

	public void setCommandList(LinkedList<command.Command> commandList) {
		this.commandList = commandList;
	}

}
