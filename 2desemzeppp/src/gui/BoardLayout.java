package gui;

/**
 * Class defining the layout of the map as displayed in the minimap.
 * A size of 495 x 495 px is assumed.
 * A maximum of 120 figures can be placed: 10 on each line, 12 lines.
 * Indexes 0-9 within one line, 0-11 for lines are used.
 * 
 */
public class BoardLayout {
	
	private static BoardLayout instance = new BoardLayout();
	
	public static BoardLayout getInstance() {
		return instance;
	}
	
	//the horizontal positions of the centers of figures on odd lines
	private int[] horodd = {17, 65, 113, 161, 209, 257, 305, 353, 401, 449};
	//the horizontal positions of the centers of figures on even lines
	private int[] horeven = {41, 89, 137, 185, 233, 281, 329, 377, 425, 473};

	//the positions of the centers of each line
	private int[] ys = {16, 56, 96, 136, 176, 216, 256, 296, 336, 376, 416, 456};
	
	/**
	 * Gives the horizontal center (in px) of a figure.
	 * @param no
	 * 		Which figure? range 0-9
	 * @param even
	 * 		Even line or odd line?
	 */
	public int getX(int no, boolean even) {
		if(no < 0 || no >= 10)
			return 0;
		if(even)
			return horeven[no];
		return horodd[no];
	}
	
	/**
	 * Gives the center (in px) of a line.
	 * @param no
	 * 		Which line? range 0-11
	 */
	public int getY(int no) {
		if(no < 0 || no >= 12)
			return 0;
		return ys[no];
		
	}

}
