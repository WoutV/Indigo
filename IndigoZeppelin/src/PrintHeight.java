
public class PrintHeight {
	public static void main(String args[]) {
		DistanceSensor sensor = new DistanceSensor();
		while(true) {
		System.out.println(sensor.getHeight());
		}
	}
}
