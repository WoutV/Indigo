package zeppelin;

/**
 * A set of enums pertaining to propellors
 */
public enum Propellor {
	X,Y,UP;
	
	public enum Mode {
		ON,OFF,PWM;
	}
	
	public enum Direction {
		FORWARD,REVERSE;
	}
}
