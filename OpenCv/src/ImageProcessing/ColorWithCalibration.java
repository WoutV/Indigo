package ImageProcessing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class ColorWithCalibration implements ActionListener {

	public static void main(String[] args) throws InterruptedException {
		ColorWithCalibration c = new ColorWithCalibration("colors.txt");
		Mat webcam_image =Highgui.imread("C:\\Users\\Study\\Desktop\\OpenCv\\latest fotos\\colorcali.jpg");
		//VideoCapture webCam = new VideoCapture("C:/Users/Study/Dropbox/grid.h264");
		
			while (true) {
				Thread.sleep(500);
				
					c.calibrateColors(webcam_image);
		
			}
	
		

	}

	public ColorWithCalibration(String filepath) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		loadFile(filepath);

	}

	String blue = "";
	String green = "";
	String red = "";
	String white = "";
	String yellow = "";

	private void loadFile(String path) {
		try {
			FileReader fr;
			fr = new FileReader(path);
			BufferedReader textReader = new BufferedReader(fr);
			for (int i = 0; i < 5; i++) {
				String readString = textReader.readLine();
				if (readString.startsWith("B")) {
					blue = readString;
				} else if (readString.startsWith("G")) {
					green = readString;
				} else if (readString.startsWith("R")) {
					red = readString;
				} else if (readString.startsWith("W")) {
					white = readString;
				} else if (readString.startsWith("Y")) {
					yellow = readString;
				}

			}
			textReader.close();
			fr.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	boolean framemade = false;
	JFrame frame, frame2;
	frame facePanel, facePanel2;

	public void calibrateColors(Mat image) {
		if (!framemade) {
			frame = new JFrame("Color Calibration");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			facePanel = new frame();
			frame.setSize(40, 40); // give the frame some arbitrary size
			frame.setBackground(Color.BLUE);
			frame.add(facePanel, BorderLayout.CENTER);
			frame.setVisible(true);

			frame2 = new JFrame("WebCam Capture Reworked");
			frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			facePanel2 = new frame();
			frame2.setSize(40, 40); // give the frame some arbitrary size
			frame2.setBackground(Color.BLUE);
			frame2.add(facePanel2, BorderLayout.CENTER);
			frame2.setVisible(true);
			createToolbars();
			frame.setSize(800, 600);
			frame2.setSize(800, 600);
			framemade = true;
		}
		Image(frame, frame2, facePanel, facePanel2, image);
	}

	private Mat adjustImage(Mat image1) {
		Mat image = new Mat(image1.size(), Core.DEPTH_MASK_8U);
		Core.inRange(image1, new Scalar(H_Min, S_Min, V_Min), new Scalar(H_Max,
				S_Max, V_Max), image);
		return image;
	}

	private void Image(JFrame frame, JFrame frame2, frame facePanel,
			frame facePanel2, Mat image) {
		Mat webcam_image = image;
		facePanel.matToBufferedImage(webcam_image);
		facePanel.repaint();
		Mat reworked_image = new Mat();

		// frame.setSize(webcam_image.width()+40,webcam_image.height()+60);
		// frame2.setSize(webcam_image.width()+40,webcam_image.height()+60);
		// Apply the classifier to the captured image
		reworked_image = adjustImage(webcam_image);
		// Display the image
		facePanel2.matToBufferedImage(reworked_image);
		facePanel2.repaint();

	}

	static int H_Min = 0;
	static int H_Max = 360;
	static int S_Min = 0;
	static int S_Max = 255;
	static int V_Max = 255;
	static int V_Min = 0;

	JSlider H_MinSlider, H_MaxSlider, S_MinSlider, S_MaxSlider, V_MaxSlider,
			V_MinSlider;

	private void createToolbars() {
		final JTextField h1 = new JTextField("H-Max:" + H_Max);
		final JTextField h2 = new JTextField("H-Min:" + H_Min);
		final JTextField s1 = new JTextField("S-Max:" + S_Max);
		final JTextField s2 = new JTextField("S-Min:" + S_Min);
		final JTextField v1 = new JTextField("V-Max:" + V_Max);
		final JTextField v2 = new JTextField("V-Min:" + V_Min);

		JFrame frame1 = new JFrame("Toolbars");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel frame = new JPanel(new GridLayout(0, 2));
		H_MinSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
		H_MinSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				if (((JSlider) ce.getSource()).getValue() <= ColorWithCalibration.H_Max) {
					ColorWithCalibration.H_Min = ((JSlider) ce.getSource())
							.getValue();
					h2.setText("H-Min:" + ColorWithCalibration.H_Min);
				} else
					((JSlider) ce.getSource())
							.setValue(ColorWithCalibration.H_Min);

			}
		});
		H_MaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 360);
		H_MaxSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				if (((JSlider) ce.getSource()).getValue() >= ColorWithCalibration.H_Min) {
					ColorWithCalibration.H_Max = ((JSlider) ce.getSource())
							.getValue();
					h1.setText("H-Max:" + ColorWithCalibration.H_Max);
				} else
					((JSlider) ce.getSource())
							.setValue(ColorWithCalibration.H_Max);

			}
		});
		S_MinSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		S_MinSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				if (((JSlider) ce.getSource()).getValue() <= ColorWithCalibration.S_Max) {
					ColorWithCalibration.S_Min = ((JSlider) ce.getSource())
							.getValue();
					s2.setText("S-Min:" + ColorWithCalibration.S_Min);
				} else
					((JSlider) ce.getSource())
							.setValue(ColorWithCalibration.S_Min);

			}
		});
		S_MaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 255);
		S_MaxSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				if (((JSlider) ce.getSource()).getValue() >= ColorWithCalibration.S_Min) {
					ColorWithCalibration.S_Max = ((JSlider) ce.getSource())
							.getValue();
					s1.setText("S-Max:" + ColorWithCalibration.S_Max);
				} else
					((JSlider) ce.getSource())
							.setValue(ColorWithCalibration.S_Max);

			}
		});
		V_MinSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		V_MinSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				if (((JSlider) ce.getSource()).getValue() <= ColorWithCalibration.V_Max) {
					ColorWithCalibration.V_Min = ((JSlider) ce.getSource())
							.getValue();
					v2.setText("V-Min:" + ColorWithCalibration.V_Min);
				} else
					((JSlider) ce.getSource())
							.setValue(ColorWithCalibration.V_Min);

			}
		});
		V_MaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 255);
		V_MaxSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				if (((JSlider) ce.getSource()).getValue() >= ColorWithCalibration.V_Min) {
					ColorWithCalibration.V_Max = ((JSlider) ce.getSource())
							.getValue();
					v1.setText("V-Max:" + ColorWithCalibration.V_Max);
				} else
					((JSlider) ce.getSource())
							.setValue(ColorWithCalibration.V_Max);

			}
		});
		frame.add(H_MaxSlider);
		frame.add(h1);
		frame.add(H_MinSlider);
		frame.add(h2);
		frame.add(S_MaxSlider);
		frame.add(s1);
		frame.add(S_MinSlider);
		frame.add(s2);
		frame.add(V_MaxSlider);
		frame.add(v1);
		frame.add(V_MinSlider);
		frame.add(v2);

		saveWhite = new JButton("Save White");
		saveWhite.setActionCommand("saveWhite");
		saveWhite.addActionListener(this);
		frame.add(saveWhite);

		loadWhite = new JButton("Load White");
		loadWhite.setActionCommand("loadWhite");
		loadWhite.addActionListener(this);
		frame.add(loadWhite);

		saveYellow = new JButton("Save Yellow");
		saveYellow.setActionCommand("saveYellow");
		saveYellow.addActionListener(this);
		frame.add(saveYellow);

		loadYellow = new JButton("Load Yellow");
		loadYellow.setActionCommand("loadYellow");
		loadYellow.addActionListener(this);
		frame.add(loadYellow);

		saveRed = new JButton("Save Red");
		saveRed.setActionCommand("saveRed");
		saveRed.addActionListener(this);
		frame.add(saveRed);

		loadRed = new JButton("Load Red");
		loadRed.setActionCommand("loadRed");
		loadRed.addActionListener(this);
		frame.add(loadRed);

		saveGreen = new JButton("Save Green");
		saveGreen.setActionCommand("saveGreen");
		saveGreen.addActionListener(this);
		frame.add(saveGreen);

		loadGreen = new JButton("Load Green");
		loadGreen.setActionCommand("loadGreen");
		loadGreen.addActionListener(this);
		frame.add(loadGreen);

		saveBlue = new JButton("Save Blue");
		saveBlue.setActionCommand("saveBlue");
		saveBlue.addActionListener(this);
		frame.add(saveBlue);

		loadBlue = new JButton("Load Blue");
		loadBlue.setActionCommand("loadBlue");
		loadBlue.addActionListener(this);
		frame.add(loadBlue);

		frame1.getContentPane().add(frame);
		frame1.pack();
		frame1.setVisible(true);
	}

	JButton saveWhite;
	JButton loadWhite;
	JButton saveYellow;

	JButton loadYellow;
	JButton saveRed;
	JButton loadRed;
	JButton saveGreen;
	JButton loadGreen;
	JButton saveBlue;
	JButton loadBlue;

	public Scalar getYellowMinScalar() {
		String[] s = yellow.split(" ");
		return new Scalar(Integer.parseInt(s[1]), Integer.parseInt(s[2]),
				Integer.parseInt(s[3]));
	}

	public Scalar getYellowMaxScalar() {
		String[] s = yellow.split(" ");
		return new Scalar(Integer.parseInt(s[4]), Integer.parseInt(s[5]),
				Integer.parseInt(s[6]));
	}

	public Scalar getRedMinScalar() {
		String[] s = red.split(" ");
		return new Scalar(Integer.parseInt(s[1]), Integer.parseInt(s[2]),
				Integer.parseInt(s[3]));
	}

	public Scalar getRedMaxScalar() {
		String[] s = red.split(" ");
		return new Scalar(Integer.parseInt(s[4]), Integer.parseInt(s[5]),
				Integer.parseInt(s[6]));
	}

	public Scalar getWhiteMinScalar() {
		String[] s = white.split(" ");
		return new Scalar(Integer.parseInt(s[1]), Integer.parseInt(s[2]),
				Integer.parseInt(s[3]));
	}

	public Scalar getWhiteMaxScalar() {
		String[] s = white.split(" ");
		return new Scalar(Integer.parseInt(s[4]), Integer.parseInt(s[5]),
				Integer.parseInt(s[6]));
	}

	public Scalar getGreenMinScalar() {
		String[] s = green.split(" ");
		return new Scalar(Integer.parseInt(s[1]), Integer.parseInt(s[2]),
				Integer.parseInt(s[3]));
	}

	public Scalar getGreenMaxScalar() {
		String[] s = green.split(" ");
		return new Scalar(Integer.parseInt(s[4]), Integer.parseInt(s[5]),
				Integer.parseInt(s[6]));
	}

	public Scalar getBlueMinScalar() {
		String[] s = blue.split(" ");
		return new Scalar(Integer.parseInt(s[1]), Integer.parseInt(s[2]),
				Integer.parseInt(s[3]));
	}

	public Scalar getBlueMaxScalar() {
		String[] s = blue.split(" ");
		return new Scalar(Integer.parseInt(s[4]), Integer.parseInt(s[5]),
				Integer.parseInt(s[6]));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] s;
		switch (e.getActionCommand()) {
		case "saveBlue":
			blue = "B" + " " + H_Min + " " + S_Min + " " + V_Min + " " + H_Max
					+ " " + S_Max + " " + V_Max;
			saveFile();
			break;
		case "loadBlue":
			s = blue.split(" ");
			H_Min = Integer.parseInt(s[1]);
			H_MinSlider.setValue(H_Min);
			H_Max = Integer.parseInt(s[4]);
			H_MaxSlider.setValue(H_Max);
			S_Min = Integer.parseInt(s[2]);
			S_MinSlider.setValue(S_Min);
			S_Max = Integer.parseInt(s[5]);
			S_MaxSlider.setValue(S_Max);
			V_Min = Integer.parseInt(s[3]);
			V_MinSlider.setValue(V_Min);
			V_Max = Integer.parseInt(s[6]);
			V_MaxSlider.setValue(V_Max);
			break;
		case "saveGreen":
			green = "G" + " " + H_Min + " " + S_Min + " " + V_Min + " " + H_Max
					+ " " + S_Max + " " + V_Max ;
			saveFile();
			break;
		case "loadGreen":
			s = green.split(" ");
			H_Min = Integer.parseInt(s[1]);
			H_MinSlider.setValue(H_Min);
			H_Max = Integer.parseInt(s[4]);
			H_MaxSlider.setValue(H_Max);
			S_Min = Integer.parseInt(s[2]);
			S_MinSlider.setValue(S_Min);
			S_Max = Integer.parseInt(s[5]);
			S_MaxSlider.setValue(S_Max);
			V_Min = Integer.parseInt(s[3]);
			V_MinSlider.setValue(V_Min);
			V_Max = Integer.parseInt(s[6]);
			V_MaxSlider.setValue(V_Max);
			break;
		case "saveRed":
			red = "R" + " " + H_Min + " " + S_Min + " " + V_Min + " " + H_Max
					+ " " + S_Max + " " + V_Max;
			saveFile();
			break;
		case "loadRed":
			s = red.split(" ");
			H_Min = Integer.parseInt(s[1]);
			H_MinSlider.setValue(H_Min);
			H_Max = Integer.parseInt(s[4]);
			H_MaxSlider.setValue(H_Max);
			S_Min = Integer.parseInt(s[2]);
			S_MinSlider.setValue(S_Min);
			S_Max = Integer.parseInt(s[5]);
			S_MaxSlider.setValue(S_Max);
			V_Min = Integer.parseInt(s[3]);
			V_MinSlider.setValue(V_Min);
			V_Max = Integer.parseInt(s[6]);
			V_MaxSlider.setValue(V_Max);

			break;
		case "saveWhite":
			white = "W" + " " + H_Min + " " + S_Min + " " + V_Min + " " + H_Max
					+ " " + S_Max + " " + V_Max ;
			saveFile();
			break;
		case "loadWhite":
			s = white.split(" ");
			H_Min = Integer.parseInt(s[1]);
			H_MinSlider.setValue(H_Min);
			H_Max = Integer.parseInt(s[4]);
			H_MaxSlider.setValue(H_Max);
			S_Min = Integer.parseInt(s[2]);
			S_MinSlider.setValue(S_Min);
			S_Max = Integer.parseInt(s[5]);
			S_MaxSlider.setValue(S_Max);
			V_Min = Integer.parseInt(s[3]);
			V_MinSlider.setValue(V_Min);
			V_Max = Integer.parseInt(s[6]);
			V_MaxSlider.setValue(V_Max);
			break;
		case "saveYellow":
			yellow = "Y" + " " + H_Min + " " + S_Min + " " + V_Min + " "
					+ H_Max + " " + S_Max + " " + V_Max;
			saveFile();
			break;
		case "loadYellow":
			s = yellow.split(" ");
			H_Min = Integer.parseInt(s[1]);
			H_MinSlider.setValue(H_Min);
			H_Max = Integer.parseInt(s[4]);
			H_MaxSlider.setValue(H_Max);
			S_Min = Integer.parseInt(s[2]);
			S_MinSlider.setValue(S_Min);
			S_Max = Integer.parseInt(s[5]);
			S_MaxSlider.setValue(S_Max);
			V_Min = Integer.parseInt(s[3]);
			V_MinSlider.setValue(V_Min);
			V_Max = Integer.parseInt(s[6]);
			V_MaxSlider.setValue(V_Max);
			break;

		}

	}

	String filepath;

	private void saveFile() {
		try {
			if (filepath == null)
				filepath = "colors.txt";
			FileWriter fw = new FileWriter(filepath);
			fw.write(blue +"\n"+ green+"\n" + red +"\n"+ white+"\n" + yellow+"\n");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
