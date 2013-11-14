package client;


import gui.GUIMain;
import gui.GuiCommands;
import gui.GuiCommands.Key;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import transfer.Transfer;
import zeppelin.Propellor;

class ReceiveThread implements Runnable
{
	Socket sock;
	private ObjectInputStream input;
	GUIMain gui;
	GuiCommands gc;
	
	/**
	 * Handles the transfer received by looking at its type.
	 * @param information
	 * 			The transfer to be handled.
	 */
	private void handleReceived(Transfer information) {
		switch(information.getTransferType()){
		case EXIT:
			exit(information);
			break;
		case IMAGE:
			image(information);
			break;
		case HEIGHT:
			height(information);
			break;
		case KEYPRESSEDEVENT:
			keyPressedEvent(information);
			break;
		case KEYRELEASEDEVENT:
			keyReleasedEvent(information);
			break;
		case MESSAGE:
			message(information);
			break;
		case PROPELLOR:
			propellor(information);
			break;
		default:
			break;
		
		}
	}
	
	
	/**
	 * Initializes this thread.
	 * @param sock
	 * 			Socket from which the input stream has to be milked.
	 * @param gui
	 * 			Gui to show the messages into.
	 */
	public ReceiveThread(Socket sock, GUIMain gui) {
		this.sock = sock;
		this.gui = gui;
		gc = gui.getGuiCommands();
		try {
			input = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			gui.showMessage("Error while getting InputStream");
			e.printStackTrace();
		}
	}//end constructor
	/**
	 * Waits until something new is received and calls handle received. 
	 * If socket closes unexpectedly then exits after 5 seconds.
	 */
	public void run() {
		try{
			while(true){
				Transfer transferRecieved = null;
				while((transferRecieved = ( Transfer) input.readObject())!= null){
					handleReceived(transferRecieved);
				}
			}
		
		}catch(IOException e){
		e.printStackTrace();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.exit(0);}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}//end run
	/**
	 * Closes the socket to free the port.
	 * @param informatin
	 */
	public void exit(Transfer informatin){
		try {
			this.sock.close();
			gui.showMessage("Sockect Closed By Server. Now Exiting...");
		} catch (IOException e) {
			gui.showMessage("Error while closing socket");
			e.printStackTrace();
		}
		System.exit(0);
	
	}
	
	//Here on code on how the things are handled.
	
	public void image(Transfer information){
		gc.receiveImage(information.getImage());
	}
	public void height(Transfer information){
		gc.receiveHeight(information.getHeight());
	}
	
	public void keyPressedEvent(Transfer information){
		if(information.getKey()==Key.ELEVATE){
			gc.receivePropellorState(Propellor.UP, true);
		}else if(information.getKey()==Key.LEFT){
			gc.receivePropellorState(Propellor.LEFT, true);
			gc.receivePropellorState(Propellor.RIGHT, true);
		}else if(information.getKey()==Key.RIGHT){
			gc.receivePropellorState(Propellor.LEFT, true);
			gc.receivePropellorState(Propellor.RIGHT, true);
		}else if(information.getKey()==Key.UP){
			gc.receivePropellorState(Propellor.LEFT, true);
			gc.receivePropellorState(Propellor.RIGHT, true);
		}else if(information.getKey()==Key.DOWN){
			gc.receivePropellorState(Propellor.LEFT, true);
			gc.receivePropellorState(Propellor.RIGHT, true);
		}
	}
	public void keyReleasedEvent(Transfer information){
		if(information.getKey()==Key.ELEVATE){
			gc.receivePropellorState(Propellor.UP, false);
		}else if(information.getKey()==Key.LEFT){
			gc.receivePropellorState(Propellor.LEFT, false);
			gc.receivePropellorState(Propellor.RIGHT, false);
		}else if(information.getKey()==Key.RIGHT){
			gc.receivePropellorState(Propellor.LEFT, false);
			gc.receivePropellorState(Propellor.RIGHT, false);
		}else if(information.getKey()==Key.UP){
			gc.receivePropellorState(Propellor.LEFT, false);
			gc.receivePropellorState(Propellor.RIGHT, false);
		}else if(information.getKey()==Key.DOWN){
			gc.receivePropellorState(Propellor.LEFT, false);
			gc.receivePropellorState(Propellor.RIGHT, false);
		}
	}
	public void message(Transfer information){
		gc.receiveMessage(information.getMessage());
	}
	
	public void propellor(Transfer information) {
		if(information.getPropellorMode() == Propellor.Mode.OFF)
			gc.receivePropellorState(information.getPropellorId(), false);
		else if(information.getPropellorMode() == Propellor.Mode.ON)
			gc.receivePropellorState(information.getPropellorId(), true);
		else if(information.getPropellorMode() == Propellor.Mode.PWM) {
			if(Math.abs(information.getPropellorPwm()) >740)
				gc.receivePropellorState(information.getPropellorId(), true);
			else
				gc.receivePropellorState(information.getPropellorId(), false);
		}
			
	}
	
}//end class recievethread

