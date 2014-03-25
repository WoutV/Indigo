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
		sender.sendTransfer("derp54321","test.test");
	}
}
