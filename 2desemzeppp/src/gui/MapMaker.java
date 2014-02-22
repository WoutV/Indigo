package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Given a file of objects, creates a map displaying the objects in a similar way they are positioned
 * on the playing field.
 *
 */
public class MapMaker {

	private static MapMaker instance = new MapMaker();

	public static MapMaker getInstance() {
		return instance;
	}

	//size of the map (in px)
	private int width = 495;

	private BoardLayout boardlayout = BoardLayout.getInstance();

	public ImageIcon getMap(String filename) {
		try {
			InputStream resource = GuiMain.class.getResourceAsStream(filename);
			InputStreamReader reader = new InputStreamReader(resource);
			BufferedReader read = new BufferedReader(reader);
			String list = read.readLine();
			read.close();
			String[] symbols = list.split(";");
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
			String[] symbols = list.split(";");

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
		if(symbols.length > 120) 
			length = 120;
		for(int i = 0; i<length;i++) {
			String currentsymbol = symbols[i];

			//odd or even line
			boolean even = true;
			if(currentsymbol.charAt(0) == '0')
				even = false;

			//colour
			char colour = currentsymbol.charAt(1);
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
			}

			//shape
			char shape = currentsymbol.charAt(2);
			int no = i%10;
			int line = i/10;
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
			case 'C':
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
