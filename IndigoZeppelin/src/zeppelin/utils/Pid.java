package zeppelin.utils;

/**
 * A class representing the PID algorithm.
 * Determines output based on input and how far this lies from a specified destination.
 * Kp, Ki and Kd are constants determining the output.
 *
 */
public class Pid {
	protected double Kp, Ki, Kd;
	
	protected double previous_error = 0;
	protected double integral = 0;
	
	protected double dest;
	
	protected int dt;
	//om de hoeveel tijd? (hoe vaak samplen??)
	
	protected int zweefpwm = 858;
	
	/**
	 * Creates a new PID Algorithm.
	 * @param Kp
	 * @param Ki
	 * @param Kd
	 * @param dest
	 * 		The destination
	 * @param dt
	 * 		The sampling frequency in ms
	 */
	public Pid(double Kp, double Ki, double Kd, double dest, int dt){
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
		this.dest = dest;
		this.dt = dt;
	}
	
	/**
	 * Determines output based on current input.
	 * @param input
	 * @return
	 */
	public double getOutput(double input) {
		double error = dest - input;
		integral = integral + error*dt;
		double derivative = (error - previous_error)/dt;
		double output = Kp*error + Ki*integral + Kd*derivative;
		
		return output + zweefpwm;
	}

}
