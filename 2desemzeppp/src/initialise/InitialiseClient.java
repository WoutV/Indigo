package initialise;

import map.*;
import navigation.*;

import org.opencv.core.Core;

import connection.ReceiverClient;
import connection.SenderClient;

import simulator.SimConnNoRabbitClient;
import simulator.SimEnemy;
import simulator.SimMain;
import simulator.Simulator;

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
        
        PositionController xpos = PositionController.getXController();
        PositionController ypos = PositionController.getYController();
        xpos.setKd(0);
        xpos.setKp(5);
        xpos.setKi(0);
        ypos.setKd(0);
        ypos.setKp(5);
        ypos.setKi(0);
        
        //mode: 1: normal zeppelin, 2: sim own and enemy, 3: sim enemy only
        //boolean rabbit
        int mode = 2;
        boolean rabbit = false;
        
        SenderClient sender = null;
        SimConnNoRabbitClient simConnClient = null;
        
        //set up conn
        if(rabbit) {
        	sender = new SenderClient();
        	ReceiverClient receiver = new ReceiverClient(gui);
            Thread receiverclientthread = new Thread(receiver);
            receiverclientthread.start();
            gui.getGuic().setSender(sender);
            xpos.setSender(sender);
    		ypos.setSender(sender);
    		Dispatch.setSender(sender);
        }
        if(!rabbit) {
        	 simConnClient = new SimConnNoRabbitClient();
        	 Thread t = new Thread(simConnClient);
             t.start();
             xpos.setSenderNoRabbit(simConnClient);
             ypos.setSenderNoRabbit(simConnClient);
        }
        
        SimEnemy simEnemy = null;
        //sim enemy
        if(mode == 2 || mode == 3) {
        	//!!!!
        }
        
        //sim own
        if(mode == 2) {
        	SimMain.makeOwnZeppSim(map, rabbit, false);
        }
        
        //TODO kp,kd,ki values
        
        
		LocationLocator locator = new LocationLocator(map,xpos,ypos,gui.getGuic());
		//camera => ImageProcessor => pureColourLocator (locateAndMove) => positioncontrollers
		Dispatch.setLoc(locator);
		Dispatch.setGUIMain(gui);
		Dispatch.setSimEnemy(simEnemy);
		Dispatch.receiveTarget(50,50);
		
		try {
	        Thread.sleep(5000);
	    }
	    catch(Exception exc) {
	    }

		if(rabbit) {
			sender.sendTransfer("0", "indigo.lcommand.motor1");
			sender.sendTransfer("0", "indigo.lcommand.motor1");
		}
		if(!rabbit) {
			simConnClient.sendTransfer("0", "indigo.lcommand.motor2");
			simConnClient.sendTransfer("0", "indigo.lcommand.motor1");
		}
	}
}
