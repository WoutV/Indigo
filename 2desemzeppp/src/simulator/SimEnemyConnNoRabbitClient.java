package simulator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import navigation.Dispatch;

public class SimEnemyConnNoRabbitClient implements Runnable {

	private Socket sock;
	private ObjectInputStream input;
	
	/**
	 * Initialises a new SimEnemyConnNoRabbitClient.
	 */
	public SimEnemyConnNoRabbitClient() {
		try {
			sock = new Socket("127.0.0.1",6787);
			
			input = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Listens for messages from the SimEnemy.
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
		if(p[0].equals("enemy.loc.loc")) {
			String[] loc = p[1].split(",");
			Dispatch.receiveEnemyLoc(Integer.parseInt(loc[0]),Integer.parseInt(loc[1]));
		}

	}

}
