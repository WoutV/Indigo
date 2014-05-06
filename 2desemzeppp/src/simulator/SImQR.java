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

	static Boolean encryptedLocked = false;

	public static String decodeQR(int tablet, SimEnemyConn sender) {
		try {
			if (encryptedLocked) {
				synchronized (encryptedLocked) {
					encryptedLocked.wait();
				}
			}
			encryptedLocked=true;
			
			Process p =Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\keys.py");
			p.waitFor();
			String key = read("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\public");
			sender.sendTransfer(key, "indigo.tablets.tablet"+tablet);
			//System.out.println(key);
			String text ="";
			int numberOfTries =0;
			Thread.sleep(2000);
			while(numberOfTries < 10){
				try{
				text = QRCodeReader.readQRCode("http://192.168.2.134:5000/static/indigo"+tablet+".png");
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			encryptedLocked.notify();
			encryptedLocked=false;
			return "";
			}

	}
	public static String read(String fileName) {
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
		} catch (IOException e) {
		}
		return result;
	}

	public static void write(String fileName, String text) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println(text);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
		}
	}
}
