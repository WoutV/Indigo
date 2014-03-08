package map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import map.Symbol.Colour;

/**
 * A class for navigating using ONLY the colour of symbols, not the shape.
 * At this moment, the class assumes an image taken using the camera with
 * the UPPER part of the image AIMED AT THE FRONT (this would correspond to decreasing y-value
 * in the grid).
 * POSSIBLY THIS NEEDS TO BE CHANGED (90° clockwise or counterclockwise)
 */
public class PureColourLocator {
	
	public PureColourLocator(List<ColorSymbol> colors){
		double[] totalCenter = new double[2];
		totalCenter = calculateCenter(colors);
		ColorSymbol middle = nearestSymbol(colors, totalCenter);
		colors.remove(middle);
		ColorSymbol nextBestSymbol = nearestSymbol(colors,middle.coordinate);
		
		//try to get the 6 symbols surrounding the middle symbol
		//technically, all of these are at the same distance
		//using a margin of 1.2 here
		double [] mid = middle.coordinate;
		double [] nearestToMid= nextBestSymbol.coordinate;
		double dist = euclideanDistance(mid, nearestToMid);
		List<ColorSymbol> neighbours = filter(colors,1.2*dist,mid);
		if(neighbours.size() == 6) {
			//TODO: all 6 neighbours are known
			//sort them (use tangent????)
			//find them in grid
		}
		else {
			//select a triangle: 2 symbols who are at about the same distance
			//from mid and from eachother
		}
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
	public double[] find6(List<ColorSymbol> neighbours,ColorSymbol mid,Map map) {
		//for now, linear search
		//might use priority queue instead
		int symbolsPerRow = map.getSymbolsOnRow();
		int lines = map.getRows();
		//odd lines (index even) => left aligned => i,i-1
		//even lines (index odd) => right aligned => i,i+1
		for(int i=0;i<lines;i++) {
			for(int j=0;j<symbolsPerRow;j++) {
				if(mid.colour==map.getSymbol(i, j).getColour()) {
					//potential mid
					Symbol leftup,rightup,leftdown,rightdown;
					if(i%2==0) {
						leftup = map.getSymbol(j-1, i-1);
						rightup = map.getSymbol(j, i-1);
						leftdown = map.getSymbol(j-1, i+1);
						rightdown = map.getSymbol(j-1, i+1);
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
					symbols.add(leftup);symbols.add(rightup);symbols.add(right);
					symbols.add(rightdown);symbols.add(leftdown);symbols.add(left);
					for(Symbol s:symbols) {
						if(s==null)
							break;
					}
					//all 6 surrounding exist
					//now match them
					for(int k=0;i<6;i++) {
						if(match(neighbours,symbols,k)) {
							//coordinates of zeppelin
							double[] coordinates = map.getSymbol(i, j).getCoordinates();
							double alpha = k*60; .... //depends on camera
						}
					}
				}
			}
		}
	}
	
	/**
	 * Checks whether the colors in list1, starting from index i, match the colours in list2.
	 * @param list1
	 * @param list2
	 * @param i
	 */
	private boolean match(List<ColorSymbol> list1, List<Symbol> list2, int i) {
		return (list1.get(i).colour == list2.get(0).getColour() && list1.get((i+1)%6).colour == list2.get(0).getColour()
				&& list1.get((i+2)%6).colour == list2.get(0).getColour() && list1.get((i+3)%6).colour == list2.get(0).getColour()
				&& list1.get((i+4)%6).colour == list2.get(0).getColour() && list1.get((i+5)%6).colour == list2.get(0).getColour());
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
		JOptionPane.showMessageDialog(null,sort0.get(0).getX() + "," + sort0.get(0).getY() + "\n" + 
				sort0.get(1).getX() + "," + sort0.get(1).getY() + "\n" + 
				sort0.get(2).getX() + "," + sort0.get(2).getY() + "\n" + 
				sort0.get(3).getX() + "," + sort0.get(3).getY() + "\n" + 
				sort0.get(4).getX() + "," + sort0.get(4).getY() + "\n" + 
				sort0.get(5).getX() + "," + sort0.get(5).getY() + "\n");
	}

}
