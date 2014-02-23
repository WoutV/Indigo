package initialise;
import transfer.Transfer;
import zeppelin.Main;
import connection.*;
public class InitialisePi {
public static void main(String[] args) {
	ReceiverPi receiverPi = new ReceiverPi();
	Thread ReceiverPiThread = new Thread(receiverPi);
	ReceiverPiThread.start();
	
	SenderPi sender = new SenderPi();
	Transfer transfer = new Transfer();
	transfer.setMessage("derpstyle");
	sender.sendTransfer(transfer);
	//Main main = Main.getInstance();
	//main.init(sender);
	// Add more swag here :p 
}
}
