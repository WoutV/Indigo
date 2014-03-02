package connection;

import gui.GuiCommands;
import gui.GuiMain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import transfer.Converter;
import transfer.Transfer;
import zeppelin.Propellor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/*
 * TOE TE VOEGEN: RECEIVEHANDLER!!!
 */
public class ReceiverClient implements Runnable{
	private final GuiMain gui;
	private final GuiCommands gc;
	private final String EXCHANGE_NAME = "server";
	
	public ReceiverClient(GuiMain gui){
		this.gui=gui;
		gc = gui.getGuic();
	}
	
	private String getTime(){
		DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss:SSS");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}

	private void handleReceived(Transfer information){
		switch(information.getTransferType()){
		
		case IMAGE:
			image(information);
			break;
		case HEIGHT:
			height(information);
			break;
		case PROPELLOR:
			propellor(information);
			break;
		default:
			break;

		}
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

			String bindingKey = "indigo.private.frompi";
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
	public void image(Transfer information){
		gc.receiveImage(information.getImage());
		// vraag hier de image om te gebruiken in image recognition.
	}
	public void height(Transfer information){
		gc.receiveHeight(information.getHeight(),true);
	}
	public void propellor(Transfer information) {
		if(information.getPropellorMode() == Propellor.Mode.OFF)
			gc.receivePropellorState(information.getPropellorId(), false);
		else if(information.getPropellorMode() == Propellor.Mode.ON)
			gc.receivePropellorState(information.getPropellorId(), true);
		else if(information.getPropellorMode() == Propellor.Mode.PWM) {
			if(Math.abs(information.getPropellorPwm()) >740)
				gc.receivePropellorState(information.getPropellorId(), true);
			else
				gc.receivePropellorState(information.getPropellorId(), false);
		}

	}
}

