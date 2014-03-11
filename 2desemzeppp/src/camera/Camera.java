package camera;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import transfer.Transfer;

import connection.SenderPi;
public abstract class Camera {
	public static void main(String[] args) throws NumberFormatException, IOException {
		System.out.println("Give the image height:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		 height= Integer.parseInt(br.readLine());
		System.out.println("Give the  image width:");
		 width= Integer.parseInt(br.readLine());
		System.out.println("Give the  wait time:");
		 time= Integer.parseInt(br.readLine());
		double time = System.currentTimeMillis();
		ImageIcon image = getImage();
		System.out.println("Sender Starting");
		SenderPi sender = new SenderPi();
		 System.out.println("Sender Initalized, making new transfer");
		 Transfer transfer = new Transfer();
		 System.out.println("Transfer has been made, setting image");
		 transfer.setImage(image);
		 System.out.println("message set, sending transfer");
		 sender.sendTransfer(transfer);
		 System.out.println("Transfer sent!");
		sender.exit();
		System.out.println("Time needed:"+( System.currentTimeMillis()-time));
		
	}

	private static boolean isInUse;
	private static int height=400,width=600,time=1;
	/**
	 * Takes a low resolution picture and gives its imageicon.
	 * @return
	 */
	public static ImageIcon getImage(){
		BufferedImage image = null;
		try {
			
			Process p = Runtime.getRuntime().exec("raspistill -t "+time+" -n -rot 270 -h "+height+" -w "+width+" -o image.jpg");
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
	