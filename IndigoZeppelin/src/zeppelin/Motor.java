package zeppelin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


public class Motor {
	private GpioPinDigitalOutput forwardPin;
	private GpioPinDigitalOutput reversePin;
	private GpioPinPwmOutput pwmPin;
	
	//PWM: value tussen 0 en 1024 (volgens WiringPi)
	//of mss tussen 0 en 100 (dit is bij SoftPWM)
	//evt een aparte klasse pwm control die gedeeld tussen alle motoren
	private boolean pwmEnabled;
	
	private GpioController gpiocontroller;
	private Propellor id;
	
	public Motor(Pin fwPin, Pin rvPin, GpioController gpio,Propellor id, GpioPinPwmOutput pwmPin) {
		gpiocontroller = gpio;
		forwardPin = gpiocontroller.provisionDigitalOutputPin(fwPin,"forward");
		reversePin = gpiocontroller.provisionDigitalOutputPin(rvPin,"backward");
		this.pwmPin = pwmPin; 
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
	
	/**
	 * Activeert pwm op deze motor
	 */
	public void PwmOn() {
		pwmEnabled = true;
	}
	
	/**
	 * Deactiveert pwm op deze motor
	 * @param set0
	 * 		Om aan te geven of de pwm pin op 0 moet worden gezet
	 * 		Dus indien geen andere motoren hier nog gebruik van aan het maken zijn.
	 */
	public void PwmOff(boolean set0) {
		pwmEnabled = false;
		if(set0)
			pwmPin.setPwm(0);
	}
	
	/**
	 * Zet de pwm value en laat de motor in de juiste richting draaien
	 * indien pwm geactiveerd is op deze motor
	 * @param value
	 * 			Getal tussen -1024 (min) en 1024 (max)
	 */
	public void setPwmValue(int value) {
		if(pwmEnabled) {
			if(value > 0){
				pwmPin.setPwm(1024);
				setForward();
				setOff();
				pwmPin.setPwm(value);
				setForward();
			}
				else{
					pwmPin.setPwm(1024);
					setReverse();
					setOff();
					pwmPin.setPwm(Math.abs(value));
					setReverse();
				
				}
	
		}
	}
	
	/**
	 * Geeft aan of pwm geactiveerd is voor deze motor.
	 * @return
	 */
	public boolean getPwmStatus() {
		return pwmEnabled;
	}
}
