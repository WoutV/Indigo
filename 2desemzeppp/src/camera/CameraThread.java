package camera;

import javax.swing.ImageIcon;

import imageProcessing.ImageProcessor;
import connection.SenderPi;

public class CameraThread implements Runnable{
	private SenderPi stc;
	
	public CameraThread(SenderPi stc){
		this.stc = stc;
	}
	
	/**
	 * Gets a new image every second and sends it to the client.
	 */
	//TODO set the time between photos
	public void run(){
		while(true){
		//Transfer picture= new Transfer();
		//picture.setImage(Camera.getImage());
		//stc.sendTransfer(picture);
		ImageIcon image = Camera.getImage();
		ImageProcessor.processImage(image);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
	}
}

