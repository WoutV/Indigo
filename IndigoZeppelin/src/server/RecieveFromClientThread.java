package server;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import transfer.Transfer;

class RecieveFromClientThread implements Runnable
{
	Socket clientSocket;
	private ObjectInputStream input;

	
	private void handleRecieved(Transfer information) {
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
	
	
	public RecieveFromClientThread(Socket clientSocket)
	{	
		this.clientSocket = clientSocket;
		try {
			input = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			System.out.println("Error getting input stream");
			e.printStackTrace();
		}
	}//end constructor
	
	
	public void run() {
		try{
		//brBufferedReader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));		
			while(true){
				Transfer information;
				while((information = ( Transfer) input.readObject())!= null){//assign message from client to messageString
					handleRecieved(information);
				}
			}
		
		}
		catch(Exception ex){
			System.out.println("Client has closed the socket. Exiting now");
			try {
				this.clientSocket.close();
			} catch (IOException e) {}
			System.exit(0);
		}
	}


	public void exit(Transfer informatin){
		try {
			this.clientSocket.close();
			System.out.println("Socket Closed By Client Now Exiting...");
			
		} catch (IOException e) {
			System.out.println("Error while closing socket");
			e.printStackTrace();
		}
		System.exit(0);
	
	}
	public void image(Transfer information){
		System.out.println("Client is not supposed to send this type of information");
	}
	public void height(Transfer information){
		System.out.println("Height information received. \n New height:"+ information.getHeight());
	}
	
	public void keyPressedEvent(Transfer information){
		System.out.println("Key Pressed Event:" + information.getKey().toString());
	}
	public void keyReleasedEvent(Transfer information){
		System.out.println("Key Released Event:"+ information.getKey().toString());
	}
	public void message(Transfer information){
		System.out.println("Message Received: \n "+ information.getMessage());
	}
}//end class RecieveFromClientThread
