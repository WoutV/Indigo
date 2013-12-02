package camera;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import server.SendToClient;
import transfer.Transfer;
import zeppelin.Main;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
public class CameraTest implements Runnable{
	SendToClient imageSender;
	public CameraTest(SendToClient imageSender) {
		this.imageSender=imageSender;
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
	@Override
	public void run() {

		int runAgain = 1;
		while(runAgain==1){
			try
			{
				System.out.println("Give the image height:");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				int height= Integer.parseInt(br.readLine());
				System.out.println("Give the  image width:");
				int width= Integer.parseInt(br.readLine());
				long time = System.currentTimeMillis();
				System.out.println("Taking picture for QR code reading!");
				Process p = Runtime.getRuntime().exec("raspistill -t 0 -h "+height+" -w "+width+" -o QRimage.jpg");
				p.waitFor();
				System.out.println("Taking picture finished...");
				System.out.println("Time taken to take picture :"+ (System.currentTimeMillis()-time));
				String filePath = "QRimage.jpg";
				BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(filePath)))));
				Transfer transfer= new Transfer();
				transfer.setImage(new ImageIcon("QRimage.jpg"));
				Main.getInstance().getSender().sendTransfer(transfer);
				System.out.println("Loading image completed. Reading QR Code");
				@SuppressWarnings("unchecked")
				Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,getMap());
				long finishTime = System.currentTimeMillis();
				System.out.println("Time taken to take picture and decode :"+ (finishTime-time));
				System.out.println(qrCodeResult.getText());
				System.out.println("Run Again?(1/0)");
				runAgain = Integer.parseInt(br.readLine());
			}
			catch (Exception ieo)
			{
				ieo.printStackTrace();
			}
		}





	}
}