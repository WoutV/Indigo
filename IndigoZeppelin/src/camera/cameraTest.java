package camera;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
public class cameraTest {

	 public static void main(String[] args) throws FileNotFoundException, NotFoundException, IOException
	  {
	    System.out.println("START PROGRAM");
	    try
	    {

	    	Process p = Runtime.getRuntime().exec("raspistill -h 200 -w 300 -o image.png");
	    	p.waitFor();
	    }
	    catch (Exception ieo)
	    {
	      ieo.printStackTrace();
	    }
	    
	    
	    //Reading the qr code
	    String filePath = "image.png"; 
		String charset = "UTF-8";// or "ISO-8859-1"
		Map<EncodeHintType, ErrorCorrectionLevel>hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		System.out.println("QR Code image created successfully!");
		System.out.println("Data read from QR Code: "+ readQRCode(filePath, charset, hintMap));
		}
	 
	public static String readQRCode(String filePath, String charset, Map hintMap)
		throws FileNotFoundException, IOException, NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,hintMap);
		return qrCodeResult.getText();
		}
}
