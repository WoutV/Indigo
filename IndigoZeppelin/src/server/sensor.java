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
		fileContent = "Duration(ms)    ; Distance(cm)\n";
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
				System.out.println("Duration motors running BACKWARD in percent:");
				try {
					percent = Long.parseLong(br.readLine());
					System.out.println("Percent:"+percent);
					durationRead = true;
				} catch(Exception e){
					System.out.println("Cannot Parse Duration try again");
				}
			}
			for(int i=1;i<=times;i++){
				goForward(motorDuration);		
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
					fileContent += ""+motorDuration +"    ; "+distanceTravelled+"\n";
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Run Again?(1/0)");
			try {
				again = Integer.parseInt(br.readLine());
			} catch (Exception e) {
				e.printStackTrace();
			}


		}
		try {
			PrintStream out = new PrintStream(new FileOutputStream("DataHorizontalMovement.txt"));
			out.println("Info About File: \n"+ fileContent);
			out.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}


	}
	public void goForward(int duration){
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

}