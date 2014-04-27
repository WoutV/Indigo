package imageProcessingWithColorFilter;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import ImageProcessing.ColorWithCalibration;
import ImageProcessing.Symbol;
import ImageProcessing.SymbolsStabalization;

class SymbolDetector1 {
	private Mat image = new Mat();
	private ArrayList<Symbol> detectedSymbols;
	private SymbolsStabalization symbolS;
	int timestamp;
	String Color;
	Mat binImageMat;

	public SymbolDetector1(ColorWithCalibration cc,
			SymbolsStabalization symbolS3, Integer timestamp2) {
		this.cc = cc;
		this.timestamp = timestamp2;
		this.symbolS = symbolS3;
	}

	public Mat getBinaryMat() {
		return binImageMat;
	}

	private Integer erodeTimes;
	private Integer dilateTimes;
	private Integer erodesize;
	private Integer dilatesize;

	public void initializeToolbarVariables(Integer erodeTimes,
			Integer dilateTimes, Integer erodesize, Integer dilatesize,
			Integer minArea, Integer epsilonApprox, Integer heartThreshold) {
		this.erodeTimes = erodeTimes;
		this.dilateTimes = dilateTimes;
		this.erodesize = erodesize;
		this.dilatesize = dilatesize;
	}

	public void setTimeStamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public void updateImage(Mat image) {
		timestamp++;
		this.image = image.clone();
		result = image;
		NotProcessedImage = image.clone();
	}

	Mat NotProcessedImage;
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
		detectedSymbols = new ArrayList<Symbol>();
		Mat blurImage = image;
		// Blurring the image
		// Imgproc.blur(image, blurImage, new Size(blur, blur));
		Mat contourMat = new Mat(image.size(), CvType.CV_8UC1);
		Mat blueBinImage = new Mat(image.size(), CvType.CV_8UC1);
		Core.inRange(blurImage, cc.getBlueMinScalar(), cc.getBlueMaxScalar(),
				blueBinImage);

		Mat greenBinImage = new Mat(image.size(), CvType.CV_8UC1);
		Core.inRange(blurImage, cc.getGreenMinScalar(), cc.getGreenMaxScalar(),
				greenBinImage);
		Core.bitwise_or(blueBinImage, greenBinImage, contourMat);

		Mat redBinImage = new Mat(image.size(), CvType.CV_8UC1);
		Core.inRange(blurImage, cc.getRedMinScalar(), cc.getRedMaxScalar(),
				redBinImage);

		Core.bitwise_or(contourMat, redBinImage, contourMat);

		Mat whiteBinImage = new Mat(image.size(), CvType.CV_8UC1);
		Core.inRange(blurImage, cc.getWhiteMinScalar(), cc.getWhiteMaxScalar(),
				whiteBinImage);
		Core.bitwise_or(contourMat, whiteBinImage, contourMat);
		Mat yellowBinImage = new Mat(image.size(), CvType.CV_8UC1);
		Core.inRange(blurImage, cc.getYellowMinScalar(),
				cc.getYellowMaxScalar(), yellowBinImage);
		Core.bitwise_or(contourMat, yellowBinImage, contourMat);
		// Making some more matrixes to see the ongoing operations.
		// eroding the image to reduce the noise;
		Mat erodedImage = contourMat;
		for (int i = 0; i < erodeTimes; i++) {
			Imgproc.erode(erodedImage, erodedImage, Imgproc
					.getStructuringElement(Imgproc.MORPH_RECT, new Size(
							erodesize, erodesize)));
		}
		// dilating to make the image clear
		Mat dilatedImage = erodedImage.clone();
		for (int i = 0; i < dilateTimes; i++) {
			Imgproc.dilate(dilatedImage, dilatedImage, Imgproc
					.getStructuringElement(Imgproc.MORPH_RECT, new Size(
							dilatesize, dilatesize)));
		}
		this.binImageMat = dilatedImage.clone();
		findContours(dilatedImage, image, new Mat(image.size(),
				Core.DEPTH_MASK_8U, new Scalar(0, 0, 0)));

	}

	int namer = 0;

	private void findContours(Mat dilatedImage, Mat image1, Mat emptyImage)
			throws InterruptedException {
		ArrayList<Symbol> detectedSymbols = new ArrayList<Symbol>();
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
			Imgproc.approxPolyDP(MatOfPointTo2f, MatOfPoint2fApprox,
					arcLength / 100, true);
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
			if (Imgproc.contourArea(contour1) >= 400) {
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

				Color = getColor(NotProcessedImage, contourCenter);

				/*
				 * Works if used on deletedcontours. And not on an
				 * approximation.
				 */
				boolean isConvex = Imgproc.isContourConvex(contour1);

				// HULL POINTS
				// interesting if you would like to know the convex hull
				// MatOfInt
				MatOfInt hullInt = new MatOfInt();
				List<MatOfPoint> hullPoints = new ArrayList<MatOfPoint>();
				
					List<Point> hullPointList = new ArrayList<Point>(
							contours.size());
					MatOfPoint hullPointMat = new MatOfPoint();
					Imgproc.convexHull(contour1, hullInt);

					for (int j = 0; j < hullInt.toList().size(); j++) {
						hullPointList.add(contour1.toList()
								.get(hullInt.toList().get(j)));
						Core.circle(result, hullPointList.get(j), 2,
								new Scalar(0, 255, 255));
					}
					hullPointMat.fromList(hullPointList);
					hullPoints.add(hullPointMat);

				if(timestamp == 6){
					Thread.sleep(10000);
					}

				Imgproc.drawContours(result, hullPoints, -1, new Scalar(255, 0,
						0, 255), 1);
				double ratioAreaHullContour = Imgproc.contourArea(hullPointMat)/Imgproc.contourArea(contour1);
				
				Core.putText(result, "" + ratioAreaHullContour, new Point(contourCenter.x+20,contourCenter.y+20),
						Core.FONT_HERSHEY_COMPLEX_SMALL, 1, new Scalar(200,
								200, 250), 1);
				// isCircleDetection

				Mat circles = new Mat();
				Core.putText(result, "" + timestamp, new Point(20, 20),
						Core.FONT_HERSHEY_COMPLEX_SMALL, 1, new Scalar(200,
								200, 250), 1);

				double rationArea = getSmallestToBiggestCircleRatio(
						contour1.toList(), contourCenter);
				// Core.putText(result, "" + (isRectangle(contour1)), new Point(
				// contourCenter.x + 20, contourCenter.y + 20),
				// Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
				// 200, 250), 3);
				Imgproc.HoughCircles(subImage, circles,
						Imgproc.CV_HOUGH_GRADIENT, 1, rec.height, 25, 17,
						(int) (rec.height / 2.5), 500);
				if (circles.cols() == 1 && isConvex) {
					Symbol S = new Symbol(Color + "C", timestamp,
							contourCenter.x, contourCenter.y);
					S = symbolS.getPossibleSymbol(S);
					detectedSymbols.add(S);
					Core.putText(result, S.toString(), contourCenter,
							Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
									200, 250), 3);
					// //zoomedContourFrame.matToBufferedImage(subImage);
					// //zoomedContourFrame.repaint();
					//
					// // Thread.sleep(1000);
				}

				// Checking for rectangles
				else if (isRectangle(subImage, rec, contourCenter, contour1)) {
					System.out.println("Creating Rectangle");
					Symbol S = new Symbol(Color + "R", timestamp,
							contourCenter.x, contourCenter.y);
					S = symbolS.getPossibleSymbol(S);
					detectedSymbols.add(S);
					System.out.println("possible symbol:" + S.toString());
					Core.putText(result, S.toString(), contourCenter,
							Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
									200, 250), 3);

				}
				// Heart Detection
				// else if (isRectangle(contours.get(i)) > 0.4
				// && isRectangle(contours.get(i)) < 0.7 && !isConvex) {
				// System.out.println("Creating Heart:" + Color +"H");
				// Symbol S = new Symbol(Color + "H", timestamp,
				// contourCenter.x, contourCenter.y);
				// S = symbolS.getPossibleSymbol(S);detectedSymbols.add(S);
				// Core.putText(result, S.toString(), contourCenter,
				// Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
				// 200, 250), 3);
				// }

				else if (rationArea < 4.5 && !isConvex
						&& !isCircle(contour1, contourCenter, rec) && ratioAreaHullContour>1.025 && ratioAreaHullContour <1.1) {
					// System.out.println("Creating Heart:" + Color + "H");
					Symbol S = new Symbol(Color + "H", timestamp,
							contourCenter.x, contourCenter.y);
					S = symbolS.getPossibleSymbol(S);
					detectedSymbols.add(S);
					Core.putText(result, S.toString(), contourCenter,
							Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
									200, 250), 3);
				}
				// Star Detection
				else if (!isConvex && rationArea >= 4.5) {
					Symbol S = new Symbol(Color + "S", timestamp,
							contourCenter.x, contourCenter.y);
					S = symbolS.getPossibleSymbol(S);
					detectedSymbols.add(S);
					Core.putText(result, S.toString(), contourCenter,
							Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
									200, 250), 3);
				}
				// Still need to check for the line in star.
				// Symbol S = new Symbol(Color + "UNKNOWN",
				// timestamp,
				// contourCenter.x, contourCenter.y);
				// S = symbolS.getPossibleSymbol(S);detectedSymbols.add(S);
				else {
					Symbol S = new Symbol(Color + "U", timestamp,
							contourCenter.x, contourCenter.y);
					S = symbolS.getPossibleSymbol(S);
					detectedSymbols.add(S);
					Core.putText(result, S.toString(), contourCenter,
							Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
									200, 250), 3);
				}

			}

		}
		this.detectedSymbols = detectedSymbols;
		checkForTriangle();

	}

	public void checkForTriangle() {
		boolean triangleFound = false;
		if (detectedSymbols.size() >= 3) {
			for (int i = 0; i < detectedSymbols.size(); i++) {
				Symbol symbol1 = detectedSymbols.get(i);
				for (int p = i + 1; p < detectedSymbols.size(); p++) {
					Symbol symbol2 = detectedSymbols.get(p);
					double distance1 = symbol2.getDistanceTo(symbol1);
					for (int q = p + 1; q < detectedSymbols.size(); q++) {
						Symbol symbol3 = detectedSymbols.get(q);
						double distance2 = symbol3.getDistanceTo(symbol1);
						double distance3 = symbol3.getDistanceTo(symbol2);
						if (fuzzyEquals(distance1, distance2, 20)
								&& fuzzyEquals(distance1, distance3, 20)
								&& fuzzyEquals(distance2, distance3, 20)) {
							Core.line(result,
									new Point(symbol1.getX(), symbol1.getY()),
									new Point(symbol2.getX(), symbol2.getY()),
									new Scalar(50 * i, 50 * i, 0));
							Core.line(result,
									new Point(symbol1.getX(), symbol1.getY()),
									new Point(symbol3.getX(), symbol3.getY()),
									new Scalar(50 * i, 50 * i, 0));
							Core.line(result,
									new Point(symbol3.getX(), symbol3.getY()),
									new Point(symbol2.getX(), symbol2.getY()),
									new Scalar(50 * i, 50 * i, 0));
							triangleFound = true;
						}

					}
				}
			}
		}
		if (!triangleFound && detectedSymbols.size() >= 2) {
			Symbol middleSymbol = getMiddleSymbol();
			Symbol nearestSymbol = getNearestSymbol(middleSymbol);
			double distance = middleSymbol.getDistanceTo(nearestSymbol);
			for (MatOfPoint contourPoints : edgeContours) {
				Point contourCenter = getCenter(contourPoints);
				double distanceToNearestSymbol = nearestSymbol
						.getDistanceTo(contourCenter);
				double distanceToMiddleSymbol = middleSymbol
						.getDistanceTo(contourCenter);
				if (fuzzyEquals(distance, distanceToNearestSymbol, 50)
						&& fuzzyEquals(distance, distanceToMiddleSymbol, 50)) {
					String color = getColor(NotProcessedImage, contourCenter);
					Core.line(
							result,
							new Point(middleSymbol.getX(), middleSymbol.getY()),
							new Point(nearestSymbol.getX(), nearestSymbol
									.getY()), new Scalar(0, 255, 255));
					Core.line(result, new Point(nearestSymbol.getX(),
							nearestSymbol.getY()), contourCenter, new Scalar(0,
							255, 255));
					Core.line(result, new Point(middleSymbol.getX(),
							middleSymbol.getY()), contourCenter, new Scalar(0,
							255, 255));
				}
			}
		}

	}

	private Symbol getNearestSymbol(Symbol middleSymbol) {
		Symbol toReturnSymbol = null;
		double distanceToSymbol = Double.MAX_VALUE;
		for (Symbol s : detectedSymbols) {
			double distance = s.getDistanceTo(middleSymbol);
			if (distance != 0 && distance < distanceToSymbol) {
				toReturnSymbol = s;
				distanceToSymbol = distance;
			}
		}
		return toReturnSymbol;
	}

	private Symbol getMiddleSymbol() {
		double distanceToMiddleSymbol = Double.MAX_VALUE;
		Symbol toReturnSymbol = null;
		double x1 = image.size().width / 2;
		double y1 = image.size().height / 2;
		for (Symbol s : detectedSymbols) {

			double distance = Math.sqrt((x1 - s.getX()) * (x1 - s.getX())
					+ (y1 - s.getY()) * (y1 - s.getY()));
			if (distance < distanceToMiddleSymbol) {
				toReturnSymbol = s;
				distanceToMiddleSymbol = distance;
			}

		}
		return toReturnSymbol;
	}

	public void setResult(Mat result) {
		this.result = result;
	}

	ArrayList<MatOfPoint> edgeContours;

	/**
	 * Filters out the edges
	 * 
	 * @param contours
	 * @param imageSize
	 * @return
	 */
	private List<MatOfPoint> filterOutEdges(List<MatOfPoint> contours,
			Size imageSize) {
		edgeContours = new ArrayList<MatOfPoint>();
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
			edgeContours.add(p);
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
		Mat canny_output = subImage;
		// frame.matToBufferedImage(subImage);
		// frame.repaint();
		//
		// Thread.sleep(5000);
		// Mat canny_output = new Mat(subImage.size(), Core.DEPTH_MASK_8U);
		// Imgproc.Canny(image.submat(rec), canny_output, 8, 2 * 27);

		// for (int i = 0; i < 2; i++) {
		// Imgproc.dilate(canny_output, canny_output, Imgproc
		// .getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2)));
		// }
		// Imgproc.HoughLines(subImage, lines, rho, theta, threshold, srn, stn)

		// frame.matToBufferedImage(canny_output);
		// frame.repaint();
		// Thread.sleep(5000);
		// Imgproc.HoughLinesP(subImage, lines, rho, theta, threshold,
		// minLineLength, maxLineGap)
		// Imgproc.HoughLinesP(subImage, lines, rho, theta, threshold,
		// minLineLength, maxLineGap);
		// Imgproc.HoughLinesP(subImage, lines, 1, Math.PI/180, 1);
		Imgproc.HoughLinesP(canny_output, lines, 1, Math.PI / 180, 1,
				Math.max(rec.height, rec.width) * 0.3, 2);
		// System.out.println("Total Lines:" + lines.cols());
		ArrayList<Line2D> lineList = new ArrayList<>();
		for (int x = 0; x < lines.cols(); x++) {
			double[] vec = lines.get(0, x);
			lineList.add(new Line2D.Double(vec[0], vec[1], vec[2], vec[3]));

			double x1 = vec[0], y1 = vec[1], x2 = vec[2], y2 = vec[3];
			Point start = new Point(x1, y1);
			Point end = new Point(x2, y2);

			Core.line(result.submat(rec), start, end, new Scalar(255, 0, 255),
					2);
			// frame.matToBufferedImage(this.image);
			// frame.repaint();
			//
			// Thread.sleep(1000);
		}

		boolean containsParallel = false;
		boolean containsRighAngle = false;
		if (lines.cols() > 1 && Imgproc.isContourConvex(contour1)) {
			for (int i = 0; (i < lineList.size() - 1)
					&& (!containsParallel || !containsRighAngle); i++) {
				Line2D line1 = lineList.get(0);
				double angle1 = Math.atan2(line1.getY1() - line1.getY2(),
						line1.getX1() - line1.getX2());
				for (int x = i + 1; x < lineList.size(); x++) {
					Line2D line2 = lineList.get(x);
					double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
							line2.getX1() - line2.getX2());
					// System.out.println("angle between lines : "+(angle1-angle2));

					double distanceToPoint1 = line1.ptLineDist(line2.getP1());
					double distanceToPoint2 = line1.ptLineDist(line2.getP2());
					// System.out.println("Angle Between Lines :"+
					// Math.toDegrees(Math.abs(angle1-angle2)));
					if (Math.toDegrees(Math.abs(angle1 - angle2)) <= 20) {
						double constraint = Math.min(rec.width, rec.height) / 3;
						if (distanceToPoint1 > constraint
								&& distanceToPoint2 > constraint) {
							containsParallel = true;
							break;
						}
					}
					if (fuzzyEquals(
							Math.abs(Math.toDegrees((angle1 - angle2))), 90, 10)
							|| fuzzyEquals(
									Math.abs(Math.toDegrees((angle1 - angle2))),
									270, 15)) {
						System.out.println("Checking rightAngle");
						double constraint = 5;
						if (distanceToPoint1 < constraint
								|| distanceToPoint2 < constraint) {
							containsRighAngle = true;
							break;
						}
					}
				}

			}

		}
		return containsParallel || containsRighAngle;
	}

	/**
	 * Returns the center of a contour using moments(see opencv moments)
	 * 
	 * @param contour1
	 * @return
	 * @throws InterruptedException
	 */
	private Point getCenter(MatOfPoint contour1) {
		Moments p = Imgproc.moments(contour1, false);
		int x = (int) (p.get_m10() / p.get_m00());
		int y = (int) (p.get_m01() / p.get_m00());
		Point center = new Point(x, y);
		return center;
	}

	double averageRadius = 0;

	// /*
	// * If all points of the contour are on an equal distance to the center
	// * point. This is a circle. Works if you don't add the inner and outer
	// * contours and don't work on approximations.
	// */
	// public boolean isCircle(MatOfPoint contour, Point center) {
	// List<Point> approx = contour.toList();
	// double radiussum = 0;
	// double x;
	// double y;
	// for (int i = 0; i < approx.size(); i++) {
	// x = approx.get(i).x;
	// y = approx.get(i).y;
	// radiussum += Math.sqrt((x - center.x) * (x - center.x)
	// + (y - center.y) * (y - center.y));
	// }
	//
	// averageRadius = radiussum / approx.size();
	// double difference = 0;
	// for (int j = 0; j < approx.size(); j++) {
	// x = approx.get(j).x;
	// y = approx.get(j).y;
	// double radius = Math.sqrt((x - center.x) * (x - center.x)
	// + (y - center.y) * (y - center.y));
	// difference += (averageRadius - radius) * (averageRadius - radius);
	// }
	// double treshold = 7;
	// if (Imgproc.contourArea(contour) < 1150)
	// treshold = 3;
	// // System.out.println("Center:x:" +center.x + "y:" + center.y +
	// // "diff/approx:"+ difference/approx.size());
	// if (difference / approx.size() < treshold)
	// return true;
	// return false;
	// }

	private boolean isCircle(MatOfPoint contour, Point center, Rect rec) {
		List<Point> approx = contour.toList();
		double x;
		double y;
		boolean isCircle = true;
		for (int i = 0; i < approx.size(); i++) {
			x = approx.get(i).x;
			y = approx.get(i).y;
			double radius = Math.sqrt((x - center.x) * (x - center.x)
					+ (y - center.y) * (y - center.y));
			if (radius < (Math.max(rec.height, rec.width) * 0.5) * 0.75) {
				isCircle = false;
				break;
			}

		}
		return isCircle;

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

	ColorWithCalibration cc;

	/*
	 * Determines if the contour is a rectangle. Returns the ratio of angles
	 * that are 180 degrees(epsilon 10 degrees) on the amount of angles that are
	 * examined.
	 */
	public double isRectangle(MatOfPoint contour) {
		List<Point> sortedList = sortPolar(new ArrayList<Point>(
				contour.toList()));
		double a = 0;
		double b = 0;
		if (sortedList.size() < 60) {
			for (int p = 0; p < (sortedList.size() - 3); p = p + 4) {
				double angle = angleBetweenVectors(sortedList.get(p),
						sortedList.get(p + 1), sortedList.get(p + 3));
				b++;
				if (fuzzyEquals(angle, 0, 10))
					a++;
			}
		} else {
			for (int p = 0; p < (sortedList.size() - 4); p = p + 5) {
				double angle = angleBetweenVectors(sortedList.get(p),
						sortedList.get(p + 2), sortedList.get(p + 4));
				b++;
				if (fuzzyEquals(angle, 0, 10))
					a++;
			}
		}
		return a / (b);
	}

	private boolean fuzzyEquals(double a, double b, double epsilon) {
		if (Math.abs(a - b) > epsilon)
			return false;
		return true;
	}

	/**
	 * Returns the center of the given contour.
	 * 
	 * @param points
	 * @return The center of the list.
	 */
	private Point findCenter(List<Point> points) {
		double x = 0;
		double y = 0;
		for (int i = 0; i < points.size(); i++) {
			x += points.get(i).x;
			y += points.get(i).y;

		}
		return new Point(x / points.size(), y / points.size());
	}

	/*
	 * Sorts the points in the given list counter clockwise.
	 */
	private List<Point> sortPolar(List<Point> list) {
		Point center = findCenter(list);
		List<Point> sorted = new LinkedList<>();
		if (list.contains(center)) {
			list.remove(center);
		}
		for (Point s : list) {
			Point copy = new Point(s.x, s.y);
			// coordinate transformation
			double x0 = copy.x - center.x;
			double y0 = copy.y - center.y;
			copy.x = (x0);
			copy.y = (y0);
			sorted.add(copy);
		}
		Collections.sort(sorted, new AngleComparator());
		for (int p = 0; p < sorted.size(); p++) {
			sorted.get(p).x += center.x;
			sorted.get(p).y += center.y;
		}
		return sorted;
	}

	/*
	 * Return the angle between three points. Point 2 is the center point.
	 */
	private double angleBetweenVectors(Point p1, Point p2, Point p3) {
		Point vector1 = new Point(p2.x - p1.x, p2.y - p1.y);
		Point vector2 = new Point(p3.x - p2.x, p3.y - p2.y);
		double prod = vector1.x * vector2.x + vector1.y * vector2.y;
		double sum = (Math.sqrt(vector1.x * vector1.x + vector1.y * vector1.y) * Math
				.sqrt(vector2.x * vector2.x + vector2.y * vector2.y));
		return Math.acos(prod / sum) * 180 / Math.PI;
	}

	private static class AngleComparator implements Comparator<Point> {
		@Override
		public int compare(Point s1, Point s2) {
			if (s1 == null)
				return -1;
			if (s2 == null)
				return 1;
			if (s1.y == 0 && s1.x > 0)
				return -1;
			if (s2.y == 0 && s2.x > 0)
				return 1;
			if (s1.y > 0 && s2.y < 0)
				// !!because y-frame points in opposite direction
				return 1;
			if (s2.y > 0 && s1.y < 0)
				// !! because y-frame points in opposite direction
				return -1;
			if (1 * s1.x * s2.y - s1.y * s2.x > 0)
				return 1;
			return -1;
		}
	}

	private double getSmallestToBiggestCircleRatio(List<Point> list,
			Point center) {
		double smallestRadius = Double.MAX_VALUE;
		double biggestRadius = 0;
		for (Point p : list) {
			double x = p.x;
			double y = p.y;
			double radius = Math.sqrt((x - center.x) * (x - center.x)
					+ (y - center.y) * (y - center.y));
			if (radius < smallestRadius) {
				smallestRadius = radius;
			}
			if (radius > biggestRadius) {
				biggestRadius = radius;
			}
		}
		return biggestRadius * biggestRadius
				/ (smallestRadius * smallestRadius);

	}

}
