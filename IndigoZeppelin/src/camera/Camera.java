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
	
	public static ImageIcon getImage(){
		try
	    {

	    	Process p = Runtime.getRuntime().exec("raspistill -cfx 128:128 -t 0 -n -h 100 -w 200 -o image.jpg");
	    	p.waitFor();
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
		  try
		    {

		    	Process p = Runtime.getRuntime().exec("raspistill -cfx 128:128 -t 0 -h 1000 -w 2000 -o QRimage.jpg");
		    	p.waitFor();
		    }
		    catch (Exception ieo)
		    {
		      ieo.printStackTrace();
		    }
		String filePath = "QRimage.jpg";
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
		@SuppressWarnings("unchecked")
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,getMap());
		return qrCodeResult.getText();
		}
	@SuppressWarnings("rawtypes")
	private static Map getMap(){
		Map<EncodeHintType, ErrorCorrectionLevel>hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		return hintMap;
	}
}
