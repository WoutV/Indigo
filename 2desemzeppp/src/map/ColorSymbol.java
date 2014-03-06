package map;

public class ColorSymbol {
	public Symbol.Colour colour;
	public double[] coordinate = new double[2];
	
	public ColorSymbol(double[] coordinate, Symbol.Colour colour){
		this.colour= colour;
		this.coordinate=coordinate;
	}

}
