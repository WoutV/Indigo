package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.ImageIcon;

import camera.Camera;
import camera.CameraThread;

import transfer.Transfer;
import transfer.Transfer.TransferType;

class ReadInput implements Runnable
{
	SendToClient stc;
	
	public ReadInput(SendToClient stc)
	{
		this.stc=stc;
		
	}
	public void run() {
		try{
		printHelp();
		boolean imageThreadStarted=false;
		while(true)
		{	
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));//get userinput
			String readString = input.readLine();//get message to send to client
			if(readString.equalsIgnoreCase("/help")){
				printHelp();
			}
			else{
				Transfer information = new Transfer();
				information.setMessage(readString);
				if(readString.equalsIgnoreCase("/exit") || readString.equalsIgnoreCase("/close")){
					information.setType(TransferType.EXIT);
				}
				if(readString.equalsIgnoreCase("/image")){
					ImageIcon image = Camera.getImage();//get message to send to client);
					information.setImage(image);
				}
				if(readString.equalsIgnoreCase("/QR")){
					information.setMessage(Camera.readQRCode());
				}
				if(readString.equalsIgnoreCase("imagethread")){
					if(!imageThreadStarted){
						CameraThread ct = new CameraThread(stc);
						Thread cameraT = new Thread(ct);
						cameraT.start();
						imageThreadStarted = true;
					}
				}
				
				stc.sendTransfer(information);
			}
			
		}//end while
		}
		catch(Exception ex){System.out.println(ex.getMessage());}	
	}//end run
	private void printHelp(){
		System.out.println("Your commands are(will ignore case):");
		System.out.print("/exit : To exit the program.This will also close the client! \n " +
				"/image: This will take a picture and sent that picture \n" +
				"/QR: Will first take picture and then try to read the QR on it (takes some time!) \n" +
				"/imagethread: Will start an image thread(if not already done) which will send picture in every two seconds \n" +
				"/help: To show this help \n");
		
	}
}//end class ReadInput