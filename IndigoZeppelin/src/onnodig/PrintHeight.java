package onnodig;

import zeppelin.DistanceSensor;
//orig: in zeppelin
public class PrintHeight {
	public static void main(String args[]) {
		DistanceSensor sensor = new DistanceSensor();
		while(true) {
			int i=0;
			double sum=0;
			while(i<10){
				sum += sensor.getHeight();
			}
				System.out.println(sum/10);
		}
	}
}
