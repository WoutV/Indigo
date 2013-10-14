
public class PrintHeight {
	public static void main(String args[]) {
		DistanceSensor sensor = new DistanceSensor();
		double oldHeight=0;
		while(true) {
		double newHeight = sensor.getHeight();
			if(Math.abs(newHeight-oldHeight)>1){
				oldHeight = newHeight;
				System.out.println(newHeight);
				
			}
		}
	}
}
