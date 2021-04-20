package csa.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;

/*
 * Handles opening and retrieving data from a json file.
 */
public class JSONDatabase implements IDatabase
{
	private JsonObject json = null;
	public JSONDatabase(String szDbPath) throws JSONDatabaseException
	{
		File fileDb = new File(szDbPath);
		if (!fileDb.exists())
		{
			throw new JSONDatabaseException(String.format("Path %s does not exist.", szDbPath));
		}
		
		try 
		{
			json = Json.createReader(new BufferedReader(new FileReader(fileDb))).readObject();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			throw new JSONDatabaseException(String.format("Path %s does not exist.", szDbPath));
		}
		catch (JsonException je)
		{
			je.printStackTrace();
			throw new JSONDatabaseException(String.format("Error opening %s. %s", szDbPath, je.toString()));
		}
	}
	
	@Override
	public JsonObject GetEntry(String szId) 
	{
		return json.getJsonObject(szId);
	}

	@Override
	public String GetValueOfEntry(String szId, String key) 
	{
		JsonObject jsonEntry = json.getJsonObject(szId);
		if (jsonEntry == null)
		{
			return null;
		}
		
		return jsonEntry.getString(key);
	}

}
