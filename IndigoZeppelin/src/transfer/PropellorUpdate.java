package transfer;

import java.io.Serializable;

import transfer.Transfer.TransferType;
import zeppelin.Propellor;

public class PropellorUpdate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Propellor id;
	private Propellor.Mode mode;
	private Propellor.Direction direction;
	private int pwmvalue;

	/**
	 * Configures this object to send a propellor update
	 * @param id
	 * @param mode
	 * @param direction
	 * 		only necessary if mode == ON
	 * @param pwmvalue
	 * 		only necessary if mode == PWM
	 */
	public PropellorUpdate(Propellor id, Propellor.Mode mode, Propellor.Direction direction, int pwmvalue) {
		this.id = id;
		this.mode = mode;
		this.direction = direction;
		this.pwmvalue = pwmvalue;
	}

	public Propellor getPropellorId() {
		return id;
	}

	public Propellor.Mode getPropellorMode() {
		return mode;
	}

	public Propellor.Direction getPropellorDirection() {
		return direction;
	}

	public int getPropellorPwm() {
		return pwmvalue;
	}
}
