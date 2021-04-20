package csa.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

public class UDPServer {

	public static void main(String[] args) throws IOException 
	{
		System.out.println("Initialising Server...");
		DatagramSocket dgram = new DatagramSocket(8888, InetAddress.getByName("127.0.0.1"));
		byte buf[] = new byte[256];
		String pong = "lolUDPPongBack";
		DatagramPacket p = new DatagramPacket(buf, buf.length);
		System.out.println("Listening for UDP Packets...");
		dgram.receive(p);
		System.out.println(String.format("Received: %s from %s:%d", new String(p.getData()), p.getAddress().getHostAddress(), p.getPort()));
		p = new DatagramPacket(pong.getBytes(), pong.length(), p.getAddress(), p.getPort());
		System.out.println("Sending response...");
		dgram.send(p);
		System.out.println("Closing server...");
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
				{

					@Override
					public void run() 
					{
						dgram.close();
					}
			
				}, 5000);
	}

}
