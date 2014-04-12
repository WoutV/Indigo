package navigation;

import gui.GuiCommands;
import gui.GuiMain;

import java.util.List;

import connection.SenderClient;

import map.Symbol;

public class Dispatch {
	
	private static LocationLocator loc;
	private static GuiMain guimain;
	private static SenderClient sender;
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
	
	public static void processSymbols(List<Symbol> symbols){
		loc.locateAndMove(symbols);
	}
	
	public static void receiveTarget(int x, int y) {
		double[] dest = {x,y};
		PositionController.setDestination(dest);
		guimain.setTargetLocation(x, y);
	}
	
	public static void foundTablet(int no) {
		if(sender!=null) {
			//send message
		}
	}
}
