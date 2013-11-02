package server;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import transfer.Transfer;

class RecieveFromClientThread implements Runnable
{
	Socket clientSocket;
	private ObjectInputStream input;
	private ReceivedHandler receiveHandler;
	
	public RecieveFromClientThread(Socket clientSocket)
	{	
		this.clientSocket = clientSocket;
		receiveHandler = new ReceivedHandler(clientSocket);
		try {
			input = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			System.out.println("Error getting input stream");
			e.printStackTrace();
		}
	}//end constructor
	
	
	public void run() {
		try{		
			while(true){
				Transfer information;
				while((information = ( Transfer) input.readObject())!= null){//assign message from client to messageString
					receiveHandler.handleRecieved(information);
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


}//end class RecieveFromClientThread
