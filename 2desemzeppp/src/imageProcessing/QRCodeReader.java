package imageProcessing;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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

public abstract class QRCodeReader {
	/**
	 * Takes a high resolution photo and looks for the qr code. Returns the
	 * string if found otherwise null
	 * 
	 * @return
	 * @throws FileNotFoundException
	 *             never throws its
	 * @throws IOException
	 *             if there is a problem reading the file
	 * @throws NotFoundException
	 *             if it cant find any qr codes
	 */
	public static String readQRCode() throws IOException, NotFoundException {

		BufferedImage bi = ImageIO.read(new URL(
				"http://raspberrypi.mshome.net/cam_pic.php?time="
						+ System.currentTimeMillis()));
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(bi)));
		System.out.println("Loading image completed. Reading QR Code");
		@SuppressWarnings("unchecked")
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
				getMap());
		System.out.println("QR read:" + qrCodeResult.getText());
		return qrCodeResult.getText();
	}

	public static String readQRCode(String url) throws MalformedURLException,
			IOException, NotFoundException {
		BufferedImage bi = ImageIO.read(new URL(url));
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(bi)));
		System.out.println("Loading image completed. Reading QR Code");
		@SuppressWarnings("unchecked")
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
				getMap());
		System.out.println("QR read:" + qrCodeResult.getText());
		return qrCodeResult.getText();
	}

	/***
	 * For the things that i do not understand.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static Map getMap() {
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		return hintMap;
	}

}
