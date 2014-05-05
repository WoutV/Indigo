package simulator;

import imageProcessing.QRCodeReader;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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

public class SImQR {

	public static String decodeQR(int tablet){
		try {
			String encryptedString = readQRCode(tablet);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static String readQRCode(int no)
			throws IOException, NotFoundException {
		
		String path = "/qrcodes/tablet"+no+".png";
		BufferedImage bi= ImageIO.read(new File(path));
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bi)));
		System.out.println("Loading image completed. Reading QR Code");
		@SuppressWarnings("unchecked")
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,getMap());
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
	
	static int numberOfTries = 0;
	private static String sendRSAKeyToTablet(){
		try{
		Process p =Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\keys.py");
		p.waitFor();
		
		String text ="";
		numberOfTries =0;
		while(numberOfTries < 10){
			try{
			text = QRCodeReader.readQRCode();
			break;
			}
			catch(IOException|NotFoundException exception){
				numberOfTries++;
			}
		}
		write("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\encrypted", text);
		p = Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\decription.py");
		p.waitFor();
		String output = read("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\result");
		System.out.println(output);
		return output;
		}catch(Exception e){
			//doe niks
		}
		return "Oops";
	
		
	}
	
	
	public static String read(String fileName){
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			result = sb.toString();
			br.close();
		} catch(IOException e) {
		}
		return result;
	}
	
	public static void write(String fileName, String text){
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println(text);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
		}
	}
}
