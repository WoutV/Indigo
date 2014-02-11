package zeppelin;

/**
 * A set of enums pertaining to propellors
 */
public enum Propellor {
	LEFT,RIGHT,UP;
	
	public enum Mode {
		ON,OFF,PWM;
	}
	
	public enum Direction {
		FORWARD,REVERSE;
	}
}
