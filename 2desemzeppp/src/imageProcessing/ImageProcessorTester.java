package imageProcessing;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import map.ColorSymbol;

public class ImageProcessorTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			System.loadLibrary("opencv_java248");
			ArrayList<ColorSymbol> arrayOfSymbols = ImageProcessor.processImage(new ImageIcon("C:/Users/Study/Desktop/OpenCv/11.jpg"));
			for(ColorSymbol cs: arrayOfSymbols){
				System.out.println("Color:"+cs.colour);
			}
		
	

	}

}
