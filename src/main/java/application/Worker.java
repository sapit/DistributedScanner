package application;

import util.Attack;
import util.Attacks;

import java.rmi.RemoteException;

public interface Worker extends java.rmi.Remote {
	
	void handleTask(String s)throws RemoteException;

	void handleTask(Attack attack)throws RemoteException;

    void handleTask(Attacks.BasicWebAttack attack)throws RemoteException;
}
