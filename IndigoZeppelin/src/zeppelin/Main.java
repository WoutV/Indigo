package zeppelin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;


public class Main {
	
	private MotorController motorController = MotorController.getInstance();
	private GpioController gpio = GpioFactory.getInstance();
	
	public Main(){
		motorController.init(gpio);
	}
	
	
	
}
