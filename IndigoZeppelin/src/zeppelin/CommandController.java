package zeppelin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import com.google.zxing.NotFoundException;

import camera.Camera;

import command.Command;
import command.MoveForward;
import command.TurnLeft;
import command.TurnRight;


/**
 * Bevat Commando's voor de zeppelin om uit te voeren.
 */
public class CommandController implements Runnable {

	private Boolean autoPilot=false;
	private Integer autoPilotInteger=0;
	private LinkedList<Command> commandList;
	private Boolean commandIsBeingExecuted;

	public CommandController(){
		commandList = new LinkedList<Command>();
	}

	boolean correction =true;
	@Override
	public  void run() {
		System.out.println("CommandController started");
		while(true){
			while(autoPilotInteger==0){
				try {
					System.out.println("AutoPilot is off. Waiting to turn it on");
					synchronized(autoPilot) {
						autoPilot.wait();
					}
					System.out.println("AutoPilot is on. Continueing");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(commandList.isEmpty()){
				//empty --> take new picture to check for more commands
				QRParser.parseQR();
				if(correction){
					try {
						double degreeDistance[] = Camera.getDegreeAndDistanceQR();
						double distanceToMove = 0;
						if((25+12.5)>degreeDistance[1]&& degreeDistance[1]>12.5){
							distanceToMove = 25;
						}
						if(degreeDistance[1]>(25+12.5)){
							distanceToMove = 50;
						}
						double amountToTurn=0;
						if(22.5>degreeDistance[0]){
							amountToTurn=0;
						}
						else if((45+22.5)>degreeDistance[0]){
							amountToTurn = 45;
						}
						else if((90+22.5)>degreeDistance[0]){
							amountToTurn = 90;
						}
						else if((90+45+22.5)<degreeDistance[0]){
							amountToTurn= 90+45;
						}
						else{
							amountToTurn=180;
						}
						Command cmd=null;
						if(degreeDistance[2]==1){
							cmd = new TurnRight(amountToTurn);
						}
						else{
							cmd= new TurnLeft(amountToTurn);
						}
						cmd.execute();
						cmd = new MoveForward(distanceToMove);
						cmd.execute();

					}
					catch (NotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			System.out.println("enteredlecommand:" + commandList.peek());
			Command cmd = commandList.pop();
			System.out.println("executing " + cmd.toString());
			cmd.execute();
			System.out.println("waiting for command to fin.");
			//				while(commandIsBeingExecuted){
			//					try {
			//						synchronized(commandIsBeingExecuted) {
			//							commandIsBeingExecuted.wait();
			//						}
			//					} catch (InterruptedException e) {
			//						// TODO Auto-generated catch block
			//						e.printStackTrace();
			//					}
			//				};

		}


	}

	public boolean isCommandIsBeingExecuted() {
		return commandIsBeingExecuted;
	}

	public void setCommandIsBeingExecuted(boolean commandIsBeingExecuted) {
		this.commandIsBeingExecuted = commandIsBeingExecuted;
		synchronized(this.commandIsBeingExecuted) {
			this.commandIsBeingExecuted.notify();
		}
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
		System.out.println("Changing auto pilot to true");
		//		autoPilot=true;
		autoPilotInteger=1;
		System.out.println("auto pilot changed to true. Notifying autoPilot.");
		synchronized(autoPilot) {
			autoPilot.notify();
		}
		System.out.println("notified.");

	} 
	private  void setAutoPilotFalse(){
		//		autoPilot=false;
		autoPilotInteger=0;

	}

	public LinkedList<command.Command> getCommandList() {
		return commandList;
	}

	public void setCommandList(LinkedList<command.Command> commandList) {
		this.commandList = commandList;
	}

	public synchronized void addCommand(Command command) {
		commandList.add(command);
		synchronized(commandList){
			commandList.notify();
		}

	}
	public synchronized void addCommands(LinkedList<Command> toAddCommands) {
		while(!toAddCommands.isEmpty()){
			Command cm= toAddCommands.pop();
			commandList.add(cm);
			System.out.println("addingCommand: "+cm.toString());
		}
		synchronized(commandList){
			commandList.notify();
		}

	}

}
