package simulator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class for connecting the Sim to a client, without using a central server.
 * It uses sockets to send messages. Port 6789 is used.
 * A point-to-point connection with a SimConnNoRabbitClient should be created,
 * and the SimConnNoRabbit should run first.
 * It receives "indigo.lcommand.motor1" and "indigo.lcommand.motor2" and sends them to the Sim.
 * It sends "indigo.private.symbollist" messages to a client.
 * It uses the same format as a central server.
 */
public class SimConnNoRabbit implements Runnable {
	
	private Simulator sim;
	
	private Socket clientSocket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	/**
	 * Initialises a new SimConnNoRabbit. Then waits for a connection from a client.
	 */
	public SimConnNoRabbit(Simulator sim) {
		this.sim = sim;
		
		ServerSocket ss;
		try {
			ss = new ServerSocket(6789);
			clientSocket = ss.accept();
		
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			output.flush();
			
			input = new ObjectInputStream(clientSocket.getInputStream());
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
	
	/**
	 * Listens for messages from the Client.
	 */
	public void run() {
		while(true){
			try{
				String transferRecieved = null;
				while((transferRecieved = (String) input.readObject())!= null){
					handleReceived(transferRecieved);
				}
			}
			catch (Exception exc) {
				exc.printStackTrace();
				return;
			}
		}
	}
	
	private void handleReceived(String s) {
		//System.out.println("received: " + s);
		String[] p = s.split("#");
		if(p[0].equals("indigo.lcommand.motor1"))
			sim.handleInput(1, Integer.parseInt(p[1]));
		if(p[0].equals("indigo.lcommand.motor2"))
			sim.handleInput(2, Integer.parseInt(p[1]));
	}
}
