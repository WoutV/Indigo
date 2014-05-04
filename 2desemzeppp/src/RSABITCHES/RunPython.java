package RSABITCHES;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class RunPython {
	public static void main(String[] args){
		RunPython rp = new RunPython();
	
			try {
				rp.run();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}

	
	public void run() throws InterruptedException{
		try {
			Process p =Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\keys.py");
			p.waitFor();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	
		String key = read("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\public");
		System.out.println(key);
		//SEND KEY TO RABBITMQ
		
		//READ QR CODE
	//	String text=read("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\result.txt");
//		try {
//			text = QRCodeReader.readQRCode();
//		} catch (NotFoundException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			Process p =Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\encrypt.py");
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("Encrypted");
		
		//write("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\encrypted.txt", text);
		
		try {
			Process p = Runtime.getRuntime().exec("cmd /c C:\\Python27\\python C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\resources\\decription.py");
			p.waitFor();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		String output = read("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\result");
		System.out.println(output);
		
	}
	
	
	public String read(String fileName){
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
	
	public void write(String fileName, String text){
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName, "UTF-8");
			writer.println(text);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
		}
	}
}
