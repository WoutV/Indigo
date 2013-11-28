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
				switch(parts[0]){
				case "V":
					toAddCommands.add(new MoveForward(Double.parseDouble(parts[1])));
					break;
				case "A":
					toAddCommands.add(new MoveBackward(Double.parseDouble(parts[1])));
					break;
				case "S":
					toAddCommands.add(new MoveUp(Double.parseDouble(parts[1])));
					break;
				case "D":
					toAddCommands.add(new MoveDown(Double.parseDouble(parts[1])));
					break;
				case "L":
					toAddCommands.add(new TurnLeft(Double.parseDouble(parts[1])));
					break;
				case "R":
					toAddCommands.add(new TurnRight(Double.parseDouble(parts[1])));
					break;
				case "N":
					if(qrCodeOrder==Integer.parseInt(parts[1])){
						addToCommandList();
						qrCodeOrder++;
					}
					else{
						toAddCommands.clear();
					}
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

	private static void addToCommandList() {
		while(!toAddCommands.isEmpty()){
			mc.addToCommandList(toAddCommands.pop());
		}
	}
}
