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
	public ReceivedHandler(Socket sock){
		this.clientSocket = sock;
		mc = main.getMotorController();
	}
	
	public void handleRecieved(Transfer information) {
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
		default:
			break;
		
		}
	}
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
	private void image(Transfer information){
		
	}
	private void height(Transfer information){
		mc.moveToHeight(information.getHeight());
	}
	
	private void keyPressedEvent(Transfer information){
		main.processPressedKeyEvent(information.getKey());
		System.out.println("Key Pressed Event:" + information.getKey().toString());
	}
	private void keyReleasedEvent(Transfer information){
		main.processReleasedKeyEvent(information.getKey());
		System.out.println("Key Released Event:"+ information.getKey().toString());
	}
	private void message(Transfer information){
		System.out.println("Message Received: \n "+ information.getMessage());
	}

	
}
