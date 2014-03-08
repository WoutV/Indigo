package initialise;
import org.opencv.core.Core;

import connection.ReceiverClient;
import gui.GuiMain;;
public class InitialiseClient {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		final GuiMain gui = new GuiMain();
		gui.enableAllButtons();
		gui.setVisible(true);
		
		ReceiverClient receiver = new ReceiverClient(gui);
		Thread receiverclientthread = new Thread(receiver);
		receiverclientthread.start();
		// Hier de andere threads starten die op de client moeten runnen(image recognition)
		// Ook mss iets op de gui tonen terwijl er connectie met de server wordt gemaakt???
		
	}
}
