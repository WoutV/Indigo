package zeppelin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;


public class Motor {
	private GpioPinDigitalOutput forwardPin;
	private GpioPinDigitalOutput reversePin;
	private GpioPinDigitalOutput pwmpin;
	private GpioController gpiocontroller;
	
	public Motor(Pin fwPin, Pin rvPin, GpioController gpio) {
		gpiocontroller = gpio;
		forwardPin = gpiocontroller.provisionDigitalOutputPin(fwPin,"forward");
		reversePin = gpiocontroller.provisionDigitalOutputPin(rvPin,"backward");
		pwmpin = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_01,"pwm");
	}
}
