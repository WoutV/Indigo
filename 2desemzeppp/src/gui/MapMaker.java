package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;

/**
 * Given a file of objects, creates a map displaying the objects in a similar way they are positioned
 * on the playing field.
 *
 * The file should be a comma separated value file.
 * Each value in this file looks as follows:
 * 0/1: is this line odd (0) (aligned to the left) or even (1) (aligned to the right)
 * R/W/Y/B/G: colour: red,white,yellow,blue,green
 * H/S/C/R: shape: heart,star,circle,rectangle
 * 
 * Examples:
 * 		0RR: a red rectangle
 * 		0RH: a red heart
 * 
 * A maximum of 120 figures is allowed on the map: 10 on each line, 12 lines
 * In case fewer figures are supplied, part of the map is not filled.
 * In case more figures are supplied, only the first 120 are displayed.
 */
public class MapMaker {

	private static MapMaker instance = new MapMaker();
	
	private int numberOnRow;
	private int height; 

	public static MapMaker getInstance() {
		return instance;
	}

	//size of the map (in px)
	private int width = 495;

	private BoardLayout boardlayout;

	/**
	 * Given a filename, gives the ImageIcon with the corresponding map.
	 * The filename should refer to a file in the resources folder, and should be a
	 * comma separated value file.
	 * The filename should start with "/" and contain the extension.
	 * @param file
	 */
	public ImageIcon getMap(String filename) {
		try {
			InputStream resource = GuiMain.class.getResourceAsStream(filename);
			InputStreamReader reader = new InputStreamReader(resource);
			BufferedReader read = new BufferedReader(reader);
			String list = "";
			String newString = read.readLine();
			while(newString!=null) {
				list += newString+",";
				height++;
				newString = read.readLine();
			}
			read.close();
			System.out.println(list);
			String[] symbols = list.split(",");
			System.out.println(symbols.length);
			numberOnRow = (symbols.length)/height;
			boardlayout = new BoardLayout(numberOnRow, height);
			System.out.println(height);
			System.out.println(numberOnRow);
			return makeMap(symbols);
		}
		catch (IOException exc) {
			return null;
		}
	}

	/**
	 * Given a file, gives the ImageIcon with the corresponding map.
	 * @param file
	 */
	public ImageIcon getMap(File file) {
		try {
			FileReader fr = new FileReader(file);
			BufferedReader read = new BufferedReader(fr);
			String list = read.readLine();
			read.close();
			String[] symbols = list.split(",");
			return makeMap(symbols);
		}
		catch (IOException exc) {
			return null;
		}


	}

	private ImageIcon makeMap(String[] symbols) {
		BufferedImage image = new BufferedImage(width, width,BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D map0 = (Graphics2D) image.getGraphics();

		//background
		map0.setColor(Color.LIGHT_GRAY);
		map0.fillRect(0, 0, width, width);

		int length = symbols.length;
		boolean even = false;
		for(int i = 0; i<length;i++) {
			String currentsymbol = symbols[i].trim();

			//colour
			char colour = currentsymbol.charAt(0);
			switch(colour) {
			case 'Y':
				map0.setColor(Color.YELLOW);
				break;
			case 'W':
				map0.setColor(Color.WHITE);
				break;
			case 'B':
				map0.setColor(Color.BLUE);
				break;
			case 'R':
				map0.setColor(Color.RED);
				break;
			case 'G':
				map0.setColor(Color.GREEN);
				break;
			case 'X':
				map0.setColor(Color.LIGHT_GRAY);
			}

			//shape
			char shape = currentsymbol.charAt(1);
			int no = i%numberOnRow;
			int line = i/numberOnRow;
			even = (line%2!=0);
			int x = boardlayout.getX(no, even);
			int y = boardlayout.getY(line);
			switch(shape) {
			case 'H':
				Polygon heart = Shapes.getShiftedHeart(x, y);
				map0.fillPolygon(heart);
				break;
			case 'S':
				Polygon star = Shapes.getShiftedStar(x, y);
				map0.fillPolygon(star);
				break;
			case 'O':
				int[] circledata = Shapes.getShiftedCircleData(x, y);
				map0.fillOval(circledata[0],circledata[1],circledata[2],circledata[3]);
				break;
			case 'R':
				Polygon rclandscape = Shapes.getShiftedLandscapeRectangle(x, y);
				map0.fillPolygon(rclandscape);
				break;
			}
		}

		ImageIcon ii = new ImageIcon(image);

		return ii;
	}

	/**
	 * Gives a map containing some shapes.
	 */
	public ImageIcon getMapDemo() {
		BufferedImage image = new BufferedImage(width, width,BufferedImage.TYPE_INT_RGB);

		image.createGraphics();

		Graphics2D map0 = (Graphics2D) image.getGraphics();
		map0.setColor(Color.WHITE);
		map0.fillRect(0, 0, width, width);

		Polygon heart = Shapes.getShiftedHeart(10, 10);
		map0.setColor(Color.RED);
		map0.fillPolygon(heart);

		Polygon rclandscape = Shapes.getShiftedLandscapeRectangle(50, 50);
		map0.setColor(Color.GREEN);
		map0.fillPolygon(rclandscape);

		Polygon rcportrait = Shapes.getShiftedPortraitRectangle(150, 140);
		map0.setColor(Color.BLUE);
		map0.fillPolygon(rcportrait);

		Polygon star = Shapes.getShiftedStar(90, 80);
		map0.fillPolygon(star);

		int[] circledata = Shapes.getShiftedCircleData(380, 380);
		map0.fillOval(circledata[0],circledata[1],circledata[2],circledata[3]);

		Polygon triangle = Shapes.getShiftedTriangle(210, 210);
		map0.setColor(Color.YELLOW);
		map0.fillPolygon(triangle);

		Polygon square = Shapes.getShiftedSquare(440, 440);
		map0.fillPolygon(square);

		ImageIcon ii = new ImageIcon(image);

		return ii;
	}
}
