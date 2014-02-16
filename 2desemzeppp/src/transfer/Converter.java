package transfer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Converter {

	public static byte[] toBytes(Transfer transfer) {
	      byte[]bytes; 
	      ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
	      try{ 
	        ObjectOutputStream oos = new ObjectOutputStream(baos); 
	        oos.writeObject(transfer); 
	        oos.flush();
	        oos.reset();
	        bytes = baos.toByteArray();
	        oos.close();
	        baos.close();
	      } catch(IOException e){ 
	        bytes = new byte[] {};
	        System.out.println("Unable to write to output stream"); 
	      }         
	      return bytes; 
	    }
	public static Transfer fromBytes(byte[] body) {
		Transfer obj = null;
		try {
	      ByteArrayInputStream bis = new ByteArrayInputStream (body);
	      ObjectInputStream ois = new ObjectInputStream (bis);
	      obj = (Transfer)ois.readObject();
	      ois.close();
	      bis.close();
		}
		catch (IOException e) {
		      e.printStackTrace();
		}
		catch (ClassNotFoundException ex) {
		      ex.printStackTrace();
		}
		  return obj;     
	}
	
/*
 * Wou kortere code schrijven... failed :p	
 */
//	public static byte[] serialize(Transfer obj) throws IOException {
//	    ByteArrayOutputStream out = new ByteArrayOutputStream();
//	    ObjectOutputStream os = new ObjectOutputStream(out);
//	    os.writeObject(obj);
//	    return out.toByteArray();
//	}
//	public static Transfer deserialize(byte[] data) throws IOException, ClassNotFoundException {
//	    
//	    Transfer transfer = null;
//	    try{
//	    	ByteArrayInputStream in = new ByteArrayInputStream(data);
//		    ObjectInputStream is = new ObjectInputStream(in);
//	    	transfer= (Transfer) is.readObject();
//	    }catch(IOException e){
//	    	System.out.println("derp");
//	    }catch(ClassNotFoundException e){
//	    	System.out.println("doublederp");
//	    }
//	    return transfer;
//	}
}
