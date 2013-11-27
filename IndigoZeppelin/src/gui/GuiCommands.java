package gui;

import java.util.ArrayList;
import zeppelin.Propellor;
import javax.swing.ImageIcon;
import transfer.Transfer.Key;
import transfer.Transfer;
import transfer.Transfer.TransferType;

import client.SendToServer;

/**
 * Klasse om de client / de GUI te linken met de zeppelin.
 * Communicatie van GUI naar zeppelin: send..
 * Communicatie van zeppelin naar GUI: receive..
 */
public class GuiCommands {

	private GUIMain gui;

	public GuiCommands(GUIMain gui){
		this.gui=gui;
	}

	/**
	 * Via deze methode stuurt de zeppelin zijn huidige hoogte door.
	 * Deze wordt dan in de GUI weergegeven
	 * @param hoogte
	 * 			De huidige hoogte in cm
	 */
	public void receiveHeight(double hoogte){
		gui.setHoogteLabel(hoogte);
		gui.addToCommandList(" - New height received from Zeppelin : "+hoogte);
	}


	/**
	 * Via deze methode stuurt de zeppelin de staat van één van zijn propellors door.
	 * @param prop
	 * 			De propellor waarvan de staat wordt doorgestuurd
	 * 			!! voorlopig met enum, dit evt veranderen adhv interne werking zeppelin
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
	 * Geeft de ontvangen afbeelding weer in een label in tab 1 van window 1 van GUI.
	 * @param image
	 */
	public void receiveImage(ImageIcon image) {
		gui.setImageDisplay(image);
	}

	/**
	 * Methode die een hoogte (in cm) doorstuurt naar de zeppelin
	 * De zeppelin moet dan naar deze hoogte gaan
	 * Wordt opgeroepen door GUIMain.
	 * @param hoogte
	 * 			hoogte in cm
	 */
	public void sendHeightZep(double hoogte){
		if(sender !=null) {
			gui.addToCommandList(" - Desired height sent to Zeppelin, Zeppelin must reach: " + hoogte +" cm");
			Transfer transfer = new Transfer();
			transfer.setHeight(hoogte);
			sender.sendTransfer(transfer);
		}
	}

	/**
	 * Methode om een pwm door te geven aan de zeppelin.
	 * Deze wordt dan gebruikt als zweef-pwm.
	 * Op deze manier zal de ZoekZweefPwm worden gestopt.
	 * @param pwm
	 */
	public void sendPwmZep(int pwm){
		if(sender !=null) {
			gui.addToCommandList(" - New float pwm sent to Zeppelin: " + pwm );
			Transfer transfer = new Transfer();
			transfer.setPwm(pwm);
			sender.sendTransfer(transfer);
		}
	}

	/**
	 * Methode om door te geven dat de zeppelin automatisch naar zweef-pwm moet zoeken.
	 */
	public void searchPwmZep() {
//		if(sender !=null) {
//			gui.addToCommandList(" - Looking for floatpwm ");
//			Transfer transfer = new Transfer();
//			transfer.searchPwm();
//			sender.sendTransfer(transfer);
//		}
	}


	//horizontale unpressed (left, right, reverse, forward) ==>
	// enkel doorsturen indien dit het event is dat als laatste is ingedrukt
	//dit vermijdt dat de gebruiker een knop moet loslaten, voor hij de andere mag indrukken
	private Key lastHorizontalKey;

	/**
	 * Methode die een Key doorstuurt van de button die wordt ingedrukt.
	 * Wordt opgeroepen door GUIMain.
	 */
	public void sendKeyPressed(Key key){
		if(key != Key.ELEVATE)
			lastHorizontalKey = key;

		if(sender != null) {
			Transfer transfer = new Transfer();
			transfer.setKeyEvent(key, TransferType.KEYPRESSEDEVENT);
			sender.sendTransfer(transfer);
		}
	}

	/**
	 * Methode die een Key doorstuurt van een button die wordt losgelaten (was ingedrukt)
	 * Wordt opgeroepen door GUIMain.
	 */
	public void sendKeyReleased(Key key){
		if(key != Key.ELEVATE && lastHorizontalKey != key)
			return;
		//niet laatste horizontal movement key ingedrukt

		if(sender != null) { 
			Transfer transfer = new Transfer();
			transfer.setKeyEvent(key, TransferType.KEYRELEASEDEVENT);
			sender.sendTransfer(transfer);
		}
	}

	private SendToServer sender;

	public void setSender(SendToServer sender){
		this.sender=sender;
	}

	/**
	 * Methode om een boodschap weer te geven in het commandodisplay op tab 1 in de GUI
	 * @param message
	 */
	public void receiveMessage(String message) {
		showInfo("Message Received From Server: "+ message);
		gui.showMessage(message);

	}

	/**
	 * Methode om een boodschap toe te voegen aan de full command list (tab 2) van de GUI.
	 * @param message
	 */
	public void showInfo(String message){
		gui.addToCommandList(message);
	}

	
	/**
	 * Doorgeven aan zeppelin dat naar manual control moet worden geschakeld.
	 */
	public void setManualContr() {
		if(sender!=null){
			gui.addToCommandList(" - Switched to manual control");
			Transfer transfer = new Transfer();
			transfer.setMode(false);
			sender.sendTransfer(transfer);
		}
	}

	/**
	 * Doorgeven aan zeppelin dat naar automatic control moet worden geschakeld.
	 */
	public void setAutomaticContr() {
		if(sender!=null) {
			gui.addToCommandList(" - Switched to automatic control");
			Transfer transfer = new Transfer();
			transfer.setMode(true);
			sender.sendTransfer(transfer);
		}
	}

	/**
	 * Geeft een boodschap weer in het commandodisplay van tab 1, zonder vermelding in
	 * de full command list van tab 2.
	 * (wel in tab 2: gebruik receiveMessage(String))
	 * @param info
	 */
	public void showOnCommandLabel(String info){
		gui.showMessage(info);
	}
	
	/**
	 * Geeft een text weer op de gui, door er een QR-code van te maken
	 * en zowel de text als de QR-code te tonen in tab 1.
	 * @param text
	 */
	public void receiveQRCode(String text) {
		gui.displayQRCode(text);
	}


}
