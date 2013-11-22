package client;
import java.net.Socket;

import javax.swing.JOptionPane;

import gui.GUIMain;

public class InitializeClient {
	 /**
     * @param args the command line arguments
     */
	
	
	public static void main(String[] args)
	{	
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        final GUIMain gui = new GUIMain();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true);
            }
        });
    
        //INITIALIZING SOCKETS AND THREADS

		setIp(gui);
		 
	}
	static String serverIP;
	static boolean ipSet=false;
	/**
	 * Sets the ip address of the server once it is set in the gui.
	 * @param gui
	 */
	public static void setIp(GUIMain gui){
		while(!ipSet){
			serverIP = gui.setIP();
			if(serverIP==null){
				System.exit(0);
			}
			if(isValidIP(serverIP)){
				ipSet=true;
				gui.showMessage("Connecting...");
			}
			else
				gui.displayMessage("Invalid IP address", JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			Socket sock = new Socket(serverIP,6789);
			Socket imageSock = new Socket(serverIP,6790);
			Socket sendSocket = new Socket(serverIP,6791);
			SendToServer sender = new SendToServer(sendSocket,gui);
			gui.getGuiCommands().setSender(sender);
			gui.enableAllButtons();
			gui.manual();
			ReceiveThread recieveThread = new ReceiveThread(sock,gui);
			Thread thread2 =new Thread(recieveThread);thread2.start();
			gui.showMessage("");
			ReceiveThread imageReceiver = new ReceiveThread(imageSock,gui);
			Thread imageThread = new Thread(imageReceiver);
			imageThread.start();
			
			
		} catch (Exception e) {
			gui.displayMessage(e.getMessage()+"\n" + "Please make sure the server is running!", JOptionPane.ERROR_MESSAGE);
			gui.showMessage("");
			ipSet=false;
			setIp(gui);
			}
	}

	//Check for valid IP Address
		public static boolean isValidIP (String ip) {
		    try {
		        if (ip == null || ip.isEmpty()) {
		            return false;
		        }

		        String[] parts = ip.split( "\\." );
		        if ( parts.length != 4 ) {
		            return false;
		        }

		        for ( String s : parts ) {
		            int i = Integer.parseInt( s );
		            if ( (i < 0) || (i > 255) ) {
		                return false;
		            }
		        }

		        return true;
		    } catch (NumberFormatException nfe) {
		        return false;
		    }
		}


}
