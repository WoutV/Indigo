package zeppelin;

import server.SendToClient;
import transfer.Transfer.Key;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class Main{

	// BELANGRIJK: Pi4J & WiringPi libraries nodig op de RPi!!
	// dus wiringpi downloaden mss

	// mss: You must initialize wiringPi with one of wiringPiSetup() or
	// wiringPiSetupGpio() functions beforehand.
	// com.pi4j.wiringpi.Gpio.wiringPiSetup();

	private MotorController motorController = MotorController.getInstance();
	private GpioController gpio = GpioFactory.getInstance();
	private DistanceSensor distanceSensor;
	private Thread distanceSensorThread;
	
	private SendToClient sender;

	private static Main main = new Main();

	private Main() {
	}

	public static Main getInstance() {
		return main;
	}
	
	/**
	 * Initialiseert de Main.
	 * Mag maar een keer opgeroepen worden.
	 * Sender moet worden meegegeven.
	 */
	public void init(SendToClient sender) {
			this.sender = sender;
			System.out.println("Sender Set");
			
			distanceSensor = new DistanceSensor(sender);
			distanceSensorThread = new Thread(distanceSensor);
			distanceSensorThread.start();
			
			motorController.init(gpio,distanceSensor,sender);
			
	}
	

	public void processPressedKeyEvent(Key pressedKey) {
		//keyevent teruggestuurd om gui te updaten, evt vervangen door iets van motorstatus
		//Transfer transfer = new Transfer();
		//transfer.setKeyEvent(pressedKey, TransferType.KEYPRESSEDEVENT);
		//sender.sendTransfer(transfer);

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

	public void processReleasedKeyEvent(Key releasedKey) {
		//Transfer transfer = new Transfer();
		//transfer.setKeyEvent(releasedKey, TransferType.KEYRELEASEDEVENT);
		//sender.sendTransfer(transfer);
		if(releasedKey==Key.ELEVATE) {
			motorController.stopElevate();
		} else {
			motorController.stopHorizontalMovement();
		}
	}

	public void setFloatPwm(int pwm) {
		System.out.println("i dont get itµ");
		motorController.setFloatPwm(pwm);
	}

	
	public void changeFlyMode(boolean autoPilot){
		motorController.changeFlyMode(autoPilot);
		//if switched to automatic control: check for QR-code so
		//the zepp can start executing commands
	}

	public MotorController getMotorController() {
		return motorController;
	}
	
	public SendToClient getSender() {
		return sender;
	}
	
	public DistanceSensor getDistanceSensor() {
		return distanceSensor;
	}

	/**
	 * Hier iets van public Command ProcessQRCode die dan QRParser gebruikt? 
	 */
}
