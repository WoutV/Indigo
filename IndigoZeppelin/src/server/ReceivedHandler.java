package server;

import java.io.IOException;
import zeppelin.*;
import java.net.Socket;

import transfer.Transfer;
import zeppelin.Main;

public class ReceivedHandler {
	private Socket clientSocket;
	private Main main = Main.getInstance();
	private MotorController mc;
	/**
	 * This class handles the transfers received in server.
	 * @param sock
	 * 		Socket to be closed when an exit type transfer is received.
	 */
	public ReceivedHandler(Socket sock){
		this.clientSocket = sock;
		mc = main.getMotorController();
	}
	/**
	 * Does what the method says. handles received transfer.
	 *
	 * @param information
	 * 			The transfer to be handled.
	 */
	public void handleReceived(Transfer information) {
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
		case PWM:
			main.setFloatPwm(information.getPwm());
			break;
		case PWMTOGGLE:
			main.searchFloatPwm();
			break;
		case FLYMODE:
			main.changeFlyMode(information.isAutoPilot());
		default:
			break;
		
		}
	}
	/**
	 * This method is called when an exit type transfer is received. 
	 * It closes the socket to free the port and to prevent broken pipe exception.
	 * @param informatin
	 *			Doesnt actually need this.
	 */
	private void exit(Transfer informatin){
		try {
			this.clientSocket.close();
			System.out.println("Socket Closed By Client Now Exiting...");
			
		} catch (IOException e) {
			System.out.println("Error while closing socket");
			e.printStackTrace();
		}
		System.exit(0);
	
	}
	/**
	 * This method is never called since no image shall be sent to server.
	 * @param information
	 */
	private void image(Transfer information){
		
	}
	/**
	 * Calls this method when information about height is received. 
	 * @param information
	 */
	private void height(Transfer information){
		mc.moveToHeight(information.getHeight());
	}
	
	/**
	 * Calls this method when a key is pressed in the gui.
	 * @param information
	 */
	private void keyPressedEvent(Transfer information){
		main.processPressedKeyEvent(information.getKey());
		System.out.println("Key Pressed Event:" + information.getKey().toString());
	}
	/**
	 * Calls this method when a key is released in the gui.
	 * @param information
	 */
	private void keyReleasedEvent(Transfer information){
		main.processReleasedKeyEvent(information.getKey());
		System.out.println("Key Released Event:"+ information.getKey().toString());
	}
	/**
	 * This method is never called since no message is sent from the client or gui.
	 * @param information
	 */
	private void message(Transfer information){
		System.out.println("Message Received: \n "+ information.getMessage());
	}

	
}
