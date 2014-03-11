package camera;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;

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
			System.out.println("Give the image height:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int height= Integer.parseInt(br.readLine());
			System.out.println("Give the  image width:");
			int width= Integer.parseInt(br.readLine());
			System.out.println("Give the  wait time:");
			int time= Integer.parseInt(br.readLine());
			Process p = Runtime.getRuntime().exec("raspistill -t "+time+" -n -h "+height+" -w "+width+" -o image.jpg");
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
	