package messagequeue;

import util.Attacks;

import java.rmi.RemoteException;

import client.ClientCallback;

public interface RMIMessageQueue extends java.rmi.Remote {

	void createTask(Attacks.BasicWebAttack attack, ClientCallback clientCallback) throws java.rmi.RemoteException;

    Attacks.BasicWebAttack getTask() throws java.rmi.RemoteException;
    
    void updateSuccessfulAttack(Attacks.BasicWebAttack attack) throws RemoteException;
}
