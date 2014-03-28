package navigation;

import gui.GuiCommands;

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
 * POSSIBLY THIS NEEDS TO BE CHANGED (90° clockwise or counterclockwise)
 * 
 * symbols are considered starting on the right, clockwise
 */
public class LocationLocator {

	private Map map;
	private PositionController xpos;
	private GuiCommands guic;
	private PositionController ypos;

	public LocationLocator(Map map, PositionController xpos, PositionController ypos, GuiCommands guic) {
		this.map = map;
		this.xpos = xpos;
		this.ypos = ypos;
		this.guic = guic;
	}

	public void locateAndMove(List<Symbol> colors){
		double[] moveTo = locate(colors);
		guic.receiveLocation(moveTo[0]*10, moveTo[1]*10, true);
		xpos.run(moveTo);
		ypos.run(moveTo);	
	}

	/**
	 * For a given list of recognised Symbols, gives the
	 * x-coordinate, y-coordinate and alpha relative to the default plane (= pointing upward)
	 * @param colors
	 */
	public double[] locate(List<Symbol> colors){
		double[] totalCenter = calculateCenter(colors);
		Symbol middle = nearestSymbol(colors, totalCenter);
		//JOptionPane.showMessageDialog(null, middle.getColour() + "  " + middle.getShape());
		colors.remove(middle);
		return locate(colors,middle);
	}
	
	public double[] locate(List<Symbol> colors, Symbol middle) {
		Symbol closestToMid = nearestSymbol(colors,middle);
		double closestToMidDist = euclideanDistance(middle.getX(), middle.getY(), closestToMid.getX(),
				closestToMid.getY());

		//filter all symbols surrounding the middle symbol
		double[] middleCoordinates = {middle.getX(),middle.getY()};
		List<Symbol> neighbours = filter(colors,1.2*closestToMidDist,middleCoordinates);
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
			if(euclideanDistance(s1.getX(), s1.getY(), s2.getX(), s2.getY())<1.2*closestToMidDist &&
					s1.getX()*s2.getY()-s1.getY()*s2.getX() < 0) {
				List<Symbol> list = new LinkedList<>();
				list.add(s1);
				list.add(s2);
				possibleSymbolLists.add(list);
			}
		}

		List<double[]> possibleLocs = new LinkedList<>();
		for(List<Symbol> possibleSymbolList : possibleSymbolLists) {
			double[] loc = find3(possibleSymbolList.get(0),possibleSymbolList.get(1),middle);
			if(loc != null) {
				//loc = correctAlpha(loc,possibleSymbolList.get(0));
				possibleLocs.add(loc);
			}
		}

		String s = "";
		for(double[] r:possibleLocs) {
			s = s + r[0] + "," + r[1] + "||" + r[2] + "\n";
		}
		JOptionPane.showMessageDialog(null, s);
		try{
			return possibleLocs.get(0);
		}catch( Exception e){
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
	 * @return
	 */
	public double alphaOtherWay(Symbol center,Symbol s1,Symbol s2,
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
	public double alphaOtherWay(double x1, double y1, double x2, double y2) {
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
		if(x2>x1) {
			return alpha;
		}
		//Q2
		if(y1>y2){
			//JOptionPane.showMessageDialog(null, "q2");
			return -(180-alpha);
		}
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

	private double[] find3(Symbol symbol1,Symbol symbol2,Symbol center) {
		//for now, linear search
		//might use priority queue instead
		int symbolsPerRow = map.getSymbolsOnRow();
		int lines = map.getRows();
		//odd lines (index even) => left aligned => i,i-1
		//even lines (index odd) => right aligned => i,i+1
		for(int i=0;i<lines;i++) {
			for(int j=0;j<symbolsPerRow;j++) {
				if(center.getColour()==map.getSymbol(j, i).getColour()
						&& center.getShape()==map.getSymbol(j, i).getShape()) {
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
							return r;
						}
					}
				}
			}
		}
		return null;
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
		return (symbolImage1.getColour() == s1.getColour() && symbolImage1.getShape() == s1.getShape() &&
				symbolImage2.getColour() == s2.getColour() && symbolImage2.getShape() == s2.getShape());
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
	 * Retrieves the ColorSymbol closest to the center coordinates.
	 * @param colors
	 * 			The center needs to be removed from this list already!
	 * @param center
	 * 			x- and y-coordinate
	 */
	private Symbol nearestSymbol(List<Symbol> colors, double[] center){
		double max = Double.MAX_VALUE;
		int smallestOne=0;
		for(int i=0; i<colors.size(); i++){
			double distance =euclideanDistance(colors.get(i).getX(),colors.get(i).getY(),center[0],center[1]);
			if(distance<max){
				smallestOne=i;
				max=distance;
			}
		}
		return colors.get(smallestOne);
	}

	/**
	 * Retrieves the Symbol closest to the center Symbol.
	 * @param colors
	 * @param center
	 */
	private Symbol nearestSymbol(List<Symbol> colors, Symbol center) {
		if(colors.contains(center))
			colors.remove(center);
		double[] coordinates = {center.getX(),center.getY()};
		return nearestSymbol(colors,coordinates);
	}

	private double euclideanDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
	}

	/**
	 * Calculates the center of the populated image (center of symbol concentration)
	 * @param colors
	 * @return
	 * 			r[0]: x, r[1]: y
	 */
	private double[] calculateCenter(List<Symbol> colors){
		double x=0;
		double y=0;
		for(int i =0; i<colors.size(); i++){
			x+= colors.get(i).getX();
			y+= colors.get(i).getY();
		}
		double[] center = new double[2];
		center[0]=1.0*x/colors.size();
		center[1]=1.0*y/colors.size();
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
		Symbol center0 = new Symbol("BR");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("BR");
		s10.setX(40);s10.setY(68);
		//list2.add(s10);
		Symbol s20 = new Symbol("RS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
//		JOptionPane.showMessageDialog(null,sort.get(0).getX() + "," + sort.get(0).getY() + "\n" + 
//				sort.get(1).getX() + "," + sort.get(1).getY());
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2,s10);
		//JOptionPane.showMessageDialog(null,loc0[0] + "," + loc0[1] + "|" + loc0[2]);
	}
}
