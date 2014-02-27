package map;

/**
 * Class representing a Symbol on the map.
 * Symbol is defined by a colour and a shape (see enums).
 * Symbol is created by a string of length 2, which looks as follows:
 * R/W/Y/B/G: colour: red,white,yellow,blue,green
 * H/S/O/R: shape: heart,star,circle,rectangle
 * 
 * Examples:
 * 		0RR: a red rectangle
 * 		0RH: a red heart
 */
public class Symbol {
	
	public enum Colour {
		WHITE, YELLOW, RED, GREEN, BLUE, BLANK
	}
	
	public enum Shape {
		CIRCLE,STAR,HEART,RECTANGLE,EMPTY
	}
	
	private Colour colour;
	private Shape shape;
	
	/**
	 * Creates a Symbol based on a string of length 2.
	 * Constraints on the contents of this string can be found in the class documentation.
	 */
	public Symbol(String string) {
		char colour = string.charAt(0);
		char shape = string.charAt(1);
		switch(colour) {
		case 'W':
			this.colour = Colour.WHITE;
			break;
		case 'Y':
			this.colour = Colour.YELLOW;
			break;
		case 'R':
			this.colour = Colour.RED;
			break;
		case 'G':
			this.colour = Colour.GREEN;
			break;
		case 'B':
			this.colour = Colour.BLUE;
			break;
		case 'X':
			this.colour = Colour.BLANK;
			break;
		}
		switch(shape) {
		case 'O':
			this.shape = Shape.CIRCLE;
			break;
		case 'S':
			this.shape = Shape.STAR;
			break;
		case 'H':
			this.shape = Shape.HEART;
			break;
		case 'R':
			this.shape = Shape.RECTANGLE;
			break;
		case 'X':
			this.shape = Shape.EMPTY;
			break;
		}
	}
	
	public Shape getShape() {
		return shape;
	}

	public Colour getColour() {
		return colour;
	}
}
