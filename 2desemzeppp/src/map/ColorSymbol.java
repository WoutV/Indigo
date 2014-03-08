package map;

import java.util.Comparator;

/**
 * Class for representing ColorSymbols, used for the PureColourLocator,
 * and hence for navigating using only colours.
 */
public class ColorSymbol {
	public Symbol.Colour colour;
	public double[] coordinate = new double[2];
	
	public ColorSymbol(double[] coordinate, Symbol.Colour colour){
		this.colour= colour;
		this.coordinate=coordinate;
	}
	
	/**
	 * Retrieves a comparator used for angular sort.
	 */
	public static Comparator<ColorSymbol> getAngularComparator() {
		return new Comparator<ColorSymbol>() {

			@Override
			public int compare(ColorSymbol s1, ColorSymbol s2) {
				if(s1 == null)
					return -1;
				if(s2 == null)
					return 1;
				if(s1.coordinate[1]==0 && s1.coordinate[0]>0)
					return -1;
				if(s2.coordinate[1]==0 && s2.coordinate[0]>0)
					return 1;
				if(s1.coordinate[1]>0 && s2.coordinate[1]<0)
					return 1;
				if(s2.coordinate[1]>0 && s1.coordinate[1]<0)
					return -1;
				if(s1.coordinate[0]*s2.coordinate[1]-s1.coordinate[1]*s2.coordinate[0]>0)
					return 1;
				return -1;
			}
			
		};
	}
	
	/**
	 * Get a copy of this ColourSymbol.
	 */
	public ColorSymbol copy() {
		double[] newcoords = new double[2];
		newcoords[0] = coordinate[0];
		newcoords[1] = coordinate[1];
		return new ColorSymbol(newcoords,colour);
	}

}
