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
		if(isInUse){
			return new ImageIcon("image.jpg");
		}
		try
		{

			isInUse=true;
			Process p = Runtime.getRuntime().exec("raspistill -t 0 -n -h 150 -w 150 -o image.jpg");
			//image = ImageIO.read(p.getInputStream());
			p.waitFor();
			isInUse=false;
		}
		catch (Exception ieo)
		{
			ieo.printStackTrace();
		}
		return new ImageIcon("image.jpg");
	}

}
	