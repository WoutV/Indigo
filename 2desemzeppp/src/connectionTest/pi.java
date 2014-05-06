package connectionTest;

import connection.ReceiverPi;
import connection.SenderPi;

public class pi {
	public static void main(String[] args){
		 ReceiverPi receiverPi = new ReceiverPi("locahost",5672);
		 Thread ReceiverPiThread = new Thread(receiverPi);
		 ReceiverPiThread.start();
		System.out.println("Sender Starting");
		SenderPi sender = new SenderPi("localhost",5672);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sender.sendTransfer("rabbitmqtesterdetest", "test.test");
	}
}
