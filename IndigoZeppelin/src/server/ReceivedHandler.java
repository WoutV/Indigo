package server;

import java.io.IOException;
import java.net.Socket;

import transfer.Transfer;
import zeppelin.Main;

public class ReceivedHandler {
	private Socket clientSocket;
	public ReceivedHandler(Socket sock){
		this.clientSocket = sock;
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
		System.out.println("Client is not supposed to send this type of information");
	}
	private void height(Transfer information){
		System.out.println("Height information received. \n New height:"+ information.getHeight());
	}
	
	private void keyPressedEvent(Transfer information){
		Main.processPressedKeyEvent(information.getKey());
		System.out.println("Key Pressed Event:" + information.getKey().toString());
	}
	private void keyReleasedEvent(Transfer information){
		Main.processReleasedKeyEvent(information.getKey());
		System.out.println("Key Released Event:"+ information.getKey().toString());
	}
	private void message(Transfer information){
		System.out.println("Message Received: \n "+ information.getMessage());
	}

	
}
