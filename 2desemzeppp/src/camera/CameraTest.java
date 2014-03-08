package camera;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import transfer.Transfer;
import zeppelin.Main;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import connection.SenderPi;
public class CameraTest implements Runnable{
	SenderPi imageSender;
	public CameraTest(SenderPi imageSender) {
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
				BufferedImage bi = ImageIO.read(new FileInputStream(filePath));
				BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bi)));
				Transfer transfer= new Transfer();
				transfer.setImage(new ImageIcon("QRimage.jpg"));
				Main.getInstance().getSender().sendTransfer(transfer);
				System.out.println("Loading image completed. Reading QR Code");
				@SuppressWarnings("unchecked")
				Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,getMap());

				float[] points=new float[6];
				points[0]=qrCodeResult.getResultPoints()[0].getX()-bi.getWidth()/2;points[3]=(-1*qrCodeResult.getResultPoints()[0].getY())-bi.getHeight()/2;
				points[1]=qrCodeResult.getResultPoints()[1].getX()-bi.getWidth()/2;points[4]=(-1*qrCodeResult.getResultPoints()[1].getY())-bi.getHeight()/2;
				points[2]=qrCodeResult.getResultPoints()[2].getX()-bi.getWidth()/2;points[5]=(-1*qrCodeResult.getResultPoints()[2].getY())-bi.getHeight()/2;
				System.out.println("Point 1: "+points[0]+","+points[3]);
				System.out.println("Point 2: "+points[1]+","+points[4]);
				System.out.println("Point 3: "+points[2]+","+points[5]);

				float[] qrMiddlePoint={(points[2]+points[0])/2,(points[5]+points[3])/2};
				double clockwise=0;
				double deg= Math.atan((qrMiddlePoint[1])/(qrMiddlePoint[0]));
				if(deg>0){
					if(qrMiddlePoint[0]<0){
						clockwise =0;
						deg= Math.PI/2+deg;
					}
					else{
						deg=(Math.PI/2)-deg;
						clockwise=1;
					}
				}
				else{
					if(qrMiddlePoint[0]<0){
						clockwise =0;
						deg= Math.PI/2+deg;
					}
					else{
						deg=(Math.PI/2)-deg;
						clockwise=1;
					}
				}

				double degree[]= {deg,
						Math.sqrt((qrMiddlePoint[0])*(qrMiddlePoint[0])
								+(qrMiddlePoint[1])*(qrMiddlePoint[1])),clockwise};
				float[] qrHorizontalMPoint={(points[1]+points[0])/2,(points[4]+points[3])/2};
				double toReturn =Math.atan((qrHorizontalMPoint[0]-points[1])/(qrHorizontalMPoint[1]-points[4]));				

				System.out.println("Angle: "+(degree[0]*180/Math.PI) +
						"DistancePixels: "+ degree[1]+
						"Clockwise:"+ degree[2] +
						"\nOrientation: "+ (toReturn*180/Math.PI));
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