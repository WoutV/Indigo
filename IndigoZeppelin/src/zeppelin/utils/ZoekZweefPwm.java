package zeppelin.utils;

import zeppelin.Motor;

import zeppelin.DistanceSensor;

/**
 * Klasse om de Zweef Pwm te zoeken.
 * Er wordt gestart met het interval  750-1024 (min voor aandrijving - max)
 * Het interval wordt elke keer verkleind.
 * Op de server wordt de huidige pwm geprint en de snelheid.
 * De gebruiker kan eventueel de uitvoering onderbreken.
 */
public class ZoekZweefPwm implements Runnable {

	private DistanceSensor distanceSensor;
	private Motor up;
	private int pwm;
	private boolean found;

	public ZoekZweefPwm(DistanceSensor distanceSensor, Motor up) {
		this.distanceSensor = distanceSensor;
		this.up = up;
	}


	public void getZweefPwm() {
		int low=750; int max=1024;
		up.setPwmValue((low+max)/2);
		double height = distanceSensor.getHeight();
		double prev = height;
		double tolerance = 3;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		height = distanceSensor.getHeight();
		double diff = height - prev;
		double v = (height-prev)/2;
		//sysout op server zodat user kan bijsturen
		System.out.println("pwm = " + (low+max)/2 + ", v = " + v);
		while(Math.abs(diff)>tolerance && !found) {
			if(diff > 0)
				max=(low+max)/2;
			else
				low= low +(max-low)/2;
			pwm = low+ (max-low)/2;
			up.setPwmValue(pwm);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				found = true;
				System.out.println("zoek zweef pwm gestopt door gebruiker");
			}
			prev = height;
			height = distanceSensor.getHeight();
			diff = height - prev;
			v = (height-prev)/2;
			//sysout op server zodat user kan bijsturen
			System.out.println("pwm = " + pwm + ", v = " + v);
		}
		found = true;
		System.out.println("found!!");
	}
	
	public void setFound() {
		found = true;
	}
	
	public int getPwmValue() {
		return pwm;
	}

	@Override
	public void run() {
		getZweefPwm();
	}
}
