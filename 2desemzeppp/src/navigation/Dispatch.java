package navigation;

import gui.GuiCommands;
import gui.GuiMain;

import java.util.List;

import simulator.SimEnemy;

import connection.SenderClient;

import map.Symbol;

public class Dispatch {
	
	private static LocationLocator loc;
	private static GuiMain guimain;
	private static SenderClient sender;
	private static SimEnemy simEnemy;
	private static GuiCommands guic;
	
	public static void setLoc(LocationLocator locc){
		loc= locc;
	}
	
	public static void setGUIMain(GuiMain g) {
		guimain = g;
		guic = guimain.getGuic();
	}
	
	public static void setSender(SenderClient senderclient) {
		sender = senderclient;
	}
	
	public static void setSimEnemy(SimEnemy enemySim) {
		simEnemy = enemySim;
	}
	
	public static void processSymbols(List<Symbol> symbols){
		loc.locateAndMove(symbols);
	}
	
	/**
	 * Receives a target location and sends it to PositionControllers, gui and possibly
	 * SimEnemy.
	 * 
	 * @param x
	 * @param y
	 */
	public static void receiveTarget(int x, int y) {
		double[] dest = {x,y};
		PositionController.setDestination(dest);
		guimain.setTargetLocation(x, y);
		if(simEnemy != null)
			simEnemy.receiveTarget(x,y);
	}
	
	/**
	 * Receives the enemy loc.
	 * 
	 * @param 	x
	 * 			x coord in mm
	 * @param 	y
	 * 			y coord in mm
	 */
	public static void receiveEnemyLoc(int x, int y) {
		guimain.setEnemyLocation((int) (x/10),(int) (y/10));
	}
	
	/**
	 * When a tablet has been found, sends a message to request the command.
	 * @param no
	 */
	public static void foundTablet(int no) {
		if(sender!=null) {
			//send message
		}
	}
}
