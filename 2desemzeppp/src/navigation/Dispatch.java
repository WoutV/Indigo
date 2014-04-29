package navigation;

import gui.GuiCommands;
import gui.GuiMain;

import java.util.List;

import simulator.SimEnemy;

import connection.SenderClient;

import map.Map;
import map.Symbol;

public class Dispatch {
	
	private static LocationLocator loc;
	private static GuiMain guimain;
	private static SenderClient sender;
	private static SimEnemy simEnemy;
	private static GuiCommands guic;
	private static Map map;
	private static PositionController xPos,yPos;
	
	private static boolean landTarget;
	private static double targetX,targetY;
	private static int targetTablet;
	
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
	
	public static void setMap(Map Map) {
		map = Map;
	}
	
	public static void setPositionControllers(PositionController xController, PositionController yController) {
		xPos = xController;
		yPos = yController;
	}
	
	/**
	 * Whenever a bunch of symbols is found, this can be used to find the position and move.
	 * >= 3 symbols are needed
	 * If the list consists of 3 symbols forming a triangle, it is better to use processTriangeOfSymbols(...).
	 * 
	 * @param symbols
	 */
	public static void processSymbols(List<Symbol> symbols){
		loc.locateAndMove(symbols);
	}
	
	/**
	 * Whenever a triangle of symbols is found, this can be used to send find the position and move.
	 * The center symbol should be the one closest to the center of the image.
	 * The order of the other symbols is irrelevant.
	 * All symbols should have their x- and y- cooordinates set to x- and y- pixel value.
	 * 
	 * @param 	center
	 * 			Symbol cloest to center of the image.
	 * @param 	s1
	 * @param 	s2
	 */
	public static void processTriangleOfSymbols(Symbol center, Symbol s1, Symbol s2) {
		loc.locAndMoveWithTriangle(center,s1,s2);
	}
	
	/**
	 * Receives a target location and sends it to PositionControllers, gui and possibly
	 * SimEnemy.
	 * 
	 * @param x
	 * @param y
	 */
	public static void receiveTarget(int x, int y) {
		targetX = x;
		targetY = y;
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
	 * Whenever the current loc is found:
	 * - searches if near tablet
	 * - searches if ready to land
	 * - updates gui
	 * - notifies positioncontrollers
	 * 
	 * @param 	lo
	 * 			x-coord (cm), y-coord (cm), alpha
	 */
	public static void nowAtLoc(double[] lo) {
		//check if near tablet
		if(targetTablet > 0) {
			double[] tabletCoord = map.getTablet(targetTablet);
			if(loc.nearLoc(lo[0],lo[1],tabletCoord[0],tabletCoord[1],20)) {
				foundTablet(targetTablet);
			}
		}
		
//		for(int tablet=0;tablet<map.getNoOfTablets();tablet++) {
//			double[] tabletCoord = map.getTablet(tablet+1);
//			if(loc.nearLoc(lo[0],lo[1],tabletCoord[0],tabletCoord[1],20)) {
//				foundTablet(tablet+1);
//			}
//		}
		
		
		//check if needs to land
		if(landTarget && loc.nearLoc(lo[0],lo[1],targetX,targetY,20)) {
			land();
		}
		

		guic.receiveOwnLocation(lo[0], lo[1], lo[2]);
		xPos.run(lo);
		//doing y now
		//System.out.println("going to y");
		yPos.run(lo);
	}
	
	/**
	 * When a tablet has been found, sends a message to request the command.
	 * @param no
	 */
	private static void foundTablet(int no) {
		if(sender!=null) {
			sender.sendTransfer(RSA, "indigo.tablet.tablet"+ no);
		}
	}
	
	/**
	 * When the zeppelin is near the target, send a message commanding it to land.
	 */
	private static void land() {
		if(sender!=null) {
			sender.sendTransfer("0", "indigo.elevate");
		}
	}
}
