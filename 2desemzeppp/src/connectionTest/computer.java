package connectionTest;

import gui.GuiMain;

import org.opencv.core.Core;

import map.Map;
import navigation.Dispatch;
import connection.ReceiverClient;
import connection.SenderClient;

public class computer {
	public static void main(String[] args){
		
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
		
		
		
		SenderClient sender = new SenderClient();
    	ReceiverClient receiver = new ReceiverClient(gui);
        Thread receiverclientthread = new Thread(receiver);
        receiverclientthread.start();
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sender.sendTransfer("Pi go up", "test.test");
	}

}
