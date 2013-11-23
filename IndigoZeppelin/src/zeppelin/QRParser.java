package zeppelin;

import java.io.FileNotFoundException;
import java.io.IOException;

import transfer.Transfer;


import com.google.zxing.NotFoundException;

import camera.Camera;
import command.*;

/**
 * Zet QR code om naar een command.
 *
 */
public abstract class QRParser {
	
	static MotorController mc = MotorController.getInstance();

	public static void parseQR(){
		try {
			String readQRCode = Camera.readQRCode();
			if(Main.getInstance().getSender()!=null) {
				Transfer transfer = new Transfer();
				transfer.setQRCode(readQRCode);
				Main.getInstance().getSender().sendTransfer(transfer);
			}
			String[] commands = readQRCode.split( "\\;" );
			for(String command: commands){
				String[] parts = command.split( "\\:" );
				switch(parts[1]){
				case "V":
					mc.addToCommandList(new MoveForward(Double.parseDouble(parts[1])));
					break;
				case "A":
					mc.addToCommandList(new MoveBackward(Double.parseDouble(parts[1])));
					break;
				case "S":
					mc.addToCommandList(new MoveUp(Double.parseDouble(parts[1])));
					break;
				case "D":
					mc.addToCommandList(new MoveDown(Double.parseDouble(parts[1])));
					break;
				case "L":
					mc.addToCommandList(new TurnLeft(Double.parseDouble(parts[1])));
					break;
				case "R":
					mc.addToCommandList(new TurnRight(Double.parseDouble(parts[1])));
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
