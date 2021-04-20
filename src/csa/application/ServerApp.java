package csa.application;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

import csa.database.Database;
import csa.database.DatabaseEntry;
import csa.server.RemoteClient;
import csa.server.IServerTask;
import csa.server.Server;
import csa.shared.RequestReply;

/*
 * Main Server Application.
 */
public class ServerApp 
{
	private final static Database objDb = new Database("UserMarks.txt");
	private final static String KEY_SHUTDOWNCODE = "sdcode";
	private final static String SHUTDOWNCODE = "abc123";
	public static void main(String[] arg)
	{
		Server server = new Server();
		IServerTask serverTask = new IServerTask() {

			@Override
			public void OnClientConnected(Socket socket, RemoteClient client) 
			{
				// Connectionless server so no need to implement this.
			}

			@Override
			public void OnDataReceived(Socket socket, RemoteClient client, RequestReply request) 
			{
				boolean bShutdown = false;
				System.out.println(String.format("OnDataReceived: %s, Data-%s", 
						client, request.toString()));
				System.out.println("Request Type: "+request.GetRequestType());
				OutputStreamWriter streamOutWriter = null;
				try 
				{
					streamOutWriter= new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					String szRequestType = request.GetRequestType();
					HashMap<String, String> mapData = request.GetData();
					RequestReply response = null;
					if (szRequestType.equals(RequestReply.HELLO))
					{
						HashMap<String, String> mapResponseData = new HashMap<String, String>();
						mapResponseData.put("data", "WELCOME");
						response = new RequestReply(RequestReply.HELLO, mapResponseData);
					}
					else if (szRequestType.equals(RequestReply.AUTH))
					{
						String szId = mapData.get(DatabaseEntry.KEY_ID);
						String szPass = mapData.get(DatabaseEntry.KEY_PASS);
						String szUserPass = objDb.GetValueOfEntry(szId, DatabaseEntry.KEY_PASS);
						if (szPass != null && szUserPass != null && szPass.equals(szUserPass))
						{
							HashMap<String, String> mapResponseData = new HashMap<String, String>();
							mapResponseData.put(DatabaseEntry.KEY_MARKS, 
									objDb.GetValueOfEntry(szId, DatabaseEntry.KEY_MARKS));
							response = new RequestReply(RequestReply.AUTH, mapResponseData);
						}
						else
						{
							HashMap<String, String> mapResponseData = new HashMap<String, String>();
							mapResponseData.put(RequestReply.ERRORKEY, "Invalid Authentication Details.");
							response = new RequestReply(RequestReply.AUTH, mapResponseData);
						}
					}
					else if (szRequestType.equals(RequestReply.END))
					{
						String szSdCode = mapData.get(KEY_SHUTDOWNCODE);
						if (szSdCode.equals(SHUTDOWNCODE))
						{
							HashMap<String, String> mapResponseData = new HashMap<String, String>();
							mapResponseData.put("data", "Server Shutting Down...");
							response = new RequestReply(RequestReply.END, mapResponseData);
							bShutdown = true;
						}
						else
						{
							HashMap<String, String> mapResponseData = new HashMap<String, String>();
							mapResponseData.put(RequestReply.ERRORKEY, "Invalid Shutdown Code");
							response = new RequestReply(RequestReply.END, mapResponseData);
						}
					}
					
					if (response != null)
					{
						String szResponse = response.toString();
						System.out.println("Responding with: "+szResponse);
						streamOutWriter.write(szResponse);
						streamOutWriter.flush();
					}
					
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
				if (bShutdown)
				{
					throw new RuntimeException("Shutdown code received.");
				}
			}
			
		};
		
		server.AddServerTask(serverTask);
		server.Start();
	}
}
