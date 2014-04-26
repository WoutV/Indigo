package imageProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import org.opencv.core.Core;
import org.opencv.core.Scalar;

public class Color{


	/**
	 * Creates a new class and loads the file to set the color filter
	 * @param filepath
	 */
	public Color(String filepath) {
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

	static int H_Min = 0;
	static int H_Max = 360;
	static int S_Min = 0;
	static int S_Max = 255;
	static int V_Max = 255;
	static int V_Min = 0;

	

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

}
