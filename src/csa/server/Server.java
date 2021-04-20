package csa.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import csa.shared.RequestReply;

public class Server implements IServer
{
	private boolean bRunning;
	private int iPort = 8888;
	private final ArrayList<IServerTask> rgServerTasks = new ArrayList<IServerTask>();
	private final Thread objThread = new Thread(this);
	
	
	@Override
	public void Start() 
	{
		bRunning = true;
		objThread.start();
	}
	
	public void Start(int iPort)
	{
		this.iPort = iPort;
		Start();
	}

	@Override
	public void Stop() 
	{
		bRunning = false;
	}
	
	@Override
	public void AddServerTask(IServerTask objTask) 
	{
		if (rgServerTasks.contains(objTask))
		{
			return;
		}
		
		rgServerTasks.add(objTask);		
	}

	@Override
	public void RemoveServerTask(IServerTask objTask) 
	{
		rgServerTasks.remove(objTask);	
	}

	@Override
	public void RemoveServerTaskByClass(Class<IServerTask> classTask) 
	{
		if (classTask == IServerTask.class)
		{
			rgServerTasks.clear();
			return;
		}
		
		for (int i = 0; i < rgServerTasks.size(); i++)
		{
			if (rgServerTasks.get(i).getClass() == classTask)
			{
				rgServerTasks.remove(i);
			}
		}
	}

	@Override
	public void run() 
	{
		ServerSocket srvSock = null;
		try 
		{
			srvSock = new ServerSocket(iPort);
			System.out.println(String.format("Server Online at %s:%d", srvSock
					.getInetAddress().getHostAddress(), srvSock.getLocalPort()));
			while (bRunning)
			{
				Socket incomingConnection = srvSock.accept();
				RemoteClient client = new RemoteClient(incomingConnection.getPort(), incomingConnection
						.getInetAddress().getHostAddress());
				
				System.out.println("Client connected: "+client);
				
				char[] chBuffer = new char[1024];
				System.out.println("Attempting to read data from client...");
				new InputStreamReader(incomingConnection.getInputStream(), "UTF-8").read(chBuffer);
				System.out.println("Data received: "+new String(chBuffer));
				rgServerTasks.forEach(new Consumer<IServerTask>(){
					public void accept(IServerTask serverTask)
					{
						RequestReply request = RequestReply.Parse(new String(chBuffer));
						serverTask.OnDataReceived(incomingConnection, client, request);
					}
				});
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Closing Server...");
			if (srvSock == null)
			{
				return;
			}
			
			try 
			{
				srvSock.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
