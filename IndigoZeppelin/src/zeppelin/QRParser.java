package zeppelin;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import com.google.zxing.NotFoundException;

import camera.Camera;
import command.*;

/**
 * Zet QR code om naar een command.
 * @author Wout
 *
 */
public abstract class QRParser {

	public static LinkedList<Command> parseQR(){
		LinkedList<Command> commandList = new LinkedList<Command>();
		try {
			String readQRCode = Camera.readQRCode();
			String[] commands = readQRCode.split( "\\;" );
			for(String command: commands){
				String[] parts = command.split( "\\:" );
				switch(parts[1]){
				case "V":
					commandList.add(new MoveForward(Double.parseDouble(parts[1])));
					break;
				case "A":
					commandList.add(new MoveBackward(Double.parseDouble(parts[1])));
					break;
				case "S":
					commandList.add(new MoveUp(Double.parseDouble(parts[1])));
					break;
				case "D":
					commandList.add(new MoveDown(Double.parseDouble(parts[1])));
					break;
				case "L":
					commandList.add(new TurnLeft(Double.parseDouble(parts[1])));
					break;
				case "R":
					commandList.add(new TurnRight(Double.parseDouble(parts[1])));
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
		return commandList;

	}
}
