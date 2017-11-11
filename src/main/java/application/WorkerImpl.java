package application;

import messagequeue.RMIMessageQueue;
import org.apache.http.NameValuePair;
import util.Attack;
import util.Attacks;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;

public class WorkerImpl extends java.rmi.server.UnicastRemoteObject implements Worker {

	private String name;
	private int count;
	private UrlAttacker urlAttacker;
	
	protected WorkerImpl() throws RemoteException {
		super();
		this.name = "default";
		count = 0;
		this.urlAttacker = new UrlAttacker();
	}
	
	protected WorkerImpl(String name) throws RemoteException {
		super();
		this.name = name;
		count = 0;
	}

	@Override
	public void handleTask(String s)throws RemoteException {
        System.out.println(name + " handled task " + count + ": " + s);
		count++;
		
	}

    @Override
    public void handleTask(Attack attack) throws RemoteException {
        ;
    }

    @Override
    public void handleTask(Attacks.BasicWebAttack attack) throws RemoteException {
        List<List<NameValuePair>> successes = UrlAttacker.performAttack(attack);
        if(successes.size()>0)
            System.out.println(successes);
    }

}
