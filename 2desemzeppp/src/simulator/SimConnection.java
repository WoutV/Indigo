package simulator;

import java.io.IOException;
import java.util.LinkedList;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * Class responsible for the connection between client and Sim.
 *
 */
public class SimConnection implements Runnable{
	private static String serverIP;
	private static String exchangeName = "server";
	private int port;
	private Channel channel;
	private Connection connection;
	
	private String queueName;

	private LinkedList<String> keys = new LinkedList<>();
	
	private Simulator sim;

	public SimConnection(Simulator sim, String serverIP, int port) {
		this.sim = sim;
		this.serverIP=serverIP;
		this.port = port;
		//x and y
		keys.add("indigo.lcommand.motor1");
		keys.add("indigo.lcommand.motor2");

		initConn();

	}

	private void initConn() {
		connection = null;
		channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(serverIP);

			factory.setUsername("indigo");
			factory.setPassword("indigo");
			factory.setPort(port);
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
		if(key.equals("indigo.lcommand.motor1")) {
			sim.handleInput(1, Integer.parseInt(information));
		}
		if(key.equals("indigo.lcommand.motor2")) {
			sim.handleInput(2, Integer.parseInt(information));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
