package map;

import java.util.ArrayList;
import java.util.HashMap;

import map.Symbol.Colour;

public class PureCollorLocator {
	
	public PureCollorLocator(ArrayList<ColorSymbol> colors){
		double[] totalCenter = new double[2];
		totalCenter = calculateCenter(colors);
		ColorSymbol middle= nearestSymbol(colors, totalCenter);
		double[] distanceNextBestSymbol = new double[2];
		ArrayList<ColorSymbol> copy = (ArrayList<ColorSymbol>) colors.clone();
		copy.remove(middle);
		ColorSymbol nextBestSymbol = nearestSymbol(copy,middle.coordinate);

		distanceNextBestSymbol= nextBestSymbol.coordinate;
		
	}
	
	
	public ColorSymbol nearestSymbol(ArrayList<ColorSymbol> colors, double[] center){
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
	
	public double[] calculateCenter(ArrayList<ColorSymbol> colors){
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

}
