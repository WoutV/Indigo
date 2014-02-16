package communication;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import transfer.Converter;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
public class ServerTest {
	public static class Send {

		private final static String QUEUE_NAME = "hello";

		public static void main(String[] args)
				throws java.io.IOException {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//			int n = 0;
//			while(n<10){
//				String message = "we shall rule!" + n;
//				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//				System.out.println(" [x] Sent '" + message +"'"+ServerTest.Send.getTime());
//				n++;
//			}
			transfer.Transfer transfer = new transfer.Transfer();
			transfer.setMessage("letestmessage");
			
			byte[] message = Converter.toBytes(transfer);
			channel.basicPublish("", QUEUE_NAME, null, message);
			System.out.println("message send" + ServerTest.Send.getTime());
			channel.close();
			connection.close();
		}
		public static String getTime(){
			DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss:SSS");
			Calendar cal = Calendar.getInstance();
			return dateFormat.format(cal.getTime());
		}
	}


}
