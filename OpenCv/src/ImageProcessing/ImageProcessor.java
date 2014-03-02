package ImageProcessing;
import java.util.*;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class ImageProcessor {
	public static void main(String[] args) {
		 System.loadLibrary("opencv_java248");
		 ImageProcessor ip = new ImageProcessor("C:/Users/Study/Desktop/OpenCv/s1.jpg");
		 ip.processImage();
	}
	
	
	private Size blurSize=new Size(3,3);
	private Mat originalImage;
	private String openCvFolder="C:/Users/Study/Desktop/OpenCv/Processed/";
	/**
	 * 
	 * @param filePath
	 * 		   The path where file is located. Can be changed later to receive a image matrix.
	 */
	public ImageProcessor(String filePath){
		System.loadLibrary("opencv_java248");
		this.originalImage = Highgui.imread(filePath, Imgproc.COLOR_BGR2GRAY);		
	}
	/**
	 * Process the image and writes the output images on the folder specified.
	 */
	private void processImage(){
		//clone the originalImage incase we need original image later.
		Mat image = originalImage.clone();
		//Changing to black & white
		Mat grayImage = new Mat();
		Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
		//Blurring the image within three by three matrix and writing it as grayimage.png
		Imgproc.blur(grayImage, grayImage,blurSize);
		Highgui.imwrite(openCvFolder+"gray_image.png",grayImage);
		//Making some more matrixes to see the ongoing operations.
	   	Mat emptyImage = new Mat(image.size(),Core.DEPTH_MASK_8U,new Scalar(0,0,0));
	   	//Mat Emptyimage1 = new Mat(image.size(),Core.DEPTH_MASK_8U,new Scalar(255,255,255));
	   	
	   	Mat canny_output = new Mat(image.size(),Core.DEPTH_MASK_8U);
	   	Imgproc.Canny(grayImage, canny_output, 100, 200);
	   	Highgui.imwrite(openCvFolder+"cannny_output.png",canny_output);
	   	Imgproc.dilate(canny_output, canny_output, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));
	   //	Imgproc.dilate(canny_output, canny_output, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));
	   	//Trying different methods.
	   	findContours(canny_output.clone(), image.clone(), emptyImage.clone());
	   	HoughCircles(canny_output.clone(), image.clone(), emptyImage.clone());
	   	HoughLines(canny_output.clone(), image.clone(), emptyImage.clone());
	   	
		
	}
	
	
	private void findContours(Mat canny_output, Mat image, Mat emptyImage){
		Mat emptyImage1 = emptyImage.clone();
	 	//Making some list to put the points.
	   	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	   	List<MatOfPoint> contoursToDraw = new ArrayList<MatOfPoint>();
	    //List<MatOfInt4> hierarchy;
	   	
	   	//Finding the contours.
	    Imgproc.findContours(canny_output, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    
	    //Matrices to put the converted contours.
	    MatOfPoint2f MatOfPointTo2f = new MatOfPoint2f();
		MatOfPoint2f MatOfPoint2fApprox = new MatOfPoint2f();
		
		//Adding the contours within given distances.
		contours = addContours(contours);
	    for( int i = 0; i< contours.size(); i++ )
	     {
	    	if (Imgproc.contourArea(contours.get(i)) >10){
	    		contoursToDraw.add(new MatOfPoint(contours.get(i).clone()));
	    		contours.get(i).convertTo(MatOfPointTo2f, CvType.CV_32FC2);  
	    		Imgproc.approxPolyDP(MatOfPointTo2f, MatOfPoint2fApprox, 10, true); 
	    		MatOfPoint2fApprox.convertTo(contours.get(i), CvType.CV_32S);
	    		System.out.println("Points In Contour: " +MatOfPoint2fApprox.toList().size());
	    			Core.circle(image,findCenter(MatOfPoint2fApprox.toList()), 10, new Scalar(255,0,0),3);
	    			if(isCircle(MatOfPoint2fApprox.toList(),findCenter(MatOfPoint2fApprox.toList()))){
	    				Core.circle(image,findCenter(MatOfPoint2fApprox.toList()), (int)averageRadius, new Scalar(255,0,0),3);
	    			}
	    			if(i==1){
	    			List<Point> returnedPoint = isRectangle(MatOfPoint2fApprox.toList());
	    			for(int z=0;z<returnedPoint.size();z++){
	    			Core.circle(image, returnedPoint.get(z), 20, new Scalar(0,0,255),5);
	    			}
	    			}
	    			
	    		
	    		for(int index=0;index < MatOfPoint2fApprox.toList().size() ; index++){
	    			Core.circle(image,MatOfPoint2fApprox.toList().get(index), 10, new Scalar(0,255,0),3);
	    			
	    		}
	    		
//				Core.circle(image, new Point(contours.get(i).get(0, 0)[0],contours.get(i).get(0, 0)[1]), 10, new Scalar(255,0,0),3);
	    	}
	    	
	     }
	    
	   	//System.out.println(contoursToDraw.size());
	    Imgproc.drawContours(emptyImage, contours, -1, new Scalar(50,50,50),10);
	    Imgproc.drawContours(emptyImage1, contoursToDraw, -1, new Scalar(50,50,50),10);
	    Highgui.imwrite(openCvFolder+"contours_approx.png",emptyImage);
	    Highgui.imwrite(openCvFolder+"contours.png",emptyImage1);
	    Highgui.imwrite(openCvFolder+"findContourResult.png",image);
	}
	
	/**
	 * Tries to find the lines in the photo.
	 * @param canny_output
	 * @param image
	 * @param Emptyimage
	 */
	private void HoughLines(Mat canny_output, Mat image, Mat Emptyimage){
	   	Mat lines = new Mat();
	    Imgproc.HoughLinesP(canny_output, lines, 1, Math.PI/180, 50, 50,10);
	    System.out.println("Total Lines:"+lines.cols());
	    for (int x = 0; x < lines.cols(); x++) 
	    {
	          double[] vec = lines.get(0, x);
	          double x1 = vec[0], 
	                 y1 = vec[1],
	                 x2 = vec[2],
	                 y2 = vec[3];
	          Point start = new Point(x1, y1);
	          Point end = new Point(x2, y2);

	          Core.line(Emptyimage, start, end, new Scalar(255,0,0), 5);
	          Core.line(image, start, end, new Scalar(0,255,0), 5);
	          

	    }
	    
	    Highgui.imwrite(openCvFolder+"HoughLinecontours.png",Emptyimage);
	    Highgui.imwrite(openCvFolder+"HoughLineResult.png",image);

	}
	
	/**
	 * Tries to find the circles in the given image.
	 * @param canny_output
	 * @param image
	 * @param Emptyimage
	 */
	private void HoughCircles(Mat canny_output, Mat image, Mat Emptyimage){
	    Mat circles = new Mat();	    
	    Imgproc.HoughCircles(canny_output, circles, Imgproc.CV_HOUGH_GRADIENT, 1, image.rows()/100, 150, 30,10,500);
	    for (int x = 0; x < circles.cols(); x++) 
	    {
	          double[] vec = circles.get(0, x);
	          double x1 = vec[0], 
	                 y1 = vec[1],
	                 r = vec[2];
	                 
	          Point start = new Point(x1, y1);
	         
	          Core.circle(image, start , (int) r, new Scalar(255,0,0),10);
	          Core.circle(Emptyimage, start , (int) r, new Scalar(255,0,0),3);
	    }
	    System.out.println("circles detected: "+circles.cols());
	    Highgui.imwrite(openCvFolder+"Circlecontours.png",Emptyimage);
	    Highgui.imwrite(openCvFolder+"HoughCircleResult.png",image);
		
	}
		
	/**
	 * 
	 * @param contours
	 * 		Lists of matrix points in which to operate.
	 * @return
	 * 		Returns a new list of mat of points after adding the contours which are near each other.
	 */
	private List<MatOfPoint> addContours(List<MatOfPoint> contours) {
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
	
	/**
	 * Returns true if the distance between the given points is less than the given epsilon.
	 * @param point1
	 * @param point2
	 * @param epsilon
	 * 			The margin to look if the given points are almost equal.
	 * @return
	 * 		
	 */
	private boolean pointsEquals(double[] point1, double[] point2,int epsilon){
		double distance = Math.sqrt((point1[0]-point2[0])*(point1[0]-point2[0])+(point1[1]-point2[1])*(point1[1]-point2[1]));
		System.out.println("Distance:="+distance);
		if(distance<epsilon)
			return true;
		return false;
	}
	
	/**
	 * Returns the center of the given contour.
	 * @param points
	 * @return
	 * 			The center of the list.
	 */
	private Point findCenter(List<Point> points){
		double x=0;
		double y=0;
		for (int i=0; i<points.size();i++){
			x += points.get(i).x;
			y += points.get(i).y;
			
		}
//		double[] centers = new double[2];
//		centers[0]=x/points.size();
//		centers[1]=y/points.size();
		return new Point(x/points.size(),y/points.size());
	}
	
	static double averageRadius=0;
	public boolean isCircle(List<Point> approx, Point center){
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
	
	public List<Point> isRectangle(List<Point> approx){
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
