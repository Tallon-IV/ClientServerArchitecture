package csa.shared;

import java.util.HashMap;

/**
 * Simple Class dictating the rules of communication between the Client and Server.
 * @author UltimateUser
 *
 */
public class RequestReply 
{
	private String szRequestType;
	private HashMap<String, String> mapData;
	
	public static final String AUTH = "AUTH";
	public static final String HELLO = "HELLO";
	public static final String END = "END";
	public static final String ERRORKEY = "error";
	
	public RequestReply(String szRequestType, HashMap<String, String> mapData)
	{
		this.szRequestType = szRequestType;
		this.mapData = mapData;
	}
	
	public String GetRequestType()
	{
		if (szRequestType == null)
		{
			return "";
		}
		
		return szRequestType;
	}
	
	public HashMap<String, String> GetData()
	{
		return mapData;
	}
	
	@Override
	public String toString() 
	{
		if (szRequestType == null)
		{
			return "";
		}
		
		StringBuilder szSerialisedRequestReply = new StringBuilder();
		szSerialisedRequestReply.append(szRequestType+"@ ");
		if (mapData != null)
		{
			String szDataFormat = "%s=%s,";
			mapData.forEach((k,v) -> szSerialisedRequestReply
					.append(String.format(szDataFormat, k, v)));
		}
		
		return szSerialisedRequestReply.toString();
	}
	
	
	public static RequestReply Parse(String szResponse)
	{
		if (szResponse == null)
		{
			return new RequestReply(null, null);
		}
		
		String szResponseComponents[] = szResponse.split("@");
		HashMap<String, String> mapData = new HashMap<String, String>();
		if (szResponseComponents.length != 2)
		{
			mapData.put("error", "Invalid message format.");
			return new RequestReply(szResponseComponents[0], mapData);
		}
		
		String szKeyvalues[] = szResponseComponents[1].trim().split(",");
		System.out.println(szKeyvalues[0]);
		if (szKeyvalues.length > 0 && !szKeyvalues[0].isEmpty())
		{
			for (int i = 0; i < szKeyvalues.length; i++)
			{
				String szKeyvalue[] = szKeyvalues[i].split("=");
				mapData.put(szKeyvalue[0], szKeyvalue[1]);
			}
		}
		
		return new RequestReply(szResponseComponents[0], mapData);
	}
}
