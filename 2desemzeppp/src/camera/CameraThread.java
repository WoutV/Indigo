package camera;

import java.io.IOException;

import javax.swing.ImageIcon;

import imageProcessing.ImageProcessor;
import connection.SenderPi;

public class CameraThread implements Runnable{
	public static void  main(String[] args){
		CameraThread ct = new CameraThread();
		Thread thread = new Thread(ct);
		thread.start();
	}
	
	public CameraThread(){
	}
	private double  height = 400;
	private double  width = 600;
	
	/**
	 * Gets a new image every second and sends it to the client.
	 */
	//TODO set the time between photos
	public void run(){
		//while(true){
		try {
			Process p = Runtime.getRuntime().exec("sudo raspistill -t 30000 -tl 40 -n -rot 270 -h "+height+" -w "+width+" -o /dev/shm/mjpeg/cam.jpg");
			p.waitFor();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		//}
		}
	}
}

