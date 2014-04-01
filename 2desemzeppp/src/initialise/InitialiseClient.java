package initialise;

import imageProcessing.ImageProcessor;

import map.*;
import navigation.*;

import org.opencv.core.Core;

import connection.ReceiverClient;
import connection.SenderClient;
import gui.GuiMain;

public class InitialiseClient {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		final String fileName = "/shapesDemo.csv";
		
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
        PositionController xpos = PositionController.getXController();
        PositionController ypos = PositionController.getYController();
        xpos.setKd(0);
        xpos.setKp(5);
        xpos.setKi(0);
        ypos.setKd(0);
        ypos.setKp(5);
        ypos.setKi(0);
        
		xpos.setSender(sender);
		ypos.setSender(sender);
		LocationLocator locator = new LocationLocator(map,xpos,ypos,gui.getGuic());
		//camera => ImageProcessor => pureColourLocator (locateAndMove) => positioncontrollers
		Dispatch.setLoc(locator);
		ReceiverClient receiver = new ReceiverClient(gui);
		Thread receiverclientthread = new Thread(receiver);
		receiverclientthread.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		// Hier de andere threads starten die op de client moeten runnen(image recognition)
		// Ook mss iets op de gui tonen terwijl er connectie met de server wordt gemaakt???
		sender.sendTransfer("0", "indigo.lcommand.motor1");
	}
}
