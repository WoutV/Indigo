package zeppelin;
import server.SendToClient;
import zeppelin.utils.Pid;
import zeppelin.utils.Pid2;
import zeppelin.utils.Pid3;
import zeppelin.utils.ZoekZweefPwm;

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
	
	private int zweefpwm;
	
	private SendToClient sender;

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
	public void init(GpioController gpio,DistanceSensor distanceSensor,SendToClient sender) {
		System.out.println("Initializing Motor before if test");
		if(gpiocontroller == null) {
			gpiocontroller = gpio;
			this.distanceSensor = distanceSensor;
			this.sender = sender;

			GpioPinPwmOutput pwm = gpiocontroller.provisionPwmOutputPin(RaspiPin.GPIO_01,"pwm");
			//init Motors
			System.out.println("Initializing Motor");
			left = new Motor(leftfw,leftrv,gpiocontroller,Propellor.LEFT,pwm,sender);
			left.setOff();
			right = new Motor(rightfw,rightrv,gpiocontroller,Propellor.RIGHT, pwm, sender);
			right.setOff();
			up = new Motor(upfw,uprv,gpiocontroller,Propellor.UP, pwm, sender);
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
		
		//sampling frequency
		int dt = 100;
		
		double height = distanceSensor.getHeight();
		//set the Kp, Kd and Ki here
		double Kp = (1024-zweefpwm)/150;
		Pid3 pid = new Pid3(Kp,0,5,dest,dt,zweefpwm);

		//current altitude
		
		
//		if((dest - height)>30){
//			derp =true;
//		}
		//tolerance: close enough to destination to quit (in cm)
		double tolerance = 3;

		//nothing to change from here
		double previousheight = height;
		double v = (height-previousheight)/(dt/1000.0);
		double previousv = v;
		double error = dest-height;
		double error1 = dest-height;
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
			System.out.println("Output: " +output );
		}
//		if(error1<0){
//			up.setPwmValue(zweefpwm+25);
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		up.setPwmValue(zweefpwm);
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

	/**
	 * Zet alle horizontale bewegingen (links draaien, rechts draaien, voorwaarts, achterwaarts) stop.
	 */
	public void stopHorizontalMovement() {
		left.setOff();
		right.setOff();
	}

	/**
	 * Zet verticale bewegingen stop (concreet wordt overgeschakeld naar zweef-pwm)
	 */
	public void stopElevate() {
		//up.setPwmValue(zweefpwm);
		up.setOff();
	}	
	
	private ZoekZweefPwm zoekZweefPwm;
	private Thread zoekZweefPwmThread;
	
	/**
	 * Stopt het automatisch zoeken naar zweef pwm
	 * en zet zweef pwm op de gegeven value
	 * @param pwm
	 */
	public void setFloatPwm(int pwm) {
		if(zoekZweefPwm != null) {
			zoekZweefPwmThread.interrupt();
			
		}
		up.setPwmValue(pwm);
		zweefpwm = pwm;
	}
	
	/**
	 * Indien niet aan het zoeken: gaat op zoek naar zweef-pwm
	 * Indien aan het zoeken: stopt
	 */
	public void searchFloatPwm() {
		//laat de ZweefZoeker automatisch de zweef pwm zoeken
		if(zoekZweefPwm == null) {
			zoekZweefPwm = new ZoekZweefPwm(distanceSensor,up);
			zoekZweefPwmThread = new Thread(zoekZweefPwm);
			zoekZweefPwmThread.start();
		}
		else {
			zoekZweefPwmThread.interrupt();
			zweefpwm = zoekZweefPwm.getPwmValue();
		}
					
	}
	
//	private boolean derp;
//	
//	public int getPwmToTarget(double currentHeight, double destination){
//		double difference = destination - currentHeight;
//		if(difference > 30){
//			return 900;
//		}
//		if(difference > 15){
//			if(derp==false){
//				return zweefpwm+15;
//			}else
//			return -800;
//		}
//		
//		if(difference >0)
//			return zweefpwm;
//		
//	}
}
