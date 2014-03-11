package initialise;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;

import camera.Camera;

import transfer.Transfer;
import zeppelin.Main;
import zeppelin.MotorController;
import connection.*;

public class InitialisePi {
	public static void main(String[] args) throws NumberFormatException,
			IOException {
		// ReceiverPi receiverPi = new ReceiverPi();
		// Thread ReceiverPiThread = new Thread(receiverPi);
		// ReceiverPiThread.start();

		System.out.println("Sender Starting");
		SenderPi sender = new SenderPi();
		// System.out.println("Sender Initalized, making new transfer");
		// Transfer transfer = new Transfer();
		// System.out.println("Transfer has been made, setting image");
		// transfer.setImage(Camera.getImage());
		// System.out.println("message set, sending transfer");
		// sender.sendTransfer(transfer);
		// System.out.println("Transfer sent!");
		sender.exit();
		Main main = Main.getInstance();
		main.init(sender);

		// TESTING SOFTPWM
		int exit = 0;
		while (exit == 0) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			MotorController mc = MotorController.getInstance();
			System.out.println("Motor?");
			int motor = Integer.parseInt(br.readLine());
			System.out.println("PWM?");
			int pwm = Integer.parseInt(br.readLine());
			mc.setMotor(motor, pwm);
			System.out.println("Exit(1/0)?");
			exit = Integer.parseInt(br.readLine());
		}
		// Add more swag here :p
	}
}
