package csa.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


/*
 * Handles the sending and receiving of data on a client side application.
 */
public class Client 
{
	private Socket sockClient;
	
	public Client(int iPort) throws IOException
	{
		sockClient = new Socket("localhost", iPort);
		sockClient.setSoTimeout(10000);
	}
	
	public void SendBytes(byte[] bytes) throws IOException
	{
		send(new String(bytes));
	}
	
	public void SendString(String data) throws IOException
	{
		send(data);
	}
	
	public byte[] ReceiveBytes() throws IOException
	{
		return new String(receive()).getBytes();
	}
	
	public String ReceiveString() throws IOException
	{
		return new String(receive());
	}
	
	private char[] receive() throws IOException
	{
		System.out.println("Receiving Data...");
		char cbuf[] = new char[1024];
		new InputStreamReader(sockClient.getInputStream(), "UTF-8")
		.read(cbuf);
		
		System.out.println("Data Received: "+new String(cbuf));
		return cbuf;
	}
	
	private void send(String data) throws IOException
	{
		System.out.println("Sending Data: "+data);
		OutputStreamWriter osw = new OutputStreamWriter(sockClient.getOutputStream(), "UTF-8");
		osw.write(data);
		osw.flush();
		System.out.println("Data Sent!");
	}
}
