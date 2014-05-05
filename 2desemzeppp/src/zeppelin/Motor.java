package zeppelin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.SoftPwm;

import connection.SenderPi;


public class Motor {
	private GpioPinDigitalOutput forwardPin;
	private GpioPinDigitalOutput reversePin;
	private GpioPinPwmOutput pwmPin;
	
	//int pins for softpwm
	private int fwPin;
	private int revPin;

	private boolean pwmEnabled;

	private GpioController gpiocontroller;
	private Propellor id;

	//previous mode and direction ==> avoids sending the same transfer twice
	private Propellor.Mode prevmode = Propellor.Mode.OFF;
	private Propellor.Direction prevdirection;

	private SenderPi sender;
	private int softPwmPin;

	/**
	 * Creates a motor.
	 * 
	 * @param fwPin
	 * 			The pin for forward movement.
	 * @param rvPin
	 * 			The pin for reverse movement.
	 * @param gpio
	 * 			The gpio controller.
	 * @param id
	 * 			The id of this propellor.
	 * @param pwmPin
	 * 			The pin if hardware pwm is used.
	 * @param sender
	 * 			The sender.
	 */
	public Motor(Pin fwPin, Pin rvPin, GpioController gpio,Propellor id, GpioPinPwmOutput pwmPin, SenderPi sender) {
		gpiocontroller = gpio;
		forwardPin = gpiocontroller.provisionDigitalOutputPin(fwPin,"forward");
		reversePin = gpiocontroller.provisionDigitalOutputPin(rvPin,"backward");
		
		this.id = id;
		this.sender = sender;
		
		//TODO nummers omgekeerd?
		if(id != Propellor.UP) {
			if(id == Propellor.X){
				this.fwPin=13;
				this.revPin=11;
			}
			else{
				this.fwPin=05;
				this.revPin=07;
			}
			SoftPwm.softPwmCreate(this.fwPin, 0, 100);
			SoftPwm.softPwmCreate(this.revPin, 0, 100);
		} else {
			this.pwmPin = pwmPin; 
		}

		//status wanneer de app wordt afgesloten
		forwardPin.setShutdownOptions(true,PinState.LOW);
		reversePin.setShutdownOptions(true,PinState.LOW);
	}

	/**
	 * Set this motor "forward".
	 */
	public void setForward() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.HIGH);

		if(prevdirection != Propellor.Direction.FORWARD || prevmode != Propellor.Mode.ON) {
			
			String message = "true";
			String key;
			if(id==Propellor.X)
				key = "indigo.private.motor1";
			else if(id==Propellor.Y)
				key = "indigo.private.motor2";
			else
				key="indigo.private.motor3";
			sender.sendTransfer(message,key);

			prevmode = Propellor.Mode.ON;
			prevdirection = Propellor.Direction.FORWARD;
		}
	}

	/**
	 * Set this motor to "forward".
	 * No update is sent.
	 */
	private void fw() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.HIGH);
	}

	/**
	 * Set this motor to "reverse".
	 */
	public void setReverse() {
		forwardPin.setState(PinState.LOW);
		reversePin.setState(PinState.HIGH);
		
		if(prevdirection != Propellor.Direction.REVERSE || prevmode != Propellor.Mode.ON) {

			String message = "true";
			String key;
			if(id==Propellor.X)
				key = "indigo.private.motor1";
			else if(id==Propellor.Y)
				key = "indigo.private.motor2";
			else
				key="indigo.private.motor3";
			sender.sendTransfer(message,key);


			prevmode = Propellor.Mode.ON;
			prevdirection = Propellor.Direction.REVERSE;
		}
	}

	/**
	 * Set this motor to "reverse".
	 * No update is sent.
	 */
	private void rv() {
		forwardPin.setState(PinState.LOW);
		reversePin.setState(PinState.HIGH);
	}

	/**
	 * Set this motor to "off".
	 */
	public void setOff() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.LOW);
		
		if(prevmode != Propellor.Mode.OFF) {

			String message = "false";
			String key;
			if(id==Propellor.X)
				key = "indigo.private.motor1";
			else if(id==Propellor.Y)
				key = "indigo.private.motor2";
			else
				key="indigo.private.motor3";
			sender.sendTransfer(message,key);


			prevmode = Propellor.Mode.OFF;
			prevdirection = null;
		}
	}
	
	/**
	 * Set this motor to "off".
	 * No update is sent.
	 */
	public void off() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.LOW);
	}

	/**
	 * Activate pwm.
	 */
	public void PwmOn() {
		pwmEnabled = true;
	}

	/**
	 * Deactivate
	 * @param set0
	 * 		To indicate whether pwm pin should be set to 0.
	 * 		This should not be done if other motors are still using it.
	 * 		This only applies to the hardware pwm pin.
	 */
	public void PwmOff(boolean set0) {
		pwmEnabled = false;
		if(pwmPin != null && set0)
			pwmPin.setPwm(0);
	}

	/**
	 * Sets this motor to a given pwm value.
	 * Pwm needs to be enabled.
	 * Hardware pwm values are assumed.
	 * @param value
	 * 			Range: -1024 (min) --> 1024 (max)
	 */
	public void setPwmValue(int value) {
		if(pwmEnabled) {
			if(value==0){
				if(id==Propellor.UP)
					pwmPin.setPwm(0);
				else
					SoftPwm.softPwmWrite(this.fwPin, 0);
					SoftPwm.softPwmWrite(this.revPin, 0);
			}
			else if(value > 0){
				if(id==Propellor.UP) {
					pwmPin.setPwm(1024);
					fw();
				} else {
					SoftPwm.softPwmWrite(this.fwPin, 100);
					SoftPwm.softPwmWrite(this.revPin, 0);
				}
//				fw();
				off();
				if(id==Propellor.UP) {
					pwmPin.setPwm(value);
					fw();
				} else {
					SoftPwm.softPwmWrite(this.fwPin, value*100/1024);
					SoftPwm.softPwmWrite(this.revPin, 0);
				}
//				fw();
			}
			else{
				if(id==Propellor.UP) {
					pwmPin.setPwm(1024);
					rv();
				} else {
					SoftPwm.softPwmWrite(this.revPin, 100);
					SoftPwm.softPwmWrite(this.fwPin, 0);
				}
				off();
				if(id==Propellor.UP) {
					pwmPin.setPwm(-value);
					rv();
				} else {
					SoftPwm.softPwmWrite(this.revPin, -value*100/1024);
					SoftPwm.softPwmWrite(this.fwPin, 0);
				}
				
			}
		}

		//send an update: only if motor running now and was not running before
		//OR is not running now and was running before
		Propellor.Mode mode;
		boolean bool;
		if(pwmLargeEnoughForMovement(value)){
			mode = Propellor.Mode.ON;
			bool=true;
		}
		else{
			mode = Propellor.Mode.OFF;
			bool=false;
		}if(prevmode != mode) {

			String message = String.valueOf(bool);
			String key;
			if(id==Propellor.X)
				key = "indigo.private.motor1";
			else if(id==Propellor.Y)
				key = "indigo.private.motor2";
			else
				key="indigo.private.motor3";
			sender.sendTransfer(message,key);

		}
		
		prevmode = mode;
	}
	
	/**
	 * Indicates whether a given pwm is large enough to make the engine run.
	 * @param pwm
	 * 			A hardware pwm value: -1024 -> 1024
	 */
	public boolean pwmLargeEnoughForMovement(int pwm) {
		if(id == Propellor.X || id == Propellor.Y)
			return true;
		return Math.abs(pwm) > 740;
	}
	
	/**
	 * Sets this motor to a given pwm value.
	 * Pwm needs to be enabled.
	 * Soft pwm values are assumed.
	 * @param value
	 * 			Range: -100 (min) --> 100 (max)
	 */
	public void setSoftPwmValue(int value) {
		if(pwmEnabled) {
			if(value == 0){
				if(id==Propellor.UP) {
					pwmPin.setPwm(1024);
				} else {
					SoftPwm.softPwmWrite(this.fwPin, 100);
					SoftPwm.softPwmWrite(this.revPin, 100);
				}
				fw();
				off();
				if(id==Propellor.UP) {
					pwmPin.setPwm(0);
				} else {
					SoftPwm.softPwmWrite(this.fwPin, 0);
					SoftPwm.softPwmWrite(this.revPin, 0);
				}
				fw();
			}
			if(value > 0){
				if(id==Propellor.UP) {
					pwmPin.setPwm(1024);
				} else {
					SoftPwm.softPwmWrite(this.fwPin, 100);
				}
				fw();
				off();
				if(id==Propellor.UP) {
					pwmPin.setPwm(value*1024/100);
				} else {
					SoftPwm.softPwmWrite(this.fwPin, value);
				}
				fw();
			}
			else{
				if(id==Propellor.UP) {
					pwmPin.setPwm(1024);
				} else {
					SoftPwm.softPwmWrite(this.revPin, 100);
				}
				rv();
				off();
				if(id==Propellor.UP) {
					pwmPin.setPwm(-value*1024/100);
				} else {
					SoftPwm.softPwmWrite(this.revPin, -value);
				}
				rv();
			}
		}
		
		//send an update: only if motor running now and was not running before
		//OR is not running now and was running before
		Propellor.Mode mode;
		boolean bool;
		if(value>15){
			mode = Propellor.Mode.ON;
			bool=true;
		}
		else{
			mode = Propellor.Mode.OFF;
			bool=false;
		}if(prevmode != mode) {

			String message = String.valueOf(bool);
			String key;
			if(id==Propellor.X)
				key = "indigo.private.motor1";
			else if(id==Propellor.Y)
				key = "indigo.private.motor2";
			else
				key="indigo.private.motor3";
			sender.sendTransfer(message,key);}

		prevmode = mode;
	}

	/**
	 * Indicates whether or not pwm is enabled.
	 */
	public boolean getPwmStatus() {
		return pwmEnabled;
	}
}
