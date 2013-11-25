package zeppelin;
import java.util.HashMap;

import server.SendToClient;
import transfer.Transfer;
import zeppelin.utils.Pid;
import zeppelin.utils.Pid2;
import zeppelin.utils.Pid3;
import zeppelin.utils.ZoekZweefPwm;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import command.Command;

/**
 * Controller voor de drie motoren.
 * !!!! right: motor 1 in doc (24,4)
 * 		up: motor 2 in doc (17,23)	
 * 		left : motor 3 in doc (9,7)
 * Usage: getInstance(), daarna: init met een GpioController
 */
public class MotorController {
	private Motor up;
	private Motor left;
	private Motor right;

	private Pin leftfw = RaspiPin.GPIO_13, leftrv = RaspiPin.GPIO_11, upfw = RaspiPin.GPIO_00,
			uprv = RaspiPin.GPIO_04, rightfw = RaspiPin.GPIO_05, rightrv = RaspiPin.GPIO_07;


	private GpioController gpiocontroller;
	private DistanceSensor distanceSensor;
	
	private int zweefpwm =848;
	
	private SendToClient sender;

	private static MotorController mc = new MotorController();
	private CommandExecutioner commandParser;
	
	private HeightController hc;
	private CommandController cc;
	
	
	public double Kp=(1024-zweefpwm)/100;;
	public double Kd=70;
	public double Ki=0.3;
	
	

	private MotorController() {
	}

	/**
	 * Geeft de enige instantie van deze singleton klasse terug
	 * Motors zijn hierin nog niet geinitialiseerd
	 * Hierna moet init() worden opgeroepen
	 */
	public static MotorController getInstance() {
		return mc;
	}

	/**
	 * Initialiseert de motoren.
	 * Mag maar een keer opgeroepen worden.
	 * GpioController moet worden meegegeven (nodig voor de motoren)
	 */
	public void init(GpioController gpio,DistanceSensor distanceSensor,SendToClient sender) {
		System.out.println("Initializing Motor before if test");
		if(gpiocontroller == null) {
			gpiocontroller = gpio;
			this.distanceSensor = distanceSensor;
			this.sender = sender;

			GpioPinPwmOutput pwm = gpiocontroller.provisionPwmOutputPin(RaspiPin.GPIO_01,"pwm");
			//init Motors
			System.out.println("Initializing Motor");
			left = new Motor(leftfw,leftrv,gpiocontroller,Propellor.LEFT,pwm,sender);
			left.setOff();
			right = new Motor(rightfw,rightrv,gpiocontroller,Propellor.RIGHT, pwm, sender);
			right.setOff();
			up = new Motor(upfw,uprv,gpiocontroller,Propellor.UP, pwm, sender);
			up.setOff();
			up.PwmOn();
			hc = new HeightController(Kp, Ki, Kd, zweefpwm, distanceSensor, up);
			Thread hct = new Thread(hc);
			hct.start();
			cc = new CommandController();
			Thread ctt = new Thread(cc);
			ctt.start();
		}
	}

	public void moveForward() {
		left.setForward();
		right.setForward();
	}

	public void moveBackward() {
		left.setReverse();
		right.setReverse();
	}

	public void turnLeft() {
		right.setForward();
		left.setReverse();
	}

	public void turnRight() {
		right.setReverse();
		left.setForward();
	}

	//dit gaat wss worden vervangen zodat de kracht kan worden ingesteld
	public void elevate() {
		hc.stop();
		up.setPwmValue(1024);
		up.setForward();
	}
	
	
	
	/**
	 * Laat de zeppelin naar een bepaalde hoogte bewegen
	 * Deze methode gebruikt het PID-algoritme
	 * @param dest
	 * 		hoogte (in cm) waar naartoe moet worden bewogen
	 */
	public void moveToHeight(double dest) {
		hc.moveToHeight(dest);
	}

	/**
	 * Zet alle horizontale bewegingen (links draaien, rechts draaien, voorwaarts, achterwaarts) stop.
	 */
	public void stopHorizontalMovement() {
		left.setOff();
		right.setOff();
	}

	/**
	 * Zet verticale bewegingen stop (concreet wordt overgeschakeld naar zweef-pwm)
	 */
	public void stopElevate() {
		hc.stop();
		up.setOff();
	}	
	
	private ZoekZweefPwm zoekZweefPwm;
	private Thread zoekZweefPwmThread;
	
	/**
	 * Stopt het automatisch zoeken naar zweef pwm
	 * en zet zweef pwm op de gegeven value
	 * @param pwm
	 */
	public void setFloatPwm(int pwm) {
		if(zoekZweefPwm != null) {
			zoekZweefPwm.stop();
			
		}
		up.setPwmValue(pwm);
		zweefpwm = pwm;
	}
	
	public void setZweefPwm(int pwm){
		up.setPwmValue(pwm);
		zweefpwm = pwm;
	}
	
	/**
	 * Indien niet aan het zoeken: gaat op zoek naar zweef-pwm
	 * Indien aan het zoeken: stopt
	 */
	public void searchFloatPwm() {
		//laat de ZweefZoeker automatisch de zweef pwm zoeken
		if(zoekZweefPwmThread!= null && zoekZweefPwmThread.isAlive()){
			zoekZweefPwm.stop();
		}
		
		zoekZweefPwm = new ZoekZweefPwm(distanceSensor,up,this);
		zoekZweefPwmThread = new Thread(zoekZweefPwm);
		zoekZweefPwmThread.start();
		
					
	}
	
	public void changeFlyMode(boolean autoPilot){
		if(autoPilot==true){
			if(!cc.isAutoPilot()){
				hc.startRunning();
				cc.setAutoPilot(true);
				}
		}else if(autoPilot==false){
			if(cc.isAutoPilot()){
				
				cc.setAutoPilot(false);
			}
		}
	}
	
	public void addToCommandList(Command command){
		cc.getCommandList().add(command);
		updateCommandList();
	}
	
	public void setCommandIsBeingExecuted(boolean execute){
		cc.setCommandIsBeingExecuted(execute);
		
	}
	
	public void updateCommandList(){
		Transfer transfer  = new Transfer();
		String fullList = "<HTML>\n";
		for(Command command :cc.getCommandList()){
			fullList=fullList + command.toString() + "<BR>";
		}
		fullList = fullList + "</HTML>";
		transfer.setCommand(fullList);
		sender.sendTransfer(transfer);
	}
	
	
}
