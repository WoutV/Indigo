package simulator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class for connecting the Sim to a client, without using a central server.
 *
 */
public class SimConnNoRabbit implements Runnable {
	
	private Simulator sim;
	
	private Socket clientSocket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void sendTransfer(String message,String key){
		try {
			String transfer = key + "#" + message;
			output.writeObject(transfer);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
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
	
	public void handleReceived(String s) {
		String[] p = s.split("#");
		if(p[0].equals("indigo.lcommand.motor1"))
			sim.handleInput(1, Integer.parseInt(p[1]));
		if(p[0].equals("indigo.lcommand.motor2"))
			sim.handleInput(2, Integer.parseInt(p[1]));
	}
}
