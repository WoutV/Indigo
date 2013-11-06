package zeppelin;
import zeppelin.utils.Pid;
import zeppelin.utils.Pid2;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Controller voor de drie motoren.
 * !!!! right: motor 1 in doc (24,4)
 * 		up: motor 2 in doc (17,23)	
 * 		left : motor 3 in doc (9,7)
 * Usage: getInstance(), daarna: init met een GpioController
 */
public class MotorController {
	private Motor up;
	private Motor left;
	private Motor right;

	private Pin leftfw = RaspiPin.GPIO_13, leftrv = RaspiPin.GPIO_11, upfw = RaspiPin.GPIO_00,
			uprv = RaspiPin.GPIO_04, rightfw = RaspiPin.GPIO_05, rightrv = RaspiPin.GPIO_07;


	private GpioController gpiocontroller;
	private DistanceSensor distanceSensor;

	private static MotorController mc = new MotorController();

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
	public void init(GpioController gpio,DistanceSensor distanceSensor) {
		if(gpiocontroller == null) {
			gpiocontroller = gpio;
			this.distanceSensor = distanceSensor;

			GpioPinPwmOutput pwm = gpiocontroller.provisionPwmOutputPin(RaspiPin.GPIO_01,"pwm");
			//init Motors
			left = new Motor(leftfw,leftrv,gpiocontroller,Propellor.LEFT,pwm );
			left.setOff();
			right = new Motor(rightfw,rightrv,gpiocontroller,Propellor.RIGHT, pwm);
			right.setOff();
			up = new Motor(upfw,uprv,gpiocontroller,Propellor.UP, pwm);
			up.setOff();
			up.PwmOn();
		}
	}

	public void moveForward() {
		left.setForward();
		right.setForward();
	}

	public void moveBackward() {
		left.setReverse();
		right.setReverse();
	}

	public void turnLeft() {
		right.setForward();
		left.setReverse();
	}

	public void turnRight() {
		right.setReverse();
		left.setForward();
	}

	//dit gaat wss worden vervangen zodat de kracht kan worden ingesteld
	public void elevate() {
		up.setForward();
	}

	public void moveToHeight(double dest) {
		//sampling frequency
		int dt = 500;
		//desired altitude

		//set the Kp, Kd and Ki here
		Pid pid = new Pid2(200,0,0,dest,dt);

		//current altitude
		double height = distanceSensor.getHeight();

		//tolerance: close enough to destination to quit
		double tolerance = 0.02;

		//nothing to change from here
		double previousheight = height;
		double v = (height-previousheight)/(dt/1000.0);
		double previousv = v;
		double error = dest-height;
		while(Math.abs(error) > tolerance) {
			double output = pid.getOutput(height);
			up.setPwmValue((int) output);
			/*if(output > 1024)
						output = 1024;*/
			try {
				Thread.sleep(dt);
			} catch (InterruptedException e) {
			}
			previousheight = height;
			height = distanceSensor.getHeight();
			error = dest-height;
			previousv = v;
			double ts = dt/1000.0;
			v = (height-previousheight)/ts;
		}
	}

	public void moveDistanceForward(double distance) {
		//TODO
	}

	public void moveDistanceBackward(double distance) {
		//TODO
	}

	public void turnDegreesLeft(double angle) {
		//TODO
	}

	public void turnDegreesRight(double angle) {
		//TODO
	}

	/*public void stopMovingForward() {
		left.setOff();
		right.setForward();
	}

	public void stopMovingBackward() {
		left.setOff();
		right.setOff();
	}

	public void stopTurningLeft() {
		left.setOff();
		right.setOff();
	}

	public void stopTurningRight() {
		left.setOff();
		right.setOff();
	}
	 */

	/**
	 * Zet alle horizontale bewegingen (links draaien, rechts draaien, voorwaarts, achterwaarts) stop.
	 */
	public void stopHorizontalMovement() {
		left.setOff();
		right.setOff();
	}

	/**
	 * Zet de motor naar boven uit.
	 */
	//dit gaat de zweef-pwm worden ipv off!
	public void stopElevate() {
		up.setOff();
	}	
}
