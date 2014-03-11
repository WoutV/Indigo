package zeppelin;

import transfer.Transfer;
import connection.SenderClient;
import zeppelin.utils.Pid;

/**
 * (x,y) plane: the default plane defined by the board, with (left,up) ==> (0,0)
 * shifted plane: (xs,ys): the plane as seen from the zeppelin
 * angle: angle between zeppelin and default plane. 0 => pointing up
 * 				clockwise: 10,20,...; counterclockwise: -10,-20,...
 */
public class PositionController {
	
	//TODO ONDERSCHEID MAKEN TUSSEN X EN Y CONTROLLER. HOE???

	private Boolean stop=false;

	private double[] destination;
	private SenderClient sender;

	private double Kp,Ki,Kd;
	private int dt = 100;
	
	private boolean x;

	private Pid pid;

	/**
	 * Creates a new positoncontroller for the x- or y-frame and motor.
	 * @param Kp
	 * @param Ki
	 * @param Kd
	 * @param motor
	 * @param x
	 * 			true if this positioncontroller is for the x-frame
	 */
	public PositionController(double Kp,double Ki, double Kd, SenderClient sender, boolean x){
		this.Kp=Kp;
		this.Kd=Kd;
		this.Ki=Ki;
		this.sender = sender;
		this.x = x;
		makePid();
	}

	/**
	 * Calculates coordinates of a point in a different plane, given by the coordinates of the
	 * center of the new plane (relative to the original plane) and by the angle.
	 * r[0]: the new x-coordinate
	 * r[1]: the new y-coordinate
	 * 
	 * @param x
	 * 			x-coordinate to be transformed
	 * @param y
	 * 			y coordinate to be transformed
	 * @param xc
	 * 			x coordinate of the center of the new plane
	 * @param yc
	 * 			y coordinate of the center of the new plane
	 * @param alpha
	 * 			angle of the new plane
	 * 			range -180 -> 180
	 */
	public static double[] newCoordinates(double x, double y, double xc, double yc, double alpha) {
		double x0 = x - xc;
		double y0 = y - yc;
		//to make the plane point upward
		double alpha00 = alpha-180;
		if(alpha00 < -180)
			alpha00 = alpha00 + 360;
		double alpha0 = alpha00/180*Math.PI;
		double xn = x0*Math.cos(alpha0) + y0*Math.sin(alpha0);
		double yn = -x0*Math.sin(alpha0) + y0*Math.cos(alpha0);
		//because the plane points upward, but x-direction still the same as in original plane
		xn = -xn;
		double[] r = {xn,yn};
		return r;
	}
	
	/**
	 * This method should be called when a new position is available,
	 * and new speeds for the motors should be calculated.
	 * 
	 * @param zepp
	 * 			The new location of the zeppelin.
	 * 			zepp[0]: x
	 * 			zepp[1]: y
	 * 			zepp[3]: alpha
	 */
	public void run(double[] zepp) {
		
		double[] toGo = newCoordinates(destination[0],destination[1], zepp[0], zepp[1], zepp[2]);
		
		if(x)
			pid.setDestination(toGo[0]);
		else
			pid.setDestination(toGo[1]);
		double output = pid.getOutput(0);

		//motor.setPwmValue((int) output);
		Transfer transfer = new Transfer();
		if(x)
			transfer.setMotor1((int) output); 
		else
			transfer.setMotor2((int) output);
		sender.sendTransfer(transfer);
	}

	private void makePid() {
		pid = new Pid(Kp,Ki,Kd,0,dt);
	}

	public void setDestination(double[] destination){
		//		System.out.println("Changing height");
		this.destination = destination;
		//		System.out.println("Height Changed");
		stop = false;
	}

	public void stop(){
		//		System.out.println("Method stop has been called");
		stop=true;
		//		System.out.println("Value of stop changed");
		synchronized(stop){
			//			System.out.println("Notifying stop");
			stop.notify();
			//			System.out.println("stop notified");
		}
	}
	
	public void startRunning(){
		stop = false;
	}

	public double getKp() {
		return Kp;
	}
	
	public void setKp(double kp) {
		Kp = kp;
		makePid();
	}
	
	public double getKi() {
		return Ki;
	}
	
	public void setKi(double ki) {
		Ki = ki;
		makePid();
	}
	
	public double getKd() {
		return Kd;
	}
	
	public void setKd(double kd) {
		Kd = kd;
		makePid();
	}
}
