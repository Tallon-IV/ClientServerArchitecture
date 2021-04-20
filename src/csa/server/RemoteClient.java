package csa.server;

/*
 * Represents the remote client that connected to the server.
 */
public class RemoteClient 
{
	private int iPort;
	private String szIpAddress;
	
	public RemoteClient(int iPort, String szIpAddress)
	{
		this.iPort = iPort;
		this.szIpAddress = szIpAddress;
	}

	public int getiPort() 
	{
		return iPort;
	}

	public String getSzIpAddress() 
	{
		return szIpAddress;
	}

	@Override
	public String toString() 
	{
		return String.format("Client [%s:%d]", szIpAddress, iPort);
	}
}
