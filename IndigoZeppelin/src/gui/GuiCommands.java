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

	//TEMP: dit wordt een soort arraylist/andere collectie van een Klasse Command
	@SuppressWarnings("unused")
	private  ArrayList<String> newcommands;

	private GUIMain gui;

	public GuiCommands(GUIMain gui){
		this.gui=gui;
	}

	//	public ArrayList<String> getNewcommands() {
	//		return newcommands;
	//	}
	//
	//	public void setNewcommands(ArrayList<String> newcommands) {
	//		this.newcommands = newcommands;
	//	}
	//
	//	public void setHoogte(double hoogte) {
	//		this.hoogte = hoogte;
	//		gui.setHoogteLabel(hoogte);
	//	}

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

	//TEMP!?
	//geeft de ontvangen afbeelding weer in GUI
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
			gui.addToCommandList(" - Desired height send to Zeppelin, Zeppelin must reach: " + hoogte +"cm");
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
			gui.addToCommandList(" - New pwm send to Zeppelin ");
			Transfer transfer = new Transfer();
			transfer.setPwm(pwm);
			sender.sendTransfer(transfer);
		}
	}
	
	public void searchPwmZep() {
		if(sender !=null) {
			gui.addToCommandList(" - Looking for zweefpwm ");
			Transfer transfer = new Transfer();
			transfer.searchPwm();
			sender.sendTransfer(transfer);
		}
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

	public void receiveMessage(String message) {
		showInfo("Message Received From Server: "+ message);
		gui.showMessage(message);

	}
	
	public void showInfo(String message){
		gui.addToCommandList(message);
	}


}
