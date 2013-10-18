package client;

import gui.GUIMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import transfer.Transfer;

public class SendToServer {

	GUIMain gui;
	Socket sock=null;
	PrintWriter print=null;
	BufferedReader brinput=null;
	private ObjectOutputStream output;
	public SendToServer(Socket sock, GUIMain gui)
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
		if(sock.isConnected())
		{
			gui.showMessage("Client connected to "+sock.getInetAddress() + " on port "+sock.getPort());
		}
	}//end constructor
	public boolean sendTransfer(Transfer transfer){
		try {
			output.writeObject(transfer);
			output.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
			
	}
}
