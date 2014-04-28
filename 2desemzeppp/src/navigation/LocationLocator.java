package navigation;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import map.*;

/**
 * A class for finding the zeppelin's location on the board.
 * At this moment, the class assumes an image taken using the camera with
 * the UPPER part of the image AIMED AT THE FRONT (this would correspond to decreasing y-value
 * in the grid).
 * 
 * Coordinates and alpha are the same as in PositionController:
 * (0,0) is top left
 * alpha = 0: points up, alpha > 0: clockwise, alpha < 0: counterclockwise
 * 
 * symbols are considered starting on the right, clockwise
 */
public class LocationLocator {

	private Map map;

	private double recentX,recentY;

	/**
	 * Initialises a LocationLocator with given map, positioncontrollers for x and y, and gui commands.
	 * 
	 * @param map
	 * @param xpos
	 * @param ypos
	 * @param guic
	 */
	public LocationLocator(Map map) {
		this.map = map;
	}

	/**
	 * Find the location, send it to the gui, and notify the positioncontrollers.
	 * 
	 * @param symbols
	 */
	public void locateAndMove(List<Symbol> symbols){
		double[] loc = locate(symbols);
		locAndMove(loc);	
	}
	
	/**
	 * Find the location, send it to gui and notify positioncontrollers.
	 * This should be used whenever a triangle has been found.
	 * s1 should be the one closest to the center
	 * @param symbols
	 */
	public void locAndMoveWithTriangle(Symbol s1, Symbol s2, Symbol s3) {
		List<Symbol> s = new LinkedList<>();
		s.add(s2);
		s.add(s1);
		s = sortImageSymbolPolar(s, s1);
		List<double[]> possibleLocs = find3(s.get(0),s.get(1),s1);
		double[] l;
		try{
			double[] nearest = possibleLocs.get(0);
			double nearestDist = euclideanDistance(recentX,recentY,nearest[0],nearest[1]);
			for(double[] loc:possibleLocs) {
				double dist = euclideanDistance(recentX,recentY,loc[0],loc[1]);
				if(dist < nearestDist) {
					nearest = loc;
					nearestDist = dist;
				}
			}

			recentX = nearest[0];
			recentY = nearest[1];
			l = nearest;
		}catch(Exception e){
			double[] l1 = {200,200,0};
			l = l1;
		}
		locAndMove(l);
	}

	/**
	 * Should be called after the loc has been found. Notifies the Dispatch of the new loc.
	 * @param loc
	 */
	private void locAndMove(double[] loc) {
		Dispatch.nowAtLoc(loc);
	}

	/**
	 * For a given list of recognised Symbols, gives the
	 * x-coordinate, y-coordinate and alpha relative to the default plane (= pointing upward)
	 * @param colors
	 */
	public double[] locate(List<Symbol> symbols){
		double[] totalCenter = calculateCenter(symbols);
		Symbol middle = nearestSymbol(symbols, totalCenter);
		//JOptionPane.showMessageDialog(null, middle.getColour() + "  " + middle.getShape());
		symbols.remove(middle);
		return locate(symbols,middle);
	}

	/**
	 * For a given list of symbols and the middle symbol,
	 * gives the x-coordinate, y-coordinate and alpha.
	 * @param symbols
	 * @param middle
	 */
	public double[] locate(List<Symbol> symbols, Symbol middle) {
		Symbol closestToMid = nearestSymbol(symbols,middle);
		double closestToMidDist = euclideanDistance(middle.getX(), middle.getY(), closestToMid.getX(),
				closestToMid.getY());

		//filter all symbols surrounding the middle symbol
		double[] middleCoordinates = {middle.getX(),middle.getY()};
		List<Symbol> neighbours = filter(symbols,1.2*closestToMidDist,middleCoordinates);
		neighbours = sortImageSymbolPolar(neighbours,middle);
		//JOptionPane.showMessageDialog(null, neighbours.get(0).getColour() + "," + neighbours.get(1).getColour());

		//find a set of 3 symbols
		//in addition to the middle, 2 more are needed
		List<List<Symbol>> possibleSymbolLists = new LinkedList<>();

		for(int i = 0;i<neighbours.size();i++) {
			Symbol s1 = neighbours.get(i);
			Symbol s2 = neighbours.get((i+1)%neighbours.size());
			//if these symbols are next to each other
			//&& the second is located clockwise of the first
			double x1 = s1.getX() - middle.getX();
			double y1 = s1.getY() - middle.getY();
			double x2 = s2.getX() - middle.getX();
			double y2 = s2.getY() - middle.getY();
			if(euclideanDistance(s1.getX(), s1.getY(), s2.getX(), s2.getY())<1.2*closestToMidDist &&
					x1*y2-y1*x2 < 0) {
				List<Symbol> list = new LinkedList<>();
				list.add(s1);
				list.add(s2);
				possibleSymbolLists.add(list);
			}
		}

		List<double[]> possibleLocs = new LinkedList<>();
		for(List<Symbol> possibleSymbolList : possibleSymbolLists) {
			List<double[]> loc = find3(possibleSymbolList.get(0),possibleSymbolList.get(1),middle);
			if(loc != null) {
				//loc = correctAlpha(loc,possibleSymbolList.get(0));
				possibleLocs.addAll(loc);
			}
		}

		String s = "";
		for(double[] r:possibleLocs) {
			s = s + r[0] + "," + r[1] + "||" + r[2] + "\n";
		}
		//JOptionPane.showMessageDialog(null, s);
		
		try{
			double[] nearest = possibleLocs.get(0);
			double nearestDist = euclideanDistance(recentX,recentY,nearest[0],nearest[1]);
			for(double[] loc:possibleLocs) {
				double dist = euclideanDistance(recentX,recentY,loc[0],loc[1]);
				if(dist < nearestDist) {
					nearest = loc;
					nearestDist = dist;
				}
			}

			recentX = nearest[0];
			recentY = nearest[1];
			return nearest;
		}catch(Exception e){
			double[] l = {200,200,0};
			return l;
		}
	}

	/**
	 * Calculates alpha, by matching symbols and finding the two that should
	 * be on one line.
	 * @param center
	 * @param s1
	 * @param s2
	 * @param centermap
	 * @param sm1
	 * @param sm2
	 */
	private double alphaOtherWay(Symbol center,Symbol s1,Symbol s2,
			Symbol centermap, Symbol sm1, Symbol sm2) {
		if(centermap.getY() == sm1.getY()) {
			if(centermap.getX() < sm1.getX())
				return alphaOtherWay(center.getX(),center.getY(),s1.getX(),s1.getY());
			return alphaOtherWay(s1.getX(),s1.getY(),center.getX(),center.getY());
		}
		if(sm1.getY() == sm2.getY()) {
			if(sm1.getX() < sm2.getX())
				return alphaOtherWay(s1.getX(),s1.getY(),s2.getX(),s2.getY());
			return alphaOtherWay(s2.getX(),s2.getY(),s1.getX(),s1.getY());
		}
		if(sm2.getX() < centermap.getX())
			return alphaOtherWay(s2.getX(),s2.getY(),center.getX(),center.getY());
		return alphaOtherWay(center.getX(),center.getY(),s2.getX(),s2.getY());
	}

	/**
	 * Given the coordinates of the points that should be horizontally on the same line,
	 * calculates alpha.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private double alphaOtherWay(double x1, double y1, double x2, double y2) {
		if(y1==y2) {
			if(x1<x2)
				return 0;
			return 180;
		}
		if(x1==x2) {
			if(y1>y2)
				return 90;
			return -90;
		}
		//JOptionPane.showMessageDialog(null, x1 + "," + y1 + "," + x2 + "," + y2);
		double vy = y2-y1;
		double vx = x2-x1;
		double alpha0 = Math.atan(vy/vx);
		double alpha = alpha0/Math.PI*180;
		//JOptionPane.showMessageDialog(null, alpha0 + "," + alpha);
		//Q1 & Q4
		if(x2>x1)
			return alpha;
		//Q2
		if(y1>y2)
			return -(180-alpha);
		//Q4
		return 180+alpha;
	}

	private double[] correctAlpha(double[] loc, Symbol s) {
		//assumes first symbol of neighbours is in right corner
		//loc[2] = loc[2] + 120 - i*60;
		if(loc[2] < -180)
			loc[2] = loc[2] + 360;
		if(loc[2] > 180)
			loc[2] = loc[2] - 360;
		//x < 0 && y > 10 => top left ==> OK
		if(s.getX() > 0 && s.getY() > 10) // top right
			loc[2] = loc[2] + 60;
		if(s.getX() > 0 && s.getY() < 10 && s.getY() > -10) // right
			loc[2] = loc[2] + 120;
		if(s.getX() > 0 && s.getY() < -10)
			loc[2] = loc[2] + 180;
		if(s.getX() < 0 && s.getY() < -10)
			loc[2] = loc[2] + 240;
		if(s.getX() < 0 && s.getY() < 10 && s.getY() > -10)
			loc[2] = loc[2] + 300;
		while(loc[2] > 180)
			loc[2] = loc[2] - 360;
		return loc;
	}

	private List<double[]> find3(Symbol symbol1,Symbol symbol2,Symbol center) {
		List<double[]> locs = new LinkedList<>();

		//for now, linear search
		//might use priority queue instead
		int symbolsPerRow = map.getSymbolsOnRow();
		int lines = map.getRows();
		//odd lines (index even) => left aligned => i,i-1
		//even lines (index odd) => right aligned => i,i+1
		for(int i=0;i<lines;i++) {
			for(int j=0;j<symbolsPerRow;j++) {
				if(center.colourMatch(map.getSymbol(j, i)) && center.shapeMatch(map.getSymbol(j, i))) {
					//potential mid
					Symbol leftup,rightup,leftdown,rightdown;
					if(i%2==0) {
						leftup = map.getSymbol(j-1, i-1);
						rightup = map.getSymbol(j, i-1);
						leftdown = map.getSymbol(j-1, i+1);
						rightdown = map.getSymbol(j, i+1);
					}
					else {
						leftup = map.getSymbol(j, i-1);
						rightup = map.getSymbol(j+1, i-1);
						leftdown = map.getSymbol(j, i+1);
						rightdown = map.getSymbol(j+1, i+1);
					}

					Symbol left = map.getSymbol(j-1, i);
					Symbol right = map.getSymbol(j+1, i);
					LinkedList<Symbol> symbols = new LinkedList<>();
					symbols.add(right);symbols.add(rightdown);symbols.add(leftdown);
					symbols.add(left);symbols.add(leftup);symbols.add(rightup);
					for(int k=0;k<6;k++) {
						if(match3(symbol1,symbol2,symbols,k)) {
							//example: sorted coloursymbols s1,s2 = left up, right up
							//match at k = 0 ==> left up in image = right in map
							//alpha = 120
							//match at k = 1 ==> left up in image = right bottom in map
							//alpha = 180
							//coordinates of zeppelin
							double x = map.getSymbol(j, i).getX();
							double y = map.getSymbol(j, i).getY();

							double alpha = alphaOtherWay(center,symbol1,symbol2,map.getSymbol(j, i),
									symbols.get(k),symbols.get((k+1)%6));
							//							
							//							double alpha = 120+k*60; //120=>480
							//							if(alpha > 180)
							//								alpha = alpha - 360; //-180 => 180


							double[] r = {x,y,alpha};
							locs.add(r);
						}
					}
				}
			}
		}
		return locs;
	}

	/**
	 * For 2 symbols next to each other (around a center) on the image, checks if they match any group of
	 * 2 in the list of neighbour symbols around the center.
	 * @param symbolImage1
	 * @param symbolImage2
	 * @param symbolsOnMap
	 * @param i
	 */
	private boolean match3(Symbol symbolImage1, Symbol symbolImage2, List<Symbol> symbolsOnMap,int i) {
		Symbol s1 = symbolsOnMap.get(i);
		Symbol s2 = symbolsOnMap.get((i+1)%6);
		if(s1 == null || s2 == null)
			return false;
		return (symbolImage1.colourMatch(s1) && symbolImage1.shapeMatch(s1) &&
				symbolImage2.colourMatch(s2) && symbolImage2.shapeMatch(s2));
	}

	/**
	 * For a list of Symbol objects, selects only those objects
	 * not further than a specified distance from the center.
	 */
	private List<Symbol> filter(List<Symbol> list,double threshold,double[] center) {
		List<Symbol> filtered = new LinkedList<>();
		for(Symbol symbol:list) {
			if(euclideanDistance(symbol.getX(),symbol.getY(),center[0],center[1]) <= threshold) {
				filtered.add(symbol);
			}
		}
		return filtered;
	}

	/**
	 * Retrieves the Symbol closest to the center coordinates.
	 * @param symbols
	 * 			The center needs to be removed from this list already!
	 * @param center
	 * 			x- and y-coordinate
	 */
	private Symbol nearestSymbol(List<Symbol> symbols, double[] center){
		double max = Double.MAX_VALUE;
		int smallestOne=0;
		for(int i=0; i<symbols.size(); i++){
			double distance =euclideanDistance(symbols.get(i).getX(),symbols.get(i).getY(),center[0],center[1]);
			if(distance<max){
				smallestOne=i;
				max=distance;
			}
		}
		return symbols.get(smallestOne);
	}

	/**
	 * Retrieves the Symbol closest to the center Symbol.
	 * @param symbols
	 * @param center
	 */
	private Symbol nearestSymbol(List<Symbol> symbols, Symbol center) {
		if(symbols.contains(center))
			symbols.remove(center);
		double[] coordinates = {center.getX(),center.getY()};
		return nearestSymbol(symbols,coordinates);
	}

	private double euclideanDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
	}

	/**
	 * Calculates the center of the populated image (center of symbol concentration)
	 * @param symbols
	 * @return
	 * 			r[0]: x, r[1]: y
	 */
	private double[] calculateCenter(List<Symbol> symbols){
		double x=0;
		double y=0;
		for(int i =0; i<symbols.size(); i++){
			x+= symbols.get(i).getX();
			y+= symbols.get(i).getY();
		}
		double[] center = new double[2];
		center[0]=1.0*x/symbols.size();
		center[1]=1.0*y/symbols.size();
		return center;
	}

	/**
	 * Compares angles and orders them according to polar coordinates.
	 * Lowest value is far right ==> 0°. Angles increase in clockwise order.
	 */
	private static class AngleComparator implements Comparator<Symbol> {
		@Override
		public int compare(Symbol s1, Symbol s2) {
			if(s1 == null)
				return -1;
			if(s2 == null)
				return 1;
			if(s1.getY()==0 && s1.getX()>0)
				return -1;
			if(s2.getY()==0 && s2.getX()>0)
				return 1;
			if(s1.getY()>0 && s2.getY()<0)
				//!!because y-frame points in opposite direction
				return -1;
			if(s2.getY()>0 && s1.getY()<0)
				//!! because y-frame points in opposite direction
				return 1;
			if(-1*s1.getX()*s2.getY()+s1.getY()*s2.getX()>0)
				return 1;
			return -1;
		}
	}

	/**
	 * This comparator also sorts the symbols using polar coordinates,
	 * but it assumes a default plane.
	 * Therefore, this should be used when working with symbols read from an image.
	 */
	private static class ImageSymbolComparator implements Comparator<Symbol> {
		@Override
		public int compare(Symbol s1, Symbol s2) {
			if(s1 == null)
				return -1;
			if(s2 == null)
				return 1;
			if(s1.getY()==0 && s1.getX()>0)
				return -1;
			if(s2.getY()==0 && s2.getX()>0)
				return 1;
			if(s1.getY()>0 && s2.getY()<0)
				return 1;
			if(s2.getY()>0 && s1.getY()<0)
				return -1;
			if(1*s1.getX()*s2.getY()-s1.getY()*s2.getX()>0)
				return 1;
			return -1;
		}
	}

	/**
	 * Comparator for comparing symbols with polar coordinates.
	 * Assumes x points right and y points up.
	 * Should be used for symbols from images.
	 * Lowest value is on the far right, alpha increases in clockwise order.
	 */
	private static class ImageSymbolComparatorNoChangeCoord implements Comparator<Symbol> {
		private Symbol center;

		public ImageSymbolComparatorNoChangeCoord(Symbol center) {
			this.center = center;
		}

		@Override
		public int compare(Symbol s1, Symbol s2) {
			if(s1 == null)
				return -1;
			if(s2 == null)
				return 1;
			double x1 = s1.getX() - center.getX();
			double y1 = s1.getY() - center.getY();
			double x2 = s2.getX() - center.getX();
			double y2 = s2.getY() - center.getY();
			if(y1==0 && x1>0)
				return -1;
			if(y2==0 && x2>0)
				return 1;
			if(y1>0 && y2<0)
				return 1;
			if(y2>0 && y1<0)
				return -1;
			if(1*x1*y2-y1*x2>0)
				return 1;
			return -1;
		}
	}

	/**
	 * Sort a list of symbols according to their polar coordinates, around the center.
	 * The lowest value is the far right ==> 0°
	 * Angles increase in clockwise order.
	 * @param list
	 * @return
	 * 			The list's coordinates are the coordinates after a coordinate transformation
	 * 			using the given center.
	 */
	private static List<Symbol> sortPolar(List<Symbol> list,Symbol center) {
		List<Symbol> sorted = new LinkedList<>();
		if(list.contains(center)) {
			list.remove(center);
		}
		for(Symbol s : list) {
			Symbol copy = s.copy();
			//coordinate transformation
			double x0 = copy.getX() - center.getX();
			double y0 = copy.getY() - center.getY();
			copy.setX(x0);
			copy.setY(y0);
			sorted.add(copy);
		}
		Collections.sort(sorted,new AngleComparator());
		return sorted;
	}

	/**
	 * Sort a list of symbols in an image according to their polar coordinates, around the center.
	 * The lowest value is the far right ==> 0°
	 * Angles increase in clockwise order.
	 * This should be used for Symbols on an image, not for Symbols on the map.
	 * @param list
	 * @return
	 * 			The list's coordinates are the coordinates after a coordinate transformation
	 * 			using the given center.
	 */
	private static List<Symbol> sortImageSymbolPolar(List<Symbol> list,Symbol center) {
		List<Symbol> sorted = new LinkedList<>();
		if(list.contains(center)) {
			list.remove(center);
		}
		for(Symbol s : list) {
			Symbol copy = s.copy();
			//coordinate transformation
			//			double x0 = copy.getX() - center.getX();
			//			double y0 = copy.getY() - center.getY();
			//			copy.setX(x0);
			//			copy.setY(y0);
			sorted.add(copy);
		}
		Collections.sort(sorted,new ImageSymbolComparatorNoChangeCoord(center));
		return sorted;
	}
	
	/**
	 * Checks whether or not: distance between point 1 and 2 <= threshold
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param threshold
	 */
	public boolean nearLoc(double x1, double y1, double x2, double y2, double threshold) {
		return euclideanDistance(x1,y1,x2,y2) <= threshold;
	}
	
	

	public static void main(String[] args) {
		//test polar sort
		List<Symbol> list = new LinkedList<>();
		Symbol center = new Symbol("RR");
		center.setX(50);center.setY(50);
		list.add(center);
		Symbol s1 = new Symbol("RR");
		s1.setX(60);s1.setY(50);
		list.add(s1);
		Symbol s2 = new Symbol("RR");
		s2.setX(60);s2.setY(60);
		list.add(s2);
		Symbol s3 = new Symbol("RR");
		s3.setX(50);s3.setY(70);
		list.add(s3);
		Symbol s4 = new Symbol("RR");
		s4.setX(40);s4.setY(40);
		list.add(s4);
		Symbol s5 = new Symbol("RR");
		s5.setX(60);s5.setY(40);
		list.add(s5);
		Symbol s6 = new Symbol("RR");
		s6.setX(40);s6.setY(50);
		list.add(s6);

		List<Symbol> sort = sortPolar(list,center);
		sort = sortImageSymbolPolar(list,center);
		//		JOptionPane.showMessageDialog(null,sort.get(0).getX() + "," + sort.get(0).getY() + "\n" + 
		//				sort.get(1).getX() + "," + sort.get(1).getY() + "\n" + 
		//				sort.get(2).getX() + "," + sort.get(2).getY() + "\n" + 
		//				sort.get(3).getX() + "," + sort.get(3).getY() + "\n" + 
		//				sort.get(4).getX() + "," + sort.get(4).getY() + "\n" + 
		//				sort.get(5).getX() + "," + sort.get(5).getY() + "\n");

		//find location
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("WS");
		center0.setX(50);center0.setY(50);
		//list2.add(center0);
		Symbol s10 = new Symbol("YR");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("RS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		Symbol s21 = new Symbol("WR");
		s21.setX(70);s21.setY(50);
		list2.add(s21);
		Symbol s22 = new Symbol("BS");
		s22.setX(60);s22.setY(32);
		list2.add(s22);
		Symbol s23 = new Symbol("YR");
		s23.setX(40);s23.setY(32);
		list2.add(s23);
		Symbol s24 = new Symbol("BR");
		s24.setX(30);s24.setY(50);
		list2.add(s24);

		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"));
		double[] loc0 = locator.locate(list2,center0);
		//JOptionPane.showMessageDialog(null,loc0[0] + "," + loc0[1] + "|" + loc0[2]);
	}
}
