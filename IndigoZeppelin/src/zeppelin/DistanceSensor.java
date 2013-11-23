package zeppelin;
import server.SendToClient;
import transfer.Transfer;
import zeppelin.utils.CircularDoubleArray;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class DistanceSensor implements Runnable{
	private CircularDoubleArray distanceArray;

	/**
	 * Class to monitor distance measured by an HC-SR04 distance sensor on a
	 * Raspberry Pi.
	 * 
	 * The main method assumes the trig pin is connected to the pin # 7 and the
	 * echo pin is connected to pin # 11. Output of the program are comma
	 * separated lines where the first value is the number of milliseconds since
	 * unix epoch, and the second value is the measured distance in centimeters.
	 */

	private final static float SOUND_SPEED = 340.29f; // speed of sound in m/s

	private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration
	// of 10 micro s

	//between readings
	private final static int WAIT_DURATION_IN_MILLIS = 20; // wait 20 milli s

	private final static int TIMEOUT = 2100;

	private final static GpioController gpio = GpioFactory.getInstance();

	private final GpioPinDigitalInput echoPin;
	private final GpioPinDigitalOutput trigPin;

	private SendToClient sender;

	public DistanceSensor(SendToClient sender) {
		this.echoPin = gpio.provisionDigitalInputPin( RaspiPin.GPIO_02 );
		this.trigPin = gpio.provisionDigitalOutputPin( RaspiPin.GPIO_03 );
		this.trigPin.low();
		this.distanceArray = new CircularDoubleArray(30);
		this.sender = sender;
	}

	/*
	 * This method returns the distance measured by the sensor in cm
	 * 
	 * @throws TimeoutException if a timeout occurs
	 */
	public float measureDistance() throws TimeoutException {
		this.triggerSensor();
		this.waitForSignal();
		long duration = this.measureSignal();

		return duration * SOUND_SPEED / (2 * 10000);
	}

	/**
	 * Put a high on the trig pin for TRIG_DURATION_IN_MICROS
	 */
	private void triggerSensor() {
		try {
			this.trigPin.high();
			Thread.sleep(0, TRIG_DURATION_IN_MICROS * 1000);
			this.trigPin.low();
		} catch (InterruptedException ex) {
			System.err.println("Interrupt during trigger");
		}
	}

	/**
	 * Wait for a high on the echo pin
	 * 
	 * @throws DistanceMonitor.TimeoutException
	 *             if no high appears in time
	 */
	private void waitForSignal() throws TimeoutException {
		int countdown = TIMEOUT;

		while (this.echoPin.isLow() && countdown > 0) {
			countdown--;
		}

		if (countdown <= 0) {
			throw new TimeoutException("Timeout waiting for signal start");
		}
	}

	/**
	 * @return the duration of the signal in micro seconds
	 * @throws DistanceMonitor.TimeoutException
	 *             if no low appears in time
	 */
	private long measureSignal() throws TimeoutException {
		int countdown = TIMEOUT;
		long start = System.nanoTime();
		while (this.echoPin.isHigh() && countdown > 0) {
			countdown--;
		}
		long end = System.nanoTime();

		if (countdown <= 0) {
			throw new TimeoutException("Timeout waiting for signal end");
		}

		return (long) Math.ceil((end - start) / 1000.0); // Return micro seconds
	}

	public double getHeight() {
		return distanceArray.getMedian();
	}



	/**
	 * Exception thrown when timeout occurs
	 */
	@SuppressWarnings("serial")
	private static class TimeoutException extends Exception {

		private final String reason;

		public TimeoutException(String reason) {
			this.reason = reason;
		}

		@Override
		public String toString() {
			return this.reason;
		}
	}


	//wait duration: 20 ms
	//send height: each second
	//hence send height after 50 readings

	@Override
	public void run() {
		//initialised at -50 to allow the distancesensor to gather some data before sending
		int i = -50;
		while(true){
			try {
				//read the current height
				double currentReading = measureDistance();
				distanceArray.add(currentReading);

				if(i % 50 == 0) {
					//om de 1s: de hoogte doorsturen
					Transfer height = new Transfer();
					height.setHeight(getHeight());
					if(sender!=null){
						sender.sendTransfer(height);
					}
					i = 0;
				}
				i++;
			} catch (TimeoutException e) {
			}
			try {
				Thread.sleep(WAIT_DURATION_IN_MILLIS);
			} catch (InterruptedException e) {
				System.err.println("timeout between readings");
			}
		}}

}
