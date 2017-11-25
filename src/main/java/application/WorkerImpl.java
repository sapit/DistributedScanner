package application;

import org.apache.http.NameValuePair;
import util.Attacks;
import util.UrlAttacker;

import java.rmi.RemoteException;
import java.util.List;

public class WorkerImpl extends java.rmi.server.UnicastRemoteObject implements Worker {

	private String name;
	protected WorkerImpl() throws RemoteException {
		super();
		this.name = "default";
	}
	
	protected WorkerImpl(String name) throws RemoteException {
		super();
		this.name = name;
	}

    @Override
    public List<List<NameValuePair>> handleTask(Attacks.BasicWebAttack attack) throws RemoteException {
        List<List<NameValuePair>> successes = UrlAttacker.performAttack(attack);
        if(successes.size()>0){
            System.out.println(successes);
        }
        return successes;
        
    }

}
