package initialise;

import gui.GuiMain;
import connection.ReceiverClient;
import connection.ReceiverPi;
import connection.SenderClient;

public class test {
	public static void main(String[] args) {
		ReceiverPi pi = new ReceiverPi();
		System.out.println("deded");
		Thread dr = new Thread(pi);
		dr.start();
		System.out.println("lelaunched");
		SenderClient sender = new SenderClient();
		System.out.println("victory");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int n = 0;
		while(n<10){
		sender.sendTransfer("derp54321","test.test");
		n++;
		}
		
	}
}
