package simulator;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimEnemyConnNoRabbit {
	
	private Socket clientSocket;
	private ObjectOutputStream output;
	
	/**
	 * Initialises a new SimEnemyConnNoRabbit. Then waits for a connection from a client.
	 */
	public SimEnemyConnNoRabbit() {
		ServerSocket ss;
		try {
			ss = new ServerSocket(6787);
			clientSocket = ss.accept();
		
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a message to the Client.
	 * @param message
	 * @param key
	 */
	public synchronized void sendTransfer(String message,String key){
		try {
			String transfer = key + "#" + message;
			//ystem.out.println("sending:" + transfer);
			output.writeObject(transfer);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
