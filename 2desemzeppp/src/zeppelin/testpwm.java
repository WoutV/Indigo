package zeppelin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.SoftPwm;

public class testpwm {
	private static GpioPinDigitalOutput xforwardPin;
	private static GpioPinDigitalOutput xreversePin;
	private static GpioPinDigitalOutput yforwardPin;
	private static GpioPinDigitalOutput yreversePin;
	private static Motor up;
	private static Motor xMotor;
	private static Motor yMotor;
	private static GpioController gpiocontroller = GpioFactory.getInstance();
	private static Pin xfw = RaspiPin.GPIO_11, xrv = RaspiPin.GPIO_13, yfw = RaspiPin.GPIO_07, yrv = RaspiPin.GPIO_05;
	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		int exit= 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		xforwardPin = gpiocontroller.provisionDigitalOutputPin(xfw,"forward");
		xreversePin = gpiocontroller.provisionDigitalOutputPin(xrv,"backward");
		yforwardPin = gpiocontroller.provisionDigitalOutputPin(yfw,"forward");
		yreversePin = gpiocontroller.provisionDigitalOutputPin(yrv,"backward");
		while(exit!=1){
				System.out.println("Give softpwm number:");
				int pwmNumber = Integer.parseInt(br.readLine());
				SoftPwm.softPwmCreate(pwmNumber, 0, 100);
				Thread.sleep(1000);
				SoftPwm.softPwmWrite(pwmNumber, 100);
				Thread.sleep(2000);
				SoftPwm.softPwmWrite(pwmNumber, 0);
			}
			System.out.println("exit? (1/0)");
			exit = Integer.parseInt(br.readLine());
		
	}
}
