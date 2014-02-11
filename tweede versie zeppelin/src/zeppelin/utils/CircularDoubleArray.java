package zeppelin.utils;

import java.util.Arrays;

/**
 * Class to represent a Circular Array of fixed size containing double values.
 * The Circular Array simply adds elements as long as the fixed size is not exceeded.
 * When the size is exceeded, the array wraps around itself, meaning the element
 * that was added first is deleted and replaced by the new element.
 * 
 * This class is based on the Circular Array class, but is optimised for working with
 * double values.
 */
public class CircularDoubleArray {
	private double[] a;
	//index of position where an element was last added
	private int i;
	private int size;
	
	//behaviour of some methods is different when the array isn't
	//completely filled yet
	private boolean full;
	
	public CircularDoubleArray(int size) {
		a = new double[size];
		i = size-1;
		this.size = size;
	}
	
	/**
	 * Adds the attached element to this Circular Array.
	 */
	public void add(double element) {
		i = (i+1)%size;
		a[i] = element;
		if(!full && i==size-1)
			full = true;
	}
	
	/**
	 * Returns the element at position 'index', where index starts at the position
	 * of the element that was added first.
	 * @param index
	 * 		An integer between 0 and the size - 1.
	 * 		If the index is higher than the amount of elements added, the returned element is 0.
	 */
	public double get(int index) {
		if(!full && index > i)
			return 0;
		if(!full)
			return a[index];
		return a[(i+1+index)%size];
	}
	
	/**
	 * Returns an array containing all the elements inside this Circular Array.
	 * The order of the elements is defined by the order in which they were added to
	 * this Circular Array.
	 */
	public double[] getList() {
		if(!full) {
			double[] list = new double[i+1];
			for(int j=0;j<i+1;j++)
				list[j] = a[j];
			return list;
		}
		double[] list = new double[size];
		for(int j=0;j<size;j++)
			list[j] = a[(j+i+1)%size];
		return list;
	}
	
	/**
	 * Returns an array which is a sorted copy of all elements currently in this Circular Array.
	 */
	public double[] getSortedCopy() {
		if(!full) {
			double[] r = new double[i+1];
			for(int j=0;j<i+1;j++)
				r[j] = a[j];
			Arrays.sort(r);
			return r;
		}
		double[] r = new double[size];
		for(int i=0;i<size;i++)
			r[i] = a[i];
		Arrays.sort(r);
		return r;
	}
	
	/**
	 * Returns the median value of all the double values.
	 */
	public double getMedian() {
		double r[] = getSortedCopy();
		if(r.length % 2 == 1)
			return r[r.length/2];
		return (r[r.length/2-1] + r[r.length/2])/2;
	}
	
	public double[] geta() {
		return a;
	}
	
	
	
	public static void main(String[] args) {
		CircularDoubleArray c = new CircularDoubleArray(5);
		c.add(0);c.add(1);c.add(4);c.add(3);c.add(1);c.add(5);c.add(7);
		double[] list = c.getList();
		System.out.println(c.get(0) + " " + c.get(4));
		System.out.println("["+list[0]+","+list[1]+","+list[2]+","+list[3]+","+list[4]+"]");
		list = c.getSortedCopy();
		System.out.println("["+list[0]+","+list[1]+","+list[2]+","+list[3]+","+list[4]+"]");
		
		CircularDoubleArray c2 = new CircularDoubleArray(5);
		c2.add(0);c2.add(1);
		list = c2.getList();
		System.out.println("["+list[0]+","+list[1]+"]");
		System.out.println("listsize: " + list.length);
		System.out.println(c2.get(1));
		if(c2.get(2) == 0)
			System.out.println("Ok");
		
		CircularDoubleArray c3 = new CircularDoubleArray(5);
		c3.add(0);c3.add(5);c3.add(1);
		System.out.println(c3.getMedian());
		c3.add(3);
		System.out.println(c3.getMedian());

	}
}
