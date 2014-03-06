package map;

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

}
