package simulator;

import java.io.IOException;
import java.io.ObjectInputStream;
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
					System.out.println("Something received");
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
//		System.out.println(s);
		if(p[0].equals("enemy.info.location")) {
			String[] loc = p[1].split(",");
//			System.out.println("sS "+(int)Integer.parseInt(loc[0]) +""+ (int)Integer.parseInt(loc[1]));
			Dispatch.receiveEnemyLoc((int)Integer.parseInt(loc[0]),(int)Integer.parseInt(loc[1]));
		}
		else if(p[0].equals("enemy.info.target")){
			String[] deel = p[1].split(",");
			Dispatch.receiveEnemyTarget(Integer.parseInt(deel[0]),Integer.parseInt(deel[1]) );
		}

	}

}
