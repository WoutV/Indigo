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

public class qrcodetester {
	
	private static Channel channel= null;
	private static Connection connection =null;
	private static final String EXCHANGE_NAME = "server";
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.2.134");
		factory.setUsername("indigo");
		factory.setPassword("indigo");
		factory.setPort(5672);
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		Process p =Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\keys.py");
		p.waitFor();
		String key = read("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\public");
		byte[] message = key.getBytes();
		try {
			
			channel.basicPublish(EXCHANGE_NAME, "indigo.tablets.tablet3", null, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(key);
		String text ="";
		Thread.sleep(5000);
		write("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\encrypted", text);
		p = Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\decription.py");
		p.waitFor();
		String output = read("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\result");
		System.out.println(output);
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
