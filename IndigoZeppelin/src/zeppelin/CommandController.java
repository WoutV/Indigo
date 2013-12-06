package zeppelin;

import java.util.LinkedList;

import command.Command;


/**
 * Bevat Commando's voor de zeppelin om uit te voeren.
 */
public class CommandController implements Runnable {

	private Boolean autoPilot=false;
	private LinkedList<Command> commandList;
	private boolean commandIsBeingExecuted;

	public CommandController(){
		commandList = new LinkedList<Command>();
	}

	@Override
	public  void run() {
		while(true){
			if(!autoPilot){
				try {
					synchronized(autoPilot) {
					autoPilot.wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(commandList.isEmpty()){
				//empty --> take new picture to check for more commands
				QRParser.parseQR();
				try {
					commandList.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				System.out.println("enteredlecommand:" + commandList.getFirst());
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

	public   void setAutoPilot(boolean autoPilott) {
		if(autoPilott){
			setAutoPilotTrue();
		}
		else{
			setAutoPilotFalse();
		}
		
	}
	private  void setAutoPilotTrue(){
		autoPilot=true;
		synchronized(autoPilot) {
			autoPilot.notify();
			}
		
	} 
	private  void setAutoPilotFalse(){
		autoPilot=false;
		synchronized(commandList) {
			commandList.notify();
			}
		
	}
	
	public LinkedList<command.Command> getCommandList() {
		return commandList;
	}

	public void setCommandList(LinkedList<command.Command> commandList) {
		this.commandList = commandList;
	}

	public synchronized void addCommand(Command command) {
		commandList.add(command);
		commandList.notify();
		
	}

}
