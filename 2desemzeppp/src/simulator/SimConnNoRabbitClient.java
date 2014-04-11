package simulator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import navigation.Dispatch;

/**
 * Class for connecting a client to a Sim, without needing a central server.
 * It uses sockets to send messages. Port 6789 is used.
 * A point-to-point connection with a SimConnNoRabbit should be created.
 * SimConnNoRabbit should be started first.
 * It allows a client to send messages to a Sim and listens for messages from a Sim.
 * It receives symbollist messages and sends them to a Dispatch.
 * It uses the same format as a central server.
 */
public class SimConnNoRabbitClient implements Runnable {

	private Socket sock;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	/**
	 * Initialises a new SimConnNoRabbitClient.
	 */
	public SimConnNoRabbitClient() {
		try {
			sock = new Socket("127.0.0.1",6789);
		
			output = new ObjectOutputStream(sock.getOutputStream());
			output.flush();
			
			input = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a message to the Sim.
	 * @param message
	 * @param key
	 */
	public synchronized void sendTransfer(String message,String key){
		try {
			String transfer = key + "#" + message;
			output.writeObject(transfer);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Listens for messages from the Sim. Symbollist messages are send to a Dispatch.
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
		String[] p = s.split("#");
		//System.out.println(s);
		if(p[0].equals("indigo.private.symbollist"))
			Dispatch.processSymbols(Simulator.StringToSymbolList(p[1]));

	}

}
