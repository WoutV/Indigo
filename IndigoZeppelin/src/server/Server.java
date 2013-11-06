package server;
import java.io.*;
import java.net.*;
import server.SendToClient;
import zeppelin.Main;


public class Server {
	public static void main(String[] args) throws IOException {
		final int port = 6789;
		System.out.println("Server waiting for connection on port "+port);
		@SuppressWarnings("resource")
		ServerSocket ss = new ServerSocket(port);
		Socket clientSocket = ss.accept();
		System.out.println("Recieved connection from "+clientSocket.getInetAddress()+" on port "+clientSocket.getPort());
		//create two threads to send and recieve from client
		RecieveFromClientThread recieve = new RecieveFromClientThread(clientSocket);
		Thread thread = new Thread(recieve);
		thread.start();
		SendToClient send = new SendToClient(clientSocket);
		ReadInput ri = new ReadInput(send);
		Thread thread2 = new Thread(ri);
		thread2.start();
		Main main = Main.getInstance();
		main.setSender(send);
		Thread thread3 = new Thread(main);
		thread3.start();
	}}
