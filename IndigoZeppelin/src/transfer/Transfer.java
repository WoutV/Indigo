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
		IMAGE, HEIGHT, KEYPRESSEDEVENT, KEYRELEASEDEVENT, EXIT , MESSAGE, PWM, PWMTOGGLE, PROPELLOR
	}
	private TransferType type;
	private double height;
	private Key keyevent;
	private String message;
	private ImageIcon image;
	public Transfer(){
	
	}

	public TransferType getTransferType(){
		return type;
	}
	
	/**
	 * 
	 * @return	Returns Height 
	 */
	public double getHeight(){
		return height;
	}
	
	/**
	 * The height is set aswell as the type is set to TransferType.HEIGHT
	 * @param height
	 */
	public void setHeight(double height){
		this.height = height;
		this.type  = TransferType.HEIGHT;
		message = "Transfering height details";
	}
	
	private int pwm;
	
	public int getPwm() {
		return pwm;
	}
	
	public void setPwm(int pwm) {
		this.type = TransferType.PWM;
		this.pwm = pwm;
		message = "Transferring float pwm supplied by user";
	}
	
	
	public void searchPwm() {
		this.type = TransferType.PWMTOGGLE;
	}
	
	
	private PropellorUpdate propupd;
	
	/**
	 * Sets this Transfer to send a propellor update
	 * @param propupd
	 * 		the propellor update to be transferred
	 */
	public void setPropellor(PropellorUpdate propupd) {
		this.propupd = propupd;
		message = "Transferring propellor update";
	}
	
	public PropellorUpdate getPropellor() {
		return propupd;
	}
	
	
	/**
	 * Sets the keyevent to given keyevent as well as the type. 
	 * @param keyevent
	 * @param type
	 */
	public void setKeyEvent(Key key, TransferType type){
		this.keyevent = key;
		this.type = type;
		message = "Transfering keyevent type: " + key.toString() ;
	}
	
	public Key getKey(){
		return keyevent;
	}
	
	
	/**
	 * Only use this to set Exit type.
	 * @param type
	 */
	public void setType(TransferType type){
		this.type = type;
	}
	
	
	public void setMessage(String message){
		this.type= TransferType.MESSAGE;
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	

	public void setImage(ImageIcon image){
		this.type=TransferType.IMAGE;
		this.image= image;
		this.message= "Sending Image";
	}
	
	public ImageIcon getImage(){
		return image;
	}
	
	/**
	 * Enum die de verschillende keys aangeeft.
	 */
	public enum Key{
		LEFT,RIGHT,UP,DOWN,ELEVATE;
	}
	
}
