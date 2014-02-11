package zeppelin.utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to represent a Circular Array of fixed size.
 * The Circular Array simply adds elements as long as the fixed size is not exceeded.
 * When the size is exceeded, the array wraps around itself, meaning the element
 * that was added first is deleted and replaced by the new element.
 */
public class CircularArray<T extends Comparable<T>> {
	private Comparable[] a;
	//index of position where an element was last added
	private int i;
	private int size;
	
	//behaviour of some methods is different when the array isn't
	//completely filled yet
	private boolean full;
	
	public CircularArray(int size) {
		a = new Comparable[size];
		i = size-1;
		this.size = size;
	}
	
	/**
	 * Adds the attached element to this Circular Array.
	 */
	public void add(T element) {
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
	 * 		If the index is higher than the amount of elements added, the returned element is null.
	 */
	public T get(int index) {
		if(!full && index > i)
			return null;
		if(!full)
			return (T) a[index];
		return (T) a[(i+1+index)%size];
	}
	
	/**
	 * Returns an arraylist containing all the elements inside this Circular Array.
	 * The order of the elements is defined by the order in which they were added to
	 * this Circular Array.
	 */
	public ArrayList<T> getList() {
		if(!full) {
			ArrayList list = new ArrayList<T>(i+1);
			for(int j=0;j<i+1;j++)
				list.add((T) a[j]);
			return list;
		}
		ArrayList list = new ArrayList<T>(size);
		for(int j=0;j<size;j++)
			list.add((T) a[(j+i+1)%size]);
		return list;
	}
	
	/**
	 * Returns an arraylist which is a sorted copy of all elements currently in this Circular Array.
	 */
	public ArrayList<T> getSortedCopy() {
		if(!full) {
			ArrayList r = new ArrayList<T>(i+1);
			for(int j=0;j<i+1;j++)
				r.add((T) a[j]);
			Collections.sort(r);
			return r;
		}
		ArrayList<T> r = new ArrayList<T>(size);
		for(int i=0;i<size;i++)
			r.add((T)a[i]);
		Collections.sort(r);
		return r;
	}
	
	
	
	public static void main(String[] args) {
		CircularArray<Integer> c = new CircularArray<Integer>(5);
		c.add(0);c.add(1);c.add(4);c.add(3);c.add(1);c.add(5);c.add(7);
		ArrayList<Integer> list = c.getList();
		System.out.println(c.get(0) + " " + c.get(4));
		System.out.println(list);
		System.out.println(c.getSortedCopy());
		
		CircularArray<Integer> c2 = new CircularArray<Integer>(5);
		c2.add(0);c2.add(1);
		System.out.println(c2.getList());
		System.out.println(c2.get(1));
		if(c2.get(2) == null)
			System.out.println("Ok");
	}
}
