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
			
			Process p =Runtime.getRuntime().exec("/home/pi/RPi_Cam_Browser_Control_Installer.sh start");
			p.waitFor(); 
			Thread.sleep(1000);
			p = Runtime.getRuntime().exec("/home/pi/RPi_Cam_Browser_Control_Installer.sh stop");
			 p.waitFor();
			 Thread.sleep(1000);
			 p = Runtime.getRuntime().exec("sudo raspistill -t 6000000 -tl 40 -rot 90 -n -h 480 -w 640 -o /dev/shm/mjpeg/cam.jpg");
			p.waitFor();
		} catch ( IOException | InterruptedException e) {
			e.printStackTrace();
		//}
		}
	}
}

