package imageProcessingWithColorFilter;
/*  
 * Captures the camera stream with OpenCV  
 * Search for the faces  
 * Display a circle around the faces using Java  
 */  
import java.awt.*;  

import java.awt.image.BufferedImage;  
import java.io.ByteArrayInputStream;  
import java.io.IOException;  
import javax.imageio.ImageIO;  
import javax.swing.*;  
import org.opencv.core.Mat;  
import org.opencv.core.MatOfByte;  
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;  

public class frame extends JPanel{  
     private static final long serialVersionUID = 1L;  
     private BufferedImage image;  
     // Create a constructor method  
     public frame(){  
          super();   
     }  
     /*  
      * Converts/writes a Mat into a BufferedImage.  
      *   
      * @param matrix Mat of type CV_8UC3 or CV_8UC1  
      * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
      */       
     public boolean matToBufferedImage(Mat matrix) {  
          MatOfByte mb=new MatOfByte();  
          Highgui.imencode(".jpg", matrix, mb);  
          try {  
               this.image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));  
          } catch (IOException e) {  
               e.printStackTrace();  
               return false; // Error  
          }  
       return true; // Successful  
     }  
     public void paintComponent(Graphics g){  
          super.paintComponent(g);   
          if (this.image==null) return;  
          Size frameSize= new Size();
			if(image.getHeight()>=image.getWidth()){
				frameSize = new Size(800*image.getWidth()/image.getHeight(), 800);
			}
			else{
				frameSize = new Size(1000,1000*image.getHeight()/image.getWidth());
			}
        
      
        //g.drawImage(this.image,10,10,(int)frameSize.width-50,(int)frameSize.height-50, null);
			g.drawImage(this.image.getScaledInstance(800, -1, this.image. SCALE_SMOOTH),10,10,(int)frameSize.width-50,(int)frameSize.height-50, null);
     }
        
}  


	
