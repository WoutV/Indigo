package connection;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import transfer.Converter;
import transfer.Transfer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderClient {
	private final String EXCHANGE_NAME = "server";
	private String routingKey;
	private Channel channel;
	private Connection connection;
	public SenderClient(){
		connection = null;
		channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");

			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(EXCHANGE_NAME, "topic");
			routingKey = "indigo.private";
		}
		catch  (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Geeft de huidige tijd terug in de vorm van een string.
	 */
	public static String getTime(){
		DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss:SSS");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	/*
	 * Stuurt transfer objecten van de pi naar de client.
	 */
	public void sendTransfer(Transfer transfer) {
		byte[] message = Converter.toBytes(transfer);
		try {
			channel.basicPublish(EXCHANGE_NAME, routingKey, null, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/*
	 * Deze methode gebruiken om de connectie te sluiten.
	 */
	public void exit(){
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
