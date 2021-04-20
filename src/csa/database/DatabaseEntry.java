package csa.database;

public class DatabaseEntry 
{
	private String szId;
	private String szPass;
	private String szMarks;
	
	public final static int COLUMNS = 3;
	public final static String KEY_ID = "id";
	public final static String KEY_PASS = "pass";
	public final static String KEY_MARKS = "marks";
	
	public DatabaseEntry(String szId, String szPass, String szMarks)
	{
		this.szId = szId;
		this.szPass = szPass;
		this.szMarks = szMarks;
	}
	
	public String getId() 
	{
		return szId;
	}


	public String getPass() 
	{
		return szPass;
	}


	public String getMarks() 
	{
		return szMarks;
	}

	@Override
	public String toString() 
	{
		return String.format("%s:%s:%s", szId, szPass, szMarks);
	}
}
