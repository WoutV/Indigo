import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
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


public class hsvtest {
	
	
	static boolean camera = false;
	static String filePath = "C:/Users/Vince/Desktop/9.jpg";
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
	      // Load the native library.  
	      System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	      //or ...     System.loadLibrary("opencv_java244");       

	      //make the JFrame
	      JFrame frame = new JFrame("WebCam Capture");  
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
	      frame facePanel = new frame();  
	      frame.setSize(40,40); //give the frame some arbitrary size 
	      frame.setBackground(Color.BLUE);
	      frame.add(facePanel,BorderLayout.CENTER);       
	      frame.setVisible(true);   
	      
	      JFrame frame2 = new JFrame("WebCam Capture Reworked");  
	      frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
	      frame facePanel2 = new frame();  
	      frame2.setSize(40,40); //give the frame some arbitrary size 
	      frame2.setBackground(Color.BLUE);
	      frame2.add(facePanel2,BorderLayout.CENTER);       
	      frame2.setVisible(true); 
	      createToolbars();
	      if(!camera)
	    	  Image(frame,frame2,facePanel,facePanel2);
	      else
	    	  Camera(frame, frame2, facePanel, facePanel2);
	 
	      } //end main 

	
	
		private static Mat adjustImage(Mat image1){
			Mat image = new Mat(image1.size(),Core.DEPTH_MASK_8U);
			Core.inRange(image1, new Scalar(H_Min,S_Min,V_Min), new Scalar(H_Max,S_Max,V_Max), image);
			return image;
		}
		
		private static void Image(JFrame frame,JFrame frame2, frame facePanel, frame facePanel2) throws InterruptedException{
			Mat webcam_image = Highgui.imread(filePath);
			facePanel.matToBufferedImage(webcam_image);  
            facePanel.repaint();  
			Mat reworked_image= new Mat();
			while( true )  
	           {   
	             if( !webcam_image.empty() )  
	              {   
	            	  Thread.sleep(30); /// This delay eases the computational load .. with little performance leakage
	                 frame.setSize(800,600);
	                 frame2.setSize(800,600);
	            	  //  frame.setSize(webcam_image.width()+40,webcam_image.height()+60);  
	                  // frame2.setSize(webcam_image.width()+40,webcam_image.height()+60);
	                   //Apply the classifier to the captured image  
	                   reworked_image=adjustImage(webcam_image);  
	                  //Display the image   
	                   facePanel2.matToBufferedImage(reworked_image);
	                   facePanel2.repaint();
	              }  
	              else  
	              {   
	                   System.out.println("Image could not be loaded");   
	                   break;   
	              }  
	             }  
		}
		
		private static void Camera(JFrame frame,JFrame frame2, frame facePanel, frame facePanel2) throws InterruptedException{
			//Open and Read from the video stream  
		       Mat webcam_image=new Mat();
		       Mat reworked_image= new Mat();
		       VideoCapture webCam =new VideoCapture(0);   

		        if( webCam.isOpened())  
		          {  
		           Thread.sleep(500); /// This one-time delay allows the Webcam to initialize itself  
		           while( true )  
		           {  
		        	 webCam.read(webcam_image);  
		             if( !webcam_image.empty() )  
		              {   
		            	  Thread.sleep(30); /// This delay eases the computational load .. with little performance leakage
		                   frame.setSize(webcam_image.width()+40,webcam_image.height()+60);  
		                   frame2.setSize(webcam_image.width()+40,webcam_image.height()+60);
		                   //Apply the classifier to the captured image  
		                   reworked_image=adjustImage(webcam_image);  
		                  //Display the image  
		                   facePanel.matToBufferedImage(webcam_image);  
		                   facePanel.repaint();  
		                   facePanel2.matToBufferedImage(reworked_image);
		                   facePanel2.repaint();
		              }  
		              else  
		              {   
		                   System.out.println(" --(!) No captured frame from webcam !");   
		                   break;   
		              }  
		             }  
		            }
		           webCam.release(); //release the webcam
		}
		
		
		static int H_Min=0;
		static int H_Max=360;
		static int S_Min=0;
		static int S_Max=255;
		static int V_Max=255;
		static int V_Min=0;
		private static void createToolbars(){
			 final JTextField h1 = new JTextField("H-Max:"+H_Max);
			 final JTextField h2 = new JTextField("H-Min:"+H_Min);
			 final JTextField s1 = new JTextField("S-Max:"+S_Max);
			 final JTextField s2 = new JTextField("S-Min:"+S_Min);
			 final JTextField v1 = new JTextField("V-Max:"+V_Max);
			 final JTextField v2 = new JTextField("V-Min:"+V_Min);
			 
			 JFrame frame1 = new JFrame("Toolbars");  
		     frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		     JPanel frame=new JPanel(new GridLayout(0,2));
		     JSlider H_Min= new JSlider(JSlider.HORIZONTAL,
                     0, 360, 0);
		     H_Min.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) {
		    		 if(((JSlider) ce.getSource()).getValue() <= hsvtest.H_Max){
		    			 hsvtest.H_Min= ((JSlider) ce.getSource()).getValue();
		    			 h2.setText("H-Min:"+hsvtest.H_Min);
		    		 }
		    		 else 
		    			 ((JSlider) ce.getSource()).setValue(hsvtest.H_Min);
					
				}
		     });
		     JSlider H_Max= new JSlider(JSlider.HORIZONTAL,
                     0, 360, 360);
		     H_Max.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) {
		    		 if(((JSlider) ce.getSource()).getValue() >= hsvtest.H_Min){
		    			 hsvtest.H_Max= ((JSlider) ce.getSource()).getValue();
		    			 h1.setText("H-Max:"+hsvtest.H_Max);
		    		 }
		    		 else 
		    			 ((JSlider) ce.getSource()).setValue(hsvtest.H_Max);
					
				}
		     });
		     JSlider S_Min= new JSlider(JSlider.HORIZONTAL,
                     0, 255, 0);
		     S_Min.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) {
		    		 if(((JSlider) ce.getSource()).getValue() <= hsvtest.S_Max){
		    			 hsvtest.S_Min= ((JSlider) ce.getSource()).getValue();
		    			 s2.setText("S-Min:"+hsvtest.S_Min);
		    		 }
		    		 else 
		    			 ((JSlider) ce.getSource()).setValue(hsvtest.S_Min);
					
				}
		     });
		     JSlider S_Max= new JSlider(JSlider.HORIZONTAL,
                     0, 255, 255);
		     S_Max.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) {
		    		 if(((JSlider) ce.getSource()).getValue() >= hsvtest.S_Min){
		    			 hsvtest.S_Max= ((JSlider) ce.getSource()).getValue();
		    			 s1.setText("S-Max:"+hsvtest.S_Max);
		    		 }
		    		 else 
		    			 ((JSlider) ce.getSource()).setValue(hsvtest.S_Max);
					
				}
		     });
		     JSlider V_Min= new JSlider(JSlider.HORIZONTAL,
                     0, 255, 0);
		     V_Min.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) { 
		    		 if(((JSlider) ce.getSource()).getValue() <= hsvtest.V_Max){
		    			 hsvtest.V_Min= ((JSlider) ce.getSource()).getValue();
		    			 v2.setText("V-Min:"+hsvtest.V_Min);
		    		 }
	    		 else 
	    			 ((JSlider) ce.getSource()).setValue(hsvtest.V_Min);
					
				}
		     });
		     JSlider V_Max= new JSlider(JSlider.HORIZONTAL,
                     0, 255, 255);
		     V_Max.addChangeListener(new ChangeListener() {
		    	 @Override
				public void stateChanged(ChangeEvent ce) {
		    		 if(((JSlider) ce.getSource()).getValue() >= hsvtest.V_Min){
		    			 hsvtest.V_Max= ((JSlider) ce.getSource()).getValue();
		    			 v1.setText("V-Max:"+hsvtest.V_Max);
		    		 }
		    		 else 
		    			 ((JSlider) ce.getSource()).setValue(hsvtest.V_Max);
					
				}
		     });
		     frame.add(H_Max);frame.add(h1);
		     frame.add(H_Min);frame.add(h2);
		     frame.add(S_Max);frame.add(s1);
		     frame.add(S_Min);frame.add(s2);
		     frame.add(V_Max);frame.add(v1);
		     frame.add(V_Min);frame.add(v2);
             frame1.getContentPane().add(frame);
             frame1.pack();
		     frame1.setVisible(true);
		}

}
