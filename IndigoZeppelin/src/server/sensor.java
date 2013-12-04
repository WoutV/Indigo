package server;
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

import zeppelin.MotorController;

import com.pi4j.component.motor.Motor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class sensor implements Runnable{

	String fileContent;
	double percent;
	@Override
	public void run() {

		int motorDuration=0;
		double distanceTravelled=0;
		int again =1;
		boolean durationRead,distanceRead; 
		fileContent = "Direction	;Duration(ms)    ; Distance(cm)\n";
		int times=1;

		while(again==1){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			durationRead=false;
			while(!durationRead){
				System.out.println("Times to run");
				try {
					times = Integer.parseInt(br.readLine());
					durationRead = true;
				} catch(Exception e){
					System.out.println("Cannot Parse time try again");
				}
			}

			durationRead=false;
			String direction = null;
			while(!durationRead){
				System.out.println("Direction(F/B/L/R)");
				try {
					direction = (br.readLine());
					System.out.println("Direction:"+direction);
					durationRead = true;
				} catch(Exception e){
					System.out.println("Cannot Parse Direction try again");
				}
			}
			
			durationRead=false;
			while(!durationRead){
				System.out.println("Duration motors running in Milliseconds:");
				try {
					motorDuration = Integer.parseInt(br.readLine());
					durationRead = true;
				} catch(Exception e){
					System.out.println("Cannot Parse Duration try again");
				}
			}
			durationRead=false;
			while(!durationRead){
				System.out.println("Duration motors running OPPOSITE in percent:");
				try {
					percent = Long.parseLong(br.readLine());
					System.out.println("Percent:"+percent);
					durationRead = true;
				} catch(Exception e){
					System.out.println("Cannot Parse Duration try again");
				}
			}
			durationRead=false;
			long pause=0;
			while(!durationRead){
				System.out.println("Duration of pause between runs:");
				try {
					pause = Long.parseLong(br.readLine());
					System.out.println("Pause Duration:"+pause);
					durationRead = true;
				} catch(Exception e){
					System.out.println("Cannot Parse Duration try again");
				}
			}
			
			for(int i=1;i<=times;i++){
				switch(direction){
				case "F":
					goForward(motorDuration);
					break;
				case "B":
					goBackward(motorDuration);
					break;
				case "L":
					turnLeft(motorDuration);
					break;
				case "R":
					turnRight(motorDuration);
				}
				try {
					Thread.sleep(pause);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			distanceRead=false;
			while(!distanceRead){
				System.out.println("Distance Travelled?");
				try {
					distanceTravelled=Double.parseDouble(br.readLine());
					distanceRead=true;
				} catch (Exception e) {
					System.out.println("Error Reading Distance Try Again");
				}
			}
			int valid=0;
			System.out.println("Test Valid?(1/0)");
			try {
				valid = Integer.parseInt(br.readLine());
				if(valid == 1)
					fileContent += ""+direction+","+motorDuration +"    ; "+distanceTravelled+"\n";
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Run Again?(1/0)");
			try {
				again = Integer.parseInt(br.readLine());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				int i =0;
				PrintStream out = new PrintStream(new FileOutputStream("DataHorizontalMovement"+i+".txt"));
				i++;
				out.println("Info About File: \n"+ fileContent);
				out.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

		}
		


	}
	public void goForward(int duration){
		try {
			MotorController.getInstance().moveForward();
			Thread.sleep(duration);
			MotorController.getInstance().stopHorizontalMovement();
			MotorController.getInstance().moveBackward();
			long Durr =  (long) ((percent/100l)*(double)duration);
			Thread.sleep(Durr);
			System.out.println(Durr);
			MotorController.getInstance().stopHorizontalMovement();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void goBackward(int duration){
		try {
			MotorController.getInstance().moveBackward();
			Thread.sleep(duration);
			MotorController.getInstance().stopHorizontalMovement();
			MotorController.getInstance().moveForward();
			long Durr =  (long) ((percent/100l)*(double)duration);
			Thread.sleep(Durr);
			System.out.println(Durr);
			MotorController.getInstance().stopHorizontalMovement();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void turnLeft(int duration){
		
		try {
			MotorController.getInstance().turnLeft();
			Thread.sleep(duration);
			MotorController.getInstance().stopHorizontalMovement();
			MotorController.getInstance().turnRight();
			long durr= (long) ((percent/100l)* (double)duration);
			Thread.sleep(durr);
			MotorController.getInstance().stopHorizontalMovement();
			System.out.println("Wander is gay");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void turnRight(int duration){
		
		try {
			MotorController.getInstance().turnRight();
			Thread.sleep(duration);
			MotorController.getInstance().stopHorizontalMovement();
			MotorController.getInstance().turnLeft();
			long durr= (long) ((percent/100l)* (double)duration);
			Thread.sleep(durr);
			MotorController.getInstance().stopHorizontalMovement();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}