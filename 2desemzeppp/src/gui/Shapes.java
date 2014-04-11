package gui;

import java.awt.Polygon;


/**
 * A class representing Shapes of 17 x 17 px.
 * The methods in this class allow to get Polygons for some shapes around a given center point.
 */
public class Shapes {
	private static int[] heartx = {-5,0,5,8,8,6,6,4,4,2,2,0,-2,-2,-4,-4,-6,-6,-8,-8};
	private static int[] hearty = {-8,-3,-8,-5,-3,-1,0,2,3,5,6,8,6,5,3,2,0,-1,-3,-5};
	
	private static int[] rclandscapex = {-8,8,8,-8};
	private static int[] rclandscapey = {-5,-5,5,5};
	
	private static int[] rcportraitx = {-5,5,5,-5};
	private static int[] rcportraity = {-8,-8,8,8};
	
	private static int[] tabletouterx = {-10,10,10,-10};
	private static int[] tabletoutery = {-16,-16,16,16};
	
	private static int[] tabletinnerx = {-9,9,9,-9};
	private static int[] tabletinnery = {-15,-15,13,13};
	
	private static int[] squarex = {-8,8,8,-8};
	private static int[] squarey = {-8,-8,8,8};
	
	private static int[] trianglex = {0,8,-8};
	private static int[] triangley = {-8,8,8};
	
	private static int[] starx = {0,1,1,2,2,3,3,5,6,7,8,8,6,6,5,5,6,6,7,7,6,4,3,0,-3,-4,-6,-7,-7,-6,-6,-5,
		-5,-6,-6,-8,-8,-7,-6,-5,-3,-3,-2,-2,-1,-1};
	private static int[] stary = {-8,-7,-6,-5,-4,-3,-2,-2,-3,-3,-4,-2,0,1,2,4,5,6,7,8,8,6,6,3,6,6,8,8,7,6,5,4,
		2,1,0,-2,-4,-3,-3,-2,-2,-3,-4,-5,-6,-7};
	
	/**
	 * Given the coordinates of the center, gives a polygon defining a Heart shape
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static Polygon getShiftedHeart(int x, int y) {
		int[] xs = new int[heartx.length];
		int[] ys = new int[hearty.length];
		for(int i=0;i<heartx.length;i++) {
			xs[i] = heartx[i] + x;
		}
		for(int i=0;i<hearty.length;i++) {
			ys[i] = hearty[i] + y;
		}
		return new Polygon(xs,ys,heartx.length);
	}
	
	/**
	 * Given the coordinates of the center, gives a polygon defining a Rectangle shape,
	 * in landscape direction
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static Polygon getShiftedLandscapeRectangle(int x, int y) {
		int[] xs = new int[rclandscapex.length];
		int[] ys = new int[rclandscapey.length];
		for(int i=0;i<rclandscapex.length;i++) {
			xs[i] = rclandscapex[i] + x;
		}
		for(int i=0;i<rclandscapey.length;i++) {
			ys[i] = rclandscapey[i] + y;
		}
		return new Polygon(xs,ys,rclandscapex.length);
	}
	
	/**
	 * Given the coordinates of the center, gives a polygon defining a Rectangle shape,
	 * in portrait direction
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static Polygon getShiftedPortraitRectangle(int x, int y) {
		int[] xs = new int[rcportraitx.length];
		int[] ys = new int[rcportraity.length];
		for(int i=0;i<rcportraitx.length;i++) {
			xs[i] = rcportraitx[i] + x;
		}
		for(int i=0;i<rcportraity.length;i++) {
			ys[i] = rcportraity[i] + y;
		}
		return new Polygon(xs,ys,rcportraitx.length);
	}
	
	/**
	 * Given the coordinates of the center, gives a polygon defining a the outer edge of a tablet.
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static Polygon getTabletOuterEdge(int x, int y) {
		int[] xs = new int[tabletouterx.length];
		int[] ys = new int[tabletoutery.length];
		for(int i=0;i<tabletouterx.length;i++) {
			xs[i] = tabletouterx[i] + x;
		}
		for(int i=0;i<tabletoutery.length;i++) {
			ys[i] = tabletoutery[i] + y;
		}
		return new Polygon(xs,ys,tabletouterx.length);
	}
	
	/**
	 * Given the coordinates of the center, gives a polygon defining a the outer edge of a tablet.
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static Polygon getTabletInnerEdge(int x, int y) {
		int[] xs = new int[tabletinnerx.length];
		int[] ys = new int[tabletinnery.length];
		for(int i=0;i<tabletinnerx.length;i++) {
			xs[i] = tabletinnerx[i] + x;
		}
		for(int i=0;i<tabletinnery.length;i++) {
			ys[i] = tabletinnery[i] + y;
		}
		return new Polygon(xs,ys,tabletinnerx.length);
	}
	
	/**
	 * Given the coordinates of the center, gives the data used for drawing the tablet circle with this center.
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static int[] getShiftedTabletCircleData(int x, int y) {
		int[] a = new int[4];
		a[0] = x - 4;
		a[1] = y - 4;
		a[2] = 8;
		a[3] = 8;
		return a;
	}
	
	/**
	 * Given the coordinates of the center, gives a polygon defining a Square shape.
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static Polygon getShiftedSquare(int x, int y) {
		int[] xs = new int[squarex.length];
		int[] ys = new int[squarey.length];
		for(int i=0;i<squarex.length;i++) {
			xs[i] = squarex[i] + x;
		}
		for(int i=0;i<squarey.length;i++) {
			ys[i] = squarey[i] + y;
		}
		return new Polygon(xs,ys,squarex.length);
	}
	
	/**
	 * Given the coordinates of the center, gives a polygon defining a Triangle shape
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static Polygon getShiftedTriangle(int x, int y) {
		int[] xs = new int[trianglex.length];
		int[] ys = new int[triangley.length];
		for(int i=0;i<trianglex.length;i++) {
			xs[i] = trianglex[i] + x;
		}
		for(int i=0;i<triangley.length;i++) {
			ys[i] = triangley[i] + y;
		}
		return new Polygon(xs,ys,trianglex.length);
	}
	
	/**
	 * Given the coordinates of the center, gives a polygon defining a Star shape
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static Polygon getShiftedStar(int x, int y) {
		int[] xs = new int[starx.length];
		int[] ys = new int[stary.length];
		for(int i=0;i<starx.length;i++) {
			xs[i] = starx[i] + x;
		}
		for(int i=0;i<stary.length;i++) {
			ys[i] = stary[i] + y;
		}
		return new Polygon(xs,ys,starx.length);
	}
	
	/**
	 * Given the coordinates of the center, gives the data used for drawing the circle with this center.
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static int[] getShiftedCircleData(int x, int y) {
		int[] a = new int[4];
		a[0] = x - 8;
		a[1] = y - 8;
		a[2] = 16;
		a[3] = 16;
		return a;
	}
	
	/**
	 * Given the coordinates of the center, gives the data used for drawing the target with this center.
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static int[] getShiftedTargetData(int x, int y) {
		int[] a = new int[4];
		a[0] = x - 20;
		if(a[0] < 0)
			a[0] = 0;
		a[1] = y - 20;
		if(a[1] < 0)
			a[1] = 0;
		a[2] = 41;
		if(a[0] + a[2] > 495)
			a[2] = 495 - a[0];
		a[3] = 41;
		if(a[1] + a[3] > 495)
			a[3] = 495 - a[1];
		return a;
	}
	
	/**
	 * Given the coordinates of the center, gives the data used for drawing the zeppelin with this center.
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static int[] getShiftedZeppelinData(int x, int y) {
		int[] a = new int[4];
		a[0] = x - 20;
		if(a[0] < 0)
			a[0] = 0;
		a[1] = y - 20;
		if(a[1] < 0)
			a[1] = 0;
		a[2] = 40;
		if(a[0] + a[2] > 495)
			a[2] = 495 - a[0];
		a[3] = 40;
		if(a[1] + a[3] > 495)
			a[3] = 495 - a[1];
		return a;
	}
	
	/**
	 * Given the coordinates of the center, gives the data used for drawing the zeppelin with this center.
	 * Point 1: r[0],r[1]: tip of arrow, Point 2: r[2],r[3], Point 3: r[4],r[5]
	 * Draw an arrow by connecting Center-Point1, Point1-Point2, Point1-Point3.
	 * 
	 * @param x
	 * 			The X coordinate of the center
	 * @param y
	 * 			The Y coordinate of the center
	 */
	public static int[] getShiftedZeppelinOrientationData(int x, int y, int alpha) {
		alpha = alpha + 90;
		if(alpha > 180)
			alpha = alpha - 360;
		int xr = (int) (20*Math.cos(alpha/180.0*Math.PI));
		int yr = (int) (20*Math.sin(alpha/180.0*Math.PI));
		
		alpha = alpha + 10;
		if(alpha > 180)
			alpha = alpha - 360;
		int x2 = (int) (16*Math.cos(alpha/180.0*Math.PI));
		int y2 = (int) (16*Math.sin(alpha/180.0*Math.PI));
		
		alpha = alpha - 20;
		if(alpha < 180)
			alpha = alpha + 360;
		int x3 = (int) (16*Math.cos(alpha/180.0*Math.PI));
		int y3 = (int) (16*Math.sin(alpha/180.0*Math.PI));
		
		int[] b = {x-xr,y-yr,x-x2,y-y2,x-x3,y-y3};
		for(int i=0;i>6;i++) {
			if(b[i]<0)
				b[i] = 0;
			if(b[i] > 495)
				b[i] = 495;
		}
		return b;
	}
}
