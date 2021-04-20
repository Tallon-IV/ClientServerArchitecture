package csa.test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import csa.client.Client;
import csa.database.DatabaseEntry;
import csa.shared.RequestReply;

/*
 * Class to run various tests to see if the server runs as intended.
 */
public class ServerTest 
{	
	public static void main(String[] args)
	{
		try 
		{
			
			Client client = new Client(8888);
			//LiveCheck(client);
			//MalformedLiveCheck(client);
			ValidLoginCheck(client);
			//InvalidLoginCheck(client);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void LiveCheck(Client client) throws IOException
	{
		System.out.println("Performing Server Alive check.");
		RequestReply request = new RequestReply(RequestReply.HELLO, null);
		client.SendString(request.toString());
		String szResponse = client.ReceiveString();
		System.out.println(szResponse);
		assert RequestReply.Parse(szResponse).GetData().get("data").equals("WELCOME"): "Unexpected Response";
		System.out.println("Remote Server is online.");
	}
	
	public static void MalformedLiveCheck(Client client) throws IOException
	{
		System.out.println("Performing Malformed Server Alive check.");
		RequestReply request = new RequestReply("ELLO", null);
		client.SendString(request.toString());
		String szResponse = null;
		try
		{
			szResponse = client.ReceiveString();
		}
		catch (SocketTimeoutException ste)
		{
			System.out.println(ste);
		}
		System.out.println(szResponse);
		assert RequestReply.Parse(szResponse).GetData() != null: "No response.";
		System.out.println("Remote Server is online.");
	}
	
	public static void ValidLoginCheck(Client client) throws IOException
	{
		System.out.println("Performing Valid User Login check.");
		HashMap<String, String> mapData = new HashMap<String, String>();
		mapData.put(DatabaseEntry.KEY_ID, "w1617832");
		mapData.put(DatabaseEntry.KEY_PASS, "password01");
		RequestReply request = new RequestReply(RequestReply.AUTH, mapData);
		client.SendString(request.toString());
		String szResponse = client.ReceiveString();
		System.out.println(szResponse);
		assert RequestReply.Parse(szResponse).GetData().get("marks").equals("90"): "Unexpected data in response.";
	}
	
	public static void InvalidLoginCheck(Client client) throws IOException
	{
		System.out.println("Performing Invalid User Login check.");
		HashMap<String, String> mapData = new HashMap<String, String>();
		mapData.put(DatabaseEntry.KEY_ID, "w129994");
		mapData.put(DatabaseEntry.KEY_PASS, "password01");
		RequestReply request = new RequestReply(RequestReply.AUTH, mapData);
		client.SendString(request.toString());
		String szResponse = client.ReceiveString();
		System.out.println(szResponse);
		assert RequestReply.Parse(szResponse).GetData().containsKey("error"): "Expected error but received none.";
	}
}
