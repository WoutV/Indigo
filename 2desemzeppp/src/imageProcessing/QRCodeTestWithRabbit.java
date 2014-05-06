package imageProcessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.google.zxing.NotFoundException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class QRCodeTestWithRabbit {
	
	private static Channel channel= null;
	private static Connection connection =null;
	private static final String EXCHANGE_NAME = "server";
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		String tablet="indigo.tablets.tablet";
		String ip = "192.168.2.134";
		int port = 5672;
		String tabletnumber = "3";
		if(args.length>=3){
			ip = args[0];
			port = Integer.parseInt(args[1]);
			tabletnumber = args[2]; 
		}
		factory.setHost(ip);
			factory.setUsername("indigo");
			factory.setPassword("indigo");
			factory.setPort(port);	
			tablet = "indigo.tablets.tablet"+tabletnumber;
		System.out.println("Trying to connect to "+ip+" on port "+port);
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		System.out.println("Connection Created, creatring RSA keys");
		System.out.println("");
		Process p =Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\keys.py");
		p.waitFor();
		String key = read("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\public");
		System.out.println("Keys Created sending the public key");
		System.out.println("");
		byte[] message = key.getBytes();
		try {
			
			channel.basicPublish(EXCHANGE_NAME, tablet, null, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(key);
		System.out.println("Public key Sent");
		String text ="";
		int numberOfTries =0;
		Thread.sleep(3000);
		while(numberOfTries < 15){
			try{
				System.out.println("Trying to get the image, try "+numberOfTries +"\nFrom :http://"+ip+":"+5000+"/static/indigo3.png");
			text = QRCodeReader.readQRCode("http://"+ip+":"+port+"/static/indigo3.png");
			break;
			}
			catch(Exception exception){
				numberOfTries++;
			}
		}
		write("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\encrypted", text);
		System.out.println("Following text decoded from the image: \n"+text);
		p = Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\decription.py");
		p.waitFor();
		String output = read("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\result");
		System.out.println("Decoded: "+output);
		connection.close();

	}
	public static String read(String fileName){
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			result = sb.toString();
			br.close();
		} catch(IOException e) {
		}
		return result;
	}
	
	public static void write(String fileName, String text){
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println(text);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
		}
	}
}
