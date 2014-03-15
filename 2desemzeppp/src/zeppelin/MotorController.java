package zeppelin;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import connection.SenderPi;


/**
 * Controller voor de drie motoren.
 * !!!! y: motor 1 in doc (24,4)
 * 		up: motor 2 in doc (17,23)	
 * 		x: motor 3 in doc (9,7)
 * Usage: getInstance(), daarna: init met een GpioController
 * 
 * Motor 1 = x, motor 2 = y, motor 3 = up
 */
public class MotorController {
	private Motor up;
	private Motor xMotor;
	private Motor yMotor;

	private Pin xfw = RaspiPin.GPIO_11, xrv = RaspiPin.GPIO_13, upfw = RaspiPin.GPIO_00,
			uprv = RaspiPin.GPIO_04, yfw = RaspiPin.GPIO_07, yrv = RaspiPin.GPIO_05;

	private GpioController gpiocontroller;
	
	private SenderPi sender;
	private static MotorController mc = new MotorController();
	private HeightController hc;
	
	private PositionController xController;
	private PositionController yController;
	
	private DistanceSensor ds;
		
	public double Kpup=30;
	public double Kdup=90;
	public double Kiup=2;
	public double Kpx=30;
	public double Kdx=90;
	public double Kix=2;
	public double Kpy=30;
	public double Kdy=90;
	public double Kiy=2;
	
	private double zDestination=100;
	private double[] horizontalDestination = {100,100};
	
	private MotorController() {
	}

	/**
	 * Get the only instance of this singleton class.
	 * Motors are not yet initialised.
	 * Afterwards, the init() method should be called.
	 */
	public static MotorController getInstance() {
		return mc;
	}

	/**
	 * Initialises the motors.
	 * Should only be called once.
	 */
	public void init(GpioController gpio,DistanceSensor distanceSensor,SenderPi sender) {
		if(gpiocontroller == null) {
			gpiocontroller = gpio;
			this.sender = sender;
			ds = distanceSensor;

			GpioPinPwmOutput pwm = gpiocontroller.provisionPwmOutputPin(RaspiPin.GPIO_01,"pwm");
			//init Motors
			xMotor = new Motor(xfw,xrv,gpiocontroller,Propellor.X,pwm,sender);
			xMotor.setOff();
			xMotor.PwmOn();
			yMotor = new Motor(yfw,yrv,gpiocontroller,Propellor.Y, pwm, sender);
			yMotor.setOff();
			yMotor.PwmOn();
			up = new Motor(upfw,uprv,gpiocontroller,Propellor.UP, pwm, sender);
			up.setOff();
			up.PwmOn();
			hc = new HeightController(Kpup, Kiup, Kdup, distanceSensor, up);
			Thread heightcontrollerthread = new Thread(hc);
			heightcontrollerthread.start();
		
//			xController = new PositionController(Kpx, Kix, Kdx, xMotor, true);
//			yController = new PositionController(Kpy, Kiy, Kdy, yMotor, false);		
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
	 * Move the zeppelin to a given height.
	 * This method uses the PID-algorithm.
	 * @param dest
	 * 		height (in cm) to go to
	 */
	public void moveToHeight(double dest) {
		hc.moveToHeight(dest);
		zDestination=dest;
	}
	
	/**
	 * Gets the height destination of the zeppelin.
	 */
	public double getDestination(){
		return zDestination;
	}
	
	/**
	 * Move the zeppelin to a given horizontal location.
	 * This method uses the PID-algorithm.
	 * @param dest
	 * 		dest[0] = x coordinate (in cm)
	 * 		dest[1] = y coordinate (in cm)
	 */
	public void moveToHorizontalLocation(double[] dest) {
		horizontalDestination=dest;
	}
	
	public double[] getHorizontalDestination(){
		return horizontalDestination;
	}
	
	/**
	 * Stop all horizontal movement (left, right, forward, backward).
	 */
	public void stopHorizontalMovement() {
		xMotor.setOff();
		yMotor.setOff();	
	}

	/**
	 * Stop all vertical movement.
	 */
	public void stopElevate() {
		hc.stop();
		up.setOff();
	}	
	
	/**
	 * Sets a given engine to a given pwm value.
	 * @param motor
	 * 			The number of the motor.
	 * 			1 -> x , 2 -> y , 3-> up.
	 * @param pwm
	 * 			The pwm-value: range -100 -> 100.
	 */
	public void setMotor(int motor,int pwm) {
		if(pwm < -100)
			pwm = -100;
		if(pwm > 100)
			pwm = 100;
		if(motor == 1)
			xMotor.setSoftPwmValue(pwm);
		if(motor == 2)
			yMotor.setSoftPwmValue(pwm);
		if(motor == 3)
			up.setSoftPwmValue(pwm);
	}
}
