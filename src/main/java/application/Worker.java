package application;

import util.Attacks;

import java.rmi.RemoteException;

public interface Worker extends java.rmi.Remote {

    void handleTask(Attacks.BasicWebAttack attack)throws RemoteException;
}
