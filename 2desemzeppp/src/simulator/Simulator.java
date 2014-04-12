package simulator;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import map.Map;
import map.Symbol;

/**
 * The simulator.
 * Acts like a server: takes the speed of motors as input, gives a set of symbols as output.
 * The simulator uses a default XY-plane. (x points right, y points up)
 * The input is the speed of the x or y motor (motor 1 and 2), assuming the default XY-plane.
 * Symbols are sent to the map as if seen by the zeppelin, and sent using the "indigo.private.symbollist"
 * key.
 */
public class Simulator {
	private double xConstant, yConstant;
	
	private SimConnection simconn;
	private SimConnNoRabbit simconn2;

	/**
	 * Set this Sim to use a SimConnection using a Rabbit server.
	 * @param sims
	 */
	public void setSimConn(SimConnection sims){
		simconn = sims;
	}
	
	/**
	 * Set this Sim to use a connection not using a Rabbit server.
	 * @param conn
	 */
	public void setSimConnNoRabbit(SimConnNoRabbit conn) {
		simconn2 = conn;
	}
	/**
	 * The time between sending symbols (in ms).
	 */
	private long wait = 500;
	
	private boolean wind;
	
	private Map map;

	//boardsize in cm
	private int boardSize = 400;

	//default: 0 = right, 180 = left
	private int alpha;
	
	//all in cm!!
	private double xPos=200,yPos=200;
	//speed in the default xy-plane.
	private double xSpeed,ySpeed,xAccel,yAccel;
	
	private Random random = new Random();

	/**
	 * Creates a new Simulator using the given map.
	 */
	public Simulator(Map map){
		init();
		this.map = map;
	}

	private void init() {
		//2 cm/s: max velocity
		//t = 0.5 s
		//vnew = 2 cm/s + 0.5*a cm/s
		//a = max 1 cm/s²?
		//motor power 100 ==> 1 cm/s² = 0,01 m/s²
		//a = power/10000
		xConstant = 1000;
		yConstant = 1000;
	}

	/**
	 * Set wind (unreliability) on.
	 */
	public void windOn() {
		wind = true;
	}

	/**
	 * Set wind (unreliability) off.
	 */
	public void windOff() {
		wind = false;
	}
	
	/**
	 * Given the pwm value and a motor, makes the zeppelin move.
	 * @param motor
	 * 			1: x
	 * 			2: y
	 * @param pwm
	 */
	public void handleInput(int motor, int pwm) {
		//System.out.println(motor);
		if(motor == 1)
			handleXInput(pwm);
		if(motor == 2) 
			handleYInput(pwm);
	}

	/**
	 * Given the pwm value the x-motor is supposed to have, makes the zeppelin move.
	 * @param pwm
	 */
	private void handleXInput(int pwm) {
		//X(zepp) -> motion in X and Y!!
		//velocity(xzepp)
		double v = xSpeed*(Math.cos(alpha*1.0/180*Math.PI)) + ySpeed*(Math.cos(rangeFit(alpha-90)*1.0/180*Math.PI));
		//acceleration
		double axzepp = pwm/xConstant;
		double axnew = 1.0/4*xAccel + (3.0/4*Math.cos(alpha*1.0/180*Math.PI)*axzepp);
		xAccel = axnew;
		xPos = xPos + xSpeed*wait/1000.0 + axnew*(wait/1000.0)*(wait/1000.0)/2;
		xSpeed = xSpeed + axnew*wait/1000.0;
		double aynew = 1.0/4*yAccel + (3.0/4*Math.cos(rangeFit(alpha-90)*1.0/180*Math.PI)*axzepp);
		yAccel = aynew;
		yPos = yPos + ySpeed*wait/1000.0 + aynew*(wait/1000.0)*(wait/1000.0)/2;
		ySpeed = ySpeed + aynew*wait/1000.0;
		System.out.println("X: pwm: " + pwm + ", anew:" + axzepp + " ,axnew: " + axnew + ",aynew: " + aynew);
		System.out.println("Loc: ( " + xPos + "," + yPos + " )");
		if(wind)
			wind();
		sendSymbols();
	}

	/**
	 * Given the pwm value the y-motor is supposed to have, makes the zeppelin move.
	 * @param pwm
	 */
	private void handleYInput(int pwm) {
		//velocity
		double v = xSpeed*(Math.sin(rangeFit(alpha-180)*1.0/180*Math.PI)) + ySpeed*(Math.sin(rangeFit(alpha+90)*1.0/180*Math.PI));
		//Y(zepp) -> motion in X and Y!!
		//acceleration
		double ayzepp = pwm/yConstant;
		double axnew = 1.0/4*xAccel + (3.0/4*Math.sin(rangeFit(alpha-180)*1.0/180*Math.PI)*ayzepp);
		xAccel = axnew;
		xPos = xPos + xSpeed*wait/1000.0 + axnew*(wait/1000.0)*(wait/1000.0)/2;
		xSpeed = xSpeed + axnew*wait/1000.0;
		double aynew = 1.0/4*yAccel + (3.0/4*Math.cos(rangeFit(alpha)*1.0/180*Math.PI)*ayzepp);
		yAccel = aynew;
		yPos = yPos + ySpeed*wait/1000.0 + aynew*(wait/1000.0)*(wait/1000.0)/2;
		ySpeed = ySpeed + aynew*wait/1000.0;
		System.out.println("Y: pwm: " + pwm + ", anew:" + ayzepp + " ,axnew: " + axnew + ",aynew: " + aynew);
		//System.out.println("Loc: ( " + xPos + "," + yPos + " )");
		if(wind)
			wind();
		//sendSymbols();
	}
	
	/**
	 * Creates a random wind effect: between -2 cm and 2 cm, alpha between -10 and 10.
	 */
	private void wind() {
		//-2cm -> 2cm
		int xExtra = random.nextInt(400);
		xExtra -= 200;
		int yExtra = random.nextInt(400);
		yExtra -= 200;
		xPos = xPos + xExtra/100.0;
		yPos = yPos + yExtra/100.0;
		int alphaExtra = random.nextInt(20);
		alphaExtra -= 10;
		alpha = alpha + alphaExtra;
	}

	/**
	 * Normalises alpha: gets the equivalent in the ]-180,180] range.
	 * @param alpha
	 */
	private int rangeFit(int alpha) {
		while(alpha <= - 180) {
			alpha = alpha + 360;
		}
		while(alpha > 180) {
			alpha = alpha - 360;
		}
		return alpha;
	}
	
	/**
	 * Given the x- and y- index of a center symbol in the map,
	 * retrieves the list of all neighbours on the map.
	 * 
	 * The neighbour to the right of the center is added first.
	 * Subsequent neighbours are added in clockwise order.
	 * 
	 * @param x
	 * 			x-index in the map. Range 0-(symbPerRow-1)
	 * @param y
	 * 			y-index in the map. Range 0-(rows-1)
	 */
	private List<Symbol> getNeighbourSymbols(int x, int y) {
		List<Symbol> neighbours = new LinkedList<>();
		
		int rightX = x + 1;
		int rightY = y;
		Symbol right = map.getSymbol(rightX, rightY);
		//could be null: use blank symbol instead
		if(right == null)
			right = new Symbol("XX");
		right = right.copy();
		neighbours.add(right);
		
		int rightbottomX = x + 1; //even row
		if(y%2 == 0)
			rightbottomX = x; //odd row
		int rightbottomY = y + 1;
		Symbol rightbottom = map.getSymbol(rightbottomX, rightbottomY);
		//could be null: use blank symbol instead
		if(rightbottom == null)
			rightbottom = new Symbol("XX");
		rightbottom = rightbottom.copy();
		neighbours.add(rightbottom);
		
		int leftbottomX = x; //even row
		if(y%2 == 0)
			leftbottomX = x - 1; //odd row
		int leftbottomY = y + 1;
		Symbol leftbottom = map.getSymbol(leftbottomX, leftbottomY);
		//could be null: use blank symbol instead
		if(leftbottom == null)
			leftbottom = new Symbol("XX");
		leftbottom = leftbottom.copy();
		neighbours.add(leftbottom);
		
		int leftX = x - 1;
		int leftY = y;
		Symbol left = map.getSymbol(leftX, leftY);
		//could be null: use blank symbol instead
		if(left == null)
			left = new Symbol("XX");
		left = left.copy();
		neighbours.add(left);
		
		int leftupX = x; //even row
		if(y%2 == 0)
			leftupX = x - 1; //odd row
		int leftupY = y - 1;
		Symbol leftup = map.getSymbol(leftupX, leftupY);
		//could be null: use blank symbol instead
		if(leftup == null)
			leftup = new Symbol("XX");
		leftup = leftup.copy();
		neighbours.add(leftup);
		
		int rightupX = x + 1; //even row
		if(y%2 == 0)
			rightupX = x; //odd row
		int rightupY = y - 1;
		Symbol rightup = map.getSymbol(rightupX, rightupY);
		//could be null: use blank symbol instead
		if(rightup == null)
			rightup = new Symbol("XX");
		rightup = rightup.copy();
		neighbours.add(rightup);
		
		return neighbours;
	}

	/**
	 * Using the current location of the zeppelin in the Sim, sends the 3 symbols
	 * which would correspond to the symbols seen in an image taken from the zeppelin
	 * at the current location.
	 */
	private void sendSymbols() {
		try {
			Thread.sleep(wait);
		}
		catch (Exception exc) {
			
		}
		int symbPerRow = map.getSymbolsOnRow();
		int rows = map.getRows();
		int nearestSymbolX = (int) Math.round(xPos/boardSize*symbPerRow);
		int nearestSymbolY = (int) Math.round(yPos/boardSize*rows);
		if(nearestSymbolX == symbPerRow)
			nearestSymbolX--;
		nearestSymbolY = rows - nearestSymbolY; //because map's y plane points other way
		if(nearestSymbolY == rows)
			nearestSymbolY--;
		Symbol nearestSymbol = map.getSymbol(nearestSymbolX, nearestSymbolY);
		
		List<Symbol> neighbours = getNeighbourSymbols(nearestSymbolX, nearestSymbolY);
		
		Symbol symb1,symb2;
		symb1 = neighbours.get(2);
		symb2 = neighbours.get(3); //alpha: 150 -> 90
		if(alpha <= 90 && alpha > 30) {
			symb1 = neighbours.get(3);
			symb2 = neighbours.get(4);
		}
		if(alpha <= 30 && alpha > -30) {
			symb1 = neighbours.get(4);
			symb2 = neighbours.get(5);
		}
		if(alpha <= -30 && alpha > -90) {
			symb1 = neighbours.get(5);
			symb2 = neighbours.get(0);
		}
		if(alpha <= -90 && alpha > -150) {
			symb1 = neighbours.get(0);
			symb2 = neighbours.get(1);
		}
		if(alpha <= -150 || alpha > 150) {
			symb1 = neighbours.get(1);
			symb2 = neighbours.get(2);
		}
		
		//set some nice coordinates
		nearestSymbol = nearestSymbol.copy();
		nearestSymbol.setX(50);
		nearestSymbol.setY(50);
		symb1.setX(40);
		symb1.setY(65);
		symb2.setX(60);
		symb2.setY(65);
		
		List<Symbol> list = new LinkedList<Symbol>();
		list.add(nearestSymbol);
		list.add(symb1);
		list.add(symb2);
			
		//System.out.println(nearestSymbol.getColour() + " " + nearestSymbol.getShape() + 
		//		"," + symb1.getColour() + " " + symb1.getShape() + "," + symb2.getColour() + " " + symb2.getShape());
		
		String key = "indigo.private.symbollist";
		String info = SymbolListToString(list);
		if(simconn != null)
			simconn.sendTransfer(info, key);
		if(simconn2 != null)
			simconn2.sendTransfer(info, key);
	}
	
	/**
	 * Transforms a list of Symbols into a String to be sent using the
	 * "indigo.private.symbollist" key.
	 * @param list
	 * 			List of symbols
	 */
	public static String SymbolListToString(List<Symbol> list) {
		String s = "";
		for(Symbol symbol : list) {
			if(symbol.getColour() == Symbol.Colour.BLUE)
				s = s + "B";
			if(symbol.getColour() == Symbol.Colour.GREEN)
				s = s + "G";
			if(symbol.getColour() == Symbol.Colour.RED)
				s = s + "R";
			if(symbol.getColour() == Symbol.Colour.WHITE)
				s = s + "W";
			if(symbol.getColour() == Symbol.Colour.YELLOW)
				s = s + "Y";
			if(symbol.getColour() == Symbol.Colour.BLANK)
				s = s + "X";
			
			s = s + ";";
			if(symbol.getShape() == Symbol.Shape.CIRCLE)
				s = s + "C";
			if(symbol.getShape() == Symbol.Shape.STAR)
				s = s + "S";
			if(symbol.getShape() == Symbol.Shape.HEART)
				s = s + "H";
			if(symbol.getShape() == Symbol.Shape.RECTANGLE)
				s = s + "R";
			if(symbol.getShape() == Symbol.Shape.EMPTY)
				s = s + "X";
			
			s = s + ";";
			
			s = s + symbol.getX();
			s = s + ";";
			s = s + symbol.getY();
			s = s + "@";
			
		}
		s = s.substring(0,s.length()-1);
		return s;
	}
	
	/**
	 * Transforms a String into a list of Symbols. This String is usually received
	 * from a "indigo.private.symbollist" message from the Sim.
	 * @param s
	 * @return
	 */
	public static List<Symbol> StringToSymbolList(String s) {
		List<Symbol> symbols = new LinkedList<>();
		
		String[] s1 = s.split("@");
		//System.out.println(s);
		for(String symb : s1) {
			String[] s2 = symb.split(";");
			String colourshape = s2[0] + s2[1];
			Symbol symbol = new Symbol(colourshape);
			symbol.setX(Double.parseDouble(s2[2]));
			symbol.setY(Double.parseDouble(s2[3]));
			symbols.add(symbol);
		}
		
		return symbols;
	}
}
