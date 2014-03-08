package imageProcessing;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;

import javax.swing.ImageIcon;
import map.ColorSymbol;
import map.Symbol;
import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class ImageProcessor {
	
	private static int erodeTimes=0;
	private static int dilateTimes=2;
	private static int blur=5;
	private static int erodesize=3;
	private static int dilatesize=3;
	private static Mat originalImage = new Mat(new Size(800,800), Core.DEPTH_MASK_8U);
//	private String openCvFolder="C:/Users/Study/Desktop/OpenCv/Processed/";
	private static int cannyThreshold=35;
	private static int minArea=100;
	private static int epsilonApprox=10;
	private static int pointsEqualEpsilon=116;
	private int pointsEqualEpsilonPoints=52;

	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	/**
	 * Processes the image and returns the found color symbols.
	 * @return 
	 */
	public static ArrayList<ColorSymbol> processImage(ImageIcon imageicon){
		BufferedImage buffered = toBufferedImage(imageicon.getImage());
		byte[] pixels = ((DataBufferByte) buffered.getRaster().getDataBuffer())
		            .getData();
		
		    originalImage = new Mat(buffered.getHeight(), buffered.getWidth(), CvType.CV_8UC3);
		    originalImage.put(0, 0, pixels);
		//clone the originalImage in case we need original image later.
		Mat image = originalImage.clone();
		//Changing to black & white
		Mat grayImage = new Mat();
		Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
		//Blurring the image within three by three matrix and writing it as grayimage.png
		Imgproc.blur(grayImage, grayImage,new Size(blur,blur));
//		Highgui.imwrite(openCvFolder+"gray_image.png",grayImage);
		//Making some more matrixes to see the ongoing operations.
	   	Mat emptyImage = new Mat(image.size(),Core.DEPTH_MASK_8U,new Scalar(0,0,0));
	   	//Mat Emptyimage1 = new Mat(image.size(),Core.DEPTH_MASK_8U,new Scalar(255,255,255));
	   	Mat canny_output = new Mat(image.size(),Core.DEPTH_MASK_8U);
	   	Imgproc.Canny(grayImage, canny_output, cannyThreshold, 2*cannyThreshold);
	   	
	   	//eroding the image to reduce the noise;
	   	Mat erodedImage = canny_output.clone();
	   	for(int i = 0; i<erodeTimes;i++){
	   		Imgproc.erode(erodedImage, erodedImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(erodesize,erodesize)));
	   	}
	   	//dilating to make the image clear
	   	Mat dilatedImage = erodedImage.clone();
	   	for(int i=0; i< dilateTimes;i++){
	   		Imgproc.dilate(dilatedImage, dilatedImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(dilatesize,dilatesize)));
	   	}
	   	//Trying different methods.
	   	return findContours(dilatedImage.clone(), image.clone(), emptyImage.clone());
	   	//HoughCircles(dilatedImage.clone(), image.clone(), emptyImage.clone());
	   	//	   	HoughLines(dilatedImage.clone(), image.clone(), emptyImage.clone());
	   	
		
	}
	
	
	private static ArrayList<ColorSymbol> findContours(Mat canny_output, Mat image1, Mat emptyImage){
		Mat emptyImage1 = emptyImage.clone();
		Mat image = image1.clone();
	 	//Making some list to put the points.
	   	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	   	List<MatOfPoint> contoursToDraw = new ArrayList<MatOfPoint>();
	   	
	    //List<MatOfInt4> hierarchy;
	   	
	   	//Finding the contours.
	    Imgproc.findContours(canny_output, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    
	    //Matrices to put the converted contours.
	    MatOfPoint2f MatOfPointTo2f = new MatOfPoint2f();
		MatOfPoint2f MatOfPoint2fApprox = new MatOfPoint2f();
		ArrayList<ColorSymbol> toReturnSymbol = new ArrayList<>();
		//Adding the contours within given distances.
//		System.out.println("Before removing contour size:"+contours.size());
		contours = addContours(contours);
//		System.out.println("After removing and adding:"+contours.size());
	    for( int i = 0; i< contours.size(); i++ )
	     {
	    	if (Imgproc.contourArea(contours.get(i)) >minArea){
	    		contoursToDraw.add(new MatOfPoint(contours.get(i).clone()));
	    		contours.get(i).convertTo(MatOfPointTo2f, CvType.CV_32FC2);  
	    		Imgproc.approxPolyDP(MatOfPointTo2f, MatOfPoint2fApprox, epsilonApprox, true); 
	    		MatOfPoint2fApprox.convertTo(contours.get(i), CvType.CV_32S);
	    		//System.out.println("Points In Contour: " +MatOfPoint2fApprox.toList().size());
    			Core.circle(image,findCenter(MatOfPoint2fApprox.toList()), 10, new Scalar(255,0,0),3);
    			Point contourCenter =findCenter(MatOfPoint2fApprox.toList());
    			ColorSymbol s =getColor(image1,contourCenter);
    			toReturnSymbol.add(s);	
    			Core.putText(image, s.colour.toString(), contourCenter, Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,200,250), 3);}
//	    	else{
//	    		System.out.println("Area too low!");
//	    	}
	    	
	     }
	    
	   	//System.out.println(contoursToDraw.size());
//	    Imgproc.drawContours(emptyImage, contours, -1, new Scalar(50,50,50),10);
	    Imgproc.drawContours(image, contoursToDraw, -1, new Scalar(50,50,50),10);
	    matAfterRework=image;
	    return toReturnSymbol;
	}
	public static Mat matAfterRework; 
	private static ColorSymbol getColor(Mat image, Point contourCenter) {
		double[] coordinate = {contourCenter.y,contourCenter.x};
		Mat bitImage = image.clone();
		//Checking for white
		Core.inRange(image.clone(), Colors.getWhiteMinScalar(), Colors.getWhiteMaxScalar(), bitImage);
		double[] col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
		return new ColorSymbol(coordinate, Symbol.Colour.WHITE);
		
		//Checking for Yellow
		Core.inRange(image.clone(), Colors.getYellowMinScalar(), Colors.getYellowMaxScalar(), bitImage);
		col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
			return new ColorSymbol(coordinate, Symbol.Colour.YELLOW);
		
		Core.inRange(image.clone(), Colors.getRedMinScalar(), Colors.getRedMaxScalar(), bitImage);
		col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
			return new ColorSymbol(coordinate, Symbol.Colour.RED);
		
		Core.inRange(image.clone(), Colors.getBlueMinScalar(), Colors.getBlueMaxScalar(), bitImage);
		col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
			return new ColorSymbol(coordinate, Symbol.Colour.BLUE);
		
		Core.inRange(image.clone(), Colors.getGreenMinScalar(), Colors.getGreenMaxScalar(), bitImage);
		col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
			return new ColorSymbol(coordinate, Symbol.Colour.GREEN);
			
		return new ColorSymbol(coordinate, Symbol.Colour.BLANK);
	}

		
	/**
	 * 
	 * @param contours
	 * 		Lists of matrix points in which to operate.
	 * @return
	 * 		Returns a new list of mat of points after adding the contours which are near each other.
	 */
	private static List<MatOfPoint> addContours(List<MatOfPoint> contours) {
		ArrayList<MatOfPoint> toRemove = new ArrayList<>();
		HashMap<Integer,Integer> fuzzyContours = new HashMap<>();
		for(int i = 0 ; i< contours.size();i++){
			MatOfPoint contour1= contours.get(i);
			for(int index = i+1; index <contours.size();index++ ){
				MatOfPoint contour2 = contours.get(index);
				if(pointsEquals(contour1.get(0, 0),contour2.get(0, 0),pointsEqualEpsilon)){
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
//		System.out.println("removed: " + toRemove.size());
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
	private static boolean pointsEquals(double[] point1, double[] point2,int epsilon){
		double distance = Math.sqrt((point1[0]-point2[0])*(point1[0]-point2[0])+(point1[1]-point2[1])*(point1[1]-point2[1]));
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
	private static Point findCenter(List<Point> points){
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
	
	
	private List<Point> reduceEqualPoints(List<Point> approx){
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
				if(pointsEquals(point1,point2,pointsEqualEpsilonPoints)){
					fuzzyPoints.add(approx.get(j));
					lone = false;
				}
			}
			if(lone){
				lonelyPoints.add(approx.get(i));
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