package gui;

public class GUIEvent {

	public enum EventType {
		HeightReceived,PropStatus,ReceivedLocation,ReceivedOther,SentOther,Misc;
	}
	
	EventType type;
	String text;
	
	public GUIEvent(EventType type,String text) {
		this.type = type;
		this.text = text;
	}
}
