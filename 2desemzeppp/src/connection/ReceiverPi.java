package connection;

import transfer.Converter;
import transfer.Transfer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import zeppelin.Main;
import zeppelin.MotorController;


/*
 * Deze klasse ontvangt messages die bepaald worden door het protocol dus geen transfer objecten.
 * TOE TE VOEGEN: RECEIVEDHANDLER!!!
 */
public class ReceiverPi implements Runnable{
	private Main main = Main.getInstance();
	private MotorController mc;
	private final String EXCHANGE_NAME = "server";
	
	public ReceiverPi(){
		mc = main.getMotorController();
	}
	
	/*
	 * Zal ik volgende keer nog wel aanpassen nadat het protocol is goedgekeurd!!!
	 */
	public void run(){
		// ontvangt hier de bytes die door werden gestuurd.

		Connection connection = null;
	    Channel channel = null;
	    try {
	      ConnectionFactory factory = new ConnectionFactory();
	      factory.setHost("localhost");
	  
	      connection = factory.newConnection();
	      channel = connection.createChannel();

	      channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	      String queueName = channel.queueDeclare().getQueue();
	 
	      String bindingKey = "indigo.private";
	      channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
	      
	    
	      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	      QueueingConsumer consumer = new QueueingConsumer(channel);
	      channel.basicConsume(queueName, true, consumer);

	      while (true) {
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	        Transfer information = Converter.fromBytes(delivery.getBody());
	        String message = information.getMessage();
	        String routingKey = delivery.getEnvelope().getRoutingKey();

	        System.out.println(" [x] Received '" + routingKey + "':'" + message + "'");   
	      }
	    }
	    catch  (Exception e) {
	      e.printStackTrace();
	    }
	    finally {
	      if (connection != null) {
	        try {
	          connection.close();
	        }
	        catch (Exception ignore) {}
	      }
	    }
	}
}
