package zeppelin;

import zeppelin.utils.Pid;

public class PositionController implements Runnable {
	
	//TODO ONDERSCHEID MAKEN TUSSEN X EN Y CONTROLLER. HOE???

	private Boolean stop=false;

	private double destination;
	private double[] destinationArray;
	private Motor motor;

	private double Kp,Ki,Kd;
	private int dt = 100;

	private Pid pid;

	public PositionController(double Kp,double Ki, double Kd, Motor motor, int pwmPin){
		this.Kp=Kp;
		this.Kd=Kd;
		this.Ki=Ki;
		this.motor = motor;
	}

	@Override
	public  void run() {
		//wait for reliable data
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		destination = destinationArray[0];
		
		makePid();
		while(true){
			if(stop){
				try {
					synchronized(stop){
						stop.wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			double position = 0;
			double output = pid.getOutput(position);

			//TODO map pid output to 700-1024 value??
			motor.setPwmValue((int) output);
			try {
				Thread.sleep(dt);
			} catch (InterruptedException e) {
			}
			position = 0;
			//				System.out.println("Output: " +output );

		}
	}

	private void makePid() {
		pid = new Pid(Kp,Ki,Kd,destination,dt);
	}

	public void moveToHeight(double height){
		//		System.out.println("Changing height");
		destination = height;
		pid.setDestination(height);
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
	public void setZweefpwm() {
		makePid();
	}
	
}
