package zeppelin;

import server.SendToClient;
import transfer.Transfer;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import command.*;

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

	private Pin leftfw = RaspiPin.GPIO_11, leftrv = RaspiPin.GPIO_13, upfw = RaspiPin.GPIO_00,
			uprv = RaspiPin.GPIO_04, rightfw = RaspiPin.GPIO_07, rightrv = RaspiPin.GPIO_05;


	private GpioController gpiocontroller;


	
	private SendToClient sender;
	private static MotorController mc = new MotorController();
	private HeightController hc;
	private CommandController cc;
	private DistanceSensor ds;
	
	
	public double Kp=30;
	public double Kd=90;
	public double Ki=2;
	
	

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
			this.sender = sender;
			ds = distanceSensor;

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
			hc = new HeightController(Kp, Ki, Kd, distanceSensor, up);
			Thread hct = new Thread(hc);
			System.out.println("Starting heightController Thread ");
			hct.start();
			System.out.println("HeightController Thread Started");
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
		right.setReverse();
		left.setForward();
	}

	public void turnRight() {
		left.setReverse();
		right.setForward();
	}

	/**
	 * Move up at full pwm.
	 * Automatic height control is shut down!
	 */
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
		right.setOff();
		left.setOff();
		
		
	}

	/**
	 * Zet verticale bewegingen stop (concreet wordt overgeschakeld naar zweef-pwm)
	 */
	public void stopElevate() {
		hc.stop();
		up.setOff();
	}	
	


	
	/**
	 * Stopt het automatisch zoeken naar zweef pwm
	 * en zet zweef pwm op de gegeven value
	 * @param pwm
	 */
	public void setFloatPwm(int pwm) {
//		if(zoekZweefPwm != null) {
//			zoekZweefPwm.stop();
//			
//		}
//		up.setPwmValue(pwm);
//		zweefpwm = pwm;
		System.out.println("buuuuuurrrrrr");
		MoveForward command = new MoveForward(pwm);
		addToCommandList(command);
	}
	

	
	
	public void changeFlyMode(boolean autoPilot){
		if(autoPilot==true){
			if(!cc.isAutoPilot()){
				//heightcontroller aanzetten (eventueel afgezet door een elevate)
				//de hoogte die moet behouden blijven, is de huidige hoogte van de zeppelin
				hc.moveToHeight(ds.getHeight());
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
		cc.addCommand(command);
		updateCommandList();
	}
	
	public void setCommandIsBeingExecuted(boolean execute){
		cc.setCommandIsBeingExecuted(execute);
		
	}
	
	public void updateCommandList(){
		Transfer transfer  = new Transfer();
		String fullList = "<HTML>";
		for(Command command :cc.getCommandList()){
			fullList=fullList + command.toString() + "<BR>";
		}
		fullList = fullList + "</HTML>";
		transfer.setCommand(fullList);
		sender.sendTransfer(transfer);
	}
	
	
}
