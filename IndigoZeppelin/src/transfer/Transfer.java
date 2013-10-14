package transfer;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class Transfer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum TransferType{
		IMAGE, HEIGHT, KEYPRESSEDEVENT, KEYRELEASEDEVENT, EXIT , MESSAGE
	}
	private TransferType type;
	private double height;
	private KeyEvent keyevent;
	private String message;
	public Transfer(){
	
	}

	public TransferType getTransferType(){
		return type;
	}
	public KeyEvent getKeyEvent(){
		return keyevent;
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
	
	/**
	 * Sets the keyevent to given keyevent as well as the type. 
	 * @param keyevent
	 * @param type
	 */
	public void setKeyEvent(KeyEvent keyevent, TransferType type){
		this.keyevent = keyevent;
		this.type = type;
		message = "Transfering keyevent type: " +keyevent.getKeyCode() ;
	}
	
	public String getMessage(){
		return message;
	}
	
	/**
	 * Only use this to set Exit type.
	 * @param type
	 */
	public void setType(TransferType type){
		this.type = type;
	}
	
	/**
	 * 
	 */
	public void setMessage(String message){
		this.type= TransferType.MESSAGE;
		this.message = message;
	}

	
}
