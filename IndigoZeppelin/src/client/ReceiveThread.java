package client;


import gui.GUIMain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import transfer.Transfer;
import transfer.Transfer.TransferType;

class ReceiveThread implements Runnable
{
	Socket sock;
	private ObjectInputStream input;
	GUIMain gui;
	
	
	private void handleReceived(Transfer transferReceived) {
		//Closing the socket if it is EXIT type
				if(transferReceived.getTransferType() == Transfer.TransferType.EXIT )
				{
					try {
						this.sock.close();
						gui.showMessage("Sockect Closed By Server. Now Exiting...");
					} catch (IOException e) {
						gui.showMessage("Error while closing socket");
						e.printStackTrace();
					}
					System.exit(0);
				}
				if(transferReceived.getTransferType()==Transfer.TransferType.MESSAGE){
					gui.showMessage(transferReceived.getMessage());
				}
				if(transferReceived.getTransferType()==TransferType.IMAGE){
					gui.setImageDisplay(transferReceived.getImage());
				}
				//What should the system do if it gets information?
		
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
		
		}catch(Exception e){
		e.printStackTrace();
		gui.showMessage("Error receiving");}
	}//end run
	
	
	
}//end class recievethread

