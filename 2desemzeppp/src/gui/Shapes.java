package gui;

import java.awt.Polygon;

public class Shapes {
	public static int[] heartx = {-5,0,5,8,8,6,6,4,4,2,2,0,-2,-2,-4,-4,-6,-6,-8,-8};
	public static int[] hearty = {-8,-3,-8,-5,-3,-1,0,2,3,5,6,8,6,5,3,2,0,-1,-3,-5};
	public static Polygon heart = new Polygon(heartx,hearty,heartx.length);
	
	public static int[] rclandscapex = {-8,8,8,-8};
	public static int[] rclandscapey = {-5,-5,5,5};
	
	public static int[] rcportraitx = {-5,5,5,-5};
	public static int[] rcportraity = {-8,-8,8,8};
	
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
}
