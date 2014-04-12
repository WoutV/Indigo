package gui;

import zeppelin.Propellor;
import javax.swing.ImageIcon;

import connection.SenderClient;

/**
 * Klasse om de client / de GUI te linken met de zeppelin.
 * Communicatie van GUI naar zeppelin: send..
 * Communicatie van zeppelin naar GUI: receive..
 */
public class GuiCommands {

	private GuiMain gui;
	private SenderClient sender;

	public GuiCommands(GuiMain gui){
		this.gui=gui;
	}

	/**
	 * Via deze methode stuurt een zeppelin zijn huidige hoogte door.
	 * Deze wordt dan in de GUI weergegeven
	 * @param hoogte
	 * 			De huidige hoogte in cm
	 * @param eigen
	 * 			Eigen zeppelin (true) of enemy (false)
	 */
	public void receiveHeight(double hoogte, boolean eigen){
		if(eigen)
			gui.setEigenHoogte(hoogte);
		else 
			gui.setEnemyHoogte(hoogte);
	}

	/**
	 * Via deze methode stuurt de zeppelin de staat van één van zijn propellors door.
	 * @param prop
	 * 			De propellor waarvan de staat wordt doorgestuurd
	 * @param running
	 * 			true = propellor aan
	 * 			false = propellor uit
	 */
	public void receivePropellorState(Propellor prop,boolean running) {
		if(running)
			gui.propellorActive(prop);
		else
			gui.propellorNotActive(prop);
	}
	
	/**
	 * Via deze methode stuurt een zeppelin zijn huidige locatie door.
	 * Deze wordt dan in de GUI weergegeven.
	 * De locatie wordt gegeven door x- en y-coordinaten volgens het x- en y-frame
	 * van de kaart.
	 * 
	 * @param x
	 * 			x in mm. X loopt van links naar rechts
	 * @param y
	 * 			y in mm. Y loopt van boven naar beneden
	 * @param eigen
	 * 			Eigen zeppelin (true) of enemy (false)
	 */
	public void receiveLocation(double x, double y, boolean eigen) {
		if(eigen)
			gui.setOwnLocation(x/10,y/10);
		else
			gui.setEnemyLocation(x/10,y/10);
	}

	/**
	 * Geeft de ontvangen afbeelding weer in tab 1 van de GUI.
	 * @param image
	 */
	public void receiveImage(ImageIcon image) {
		gui.setImageDisplay(image);
	}
	
	/**
	 * Via deze methode wordt de eigen locatie en hoek ontvangen.
	 * De locatie wordt gegeven door x- en y-coordinaten volgens het x- en y-frame
	 * van de kaart.
	 * 
	 * @param x
     * 			x in cm. X loopt van links naar rechts.
     * @param y
     * 			y in cm. Y loopt van boven naar beneden.
     * @param alpha
     * 			Hoek. 0 -> wijst naar boven, > 0 -> wijzerzin, < 0 -> tegenwijzerzin
	 */
	public void receiveOwnLocation(double x, double y, double alpha) {
		gui.setOwnLocation(x, y, alpha);
	}

//	/**
//	 * Methode om een boodschap weer te geven in het commandodisplay op tab 1 in de GUI
//	 * @param message
//	 */
//	public void receiveMessage(String message) {
//		showInfo("Message Received From Server: "+ message);
//		gui.showMessage(message);
//	}

	/**
	 * Methode om een boodschap toe te voegen aan de full command list (tab 2) van de GUI.
	 * @param message
	 */
	public void showInfo(String message){
		gui.addToGUIEventList(GUIEvent.EventType.Misc,message);
	}
	
	/**
	 * Methode om een target location (in cm) te sturen.
	 * 
	 * @param x
	 * @param y
	 */
	public void sendTarget(int x,int y) {
		
	}
	
	public void setSender(SenderClient sender) {
		this.sender = sender;
	}

}
