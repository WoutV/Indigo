package server;
import java.io.*;
import java.net.*;
import server.SendToClient;
import zeppelin.Main;


public class Server {
	/**
	 * Initializes the server and waits on port 6789. When the connection is made,
	 * the threads(imageThread en ReceiveFromClientThread & ReadInputThread are started.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final int port = 6789;
		System.out.println("Server waiting for connection on port "+port);
		@SuppressWarnings("resource")
		ServerSocket ss = new ServerSocket(port);
		@SuppressWarnings("resource")
		ServerSocket imageSocket = new ServerSocket(6790);
		Socket clientSocket = ss.accept();
		Socket imageSock = imageSocket.accept();
		System.out.println("Recieved connection from "+clientSocket.getInetAddress()+" on port "+clientSocket.getPort());
		//create two threads to send and recieve from client
		ReceiveFromClientThread recieve = new ReceiveFromClientThread(clientSocket);
		Thread thread = new Thread(recieve);
		thread.start();
		SendToClient send = new SendToClient(clientSocket);
		SendToClient imageSender = new SendToClient(imageSock);
		ReadInput ri = new ReadInput(send,imageSender);
		Thread thread2 = new Thread(ri);
		thread2.start();
		Main main = Main.getInstance();
		main.setSender(send);
		Thread thread3 = new Thread(main);
		thread3.start();
	}}
