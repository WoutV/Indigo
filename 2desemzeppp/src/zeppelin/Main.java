package zeppelin;



import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

import connection.SenderPi;

public class Main{


	private MotorController motorController = MotorController.getInstance();
	private GpioController gpio = GpioFactory.getInstance();
	private DistanceSensor distanceSensor;
	private Thread distanceSensorThread;
	
	private SenderPi sender;

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
	public void init(SenderPi sender) {
			this.sender = sender;
			System.out.println("Sender Set");
			
			distanceSensor = new DistanceSensor(sender);
			distanceSensorThread = new Thread(distanceSensor);
			distanceSensorThread.start();
			
			motorController.init(gpio,distanceSensor,sender);
			
	}
	
	public MotorController getMotorController() {
		return motorController;
	}
	
	public SenderPi getSender() {
		return sender;
	}
	
	public DistanceSensor getDistanceSensor() {
		return distanceSensor;
	}

}