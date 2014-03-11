package zeppelin;

import transfer.Transfer;
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
	private int fwPin;
	private int revPin;

	// PWM: value tussen 0 en 1024 (volgens WiringPi)
	private boolean pwmEnabled;

	private GpioController gpiocontroller;
	private Propellor id;

	// previous mode and direction ==> avoids sending the same transfer twice
	private Propellor.Mode prevmode = Propellor.Mode.OFF;
	private Propellor.Direction prevdirection;

	private SenderPi sender;
	private int softPwmPin;

	/**
	 * Create a motor.
	 * 
	 * @param fwPin
	 *            The pin for forward movement.
	 * @param rvPin
	 *            The pin for reverse movement.
	 * @param gpio
	 *            The gpio controller.
	 * @param id
	 *            The id of this propellor.
	 * @param pwmPin
	 *            The pin if hardware pwm is used.
	 * @param sender
	 *            The sender.
	 * @param softPwmPin
	 *            The pin (int) if soft pwm is used.
	 */
	public Motor(Pin fwPin, Pin rvPin, GpioController gpio, Propellor id,
			GpioPinPwmOutput pwmPin, SenderPi sender) {
		gpiocontroller = gpio;
		forwardPin = gpiocontroller.provisionDigitalOutputPin(fwPin, "forward");
		reversePin = gpiocontroller
				.provisionDigitalOutputPin(rvPin, "backward");

		this.id = id;
		this.sender = sender;

		if (id != Propellor.UP) {
			if (id == Propellor.X) {
				this.fwPin = 13;
				this.revPin = 11;
			} else {
				this.fwPin = 5;
				this.revPin = 7;
			}
			SoftPwm.softPwmCreate(this.fwPin, 0, 100);
			SoftPwm.softPwmCreate(this.revPin, 0, 100);
			// TODO PIN NUMMER !!!!! EERSTE ARGUMENT
		} else {
			this.pwmPin = pwmPin;
		}

		// status wanneer de app wordt afgesloten
		forwardPin.setShutdownOptions(true, PinState.LOW);
		reversePin.setShutdownOptions(true, PinState.LOW);
	}

	/**
	 * Set this motor "forward".
	 */
	public void setForward() {
		reversePin.setState(PinState.LOW);
		forwardPin.setState(PinState.HIGH);

		if (prevdirection != Propellor.Direction.FORWARD
				|| prevmode != Propellor.Mode.ON) {
			Transfer transfer = new Transfer();
			transfer.setPropellor(id, Propellor.Mode.ON,
					Propellor.Direction.FORWARD, 0);
			sender.sendTransfer(transfer);

			prevmode = Propellor.Mode.ON;
			prevdirection = Propellor.Direction.FORWARD;
		}
	}

	/**
	 * Set this motor to "forward". No update is sent.
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

		if (prevdirection != Propellor.Direction.REVERSE
				|| prevmode != Propellor.Mode.ON) {
			Transfer transfer = new Transfer();
			transfer.setPropellor(id, Propellor.Mode.ON,
					Propellor.Direction.REVERSE, 0);
			sender.sendTransfer(transfer);

			prevmode = Propellor.Mode.ON;
			prevdirection = Propellor.Direction.REVERSE;
		}
	}

	/**
	 * Set this motor to "reverse". No update is sent.
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

		if (prevmode != Propellor.Mode.OFF) {
			Transfer transfer = new Transfer();
			transfer.setPropellor(id, Propellor.Mode.OFF, null, 0);
			sender.sendTransfer(transfer);

			prevmode = Propellor.Mode.OFF;
			prevdirection = null;
		}
	}

	/**
	 * Set this motor to "off". No update is sent.
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
	 * 
	 * @param set0
	 *            To indicate whether pwm pin should be set to 0. This should
	 *            not be done if other motors are still using it. This only
	 *            applies to the hardware pwm pin.
	 */
	public void PwmOff(boolean set0) {
		pwmEnabled = false;
		if (pwmPin != null && set0)
			pwmPin.setPwm(0);
	}

	/**
	 * Set this motor to a given pwm value. Pwm needs to be enabled. Hardware
	 * pwm values are assumed.
	 * 
	 * @param value
	 *            Range: -1024 (min) --> 1024 (max)
	 */
	public void setPwmValue(int value) {
		if (pwmEnabled) {
			if (value > 0) {
				if (id == Propellor.UP) {
					pwmPin.setPwm(1024);
				} else {
					SoftPwm.softPwmWrite(this.fwPin, 100);
				}
				fw();
				off();
				if (id == Propellor.UP) {
					pwmPin.setPwm(value);
				} else {
					SoftPwm.softPwmWrite(this.fwPin, value * 100 / 1024);
				}
				fw();
			} else {
				if (id == Propellor.UP) {
					pwmPin.setPwm(1024);
				} else {
					SoftPwm.softPwmWrite(this.revPin, 100);
				}
				rv();
				off();
				if (id == Propellor.UP) {
					pwmPin.setPwm(-value);
				} else {
					SoftPwm.softPwmWrite(this.revPin, -value * 100 / 1024);
				}
				rv();
			}
		}

		// send an update: only if motor running now and was not running before
		// OR is not running now and was running before
		Propellor.Mode mode;
		if (pwmLargeEnoughForMovement(value))
			mode = Propellor.Mode.ON;
		else
			mode = Propellor.Mode.OFF;
		if (prevmode != mode) {
			Transfer transfer = new Transfer();
			transfer.setPropellor(id, Propellor.Mode.PWM, null, value);
			sender.sendTransfer(transfer);
		}

		prevmode = mode;
	}

	/**
	 * Indicates whether a given pwm is large enough to make the engine run.
	 * 
	 * @param pwm
	 *            A hardware pwm value: -1024 -> 1024
	 */
	public boolean pwmLargeEnoughForMovement(int pwm) {
		return Math.abs(pwm) > 740;
	}

	/**
	 * Set this motor to a given pwm value. Pwm needs to be enabled. Soft pwm
	 * values are assumed.
	 * 
	 * @param value
	 *            Range: -100 (min) --> 100 (max)
	 */
	public void setSoftPwmValue(int value) {
		if (pwmEnabled) {
			System.out.println("Setting software pwm value in %:" + value
					+ " for propellor:" + id);
			if (value == 0) {
				if (id == Propellor.UP) {
					pwmPin.setPwm(0);
					fw();
				} else {
					SoftPwm.softPwmWrite(fwPin, 0);
					SoftPwm.softPwmWrite(revPin, 0);
				}
			} else if (value > 0) {
				if (id == Propellor.UP) {
					pwmPin.setPwm(1024);
					fw();
				} else {
					SoftPwm.softPwmWrite(fwPin, 100);
					SoftPwm.softPwmWrite(revPin, 0);
				}
				// fw();
				off();
				if (id == Propellor.UP) {
					pwmPin.setPwm(value * 1024 / 100);
					fw();
				} else {
					SoftPwm.softPwmWrite(fwPin, value);
					System.out.println("fwPin set to value:"+value);
					SoftPwm.softPwmWrite(revPin, 0);
					System.out.println("revPin set to value:0");
				}

			} else {
				if (id == Propellor.UP) {
					pwmPin.setPwm(1024);
					rv();
				} else {
					SoftPwm.softPwmWrite(revPin, 100);
					SoftPwm.softPwmWrite(fwPin, 0);
				}

				off();
				if (id == Propellor.UP) {
					pwmPin.setPwm(-value * 1024 / 100);
					rv();
				} else {
					SoftPwm.softPwmWrite(revPin, -value);
					SoftPwm.softPwmWrite(fwPin, 0);
				}

			}
		}
	}

	/**
	 * Indicates whether or not pwm is enabled.
	 */
	public boolean getPwmStatus() {
		return pwmEnabled;
	}
}
