package imageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

/**
 * Gets the latest image from the raspberry pi and saves.µ
 * We implement threading system so that the image recognition thread doesn't not have to spend time to get the image from the pi.
 * @author Study
 *
 */
public class ImageUpdater implements Runnable{
	VideoCapture vc;
	protected ImageUpdater(){
		 vc = new VideoCapture("C:/Users/Study/Desktop/OpenCv/for final/VID_20140512_161640.mp4");
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
				vc.read(piImage);
			
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			
		}
		}
	}
	Mat piImage = new Mat();
	
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
