package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import javax.swing.ImageIcon;

import transfer.Transfer;
import transfer.Transfer.TransferType;

class SendToClientThread implements Runnable
{
	PrintWriter pwPrintWriter;
	Socket clientSock = null;
	private ObjectOutputStream output;
	
	public SendToClientThread(Socket clientSock)
	{
		this.clientSock = clientSock;
		try {
			output = new ObjectOutputStream(clientSock.getOutputStream());
			output.flush();
		} catch (IOException e) {
			System.out.println("Error getting outputStream");
			e.printStackTrace();
		}
		
	}
	public void run() {
		try{
		//pwPrintWriter =new PrintWriter(new OutputStreamWriter(this.clientSock.getOutputStream()));//get outputstream
		
		while(true)
		{
			Transfer information = new Transfer();
			String readString;
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));//get userinput
			readString = input.readLine();//get message to send to client
			information.setMessage(readString);
			boolean exit = false;
			if(readString.equalsIgnoreCase("exit") || readString.equalsIgnoreCase("close")){
				information.setType(TransferType.EXIT);
				exit= true;
			}
			if(readString.equalsIgnoreCase("image")){
				System.out.println("Give the URL of image");
				ImageIcon image = new ImageIcon((input.readLine()));//get message to send to client);
				information.setImage(image);
			}
			//pwPrintWriter.println(msgToClientString);//send message to client with PrintWriter
			//pwPrintWriter.flush();//flush the PrintWriter
			output.writeObject(information);
			output.flush();
			if(exit){
				clientSock.close();
				System.out.println("Socket Closed Exiting Now...");
				System.exit(0);				
			}
		}//end while
		}
		catch(Exception ex){System.out.println(ex.getMessage());}	
	}//end run
}//end class SendToClientThread