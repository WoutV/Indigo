package zeppelin;

import java.util.LinkedList;


import transfer.Transfer;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import connection.SenderPi;


/**
 * Controller voor de drie motoren.
 * !!!! right: motor 1 in doc (24,4)
 * 		up: motor 2 in doc (17,23)	
 * 		left : motor 3 in doc (9,7)
 * Usage: getInstance(), daarna: init met een GpioController
 */
public class MotorController {
	private Motor up;
	private Motor xMotor;
	private Motor yMotor;

	private Pin xfw = RaspiPin.GPIO_11, xrv = RaspiPin.GPIO_13, upfw = RaspiPin.GPIO_00,
			uprv = RaspiPin.GPIO_04, yfw = RaspiPin.GPIO_07, yrv = RaspiPin.GPIO_05;

	private int pwmPinX;
	private int pwmPinY;
	//TODO PINNEN NUMMERS NODIG !!!!!!!

	private GpioController gpiocontroller;


	
	private SenderPi sender;
	private static MotorController mc = new MotorController();
	private HeightController hc;
	
	private PositionController xController;
	private PositionController yController;
	
	private DistanceSensor ds;
	
	
	public double Kp=30;
	public double Kd=90;
	public double Ki=2;
	
	

	private MotorController() {
	}

	/**
	 * Geeft de enige instantie van deze singleton klasse terug
	 * Motors zijn hierin nog niet geinitialiseerd
	 * Hierna moet init() worden opgeroepen
	 */
	public static MotorController getInstance() {
		return mc;
	}

	/**
	 * Initialiseert de motoren.
	 * Mag maar een keer opgeroepen worden.
	 * GpioController moet worden meegegeven (nodig voor de motoren)
	 */
	public void init(GpioController gpio,DistanceSensor distanceSensor,SenderPi sender) {
		System.out.println("Initializing Motor before if test");
		if(gpiocontroller == null) {
			gpiocontroller = gpio;
			this.sender = sender;
			ds = distanceSensor;

			GpioPinPwmOutput pwm = gpiocontroller.provisionPwmOutputPin(RaspiPin.GPIO_01,"pwm");
			//init Motors
			System.out.println("Initializing Motor");
			xMotor = new Motor(xfw,xrv,gpiocontroller,Propellor.LEFT,pwm,sender, pwmPinX);
			xMotor.setOff();
			yMotor = new Motor(yfw,yrv,gpiocontroller,Propellor.RIGHT, pwm, sender, pwmPinY);
			yMotor.setOff();
			up = new Motor(upfw,uprv,gpiocontroller,Propellor.UP, pwm, sender, 0);
			up.setOff();
			up.PwmOn();
			hc = new HeightController(Kp, Ki, Kd, distanceSensor, up);
			Thread hct = new Thread(hc);
			System.out.println("Starting heightController Thread ");
			hct.start();
			System.out.println("HeightController Thread Started");
			
			xController = new PositionController(Kp, Ki, Kd, xMotor, 0);
			yController = new PositionController(Kp, Ki, Kd, yMotor, 0);
			
		}
	}

	/**
	 * Move up at full pwm.
	 * Automatic height control is shut down!
	 */
	public void elevate() {
		hc.stop();
		up.setPwmValue(1024);
		up.setForward();
	}
	
	
	
	/**
	 * Laat de zeppelin naar een bepaalde hoogte bewegen
	 * Deze methode gebruikt het PID-algoritme
	 * @param dest
	 * 		hoogte (in cm) waar naartoe moet worden bewogen
	 */
	public void moveToHeight(double dest) {
		hc.moveToHeight(dest);
		destination=dest;
	}
	
	private double destination=100;
	
	public double getDestination(){
		return destination;
	}
	/**
	 * Zet alle horizontale bewegingen (links draaien, rechts draaien, voorwaarts, achterwaarts) stop.
	 */
	public void stopHorizontalMovement() {
		xMotor.setOff();
		yMotor.setOff();
		
		
	}

	/**
	 * Zet verticale bewegingen stop (concreet wordt overgeschakeld naar zweef-pwm)
	 */
	public void stopElevate() {
		hc.stop();
		up.setOff();
	}

	private double[] horizontalDestination = {100,100};
	
	public double[] getHorizontalDestination() {
		return horizontalDestination;
	}	
	
	
	
	
}
