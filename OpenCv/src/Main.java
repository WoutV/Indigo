import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class Main {  
    
	public static void main(String arg[]) throws InterruptedException{  
      // Load the native library.  
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
      //or ...     System.loadLibrary("opencv_java244");       

      //make the JFrame
      JFrame frame = new JFrame("WebCam Capture - Face detection");  
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
      frame facePanel = new frame();  
      frame.setSize(400,400); //give the frame some arbitrary size 
      frame.setBackground(Color.BLUE);
      frame.add(facePanel,BorderLayout.CENTER);       
      frame.setVisible(true);       
      
      //Open and Read from the video stream  
       Mat webcam_image=new Mat();  
       VideoCapture webCam =new VideoCapture(0);   
   
        if( webCam.isOpened())  
          {  
           Thread.sleep(500); /// This one-time delay allows the Webcam to initialize itself  
           while( true )  
           {  
        	 webCam.read(webcam_image);  
             if( !webcam_image.empty() )  
              {   
            	  Thread.sleep(200); /// This delay eases the computational load .. with little performance leakage
                   frame.setSize(webcam_image.width()+40,webcam_image.height()+60);  
                   //Apply the classifier to the captured image  
                   webcam_image=Webcamtest.analyze(webcam_image);  
                  //Display the image  
                   facePanel.matToBufferedImage(webcam_image);  
                   facePanel.repaint();   
              }  
              else  
              {   
                   System.out.println(" --(!) No captured frame from webcam !");   
                   break;   
              }  
             }  
            }
           webCam.release(); //release the webcam
 
      } //end main 
}
