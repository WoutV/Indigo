import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;



public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 System.loadLibrary("opencv_java248");
		HoughLines();

	}
	static void original(){
		Mat image = Highgui.imread("C:/Users/Study/Desktop/r.png", Imgproc.COLOR_BGR2GRAY);
	   	Mat Emptyimage = new Mat(image.size(),Core.DEPTH_MASK_8U, new Scalar(255,255,255));
	    Mat imageHSV = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageBlurr = new Mat(image.size(), Core.DEPTH_MASK_8U);
	    Mat imageA = new Mat(image.size(), Core.DEPTH_MASK_ALL);
	    Imgproc.cvtColor(image, imageHSV, Imgproc.COLOR_BGR2GRAY);
	    Imgproc.GaussianBlur(imageHSV, imageBlurr, new Size(5,5), 0);
	    Imgproc.adaptiveThreshold(imageBlurr, imageA, 255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7, 5);
	    
	    Highgui.imwrite("C:/Users/Study/Desktop/test1.png",imageBlurr);
	    MatOfPoint2f approx = new MatOfPoint2f();
	    List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	    List<MatOfPoint> contoursToDraw = new ArrayList<MatOfPoint>();   
	    Imgproc.findContours(imageA, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    int detected = 0;
	    int size= contours.size();
	    for(int i=0; i< size;i++){
//	        System.out.println(Imgproc.contourArea(contours.get(i)));
	        if (Imgproc.contourArea(contours.get(i)) > 1000){
	        	Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), approx, Imgproc.arcLength(new MatOfPoint2f(contours.get(i).toArray()), true)*1, true);
	        	System.out.println("Size approx:"+ approx.elemSize1());
	        	Rect rect = Imgproc.boundingRect(contours.get(i));
//	            System.out.println(rect.height);
	            detected++;
	            if (rect.height > 28){
	            //System.out.println(rect.x +","+rect.y+","+rect.height+","+rect.width);
	            	contoursToDraw.add(contours.get(i));
	            	Core.rectangle(image, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255));
	            	
	            }
	        }
	    }
	    
	    System.out.println("Detected: " +detected);
	    Imgproc.drawContours(Emptyimage, contoursToDraw, -1, new Scalar(255,50,50));
	    Highgui.imwrite("C:/Users/Study/Desktop/contours.png",Emptyimage);
	    Highgui.imwrite("C:/Users/Study/Desktop/test2.png",image);
		// TODO Auto-generated method stub
		
	}
	static void HoughLines(){
		Mat image = Highgui.imread("C:/Users/Study/Desktop/OpenCv/img.png");
	   	Mat Emptyimage = new Mat(image.size(),Core.DEPTH_MASK_8U);
	   	Core.inRange(image, new Scalar(0,0,0), new Scalar(1,1,1), image);
	    Mat lines = new Mat();	    
	    Imgproc.HoughLinesP(image, lines, 1, Math.PI/180, 50, 50,10);
	    for (int x = 0; x < lines.cols(); x++) 
	    {
	          double[] vec = lines.get(0, x);
	          double x1 = vec[0], 
	                 y1 = vec[1],
	                 x2 = vec[2],
	                 y2 = vec[3];
	          Point start = new Point(x1, y1);
	          Point end = new Point(x2, y2);

	          Core.line(image, start, end, new Scalar(255,0,0), 3);
	          Core.line(Emptyimage, start, end, new Scalar(255,0,0), 3);

	    }
	    
	    Highgui.imwrite("C:/Users/Study/Desktop/contours.png",Emptyimage);
	    Highgui.imwrite("C:/Users/Study/Desktop/test2.png",image);
		// TODO Auto-generated method stub
	}

}
