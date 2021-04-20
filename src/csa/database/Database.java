package csa.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Database implements IDatabase
{
	private final HashMap<String, DatabaseEntry> mapDbEntries = new HashMap<String, DatabaseEntry>();
	private final int iColumns = DatabaseEntry.COLUMNS;
	
	public Database(String szDbPath) throws DatabaseException
	{
		File fileDb = new File(szDbPath);
		if (!fileDb.exists())
		{
			throw new DatabaseException(String.format("Path %s does not exist.", szDbPath));
		}
		
		try 
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileDb));
			String szLine = null;
			int iLineNo = 0;
			while ((szLine = bufferedReader.readLine()) != null)
			{
				iLineNo++;
				String szValues[] = szLine.split(":");
				if (szValues.length != iColumns)
				{
					System.out.println("Invalid entry on line "+iLineNo);
					continue;
				}
				
				mapDbEntries.put(szValues[0], new DatabaseEntry(szValues[0], szValues[1], szValues[2]));
			}
			
			bufferedReader.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			throw new DatabaseException(String.format("Path %s does not exist.", szDbPath));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			throw new DatabaseException(String.format("Error opening file %s.", szDbPath));
		}
	}
	
	@Override
	public DatabaseEntry GetEntry(String szId) 
	{
		return mapDbEntries.get(szId);
	}

	@Override
	public String GetValueOfEntry(String szId, String szKey) 
	{
		if (szKey.equals(DatabaseEntry.KEY_PASS))
		{
			DatabaseEntry dbEntry = mapDbEntries.get(szId);
			if (dbEntry != null)
			{
				return dbEntry.getPass();
			}
		}
		else if (szKey.equals(DatabaseEntry.KEY_MARKS))
		{
			DatabaseEntry dbEntry = mapDbEntries.get(szId);
			if (dbEntry != null)
			{
				return dbEntry.getMarks();
			}
		}
		
		return null;
	}
}
