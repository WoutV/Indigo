package imageProcessing;

import java.io.IOException;

public class CameraThreadForPi implements Runnable{
	public static void  main(String[] args){
		CameraThreadForPi ct = new CameraThreadForPi();
		Thread thread = new Thread(ct);
		thread.start();
	}
	
	public CameraThreadForPi(){
	}
	private double  height = 480;
	private double  width = 640;
	
	/**
	 * Gets a new image every second and sends it to the client.
	 */
	//TODO set the time between photos
	public void run(){
		//while(true){
		try {
			 Process p = Runtime.getRuntime().exec("sudo raspistill -t 600000 -tl 40 -rot 90 -n -h "+height+" -w "+width+" -o /dev/shm/mjpeg/cam.jpg");
			p.waitFor();
		} catch ( IOException | InterruptedException e) {
			e.printStackTrace();
		//}
		}
	}
}

