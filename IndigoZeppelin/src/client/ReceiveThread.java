package client;


import gui.GUIMain;
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
		default:
			break;
		
		}
	}
	
	
	
	public ReceiveThread(Socket sock, GUIMain gui) {
		this.sock = sock;
		this.gui = gui;
		try {
			input = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			gui.showMessage("Error while getting InputStream");
			e.printStackTrace();
		}
	}//end constructor
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
	public void image(Transfer information){
		gui.getGuiCommands().receiveImage(information.getImage());
	}
	public void height(Transfer information){
		gui.getGuiCommands().receiveHeight(information.getHeight());
	}
	
	public void keyPressedEvent(Transfer information){
		if(information.getKey()==Key.ELEVATE){
			gui.getGuiCommands().receivePropellorState(Propellor.UP, true);
		}else if(information.getKey()==Key.LEFT){
			gui.getGuiCommands().receivePropellorState(Propellor.LEFT, true);
			gui.getGuiCommands().receivePropellorState(Propellor.RIGHT, true);
		}else if(information.getKey()==Key.RIGHT){
			gui.getGuiCommands().receivePropellorState(Propellor.LEFT, true);
			gui.getGuiCommands().receivePropellorState(Propellor.RIGHT, true);
		}else if(information.getKey()==Key.UP){
			gui.getGuiCommands().receivePropellorState(Propellor.LEFT, true);
			gui.getGuiCommands().receivePropellorState(Propellor.RIGHT, true);
		}else if(information.getKey()==Key.DOWN){
			gui.getGuiCommands().receivePropellorState(Propellor.LEFT, true);
			gui.getGuiCommands().receivePropellorState(Propellor.RIGHT, true);
		}
	}
	public void keyReleasedEvent(Transfer information){
		if(information.getKey()==Key.ELEVATE){
			gui.getGuiCommands().receivePropellorState(Propellor.UP, false);
		}else if(information.getKey()==Key.LEFT){
			gui.getGuiCommands().receivePropellorState(Propellor.LEFT, false);
			gui.getGuiCommands().receivePropellorState(Propellor.RIGHT, false);
		}else if(information.getKey()==Key.RIGHT){
			gui.getGuiCommands().receivePropellorState(Propellor.LEFT, false);
			gui.getGuiCommands().receivePropellorState(Propellor.RIGHT, false);
		}else if(information.getKey()==Key.UP){
			gui.getGuiCommands().receivePropellorState(Propellor.LEFT, false);
			gui.getGuiCommands().receivePropellorState(Propellor.RIGHT, false);
		}else if(information.getKey()==Key.DOWN){
			gui.getGuiCommands().receivePropellorState(Propellor.LEFT, false);
			gui.getGuiCommands().receivePropellorState(Propellor.RIGHT, false);
		}
	}
	public void message(Transfer information){
		gui.getGuiCommands().receiveMessage(information.getMessage());
	}
	
}//end class recievethread

