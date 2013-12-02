
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
public class QRCode {
	public static void main(String[] args) throws WriterException, IOException,NotFoundException {
		try
		{
			System.out.println("Give the height:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int height= Integer.parseInt(br.readLine());
			System.out.println("Give the width:");
			int width= Integer.parseInt(br.readLine());
			long time = System.currentTimeMillis();
			System.out.println("Taking picture for QR code reading!");
			Process p = Runtime.getRuntime().exec("raspistill -t 0 -h "+height+" -w "+width+" -o QRimage.jpg");
			p.waitFor();
			System.out.println("Taking picture finished...");
			System.out.println("Time taken to take picture :"+ (time - System.currentTimeMillis()));
			String filePath = "QRimage.jpg";
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
			System.out.println("Loading image completed. Reading QR Code");
			@SuppressWarnings("unchecked")
			Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,getMap());
			long finishTime = System.currentTimeMillis();
			System.out.println("Time taken to take picture and decode :"+ (time - finishTime));
			System.out.println(qrCodeResult.getText());
		}
		catch (Exception ieo)
		{
			ieo.printStackTrace();
		}
		

	}
	/***
	 * For the things that i do not understand.
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Map getMap(){
		Map<EncodeHintType, ErrorCorrectionLevel>hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		return hintMap;
	}
}