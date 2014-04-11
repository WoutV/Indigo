package map;

import gui.GuiMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Class representing the playing field.
 * 
 * Given a file of objects, a Map can be created.
 *
 * The file should be a comma separated value file.
 * Each value in this file looks as follows:
 * R/W/Y/B/G: colour: red,white,yellow,blue,green
 * H/S/O/R: shape: heart,star,circle,rectangle
 * 
 * Examples:
 * 		0RR: a red rectangle
 * 		0RH: a red heart
 * 
 * The first row is left-aligned. Lines alternate between left- and right-aligned.
 * 
 * Each row should contain the same amount of symbols.
 */
public class Map {
	
	private Symbol[][] map;
	
	private int symbolsOnRow;
	private int height; 
	
	//map size in cm
	private double boardSize = 400;
	
	private double[][] tablets;
	
	/**
	 * Given a filename, creates the corresponding map.
	 * The filename should refer to a file in the resources folder, and should be a
	 * comma separated value file.
	 * The filename should start with "/" and contain the extension.
	 * 
	 * Constraints on the contents of the file can be found in the class documentation.
	 */
	public Map(String filename) {
		try {
			InputStream resource = GuiMain.class.getResourceAsStream(filename);
			InputStreamReader reader = new InputStreamReader(resource);
			BufferedReader read = new BufferedReader(reader);

			readMap(read);
		}
		catch (IOException exc) {
			symbolsOnRow = 0;
			height = 0;
		}
	}

	/**
	 * Given a file, creates the corresponding map.
	 * 
	 * Constraints on the contents of the file can be found in the class documentation.
	 */
	public Map(File file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader read = new BufferedReader(fr);

			readMap(read);
		}
		catch (IOException exc) {
			symbolsOnRow = 0;
			height = 0;
		}
	}
	
	private void readMap(BufferedReader read) throws IOException {
		LinkedList<String> rows = new LinkedList<>();
		LinkedList<String> tablets = new LinkedList<>();
		int row = 0;
		int tablet = 0;
		String currentLine = read.readLine();
		while(currentLine!=null && !currentLine.startsWith("(")) {
			row++;
			rows.add(currentLine);
			currentLine = read.readLine();
		}
		while(currentLine!=null) {
			tablet++;
			tablets.add(currentLine);
			currentLine = read.readLine();
		}
		
		map = new Symbol[row][];
		for(int i=0;i<row;i++) {
			String current = rows.removeFirst();
			String[] thisRow = current.split(",");
			map[i] = new Symbol[thisRow.length];
			for(int j=0;j<thisRow.length;j++) {
				Symbol symbol = new Symbol(thisRow[j].trim());
				map[i][j] = symbol;
			}
		}
		
		//tablets: currently assuming '(x,y),no'
		this.tablets = new double[tablet][];
		for(int i=0;i<tablet;i++) {
			String current = tablets.removeFirst();
			current = current.substring(1,current.length());
			String[] splitted0 = current.split(",");
			int x = Integer.parseInt(splitted0[0]);
			String[] splitted1 = splitted0[1].split("),");
			int y  = Integer.parseInt(splitted1[0]);
			double[] t = {x,y};
			this.tablets[i] = t;
		}
		
		read.close();
		
		symbolsOnRow = map[0].length;
		height = map.length;
		
		//set the x and y coordinates of each symbol
		double distBetweenX = boardSize/(symbolsOnRow-0.5);
		double distBetweenY = 1.0*boardSize/(height-1);
		for(int i = 0;i<height;i++) {
			boolean even = (i%2!=0);
			for(int j = 0;j<symbolsOnRow;j++) {
				double x;
				if(even)
					x = (j+0.5)*distBetweenX;
				else
					x = j*distBetweenX;
				double y = i*distBetweenY;
				map[i][j].setX(x);
				map[i][j].setY(y);
			}
		}
	}
	
	/**
	 * How many symbols on one row?
	 */
	public int getSymbolsOnRow() {
		return symbolsOnRow;
	}
	
	/**
	 * How many lines?
	 */
	public int getRows() {
		return height;
	}
	
	/**
	 * Get the Symbol at "no" at the specified line.
	 * @param no
	 * 		Index on line. range 0-(symbolsOnRow-1)
	 * @param line
	 * 		Line index. range 0-(height-1)
	 */
	public Symbol getSymbol(int no, int line) {
		if(no < 0 || no >= symbolsOnRow || line < 0 || line >= height)
			return null;
		return map[line][no];
	}
	
	/**
	 * Get the amount of tablets on the map.
	 */
	public int getNoOfTablets() {
		return tablets.length;
	}
	
	/**
	 * Get the cooordinates of the tablet at index 'no'.
	 * 'no' should be between 1 and the amount of tablets.
	 * r[0]: x, r[1]: y
	 */
	public double[] getTablet(int no) {
		if(no<0 || no>tablets.length) {
			return null;
		}
		return tablets[no-1];
	}
}
