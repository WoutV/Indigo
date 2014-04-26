package fromOldFiles;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class Colors {

	public static Scalar getYellowMinScalar() {
		return new Scalar(0, 0, 202);
	}

	public static Scalar getYellowMaxScalar() {
		return new Scalar(180, 255, 255);
	}

	public static Scalar getRedMinScalar() {
		return new Scalar(0,0,150);
	}

	public static Scalar getRedMaxScalar() {
		return new Scalar(200,100,255);
	}

	public static Scalar getWhiteMinScalar() {
		return new Scalar(150,0,150);
	}

	public static Scalar getWhiteMaxScalar() {
		return new Scalar(360,255,255);
	}

	public static Scalar getGreenMinScalar() {
		return new Scalar(0,0,0);
	}

	public static Scalar getGreenMaxScalar() {
		return new Scalar(100,255,70);
	}

	public static Scalar getBlueMinScalar() {
		return new Scalar(108,0,0);
	}

	public static Scalar getBlueMaxScalar() {
		return new Scalar(180,255,80);
	}
	
	public static String getColor(Mat image, Point contourCenter) {
		try {
			Mat bitImage = image.clone();
			// Checking for white
			Core.inRange(image.clone(), Colors.getWhiteMinScalar(),
					Colors.getWhiteMaxScalar(), bitImage);
			double[] col = bitImage.get((int) contourCenter.y,
					(int) contourCenter.x);
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "W";

			// Checking for Yellow
			Core.inRange(image.clone(), Colors.getYellowMinScalar(),
					Colors.getYellowMaxScalar(), bitImage);
			col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "Y";

			Core.inRange(image.clone(), Colors.getRedMinScalar(),
					Colors.getRedMaxScalar(), bitImage);
			col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "R";

			Core.inRange(image.clone(), Colors.getBlueMinScalar(),
					Colors.getBlueMaxScalar(), bitImage);
			col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "B";

			Core.inRange(image.clone(), Colors.getGreenMinScalar(),
					Colors.getGreenMaxScalar(), bitImage);
			col = bitImage.get((int) contourCenter.y, (int) contourCenter.x);
			// System.out.println("col[0]" + col[0] + " length:" + col.length);
			if (col[0] >= 150)
				return "G";

//			System.out.println("col:" + col + "MatrixSize:" + image.height()
//					+ "," + image.width());
			return "NI";
		} catch (Exception e) {
			return "NI";
		}
	}

}
