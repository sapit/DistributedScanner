package client;

import java.rmi.RemoteException;

import util.Attacks.BasicWebAttack;

public interface ClientCallback extends java.rmi.Remote {
	
	void update(BasicWebAttack attacks) throws RemoteException;

}
