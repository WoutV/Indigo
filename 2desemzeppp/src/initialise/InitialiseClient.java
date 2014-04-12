package initialise;

import imageProcessing.ImageProcessor;

import map.*;
import navigation.*;

import org.opencv.core.Core;

import simulator.SimConnNoRabbitClient;

import connection.ReceiverClient;
import connection.SenderClient;
import gui.GuiMain;

public class InitialiseClient {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String fileName = "/shapesDemo.csv";
		
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
        GuiMain gui = new GuiMain();
        gui.setVisible(true);
        gui.enableAllButtons();
        gui.setMap(fileName);
        gui.setOwnLocation(200, 200);
        
        //TODO kp,kd,ki values
        PositionController xpos = PositionController.getXController();
        PositionController ypos = PositionController.getYController();
        xpos.setKd(0);
        xpos.setKp(5);
        xpos.setKi(0);
        ypos.setKd(0);
        ypos.setKp(5);
        ypos.setKi(0);
        
        //rabbit
        //SenderClient sender = new SenderClient();
        //gui.getGuic().setSender(sender);
//		xpos.setSender(sender);
//		ypos.setSender(sender);
        //Dispatch.setSender(sender);
        
      //ReceiverClient receiver = new ReceiverClient(gui);
      //Thread receiverclientthread = new Thread(receiver);
      //receiverclientthread.start();
        
        //norabbit
        SimConnNoRabbitClient simConnClient = new SimConnNoRabbitClient();
        Thread t = new Thread(simConnClient);
        t.start();
        xpos.setSenderNoRabbit(simConnClient);
        ypos.setSenderNoRabbit(simConnClient);
        
		LocationLocator locator = new LocationLocator(map,xpos,ypos,gui.getGuic());
		//camera => ImageProcessor => pureColourLocator (locateAndMove) => positioncontrollers
		Dispatch.setLoc(locator);
		Dispatch.setGUIMain(gui);
		
		Dispatch.receiveTarget(50,50);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		// Hier de andere threads starten die op de client moeten runnen(image recognition)
		// Ook mss iets op de gui tonen terwijl er connectie met de server wordt gemaakt???
		//sender.sendTransfer("0", "indigo.lcommand.motor1");
		simConnClient.sendTransfer("0", "indigo.lcommand.motor2");
		simConnClient.sendTransfer("0", "indigo.lcommand.motor1");
	}
}
