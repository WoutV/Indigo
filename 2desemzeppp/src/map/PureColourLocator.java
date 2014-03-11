package map;

import gui.GuiCommands;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import zeppelin.PositionController;


/**
 * A class for navigating using ONLY the colour of symbols, not the shape.
 * At this moment, the class assumes an image taken using the camera with
 * the UPPER part of the image AIMED AT THE FRONT (this would correspond to decreasing y-value
 * in the grid).
 * POSSIBLY THIS NEEDS TO BE CHANGED (90° clockwise or counterclockwise)
 * 
 * symbols are considered starting on the right, clockwise
 */
public class PureColourLocator {

	private Map map;
	private PositionController xpos;
	private GuiCommands guic;
	private PositionController ypos;

	public PureColourLocator(Map map, PositionController xpos, PositionController ypos, GuiCommands guic) {
		this.map = map;
		this.xpos = xpos;
		this.ypos = ypos;
		this.guic = guic;
	}

	public void locateAndMove(List<ColorSymbol> colors){
		double[] moveTo = locate(colors);
		guic.receiveLocation(moveTo[0]*10, moveTo[1]*10, true);
		xpos.run(moveTo);
		ypos.run(moveTo);
		
	}
	/**
	 * For a given list of recognised ColourSymbols, gives the
	 * x-coordinate, y-coordinate and alpha relative to the default plane (= pointing upward)
	 * @param colors
	 */
	public double[] locate(List<ColorSymbol> colors){
		double[] totalCenter = new double[2];
		totalCenter = calculateCenter(colors);
		ColorSymbol middle = nearestSymbol(colors, totalCenter);
		colors.remove(middle);
		//ColorSymbol nextBestSymbol = nearestSymbol(colors,middle.coordinate);
		//try to get the 6 symbols surrounding the middle symbol
		//technically, all of these are at the same distance
		//using a margin of 1.2 here
		Collections.sort(colors, sixBestNeighboursComparator(middle));
		while(colors.size()>6){
			colors.remove(colors.size()-1);
		}
//		double [] mid = middle.coordinate;
//		double [] nearestToMid= nextBestSymbol.coordinate;
//		double dist = euclideanDistance(mid, nearestToMid);
//		List<ColorSymbol> neighbours = filter(colors,1.6*dist,mid);
		List<ColorSymbol> neighbours = colors;
		if(neighbours.size() == 6) {
			//all 6 neighbours are known
			//sort them
			neighbours = sortColourSymbolPolar(neighbours, middle);
			double[] r = find6(neighbours,middle);
			if(r != null)
				return r;
		}
		
		neighbours = sortColourSymbolPolar(neighbours, middle);
		
		if(colors.size()<6 && colors.size()>2){
			while(colors.size()>2){
				colors.remove(colors.size()-1);
			}
		}
		List<double[]> possiblelocs = new LinkedList<>();
//		JOptionPane.showMessageDialog(null, middle.colour + "," + neighbours.get(0).colour + 
//				"," + neighbours.get(1).colour + "," + neighbours.get(2).colour);
		for(int i = 0;i<neighbours.size();i++) {
			if(//euclideanDistance(neighbours.get(i).coordinate,neighbours.get((i+1)%neighbours.size()).coordinate) < 1.4*dist
				 neighbours.get(i).coordinate[0]*neighbours.get((i+1)%neighbours.size()).coordinate[1]-
					neighbours.get(i).coordinate[1]*neighbours.get((i+1)%neighbours.size()).coordinate[0]<0) {
			List<double[]> r = find3(neighbours.get(i),neighbours.get((i+1)%neighbours.size()),middle);
			for(double[] loc:r) {
				//assumes first symbol of neighbours is in right corner
				//loc[2] = loc[2] + 120 - i*60;
				if(loc[2] < -180)
					loc[2] = loc[2] + 360;
				if(loc[2] > 180)
					loc[2] = loc[2] - 360;
				if(neighbours.get(i).coordinate[0] < 0 && neighbours.get(i).coordinate[1] == 0)
					loc[2] = loc[2] + 300;
				if(neighbours.get(i).coordinate[0] > 0 && neighbours.get(i).coordinate[1] > 0)
					loc[2] = loc[2] + 60;
				if(neighbours.get(i).coordinate[0] > 0 && neighbours.get(i).coordinate[1] == 0)
					loc[2] = loc[2] + 120;
				if(neighbours.get(i).coordinate[0] > 0 && neighbours.get(i).coordinate[1] < 0)
					loc[2] = loc[2] + 180;
				if(neighbours.get(i).coordinate[0] < 0 && neighbours.get(i).coordinate[1] < 0)
					loc[2] = loc[2] + 240;
				if(loc[2] > 180)
					loc[2] = loc[2] - 360;
				possiblelocs.add(loc);
			}
			}
		}
		String s = "";
		for(double[] r:possiblelocs) {
			s = s + r[0] + "," + r[1] + "||" + r[2] + "\n";
		}
		//JOptionPane.showMessageDialog(null, s);
		try{
			return possiblelocs.get(0);
		}catch( Exception e){
			double[] l = {200,200,0};
			return l;
		}
		
		//select a triangle: 2 symbols who are at about the same distance
	}

	private List<double[]> find3(ColorSymbol symbol1,ColorSymbol symbol2,ColorSymbol center) {
		//for now, linear search
		//might use priority queue instead
		int symbolsPerRow = map.getSymbolsOnRow();
		int lines = map.getRows();
		List<double[]> possiblelocs = new LinkedList<>();
		//odd lines (index even) => left aligned => i,i-1
		//even lines (index odd) => right aligned => i,i+1
		for(int i=0;i<lines;i++) {
			for(int j=0;j<symbolsPerRow;j++) {
				if(center.colour==map.getSymbol(j, i).getColour()) {
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
							double alpha = 120+k*60; //120=>480
							if(alpha > 180)
								alpha = alpha - 360; //-180 => 180
							double[] r = {x,y,alpha};
							possiblelocs.add(r);
						}
					}
				}
			}
		}
		return possiblelocs;
	}
	
	/**
	 * For 2 images next to each other (around a center) on the image, checks if they match any group of
	 * 2 in the list of neighbour symbols around the center.
	 * @param symbolImage1
	 * @param symbolImage2
	 * @param symbolsOnMap
	 * @param i
	 * @return
	 */
	private boolean match3(ColorSymbol symbolImage1, ColorSymbol symbolImage2, List<Symbol> symbolsOnMap,int i) {
		Symbol s1 = symbolsOnMap.get(i);
		Symbol s2 = symbolsOnMap.get((i+1)%6);
		if(s1 == null || s2 == null)
			return false;
		return (symbolImage1.colour == s1.getColour() && symbolImage2.colour == s2.getColour());
	}

	/**
	 * For a list of ColorSymbol objects, selects only those objects
	 * not further than a specified distance from the center.
	 */
	private List<ColorSymbol> filter(List<ColorSymbol> list,double threshold,double[] center) {
		List<ColorSymbol> filtered = new LinkedList<>();
		for(ColorSymbol symbol:list) {
			if(euclideanDistance(symbol.coordinate, center) <= threshold) {
				filtered.add(symbol);
			}
		}
		return filtered;
	}

	/**
	 * Given a list of 6 (!!) neighbours, sorted in clockwise (!!) order,
	 * finds the coordinates in cm on the map, and alpha relative to the default frame.
	 * @param neighbours
	 * @param mid
	 * @param map
	 */
	public double[] find6(List<ColorSymbol> neighbours,ColorSymbol mid) {
		//for now, linear search
		//might use priority queue instead
		int symbolsPerRow = map.getSymbolsOnRow();
		int lines = map.getRows();
		//odd lines (index even) => left aligned => i,i-1
		//even lines (index odd) => right aligned => i,i+1
		for(int i=0;i<lines;i++) {
			for(int j=0;j<symbolsPerRow;j++) {
				if(mid.colour==map.getSymbol(j, i).getColour()) {
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
					boolean allexist = true;
					for(Symbol s:symbols) {
						if(s==null)
							allexist=false;
					}
					//all 6 surrounding exist
					//now match them
					if(allexist) {
						for(int k=0;k<6;k++) {
							if(match(neighbours,symbols,k)) {
								//example: sorted coloursymbols 0-5 0=right of center
								//0-5 symbols surrounding a center
								//match at k = 0 ==> right in image = right in map
								//alpha = 0
								//match at k = 1 ==> rightbottom in image = right in map
								//alpha = -60
								//coordinates of zeppelin
								double x = map.getSymbol(j, i).getX();
								double y = map.getSymbol(j, i).getY();
								double alpha = k*(-60); //0==> -360
								if(alpha < -180)
									alpha = alpha + 360; //-180 => 180
								double[] r = {x,y,alpha};
								return r;
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Checks whether the colors in list1, starting from index i, match the colours in list2.
	 * @param list1
	 * @param list2
	 * @param i
	 */
	private boolean match(List<ColorSymbol> list1, List<Symbol> list2, int i) {
		return (list1.get(i).colour == list2.get(0).getColour() && list1.get((i+1)%6).colour == list2.get(1).getColour()
				&& list1.get((i+2)%6).colour == list2.get(2).getColour() && list1.get((i+3)%6).colour == list2.get(3).getColour()
				&& list1.get((i+4)%6).colour == list2.get(4).getColour() && list1.get((i+5)%6).colour == list2.get(5).getColour());
	}

	/**
	 * Retrieves the ColorSymbol closest to the center
	 * @param colors
	 * 			The center needs to be removed from this list already!
	 * @param center
	 * 			x- and y-coordinate
	 */
	public ColorSymbol nearestSymbol(List<ColorSymbol> colors, double[] center){
		double max = Double.MAX_VALUE;
		int smallestOne=0;
		for(int i=0; i<colors.size(); i++){
			double distance =euclideanDistance(colors.get(i).coordinate,center);
			if(distance<max){
				smallestOne=i;
				max=distance;
			}
		}
		return colors.get(smallestOne);
	}

	private double euclideanDistance(double[] symbol1, double[] symbol2){
		return Math.sqrt((symbol2[1]-symbol1[1])*(symbol2[1]-symbol1[1])+(symbol2[0]-symbol1[0])*(symbol2[0]-symbol1[0]));
	}

	/**
	 * Calculates the center of the populated image (center of symbol concentration)
	 * @param colors
	 * @return
	 * 			r[0]: x, r[1]: y
	 */
	public double[] calculateCenter(List<ColorSymbol> colors){
		double x=0;
		double y=0;
		for(int i =0; i<colors.size(); i++){
			x+= colors.get(i).coordinate[0];
			y+= colors.get(i).coordinate[1];
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
	 * Sort a list of symbols according to their polar coordinates, around the center.
	 * The lowest value is the far right ==> 0°
	 * Angles increase in clockwise order.
	 * @param list
	 * @return
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
	 * Sort a list of coloursymbols according to their polar coordinates.
	 * The lowest value is the far right ==> 0°
	 * Angles increase in clockwise order.
	 * @param list
	 * @return
	 */
	private static List<ColorSymbol> sortColourSymbolPolar(List<ColorSymbol> list,ColorSymbol center) {
		List<ColorSymbol> sorted = new LinkedList<>();
		if(list.contains(center)) {
			list.remove(center);
		}
		for(ColorSymbol s : list) {
			ColorSymbol copy = s.copy();
			//coordinate transformation
			copy.coordinate[0] = copy.coordinate[0] - center.coordinate[0];
			copy.coordinate[1] = copy.coordinate[1] - center.coordinate[1];
			sorted.add(copy);
		}
		Collections.sort(sorted,ColorSymbol.getAngularComparator());
		return sorted;
	}

	public static void main(String[] args) {
		//test ColourSymbolCompare
		List<ColorSymbol> list = new LinkedList<>();
		double[] coords = {50,50};
		ColorSymbol center = new ColorSymbol(coords,Symbol.Colour.RED);
		list.add(center);
		double[] coords1 = {60,50};
		list.add(new ColorSymbol(coords1,Symbol.Colour.RED));
		double[] coords2 = {60,60};
		list.add(new ColorSymbol(coords2,Symbol.Colour.RED));
		double[] coords3 = {50,70};
		list.add(new ColorSymbol(coords3,Symbol.Colour.RED));
		double[] coords4 = {40,40};
		list.add(new ColorSymbol(coords4,Symbol.Colour.RED));
		double[] coords5 = {60,40};
		list.add(new ColorSymbol(coords5,Symbol.Colour.RED));
		double[] coords6 = {40,50};
		list.add(new ColorSymbol(coords6,Symbol.Colour.RED));
		List<ColorSymbol> sort = sortColourSymbolPolar(list,center);
		//		JOptionPane.showMessageDialog(null,sort.get(0).coordinate[0] + "," + sort.get(0).coordinate[1] + "\n" + 
		//				sort.get(1).coordinate[0] + "," + sort.get(1).coordinate[1] + "\n" + 
		//				sort.get(2).coordinate[0] + "," + sort.get(2).coordinate[1] + "\n" + 
		//				sort.get(3).coordinate[0] + "," + sort.get(3).coordinate[1] + "\n" + 
		//				sort.get(4).coordinate[0] + "," + sort.get(4).coordinate[1] + "\n" + 
		//				sort.get(5).coordinate[0] + "," + sort.get(5).coordinate[1] + "\n");
		//		
		//test SymbolCompare
		List<Symbol> list0 = new LinkedList<>();
		Symbol center0 = new Symbol("RR");
		center0.setX(50);
		center0.setY(50);
		list0.add(center0);
		Symbol s1 = new Symbol("RR");
		s1.setX(60);
		s1.setY(50);
		list0.add(s1);
		Symbol s2 = new Symbol("RR");
		s2.setX(60);
		s2.setY(60);
		list0.add(s2);
		Symbol s3 = new Symbol("RR");
		s3.setX(50);
		s3.setY(70);
		list0.add(s3);
		Symbol s4 = new Symbol("RR");
		s4.setX(40);
		s4.setY(40);
		list0.add(s4);
		Symbol s5 = new Symbol("RR");
		s5.setX(60);
		s5.setY(40);
		list0.add(s5);
		Symbol s6 = new Symbol("RR");
		s6.setX(40);
		s6.setY(50);
		list0.add(s6);
		List<Symbol> sort0 = sortPolar(list0,center0);
		//		JOptionPane.showMessageDialog(null,sort0.get(0).getX() + "," + sort0.get(0).getY() + "\n" + 
		//				sort0.get(1).getX() + "," + sort0.get(1).getY() + "\n" + 
		//				sort0.get(2).getX() + "," + sort0.get(2).getY() + "\n" + 
		//				sort0.get(3).getX() + "," + sort0.get(3).getY() + "\n" + 
		//				sort0.get(4).getX() + "," + sort0.get(4).getY() + "\n" + 
		//				sort0.get(5).getX() + "," + sort0.get(5).getY() + "\n");


		//rood,rood,rood,wit,wit,rood,groen
		List<ColorSymbol> list1 = new LinkedList<>();
		double[] coord0 = {50,50};
		ColorSymbol center1 = new ColorSymbol(coord0,Symbol.Colour.RED);
		list1.add(center1);
		double[] coord1 = {40,60};
		list1.add(new ColorSymbol(coord1,Symbol.Colour.RED));
		double[] coord2 = {60,60};
		list1.add(new ColorSymbol(coord2,Symbol.Colour.WHITE));
		double[] coord3 = {70,50};
		list1.add(new ColorSymbol(coord3,Symbol.Colour.WHITE));
		double[] coord4 = {60,40};
		list1.add(new ColorSymbol(coord4,Symbol.Colour.RED));
		double[] coord5 = {40,40};
		list1.add(new ColorSymbol(coord5,Symbol.Colour.GREEN));
		double[] coord6 = {30,50};
		list1.add(new ColorSymbol(coord6,Symbol.Colour.RED));
		double[] coord7 = {20,20};
		list1.add(new ColorSymbol(coord7,Symbol.Colour.BLUE));
		double[] coord8 = {80,80};
		list1.add(new ColorSymbol(coord8,Symbol.Colour.RED));
		PureColourLocator locator = new PureColourLocator(new Map("/shapesDemo.csv"));
		double[] loc = locator.locate(list1);
		if(loc == null)
			JOptionPane.showMessageDialog(null,"null");

		if(loc != null)
			JOptionPane.showMessageDialog(null, loc[0] + "," + loc[1] + "|" + loc[2]);	
//		
//		List<ColorSymbol> list2 = new LinkedList<>();
//		double[] coor0 = {50,50};
//		ColorSymbol center2 = new ColorSymbol(coor0,Symbol.Colour.BLUE);
//		list2.add(center2);
//		double[] coor1 = {42,60};
//		list2.add(new ColorSymbol(coor1,Symbol.Colour.YELLOW));
//		double[] coor2 = {58,60};
//		list2.add(new ColorSymbol(coor2,Symbol.Colour.WHITE));
////		double[] coor3 = {66,50};
////		list2.add(new ColorSymbol(coor3,Symbol.Colour.WHITE));
//		//JOptionPane.showMessageDialog(null,locator.euclideanDistance(coor1, coor2) +
//		//		"," + locator.euclideanDistance(coor1, coor2) + "," + locator.euclideanDistance(coor1, coor2));
//		double[] loc0 = locator.locate(list2);
	}
	public Comparator<ColorSymbol> sixBestNeighboursComparator(final ColorSymbol middle){
		return new Comparator<ColorSymbol>() {

			@Override
			public int compare(ColorSymbol o1, ColorSymbol o2) {
				double d1 = euclideanDistance(o1.coordinate, middle.coordinate);
				double d2 = euclideanDistance(o2.coordinate, middle.coordinate);
				if(d1 > d2)
					return 1;
				return -1;
			}
		};
	}
	public PureColourLocator(Map map){
		this.map = map;
	}
	
}
