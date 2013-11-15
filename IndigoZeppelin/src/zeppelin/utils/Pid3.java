package zeppelin.utils;

/**
 * A class representing a PID algorithm with adjusted power output.
 * Output is now at most 1024.
 * Determines output based on input and how far this lies from a specified destination.
 * Kp, Ki and Kd are constants determining the output.
 *
 */
public class Pid3 extends Pid {
	//when a larger error ==> automatically sets output to max (1024)
	//for some values of Kp this is unnecessary
	private double minErrorForMaxPwm = 1;
	
	//a similar strategy could be used for negative errors
	
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
	public Pid3(double Kp, double Ki, double Kd, double dest, int dt, int zweefpwm){
		super(Kp,Ki,Kd,dest,dt);
		this.zweefpwm=zweefpwm;
	}
	
	/**
	 * Determines output based on current input.
	 * @param input
	 * @return
	 */
	public double getOutput(double input) {
		double error = dest - input;
		System.out.println("currentheight: "+ input);
		integral = integral + error*dt/1000.0;
		double derivative = (error - previous_error)/(dt/1000.0);
		System.out.println("error: "+error);
		System.out.println("derivative: "+derivative);
		double output = Kp*error + Ki*integral + Kd*derivative;
		System.out.println("Zweefpwm + (" +output+")");
		output = output + zweefpwm;
		previous_error = error;
//		if(error > minErrorForMaxPwm)
//			return 900;
		if(output > 1024)
			return 1024;
		if(output < -750)
			return -750;
		return output;
	}

}
