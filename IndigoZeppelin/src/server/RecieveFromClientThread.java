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
		//Closing the socket if it is EXIT type
		if(information.getTransferType() == Transfer.TransferType.EXIT )
		{
			try {
				this.clientSocket.close();
				System.out.println("Socket Closed By Client Now Exiting...");
				
			} catch (IOException e) {
				System.out.println("Error while closing socket");
				e.printStackTrace();
			}
			System.exit(0);
		}
		//What should the system do if it gets information?
		if(information.getTransferType()==Transfer.TransferType.MESSAGE){
			System.out.println(information.getMessage());
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
			ex.printStackTrace();
			System.out.println("Error receiving information, socket has been closed unexpectedly");}
	}



}//end class RecieveFromClientThread
