package csa.database;

public interface IDatabase 
{
	/**
	 * Retrieves an entry by unique key.
	 * @param szId    Unique key that represents data.
	 * @return        Returns a custom representation of the row matching the id or null if no match.
	 */
	public Object GetEntry(String szId);
	
	/**
	 * Retrieves a value from a specific key.
	 * @param szId    Unique key that identifies an entry.
	 * @param szKey   Key representing value in entry.
	 * @return        Returns a custom representation of the value or null if invalid id or key.
	 */
	public Object GetValueOfEntry(String szId, String szKey);
}
