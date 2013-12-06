package zeppelin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import transfer.Transfer;





import com.google.zxing.NotFoundException;

import camera.Camera;
import command.*;

/**
 * Zet QR code om naar een command.
 *
 */
public abstract class QRParser {

	static LinkedList<Command> toAddCommands = new LinkedList<Command>();
	static int qrCodeOrder=1;
	static MotorController mc = MotorController.getInstance();

	private static void tryParsing() throws FileNotFoundException, NotFoundException, IOException{
			String readQRCode = Camera.readQRCode();
			if(Main.getInstance().getSender()!=null) {
				Transfer transfer = new Transfer();
				transfer.setQRCode(readQRCode);
				Main.getInstance().getSender().sendTransfer(transfer);
			}
			String[] commands = readQRCode.split( "\\;" );
			for(String command: commands){
				String[] parts = command.split( "\\:" );
				switch(parts[0].trim()){
				case "V":
					toAddCommands.add(new MoveForward(Double.parseDouble(parts[1].trim())));
					break;
				case "A":
					toAddCommands.add(new MoveBackward(Double.parseDouble(parts[1].trim())));
					break;
				case "S":
					toAddCommands.add(new MoveUp(Double.parseDouble(parts[1].trim())));
					break;
				case "D":
					toAddCommands.add(new MoveDown(Double.parseDouble(parts[1].trim())));
					break;
				case "L":
					toAddCommands.add(new TurnLeft(Double.parseDouble(parts[1].trim())));
					break;
				case "R":
					toAddCommands.add(new TurnRight(Double.parseDouble(parts[1].trim())));
					break;
				case "N":
					System.out.println("qrCodeOrder: "+ qrCodeOrder);
					System.out.println("Before trimming"+ Integer.parseInt(parts[1]));
					System.out.println("After trimming"+ Integer.parseInt(parts[1].trim()));
					if(qrCodeOrder==Integer.parseInt(parts[1].trim())){
						System.out.println("Adding commands");
						mc.addToCommandList(toAddCommands);
						System.out.println("qrCodeOrder="+qrCodeOrder);
						qrCodeOrder++;
						falseFound=false;
					}
					else{
						toAddCommands.clear();
						falseFound=true;
						
					}
					break;
				}}
	
	}
	
	static boolean falseFound=false;
	static int tried=0;
	static boolean heightChanged=false;
	
	public static void parseQR(){
		boolean success=false;
		double originalHeight=mc.getDestination();
		while(!success){
			try {
				tryParsing();
				if(!falseFound)
					success=true;
				else{
					mc.moveToHeight(originalHeight-12);
					System.out.println("Wrong QR Read trying to go to: "+(originalHeight-12));
					heightChanged=true;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("This should not happen since the file is made in this method");
				e.printStackTrace();
			} catch (NotFoundException e) {
				// What should we de if we cannot find the QR?.
				if(tried==0){
						tried++;
					}
				else if(Main.getInstance().getDistanceSensor().getHeight()>150){
					success=true;
					System.out.println("QR NOT FOUND. GIVING UP");
				
				}
				else{
					mc.moveToHeight(originalHeight+tried*25);
					double togo= originalHeight+(tried*25);
					System.out.println("qr not found going to: "+togo);
					heightChanged=true;
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					tried++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(heightChanged){
				mc.moveToHeight(originalHeight);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		
	}
}
