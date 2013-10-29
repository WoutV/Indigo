package camera;

import server.SendToClient;
import transfer.Transfer;

public class CameraThread implements Runnable{
	private SendToClient stc;
	public CameraThread(SendToClient stc){
		this.stc = stc;
	}
	public void run(){
		while(true){
		Transfer picture= new Transfer();
		picture.setImage(Camera.getImage());
		stc.sendTransfer(picture);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
}

