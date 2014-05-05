package initialise;

import imageProcessing.MainImageProcessorThread;
import map.*;
import navigation.*;

import org.opencv.core.Core;

import connection.ReceiverClient;
import connection.SenderClient;

import simulator.SimConnNoRabbitClient;
import simulator.SimEnemy;
import simulator.SimEnemyConnNoRabbitClient;
import simulator.SimMain;

import gui.GuiMain;

public class InitialiseClient {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		String fileName = "/field.csv";
		
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
        int mode = 3;
        boolean rabbit = true;
        
        SenderClient sender = null;
        SimConnNoRabbitClient simConnClient = null;
        
        SimEnemy simEnemy = null;
        //sim enemy
        if(mode == 2 || mode == 3) {
        	simEnemy = SimMain.makeSimEnemy(rabbit, 0, 0, -1, -1,map);
        }
        
        //sim own
        if(mode == 2) {
        	SimMain.makeOwnZeppSim(map, rabbit, false);
        }
        
        //set up conn
        if(rabbit) {
        	System.out.println("Initializing sender");
        	sender = new SenderClient();
        	System.out.println("Sender initialized, Initializing receiver");
        	ReceiverClient receiver = new ReceiverClient(gui);
            Thread receiverclientthread = new Thread(receiver);
            receiverclientthread.start();
            System.out.println("Receiver Initialised");
            gui.getGuic().setSender(sender);
            xpos.setSender(sender);
    		ypos.setSender(sender);
    		Dispatch.setSender(sender);
        }
        if(mode == 2 && !rabbit) {
        	 simConnClient = new SimConnNoRabbitClient();
        	 Thread t = new Thread(simConnClient);
             t.start();
             xpos.setSenderNoRabbit(simConnClient);
             ypos.setSenderNoRabbit(simConnClient);
        }
        if((mode == 2 || mode == 3) && !rabbit) {
       	 	SimEnemyConnNoRabbitClient simEnemyConnClient = new SimEnemyConnNoRabbitClient();
       	 	Thread t = new Thread(simEnemyConnClient);
            t.start();
        }
        
        //TODO kp,kd,ki values
              
		LocationLocator locator = new LocationLocator(map);
		//camera => ImageProcessor => pureColourLocator (locateAndMove) => positioncontrollers
		Dispatch.setLoc(locator);
		Dispatch.setGUIMain(gui);
		Dispatch.setSimEnemy(simEnemy);
		Dispatch.setMap(map);
		Dispatch.setPositionControllers(xpos,ypos);
		Dispatch.receiveTarget(50,50);
		
//		try {
//	        Thread.sleep(5000);
//	    }
//	    catch(Exception exc) {
//	    }

		if(rabbit) {
			sender.sendTransfer("0", "indigo.lcommand.motor1");
			sender.sendTransfer("0", "indigo.lcommand.motor1");
		}
		if(!rabbit && mode == 2) {
			simConnClient.sendTransfer("0", "indigo.lcommand.motor2");
			simConnClient.sendTransfer("0", "indigo.lcommand.motor1");
		}
		
		if(mode!=2) {
//			MainImageProcessorThread imageProcessor = new MainImageProcessorThread(3, 50, "color.txt");
//			Thread imageProcessorThread = new Thread(imageProcessor);
//			imageProcessorThread.start();
		}
	}
}
