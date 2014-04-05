package ImageProcessing;
import java.awt.Rectangle;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
public class ImageRotator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String filepath ="C:/Users/Study/Desktop/OpenCv/templates/rec.png";
		Mat image = Highgui.imread(filepath);
		Mat bigImage = new Mat(new Size(800,800), image.type());
		Rect roi = new Rect(new Point(400-image.width()/2,400-image.height()/2), image.size());
		image.copyTo(bigImage.submat(roi));
		Mat output = new Mat();
		int len = Math.max(image.height(), image.width());
		for(int i=0;i<18;i++){
		Mat rotatedImage = Imgproc.getRotationMatrix2D(new Point(bigImage.width()/2,bigImage.height()/2), i*10, 1.0);
		Imgproc.warpAffine(bigImage, output, rotatedImage, new Size(800,800));
		Mat subMat = output.submat(new Range(200, 600),new Range(200, 600));
		Highgui.imwrite("C:/Users/Study/Desktop/OpenCv/templates/rect/rect"+i+".png", subMat);
			
		}
		System.out.println("Done");
	}

}
