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

