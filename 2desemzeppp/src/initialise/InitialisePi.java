package initialise;
import javax.swing.ImageIcon;

import camera.Camera;

import transfer.Transfer;
import zeppelin.Main;
import connection.*;
public class InitialisePi {
public static void main(String[] args) {
//	ReceiverPi receiverPi = new ReceiverPi();
//	Thread ReceiverPiThread = new Thread(receiverPi);
//	ReceiverPiThread.start();
	System.out.println("Sender Starting");
	SenderPi sender = new SenderPi();
	System.out.println("Sender Initalized, making new transfer");
	Transfer transfer = new Transfer();
	System.out.println("Transfer has been made, setting image");
	transfer.setImage(Camera.getImage());
	System.out.println("message set, sending transfer");
	sender.sendTransfer(transfer);
	System.out.println("Transfer sent!");
	sender.exit();
	//Main main = Main.getInstance();
	//main.init(sender);
	// Add more swag here :p 
}
}
