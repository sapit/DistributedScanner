package messagequeue;

import application.Worker;
import util.Attacks;

public interface RMIMessageQueue extends java.rmi.Remote {
	
	void createTask(String s) throws java.rmi.RemoteException;

	void createTask(Attacks.BasicWebAttack attack) throws java.rmi.RemoteException;
	
	void registerWorker(Worker w) throws java.rmi.RemoteException;

	String getTaskString() throws java.rmi.RemoteException;

    Attacks.BasicWebAttack getTask() throws java.rmi.RemoteException;
}
