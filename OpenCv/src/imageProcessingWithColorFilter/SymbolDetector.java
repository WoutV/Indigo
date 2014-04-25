package imageProcessingWithColorFilter;

import java.awt.geom.Line2D;
import java.util.ArrayList;
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
import org.opencv.imgproc.Moments;

import ImageProcessing.ColorWithCalibration;
import ImageProcessing.Symbol;
import ImageProcessing.SymbolsStabalization;

class SymbolDetector {
	Mat image = new Mat();
	ArrayList<Symbol> symbolList;
	Scalar min;
	Scalar max;
	SymbolsStabalization symbolS;
	int timestamp;
	String Color;
	Mat binImageMat;

	/**
	 * Iniatilizes the symboldetector;
	 * 
	 * @param toNotify
	 *            The boolean to notify after the processing has been done.
	 * @param imageToProcess
	 *            The image to process;
	 * @param symbolList
	 *            The arraylist to put the symbols
	 */
	public SymbolDetector(ArrayList<Symbol> symbolList, Scalar min, Scalar max,
			SymbolsStabalization symbolS, int timestamp, String Color) {
		this.symbolList = symbolList;
		this.symbolS = symbolS;
		this.timestamp = timestamp;
		this.Color = Color;
		this.min = min;
		this.max = max;

	}

	public Mat getBinaryMat() {
		return binImageMat;
	}

	private Integer erodeTimes;
	private Integer dilateTimes;
	private Integer blur;
	private Integer erodesize;
	private Integer dilatesize;
	private Integer heartThreshold;
	private Integer minArea;
	private Integer epsilonApprox;

	public void initializeToolbarVariables(Integer erodeTimes,
			Integer dilateTimes, Integer blur, Integer erodesize,
			Integer dilatesize, Integer minArea, Integer epsilonApprox,
			Integer heartThreshold) {
		this.erodeTimes = erodeTimes;
		this.dilateTimes = dilateTimes;
		this.erodesize = erodesize;
		this.dilatesize = dilatesize;
		this.blur = blur;
		this.minArea = minArea;
		this.heartThreshold = heartThreshold;
		this.epsilonApprox = epsilonApprox;
	}

	public void setTimeStamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public void updateImage(Mat image) {
		timestamp++;
		this.image = image.clone();
		result = image;
	}

	Mat result;

	public Mat getResult() {
		return result;
	}

	/**
	 * Process the image and writes the output images on the folder specified.
	 * 
	 * @throws InterruptedException
	 */
	public void processImage() throws InterruptedException {
		Mat blurImage = image;
		// Blurring the image
	//	Imgproc.blur(image, blurImage, new Size(blur, blur));
		Mat colorFilter = new Mat(blurImage.size(), Core.DEPTH_MASK_8U);
		Core.inRange(blurImage, min, max, colorFilter);
		// Making some more matrixes to see the ongoing operations.

		// eroding the image to reduce the noise;
		Mat erodedImage = colorFilter;
		for (int i = 0; i < erodeTimes; i++) {
			Imgproc.erode(erodedImage, erodedImage, Imgproc
					.getStructuringElement(Imgproc.MORPH_RECT, new Size(
							erodesize, erodesize)));
		}
		// dilating to make the image clear
		Mat dilatedImage = erodedImage.clone();
		// for (int i = 0; i < dilateTimes; i++) {
		// Imgproc.dilate(dilatedImage, dilatedImage, Imgproc
		// .getStructuringElement(Imgproc.MORPH_RECT, new Size(
		// dilatesize, dilatesize)));
		// }
		this.binImageMat = dilatedImage.clone();
		findContours(dilatedImage, image, new Mat(image.size(),
				Core.DEPTH_MASK_8U, new Scalar(0, 0, 0)));

	}

	private void findContours(Mat dilatedImage, Mat image1, Mat emptyImage)
			throws InterruptedException {
		Mat image = image1.clone();
		// Making some list to put the points.
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

		// Finding the contours.
		Imgproc.findContours(dilatedImage, contours, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		// Matrices to put the converted contours.
		MatOfPoint2f MatOfPointTo2f = new MatOfPoint2f();
		MatOfPoint2f MatOfPoint2fApprox = new MatOfPoint2f();

		Mat contourFoundImage = image1.clone();
		Imgproc.drawContours(contourFoundImage, contours, -1, new Scalar(0, 0,
				255), 2);

		List<MatOfPoint> ApproxContours = new ArrayList<MatOfPoint>();
		// Approximating every contour
		for (int i = 0; i < contours.size(); i++) {
			contours.get(i).convertTo(MatOfPointTo2f, CvType.CV_32FC2);
			double arcLength = Imgproc.arcLength(MatOfPointTo2f, true);
			Imgproc.approxPolyDP(MatOfPointTo2f, MatOfPoint2fApprox, arcLength/100, true);
			MatOfPoint mop = new MatOfPoint();
			MatOfPoint2fApprox.convertTo(mop, CvType.CV_32S);
			ApproxContours.add(mop);
		}

		ApproxContours = filterOutEdges(ApproxContours, image.size());
		// System.out.println("After filtering out edges:" +
		// ApproxContours.size());
		Imgproc.drawContours(contourFoundImage, ApproxContours, -1, new Scalar(
				255, 0, 0), 2);

		Mat contourMat = new Mat(contourFoundImage.size(), CvType.CV_8UC1);

		Imgproc.drawContours(contourMat, ApproxContours, -1, new Scalar(255, 0,
				0), 2);
		for (int i = 0; i < ApproxContours.size(); i++) {
			MatOfPoint contour1 = ApproxContours.get(i);
			if (Imgproc.contourArea(contour1) >= minArea) {
				
				
				
				
				
				Thread.sleep(10);
				ArrayList<Point> contoursPoint = new ArrayList<Point>(
						contour1.toList());

				Point contourCenter = getCenter(contour1);
				Core.circle(result, contourCenter, 4, new Scalar(255, 49, 0,
						255));

				Rect rec = Imgproc.boundingRect(contour1);

				Point center = new Point();
				float[] radius = new float[5];
				contour1.convertTo(MatOfPointTo2f, CvType.CV_32FC2);
				Imgproc.minEnclosingCircle(MatOfPointTo2f, center, radius);
				Core.rectangle(result, rec.tl(), rec.br(),
						new Scalar(255, 0, 0));

				for (int index = 0; index < contoursPoint.size(); index++) {
					Point p = contoursPoint.get(index);
					Core.circle(result, p, 1, new Scalar(255, 0, 0), 3);

				}

				Mat subImage = contourMat.submat(rec);
				
				
				
				
				
				MatOfPoint corners = new MatOfPoint();
				Imgproc.goodFeaturesToTrack(subImage, corners, 10, 0.001, 3);
//				Imgproc.goodFeaturesToTrack(subImage, corners, 10, 0.01,
//						Math.min(rec.height, rec.width) / 3, new Mat(), 3,
//						true, 0.04);
	
				System.out.println("Detected Corners:" + corners.cols());
	
				if (corners.cols() == 1) {
					Mat colored = image.submat(rec);
					Core.circle(colored, new Point(corners.get(0, 0)[0],
							corners.get(0, 0)[1]), 2, new Scalar(0, 255,
							255), 1);
					//zoomedContourFrame.matToBufferedImage(colored);
					//zoomedContourFrame.repaint();
	
					// Thread.sleep(1000);
	
				}

				// Checking for rectangles
				if (isRectangle(subImage, rec, contourCenter, contour1)) {
					Symbol S = new Symbol(Color + "R", timestamp,
							contourCenter.x, contourCenter.y);
					S = symbolS.getPossibleSymbol(S);
					Core.putText(result, S.toString(), contourCenter,
							Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
									200, 250), 3);

				}

				else { // It is not a rectangle now checking for circles

					Mat circles = new Mat();
					// if (isCircle(contours.get(i), contourCenter)) {
					// Symbol S = new Symbol(Color + "C", timestamp,
					// contourCenter.x, contourCenter.y);
					// S = symbolS.getPossibleSymbol(S);
					// Core.putText(result, S.toString(), contourCenter,
					// Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
					// 200, 250), 3);
					//
					// }
					//
					//
					//
					// // // isCircle(approx, center)
					Imgproc.HoughCircles(subImage, circles,
							Imgproc.CV_HOUGH_GRADIENT, 1, rec.height, 25, 17,
							(int) (rec.height / 2.5), 500);

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
					if (circles.cols() == 1 && Imgproc.isContourConvex(contour1)) {
						Symbol S = new Symbol(Color + "C", timestamp,
								contourCenter.x, contourCenter.y);
						S = symbolS.getPossibleSymbol(S);
						Core.putText(image, S.toString(), contourCenter,
								Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(
										200, 200, 250), 3);
						// //zoomedContourFrame.matToBufferedImage(subImage);
						// //zoomedContourFrame.repaint();
						//
						// // Thread.sleep(1000);
					}

					else { // Not a circle either now checking for heart;
						Imgproc.HoughCircles(subImage, circles,
								Imgproc.CV_HOUGH_GRADIENT, 1, rec.height,
								heartThreshold, 10, (int) (rec.height / 2.5),
								500);
						// System.out.println("Found circles:" +
						// circles.cols());

						if (circles.cols() == 1
								&& !Imgproc.isContourConvex(contour1)) {
							Symbol S = new Symbol(Color + "H", timestamp,
									contourCenter.x, contourCenter.y);
							S = symbolS.getPossibleSymbol(S);
							Core.putText(result, S.toString(), contourCenter,
									Core.FONT_HERSHEY_COMPLEX_SMALL, 2,
									new Scalar(200, 200, 250), 3);
							// zoomedContourFrame.matToBufferedImage(subImage);
							// zoomedContourFrame.repaint();

							// Thread.sleep(1000);

						}

						else {
							
							
							if(contour1.size().height==10 && !Imgproc.isContourConvex(contour1)){
								Symbol S = new Symbol(Color + "S", timestamp,
										contourCenter.x, contourCenter.y);
								S = symbolS.getPossibleSymbol(S);
								Core.putText(result, S.toString(), contourCenter,
										Core.FONT_HERSHEY_COMPLEX_SMALL, 2,
										new Scalar(200, 200, 250), 3);
							}
							// Still need to check for the line in star.
							// Symbol S = new Symbol(Color + "UNKNOWN",
							// timestamp,
							// contourCenter.x, contourCenter.y);
							// S = symbolS.getPossibleSymbol(S);
							else{
							Core.putText(result, Color + "X", contourCenter,
									Core.FONT_HERSHEY_COMPLEX_SMALL, 2,
									new Scalar(200, 200, 250), 3);
							}
						}//STAR CLOSE
					}//HEART CLOSE
				}//CIRCLE CLOSE
			
			} // first area close
		
		
		}

	}
	public void setResult(Mat result){
		this.result = result;
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

	frame frame;

	public void setFrame(frame frame) {
		this.frame = frame;
	}

	private boolean isRectangle(Mat subImage, Rect rec, Point contourCenter,
			MatOfPoint contour1) throws InterruptedException {
		Mat lines = new Mat();
		System.out.println("Convex:" + Imgproc.isContourConvex(contour1));
		// if(Color.equals("G")){
		// frame.matToBufferedImage(subImage);
		// frame.repaint();
		//
		// Thread.sleep(5000);}
		Mat canny_output = new Mat(subImage.size(), Core.DEPTH_MASK_8U);
		Imgproc.Canny(image.submat(rec), canny_output, 8, 2 * 27);

		for (int i = 0; i < 2; i++) {
			Imgproc.dilate(canny_output, canny_output, Imgproc
					.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));
		}
		// Imgproc.HoughLines(subImage, lines, rho, theta, threshold, srn, stn)
		// if(Color.equals("G")){
		// frame.matToBufferedImage(subImage);
		// frame.repaint();
		//
		// Thread.sleep(1000);}
		Imgproc.HoughLinesP(canny_output, lines, 1, Math.PI / 180, 2,
				Math.max(rec.height, rec.width) / 1.25,
				Math.min(rec.height, rec.width) * 0.25);
		// System.out.println("Total Lines:" + lines.cols());
		ArrayList<Line2D> lineList = new ArrayList<>();
		for (int x = 0; x < lines.cols(); x++) {
			double[] vec = lines.get(0, x);
			lineList.add(new Line2D.Double(vec[0], vec[1], vec[2], vec[3]));

			double x1 = vec[0], y1 = vec[1], x2 = vec[2], y2 = vec[3];
			Point start = new Point(x1, y1);
			Point end = new Point(x2, y2);
			// if(Color.equals("G")){
			// Core.line(this.image.submat(rec), start, end, new Scalar(255, 0,
			// 255), 2);
			// frame.matToBufferedImage(this.image);
			// frame.repaint();
			//
			// Thread.sleep(1000);}

		}
		boolean containsParallel = false;
		Line2D pline1 = null, pline2 = null;
		if (lines.cols() > 1 && Imgproc.isContourConvex(contour1)) {
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
		return containsParallel;
	}

	/**
	 * Returns the center of a contour using moments(see opencv moments)
	 * 
	 * @param contour1
	 * @return
	 * @throws InterruptedException
	 */
	private Point getCenter(MatOfPoint contour1) throws InterruptedException {
		Moments p = Imgproc.moments(contour1, false);
		int x = (int) (p.get_m10() / p.get_m00());
		int y = (int) (p.get_m01() / p.get_m00());
		Point center = new Point(x, y);
		return center;
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

}
