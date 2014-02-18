package connection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import transfer.Converter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;


public class ReceiverClient {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args)
			throws java.io.IOException,
			java.lang.InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(QUEUE_NAME, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			transfer.Transfer transfer = Converter.fromBytes(delivery.getBody());
			System.out.println(transfer.getMessage()+ ReceiverClient.getTime());
			//String message = new String(delivery.getBody());
			//System.out.println(" [x] Received '" + message + "'" + Receiver.Recv.getTime());
		}
	}
	public static String getTime(){
		DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss:SSS");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
}
