package connection;

import java.util.ArrayList;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import zeppelin.Main;



/*
 * Deze klasse ontvangt messages die bepaald worden door het protocol dus geen transfer objecten.
 * TOE TE VOEGEN: RECEIVEDHANDLER!!!
 */
public class ReceiverPi implements Runnable{
	//private Main main = Main.getInstance();
	
	private final String EXCHANGE_NAME = "server";
	private ArrayList<String> keys;
	
	public ReceiverPi(){
		keys = new ArrayList<String>();
		keys.add("indigo.lcommand.motor1");
		keys.add("indigo.lcommand.motor2");
		keys.add("indigo.lcommand.motor3");
		keys.add("test.test");
	}
	
	
	private void handleReceived(String information, String key){
		if(key.equals("indigo.lcommand.motor1")){
			//main.activateMotor1(Integer.parseInt(information));
		}else if(key.equals("indigo.lcommand.motor2")){
			//main.activateMotor2(Integer.parseInt(information));
		}else if(key.equals("indigo.lcommand.motor3")){
			//main.activateMotor3(Integer.parseInt(information));
		}else if(key.equals("test.test")){
			System.out.println(information);
		}
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
	      factory.setHost(SenderPi.serverIP);
	  
	      connection = factory.newConnection();
	      channel = connection.createChannel();

	      channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	      String queueName = channel.queueDeclare().getQueue();
	 
	      for(String next : keys){
	      
	      channel.queueBind(queueName, EXCHANGE_NAME, next);
	      }
	    
	      QueueingConsumer consumer = new QueueingConsumer(channel);
	      channel.basicConsume(queueName, true, consumer);

	      while (true) {
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	        String information = delivery.getBody().toString();
	        
	        handleReceived(information, delivery.getEnvelope().getRoutingKey());   
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
