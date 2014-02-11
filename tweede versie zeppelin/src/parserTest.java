import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import command.Command;
import command.MoveBackward;
import command.MoveDown;
import command.MoveForward;
import command.MoveUp;
import command.TurnLeft;
import command.TurnRight;


public class parserTest {

	public static void main(String[] args)
	{
		int again =1;
		while(again==1){
			System.out.println("?");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				parseQR(br.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Again?(1/0)");
			try {
				again = Integer.parseInt(br.readLine());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	static LinkedList<Command> toAddCommands = new LinkedList<Command>();
	static int qrCodeOrder=1;
	public static void parseQR(String readQRCode){
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

	}




	private static void addToCommandList() {
		while(!toAddCommands.isEmpty()){
			System.out.println(toAddCommands.pop().toString());
		}
	}
}
