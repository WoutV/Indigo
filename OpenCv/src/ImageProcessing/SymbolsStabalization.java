package ImageProcessing;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class SymbolsStabalization {
	ArrayList<Symbol> latestDetectedSymbols;
	int maxTimeStamp;
	double maxDistance;
	public SymbolsStabalization(int maxTimeStamp, double maxDistance){
		latestDetectedSymbols=new ArrayList<Symbol>();
		this.maxTimeStamp = maxTimeStamp;
		this.maxDistance = maxDistance;
	}
	public synchronized void addSymbol(Symbol S){
		latestDetectedSymbols.add(S);
		System.out.println("Added Symbol " + S.toString());
//		System.out.println("TotalSize: "+ latestDetectedSymbols.size());
	}
	int timestamp;
	public int getCurrentTimestamp(){
		return timestamp;
	}
	public void setTimestamp(int timestamp){
		this.timestamp= timestamp;
		ArrayList<Symbol> toRemoveSymbols = new ArrayList<>();
		for(int i = 0 ; i < latestDetectedSymbols.size() ; i++){
			if(latestDetectedSymbols.get(i).getTimestamp()< getCurrentTimestamp()-maxTimeStamp ){
				toRemoveSymbols.add(latestDetectedSymbols.get(i));
//				System.out.println("Removing");
			}
		}
		latestDetectedSymbols.removeAll(toRemoveSymbols);
			
	}
	public void increaseTimestamp(){
		setTimestamp(getCurrentTimestamp()+1);
	}
	
	
	public synchronized Symbol getPossibleSymbol(Symbol S){
//		System.out.println("Detected: "+S.toString() );
//		System.out.println("time stamp symbol" + S.getTimestamp());
//		System.out.println("time stamp now" + getCurrentTimestamp());
		addSymbol(S);
		int heart = 0,circle=0,rectangle=0,star=0;
		Point2D symbolCoordinate = new Point2D.Double(S.getX(), S.getY());
		for(Symbol s: latestDetectedSymbols){
			Point2D p = new Point2D.Double(s.getX(), s.getY());
			if(p.distance(symbolCoordinate)<= maxDistance){
				if(s.getShape()==Symbol.Shape.CIRCLE)
					circle++;
				if(s.getShape()==Symbol.Shape.HEART)
					heart++;
				if(s.getShape()==Symbol.Shape.RECTANGLE)
					rectangle++;
				if(s.getShape()==Symbol.Shape.STAR)
					star++;				
			}
		}
//		System.out.println("C: "+ circle +" H: "+ heart+" R:" +rectangle +" S :"+ star);
		int max = Math.max(heart, Math.max(circle, Math.max(rectangle, star)));
		Symbol returningSymbol= S;		
		if(max == heart)
			returningSymbol =  new Symbol(S.getColour(), Symbol.Shape.HEART , getCurrentTimestamp(), S.getX(),S.getY() );
		else if(max == circle)
			returningSymbol = new Symbol(S.getColour(),Symbol.Shape.CIRCLE, getCurrentTimestamp(),S.getX(),S.getY());
		else if( max == rectangle)
			returningSymbol = new Symbol(S.getColour(),Symbol.Shape.RECTANGLE, getCurrentTimestamp(),S.getX(),S.getY());
		else 
			returningSymbol =new Symbol(S.getColour(),Symbol.Shape.STAR, getCurrentTimestamp(),S.getX(),S.getY());
	
//		System.out.println("ReturninSymbol: "+ returningSymbol.toString());
		return returningSymbol;
	}
		
	
	
	
	
	
	
	

}
