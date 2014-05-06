package navigation;

import simulator.SimConnNoRabbitClient;
import connection.SenderClient;
import zeppelin.utils.Pid;

/**
 * Class to control the position in X- and Y-direction.
 * The zeppelin is placed in the center (coordinate transformation), and the new
 * coordinates of the destination are computed.
 * These coordinates indicate exactly how much the zeppelin should move in X- and Y-direction.
 * 
 * (x,y) plane: the default plane defined by the board, with (left,up) ==> (0,0)
 * shifted plane: (xs,ys): the plane as seen from the zeppelin
 * angle: angle between zeppelin and default plane. 0 => pointing up
 * 				clockwise: 10,20,...; counterclockwise: -10,-20,...
 */
public class PositionController {

	private static double[] destination;
	
	private static PositionController xController = new PositionController(0,0,0,true);
	private static PositionController yController = new PositionController(0,0,0,false);
	
	public static PositionController getXController() {
		return xController;
	}
	
	public static PositionController getYController() {
		return yController;
	}
	
	//used because positioncontroller running on client
	private SenderClient sender;
	private SimConnNoRabbitClient sender2;
	private static int i;
	private double Kp,Ki,Kd;
	public static boolean xrunning = false;
	//TODO
	private int dt = 500;
	
	private boolean x;

	private Pid pid;

	/**
	 * Creates a new positioncontroller for the x- or y-frame and motor.
	 * 
	 * @param Kp
	 * @param Ki
	 * @param Kd
	 * @param x
	 * 			true if this positioncontroller is for the x-frame
	 */
	private PositionController(double Kp,double Ki, double Kd, boolean x){
		this.Kp=Kp;
		this.Kd=Kd;
		this.Ki=Ki;
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
	 * Sets a new destination for the zeppelin. Coordinates should be given in map notation:
	 * (left,up) = (0,0).
	 * 
	 * @param dest
	 * 			dest[0]: x-coordinate (cm)
	 * 			dest[1]: y-coordinate (cm)
	 */
	public static void setDestination(double[] dest){
		destination = dest;
	}
	
	/**
	 * This method should be called when a new position is available,
	 * and new speeds for the motors should be calculated.
	 * 
	 * @param zepp
	 * 			The new location of the zeppelin.
	 * 			Should use map notation: (left,up) = (0,0).
	 * 			zepp[0]: x
	 * 			zepp[1]: y
	 * 			zepp[3]: alpha
	 */
	public void run(double[] zepp) {
		
		double[] toGo = newCoordinates(destination[0],destination[1], zepp[0], zepp[1], zepp[2]);
		//System.out.println(toGo[0] + "," + toGo[1]);
		if(x)
			pid.setDestination(toGo[0]);
		else
			pid.setDestination(toGo[1]);
		double output = pid.getOutput(0);

		if(!xrunning && x)
			return;
		if(xrunning && !x)
			return;
		//System.out.println(output);

		i++;
		if(i==3) {
			i=0;
		xrunning = !xrunning;}
		//output needs to be in range -100 -> 100
		String message="";
		String key;
		if(x)
			key = "indigo.lcommand.motor1";
		else
			key = "indigo.lcommand.motor2";
		message = message+ (int) (100.0/1024*output);
		if(sender != null)
			sender.sendTransfer(message,key);
		if(sender2 != null)
			sender2.sendTransfer(message, key);
	}

	private void makePid() {
		pid = new Pid(Kp,Ki,Kd,0,dt);
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
	
	/**
	 * Sets this PositionController to use a central server to send to the zeppelin/Sim.
	 * @param sender
	 */
	public void setSender(SenderClient sender) {
		this.sender = sender;
	}
	
	/**
	 * Sets this PositionController to use a non-central server to send to the Sim.
	 * @param sender
	 */
	public void setSenderNoRabbit(SimConnNoRabbitClient sender) {
		sender2 = sender;
	}
}
