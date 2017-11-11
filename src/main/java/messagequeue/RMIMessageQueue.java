package messagequeue;

import util.Attacks;

public interface RMIMessageQueue extends java.rmi.Remote {

	void createTask(Attacks.BasicWebAttack attack) throws java.rmi.RemoteException;

    Attacks.BasicWebAttack getTask() throws java.rmi.RemoteException;
}
