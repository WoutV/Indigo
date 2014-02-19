package gui;

import java.awt.Color;
import java.awt.Graphics2D;
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
		map0.setColor(Color.BLACK);

		//draw some other shapes
		map0.fillRect(450, 0, width, 90);
		
		map0.fillOval(20, 20, 60, 60);

		ImageIcon ii = new ImageIcon(image);
		return ii;
	}
}

