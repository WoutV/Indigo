package zeppelin;

import java.util.LinkedList;

import command.Command;


/**
 * Processed QR code, Wordt gebruikt in main? 
 * Bevat Commando's voor de zeppelin om uit te voeren. (ArrayList)?
 * @author Wout
 *
 */
public class CommandThread implements Runnable {

	private boolean autoPilot;
	private LinkedList<Command> commandList;
	
	public CommandThread(){
		commandList = new LinkedList<Command>();
	}
	
	@Override
	public void run() {
		
		while(autoPilot){
			if(commandList.isEmpty()){
				
			}else{
				commandList.get(0).execute();
			}
		}
		
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
