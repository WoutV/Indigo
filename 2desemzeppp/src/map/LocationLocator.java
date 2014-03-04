package map;

import java.awt.Dimension;
import java.util.ArrayList;

public class LocationLocator {
	private Map map;
	public LocationLocator(Map map){
		this.map=map;
	}
	
/**
 * 
 * @param symbols
 * @param pixelRef
 * @return
 */
	public double[] locatePosition(Symbol[] symbols, double[] pixelRef){
		Symbol ref = symbols[0];
		symbols = filterSymbols(symbols);
		int[] position = new int[2];
		ArrayList<int[]> positionsInMap = new ArrayList<int[]>();
		for(int i=0; i<map.getSymbolsOnRow();i++){
			for(int index= 0; index < map.getRows();index++){
				Symbol tocheck = map.getSymbol(i,index);
				if(tocheck.equals(ref)){
					position[0]= index;
					position[1]= i;
					positionsInMap.add(position);
				}
			}
		}
		ArrayList<ArrayList<Symbol>> neighbouringSymbols = new ArrayList<ArrayList<Symbol>>();
		for(int i= 0; i<positionsInMap.size();i++){
			neighbouringSymbols.add(getNeighbouringSymbols(positionsInMap.get(i)));
		}
		ArrayList<ArrayList<Symbol>> removeArrayList = new ArrayList<ArrayList<Symbol>>();
		for(ArrayList<Symbol> s: neighbouringSymbols){
			if(!s.contains(symbols[0])|| !s.contains(symbols[1])){
				removeArrayList.add(s);
			}
		}
		for(ArrayList<Symbol> s: removeArrayList){
			neighbouringSymbols.remove(s);
		}
		if(neighbouringSymbols.size()!=1){
			//TODO: vergelijken met vorige absolute positie van laatste positiebepalen(indien eerste keer (0,0))
			//TODO: de kleinste afstand is de juiste
			System.out.println("PROBLEEM!");
		}
		
		ArrayList<Symbol> mapRoster = neighbouringSymbols.get(0);
		Symbol ourSymbolInMap=mapRoster.get(0);
		//TODO: getOrientation and position
		double distanceInPixel = Math.sqrt((pixelRef[0]-imageSize.getWidth()/2)*((pixelRef[0]-imageSize.getWidth()/2))+
				((pixelRef[1]-imageSize.getHeight()/2)*(pixelRef[1]-imageSize.getHeight()/2)));
		double[] positionOfOurSymbol={ourSymbolInMap.getX(),ourSymbolInMap.getY()};
		return positionOfOurSymbol;
	}
	private static Dimension imageSize = new Dimension(800,600);
/**
 * If there are more than 3 symbols in the array then it gives back the array with 3 symbols. symbol[0] and the symbols those lie nearest to it.
 * @param symbols
 * @return
 */
	private Symbol[] filterSymbols(Symbol[] symbols) {
		double smallestDistance = Double.MAX_VALUE;
		double smallestDistance1 = Double.MAX_VALUE;
		Symbol[] filteredSymbol = new Symbol[2];
		for(int i =1 ; i<symbols.length;i++){
			double distance =distanceBetween(symbols[0],symbols[i]);
			if(smallestDistance>distance){
				smallestDistance=distance;
				filteredSymbol[0]=symbols[i];
			}
			else if(smallestDistance1>distance){
				smallestDistance1=distance;
				filteredSymbol[1]= symbols[i];
			}
		}
		return filteredSymbol;
	}
/**
 * Returns the pixeldistance between two symbols.
 * @param symbol
 * @param symbol2
 * @returnm
 */
	private double distanceBetween(Symbol symbol, Symbol symbol2) {
		return Math.sqrt((symbol.getX()-symbol2.getX())*(symbol.getX()-symbol2.getX())+
				(symbol.getY()-symbol2.getY())*(symbol.getY()-symbol2.getY()));
	}

	/**
	 * Returns the array of symbols those like near the given index. The first one is always the symbol itself. 
	 * @param ds
	 * 			
	 * @return
	 */
	private ArrayList<Symbol> getNeighbouringSymbols(int[] ds) {
		ArrayList<Symbol> neighbouringSymbols = new ArrayList<Symbol>();
		Symbol ourSymbolInMap = map.getSymbol(ds[0], ds[1]);
		Symbol symbol1 = map.getSymbol(ds[0]+1, ds[1]);
		if(symbol1!=null && symbol1.getColour()!=Symbol.Colour.BLANK){
			neighbouringSymbols.add(symbol1);
		}
		Symbol symbol2 = map.getSymbol(ds[0]-1, ds[1]);
		if(symbol2!=null && symbol2.getColour()!=Symbol.Colour.BLANK){
			neighbouringSymbols.add(symbol2);
		}
		Symbol symbol3 = map.getSymbol(ds[0], ds[1]-1);
		if(symbol3!=null && symbol3.getColour()!=Symbol.Colour.BLANK){
			neighbouringSymbols.add(symbol3);
		}
		Symbol symbol4 = map.getSymbol(ds[0], ds[1]+1);;
		if(symbol4!=null && symbol4.getColour()!=Symbol.Colour.BLANK){
			neighbouringSymbols.add(symbol4);
		}
		Symbol symbol5,symbol6;
		if(ds[1]%2==0){
			 symbol5 = map.getSymbol(ds[0]-1, ds[1]-1);
			 symbol6 = map.getSymbol(ds[0]-1, ds[1]+1);;
		}
		else{
			 symbol5 = map.getSymbol(ds[0]+1, ds[1]-1);
			 symbol6 = map.getSymbol(ds[0]+1, ds[1]+1);;
		}
		if(symbol5!=null && symbol5.getColour()!=Symbol.Colour.BLANK){
			neighbouringSymbols.add(symbol5);
		}
		if(symbol6!=null && symbol6.getColour()!=Symbol.Colour.BLANK){
			neighbouringSymbols.add(symbol6);
		}
		
		
		return neighbouringSymbols;
	}

}
