package zeppelin.utils;

/**
 * A class simulating the PID algorithm.
 */
public class PidSim {
	
	//Kp = 1000, zweefpwm = 400, Kd = Ki = 0;
	//gedaan voor dt = 200 (OK), dt = 500 (minder), dt = 1 (niet goed)
	
	public PidSim() {
	}
	
	/*public int getZweefPwm() {
	  int min=750; int max=1024
		up.setPwmValue((min+max)/2);
		double height = distanceSensor.getHeight();
		double prev = height;
		double tolerance = 3;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		height = distanceSensor.getHeight();
		double diff = height - prev;
		//sysout
		while(Math.abs(diff)>tolerance) {
			if(diff > 0)
				max=(low+max)/2
			else
				low= low +(max-low)/2
			pwm = low+ (max-low)/2;
			up.setPwmValue(pwm);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			prev = height;
			height = distanceSensor.getHeight();
			diff = height - prev;
			//sysout
		}
		return pwm;
	}*/
	
	public void run() {
		//sampling frequency
		int dt = 200;
		//desired altitude
		double dest = 2;
		
		//set the Kp, Kd and Ki here
		Pid pid = new Pid2(200,0,150,dest,dt);
		
		//current altitude
		double height = 0.5;
		
		//tolerance: close enough to destination to quit
		double tolerance = 0.01;
		
		//nothing to change from here
		double previousheight = height;
		double v = (height-previousheight)/(dt/1000.0);
		double previousv = v;
		double a = (v - previousv)/(dt/1000.0);
		double error = dest-height;
		while(Math.abs(error) > tolerance) {
			double output = pid.getOutput(height);
			/*if(output > 1024)
				output = 1024;*/
			try {
				Thread.sleep(dt);
			} catch (InterruptedException e) {
			}
			previousheight = height;
			height = nieuwehoogte(output,dt,v,a,height);
			error = dest-height;
			previousv = v;
			double ts = dt/1000.0;
			v = (height-previousheight)/ts;
			
			a = (v - previousv)/ts;
			System.out.println("Height: " + height + ",error: " + error + ",v: " + v + ",a: " + a + ",Output: " + output);
		}
	}
	
	//bv max output: 1024 --> 5 m/s� + 9.81 --> 14.81 m/s�
	//1024 --> max
	//400 (= zweef) --> 9.81
	//400/x = 9.81 ==> X (factor) = 400/9.81
	
	private double X = 400/9.81;
	
	
	/**
	 * Bepaalt de nieuwe hoogte op basis van de output van de motor, het interval (in ms), de snelheid, 
	 * de vorige totale a en de huidige hoogte
	 * @param outputmotor
	 * @param t
	 * @param v
	 * @param previousa
	 * @param height
	 * @return
	 */
	public double nieuwehoogte(double outputmotor, int t, double v, double previousa, double height) {
		//er van uit gegaan dat de outputversnelling omhoog afh van de outputpower van de motor, 
		//lineair volgens factor X
		//gedempt door de vorige a, waar we wel 9.81 moeten bijtellen omdat het over de a omhoog gaat
		//nadien de echte a door er g van af te trekken
		double a = (outputmotor/X);
		double atotal = a - 9.81;
		//tijd in s
		double ts = t/1000.0;
		double newHeight = height + v*ts + atotal*ts*ts/2;
		return newHeight;
	}
	
	public static void main(String[] args) {
		PidSim pidsim = new PidSim();
		pidsim.run();
	}
	
	
}
