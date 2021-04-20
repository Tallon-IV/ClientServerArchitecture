package csa.server;

import java.net.Socket;

import csa.shared.RequestReply;

public interface IServerTask 
{
	/**
	 * Callback for when a server accepts a connection from a client.
	 * @param client    Object for connected client's data.
	 */
	public void OnClientConnected(Socket socket, RemoteClient client);
	
	
	/**
	 * Callback for when a server receives data.
	 * 
	 * @param client    Object for connected client's data.
	 * @param data      Object containing data for a request/response.
	 */
	public void OnDataReceived(Socket socket, RemoteClient client, RequestReply data);
}
