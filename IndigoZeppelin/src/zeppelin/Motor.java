package zeppelin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


public class Motor {
	private GpioPinDigitalOutput forwardPin;
	private GpioPinDigitalOutput reversePin;
	private GpioPinDigitalOutput pwmpin;
	private GpioController gpiocontroller;
	private Propellor id;
	
	public Motor(Pin fwPin, Pin rvPin, GpioController gpio,Propellor id) {
		gpiocontroller = gpio;
		forwardPin = gpiocontroller.provisionDigitalOutputPin(fwPin,"forward");
		reversePin = gpiocontroller.provisionDigitalOutputPin(rvPin,"backward");
		pwmpin = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_01,"pwm");
		this.id = id;
		
		//status wanneer de app wordt afgesloten
		forwardPin.setShutdownOptions(true,PinState.LOW);
		reversePin.setShutdownOptions(true,PinState.LOW);
	}
	
	/**
	 * Laat deze motor naar voor draaien.
	 */
	public void setForward() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.HIGH);
	}
	
	/**
	 * Laat deze motor naar achter draaien.
	 */
	public void setReverse() {
		forwardPin.setState(PinState.LOW);
		reversePin.setState(PinState.HIGH);
	}
	
	/**
	 * Zet deze motor uit.
	 */
	public void setOff() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.LOW);
	}
	
	/**
	 * Geef de status terug van de pinstates
	 * @return
	 * 		PinState[0]: state forward pin
	 * 		PinState[1]: state reverse pin
	 */
	//TEMP
	public PinState[] getPinStates() {
		PinState[] pinstates = new PinState[2];
		pinstates[0] = forwardPin.getState();
		pinstates[1] = reversePin.getState();
		return pinstates;
	}
}
