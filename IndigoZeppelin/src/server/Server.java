package server;
import java.io.*;
import java.net.*;
import camera.*;
import server.SendToClient;
import zeppelin.Main;
import zeppelin.QRParser;


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
		@SuppressWarnings("resource")
		ServerSocket receiveSocket = new ServerSocket(6791);
		Socket clientSocket = ss.accept();
		Socket imageSock = imageSocket.accept();
		Socket receive = receiveSocket.accept();
//		System.out.println("Recieved connection from "+clientSocket.getInetAddress()+" on port "+clientSocket.getPort());
		//create two threads to send and recieve from client
		ReceiveFromClientThread recieve = new ReceiveFromClientThread(receive);
		Thread thread = new Thread(recieve);
		thread.start();
//		System.out.println("ReceiveFromClientThread initialized");
		SendToClient send = new SendToClient(clientSocket);
		SendToClient imageSender = new SendToClient(imageSock);
		Main main = Main.getInstance();
//		System.out.println("Initializing Main");
		main.init(send);
		System.out.println("Main Initialized");
//		ReadInput motor = new ReadInput(send, imageSender);
		sensor s = new sensor();
		Thread thread2 = new Thread(s);
//		Thread thread2 = new Thread(motor);
		thread2.start();
//		CameraTest cameraTest= new CameraTest(imageSender);
//		Thread camera = new Thread(cameraTest);
//		camera.start();
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		CameraThread ct = new CameraThread(imageSender);
//		Thread cameraT = new Thread(ct);
//		cameraT.start();
	}}
