package zeppelin;

import zeppelin.utils.Pid3;

public class HeightController implements Runnable{

	private Boolean stop=false;

	private double destination;
	private DistanceSensor ds;
	private Motor up;

	private double Kp,Ki,Kd;
	private int dt = 100;

	private Pid3 pid;

	public HeightController(double Kp,double Ki, double Kd,DistanceSensor ds,Motor up){
		this.Kp=Kp;
		this.Kd=Kd;
		this.Ki=Ki;
		this.ds=ds;
		this.up =up;
	}

	@Override
	public  void run() {
		//wait for reliable data
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		destination = ds.getHeight();

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
			double height = ds.getHeight();
							System.out.println("going to : " + destination);
			double output = pid.getOutput(height);
			
			//TODO map pid output to 700-1024 value??
			up.setPwmValue((int) output);
			try {
				Thread.sleep(dt);
			} catch (InterruptedException e) {
			}
			height = ds.getHeight();
			//				System.out.println("Output: " +output );

		}
	}

	private void makePid() {
		pid = new Pid3(Kp,Ki,Kd,destination,dt);
	}

	public void moveToHeight(double height){
		System.out.println("Changing height");
		destination = height;
		pid.setDestination(height);
		System.out.println("Height Changed");
		stop = false;
	}

	public void stop(){
		System.out.println("Method stop has been called");
		stop=true;
		System.out.println("Value of stop changed");
		synchronized(stop){
			System.out.println("Notifying stop");
			stop.notify();
			System.out.println("stop notified");
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
