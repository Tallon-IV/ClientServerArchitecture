package csa.server;

public interface IServer extends Runnable
{
	/**
	 * Starts instance of IServer.
	 */
	public void Start();
	
	/**
	 * Stops instance of IServer.
	 */
	public void Stop();
	
	/**
	 * Adds a task to an instance of IServer. Look at IServerTask class for more info
	 * regarding the purpose of a task.
	 * @param objTask    Instance of IServerTask.
	 */
	public void AddServerTask(IServerTask objTask);
	
	/**
	 * Removes a specific task from an instance of IServer.
	 * @param objTask
	 */
	public void RemoveServerTask(IServerTask objTask);
	
	/**
	 * Removes all tasks from an instance of IServer by class type.
	 * @param classTask
	 */
	public void RemoveServerTaskByClass(Class<IServerTask> classTask);
}
