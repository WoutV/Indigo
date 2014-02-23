package connection;

import gui.GuiMain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import transfer.Converter;
import transfer.Transfer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/*
 * TOE TE VOEGEN: RECEIVEHANDLER!!!
 */
public class ReceiverClient implements Runnable{
	private final GuiMain gui;
	private final String EXCHANGE_NAME = "server";
	public ReceiverClient(GuiMain gui){
		this.gui=gui;
	}
	private String getTime(){
		DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss:SSS");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	public void run(){
		//hier komt connectie+ontvangst
		
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

