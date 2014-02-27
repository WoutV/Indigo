package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import map.*;

import javax.swing.ImageIcon;

/**
 * Given a Map, creates a map displaying the objects in a similar way they are positioned
 * on the playing field.
 * 
 * When more than 15 symbols are on one row, or when there are more than 15 rows, no decent displaying
 * of the map is assured!!
 */
public class MapMaker {

	private static MapMaker instance = new MapMaker();

	public static MapMaker getInstance() {
		return instance;
	}

	//size of the map (in px)
	private int width = 495;

	private BoardLayout boardlayout;

	public ImageIcon getMap(Map map) {
		BufferedImage image = new BufferedImage(width, width,BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D map0 = (Graphics2D) image.getGraphics();

		//background
		map0.setColor(Color.LIGHT_GRAY);
		map0.fillRect(0, 0, width, width);

		int symbolsOnRow = map.getSymbolsOnRow();
		int lines = map.getRows();
		boardlayout = new BoardLayout(symbolsOnRow,lines);
		for(int i = 0; i<lines;i++) {
			for(int j=0;j<symbolsOnRow;j++) {
			Symbol currentSymbol = map.getSymbol(j, i);

			//colour
			Symbol.Colour colour = currentSymbol.getColour();
			if(colour == Symbol.Colour.YELLOW)
				map0.setColor(Color.YELLOW);
			if(colour == Symbol.Colour.WHITE)
				map0.setColor(Color.WHITE);
			if(colour == Symbol.Colour.BLUE)
				map0.setColor(Color.BLUE);
			if(colour == Symbol.Colour.RED)
				map0.setColor(Color.RED);
			if(colour == Symbol.Colour.GREEN)
				map0.setColor(Color.GREEN);

			//shape
			Symbol.Shape shape = currentSymbol.getShape();
			boolean even = (i%2!=0);
			int x = boardlayout.getX(j, even);
			int y = boardlayout.getY(i);
			if(shape == Symbol.Shape.HEART) {
				Polygon heart = Shapes.getShiftedHeart(x, y);
				map0.fillPolygon(heart);
			}
			if(shape == Symbol.Shape.STAR) {
				Polygon star = Shapes.getShiftedStar(x, y);
				map0.fillPolygon(star);
			}
			if(shape == Symbol.Shape.CIRCLE) {
				int[] circledata = Shapes.getShiftedCircleData(x, y);
				map0.fillOval(circledata[0],circledata[1],circledata[2],circledata[3]);
			}
			if(shape == Symbol.Shape.RECTANGLE) {
				Polygon rclandscape = Shapes.getShiftedLandscapeRectangle(x, y);
				map0.fillPolygon(rclandscape);
			}
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
