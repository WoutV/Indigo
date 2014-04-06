package simulator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import navigation.Dispatch;

/**
 * Class for connecting a client to a Sim, without needing a cetral server.
 *
 */
public class SimConnNoRabbitClient {

	private Socket sock;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	
	public SimConnNoRabbitClient() {
		try {
			sock = new Socket("127.0.0.1",6789);
		
			output = new ObjectOutputStream(sock.getOutputStream());
			output.flush();
			
			input = new ObjectInputStream(sock.getInputStream());
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
		if(p[0].equals("indigo.private.symbollist"))
			Dispatch.processSymbols(Simulator.StringToSymbolList(p[1]));

	}

}
