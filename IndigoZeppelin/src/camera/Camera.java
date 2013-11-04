package camera;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
public abstract class Camera {
	/**
	 * Takes a low resolution picture and gives its imageicon.
	 * @return
	 */
	
	private static boolean isInUse;
	
	public static ImageIcon getImage(){
		if(isInUse){
			return new ImageIcon("image.jpg");
		}
		try
	    {

	    	isInUse=true;
			Process p = Runtime.getRuntime().exec("raspistill -t 0 -n -h 300 -w 300 -o image.jpg");
	    	p.waitFor();
	    	isInUse=false;
	    }
		catch (Exception ieo)
	    {
	      ieo.printStackTrace();
	    }
		return new ImageIcon("image.jpg");
	}
	/**
	 * Takes a high resolution photo and looks for the qr code. Returns the string if found otherwise null 
	 * @return
	 * @throws FileNotFoundException
	 * 			never throws its
	 * @throws IOException
	 * 			if there is a problem reading the file
	 * @throws NotFoundException
	 * 			if it cant find any qr codes
	 */
	public static String readQRCode()
		throws FileNotFoundException, IOException, NotFoundException {
		boolean printed =false;
		while(isInUse){
			if(!printed){
			System.out.println("Camera is in use by getImage() waiting for it to finish");
			printed =true;
			}
		}
		  try
		    {
			  	System.out.println("Taking picture for QR code reading!");
			  	isInUse=true;
		    	Process p = Runtime.getRuntime().exec("raspistill -cfx 128:128 -t 0 -h 1000 -w 2000 -o QRimage.jpg");
		    	p.waitFor();
		    	isInUse=false;
		    	System.out.println("Taking picture finished...");
		    }
		    catch (Exception ieo)
		    {
		      ieo.printStackTrace();
		    }
		String filePath = "QRimage.jpg";
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
		System.out.println("Loading image completed. Reading QR Code");
		try{
		@SuppressWarnings("unchecked")
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,getMap());
		return qrCodeResult.getText();
		}catch(NotFoundException ex){
			return "No QR code Found";
		}
		
		}
	@SuppressWarnings("rawtypes")
	private static Map getMap(){
		Map<EncodeHintType, ErrorCorrectionLevel>hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		return hintMap;
	}
}
