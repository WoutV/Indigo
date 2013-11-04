package zeppelin;

import gui.GuiCommands.Key;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class Main {

	// BELANGRIJK: Pi4J & WiringPi libraries nodig op de RPi!!
	// dus wiringpi downloaden mss

	// mss: You must initialize wiringPi with one of wiringPiSetup() or
	// wiringPiSetupGpio() functions beforehand.
	// com.pi4j.wiringpi.Gpio.wiringPiSetup();

	private static MotorController motorController = MotorController.getInstance();
	private static GpioController gpio = GpioFactory.getInstance();
//	private static DistanceSensor distanceSensor = new DistanceSensor();
	private Thread distanceSensorThread;

	private static boolean init;

	public static void processPressedKeyEvent(Key pressedKey) {
		if(!init){
			motorController.init(gpio);
			init=true;
		}
		switch (pressedKey) {
		case UP:
			motorController.moveForward();
			break;
		case DOWN:
			motorController.moveBackward();
			break;
		case LEFT:
			motorController.turnLeft();
			break;
		case RIGHT:
			motorController.turnRight();
			break;
		case ELEVATE:
			motorController.elevate();
			break;
		default:
			break;
		}
	}
	
	public static void processReleasedKeyEvent(Key releasedKey) {
		if(releasedKey==Key.ELEVATE) {
			motorController.stopElevate();
		} else {
			motorController.stopHorizontalMovement();
		}
	}
}
