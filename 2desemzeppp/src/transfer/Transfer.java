package transfer;


import java.io.Serializable;
import javax.swing.ImageIcon;

import zeppelin.Propellor;

public class Transfer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum TransferType{
		IMAGE, HEIGHT, PROPELLOR,MOTOR1,MOTOR2,MOTOR3,DESTINATION;
	}
	
	private String message;
	private TransferType type;
	private double height;
	private ImageIcon image;
	
	public Transfer(){
	
	}

	public TransferType getTransferType(){
		return type;
	}
	
	/**
	 * Get height.
	 */
	public double getHeight(){
		return height;
	}
	
	/**
	 * The height is set as well as the type is set to TransferType.HEIGHT
	 * @param height
	 */
	public void setHeight(double height){
		this.height = height;
		this.type  = TransferType.HEIGHT;
		
	}
	
	/**
	 * Only use this to set Exit type.
	 * @param type
	 */
	public void setType(TransferType type){
		this.type = type;
	}
	
	/**
	 * Send an image through this transfer.
	 * @param image
	 */
	public void setImage(ImageIcon image){
		this.type=TransferType.IMAGE;
		this.image= image;
		
	}
	
	/**
	 * Get image.
	 */
	public ImageIcon getImage(){
		return image;
	}
	
	private Propellor id;
	private Propellor.Mode mode;
	private Propellor.Direction direction;
	private int pwmvalue;
	
	/**
	 * Configures this object to send a propellor update
	 * @param id
	 * @param mode
	 * @param direction
	 * 		only necessary if mode == ON
	 * @param pwmvalue
	 * 		only necessary if mode == PWM
	 */
	public void setPropellor(Propellor id, Propellor.Mode mode, Propellor.Direction direction, int pwmvalue) {
		this.setType(Transfer.TransferType.PROPELLOR);
		
		this.id = id;
		this.mode = mode;
		this.direction = direction;
		this.pwmvalue = pwmvalue;
	}

	public Propellor getPropellorId() {
		return id;
	}

	public Propellor.Mode getPropellorMode() {
		return mode;
	}

	public Propellor.Direction getPropellorDirection() {
		return direction;
	}

	public int getPropellorPwm() {
		return pwmvalue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setMotor1(int pwmvalue){
		this.pwmvalue = pwmvalue;
		this.setType(Transfer.TransferType.MOTOR1);
	}
	
	public void setMotor2(int pwmvalue){
		this.pwmvalue = pwmvalue;
		this.setType(Transfer.TransferType.MOTOR2);
	}
	
	public void setMotor3(int pwmvalue){
		this.pwmvalue = pwmvalue;
		this.setType(Transfer.TransferType.MOTOR3);
	}
	
	private int x;
	private int y;
	
	public void setDestination(int x,int y){
		this.x=x;
		this.y=y;
		this.setType(Transfer.TransferType.DESTINATION);
	}
}
