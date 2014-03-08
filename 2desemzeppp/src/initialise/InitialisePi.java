package initialise;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;

import transfer.Transfer;
import zeppelin.Main;
import zeppelin.MotorController;
import connection.*;
public class InitialisePi {
public static void main(String[] args) {
//	ReceiverPi receiverPi = new ReceiverPi();
//	Thread ReceiverPiThread = new Thread(receiverPi);
//	ReceiverPiThread.start();
	SenderPi sender = new SenderPi();
	Main main = Main.getInstance();
	main.init(sender);
	boolean exit = false;
	MotorController mc = MotorController.getInstance();
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	while(!exit){
		try {
			System.out.println("Which Motor?");
			int motor= Integer.parseInt(input.readLine());
			System.out.println("PWM");
			mc.setMotor(motor, Integer.parseInt(input.readLine()));
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	sender.exit();
	// Add more swag here :p 
}
}
