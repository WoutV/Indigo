package zeppelin;
import transfer.Transfer;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import connection.SenderPi;


public class Motor {
	private GpioPinDigitalOutput forwardPin;
	private GpioPinDigitalOutput reversePin;
	private GpioPinPwmOutput pwmPin;

	//PWM: value tussen 0 en 1024 (volgens WiringPi)
	private boolean pwmEnabled;

	private GpioController gpiocontroller;
	private Propellor id;

	//previous mode and direction ==> avoids sending the same transfer twice
	private Propellor.Mode prevmode = Propellor.Mode.OFF;
	private Propellor.Direction prevdirection;

	private SenderPi sender;

	public Motor(Pin fwPin, Pin rvPin, GpioController gpio,Propellor id, GpioPinPwmOutput pwmPin, SenderPi sender) {
		gpiocontroller = gpio;
		forwardPin = gpiocontroller.provisionDigitalOutputPin(fwPin,"forward");
		reversePin = gpiocontroller.provisionDigitalOutputPin(rvPin,"backward");
		this.pwmPin = pwmPin; 
		this.id = id;
		this.sender = sender;

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

		if(prevdirection != Propellor.Direction.FORWARD || prevmode != Propellor.Mode.ON) {
			Transfer transfer = new Transfer();
			transfer.setPropellor(id, Propellor.Mode.ON, Propellor.Direction.FORWARD, 0);
			sender.sendTransfer(transfer);

			prevmode = Propellor.Mode.ON;
			prevdirection = Propellor.Direction.FORWARD;
		}
	}

	/**
	 * Laat deze motor naar voor draaien.
	 * Stuurt geen update
	 */
	private void fw() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.HIGH);
	}

	/**
	 * Laat deze motor naar achter draaien.
	 */
	public void setReverse() {
		forwardPin.setState(PinState.LOW);
		reversePin.setState(PinState.HIGH);
		
		if(prevdirection != Propellor.Direction.REVERSE || prevmode != Propellor.Mode.ON) {
			Transfer transfer = new Transfer();
			transfer.setPropellor(id, Propellor.Mode.ON, Propellor.Direction.REVERSE, 0);
			sender.sendTransfer(transfer);

			prevmode = Propellor.Mode.ON;
			prevdirection = Propellor.Direction.REVERSE;
		}
	}

	/**
	 * Laat deze motor naar achter draaien.
	 * Stuurt geen update
	 */
	private void rv() {
		forwardPin.setState(PinState.LOW);
		reversePin.setState(PinState.HIGH);
	}

	/**
	 * Zet deze motor uit.
	 */
	public void setOff() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.LOW);
		
		if(prevmode != Propellor.Mode.OFF) {
			Transfer transfer = new Transfer();
			transfer.setPropellor(id, Propellor.Mode.OFF, null, 0);
			sender.sendTransfer(transfer);

			prevmode = Propellor.Mode.OFF;
			prevdirection = null;
		}
	}
	
	/**
	 * Zet deze motor uit.
	 * Stuurt geen update
	 */
	public void off() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.LOW);
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
				fw();
				off();
				pwmPin.setPwm(value);
				fw();
			}
			else{
				pwmPin.setPwm(1024);
				rv();
				off();
				pwmPin.setPwm(Math.abs(value));
				rv();
			}
			
			
//			Transfer transfer = new Transfer();
//			transfer.setPropellor(id, Propellor.Mode.PWM, null, value);
//			sender.sendTransfer(transfer);
			prevmode = Propellor.Mode.PWM;
			
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
