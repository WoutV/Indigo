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

	private Boolean autoPilot=false;
	private LinkedList<Command> commandList;
	private boolean commandIsBeingExecuted;

	public CommandController(){
		commandList = new LinkedList<Command>();
	}

	@Override
	public void run() {
		while(true){
			if(!autoPilot){
				try {
					autoPilot.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(commandList.isEmpty()){

			}else{
				System.out.println("enteredlecommand");
				commandList.pop().execute();
				while(commandIsBeingExecuted){};
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

	public void setAutoPilot(boolean autoPilott) {
		this.autoPilot = autoPilott;
		if(autoPilott)
		autoPilot.notify();
	}

	public LinkedList<command.Command> getCommandList() {
		return commandList;
	}

	public void setCommandList(LinkedList<command.Command> commandList) {
		this.commandList = commandList;
	}
	
	public void addCommand(Command command) {
		commandList.add(command);
	}

}
