package zeppelin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Controller voor de drie motoren.
 * !!!! left: motor 1 in doc (24,4)
 * 		right: motor 2 in doc (17,23)	
 * 		up : motor 3 in doc (9,7)
 * Usage: getInstance(), daarna: init met een GpioController
 */
public class MotorController {
	private Motor up;
	private Motor left;
	private Motor right;
	
	private Pin leftfw = RaspiPin.GPIO_05, leftrv = RaspiPin.GPIO_07, rightfw = RaspiPin.GPIO_00,
			rightrv = RaspiPin.GPIO_04, upfw = RaspiPin.GPIO_13, uprv = RaspiPin.GPIO_11;
	
	
	private GpioController gpiocontroller;
	
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
	public void init(GpioController gpio) {
		if(gpiocontroller == null) {
			gpiocontroller = gpio;

			//init Motors
			left = new Motor(leftfw,leftrv,gpiocontroller,Propellor.LEFT);
			right = new Motor(rightfw,rightrv,gpiocontroller,Propellor.RIGHT);
			up = new Motor(upfw,uprv,gpiocontroller,Propellor.UP);
		}
	}
	
	
}