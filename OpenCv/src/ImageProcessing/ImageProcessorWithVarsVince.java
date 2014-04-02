package ImageProcessing;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import map.Symbol;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class ImageProcessorWithVarsVince {
	
	public static void main(String[] args) {
		 System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		 try {
			ImageProcessorWithVarsVince ip = new ImageProcessorWithVarsVince("C:/Users/Vince/Desktop/a/2/b (107).jpg");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ip.processImage();	 
	}
	
	
	private int erodeTimes=0;
	private int dilateTimes=2;
	private int blur=5;
	private int erodesize=3;
	private int dilatesize=3;
	private Mat originalImage;
	private String openCvFolder="C:/Users/Vince/Desktop/";
	private int cannyThreshold=35;
	private int minArea=100;
	private int epsilonApprox=10;
	private int pointsEqualEpsilon=116;
	private int pointsEqualEpsilonPoints=52;
	private HashMap<Double, Point> areaSingle;
	private HashMap<Double, Point> areaDouble;
	/**
	 * 
	 * @param filePath
	 * 		   The path where file is located. Can be changed later to receive a image matrix.
	 * @throws InterruptedException 
	 */
	public ImageProcessorWithVarsVince(String filePath) throws InterruptedException{
		System.loadLibrary("opencv_java248");
		BufferedImage buffered = toBufferedImage(new ImageIcon(filePath).getImage());
		byte[] pixels = ((DataBufferByte) buffered.getRaster().getDataBuffer())
		            .getData();
		
		    originalImage = new Mat(buffered.getHeight(), buffered.getWidth(), CvType.CV_8UC3);
		    originalImage.put(0, 0, pixels);
		//this.originalImage = Highgui.imread(filePath, Imgproc.COLOR_BGR2GRAY);
		createToolbars();
		Size frameSize= new Size();
			if(originalImage.height()>=originalImage.width()){
				//frameSize = new Size(700*originalImage.width()/originalImage.height(), 700);
				frameSize = new Size(800, 700);
			}
			else{
//				frameSize = new Size(800,800*originalImage.height()/originalImage.width());
				frameSize = new Size(800,700);
			}
		
	    //make the JFrame
	        
	      frame facePanel = makeFrame("Canny Output", frameSize);      
	      frame facePanel2 = makeFrame("Eroded Output", frameSize); 
	      frame facePanel3 = makeFrame("Dilated Output", frameSize);
	      findContourFrame = makeFrame("Result", frameSize);
//	      HoughLineFrame = makeFrame("Line",frameSize);
	      while(true){
	    	  Thread.sleep(200);
	    	  processImage(facePanel, facePanel2, facePanel3);
	    	  
	      }
	 
	}
	
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
	private frame findContourFrame; 
	private frame HoughLineFrame;
	/**
	 * The title of the frame
	 * @param Title
	 * @return
	 */
	private frame makeFrame(String title,Size frameSize){
	    //make the JFrame
	      JFrame frame = new JFrame(title);  
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
	      frame facePanel = new frame();  
	      frame.setSize((int)frameSize.width,(int)frameSize.height); //give the frame some arbitrary size 
	      frame.setBackground(Color.BLUE);
	      frame.add(facePanel,BorderLayout.CENTER); 
	      frame.setVisible(true); 
	      return facePanel;
	}
	/**
	 * Process the image and writes the output images on the folder specified.
	 */
	private void processImage(frame cannyoutput, frame erodedoutput,frame dilatedoutput){
		//clone the originalImage incase we need original image later.
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
	   	
	   	cannyoutput.matToBufferedImage(canny_output);  
	   	cannyoutput.repaint();  
//	   	Highgui.imwrite(openCvFolder+"cannny_output.png",canny_output);
	   	
	   	//eroding the image to reduce the noise;
	   	Mat erodedImage = canny_output.clone();
	   	for(int i = 0; i<erodeTimes;i++){
	   		Imgproc.erode(erodedImage, erodedImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(erodesize,erodesize)));
	   	}
	   	
	   	erodedoutput.matToBufferedImage(erodedImage);  
	   	erodedoutput.repaint();
	   	//dilating to make the image clear
	   	Mat dilatedImage = erodedImage.clone();
	   	for(int i=0; i< dilateTimes;i++){
	   		Imgproc.dilate(dilatedImage, dilatedImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(dilatesize,dilatesize)));
	   	}
	   	dilatedoutput.matToBufferedImage(dilatedImage);  
	   	dilatedoutput.repaint();
	   	//Trying different methods.
	   	findContours(dilatedImage.clone(), image.clone(), emptyImage.clone());
	   	//HoughCircles(dilatedImage.clone(), image.clone(), emptyImage.clone());
//	   	HoughLines(dilatedImage.clone(), image.clone(), emptyImage.clone());
	   	
		
	}
	
	
	private void findContours(Mat canny_output, Mat image1, Mat emptyImage){
		Mat emptyImage1 = emptyImage.clone();
		Mat image = image1.clone();
	 	//Making some list to put the points.
	   	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	   	List<MatOfPoint> notDeletedContours = new ArrayList<MatOfPoint>();
	   	List<MatOfPoint> contoursToDraw = new ArrayList<MatOfPoint>();
	   	
	   	//Finding the contours.
	    Imgproc.findContours(canny_output, notDeletedContours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	    
	    //Matrices to put the converted contours.
	    MatOfPoint2f MatOfPointTo2f = new MatOfPoint2f();
		MatOfPoint2f MatOfPoint2fApprox = new MatOfPoint2f();
		
		//Adding the contours within given distances.
		System.out.println("Before removing contour size:"+contours.size());

		
		contours = deleteContours(notDeletedContours);
//   		ArrayList<Double> areas = new ArrayList<Double>(starPoints.keySet());
//		for(int p=0; p< areas.size(); p++){
//			if(fuzzyEquals(starPoints.get(areas.get(p)).y,426,2)){
//				System.out.println("Star:"+areas.get(p));
//			}
//			else{
//				System.out.println("x:"+Math.round(starPoints.get(areas.get(p)).x) +"areas:"+areas.get(p));
//			}
//		}
		System.out.println("After removing and adding:"+contours.size());
	    for( int i = 0; i< contours.size(); i++ )
	     {
	    	Point contourCenters =findCenter(contours.get(i).toList());

		
			
			/*
			 * Display all the points of the contours after deletion.
			 */
			for(int index=0;index < notDeletedContours.get(i).toList().size() ; index++){
    			Core.circle(image,notDeletedContours.get(i).toList().get(index), 1, new Scalar(0,255,255),1);
    		}

	    	if (Imgproc.contourArea(contours.get(i)) >minArea){
	    		/*
				 * Works if used on deletedcontours. And not on an approximation.
				 */
				if(isCircle(contours.get(i).toList(),contourCenters)){
					Core.putText(image,"C", contourCenters, Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,200,250), 3);
					System.out.println("CircleArea:" + Imgproc.contourArea(contours.get(i)));
				}
				
				/*
				 * Works on the deleted contours. Other possibility is to work on both contours. The inner and outer
				 * contour to one figure.
				 */
				else if(isRectangle(contours.get(i))>0.7){
					Core.putText(image,"R", contourCenters, Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,200,250), 3);
				}
				
				else if(isStar(contours.get(i))>2.1 && isStar(contours.get(i))<2.2){
					Core.putText(image,"R", contourCenters, Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,200,250), 3);
				}
				
				else{
					Core.putText(image,""+Imgproc.contourArea(contours.get(i)), contourCenters, Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,200,250), 3);
				}
//	    		for(int z=0; z < contours.get(i).toList().size();z++){
//    				Core.circle(image, contours.get(i).toList().get(z), 1, new Scalar(0,0,255),1);
//    				}
//	    		ArrayList<Double> areas = new ArrayList<Double>(area.keySet());
//	    		for(int p=0; p< areas.size(); p++){
//	    		Core.putText(image,""+areas.get(p), area.get(areas.get(p)), Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,200,250), 3);
//	    		}

	    		contoursToDraw.add(new MatOfPoint(contours.get(i).clone()));
	    		contours.get(i).convertTo(MatOfPointTo2f, CvType.CV_32FC2);  
	    		Imgproc.approxPolyDP(MatOfPointTo2f, MatOfPoint2fApprox, epsilonApprox, true); 
	    		MatOfPoint2fApprox.convertTo(contours.get(i), CvType.CV_32S);
	    		
	    		//System.out.println("Points In Contour: " +MatOfPoint2fApprox.toList().size());

    			Point contourCenter =findCenter(MatOfPoint2fApprox.toList());

    			

    			
//    			String Color= getColor(image1,contourCenter);
//    			List<Point> returnedPoint = reduceEqualPoints(MatOfPoint2fApprox.toList());
//    			for(int z=0;z<returnedPoint.size();z++){
//    				Core.circle(image, returnedPoint.get(z), 5, new Scalar(0,0,255),5);
//    				}
//    			if(returnedPoint.size()==4){
//    				Core.putText(image, Color+"REC", contourCenter, Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,200,250), 3);
//    			}
//    			else
//    				Core.putText(image, Color, contourCenter, Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,200,250), 3);
//    			
//	    		for(int index=0;index < MatOfPoint2fApprox.toList().size() ; index++){
//	    			Core.circle(image,MatOfPoint2fApprox.toList().get(index), 2, new Scalar(0,255,255),3);
//	    			//Core.putText(image, "C:"+i, MatOfPoint2fApprox.toList().get(index), Core.FONT_HERSHEY_COMPLEX_SMALL, 3, new Scalar(200,200,250), 3);
//	    		}
	    		
				//Core.circle(image, new Point(contours.get(i).get(0, 0)[0],contours.get(i).get(0, 0)[1]), 10, new Scalar(255,0,0),3);
	    	}
	    	else{
	    		System.out.println("Area too low!");
	    	}
	    	
	     }
	    
	   	//System.out.println(contoursToDraw.size());
	    Imgproc.drawContours(emptyImage, contours, -1, new Scalar(50,50,50),10);
	    Imgproc.drawContours(emptyImage1, contoursToDraw, -1, new Scalar(50,50,50),10);
//	    Highgui.imwrite(openCvFolder+"contours_approx.png",emptyImage);
//	    Highgui.imwrite(openCvFolder+"contours.png",emptyImage1);
	    findContourFrame.matToBufferedImage(image);
	    findContourFrame.repaint();
//	    Highgui.imwrite(openCvFolder+"findContourResult.png",image);
	}
	
	private String getColor(Mat image, Point contourCenter) {
		Mat bitImage = image.clone();
		//Checking for white
		Core.inRange(image.clone(), Colors.getWhiteMinScalar(), Colors.getWhiteMaxScalar(), bitImage);
		double[] col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		//System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
		return "W";
		
		//Checking for Yellow
		Core.inRange(image.clone(), Colors.getYellowMinScalar(), Colors.getYellowMaxScalar(), bitImage);
		col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		//System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
		return "Y";
		
		Core.inRange(image.clone(), Colors.getRedMinScalar(), Colors.getRedMaxScalar(), bitImage);
		col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		//System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
		return "R";
		
		Core.inRange(image.clone(), Colors.getBlueMinScalar(), Colors.getBlueMaxScalar(), bitImage);
		col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		//System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
		return "B";
		
		Core.inRange(image.clone(), Colors.getGreenMinScalar(), Colors.getGreenMaxScalar(), bitImage);
		col= bitImage.get((int)contourCenter.y,(int)contourCenter.x);
		//System.out.println("col[0]"+col[0]+" length:"+col.length);
		if(col[0]>=150)
		return "G";
			
		//System.out.println("col:"+col +"MatrixSize:"+ image.height() +","+image.width());
		return "NI";
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
	   // System.out.println("Total Lines:"+lines.cols());
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
	    
//	    Highgui.imwrite(openCvFolder+"HoughLinecontours.png",Emptyimage);
//	    Highgui.imwrite(openCvFolder+"HoughLineResult.png",image);
	    HoughLineFrame.matToBufferedImage(image);
	    HoughLineFrame.repaint();
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
	   // System.out.println("circles detected: "+circles.cols());
//	    Highgui.imwrite(openCvFolder+"Circlecontours.png",Emptyimage);
//	    Highgui.imwrite(openCvFolder+"HoughCircleResult.png",image);
		
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
				if(pointsEquals(findCenter(contour1.toList()),findCenter(contour2.toList()),pointsEqualEpsilon)){
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
	
	private List<MatOfPoint> deleteContours(List<MatOfPoint> contours) {
		this.areaSingle = new HashMap<Double,Point>();
		this.areaDouble= new HashMap<Double,Point>();
		ArrayList<MatOfPoint> toRemove = new ArrayList<>();
		HashMap<Integer,Integer> fuzzyContours = new HashMap<>();
		for(int i = 0 ; i< contours.size();i++){
			MatOfPoint contour1= contours.get(i);
			isStar(contour1);
			for(int index = i+1; index <contours.size();index++ ){
				MatOfPoint contour2 = contours.get(index);
				if(pointsEquals(findCenter(contour1.toList()),findCenter(contour2.toList()),150)){
					fuzzyContours.put(i, index);
					toRemove.add(contour2);
					areaDouble.put((isRectangle(contour1)+isRectangle(contour2)/2),findCenter(contour1.toList()));
				}
			}
			
		}
		for(int p=0; p<toRemove.size(); p++){
			contours.remove(toRemove.get(p));
		}
		for(int z=0; z<contours.size();z++){
			double a =isRectangle(contours.get(z));
			areaSingle.put(a,findCenter(contours.get(z).toList()));
		}
		return contours;
	}
	HashMap<Double,Point> starPoints= new HashMap<Double,Point>();
	
	/*
	 * Calculates the arearatio between the bounding circle and the star.
	 */
	private double isStar(MatOfPoint contour1){
		List<Point> contour= contour1.toList();
		Point center = findCenter(contour);
		List<Point> sortedList = sortPolar(contour);
		double maxdist=0;
		Point farest=null;
		for(int i=0; i<sortedList.size();i++){

			double distance=calculateDistance(sortedList.get(i),center);
			if(distance>maxdist){
				maxdist= distance;
				farest=sortedList.get(i);
			}
		}
		double radius=calculateDistance(farest, center);
		double circleArea=radius*radius*Math.PI;
		double Area= Imgproc.contourArea(contour1);
		double divide= circleArea/Area;
		starPoints.put(divide,center);
		return divide;
	}
	
	/*
	 * Werkt niet. Proberen op basis van verschil tussen oppervlakte gemiddelde cirkel. Af te leiden wat een cirkel is.
	 */
	private double calculateCircleDifference(MatOfPoint contour1,MatOfPoint contour2){
		double averageArea= (Imgproc.contourArea(contour1) + Imgproc.contourArea(contour2))/2;
		double radius1= Math.sqrt(Imgproc.contourArea(contour1)/(Math.PI));
		double radius2=Math.sqrt(Imgproc.contourArea(contour2)/(Math.PI));
		double realArea= Math.PI*((radius1+radius2)/2)*(radius1+radius2)/2;
		double difference = Math.PI/4*(radius1*radius1+radius2*radius2-2*radius1*radius2);
		return difference;
	}
	
	/*
	 * Calculate distance between two points.
	 */
	private double calculateDistance(Point point1,Point point2){
		return Math.sqrt((point1.x-point2.x)*(point1.x-point2.x)+(point1.y-point2.y)*(point1.y-point2.y));
	}
	

	/**
	 * 
	 * @param contours
	 * 		Lists of matrix points in which to operate.
	 * @return
	 * 		Returns a new list of mat of points after adding the contours which are near each other.
	 */
	private List<MatOfPoint> addContours1(List<MatOfPoint> contours) {
		ArrayList<MatOfPoint> toRemove = new ArrayList<>();
		HashMap<Integer,Integer> fuzzyContours = new HashMap<>();
		for(int i = 0 ; i< contours.size();i++){
			MatOfPoint contour1= contours.get(i);
			for(int index = i+1; index <contours.size();index++ ){
				MatOfPoint contour2 = contours.get(index);
				if(pointsEquals(findCenter(contour1.toList()),findCenter(contour2.toList()),250)){
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
		//System.out.println("removed: " + toRemove.size());
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
	private boolean pointsEquals(Point point1, Point point2,int epsilon){
		double distance = calculateDistance(point1, point2);
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
		return new Point(x/points.size(),y/points.size());
	}
	
	static double averageRadius=0;
	/*
	 * If all points of the contour are on an equal distance to the center point. This is a circle.
	 * Works if you don't add the inner and outer contours and don't work on approximations.
	 */
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
		
		//System.out.println(difference/approx.size());
		if(difference<5*approx.size())
			return true;
		return false;
	}
	
	/*
	 * Determines if the contour is a rectangle.
	 * Returns the ratio of angles that are 180 degrees(epsilon 10 degrees) on the amount of angles that are examined.
	 */
	public double isRectangle(MatOfPoint contour){
		List<Point> sortedList = sortPolar(contour.toList());
		double a=0;
		double b=0;
		for(int p=0; p<(sortedList.size()-4); p=p+5){
			double angle = angleBetweenVectors(sortedList.get(p), sortedList.get(p+2), sortedList.get(p+4));
			b++;
			if(fuzzyEquals(angle,0,10))
				a++;
		}
			return a/b;
	}
	
	private boolean fuzzyEquals(double a,double b,double epsilon){
		if(Math.abs(a-b)>epsilon)
			return false;
		return true;
	}
	/*
	 * Sorts the points in the given list counter clockwise.
	 */
	private List<Point> sortPolar(List<Point> list) {
		Point center = findCenter(list);
		List<Point> sorted = new LinkedList<>();
		if(list.contains(center)) {
			list.remove(center);
		}
		for(Point s : list) {
			Point copy = new Point(s.x,s.y);
			//coordinate transformation
			double x0 = copy.x - center.x;
			double y0 = copy.y - center.y;
			copy.x= (x0);
			copy.y=(y0);
			sorted.add(copy);
		}
		Collections.sort(sorted,new AngleComparator());
		for(int p=0; p<sorted.size(); p++){
			sorted.get(p).x+=center.x;
			sorted.get(p).y+=center.y;
		}
		return sorted;
	}
	
	/*
	 * Return the angle between three points. 
	 * Point 2 is the center point.
	 */
	private double angleBetweenVectors(Point p1,Point p2,Point p3){
		Point vector1 = new Point(p2.x-p1.x,p2.y-p1.y);
		Point vector2 = new Point(p3.x-p2.x,p3.y-p2.y);
		double prod = vector1.x*vector2.x+vector1.y*vector2.y;
		double sum = (Math.sqrt(vector1.x*vector1.x+vector1.y*vector1.y)*Math.sqrt(vector2.x*vector2.x+vector2.y*vector2.y));
		return Math.acos(prod/sum)*180/Math.PI;
	}
	
	private static class AngleComparator implements Comparator<Point> {
		@Override
		public int compare(Point s1, Point s2) {
			if(s1 == null)
				return -1;
			if(s2 == null)
				return 1;
			if(s1.y==0 && s1.x>0)
				return -1;
			if(s2.y==0 && s2.x>0)
				return 1;
			if(s1.y>0 && s2.y<0)
				//!!because y-frame points in opposite direction
				return 1;
			if(s2.y>0 && s1.y<0)
				//!! because y-frame points in opposite direction
				return -1;
			if(1*s1.x*s2.y-s1.y*s2.x>0)
				return 1;
			return -1;
		}
	}
	
	public List<Point> reduceEqualPoints(List<Point> approx){
		ArrayList<Point> newApprox= new ArrayList<Point>(approx);
		ArrayList<Point> lonelyPoints= new ArrayList<Point>();
		List<Point> fuzzyPoints = new ArrayList<Point>();
		boolean lone=true;
		for(int i=0; i<approx.size();i++){
			Point point1 = approx.get(i);
			for(int j=i+1;j<approx.size();j++){
				Point point2 = approx.get(j);
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
	
	
	
	private void createToolbars(){
		 final JTextField h1 = new JTextField("BlurSize: "+blur);
		 final JTextField h2 = new JTextField("CannyThreshold: "+cannyThreshold);
		 final JTextField s1 = new JTextField("Erode Size:"+erodesize);
		 final JTextField s2 = new JTextField("erodetimes:"+erodeTimes);
		 final JTextField v1 = new JTextField("dilatedSize:"+dilatesize);
		 final JTextField v2 = new JTextField("dilateTimes:"+dilateTimes);
		 final JTextField minAreaField = new JTextField("Min Area:"+minArea);
		 final JTextField epsilonApproxTF = new JTextField("Approx Epsilon:"+epsilonApprox);
		 final JTextField pointsEqualEpsilonTF = new JTextField("pointsEqualEpsilonContour:"+pointsEqualEpsilon);
		 final JTextField pointsEqualEpsilonPointsTF = new JTextField("pointsEqualEpsilonPoints:"+pointsEqualEpsilonPoints);
		 JFrame frame1 = new JFrame("Toolbars");  
	     frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     JPanel frame=new JPanel(new GridLayout(0,2));
	     JSlider blurSlider= new JSlider(JSlider.HORIZONTAL,
                1, 10, blur);
	     blurSlider.addChangeListener(new ChangeListener() {
	    	 @Override
			public void stateChanged(ChangeEvent ce) {
	    		 
	    			 blur= ((JSlider) ce.getSource()).getValue();
	    			 h1.setText("BlurSize: "+blur);
	    		 
				
			}
	     });
	     JSlider cannySlider= new JSlider(JSlider.HORIZONTAL,
                0, 500, cannyThreshold);
	     cannySlider.addChangeListener(new ChangeListener() {
	    	 @Override
			public void stateChanged(ChangeEvent ce) {
	    			 cannyThreshold= ((JSlider) ce.getSource()).getValue();
	    			 h2.setText("CannyThreshold:"+cannyThreshold);
	    		 	
			}
	     });
	     JSlider erodeSizeSlider= new JSlider(JSlider.HORIZONTAL,
                1, 20, erodesize);
	     erodeSizeSlider.addChangeListener(new ChangeListener() {
	    	 @Override
			public void stateChanged(ChangeEvent ce) {
	    			 erodesize= ((JSlider) ce.getSource()).getValue();
	    			 s1.setText("erodesize:"+erodesize);
				
			}
	     });
	     JSlider erodeTimeSlider= new JSlider(JSlider.HORIZONTAL,
                0, 20, erodeTimes);
	     erodeTimeSlider.addChangeListener(new ChangeListener() {
	    	 @Override
			public void stateChanged(ChangeEvent ce) {
	    		 
	    		 	erodeTimes= ((JSlider) ce.getSource()).getValue();
	    			s2.setText("erodeTimes:"+erodeTimes);
	    		
				
			}
	     });
	     JSlider V_Min= new JSlider(JSlider.HORIZONTAL,
                1, 20, dilatesize);
	     V_Min.addChangeListener(new ChangeListener() {
	    	 @Override
			public void stateChanged(ChangeEvent ce) { 
	    			 dilatesize= ((JSlider) ce.getSource()).getValue();
	    			 v1.setText("dilatesize:"+dilatesize);
				
			}
	     });
	     JSlider V_Max= new JSlider(JSlider.HORIZONTAL,
                0, 20, dilateTimes);
	     V_Max.addChangeListener(new ChangeListener() {
	    	 @Override
			public void stateChanged(ChangeEvent ce) {
	    	
	    		 	dilateTimes = ((JSlider) ce.getSource()).getValue();
	    			 v2.setText("dilateTimes:"+dilateTimes);
	    	
				
			}
	     });
	     
	     
	     JSlider minAreaSlider= new JSlider(JSlider.HORIZONTAL,
	                1, 10000, minArea);
	     minAreaSlider.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) {
		    	
		    		 minArea = ((JSlider) ce.getSource()).getValue();
		    		 	minAreaField.setText("Min Area:"+minArea);
		    	
					
				}
		     });
	     
		     
	     
	     JSlider epsilonApproxSlider= new JSlider(JSlider.HORIZONTAL,
	                1, 50, epsilonApprox);
	     epsilonApproxSlider.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) {
		    	
		    		 epsilonApprox = ((JSlider) ce.getSource()).getValue();
		    		 	epsilonApproxTF.setText("Epsilon Approx:"+epsilonApprox);
		    	
					
				}
		     });
	     JSlider pointsEqualEpsilonSlider= new JSlider(JSlider.HORIZONTAL,
	                1, 500, pointsEqualEpsilon);
	     pointsEqualEpsilonSlider.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) {
		    	
		    		 pointsEqualEpsilon = ((JSlider) ce.getSource()).getValue();
		    		 pointsEqualEpsilonTF.setText("pointsEqualEpsilonContour:"+pointsEqualEpsilon);
		    	
					
				}
		     });
	     JSlider pointsEqualEpsilonPointsSlider= new JSlider(JSlider.HORIZONTAL,
	                1, 500, pointsEqualEpsilonPoints);
	     pointsEqualEpsilonPointsSlider.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) {
		    	
		    		 pointsEqualEpsilonPoints = ((JSlider) ce.getSource()).getValue();
		    		 pointsEqualEpsilonPointsTF.setText("pointsEqualEpsilonPoints:"+pointsEqualEpsilonPoints);
		    	
					
				}
		     });
	     frame.add(blurSlider);frame.add(h1);
	     frame.add(cannySlider);frame.add(h2);
	     frame.add(erodeSizeSlider);frame.add(s1);
	     frame.add(erodeTimeSlider);frame.add(s2);
	     frame.add(V_Min);frame.add(v1);
	     frame.add(V_Max);frame.add(v2);
	     frame.add(minAreaSlider);frame.add(minAreaField);
	     frame.add(epsilonApproxSlider);frame.add(epsilonApproxTF);
	     frame.add(pointsEqualEpsilonSlider);frame.add(pointsEqualEpsilonTF);
	     frame.add(pointsEqualEpsilonPointsSlider);frame.add(pointsEqualEpsilonPointsTF);
	     frame1.getContentPane().add(frame);
	     frame1.pack();
	     frame1.setVisible(true);
	}


}
