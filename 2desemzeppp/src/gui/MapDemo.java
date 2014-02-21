package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

/**
 * Given a file of objects, creates a map displaying the objects in a similar way they are positioned
 * on the playing field.
 *
 */
public class MapDemo {

	public static int width = 495;

	public static ImageIcon getMap() {
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
		
		int[] circledata = getShiftedCircleData(380, 380);
		map0.fillOval(circledata[0],circledata[1],circledata[2],circledata[3]);
		
		Polygon triangle = Shapes.getShiftedTriangle(210, 210);
		map0.setColor(Color.YELLOW);
		map0.fillPolygon(triangle);
		
		Polygon square = Shapes.getShiftedSquare(440, 440);
		map0.fillPolygon(square);
		
		ImageIcon ii = new ImageIcon(image);

		return ii;
	}
	
	/**
	 * Given the coordinates of the center, gives the data used for drawing the circle with this center.
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static int[] getShiftedCircleData(int x, int y) {
		int[] a = new int[4];
		a[0] = x - 8;
		a[1] = y - 8;
		a[2] = 16;
		a[3] = 16;
		return a;
	}
}

