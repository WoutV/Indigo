package initialise;

import imageProcessing.ImageProcessor;
import map.Map;
import map.PureColourLocator;

import org.opencv.core.Core;

import zeppelin.PositionController;
import connection.ReceiverClient;
import connection.SenderClient;
import gui.GuiMain;

public class InitialiseClient {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		final String fileName = "/grid.csv";
		
		Map map = new Map(fileName);
		 
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GuiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        final GuiMain gui = new GuiMain();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
        		gui.setVisible(true);
        		gui.enableAllButtons();
        		gui.setMap(fileName);
        		gui.setOwnLocation(200, 200);	        		
            }
        });
        SenderClient sender = new SenderClient();
        gui.getGuic().setSender(sender);
        
        //here because controllers running on client
        //TODO kp,kd,ki values
        PositionController xpos = new PositionController(0, 0, 0, sender,true);
		PositionController ypos = new PositionController(10, 10, 10, sender, false);
		PureColourLocator locator = new PureColourLocator(map,xpos,ypos,gui.getGuic());
		//camera => ImageProcessor => pureColourLocator (locateAndMove) => positioncontrollers
		ImageProcessor.setLocator(locator);
		ReceiverClient receiver = new ReceiverClient(gui);
		Thread receiverclientthread = new Thread(receiver);
		receiverclientthread.start();
		// Hier de andere threads starten die op de client moeten runnen(image recognition)
		// Ook mss iets op de gui tonen terwijl er connectie met de server wordt gemaakt???
	}
}
