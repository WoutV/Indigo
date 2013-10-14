package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import gui.GUIMain;

public class InitializeClient {
	 /**
     * @param args the command line arguments
     */
	
	
	public static void main(String[] args)
	{
		
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>
        final GUIMain gui = new GUIMain();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true);
            }
        });
    
        //INITIALIZING SOCKETS AND THREADS

		setIp();
		try {
			Socket sock = new Socket(serverIP,6789);
			SendThread sendThread = new SendThread(sock,gui);
			Thread thread = new Thread(sendThread);thread.start();
			RecieveThread recieveThread = new RecieveThread(sock,gui);
			Thread thread2 =new Thread(recieveThread);thread2.start();
		} catch (Exception e) {System.out.println(e.getMessage());} 
	}
	static String serverIP;
	static boolean ipSet=false;
	public static void setIp(){
		while(!ipSet){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter Server's IP: ");
			try {
				serverIP = br.readLine();
			} catch (IOException e) {
				System.out.println("Something went wrong! Try again");
			}
			if(isValidIP(serverIP))
				ipSet=true;
			else
				System.out.println("Invalid IP address");
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
