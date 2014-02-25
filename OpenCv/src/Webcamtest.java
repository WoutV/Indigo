import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.highgui.*;
import org.opencv.imgproc.*;

public class Webcamtest
{
	
 public static Mat analyze(Mat image1){	   
	   System.loadLibrary("opencv_java248");
//		 image = Highgui.imread("C:/Users/Study/Desktop/symbols.jpg", Imgproc.COLOR_BGR2GRAY);
//	   	Mat Emptyimage = Highgui.imread("C:/Users/Study/Desktop/empty.png", Imgproc.COLOR_BGR2GRAY);
	    Mat image = image1.clone();
	   	Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageBlurr = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageA = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
	    
	    Highgui.imwrite("C:/Users/Study/Desktop/test1.png",imageBlurr);
	    
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	    List<MatOfPoint> contoursToDraw = new ArrayList<MatOfPoint>();   
	    Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_NONE);
	    int detected = 0;
	    int size= contours.size();
	    for(int i=0; i< size;i++){
	        System.out.println(Imgproc.contourArea(contours.get(i)));
	        if (Imgproc.contourArea(contours.get(i)) > 1000 ){
	            Rect rect = Imgproc.boundingRect(contours.get(i));
	            System.out.println(rect.height);
	            detected++;
	            if (rect.height > 28){
	            //System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
	            	contoursToDraw.add(contours.get(i));
	            	Core.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
	            	
	            }
	        }
	    }
	    
	    System.out.println("Detected: " +detected);
	    Imgproc.drawContours(image, contoursToDraw, -1, new Scalar(50,50,50));
	 //   Highgui.imwrite("C:/Users/Study/Desktop/contours.png",Emptyimage);
//	    Highgui.imwrite("C:/Users/Study/Desktop/test2.png",image);
	    return image;
	}
	   
	   
	   
	   
}
   
