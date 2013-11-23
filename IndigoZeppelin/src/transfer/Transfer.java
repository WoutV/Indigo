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
		IMAGE, HEIGHT, KEYPRESSEDEVENT, KEYRELEASEDEVENT, EXIT , MESSAGE, PWM, PWMTOGGLE, PROPELLOR,FLYMODE, COMMAND,
		QRCODE;
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
	
	private Propellor id;
	private Propellor.Mode mode;
	private Propellor.Direction direction;
	private int pwmvalue;
	public boolean isAutoPilot() {
		return autoPilot;
	}

	private boolean autoPilot;

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
		message = "Transferring propellor update";
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
	
	public void setMode(boolean autoPilot){
		this.autoPilot=autoPilot;
		this.setType(TransferType.FLYMODE);
	}
	
	public void setCommand(String message){
		this.setType(TransferType.COMMAND);
		this.setMessage(message);
	}
	
	public void setQRCode(String txt) {
		this.setType(TransferType.QRCODE);
		this.setMessage(message);
	}
}
