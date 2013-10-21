package zeppelin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;


public class MotorController {
	private Motor forward;
	private Motor left;
	private Motor right;
	
	private Pin leftfw = RaspiPin.GPIO_05, leftrv = RaspiPin.GPIO_07, rightfw = RaspiPin.GPIO_00,
			rightrv = RaspiPin.GPIO_04, upfw = RaspiPin.GPIO_13, uprv = RaspiPin.GPIO_11;
	
	
	private GpioController gpiocontroller;
	
	private static MotorController mc = new MotorController();
	
	private MotorController() {
		//init Motors
	}
	
	/**
	 * Geeft de enige instantie van deze singleton klasse terug
	 * Motors zijn hierin al geinitialiseerd.
	 */
	public static MotorController getInstance() {
		return mc;
	}
	
	/**
	 * Zet de gpioController van de MotorController
	 * Mag maar een keer opgeroepen worden.
	 */
	public void setGpio(GpioController gpio) {
		if(gpiocontroller == null)
			gpiocontroller = gpio;
	}
}
