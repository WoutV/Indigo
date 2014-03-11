package initialise;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import imageProcessing.ImageProcessor;
import map.ColorSymbol;
import map.Map;
import map.PureColourLocator;
import map.Symbol;

import org.opencv.core.Core;

import zeppelin.PositionController;
import connection.ReceiverClient;
import connection.SenderClient;
import gui.GuiMain;;
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
        //</editor-fold>
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
        PositionController xpos = new PositionController(0, 0, 0, sender,true);
		PositionController ypos = new PositionController(10, 10, 10, sender, false);
		PureColourLocator locator = new PureColourLocator(map,xpos,ypos,gui.getGuic());
		
		ImageProcessor.setLocator(locator);
		ReceiverClient receiver = new ReceiverClient(gui);
		Thread receiverclientthread = new Thread(receiver);
		receiverclientthread.start();
		ImageProcessor.processImage(new ImageIcon("C:/Users/Vince/Desktop/a/2/b (105).jpg"));
		// Hier de andere threads starten die op de client moeten runnen(image recognition)
		// Ook mss iets op de gui tonen terwijl er connectie met de server wordt gemaakt???
//		List<ColorSymbol> list1 = new LinkedList<>();
//		double[] coord0 = {50,50};
//		ColorSymbol center1 = new ColorSymbol(coord0,Symbol.Colour.RED);
//		list1.add(center1);
//		double[] coord1 = {40,60};
//		list1.add(new ColorSymbol(coord1,Symbol.Colour.RED));
//		double[] coord2 = {60,60};
//		list1.add(new ColorSymbol(coord2,Symbol.Colour.WHITE));
//		double[] coord3 = {70,50};
//		list1.add(new ColorSymbol(coord3,Symbol.Colour.WHITE));
//		double[] coord4 = {60,40};
//		list1.add(new ColorSymbol(coord4,Symbol.Colour.RED));
//		double[] coord5 = {40,40};
//		list1.add(new ColorSymbol(coord5,Symbol.Colour.GREEN));
//		double[] coord6 = {30,50};
//		list1.add(new ColorSymbol(coord6,Symbol.Colour.RED));
//		double[] coord7 = {20,20};
//		list1.add(new ColorSymbol(coord7,Symbol.Colour.BLUE));
//		double[] coord8 = {80,80};
//		list1.add(new ColorSymbol(coord8,Symbol.Colour.RED));
//		locator = new PureColourLocator(new Map("/shapesDemo.csv"),null,null,gui.getGuic());
//		locator.locateAndMove(list1);
//		if(loc == null)
//			JOptionPane.showMessageDialog(null,"null");
//
//		if(loc != null)
//			JOptionPane.showMessageDialog(null, loc[0] + "," + loc[1] + "|" + loc[2]);
	}
}
