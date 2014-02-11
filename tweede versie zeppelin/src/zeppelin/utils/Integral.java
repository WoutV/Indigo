package zeppelin.utils;

/**
 * A class representing an integral as a record of blocks.
 * A size is set when the integral is created, and this equals the amount of values recorded.
 * For example, an integral of size 4 keeps track of the 4 most recently supplied values.
 * The value of the integral equals the sum of these 4 values.
 * When the fifth value is supplied, the first value is overwritten.
 * Hence, the value of the integral always equals the sum of the ('size') most recently supplied values.
 */
public class Integral {
	
	private CircularDoubleArray i;
	private double currentValue;
	private int size;
	
	private boolean full;
	private int v;
	
	/**
	 * Creates a new integral
	 * @param size
	 * 		The amount of values kept
	 */
	public Integral(int size) {
		this.size = size;
		i = new CircularDoubleArray(size);
	}
	
	/**
	 * Adds the supplied value, possibly overwriting the least recently supplied value.
	 * Updates the current value of this integral accordingly.
	 * @param value
	 */
	public void addToIntegral(double value) {
		if(!full) {
			i.add(value);
			v++;
			currentValue = currentValue + value;
			if(v == size)
				full=true;
			return;
		}
		double valueToRemove = i.get(0);
		currentValue = currentValue - valueToRemove;
		i.add(value);
		currentValue = currentValue + value;
	}
	
	/**
	 * Gets the current value of this integral.
	 */
	public double getValue() {
		return currentValue;
	}
	
	public static void main(String[] args) {
		Integral i = new Integral(4);
		i.addToIntegral(4);
		i.addToIntegral(5);
		System.out.println("Value: " + i.getValue() + "/Should be: 9");
		
		i.addToIntegral(1);
		i.addToIntegral(2);
		i.addToIntegral(3);
		i.addToIntegral(10);
		System.out.println("Value: " + i.getValue() + "/Should be: 16");
		
	}
}
