
package gui;

/**
 * Class defining the layout of the map as displayed in the minimap.
 * A size of 495 x 495 px is assumed.
 * Indexes 0-(figuresPerRow-1) within one line, 0-(rows-1) for lines are used.
 *
 */
public class BoardLayout {

	//the horizontal positions of the centers of figures on odd lines
	private int[] horodd;
	//the horizontal positions of the centers of figures on even lines
	private int[] horeven;
	//the positions of the centers of each line
	private int[] ys;
	
	private int figuresPerRow;
	private int rows;
	
	//size of the map (in cm)
	private int size = 400;
	
	//size of the map (px)
	private int sizemap = 495;
	
	/**
	 * Creates a default BoardLayout with 10 figures on each line, 12 lines.
	 */
	public BoardLayout() {
		int[] horodd = {17, 65, 113, 161, 209, 257, 305, 353, 401, 449};
		int[] horeven = {41, 89, 137, 185, 233, 281, 329, 377, 425, 473};
		this.horodd = horodd;
		this.horeven = horeven;
		int[] ys = {16, 56, 96, 136, 176, 216, 256, 296, 336, 376, 416, 456};
		this.ys = ys;
		figuresPerRow = horodd.length;
		rows = ys.length;
	}
	
	/**
	 * Creates a BoardLayout for a custom Board.
	 * @param figuresPerRow
	 * 		How many figures in one row?
	 * @param rows
	 * 		How many rows?
	 */
	public BoardLayout(int figuresPerRow, int rows) {
		int availablePx = sizemap-16-16;
		double needed = figuresPerRow - 0.5;
		int distanceBetweenHor = (int) (availablePx/needed);
		if(distanceBetweenHor%2==1)
			distanceBetweenHor--;
		int startLine1 = (int) (sizemap-(figuresPerRow-0.5)*distanceBetweenHor)/2;
		int startLine2 = startLine1 + distanceBetweenHor/2;
		horodd = new int[figuresPerRow];
		horeven = new int[figuresPerRow];
		for(int i=0,current=startLine1;i<figuresPerRow;i++) {
			horodd[i] = current;
			current+=distanceBetweenHor;
		}
		for(int i=0,current=startLine2;i<figuresPerRow;i++) {
			horeven[i] = current;
			current+=distanceBetweenHor;
		}
		
		int distanceBetweenY = availablePx/(rows-1);
		int startY = (sizemap-(rows-1)*distanceBetweenY)/2;
		ys = new int[rows];
		for(int i=0,current=startY;i<rows;i++) {
			ys[i] = current;
			current+=distanceBetweenY;
		}
		this.figuresPerRow = horodd.length;
		this.rows = ys.length;
	}

	/**
	 * Gives the horizontal center (in px) of a figure.
	 * @param no
	 * Which figure? range 0-(figuresPerRow-1)
	 * @param even
	 * Even line or odd line? (left aligned or right aligned)
	 */
	public int getX(int no, boolean even) {
		if(no < 0 || no >= figuresPerRow)
			return 0;
		if(even)
			return horeven[no];
		return horodd[no];
	}

	/**
	 * Gives the center (in px) of a line.
	 * @param no
	 * Which line? range 0-(rows-1)
	 */
	public int getY(int no) {
		if(no < 0 || no >= rows)
			return 0;
		return ys[no];
	}
	
	/**
	 * Given the coordinates of a location (in cm), gives
	 * the coordinates of the center in px.
	 * @param coords
	 * 			coords[0]: x-coord
	 * 			coords[1]: y-coord
	 */
	public int[] getPx(double[] coords) {
		int x = (int) (1.0*coords[0]/size*sizemap);
		int y = (int) (1.0*coords[1]/size*sizemap);
		int[] r = {x,y};
		return r;
	}
}
