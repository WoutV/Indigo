package zeppelin.utils;

/**
 * A class representing a PID algorithm with adjusted power output.
 * Output is now at most 1024.
 * Determines output based on input and how far this lies from a specified destination.
 * Kp, Ki and Kd are constants determining the output.
 *
 */
public class Pid3 {
	protected double Kp, Ki, Kd;
	
	protected double previous_error = 0;
	protected Integral integ = new Integral(200);
	protected double integral;
	
	protected double dest;
	
	protected int dt;
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
	public Pid3(double Kp, double Ki, double Kd, double dest, int dt){
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
		this.dest = dest;
		this.dt = dt;
		this.integral = 848/Ki;
	
	}
	
	/**
	 * Determines output based on current input.
	 * @param input
	 * @return
	 */
	public double getOutput(double input) {
		double error = dest - input;
		System.out.println("Destination="+dest);
		System.out.println("currentheight: "+ input);
//		integral += error*dt/1000.0;
		integ.addToIntegral(error*dt/1000.0);
		integral = integ.getValue();
		System.out.println("Integral:"+integral);
		double derivative = (error - previous_error)/(dt/1000.0);
//		System.out.println("error: "+error);
//		System.out.println("derivative: "+derivative);
		double output = Kp*error + Ki*integral + Kd*derivative;
		System.out.println("Ki*integral:"+Ki*integral);
		System.out.println("Output: (" +output+")");
		previous_error = error;
//		if(error > minErrorForMaxPwm)
//			return 900;
		if(output > 1024)
			return 1024;
		if(output < -1024)
			return -1024;
		return output;
	}
	
	public void setDestination(double dest){
		this.dest=dest;
	}

}
