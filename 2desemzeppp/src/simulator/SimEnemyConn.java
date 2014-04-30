package simulator;

import java.io.IOException;
import java.util.LinkedList;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Class responsible for the conn between server and enemy sim.
 *
 */
public class SimEnemyConn implements Runnable{
	private static String serverIP="localhost";
	private static String exchangeName = "server";

	private Channel channel;
	private Connection connection;
	
	private String queueName;

	private LinkedList<String> keys = new LinkedList<>();
	
	private SimEnemy sim;

	public SimEnemyConn(SimEnemy sim) {
		this.sim = sim;
		
		keys.add("");

		initConn();

	}

	private void initConn() {
		connection = null;
		channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(serverIP);

			connection = factory.newConnection();
			channel = connection.createChannel();

			channel.exchangeDeclare(exchangeName, "topic");

			queueName = channel.queueDeclare().getQueue();

			for(String next : keys){
				channel.queueBind(queueName, exchangeName, next);
			}

		}
		catch  (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receives information.
	 */
	public void run() {
		try{
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, true, consumer);

			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String information = new String(delivery.getBody());

				handleReceived(information, delivery.getEnvelope().getRoutingKey());   
			}
		}
		catch (Exception exc) {
			System.out.println(exc);
			exc.printStackTrace();
			run();
		}
	}
	
	private void handleReceived(String information, String key) {
		if(key.equals("")) {
		}
	}
	
	/**
	 * Sends information.
	 */
	public void sendTransfer(String info, String key) {
		byte[] message = info.getBytes();
		try {
			channel.basicPublish(exchangeName, key, null, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
