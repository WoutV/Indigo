package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 * Gets the latest image from the raspberry pi and saves.µ
 * We implement threading system so that the image recognition thread doesn't not have to spend time to get the image from the pi.
 * @author Study
 *
 */
public class ImageUpdater implements Runnable{
	protected ImageUpdater(){
		
	}
	private Boolean stop = false;
	@Override
	public void run() {
		while(true){
			if(stop){
				try {
					synchronized(stop){
						stop.wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			BufferedImage buffered;
			try {
				Thread.sleep(100);
				buffered = ImageIO.read(new URL(
						"http://raspberrypi.mshome.net/cam_pic.php?time="
								+ System.currentTimeMillis()));
				byte[] pixels = ((DataBufferByte) buffered.getRaster()
						.getDataBuffer()).getData();
				
				piImage = new Mat(buffered.getHeight(),
						buffered.getWidth(), CvType.CV_8UC3);
				piImage.put(0, 0, pixels);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	Mat piImage;
	
	public void stop(){
		//		System.out.println("Method stop has been called");
		stop=true;
//		//		System.out.println("Value of stop changed");
//		synchronized(stop){
//			//			System.out.println("Notifying stop");
//			stop.notify();
//			//			System.out.println("stop notified");
//		}
	}
	public void startRunning(){
		stop = false;
		synchronized(stop){
			//			System.out.println("Notifying stop");
			stop.notify();
			//			System.out.println("stop notified");
		}
	}

	public Mat getLatestImage(){
		return piImage;
	}
	
}
