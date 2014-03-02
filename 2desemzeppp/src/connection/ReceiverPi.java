package connection;

import transfer.Converter;
import transfer.Transfer;

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
	private Main main = Main.getInstance();
	
	private final String EXCHANGE_NAME = "server";
	
	public ReceiverPi(){
		
	}
	
	
	private void handleReceived(Transfer information){
		switch(information.getTransferType()){
		case MOTOR1:
			main.activateMotor1(information.getPropellorPwm());
			break;
		case MOTOR2:
			main.activateMotor2(information.getPropellorPwm());
			break;
		case MOTOR3:
			main.activateMotor3(information.getPropellorPwm());
			break;
		case DESTINATION:
			main.goToDestination(information);
		default:
			break;

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
	      factory.setHost("localhost");
	  
	      connection = factory.newConnection();
	      channel = connection.createChannel();

	      channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	      String queueName = channel.queueDeclare().getQueue();
	 
	      String bindingKey = "indigo.private.fromclient";
	      channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
	      
	    
	      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	      QueueingConsumer consumer = new QueueingConsumer(channel);
	      channel.basicConsume(queueName, true, consumer);

	      while (true) {
	        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	        Transfer information = Converter.fromBytes(delivery.getBody());
	        handleReceived(information);   
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
