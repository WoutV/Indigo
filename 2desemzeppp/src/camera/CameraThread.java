package camera;

import connection.SenderPi;
import transfer.Transfer;

public class CameraThread implements Runnable{
	private SenderPi stc;
	public CameraThread(SenderPi stc){
		this.stc = stc;
	}
	/**
	 * gets a new image every one second and sends it to the client.
	 */
	public void run(){
		while(true){
		Transfer picture= new Transfer();
		picture.setImage(Camera.getImage());
		stc.sendTransfer(picture);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
}

