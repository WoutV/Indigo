package gui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GuiCommands {

	private double hoogte;
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
	
	//De raspberry stuurt via hier de hoogte door, deze wordt dan op de gui weergegeven.
	public void getHeightZep(double hoogte){
		gui.setHoogteLabel(hoogte);
	}
	
	//methode die de hoogte doorstuurt naar de raspberry, die moet dan naar de doorgestuurde hoogte gaan.
	public void sendHeightZep(double hoogte){
		
	}
	
	
	public void sendKeyPressed(KeyEvent evt){
		
	}
	
	public void sendKeyUnpressed(KeyEvent evt){
		
	}
}
