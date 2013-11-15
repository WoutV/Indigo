package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import transfer.Transfer;
import transfer.Transfer.TransferType;

public class SendToClient {
	
	private Socket sock;
	private ObjectOutputStream output;
	/**
	 * Gets the outputstream to write the objects into.
	 * @param sock
	 * 			Socket from which outputstream has to be initialized.
	 */
	public SendToClient(Socket sock){
		this.sock = sock;
		try {
			output = new ObjectOutputStream(sock.getOutputStream());
			output.flush();
			toSendTransfers = new ArrayList<Transfer>();
		} catch (IOException e) {
			System.out.println("Error getting output stream");
			e.printStackTrace();
		}
	}//end constructor
	
	private ArrayList<Transfer> toSendTransfers;
	
	boolean busy;
	/**
	 * Writes the given object to the outputstream. If transfer type is exit then
	 * it also closes the socket after sending the exit transfer.
	 * @param transfer
	 * 			Transfer to be sent.
	 * @return
	 * 			Returns true if the item has been sent succesfully.
	 */
	public void sendTransfer(Transfer transfer){
		try {
			if(busy){
				toSendTransfers.add(transfer);
				return;
			}
			
			busy=true;
			output.writeObject(transfer);
			output.flush();
			if(transfer.getTransferType()==TransferType.EXIT)
				closeSocket();
			while(!toSendTransfers.isEmpty()){
				Transfer trans = toSendTransfers.get(0);
				output.writeObject(trans);
				output.flush();
				toSendTransfers.remove(trans);
			}
			busy=false;
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	/**
	 * Closes the socket in order to free the port and to prevent broken pipe exception.
	 */
	private void closeSocket(){
		try {
			sock.close();
			System.out.println("Socket Closed Exiting Now...");
			System.exit(0);				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	

}
