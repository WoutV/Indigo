package camera;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import zeppelin.Main;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
public abstract class Camera {

	private static boolean isInUse;

	/**
	 * Takes a low resolution picture and gives its imageicon.
	 * @return
	 */
	public static ImageIcon getImage(){
		if(isInUse){
			return new ImageIcon("image.jpg");
		}
		try
		{

			isInUse=true;
			Process p = Runtime.getRuntime().exec("raspistill -t 0 -n -h 150 -w 150 -o image.jpg");
			p.waitFor();
			isInUse=false;
		}
		catch (Exception ieo)
		{
			ieo.printStackTrace();
		}
		return new ImageIcon("image.jpg");
	}
	private static boolean lastQRused=true;
	private static float[] points= new float[6];
	private static BufferedImage bi;
	private static double fotoHeight;
	/**
	 * 
	 * @return
	 * 			Returns double{which angle the qr code is situated, how much distance,degrees clockwise(1/0)}
	 * @throws NotFoundException
	 * 			When the qr code is not found;
	 * @throws FileNotFoundException
	 * 			Normally not thrown;
	 * @throws IOException
	 * 			If there is something wrong while reading the file;
	 */
	public static double[] getDegreeAndDistanceQR() throws NotFoundException, FileNotFoundException, IOException{
		if(lastQRused){
			readQRCode();
		}
		else{
			lastQRused=true;
		}
		float[] qrMiddlePoint={(points[2]+points[0])/2,(points[5]+points[3])/2};
		double clockwise=0;
		double deg= Math.atan((-1*qrMiddlePoint[0])/(qrMiddlePoint[1]));

		if(qrMiddlePoint[0]<0){
			clockwise =0;
			deg= Math.PI/2+deg;
		}
		else{
			deg=Math.PI/2-deg;
			clockwise=1;
		}
		double distance=0;
		if(bi.getHeight()==600){
			distance = Math.sqrt((qrMiddlePoint[0])*(qrMiddlePoint[0])
					+(qrMiddlePoint[1])*(qrMiddlePoint[1]))*0.126*fotoHeight;
			
		}
		else{
			distance = Math.sqrt((qrMiddlePoint[0])*(qrMiddlePoint[0])
					+(qrMiddlePoint[1])*(qrMiddlePoint[1]))*0.17*fotoHeight;
					} 
		double degree[]= {(deg/Math.PI)*180,
				distance,clockwise};
		return degree;
	}
	/**
	 * Returns the degree that you have to add to get to absoulte 0.{degree, clockwise}.
	 * @return
	 * @throws NotFoundException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static double[] getOrientation() throws NotFoundException, FileNotFoundException, IOException{
		if(lastQRused){
			readQRCode();
		}
		else{
			lastQRused=true;
		}
		float[] qrHorizontalMPoint={(points[1]+points[0])/2,(points[4]+points[3])/2};
		double toReturn =Math.atan((qrHorizontalMPoint[0]-points[1])/(qrHorizontalMPoint[1]-points[4]));
		double clockwise=0;
		if(toReturn>0){
			if(points[1]>qrHorizontalMPoint[0]){
				toReturn = Math.PI-toReturn;
				clockwise=1;
			}
			if(points[1]<qrHorizontalMPoint[0]){
				toReturn= Math.PI+toReturn;
				clockwise=0;
			}
		}
		else if(toReturn<0){
			if(points[1]>qrHorizontalMPoint[0]){
				toReturn = Math.PI-toReturn;
				clockwise=1;
			}
			if(points[1]<qrHorizontalMPoint[0]){
				toReturn= Math.PI+toReturn;
				clockwise=0;
			}
		}
		double[] toReturnArray={(toReturn/Math.PI)*180,clockwise};
		return toReturnArray;

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
			double altitude=Main.getInstance().getDistanceSensor().getHeight();
			double height=600,width=800;
			if(altitude<120){
				height=400;
				width=600;
			}
			fotoHeight = altitude;
			Process p = Runtime.getRuntime().exec("raspistill -t 0 -h "+height+" -w "+width+" -o QRimage.jpg");
			p.waitFor();
			isInUse=false;
			System.out.println("Taking picture finished...");
		}
		catch (Exception ieo)
		{
			ieo.printStackTrace();
		}
		String filePath = "QRimage.jpg";
		bi= ImageIO.read(new FileInputStream(filePath));
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bi)));
		System.out.println("Loading image completed. Reading QR Code");
		@SuppressWarnings("unchecked")
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,getMap());
		points[0]=qrCodeResult.getResultPoints()[0].getX()-bi.getWidth()/2;points[3]=(-1*qrCodeResult.getResultPoints()[0].getY())-bi.getHeight()/2;
		points[1]=qrCodeResult.getResultPoints()[1].getX()-bi.getWidth()/2;points[4]=(-1*qrCodeResult.getResultPoints()[1].getY())-bi.getHeight()/2;
		points[2]=qrCodeResult.getResultPoints()[2].getX()-bi.getWidth()/2;points[5]=(-1*qrCodeResult.getResultPoints()[2].getY())-bi.getHeight()/2;
		lastQRused=false;
		System.out.println("QR read:"+qrCodeResult.getText());
		return qrCodeResult.getText();

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
