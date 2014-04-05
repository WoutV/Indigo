package ImageProcessing;

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
import org.opencv.core.Core.MinMaxLocResult;
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

public class templatematching {
	private int erodeTimes = 0;
	private int dilateTimes = 1;
	private int blur = 6;
	private int erodesize = 3;
	private int dilatesize = 2;
	private Mat originalImage;
	private String openCvFolder = "C:/Users/Study/Desktop/OpenCv/Processed/";
	private int cannyThresholdMin = 8;
	private int cannyThresholdMax = 18;
	private int matchMethod = 2;
	private int epsilonApprox = 2;
	private int pointsEqualEpsilon = 116;
	private int pointsEqualEpsilonPoints = 52;
	private Size frameSize = new Size(800, 700);
	private frame cannyoutput = makeFrame("Canny Output", frameSize);
	private frame erodedoutput = makeFrame("Eroded Output", frameSize);
	private frame dilatedoutput = makeFrame("Dilated Output", frameSize);
	private frame foundContours = makeFrame("Template", frameSize);;
	private frame resultFrame = makeFrame("Result", frameSize);
	private Mat circleTemplate, starTemplate, rectangleTemplate;
	MatOfPoint heartTemplate;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		templatematching lip = new templatematching();
		lip.start("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\fotos\\b (111).jpg");
	}

	public templatematching() {
		System.loadLibrary("opencv_java248");
		createToolbars();
		createTemplates();
	}

	private void createTemplates() {

		Mat heartImage1 = Highgui.imread("../fotos/heart.jpg");
		Mat heartImage = heartImage1.clone();
		Imgproc.cvtColor(heartImage, heartImage, Imgproc.COLOR_BGR2GRAY);
		// Blurring the image within three by three matrix and writing it as
		// grayimage.png
		Imgproc.blur(heartImage, heartImage, new Size(4, 4));
		// Highgui.imwrite(openCvFolder+"gray_image.png",grayImage);
		// Making some more matrixes to see the ongoing operations.
		Imgproc.Canny(heartImage, heartImage, 8, 50);
		List<MatOfPoint> heartTemplate = new ArrayList<MatOfPoint>();
		Imgproc.findContours(heartImage, heartTemplate, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		System.out.println("Heart Template Contours Size"
				+ heartTemplate.size());
		this.heartTemplate = heartTemplate.get(1);
		Imgproc.drawContours(heartImage1, heartTemplate, -1, new Scalar(0, 255,
				255));
		foundContours.matToBufferedImage(heartImage1);
		foundContours.repaint();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Mat circleImage1 = Highgui.imread("../fotos/circle.jpg");
		Mat circleImage = circleImage1.clone();
		Imgproc.cvtColor(circleImage, circleImage, Imgproc.COLOR_BGR2GRAY);
		// Blurring the image within three by three matrix and writing it as
		// grayimage.png
		Imgproc.blur(circleImage, circleImage, new Size(4, 4));
		// Highgui.imwrite(openCvFolder+"gray_image.png",grayImage);
		// Making some more matrixes to see the ongoing operations.
		Imgproc.Canny(circleImage, circleImage, 8, 50);
		List<MatOfPoint> circleTemplate = new ArrayList<MatOfPoint>();
		Imgproc.findContours(circleImage, circleTemplate, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		System.out.println("circle Template Contours Size"
				+ circleTemplate.size());
		this.circleTemplate = circleTemplate.get(0);
		Imgproc.drawContours(circleImage1, circleTemplate, -1, new Scalar(0, 255,
				255));
		foundContours.matToBufferedImage(circleImage1);
		foundContours.repaint();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Mat starImage1 = Highgui.imread("../fotos/star.jpg");
		Mat starImage = starImage1.clone();
		Imgproc.cvtColor(starImage, starImage, Imgproc.COLOR_BGR2GRAY);
		// Blurring the image within three by three matrix and writing it as
		// grayimage.png
		Imgproc.blur(starImage, starImage, new Size(4, 4));
		// Highgui.imwrite(openCvFolder+"gray_image.png",grayImage);
		// Making some more matrixes to see the ongoing operations.
		Imgproc.Canny(starImage, starImage, 8, 50);
		List<MatOfPoint> starTemplate = new ArrayList<MatOfPoint>();
		Imgproc.findContours(starImage, starTemplate, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		System.out.println("star Template Contours Size"
				+ starTemplate.size());
		this.starTemplate = starTemplate.get(0);
		Imgproc.drawContours(starImage1, starTemplate, -1, new Scalar(0, 255,
				255));
		foundContours.matToBufferedImage(starImage1);
		foundContours.repaint();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Mat rectangleImage1 = Highgui.imread("../fotos/rectangle.jpg");
		Mat rectangleImage = rectangleImage1.clone();
		Imgproc.cvtColor(rectangleImage, rectangleImage, Imgproc.COLOR_BGR2GRAY);
		// Blurring the image within three by three matrix and writing it as
		// grayimage.png
		Imgproc.blur(rectangleImage, rectangleImage, new Size(3, 3));
		// Highgui.imwrite(openCvFolder+"gray_image.png",grayImage);
		// Making some more matrixes to see the ongoing operations.
		Imgproc.Canny(rectangleImage, rectangleImage, 8, 80);
		List<MatOfPoint> rectangleTemplate = new ArrayList<MatOfPoint>();
		Imgproc.findContours(rectangleImage, rectangleTemplate, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		System.out.println("rectangle Template Contours Size"
				+ rectangleTemplate.size());
		this.rectangleTemplate = rectangleTemplate.get(0);
		Imgproc.drawContours(rectangleImage1, rectangleTemplate, -1, new Scalar(0, 255,
				255));
		foundContours.matToBufferedImage(rectangleImage1);
		foundContours.repaint();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start() {

		try {
			while (true) {
				BufferedImage buffered = ImageIO.read(new URL(
						"http://raspberrypi.mshome.net/cam_pic.php?time="
								+ System.currentTimeMillis()));
				byte[] pixels = ((DataBufferByte) buffered.getRaster()
						.getDataBuffer()).getData();

				originalImage = new Mat(buffered.getHeight(),
						buffered.getWidth(), CvType.CV_8UC3);
				originalImage.put(0, 0, pixels);
				processImage();
				Thread.sleep(200);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void start(String urlImage) {

		try {
			while (true) {
				this.originalImage = Highgui.imread(urlImage,
						Imgproc.COLOR_BGR2GRAY);
				processImage();
				//Thread.sleep(200);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		// Highgui.imwrite(openCvFolder+"cannny_output.png",canny_output);

		// eroding the image to reduce the noise;
		Mat erodedImage = canny_output.clone();
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

		System.out.println("\nRunning Template Matching");

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		
		// List<MatOfInt4> hierarchy;

		// Finding the contours.
		Imgproc.findContours(dilatedImage, contours, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		contours = removeContours(contours);
		contours = filterOutEdges(contours, originalImage.size());
		for (int i = 0; i < contours.size(); i++) {
			Rect rec = Imgproc.boundingRect(contours.get(i));
			resultFrame.matToBufferedImage(originalImage.submat(rec));
			resultFrame.repaint();
			Double shapematch = Imgproc.matchShapes(contours.get(i),
					heartTemplate, getMatchMethod(), 0);
			System.out.println("Heart:" + shapematch);
			shapematch = Imgproc.matchShapes(contours.get(i),
					starTemplate, getMatchMethod(), 0);
			System.out.println("Star:" + shapematch);
			shapematch = Imgproc.matchShapes(contours.get(i),
					circleTemplate, getMatchMethod(), 0);
			System.out.println("Circle:" + shapematch);
			shapematch = Imgproc.matchShapes(contours.get(i),
					rectangleTemplate, getMatchMethod(), 0);
			System.out.println("Rectangle:" + shapematch +"\n\n");
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private int getMatchMethod() {
		if (matchMethod == 0)
			return Imgproc.CV_CONTOURS_MATCH_I1;
		if (matchMethod == 1)
			return Imgproc.CV_CONTOURS_MATCH_I2;
		return Imgproc.CV_CONTOURS_MATCH_I3;
	}

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
	private String getColor(Mat image, Point contourCenter) {
		Mat bitImage = image.clone();
		// Checking for white
		Core.inRange(image.clone(), Colors.getWhiteMinScalar(),
				Colors.getWhiteMaxScalar(), bitImage);
		double[] col = bitImage.get((int) contourCenter.y,
				(int) contourCenter.x);
		System.out.println("col[0]" + col[0] + " length:" + col.length);
		if (col[0] >= 150)
			return "W";

		// Checking for Yellow
		Core.inRange(image.clone(), Colors.getYellowMinScalar(),
				Colors.getYellowMaxScalar(), bitImage);
		col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
		System.out.println("col[0]" + col[0] + " length:" + col.length);
		if (col[0] >= 150)
			return "Y";

		Core.inRange(image.clone(), Colors.getRedMinScalar(),
				Colors.getRedMaxScalar(), bitImage);
		col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
		System.out.println("col[0]" + col[0] + " length:" + col.length);
		if (col[0] >= 150)
			return "R";

		Core.inRange(image.clone(), Colors.getBlueMinScalar(),
				Colors.getBlueMaxScalar(), bitImage);
		col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
		System.out.println("col[0]" + col[0] + " length:" + col.length);
		if (col[0] >= 150)
			return "B";

		Core.inRange(image.clone(), Colors.getGreenMinScalar(),
				Colors.getGreenMaxScalar(), bitImage);
		col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
		System.out.println("col[0]" + col[0] + " length:" + col.length);
		if (col[0] >= 150)
			return "G";

		System.out.println("col:" + col + "MatrixSize:" + image.height() + ","
				+ image.width());
		return "NI";
	}

	/**
	 * 
	 * @param contours
	 *            Lists of matrix points in which to operate.
	 * @return Returns a new list of mat of points after adding the contours
	 *         which are near each other.
	 */
	private List<MatOfPoint> removeContours(List<MatOfPoint> contours) {
		ArrayList<MatOfPoint> toRemove = new ArrayList<>();
		HashMap<Integer, Integer> fuzzyContours = new HashMap<>();
		for (int i = 0; i < contours.size(); i++) {
			MatOfPoint contour1 = contours.get(i);
			if (Imgproc.contourArea(contour1) > 1000) {
				for (int index = i + 1; index < contours.size(); index++) {
					MatOfPoint contour2 = contours.get(index);
					if (pointsEquals(contour1.get(0, 0), contour2.get(0, 0),
							pointsEqualEpsilon)) {
						fuzzyContours.put(i, index);
						toRemove.add(contour2);
					}
				}
			}
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
																		// the
																		// frame
																		// some
																		// arbitrary
																		// size
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
		final JTextField matchMethodField = new JTextField("Match Method:"
				+ matchMethod);
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

		JSlider matchMethodSlider = new JSlider(JSlider.HORIZONTAL, 0, 5,
				matchMethod);
		matchMethodSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {

				matchMethod = ((JSlider) ce.getSource()).getValue();
				matchMethodField.setText("Match Method:" + matchMethod);

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
		frame.add(matchMethodSlider);
		frame.add(matchMethodField);
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
