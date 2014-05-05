package connection;

import gui.GuiCommands;
import gui.GuiMain;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import simulator.Simulator;

import navigation.Dispatch;

import zeppelin.Propellor;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/*
 * TOE TE VOEGEN: RECEIVEHANDLER!!!
 */
public class ReceiverClient implements Runnable{
	
	private final GuiCommands gc;
	private final String EXCHANGE_NAME = "server";
	private ArrayList<String> keys;
	private String enemyTeam = "enemy";
	public ReceiverClient(GuiMain gui){
		
		gc = gui.getGuic();
		keys = new ArrayList<String>();
		// MOET NOG AANGEPAST WORDEN.
		keys.add(enemyTeam+ ".info.location");
		keys.add(enemyTeam+ ".info.height");
		keys.add("indigo.info.height");
		keys.add("indigo.private.motor1");
		keys.add("indigo.private.motor2");
		keys.add("indigo.private.motor3");
		keys.add("indigo.private.symbollist");
		keys.add("test.test");
		keys.add("enemy.info.target");
		
	}

	private void handleReceived(String information, String key){
		if(key.equals("indigo.info.height")){
			height(information, true);
		}else if(key.equals(enemyTeam+".info.location")){
			String[] parts = information.split(",");
			Dispatch.receiveEnemyLoc(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		}else if(key.equals(enemyTeam+".info.height")){
			height(information, false);
		}else if(key.equals("indigo.private.motor1")){
			if(information.equals("true"))
				gc.receivePropellorState(Propellor.X, true);
			else
				gc.receivePropellorState(Propellor.X, false);
		}else if(key.equals("indigo.private.motor2")){
			if(information.equals("true"))
				gc.receivePropellorState(Propellor.Y, true);
			else
				gc.receivePropellorState(Propellor.Y, false);
		}else if(key.equals("indigo.private.motor3")){
			if(information.equals("true"))
				gc.receivePropellorState(Propellor.UP, true);
			else
				gc.receivePropellorState(Propellor.UP, false);
		}else if(key.equals("indigo.private.symbollist")){
			Dispatch.processSymbols(Simulator.StringToSymbolList(information));
		}
		else if(key.equals("test.test")){
			System.out.println(information);
		}else if(key.equals("enemy.info.target")){
			String[] deel = information.split(",");
			
			Dispatch.receiveEnemyTarget(Integer.parseInt(deel[0]),Integer.parseInt(deel[1]) );
		}
	}
	public void run(){
		//hier komt connectie+ontvangst

		Connection connection = null;
		Channel channel = null;
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");

			factory.setUsername("indigo");
			factory.setPassword("indigo");
			factory.setPort(5673);
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
				String information = new String(delivery.getBody());
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
	//TEMP METHOD
//	boolean framemade= false;
//	private void imagePanel(BufferedImage bi){
//		JFrame frame = new JFrame("Pi Photo");  
//	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
//	      this.facePanel = new frame();  
//	      frame.setSize((int)bi.getWidth(),(int)bi.getHeight()); //give the frame some arbitrary size 
//	      frame.setBackground(Color.BLUE);
//	      frame.add(facePanel,BorderLayout.CENTER); 
//	      frame.setVisible(true);
//	      framemade=true;
//	}
//	private frame facePanel;
//	public void image(Transfer information){
//		System.out.println("We will be victorious!");
//		BufferedImage bi = toBufferedImage(information.getImage().getImage());
//		 if(!framemade)
//			 imagePanel(bi);
//	    
//		//		gc.receiveImage(information.getImage());
//		 
//			
//		ImageProcessor.processImage(information.getImage());
//		facePanel.bufferedImage(bi);
//		facePanel.repaint();
//		
//		//TODO:
//		//Purecolorlocator must be called here to process the information
//		// vraag hier de image om te gebruiken in image recognition.
//	}
	
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	
	public void height(String information, boolean bool){
		gc.receiveHeight(Integer.parseInt(information),bool);
	}
//	public void propellor(Transfer information) {
//		if(information.getPropellorMode() == Propellor.Mode.OFF)
//			gc.receivePropellorState(information.getPropellorId(), false);
//		else if(information.getPropellorMode() == Propellor.Mode.ON)
//			gc.receivePropellorState(information.getPropellorId(), true);
//		else if(information.getPropellorMode() == Propellor.Mode.PWM) {
//			if(Math.abs(information.getPropellorPwm()) >740)
//				gc.receivePropellorState(information.getPropellorId(), true);
//			else
//				gc.receivePropellorState(information.getPropellorId(), false);
//		}
//
//	}
}

