package initialise;

import imageProcessing.CameraThreadForPi;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;

import zeppelin.Main;
import zeppelin.MotorController;
import connection.*;

public class InitialisePi {
	public static void main(String[] args) throws NumberFormatException,
			IOException, InterruptedException {
		String ipAddress = args[0];
		int port = Integer.parseInt(args[1]);
		CameraThreadForPi cp = new CameraThreadForPi();
		Thread thread= new Thread(cp);
		thread.start();
		ReceiverPi receiverPi = new ReceiverPi(ipAddress,port);
		Thread ReceiverPiThread = new Thread(receiverPi);
		ReceiverPiThread.start();

		System.out.println("Sender Starting");
		SenderPi sender = new SenderPi(ipAddress,port);
		Thread.sleep(2000);
		sender.sendTransfer("rabbitmqtesterdetest", "test.test");
		// System.out.println("Sender Initalized, making new transfer");
		// Transfer transfer = new Transfer();
		// System.out.println("Transfer has been made, setting image");
		// transfer.setImage(Camera.getImage());
		// System.out.println("message set, sending transfer");
		// sender.sendTransfer(transfer);
		// System.out.println("Transfer sent!");

		Main main = Main.getInstance();
		System.out.println("Got Main");
		main.init(sender);
		System.out.println("Main Initialized");
		main.activateMotor1(100);
		Thread.sleep(2000);
		main.activateMotor1(0);
		main.activateMotor2(100);
		Thread.sleep(2000);
		main.activateMotor2(0);
		main.activateMotor3(100);
		Thread.sleep(2000);
		main.activateMotor3(0);

		// TESTING SOFTPWM
		// int exit = 0;
		// while (exit == 0) {
		// BufferedReader br = new BufferedReader(new InputStreamReader(
		// System.in));
		// MotorController mc = MotorController.getInstance();
		// System.out.println("Motor?");
		// int motor = Integer.parseInt(br.readLine());
		// System.out.println("PWM?");
		// int pwm = Integer.parseInt(br.readLine());
		// mc.setMotor(motor, pwm);
		// System.out.println("Exit(1/0)?");
		// exit = Integer.parseInt(br.readLine());
		// }
		// Add more swag here :p
		// sender.exit();
	}
}
