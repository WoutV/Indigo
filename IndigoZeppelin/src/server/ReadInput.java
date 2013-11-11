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
	SendToClient imageSender;
	/**
	 * This class reads the input in the server.
	 * @param stc
	 * 			the class to send the informations about the state of the raspberry pi.
	 * @param imageSender
	 * 			the class to send the image.
	 */
	public ReadInput(SendToClient stc, SendToClient imageSender)
	{
		this.stc=stc;
		this.imageSender = imageSender;
		
	}
	/**
	 * Loop.
	 * This thread waits until the user types in something, processes that information
	 * and calls the specific methods.
	 *\/help shows the help menu
	 *\/image takes an image sends that to the client.
	 *\/imagethread starts a thread(if not already started) which sends image every one - two second to the client.
	 *\/QR takes a high resolution image and processes the QR code found in it.
	 *\/exit sends a exit type transfer to the client.
	 *If something other is written then it is sent as a message to the client. 
	 */
	public void run() {
		try{
		printHelp();
		boolean imageThreadStarted=false;
		while(true)
		{	boolean imageSend=false;
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
					imageSend=true;
					ImageIcon image = Camera.getImage();//get message to send to client);
					information.setImage(image);
				}
				if(readString.equalsIgnoreCase("/QR")){
					String readQR = "Read from QR: "+Camera.readQRCode();
					System.out.println(readQR);
					information.setMessage(readQR);
				}
				if(readString.equalsIgnoreCase("/imagethread")){
					if(!imageThreadStarted){
						CameraThread ct = new CameraThread(imageSender);
						Thread cameraT = new Thread(ct);
						cameraT.start();
						imageThreadStarted = true;
					}
				}
				if(!imageSend)
				stc.sendTransfer(information);
				else
					imageSender.sendTransfer(information);
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