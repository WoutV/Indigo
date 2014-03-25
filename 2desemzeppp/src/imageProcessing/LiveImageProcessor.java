package imageProcessing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
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
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class LiveImageProcessor {
	private int blur = 5;
	private Mat originalImage;
	private int cannyThresholdMin = 8;
	private int cannyThresholdMax = 27;
	private int minArea = 1000;
	private int epsilonApprox = 1;
	private int pointsEqualEpsilon = 50;
	private int pointsEqualEpsilonPoints = 52;
	private Size frameSize = new Size(800, 700);
	private frame cannyoutput = makeFrame("Canny Output", frameSize);
	private frame foundContours = makeFrame(
			"Found Contours(Green)/Approx Contours(Blue)", frameSize);;
	private frame resultFrame = makeFrame("Result", frameSize);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LiveImageProcessor lip = new LiveImageProcessor();
		// lip.start("../fotos/b (110)" + ".jpg");
		lip.start();
		lip.startVideoProcessing("C:/Users/Study/Dropbox/grid.h264");
	}

	public LiveImageProcessor() {
		System.loadLibrary("opencv_java248");
		createToolbars();
	}

	public void start() {

		try {
			while (true) {
				double startTime = System.currentTimeMillis();
				BufferedImage buffered = ImageIO.read(new URL(
						"http://raspberrypi.mshome.net/cam_pic.php?time="
								+ System.currentTimeMillis()));
				byte[] pixels = ((DataBufferByte) buffered.getRaster()
						.getDataBuffer()).getData();

				originalImage = new Mat(buffered.getHeight(),
						buffered.getWidth(), CvType.CV_8UC3);
				originalImage.put(0, 0, pixels);
				processImage();
				System.out.println("Time taken to process: " +(System.currentTimeMillis()-startTime)/1000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start(String urlImage) {
		while (true) {
			try {
				this.originalImage = Highgui.imread(urlImage,
						Imgproc.COLOR_BGR2GRAY);
				processImage();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void startVideoProcessing(String videoPath) {
		this.originalImage=new Mat();
		VideoCapture vc = new VideoCapture(videoPath);
		while (true) {
			try {
				vc.read(originalImage);
				processImage();
				//Thread.sleep(200);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Process the image and writes the output images on the folder specified.
	 */
	private void processImage() {
		// clone the originalImage incase we need original image later.
		Mat image = originalImage.clone();
		// Changing to black & white
		Mat grayImage = new Mat();
		Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
		// Blurring the image within three by three matrix and writing it as
		// grayimage.png
		Imgproc.blur(grayImage, grayImage, new Size(blur, blur));
		// Highgui.imwrite(openCvFolder+"gray_image.png",grayImage);
		// Making some more matrixes to see the ongoing operations.
		Mat canny_output = new Mat(image.size(), Core.DEPTH_MASK_8U);
		Imgproc.Canny(grayImage, canny_output, cannyThresholdMin,
				2 * cannyThresholdMax);
		cannyoutput.matToBufferedImage(canny_output);
		cannyoutput.repaint();
		Mat dilatedImage = canny_output.clone();
		for (int i = 0; i < 1; i++) {
			Imgproc.dilate(dilatedImage, dilatedImage, Imgproc
					.getStructuringElement(Imgproc.MORPH_RECT, new Size(
							3, 3)));
		}
		findContours(dilatedImage, image, new Mat(image.size(),
				Core.DEPTH_MASK_8U, new Scalar(0, 0, 0)));
	}

	private void findContours(Mat dilatedImage, Mat image1, Mat emptyImage) {
		Mat dilatedImage1 = dilatedImage.clone();
		Mat image = image1.clone();
		Mat imageClone = image.clone();
		// Making some list to put the points.
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		// List<MatOfInt4> hierarchy;

		// Finding the contours.
		Imgproc.findContours(dilatedImage, contours, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		// Matrices to put the converted contours.
		MatOfPoint2f MatOfPointTo2f = new MatOfPoint2f();
		MatOfPoint2f MatOfPoint2fApprox = new MatOfPoint2f();

		// Adding the contours within given distances.
		//System.out.println("Before adding contours size:" + contours.size());
		contours = addContours(contours);
		//System.out.println("After adding contours:" + contours.size());
		Mat contourFoundImage = image1.clone();
		Imgproc.drawContours(contourFoundImage, contours, -1, new Scalar(0, 0,
				255), 2);

		List<MatOfPoint> ApproxContours = new ArrayList<MatOfPoint>();
		// Approximating every contour
		for (int i = 0; i < contours.size(); i++) {
			if (Imgproc.contourArea(contours.get(i)) > minArea) {
				contours.get(i).convertTo(MatOfPointTo2f, CvType.CV_32FC2);
				Imgproc.approxPolyDP(MatOfPointTo2f, MatOfPoint2fApprox,
						epsilonApprox, true);
				MatOfPoint mop = new MatOfPoint();
				MatOfPoint2fApprox.convertTo(mop, CvType.CV_32S);
				ApproxContours.add(mop);
			}
		}
		ApproxContours = filterOutEdges(ApproxContours, image.size());
		//System.out.println("After filtering out edges:" + ApproxContours.size());
		Imgproc.drawContours(contourFoundImage, ApproxContours, -1, new Scalar(
				255, 0, 0), 2);
		foundContours.matToBufferedImage(contourFoundImage);
		foundContours.repaint();
		for (int i = 0; i < ApproxContours.size(); i++) {
			MatOfPoint contour1 = ApproxContours.get(i);
			ArrayList<Point> contoursPoint = new ArrayList<Point>(
					contour1.toList());
			Point contourCenter = findCenter(contoursPoint);
			Rect rec = Imgproc.boundingRect(contour1);
			Point center = new Point();
			float[] radius = new float[5];
			contour1.convertTo(MatOfPointTo2f, CvType.CV_32FC2);
			Imgproc.minEnclosingCircle(MatOfPointTo2f, center, radius);
			Core.circle(image, center, (int) radius[0], new Scalar(255, 0, 0),
					3);
			Core.rectangle(image, rec.tl(), rec.br(), new Scalar(255, 0, 0));

			for (int index = 0; index < contoursPoint.size(); index++) {
				Point p = contoursPoint.get(index);
				Core.circle(imageClone, p, 1, new Scalar(255, 0, 0), 3);

			}

			// Checking for rectangles
			Mat lines = new Mat();
			Imgproc.HoughLinesP(dilatedImage1.submat(rec), lines, 1,
					Math.PI / 180, 2, rec.height / 2, 5);
			double height = rec.height;
			double width = rec.width;
			if (rec.height < rec.width) {
				double heightdummy = width;
				width = height;
				height = heightdummy;

			}
			String Color = Colors.getColor(image1.submat(rec), new Point(
					height / 2, width / 2));
			// if it is rectangle then lines.cols is not null -> Check for ipad!
			if (lines.cols() > 0) {
				Core.putText(image, Color + ":Rec", contourCenter,
						Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
								200, 250), 3);
				// TODO:check if it is ipad?
			} else { // It is not a rectangle now checking for circles
				Mat circles = new Mat();
				Imgproc.HoughCircles(dilatedImage1.submat(rec), circles,
						Imgproc.CV_HOUGH_GRADIENT, 1, rec.height,
						2 * cannyThresholdMax, 17, (int) (rec.height / 2.5),
						500);
				if (circles.cols() == 1) {
					Core.putText(image, Color + ":Cir", contourCenter,
							Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(200,
									200, 250), 3);
				} else { // Not a circle either now checking for heart;
					Imgproc.HoughCircles(dilatedImage1.submat(rec), circles,
							Imgproc.CV_HOUGH_GRADIENT, 1, rec.height,
							2 * cannyThresholdMax, 10,
							(int) (rec.height / 2.5), 500);
					if (circles.cols() == 1) {
						Core.putText(image, Color + ":Heart", contourCenter,
								Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(
										200, 200, 250), 3);
					}
					else {
						Core.putText(image, Color + ":Star", contourCenter,
								Core.FONT_HERSHEY_COMPLEX_SMALL, 2, new Scalar(
										200, 200, 250), 3);
					}
				}
			}

		}
		resultFrame.matToBufferedImage(image);
		resultFrame.repaint();
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
			if(Imgproc.contourArea(contour1)>=minArea){
				for (int index = i + 1; index < contours.size(); index++) {
					MatOfPoint contour2 = contours.get(index);
					if (pointsEquals(contour1.get(0, 0), contour2.get(0, 0),
						pointsEqualEpsilon)&& Imgproc.contourArea(contour2)>=minArea) {
						fuzzyContours.put(i, index);
						toRemove.add(contour2);
					}
				}
			}
			else{
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

	static double averageRadius = 0;

	public boolean isCircle(List<Point> approx, Point center) {
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

		System.out.println(difference / approx.size());
		if (difference < 16 * approx.size())
			return true;
		return false;
	}

	public List<Point> reduceEqualPoints(List<Point> approx) {
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
		frame.setSize((int) frameSize.width, (int) frameSize.height); 
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
		JSlider blurSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, blur);
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
		JSlider minAreaSlider = new JSlider(JSlider.HORIZONTAL, 1, 1500,
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

}
