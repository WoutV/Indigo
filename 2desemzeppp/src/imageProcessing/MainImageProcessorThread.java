package imageProcessing;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import map.Symbol;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class MainImageProcessorThread implements Runnable{
	private Integer erodeTimes = 0;
	private Integer dilateTimes = 1;
	private Integer erodesize = 3;
	private Integer dilatesize = 3;
	private Mat originalImage;
	private Integer cannyThresholdMax = 13;
	private Integer minArea = 1000;
	private Integer epsilonApprox = 1;
	private Size frameSize = new Size(800, 700);
	// private frame cannyoutput = makeFrame("Canny Output", frameSize);
	private Frame foundContours = makeFrame(
			"Filtered Image", frameSize);;
	private Frame resultFrame = makeFrame("Result", frameSize);

	// private frame zoomedContourFrame = makeFrame("Zoomed", frameSize);



	/**
	 * Initialises this class with the given variables. Also initialises and starts the imageupdater thread. 
	 * Note: imageupdater thread is in pause call the method startImageUpdater to resume.
	 * @param frameMem
	 * 					This variable tells the symbol stabalizer up to which past frame or image the symbols should be kept. Give 4 in as default 
	 * @param Distance
	 * 					The distance in which the symbols are related to each other. Give 50 as default 
	 * @param colorText
	 * 					File path where color.txt is located.
	 */
	public MainImageProcessorThread(int frameMem, double Distance,String colorText) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		symbolS = new SymbolsStabalization(3, 50);
		cc = new Color("colors.txt");
		this.filepath = colorText;
//		imgUpdtr = new ImageUpdater();
//		Thread imgUpdtrThread = new Thread(imgUpdtr);
//		imgUpdtrThread.start();
		
	}
	ImageUpdater imgUpdtr;
	Color cc;
	Integer timestamp = 0;
	private SymbolsStabalization symbolS;
	/**
	 *Incase if ImageUpdater is needed to stop 
	 */
	public void stopImageUpdater(){
		imgUpdtr.stop();
	}
	/**
	 * Resumes the imageUpdater.
	 */
	public void startImageUpdater(){
		imgUpdtr.startRunning();
	}

	/**
	 * The title of the frame
	 * 
	 * @param Title
	 * @return
	 */
	private Frame makeFrame(String title, Size frameSize) {
		// make the JFrame
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame facePanel = new Frame();
		frame.setSize((int) frameSize.width, (int) frameSize.height); // give
		frame.setBackground(java.awt.Color.BLUE);
		frame.add(facePanel, BorderLayout.CENTER);
		frame.setVisible(true);
		return facePanel;
	}

	ArrayList<Symbol> Symbols;
	SymbolDetector symbolDetector;
	boolean threadInitialized = false;

	private void startSymbolDetector() {
		symbolDetector = new SymbolDetector(cc, symbolS,timestamp);
		symbolDetector.setFrame(foundContours);
		symbolDetector.initializeToolbarVariables(erodeTimes, dilateTimes,
				 erodesize, dilatesize, minArea, epsilonApprox,
				cannyThresholdMax);
//		System.out.println("Symbol Detector Inialized");
	}


	int typeInput;
	String filepath; // type 0 -> pi Image ; 1 -> video ; 2-> image
	boolean vcInitialized = false;
	VideoCapture vc;

	private void updateOriginalImage() {
		try {
//			this.originalImage = imgUpdtr.getLatestImage();
//			while(this.originalImage == null){
//				this.originalImage = imgUpdtr.getLatestImage();
//				System.out.println("Got null");
//			}
			this.originalImage = Highgui.imread("C:\\Users\\Study\\Desktop\\OpenCv\\latest fotos\\foto"+timestamp+".jpg",
					Imgproc.COLOR_BGR2GRAY);
			timestamp++;
			symbolS.increaseTimestamp();
			symbolDetector.updateImage(originalImage);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private Boolean stop = false;
	@Override
	public void run() {
		startSymbolDetector();
		updateOriginalImage();
		while(true){
			if(stop){
				try {
					synchronized(stop){
						stop.wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
			symbolDetector.processImage();
			Mat contourMat = symbolDetector.getBinaryMat();
			foundContours.matToBufferedImage(contourMat);
			foundContours.repaint();
			resultFrame.matToBufferedImage(symbolDetector.getResultImage());
			resultFrame.repaint();	
			updateOriginalImage();
//			if(timestamp>30){
//			Thread.sleep(300);}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public ArrayList<Symbol> getDetectedSymbols(){
		return symbolDetector.getDetectedSymbols();
	}
	public void stop(){
		//		System.out.println("Method stop has been called");
		stop=true;
		//		System.out.println("Value of stop changed");
//		synchronized(stop){
//			//			System.out.println("Notifying stop");
//			stop.notify();
//			//			System.out.println("stop notified");
//		}
	}
	public void startRunning(){
		stop = false;
		synchronized(stop){
			//			System.out.println("Notifying stop");
			stop.notify();
			//			System.out.println("stop notified");
		}
	}
	
	


}
