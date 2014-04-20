package ImageProcessing;

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
		CIRCLE,STAR,HEART,RECTANGLE,EMPTY,UNRECOGNISED
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
		case 'C':
			
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
		if(this.shape == null)
			System.out.println(string);
	}
	
	public Symbol(Colour colour,Shape shape) {
		this.colour = colour;
		this.shape = shape;
	}
	
	public Symbol(Colour colour,Shape shape, int timestamp, double x , double y) {
		this.colour = colour;
		this.shape = shape;
		setX(x);setY(y);setTimestamp(timestamp);
	}
	
	public Symbol(String string, int timestamp, double x , double y) {
		setX(x);setY(y);setTimestamp(timestamp);
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
		case 'C':
			
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
		if(this.shape == null)
			System.out.println(string);
	}
	
	private double x,y;
	
	private int timestamp;
	public int getTimestamp(){
		return timestamp;
	}
	public void setTimestamp(int timestamp){
		this.timestamp=timestamp;
	}
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Shape getShape() {
		return shape;
	}

	public Colour getColour() {
		return colour;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Symbol){
			Symbol os = (Symbol) o;
			if(os.getShape()==this.getShape() && os.getColour()==this.getColour() && 
					os.getX() == x && os.getY() == y)
				return true;
			return false;
		}
		return false;
	}
	
	/**
	 * Get a copy of this Symbol.
	 * @return
	 */
	public Symbol copy() {
		Symbol s = new Symbol(colour,shape);
		s.x = x;
		s.y = y;
		return s;
	}
	
	public boolean colourMatch(Symbol other) {
		return this.colour == other.colour;
	}
	
	public boolean shapeMatch(Symbol other) {
		return this.shape == other.shape || this.shape == Shape.UNRECOGNISED || other.shape == Shape.UNRECOGNISED;
	}
	
	public String toString(){
		String color = getColour().toString().substring(0, 1);
		String shape = getShape().toString().substring(0, 1);
		return color+shape;
	
	
	}
}
