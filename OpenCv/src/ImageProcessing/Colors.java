package ImageProcessing;

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
		return new Scalar(100,255,65);
	}

	public static Scalar getBlueMinScalar() {
		return new Scalar(100,0,0);
	}

	public static Scalar getBlueMaxScalar() {
		return new Scalar(180,255,80);
	}

}
