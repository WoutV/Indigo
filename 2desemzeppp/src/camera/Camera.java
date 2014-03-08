package camera;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
public abstract class Camera {
	public static void main(String[] args) {
		double time = System.currentTimeMillis();
		ImageIcon image = getImage();
		if(image==null){
			System.out.println("FAILED");
		}
		else{
			System.out.println("Success!");
		}
		System.out.println("Time needed:"+( System.currentTimeMillis()-time));
		
	}

	private static boolean isInUse;

	/**
	 * Takes a low resolution picture and gives its imageicon.
	 * @return
	 */
	public static ImageIcon getImage(){
		BufferedImage image = null;
		try {
			Process p = Runtime.getRuntime().exec("raspistill -t 10 -n -h 800 -w 800 -o image.jpg");
			image = ImageIO.read(p.getInputStream());
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("image from inputstream"+image);
		return new ImageIcon("image.jpg");
	}

}
	