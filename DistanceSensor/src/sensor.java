/**
 * Class to monitor distance measured by an HC-SR04 distance sensor on a 
 * Raspberry Pi.
 * 
 * The main method assumes the trig pin is connected to the pin # 7 and the echo
 * pin is connected to pin # 11.  Output of the program are comma separated lines
 * where the first value is the number of milliseconds since unix epoch, and the
 * second value is the measured distance in centimeters.
 */

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 * sensor class to monitor distance measured by sensor
 * 
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public class sensor {
    
    private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s
    
    private static int TRIG_DURATION_IN_MICROS; // trigger duration of 10 micro s
    private static int WAIT_DURATION_IN_MILLIS; // wait 60 milli s

    private final static int TIMEOUT = 2100;
    
    private final static GpioController gpio = GpioFactory.getInstance();
    
    private final GpioPinDigitalInput echoPin;
    private final GpioPinDigitalOutput trigPin;
            
    private sensor( Pin echoPin, Pin trigPin ) {
        this.echoPin = gpio.provisionDigitalInputPin( echoPin );
        this.trigPin = gpio.provisionDigitalOutputPin( trigPin );
        this.trigPin.low();
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
        
        return duration * SOUND_SPEED / ( 2 * 10000 );
    }

    /**
     * Put a high on the trig pin for TRIG_DURATION_IN_MICROS
     */
    private void triggerSensor() {
        try {
            this.trigPin.high();
            Thread.sleep( 0, TRIG_DURATION_IN_MICROS * 1000 );
            this.trigPin.low();
        } catch (InterruptedException ex) {
            System.err.println( "Interrupt during trigger" );
        }
    }
    
    /**
     * Wait for a high on the echo pin
     * 
     * @throws sensor.TimeoutException if no high appears in time
     */
    private void waitForSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        
        while( this.echoPin.isLow() && countdown > 0 ) {
            countdown--;
        }
        
        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal start" );
        }
    }
    
    /**
     * @return the duration of the signal in micro seconds
     * @throws sensor.TimeoutException if no low appears in time
     */
    private long measureSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while( this.echoPin.isHigh() && countdown > 0 ) {
            countdown--;
        }
        long end = System.nanoTime();
        
        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal end" );
        }
        
        return (long)Math.ceil( ( end - start ) / 1000.0 );  // Return micro seconds
    }
    
    public static void main( String[] args ) {
        Pin echoPin = RaspiPin.GPIO_12; // PI4J custom numbering (pin 11)
        Pin trigPin = RaspiPin.GPIO_14; // PI4J custom numbering (pin 7)
        sensor monitor = new sensor( echoPin, trigPin );
        Pin upfw;
        Pin uprv;
        int programDuration;
        int actualDistance;
        ArrayList<Float> distances = new ArrayList();
        try {
        	 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             System.out.println("Trigger duration in microsecond:");
             TRIG_DURATION_IN_MICROS = Integer.parseInt(br.readLine());
             System.out.println("Wait duration in millisecond:");
             WAIT_DURATION_IN_MILLIS = Integer.parseInt(br.readLine());
             System.out.println("Duration of program running in Milliseconds:");
             programDuration = Integer.parseInt(br.readLine());
             System.out.println("Actual Distance in cm:");
             actualDistance = Integer.parseInt(br.readLine());
          
      
        
             PrintStream out = new PrintStream(new FileOutputStream("Trig_"+TRIG_DURATION_IN_MICROS+"usWait_"+WAIT_DURATION_IN_MILLIS+"msDistance"+actualDistance+".txt"));
 			 out.println("Info About File: \n" +
 			 		"Trigger duration in microsecond:" + TRIG_DURATION_IN_MICROS+
 			 				"\nWait duration in millisecond:" + WAIT_DURATION_IN_MILLIS +
 			 				"\nDuration of program running in Milliseconds:"+programDuration +
 			 				"\nActual Distance in cm: "+ actualDistance);
 			 long startTime = System.currentTimeMillis();
 			 while((System.currentTimeMillis() - startTime) < programDuration ) {
 	            try {
 	            	float distance = monitor.measureDistance();
 	                out.println(distance);
 	                System.out.println(distance);
 	                
 	                distances.add(distance);
 	                
 	            }
 	            catch( TimeoutException e ) {
 	                System.err.println( e );
 	            }

 	            try {
 	                Thread.sleep( WAIT_DURATION_IN_MILLIS );
 	            } catch (InterruptedException ex) {
 	                System.err.println( "Interrupt during trigger" );
 	            }
 	        }
 			float mean=0;
 			for(int i = 0 ; i < distances.size();i++){
 				mean+=distances.get(i);
 			}
 			mean = mean/distances.size();
 			System.out.println("Mean:"+mean + "cm");
 			out.println("Mean:"+mean + "cm");
 			
 			out.close();
 			

        
        } catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        
        
    }

    /**
     * Exception thrown when timeout occurs
     */
    private static class TimeoutException extends Exception {

        private final String reason;
        
        public TimeoutException( String reason ) {
            this.reason = reason;
        }
        
        @Override
        public String toString() {
            return this.reason;
        }
    }
    
}