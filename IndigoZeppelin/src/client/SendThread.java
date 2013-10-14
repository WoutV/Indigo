package client;


import gui.GUIMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import transfer.Transfer;
import transfer.Transfer.TransferType;

class SendThread implements Runnable
{	
	GUIMain gui;
	Socket sock=null;
	PrintWriter print=null;
	BufferedReader brinput=null;
	private ObjectOutputStream output;
	public SendThread(Socket sock, GUIMain gui)
	{
		this.sock = sock;
		this.gui = gui;
		try {
			output = new ObjectOutputStream(sock.getOutputStream());
			output.flush();
		} catch (IOException e) {
			gui.showMessage("Error getting output stream");
			e.printStackTrace();
		}
		
	}//end constructor
	public void run(){
		try{
		if(sock.isConnected())
		{
			gui.showMessage("Client connected to "+sock.getInetAddress() + " on port "+sock.getPort());
			//this.print = new PrintWriter(sock.getOutputStream(), true);	
		while(true){
			brinput = new BufferedReader(new InputStreamReader(System.in));
			Transfer information= new Transfer();
			String readString = brinput.readLine();
			boolean exit=false;
			information.setMessage(readString);
			if(readString.equalsIgnoreCase("exit") || readString.equalsIgnoreCase("close")){
				information.setType(TransferType.EXIT);
				exit= true;
			}
			//this.print.println(msgtoServerString);
			//this.print.flush();
			output.writeObject(information);
			output.flush();
			if(exit){
				sock.close();
				gui.showMessage("Socket Closed Exiting Now....");
				System.exit(0);
			}
			}//end while
		}}catch(Exception e){e.printStackTrace(); gui.showMessage("Error Sending");}
	}//end run method
}//end class
