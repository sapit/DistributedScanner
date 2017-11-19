package application;

import util.Attacks;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.http.NameValuePair;

public interface Worker extends java.rmi.Remote {

    List<List<NameValuePair>> handleTask(Attacks.BasicWebAttack attack)throws RemoteException;
}
