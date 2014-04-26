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

public class ImageProcessorWithColorFiltering1 {
	private Integer erodeTimes = 0;
	private Integer dilateTimes = 1;
	private Integer blur = 0;
	private Integer erodesize = 3;
	private Integer dilatesize = 3;
	private Mat originalImage;
	private Integer cannyThresholdMin = 8;
	private Integer cannyThresholdMax = 13;
	private Integer minArea = 1000;
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
//		 ImageProcessorWithColorFiltering1 lip = new
//		 ImageProcessorWithColorFiltering1(
//		 1, "C:/Users/Study/Dropbox/grid.h264");
		ImageProcessorWithColorFiltering1 lip = new ImageProcessorWithColorFiltering1(
				2, "C:\\Users\\Study\\Desktop\\OpenCv\\latest fotos\\foto");
//		
		// ImageProcessorWithColorFiltering lip = new
		// ImageProcessorWithColorFiltering(2,"../fotos/b (114)" + ".jpg");
		while (true) {
			lip.startThreadProcessing();
			try {
				Thread.sleep(500);
				System.out
						.println("-----------------------------------------------------------");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	boolean colorCalibration = false;

	/**
	 * @param inputType
	 *            type 0 -> pi Image ; 1 -> video ; 2-> image
	 * @param filepath
	 */
	public ImageProcessorWithColorFiltering1(int inputType, String filepath) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		createToolbars();
		symbolS = new SymbolsStabalization(3, 50);
		cc = new ColorWithCalibration("colors.txt");
		this.typeInput = inputType;
		this.filepath = filepath;
	}

	ColorWithCalibration cc;
	Integer timestamp = 0;
	private SymbolsStabalization symbolS;


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

	ArrayList<Symbol> Symbols;
	SymbolDetector1 symbolDetector;
	boolean threadInitialized = false;

	public void startSymbolDetectorThreads() {
		Symbols = new ArrayList<>();
		symbolDetector = new SymbolDetector1(Symbols,cc, symbolS,timestamp);
		symbolDetector.setFrame(erodedoutput);
		symbolDetector.initializeToolbarVariables(erodeTimes, dilateTimes,
				 erodesize, dilatesize, minArea, epsilonApprox,
				cannyThresholdMax);
		System.out.println("Threads Up and Running");
	}

	public void startThreadProcessing() {

		if (!threadInitialized) {
			startSymbolDetectorThreads();
			threadInitialized = true;
//			for(int i =0;i<10;i++){
//				updateOriginalImage();
//			}
		}

		updateOriginalImage();
		try {
			symbolDetector.processImage();
			Mat contourMat = symbolDetector.getBinaryMat();
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
				Highgui.imwrite("C:\\Users\\Study\\Desktop\\OpenCv\\latest fotos\\"+"foto"+timestamp+".jpg", originalImage);
				timestamp++;
				symbolS.increaseTimestamp();
			} else if (typeInput == 1) {
				this.originalImage = new Mat();
				if (!vcInitialized) {
					vc = new VideoCapture(filepath);
					vcInitialized = true;
				}
				vc.read(originalImage);
				timestamp++;
				symbolS.increaseTimestamp();
			} else {
				this.originalImage = Highgui.imread(filepath+timestamp+".jpg",
						Imgproc.COLOR_BGR2GRAY);
				// Thread.sleep(200);
				timestamp++;
				symbolS.increaseTimestamp();

			}

			symbolDetector.updateImage(originalImage);
			System.out.println("Timestamp:"+timestamp);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
