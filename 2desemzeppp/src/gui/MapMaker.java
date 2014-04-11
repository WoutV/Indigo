package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

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

	private BufferedImage image;
	private BoardLayout boardlayout;

	/**
	 * Get the map with the locations of supplied zeppelins and target.
	 * @param own
	 * 			coordinates of own zeppelin, in cm on the map
	 * 			own[0]: x, own[1]: y, own[2]: alpha
	 * @param enemy
	 * 			coordinates of enemy zeppelin, in cm on the map
	 * 			enemy[0]: x, enemy[1]: y
	 * @param dest
	 * 			coordinates of the target, in cm on the map
	 */
	public ImageIcon getLocations(double[] own, double[] enemy, double[] dest) {
		ColorModel colormodel =  image.getColorModel();
		boolean isAlpha = colormodel.isAlphaPremultiplied();	
		WritableRaster raster = image.copyData(null);
		BufferedImage image2 = new BufferedImage(colormodel,raster,isAlpha,null);

		image2.createGraphics();

		Graphics2D map0 = (Graphics2D) image2.getGraphics();

		//own zeppelin
		if(own != null) {
			int[] ownZepp = boardlayout.getPx(own);
			int[] ownZeppdata = Shapes.getShiftedZeppelinData(ownZepp[0], ownZepp[1]);
			map0.setColor(Color.MAGENTA);
			map0.fillOval(ownZeppdata[0],ownZeppdata[1],ownZeppdata[2],ownZeppdata[3]);
			
			int[] point = Shapes.getShiftedZeppelinOrientationData(ownZepp[0], ownZepp[1], (int) own[2]);
			map0.setColor(Color.BLACK);
			map0.drawLine(ownZepp[0],ownZepp[1],point[0],point[1]);
			map0.drawLine(point[0],point[1],point[2],point[3]);
			map0.drawLine(point[0],point[1],point[4],point[5]);
			
		}

		//enemy zeppelin
		if(enemy != null) {
			int[] enemyZepp = boardlayout.getPx(enemy);
			int[] enemyZeppdata = Shapes.getShiftedZeppelinData(enemyZepp[0], enemyZepp[1]);
			map0.setColor(Color.RED);
			map0.fillOval(enemyZeppdata[0],enemyZeppdata[1],enemyZeppdata[2],enemyZeppdata[3]);
		}

		//target
		if(dest != null) {
			int[] target = boardlayout.getPx(dest);
			int[] targetdata = Shapes.getShiftedTargetData(target[0], target[1]);
			map0.setColor(Color.RED);
			map0.drawRect(targetdata[0],targetdata[1],targetdata[2],targetdata[3]);
		}

		ImageIcon ii = new ImageIcon(image2);

		return ii;
	}

	/**
	 * Creates and gets the map containing the symbols.
	 * No zeppelin or target is added.
	 * @param map
	 */
	public ImageIcon getMap(Map map) {
		image = new BufferedImage(width, width,BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D map0 = (Graphics2D) image.getGraphics();

		//background
		map0.setColor(Color.LIGHT_GRAY);
		map0.fillRect(0, 0, width, width);

		int symbolsOnRow = map.getSymbolsOnRow();
		int lines = map.getRows();
		boardlayout = new BoardLayout(symbolsOnRow,lines);

		map0.setColor(Color.BLACK);

		for(int i = 0; i<lines;i++) {
			int y = boardlayout.getY(i);
			boolean even = i%2 != 0;

			for(int j=0;j<symbolsOnRow-1;j++) {
				map0.drawLine(boardlayout.getX(j, even),y,boardlayout.getX(j+1, even),y);
			}

			if(!even) {
				if(i<lines-1) {
					int nexty = boardlayout.getY(i+1);

					map0.drawLine(boardlayout.getX(0, even),y,boardlayout.getX(0, !even),nexty);
					for(int j=1;j<symbolsOnRow;j++) {
						map0.drawLine(boardlayout.getX(j, even),y,boardlayout.getX(j-1, !even),nexty);
						map0.drawLine(boardlayout.getX(j, even),y,boardlayout.getX(j, !even),nexty);
					}
				}
				if(i>0) {
					int prevy = boardlayout.getY(i-1);

					map0.drawLine(boardlayout.getX(0, even),y,boardlayout.getX(0, !even),prevy);
					for(int j=1;j<symbolsOnRow;j++) {
						map0.drawLine(boardlayout.getX(j, even),y,boardlayout.getX(j-1, !even),prevy);
						map0.drawLine(boardlayout.getX(j, even),y,boardlayout.getX(j, !even),prevy);
					}
				}
			}
		}

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
		
		//tablets
		int tablets = map.getNoOfTablets();
		for(int i=0;i<tablets;i++) {
			double[] tablet = map.getTablet(i+1);
			
			Polygon outer = Shapes.getTabletOuterEdge((int) tablet[0], (int) tablet[1]);
			map0.setColor(Color.GRAY);
			map0.fillPolygon(outer);
			
			Polygon inner = Shapes.getTabletInnerEdge((int) tablet[0], (int) tablet[1]);
			map0.setColor(Color.WHITE);
			map0.fillPolygon(inner);
			
			int[] circledata = Shapes.getShiftedTabletCircleData((int) tablet[0], (int) tablet[1]);
			map0.setColor(Color.BLACK);
			map0.fillOval(circledata[0],circledata[1],circledata[2],circledata[3]);
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
