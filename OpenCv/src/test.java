import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
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
	static String filePath ="C:/Users/Study/Desktop/OpenCv/s1.jpg";
	static boolean filter= true;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 System.loadLibrary("opencv_java248");
		SimpleForms();

	}
	static void original(){
		Mat image = Highgui.imread("filePath", Imgproc.COLOR_BGR2GRAY);
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
	
		
	}
	static void HoughLines(){
		Mat image = Highgui.imread(filePath);
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

	}
	static void HoughCircles(){
		Mat image = Highgui.imread(filePath);
	   	Mat Emptyimage = new Mat(image.size(),Core.DEPTH_MASK_8U);
	   	Core.inRange(image, new Scalar(0,0,0), new Scalar(0,0,255), image);
	    Mat lines = new Mat();	    
	    Imgproc.HoughCircles(image, lines, Imgproc.CV_HOUGH_GRADIENT, 1, image.rows()/8, 200, 100,0,0);
	    for (int x = 0; x < lines.cols(); x++) 
	    {
	          double[] vec = lines.get(0, x);
	          double x1 = vec[0], 
	                 y1 = vec[1],
	                 r = vec[2];
	                 
	          Point start = new Point(x1, y1);
	         
	          Core.circle(image, start , (int) r, new Scalar(255,0,0),3);
	          Core.circle(Emptyimage, start , (int) r, new Scalar(255,0,0),3);
	    }
	    System.out.println("circles detected: ");
	    Highgui.imwrite("C:/Users/Study/Desktop/contours.png",Emptyimage);
	    Highgui.imwrite("C:/Users/Study/Desktop/test2.png",image);
		
	}
	static void SimpleForms(){
		Mat image = Highgui.imread(filePath, Imgproc.COLOR_BGR2GRAY);
		Mat grayImage = new Mat();
		Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(grayImage, grayImage, new Size(3,3));
		Highgui.imwrite("C:/Users/Study/Desktop/gray_image.png",grayImage);
	   	Mat Emptyimage = new Mat(image.size(),Core.DEPTH_MASK_8U,new Scalar(255,255,255));
	   	Mat Emptyimage1 = new Mat(image.size(),Core.DEPTH_MASK_8U,new Scalar(255,255,255));
	   	Mat canny_output = new Mat(image.size(),Core.DEPTH_MASK_8U);
	   	Imgproc.Canny(grayImage, canny_output, 100, 200);
	   	Highgui.imwrite("C:/Users/Study/Desktop/cannny_output.png",canny_output);
	   	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	   	List<MatOfPoint> contoursToDraw = new ArrayList<MatOfPoint>();
	    //List<MatOfInt4> hierarchy;
	    Imgproc.findContours(canny_output, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    MatOfPoint2f mMOP2f1 = new MatOfPoint2f();
		MatOfPoint2f mMOP2f2 = new MatOfPoint2f();
		if(filter)
			contours = addContours(contours);
	    for( int i = 0; i< contours.size(); i++ )
	     {
	    	if (Imgproc.contourArea(contours.get(i)) >10){
	    		contoursToDraw.add(new MatOfPoint(contours.get(i).clone()));
	    		contours.get(i).convertTo(mMOP2f1, CvType.CV_32FC2);  
	    		Imgproc.approxPolyDP(mMOP2f1, mMOP2f2, 10, true); 
	    		mMOP2f2.convertTo(contours.get(i), CvType.CV_32S);
	    		System.out.println("Points In Contour: " +mMOP2f2.toList().size());
	    			Core.circle(image,findCenter(mMOP2f2.toList()), 10, new Scalar(255,0,0),3);
	    			if(isCircle(mMOP2f2.toList(),findCenter(mMOP2f2.toList()))){
	    				Core.circle(image,findCenter(mMOP2f2.toList()), (int)averageRadius, new Scalar(255,0,0),3);
	    			}
	    			if(i==1){
	    			List<Point> returnedPoint = isRectangle(mMOP2f2.toList());
	    			for(int z=0;z<returnedPoint.size();z++){
	    			Core.circle(image, returnedPoint.get(z), 20, new Scalar(0,0,255),5);
	    			}
	    			}
	    			
	    		
	    		for(int index=0;index < mMOP2f2.toList().size() ; index++){
	    			Core.circle(image,mMOP2f2.toList().get(index), 10, new Scalar(0,255,0),3);
	    			
	    		}
	    		
//				Core.circle(image, new Point(contours.get(i).get(0, 0)[0],contours.get(i).get(0, 0)[1]), 10, new Scalar(255,0,0),3);
	    	}
	    	
	     }
	    
	   	//System.out.println(contoursToDraw.size());
	    Imgproc.drawContours(Emptyimage, contours, -1, new Scalar(50,50,50),10);
	    Imgproc.drawContours(Emptyimage1, contoursToDraw, -1, new Scalar(50,50,50),10);
	    Highgui.imwrite("C:/Users/Study/Desktop/contours_approx.png",Emptyimage);
	    Highgui.imwrite("C:/Users/Study/Desktop/contours.png",Emptyimage1);
	    Highgui.imwrite("C:/Users/Study/Desktop/test2.png",image);
		
	}
	private static List<MatOfPoint> addContours(List<MatOfPoint> contours) {
		ArrayList<MatOfPoint> toRemove = new ArrayList<>();
		HashMap<Integer,Integer> fuzzyContours = new HashMap<>();
		for(int i = 0 ; i< contours.size();i++){
			MatOfPoint contour1= contours.get(i);
			for(int index = i+1; index <contours.size();index++ ){
				MatOfPoint contour2 = contours.get(index);
				if(pointsEquals(contour1.get(0, 0),contour2.get(0, 0),250)){
					fuzzyContours.put(i, index);
					toRemove.add(contour2);
				}
			}
		}
		for(int j=0; j< fuzzyContours.size(); j++){
			int a = (int) fuzzyContours.keySet().toArray()[j];
			int b = fuzzyContours.get(fuzzyContours.keySet().toArray()[j]);
			ArrayList<Point> firstone = new ArrayList<>(contours.get(a).toList());
			ArrayList<Point> secondone = new ArrayList<>(contours.get(b).toList());
			for(int q=0;q<firstone.size();q++){
				secondone.add(firstone.get(q));
			}
			contours.get(a).fromList(secondone);
		}
		
		for(MatOfPoint p: toRemove ){
			contours.remove(p);
		}
		System.out.println("removed: " + toRemove.size());
		return contours;
	}
	private static boolean pointsEquals(double[] point1, double[] point2,int epsilon){
		double distance = Math.sqrt((point1[0]-point2[0])*(point1[0]-point2[0])+(point1[1]-point2[1])*(point1[1]-point2[1]));
		System.out.println("Distance:="+distance);
		if(distance<epsilon)
			return true;
		return false;
	}
	
	private static Point findCenter(List<Point> approx){
		double x=0;
		double y=0;
		for (int i=0; i<approx.size();i++){
			x += approx.get(i).x;
			y += approx.get(i).y;
			
		}
//		double[] centers = new double[2];
//		centers[0]=x/approx.size();
//		centers[1]=y/approx.size();
		return new Point(x/approx.size(),y/approx.size());
	}
	
	static double averageRadius=0;
	public static boolean isCircle(List<Point> approx, Point center){
		double radiussum=0;
		double x;
		double y;
		for(int i=0; i<approx.size(); i++){
			x= approx.get(i).x;
			y= approx.get(i).y;
			radiussum += Math.sqrt((x-center.x)*(x-center.x)+(y-center.y)*(y-center.y));
		}

		averageRadius=radiussum/approx.size();
		double difference=0;
		for(int j=0; j<approx.size();j++){
			x= approx.get(j).x;
			y= approx.get(j).y;
			double radius = Math.sqrt((x-center.x)*(x-center.x)+(y-center.y)*(y-center.y));
			difference += (averageRadius - radius )*(averageRadius - radius );
		}
		
		System.out.println(difference/approx.size());
		if(difference<16*approx.size())
			return true;
		return false;
	}
	
	public static List<Point> isRectangle(List<Point> approx){
		ArrayList<Point> newApprox= new ArrayList<Point>(approx);
		ArrayList<Point> lonelyPoints= new ArrayList<Point>();
		List<Point> fuzzyPoints = new ArrayList<Point>();
		double[] point1 = new double[2];
		double[] point2 = new double[2];
		boolean lone=true;
		for(int i=0; i<approx.size();i++){
			point1[0]=approx.get(i).x;
			point1[1]=approx.get(i).y;
			for(int j=i+1;j<approx.size();j++){
				point2[0]=approx.get(j).x;
				point2[1]=approx.get(j).y;
				System.out.println("Test");
				if(pointsEquals(point1,point2,10)){
					fuzzyPoints.add(approx.get(j));
					System.out.println("index fuzzy:=" + j);
					lone = false;
				}
			}
			if(lone){
				lonelyPoints.add(approx.get(i));
				System.out.println("lonely point:="+ i);
			}
			lone = true;
			
		}
		for(int k=0; k<fuzzyPoints.size();k++){
			if(newApprox.contains(fuzzyPoints.get(k)))
				newApprox.remove(fuzzyPoints.get(k));
		}
		for(int p=0; p<lonelyPoints.size(); p++){
			if(newApprox.contains(lonelyPoints.get(p)))
				newApprox.remove(lonelyPoints.get(p));
		}
		return newApprox;
	}

}
