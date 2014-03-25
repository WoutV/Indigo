package navigation;

import java.util.List;

import map.Symbol;

public class Dispatch {
	private static LocationLocator loc;
	
	public static void setLoc(LocationLocator locc){
		loc= locc;
	}
	
	public static void processSymbols(List<Symbol> symbols){
		loc.locateAndMove(symbols);
	}
}
