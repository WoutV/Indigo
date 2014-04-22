package imageProcessingWithColorFilter;

import ImageProcessing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class ImageProcessorWithColorFiltering {
	private Integer erodeTimes = 0;
	private Integer dilateTimes = 2;
	private Integer blur = 4;
	private Integer erodesize = 3;
	private Integer dilatesize = 3;
	private Mat originalImage;
	private Integer cannyThresholdMin = 8;
	private Integer cannyThresholdMax = 13;
	private Integer minArea = 2000;
	private Integer epsilonApprox = 1;
	private Integer pointsEqualEpsilon = 50;
	private Integer pointsEqualEpsilonPoints = 52;
	private Size frameSize = new Size(800, 700);
	// private frame cannyoutput = makeFrame("Canny Output", frameSize);
	private frame erodedoutput = makeFrame("Eroded Output", frameSize);
	private frame dilatedoutput = makeFrame("Dilated Output", frameSize);
	private frame foundContours = makeFrame(
			"Found Contours(Green)/Approx Contours(Blue)", frameSize);;
	private frame resultFrame = makeFrame("Result", frameSize);

	// private frame zoomedContourFrame = makeFrame("Zoomed", frameSize);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// type 0 -> pi Image ; 1 -> video ; 2-> image
		ImageProcessorWithColorFiltering lip = new ImageProcessorWithColorFiltering(1,"C:/Users/Study/Desktop/test.wmv");
	//	ImageProcessorWithColorFiltering lip = new ImageProcessorWithColorFiltering(1,"C:/Users/Study/Dropbox/grid.h264");
		//ImageProcessorWithColorFiltering lip = new ImageProcessorWithColorFiltering(2,"../fotos/b (114)" + ".jpg");
		//lip.start();
		while(true){
		lip.startThreadProcessing();
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

	}

	boolean colorCalibration = false;
	/**
	 * @param inputType
	 * 				 type 0 -> pi Image ; 1 -> video ; 2-> image
	 * @param filepath
	 */
	public ImageProcessorWithColorFiltering(int inputType , String filepath) {
		System.loadLibrary("opencv_java248");
		createToolbars();
		symbolS = new SymbolsStabalization(6, 50);
		cc = new ColorWithCalibration("colors.txt");
		this.typeInput =inputType;
		this.filepath = filepath;
	}

	ColorWithCalibration cc;
	Integer timestamp = 0;
	private SymbolsStabalization symbolS;

	public void start() {

		try {
			while (true) {
				updateOriginalImage();
				processImage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * Process the image and writes the output images on the folder specified.
	 * 
	 * @throws InterruptedException
	 */
	private void processImage() throws InterruptedException {
		// clone the originalImage incase we need original image later.
		Mat image = originalImage;
		// Changing to black & white
		Mat grayImage = new Mat();
		// Blurring the image
		Imgproc.blur(image, grayImage, new Size(blur, blur));
		Mat greenFilter = new Mat(grayImage.size(), Core.DEPTH_MASK_8U);
		Core.inRange(grayImage, cc.getYellowMinScalar(),
				cc.getYellowMaxScalar(), greenFilter);
		// Making some more matrixes to see the ongoing operations.

		// eroding the image to reduce the noise;
		Mat erodedImage = greenFilter;
		for (int i = 0; i < erodeTimes; i++) {
			Imgproc.erode(erodedImage, erodedImage, Imgproc
					.getStructuringElement(Imgproc.MORPH_RECT, new Size(
							erodesize, erodesize)));
		}
		erodedoutput.matToBufferedImage(erodedImage);
		erodedoutput.repaint();
		// dilating to make the image clear
		Mat dilatedImage = erodedImage.clone();
		for (int i = 0; i < dilateTimes; i++) {
			Imgproc.dilate(dilatedImage, dilatedImage, Imgproc
					.getStructuringElement(Imgproc.MORPH_RECT, new Size(
							dilatesize, dilatesize)));
		}
		dilatedoutput.matToBufferedImage(dilatedImage);
		dilatedoutput.repaint();
		findContours(dilatedImage, image, new Mat(image.size(),
				Core.DEPTH_MASK_8U, new Scalar(0, 0, 0)));

	}

	private void findContours(Mat dilatedImage, Mat image1, Mat emptyImage)
			throws InterruptedException {
		Mat dilatedImage1 = dilatedImage.clone();
		Mat image = image1.clone();
		Mat imageClone = image.clone();
		// Making some list to put the points.
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

		// Finding the contours.
		Imgproc.findContours(dilatedImage, contours, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		// Matrices to put the converted contours.
		MatOfPoint2f MatOfPointTo2f = new MatOfPoint2f();
		MatOfPoint2f MatOfPoint2fApprox = new MatOfPoint2f();

		// Adding the contours within given distances.
		// System.out.println("Before adding contours size:" + contours.size());
		contours = addContours(contours);
		// System.out.println("After adding contours:" + contours.size());
		Mat contourFoundImage = image1.clone();
		Imgproc.drawContours(contourFoundImage, contours, -1, new Scalar(0, 0,
				255), 2);

		List<MatOfPoint> ApproxContours = new ArrayList<MatOfPoint>();
		// Approximating every contour
		for (int i = 0; i < contours.size(); i++) {
			contours.get(i).convertTo(MatOfPointTo2f, CvType.CV_32FC2);
			Imgproc.approxPolyDP(MatOfPointTo2f, MatOfPoint2fApprox,
					epsilonApprox, true);
			MatOfPoint mop = new MatOfPoint();
			MatOfPoint2fApprox.convertTo(mop, CvType.CV_32S);
			ApproxContours.add(mop);
		}

		/*
		 * interesting if you would like to know the convex hull MatOfInt
		 * hullInt = new MatOfInt(); List<MatOfPoint> hullPoints = new
		 * ArrayList<MatOfPoint>(); for (int k=0; k < contours.size(); k++){
		 * List<Point> hullPointList = new ArrayList<Point>(contours.size());
		 * MatOfPoint hullPointMat = new MatOfPoint();
		 * Imgproc.convexHull(contours.get(k), hullInt);
		 * 
		 * for(int j=0; j < hullInt.toList().size(); j++){
		 * hullPointList.add(contours
		 * .get(k).toList().get(hullInt.toList().get(j)));
		 * Core.circle(originalImage, hullPointList.get(j), 2, new
		 * Scalar(0,255,255)); } hullPointMat.fromList(hullPointList);
		 * hullPoints.add(hullPointMat);
		 * 
		 * }
		 * 
		 * Imgproc.drawContours( originalImage, hullPoints, -1, new
		 * Scalar(255,0,0, 255), 1);
		 * resultFrame.matToBufferedImage(originalImage); resultFrame.repaint();
		 * Thread.sleep(5000);
		 */

		ApproxContours = filterOutEdges(ApproxContours, image.size());
		// System.out.println("After filtering out edges:" +
		// ApproxContours.size());
		Imgproc.drawContours(contourFoundImage, ApproxContours, -1, new Scalar(
				255, 0, 0), 2);
		Mat contourMat = new Mat(contourFoundImage.size(), CvType.CV_8UC1);

		Imgproc.drawContours(contourMat, ApproxContours, -1, new Scalar(255, 0,
				0), 2);
		foundContours.matToBufferedImage(contourMat);
		foundContours.repaint();
		for (int i = 0; i < ApproxContours.size(); i++) {
			MatOfPoint contour1 = ApproxContours.get(i);

			ArrayList<Point> contoursPoint = new ArrayList<Point>(
					contour1.toList());

			Point contourCenter = getCenter(contour1);
			Core.circle(image, contourCenter, 4, new Scalar(255, 49, 0, 255));

			Rect rec = Imgproc.boundingRect(contour1);
			String Color = getColor(image1.submat(rec), new Point(
					rec.height / 2, rec.width / 2));

			Point center = new Point();
			float[] radius = new float[5];
			contour1.convertTo(MatOfPointTo2f, CvType.CV_32FC2);
			Imgproc.minEnclosingCircle(MatOfPointTo2f, center, radius);
			Core.rectangle(image, rec.tl(), rec.br(), new Scalar(255, 0, 0));

			for (int index = 0; index < contoursPoint.size(); index++) {
				Point p = contoursPoint.get(index);
				Core.circle(image, p, 1, new Scalar(255, 0, 0), 3);

			}

			Mat subImage = contourMat.submat(rec);

			// MatOfPoint corners = new MatOfPoint();
			//
			// Imgproc.goodFeaturesToTrack(subImage, corners, 10, 0.01,
			// Math.min(rec.height, rec.width) / 3, new Mat(), 3,
			// true, 0.04);
			//
			// System.out.println("Detected Corners:" + corners.cols());
			//
			// if (corners.cols() == 1) {
			// Mat colored = image.submat(rec);
			// Core.circle(colored, new Point(corners.get(0, 0)[0],
			// corners.get(0, 0)[1]), 2, new Scalar(0, 255,
			// 255), 1);
			// //zoomedContourFrame.matToBufferedImage(colored);
			// //zoomedContourFrame.repaint();
			//
			// //Thread.sleep(1000);
			//
			// }

			// Checking for rectangles
			if (isRectangle(subImage, rec, contourCenter)) {
				Symbol S = new Symbol(Color + "R", timestamp, contourCenter.x,
						contourCenter.y);
				S = symbolS.getPossibleSymbol(S);
				Core.putText(image, S.toString(), contourCenter,
						Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
								200, 250), 3);

			}

			else { // It is not a rectangle now checking for circles

				Mat circles = new Mat();
				if (isCircle(contours.get(i), contourCenter)) {
					Symbol S = new Symbol(Color + "C", timestamp,
							contourCenter.x, contourCenter.y);
					S = symbolS.getPossibleSymbol(S);
					Core.putText(image, S.toString(), contourCenter,
							Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
									200, 250), 3);

				}
				//
				//
				//
				// // // isCircle(approx, center)
				// Imgproc.HoughCircles(dilatedImage1.submat(rec), circles,
				// Imgproc.CV_HOUGH_GRADIENT, 1, rec.height,
				// 2 * cannyThresholdMax, 17, (int) (rec.height / 2.5),
				// 500);
				//
				// // System.out.println("Found circles:" + circles.cols());
				// for (int x = 0; x < circles.cols(); x++) {
				// double[] vec = circles.get(0, x);
				// double x1 = vec[0], y1 = vec[1], r = vec[2];
				//
				// Point start = new Point(x1, y1);
				//
				// Core.circle(subImage, start, (int) r,
				// new Scalar(0, 255, 0), 1);
				//
				// }
				// if (circles.cols() == 1) {
				// Symbol S = new Symbol(Color+"C", timestamp, contourCenter.x,
				// contourCenter.y);
				// S = symbolS.getPossibleSymbol(S);
				// Core.putText(image, S.toString(), contourCenter,
				// Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(
				// 200, 200, 250), 3);
				// //zoomedContourFrame.matToBufferedImage(subImage);
				// //zoomedContourFrame.repaint();
				//
				// // Thread.sleep(1000);
				// }

				else { // Not a circle either now checking for heart;
					Imgproc.HoughCircles(dilatedImage1.submat(rec), circles,
							Imgproc.CV_HOUGH_GRADIENT, 1, rec.height,
							2 * cannyThresholdMax, 10,
							(int) (rec.height / 2.5), 500);
					// System.out.println("Found circles:" + circles.cols());

					if (circles.cols() == 1) {
						Symbol S = new Symbol(Color + "H", timestamp,
								contourCenter.x, contourCenter.y);
						S = symbolS.getPossibleSymbol(S);
						Core.putText(image, S.toString(), contourCenter,
								Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(
										200, 200, 250), 3);
						// zoomedContourFrame.matToBufferedImage(subImage);
						// zoomedContourFrame.repaint();

						// Thread.sleep(1000);

					}

					else {
						Symbol S = new Symbol(Color + "S", timestamp,
								contourCenter.x, contourCenter.y);
						S = symbolS.getPossibleSymbol(S);
						Core.putText(image, S.toString(), contourCenter,
								Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(
										200, 200, 250), 3);
						// zoomedContourFrame.matToBufferedImage(subImage);
						// zoomedContourFrame.repaint();

						// Thread.sleep(1000);

					}
				}
			}

		}
		resultFrame.matToBufferedImage(image);
		resultFrame.repaint();
		// Thread.sleep(5000);

	}

	private Point getCenter(MatOfPoint contour1) throws InterruptedException {
		Moments p = Imgproc.moments(contour1, false);
		int x = (int) (p.get_m10() / p.get_m00());
		int y = (int) (p.get_m01() / p.get_m00());
		Point center = new Point(x, y);
		return center;
	}

	private boolean isRectangle(Mat subImage, Rect rec, Point contourCenter)
			throws InterruptedException {
		Mat lines = new Mat();
		// zoomedContourFrame.matToBufferedImage(subImage);
		// zoomedContourFrame.repaint();
		// Thread.sleep(5000);
		Imgproc.HoughLinesP(subImage, lines, 1, Math.PI / 180, 12,
				Math.max(rec.height, rec.width) / 1.25, 10);
		// System.out.println("Total Lines:" + lines.cols());
		ArrayList<Line2D> lineList = new ArrayList<>();
		for (int x = 0; x < lines.cols(); x++) {
			double[] vec = lines.get(0, x);
			lineList.add(new Line2D.Double(vec[0], vec[1], vec[2], vec[3]));

			double x1 = vec[0], y1 = vec[1], x2 = vec[2], y2 = vec[3];
			Point start = new Point(x1, y1);
			Point end = new Point(x2, y2);

			Core.line(originalImage.submat(rec), start, end, new Scalar(255, 0,
					255), 2);
			// zoomedContourFrame.matToBufferedImage(originalImage);
			// zoomedContourFrame.repaint();
			// Thread.sleep(1000);

		}
		boolean containsParallel = false;
		Line2D pline1 = null, pline2 = null;
		if (lines.cols() > 1) {
			for (int i = 0; (i < lineList.size() - 1) && !containsParallel; i++) {
				Line2D line1 = lineList.get(0);
				double angle1 = Math.atan2(line1.getY1() - line1.getY2(),
						line1.getX1() - line1.getX2());
				for (int x = i + 1; x < lineList.size(); x++) {
					Line2D line2 = lineList.get(x);
					double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
							line2.getX1() - line2.getX2());
					// System.out.println("angle between lines : "+(angle1-angle2));
					if (Math.abs(angle1 - angle2) <= 0.174532925) {
						double distanceToPoint1 = line1.ptLineDist(line2
								.getP1());
						double distanceToPoint2 = line1.ptLineDist(line2
								.getP2());
						double constraint = Math.min(rec.width, rec.height) / 3;
						if (distanceToPoint1 > constraint
								&& distanceToPoint2 > constraint) {
							containsParallel = true;
							pline1 = line1;
							pline2 = line2;
							break;
						}
					}
				}

			}

		}
		// showing the parallel lines;
		// if(containsParallel){
		// System.out.println("Parallel Detected");
		// Mat colored = originalImage.submat(rec);
		// Core.line(colored, new Point(pline1.getX1(), pline1.getY1()), new
		// Point(pline1.getX2(), pline1.getY2()), new Scalar(0, 0, 255), 2);
		// Core.line(colored, new Point(pline2.getX1(), pline2.getY1()), new
		// Point(pline2.getX2(), pline2.getY2()), new Scalar(0, 0, 255), 2);
		// zoomedContourFrame.matToBufferedImage(colored);
		// zoomedContourFrame.repaint();
		// Thread.sleep(5000);
		// }

		return containsParallel;
	}

	/**
	 * Filters out the edges
	 * 
	 * @param contours
	 * @param imageSize
	 * @return
	 */
	private List<MatOfPoint> filterOutEdges(List<MatOfPoint> contours,
			Size imageSize) {
		ArrayList<MatOfPoint> toRemove = new ArrayList<>();

		for (int i = 0; i < contours.size(); i++) {
			MatOfPoint contour1 = contours.get(i);
			ArrayList<Point> contoursPoint = new ArrayList<Point>(
					contour1.toList());
			for (int index = 0; index < contoursPoint.size(); index++) {
				Point p = contoursPoint.get(index);
				if (p.x <= 0.01 * imageSize.width
						|| p.x >= 0.99 * imageSize.width
						|| p.y <= 0.01 * imageSize.height
						|| p.y >= 0.99 * imageSize.height) {
					toRemove.add(contour1);
					break;
				}
			}

		}

		for (MatOfPoint p : toRemove) {
			contours.remove(p);
		}

		return contours;
	}

	/**
	 * 
	 * @param contours
	 *            Lists of matrix points in which to operate.
	 * @return Returns a new list of mat of points after adding the contours
	 *         which are near each other.
	 */
	private List<MatOfPoint> addContours(List<MatOfPoint> contours) {
		ArrayList<MatOfPoint> toRemove = new ArrayList<>();
		HashMap<Integer, Integer> fuzzyContours = new HashMap<>();
		for (int i = 0; i < contours.size(); i++) {
			MatOfPoint contour1 = contours.get(i);
			if (Imgproc.contourArea(contour1) >= minArea) {
				for (int index = i + 1; index < contours.size(); index++) {
					MatOfPoint contour2 = contours.get(index);
					if (pointsEquals(contour1.get(0, 0), contour2.get(0, 0),
							pointsEqualEpsilon)
							&& Imgproc.contourArea(contour2) >= minArea) {
						fuzzyContours.put(i, index);
						toRemove.add(contour2);
					}
				}
			} else {
				toRemove.add(contour1);
			}

		}
		for (int j = 0; j < fuzzyContours.size(); j++) {
			int a = (int) fuzzyContours.keySet().toArray()[j];
			int b = fuzzyContours.get(fuzzyContours.keySet().toArray()[j]);
			ArrayList<Point> firstone = new ArrayList<>(contours.get(a)
					.toList());
			ArrayList<Point> secondone = new ArrayList<>(contours.get(b)
					.toList());
			for (int q = 0; q < firstone.size(); q++) {
				secondone.add(firstone.get(q));
			}
			contours.get(a).fromList(secondone);
		}

		for (MatOfPoint p : toRemove) {
			contours.remove(p);
		}
		// System.out.println("removed: " + toRemove.size());
		return contours;
	}

	/**
	 * Returns true if the distance between the given points is less than the
	 * given epsilon.
	 * 
	 * @param point1
	 * @param point2
	 * @param epsilon
	 *            The margin to look if the given points are almost equal.
	 * @return
	 * 
	 */
	private boolean pointsEquals(double[] point1, double[] point2, int epsilon) {
		double distance = Math.sqrt((point1[0] - point2[0])
				* (point1[0] - point2[0]) + (point1[1] - point2[1])
				* (point1[1] - point2[1]));
		if (distance < epsilon)
			return true;
		return false;
	}

	double averageRadius = 0;

	/*
	 * If all points of the contour are on an equal distance to the center
	 * point. This is a circle. Works if you don't add the inner and outer
	 * contours and don't work on approximations.
	 */
	public boolean isCircle(MatOfPoint contour, Point center) {
		List<Point> approx = contour.toList();
		double radiussum = 0;
		double x;
		double y;
		for (int i = 0; i < approx.size(); i++) {
			x = approx.get(i).x;
			y = approx.get(i).y;
			radiussum += Math.sqrt((x - center.x) * (x - center.x)
					+ (y - center.y) * (y - center.y));
		}

		averageRadius = radiussum / approx.size();
		double difference = 0;
		for (int j = 0; j < approx.size(); j++) {
			x = approx.get(j).x;
			y = approx.get(j).y;
			double radius = Math.sqrt((x - center.x) * (x - center.x)
					+ (y - center.y) * (y - center.y));
			difference += (averageRadius - radius) * (averageRadius - radius);
		}
		double treshold = 7;
		if (Imgproc.contourArea(contour) < 1150)
			treshold = 3;
		// System.out.println("Center:x:" +center.x + "y:" + center.y +
		// "diff/approx:"+ difference/approx.size());
		if (difference / approx.size() < treshold)
			return true;
		return false;
	}

	private List<Point> reduceEqualPoints(List<Point> approx) {
		ArrayList<Point> newApprox = new ArrayList<Point>(approx);
		ArrayList<Point> lonelyPoints = new ArrayList<Point>();
		List<Point> fuzzyPoints = new ArrayList<Point>();
		double[] point1 = new double[2];
		double[] point2 = new double[2];
		boolean lone = true;
		for (int i = 0; i < approx.size(); i++) {
			point1[0] = approx.get(i).x;
			point1[1] = approx.get(i).y;
			for (int j = i + 1; j < approx.size(); j++) {
				point2[0] = approx.get(j).x;
				point2[1] = approx.get(j).y;
				if (pointsEquals(point1, point2, pointsEqualEpsilonPoints)) {
					fuzzyPoints.add(approx.get(j));
					lone = false;
				}
			}
			if (lone) {
				lonelyPoints.add(approx.get(i));
			}
			lone = true;

		}
		for (int k = 0; k < fuzzyPoints.size(); k++) {
			if (newApprox.contains(fuzzyPoints.get(k)))
				newApprox.remove(fuzzyPoints.get(k));
		}
		for (int p = 0; p < lonelyPoints.size(); p++) {
			if (newApprox.contains(lonelyPoints.get(p)))
				newApprox.remove(lonelyPoints.get(p));
		}
		return newApprox;
	}

	private String getColor(Mat image, Point contourCenter) {
		try {
			Mat bitImage = image.clone();
			// Checking for white
			Core.inRange(image.clone(), cc.getWhiteMinScalar(),
					cc.getWhiteMaxScalar(), bitImage);
			// System.out.println("Center Points height:" + contourCenter.x
			// + ", width:" + contourCenter.y);
			double[] col = bitImage.get((int) contourCenter.y,
					(int) contourCenter.x);
			// System.out.println("Matrix Size size:" + image.size());
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "W";

			// Checking for Yellow
			Core.inRange(image.clone(), cc.getYellowMinScalar(),
					cc.getYellowMaxScalar(), bitImage);
			col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "Y";

			Core.inRange(image.clone(), cc.getRedMinScalar(),
					cc.getRedMaxScalar(), bitImage);
			col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "R";

			Core.inRange(image.clone(), cc.getBlueMinScalar(),
					cc.getBlueMaxScalar(), bitImage);
			col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "B";

			Core.inRange(image.clone(), cc.getGreenMinScalar(),
					cc.getGreenMaxScalar(), bitImage);
			col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "G";

			// System.out.println("col:" + col + "MatrixSize:" + image.height()
			// + "," + image.width());
			return "X";
		} catch (Exception e) {
			return "X";
		}
	}

	/**
	 * The title of the frame
	 * 
	 * @param Title
	 * @return
	 */
	private frame makeFrame(String title, Size frameSize) {
		// make the JFrame
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame facePanel = new frame();
		frame.setSize((int) frameSize.width, (int) frameSize.height); // give
		frame.setBackground(Color.BLUE);
		frame.add(facePanel, BorderLayout.CENTER);
		frame.setVisible(true);
		return facePanel;
	}

	private void createToolbars() {
		final JTextField blurTF = new JTextField("Blur Size:" + blur);
		final JTextField cannyThresholdMinTF = new JTextField(
				"CannyMinThreshold:" + cannyThresholdMin);
		final JTextField cannyThresholdMaxTF = new JTextField(
				"CannyMaxThreshold:" + cannyThresholdMax);
		final JTextField erodesizeTF = new JTextField("Erode Size:" + erodesize);
		final JTextField erodetimesTF = new JTextField("Erode Times:"
				+ erodeTimes);
		final JTextField dilatesizeTF = new JTextField("Dilate Size:"
				+ dilatesize);
		final JTextField dilatetimesTF = new JTextField("Dilate Times:"
				+ dilateTimes);
		final JTextField minAreaField = new JTextField("Min Area:" + minArea);
		final JTextField epsilonApproxTF = new JTextField("Epsilon Approx:"
				+ epsilonApprox);
		final JTextField pointsEqualEpsilonTF = new JTextField(
				"PointsEqualEpsilonContour:" + pointsEqualEpsilon);
		final JTextField pointsEqualEpsilonPointsTF = new JTextField(
				"PointsEqualEpsilonPoints:" + pointsEqualEpsilonPoints);
		JFrame frame1 = new JFrame("Toolbars");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel frame = new JPanel(new GridLayout(0, 2));
		JSlider blurSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, blur);
		blurSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {

				blur = ((JSlider) ce.getSource()).getValue();
				blurTF.setText("Blur Size: " + blur);

			}
		});
		JSlider cannyMinSlider = new JSlider(JSlider.HORIZONTAL, 0, 500,
				cannyThresholdMin);
		cannyMinSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				cannyThresholdMin = ((JSlider) ce.getSource()).getValue();
				cannyThresholdMinTF.setText("CannyMinThreshold:"
						+ cannyThresholdMin);

			}
		});
		JSlider cannyMaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 500,
				cannyThresholdMax);
		cannyMaxSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				cannyThresholdMax = ((JSlider) ce.getSource()).getValue();
				cannyThresholdMaxTF.setText("CannyMaxThreshold:"
						+ cannyThresholdMax);

			}
		});
		JSlider erodeSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 20,
				erodesize);
		erodeSizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				erodesize = ((JSlider) ce.getSource()).getValue();
				erodesizeTF.setText("Erode Size:" + erodesize);

			}
		});
		JSlider erodeTimeSlider = new JSlider(JSlider.HORIZONTAL, 0, 20,
				erodeTimes);
		erodeTimeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {

				erodeTimes = ((JSlider) ce.getSource()).getValue();
				erodetimesTF.setText("Erode Times:" + erodeTimes);

			}
		});
		JSlider dilatesizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 20,
				dilatesize);
		dilatesizeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				dilatesize = ((JSlider) ce.getSource()).getValue();
				dilatesizeTF.setText("Dilate Size:" + dilatesize);

			}
		});
		JSlider dilatetimesSlider = new JSlider(JSlider.HORIZONTAL, 0, 20,
				dilateTimes);
		dilatetimesSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {

				dilateTimes = ((JSlider) ce.getSource()).getValue();
				dilatetimesTF.setText("Dilate Times:" + dilateTimes);

			}
		});

		JSlider minAreaSlider = new JSlider(JSlider.HORIZONTAL, 1, 10000,
				minArea);
		minAreaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {

				minArea = ((JSlider) ce.getSource()).getValue();
				minAreaField.setText("Min Area:" + minArea);

			}
		});

		JSlider epsilonApproxSlider = new JSlider(JSlider.HORIZONTAL, 1, 50,
				epsilonApprox);
		epsilonApproxSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {

				epsilonApprox = ((JSlider) ce.getSource()).getValue();
				epsilonApproxTF.setText("Epsilon Approx:" + epsilonApprox);

			}
		});
		JSlider pointsEqualEpsilonSlider = new JSlider(JSlider.HORIZONTAL, 1,
				500, pointsEqualEpsilon);
		pointsEqualEpsilonSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {

				pointsEqualEpsilon = ((JSlider) ce.getSource()).getValue();
				pointsEqualEpsilonTF.setText("PointsEqualEpsilonContour:"
						+ pointsEqualEpsilon);

			}
		});
		JSlider pointsEqualEpsilonPointsSlider = new JSlider(
				JSlider.HORIZONTAL, 1, 500, pointsEqualEpsilonPoints);
		pointsEqualEpsilonPointsSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {

				pointsEqualEpsilonPoints = ((JSlider) ce.getSource())
						.getValue();
				pointsEqualEpsilonPointsTF.setText("PointsEqualEpsilonPoints:"
						+ pointsEqualEpsilonPoints);

			}
		});
		frame.add(blurSlider);
		frame.add(blurTF);
		frame.add(cannyMinSlider);
		frame.add(cannyThresholdMinTF);
		frame.add(cannyMaxSlider);
		frame.add(cannyThresholdMaxTF);
		frame.add(erodeSizeSlider);
		frame.add(erodesizeTF);
		frame.add(erodeTimeSlider);
		frame.add(erodetimesTF);
		frame.add(dilatesizeSlider);
		frame.add(dilatesizeTF);
		frame.add(dilatetimesSlider);
		frame.add(dilatetimesTF);
		frame.add(minAreaSlider);
		frame.add(minAreaField);
		frame.add(epsilonApproxSlider);
		frame.add(epsilonApproxTF);
		frame.add(pointsEqualEpsilonSlider);
		frame.add(pointsEqualEpsilonTF);
		frame.add(pointsEqualEpsilonPointsSlider);
		frame.add(pointsEqualEpsilonPointsTF);
		frame1.getContentPane().add(frame);
		frame1.pack();
		frame1.setVisible(true);
	}

	Boolean blueDone = false;
	Boolean greenDone = false;
	Boolean redDone = false;
	Boolean whiteDone = false;
	Boolean yellowDone = false;
	Boolean canBlueResume ;
	Boolean canGreenResume ;
	Boolean canRedResume ;
	Boolean canWhiteResume ;
	Boolean canYellowResume ;
	ArrayList<Symbol> blueSymbols;
	ArrayList<Symbol> greenSymbols;
	ArrayList<Symbol> redSymbols;
	ArrayList<Symbol> whiteSymbols;
	ArrayList<Symbol> yellowSymbols;
	SymbolDetectorThread blueSymbolDetectorThread;
	SymbolDetectorThread greenSymbolDetectorThread;
	SymbolDetectorThread redSymbolDetectorThread;
	SymbolDetectorThread whiteSymbolDetectorThread;
	SymbolDetectorThread yellowSymbolDetectorThread;
	boolean threadInitialized = false;

	public void startSymbolDetectorThreads() {
		blueSymbols = new ArrayList<>();
		greenSymbols = new ArrayList<>();
		redSymbols = new ArrayList<>();
		whiteSymbols = new ArrayList<>();
		yellowSymbols = new ArrayList<>();
		blueSymbolDetectorThread = new SymbolDetectorThread(blueDone,
				 blueSymbols,
				cc.getBlueMinScalar(), cc.getBlueMaxScalar(), symbolS,
				timestamp, "B");
		canBlueResume = blueSymbolDetectorThread.getCanResume();
		greenSymbolDetectorThread = new SymbolDetectorThread(greenDone,
				  greenSymbols,
				cc.getGreenMinScalar(), cc.getGreenMaxScalar(), symbolS,
				timestamp, "G");
		canGreenResume = greenSymbolDetectorThread.getCanResume();
		redSymbolDetectorThread = new SymbolDetectorThread(redDone,
				  redSymbols, cc.getRedMinScalar(),
				cc.getRedMaxScalar(), symbolS, timestamp, "R");
		canRedResume = redSymbolDetectorThread.getCanResume();
		whiteSymbolDetectorThread = new SymbolDetectorThread(whiteDone,
				  whiteSymbols,
				cc.getWhiteMinScalar(), cc.getWhiteMaxScalar(), symbolS,
				timestamp, "W");
		canWhiteResume = whiteSymbolDetectorThread.getCanResume();
		yellowSymbolDetectorThread = new SymbolDetectorThread(yellowDone,
				  yellowSymbols,
				cc.getYellowMinScalar(), cc.getYellowMaxScalar(), symbolS,
				timestamp, "Y");
		canYellowResume = yellowSymbolDetectorThread.getCanResume();
		blueSymbolDetectorThread.initializeToolbarVariables(erodeTimes,
				dilateTimes, blur, erodesize, dilatesize, minArea,
				epsilonApprox, cannyThresholdMax);
		greenSymbolDetectorThread.initializeToolbarVariables(erodeTimes,
				dilateTimes, blur, erodesize, dilatesize, minArea,
				epsilonApprox, cannyThresholdMax);
		redSymbolDetectorThread.initializeToolbarVariables(erodeTimes,
				dilateTimes, blur, erodesize, dilatesize, minArea,
				epsilonApprox, cannyThresholdMax);
		whiteSymbolDetectorThread.initializeToolbarVariables(erodeTimes,
				dilateTimes, blur, erodesize, dilatesize, minArea,
				epsilonApprox, cannyThresholdMax);
		yellowSymbolDetectorThread.initializeToolbarVariables(erodeTimes,
				dilateTimes, blur, erodesize, dilatesize, minArea,
				epsilonApprox, cannyThresholdMax);
		Thread blueThread = new Thread(blueSymbolDetectorThread);
		Thread greenThread = new Thread(greenSymbolDetectorThread);
		Thread redThread = new Thread(redSymbolDetectorThread);
		Thread whiteThread = new Thread(whiteSymbolDetectorThread);
		Thread yellowThread = new Thread(yellowSymbolDetectorThread);
		blueThread.start();
		greenThread.start();
		redThread.start();
		whiteThread.start();
		yellowThread.start();
		System.out.println("Threads Up and Running");
	}

	public void startThreadProcessing() {
		
		if (!threadInitialized){
			startSymbolDetectorThreads();
			threadInitialized= true;
		}
		updateOriginalImage();
		
		synchronized(canBlueResume){
			canBlueResume.notify();
		}
		synchronized(canGreenResume){
			canGreenResume.notify();
		}
		synchronized(canRedResume){
			canRedResume.notify();
		}
		synchronized(canWhiteResume){
			canWhiteResume.notify();
		}
		synchronized(canYellowResume){
			canYellowResume.notify();
		}
		try {
			System.out.println("Waiting for processing");
			Thread.sleep(200);
//			synchronized(blueDone){
//				blueDone.wait();
//			}
//			
//			
//			synchronized(greenDone){
//				greenDone.wait();
//			}
//			
//			synchronized(redDone){
//				redDone.wait();
//			}
//			
//			synchronized(whiteDone){
//				whiteDone.wait();
//			}
//			
//			synchronized(yellowDone){
//				yellowDone.wait();
//			}
			System.out.println("Waiting finished");
			Mat contourMat = new Mat(originalImage.size(), CvType.CV_8UC1);
			Mat blueBinImage = blueSymbolDetectorThread.getBinaryMat();
			Mat greenBinImage = greenSymbolDetectorThread.getBinaryMat();
			Core.bitwise_or(blueBinImage, greenBinImage, contourMat);
			Mat redBinImage= redSymbolDetectorThread.getBinaryMat();
			Core.bitwise_or(contourMat, redBinImage, contourMat);
			Mat whiteBinImage= whiteSymbolDetectorThread.getBinaryMat();
			Core.bitwise_or(contourMat, whiteBinImage, contourMat);
			Mat yellowBinImage = yellowSymbolDetectorThread.getBinaryMat();
			Core.bitwise_or(contourMat, yellowBinImage, contourMat);
			foundContours.matToBufferedImage(contourMat);
			foundContours.repaint();
			resultFrame.matToBufferedImage(originalImage);
			resultFrame.repaint();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	int typeInput;
	String filepath; // type 0 -> pi Image ; 1 -> video ; 2-> image
	boolean vcInitialized = false;
	VideoCapture vc;

	private void updateOriginalImage() {
		try {
			if (typeInput == 0) {
				BufferedImage buffered = ImageIO.read(new URL(
						"http://raspberrypi.mshome.net/cam_pic.php?time="
								+ System.currentTimeMillis()));
				byte[] pixels = ((DataBufferByte) buffered.getRaster()
						.getDataBuffer()).getData();

				originalImage = new Mat(buffered.getHeight(),
						buffered.getWidth(), CvType.CV_8UC3);
				originalImage.put(0, 0, pixels);
				timestamp++;
				symbolS.increaseTimestamp();
			} else if (typeInput == 1) {
				this.originalImage = new Mat();
				if (!vcInitialized){
					vc = new VideoCapture(filepath);
					vcInitialized=true;
				}
				vc.read(originalImage);
				timestamp++;
				symbolS.increaseTimestamp();
			} else {
				this.originalImage = Highgui.imread(filepath,
						Imgproc.COLOR_BGR2GRAY);
				// Thread.sleep(200);
				timestamp++;
				symbolS.increaseTimestamp();

			}

			blueSymbolDetectorThread.updateImage(originalImage);
			greenSymbolDetectorThread.updateImage(originalImage);
			redSymbolDetectorThread.updateImage(originalImage);
			whiteSymbolDetectorThread.updateImage(originalImage);
			yellowSymbolDetectorThread.updateImage(originalImage);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
