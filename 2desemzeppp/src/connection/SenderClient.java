package connection;

import java.io.IOException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderClient {
	private final String EXCHANGE_NAME = "server";
	
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
			
		}
		catch  (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendTransfer(String info, String key) {
		byte[] message = info.getBytes();
		try {
			System.out.println(key);
			channel.basicPublish(EXCHANGE_NAME, key, null, message);
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
