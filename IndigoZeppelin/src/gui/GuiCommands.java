package gui;

import java.util.ArrayList;

public class GuiCommands {

	private double hoogte;
	private  ArrayList<String> newcommands;
	private GUIMain gui;
	
	public GuiCommands(GUIMain gui){
		this.gui=gui;
	}

	public ArrayList<String> getNewcommands() {
		return newcommands;
	}

	public void setNewcommands(ArrayList<String> newcommands) {
		this.newcommands = newcommands;
	}

	public void setHoogte(double hoogte) {
		this.hoogte = hoogte;
		gui.setHoogteLabel(hoogte);
	}
	
	
}
