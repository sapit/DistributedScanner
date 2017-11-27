package application;

import org.apache.http.NameValuePair;
import util.Attacks;
import util.UrlAttacker;

import java.rmi.RemoteException;
import java.util.List;

public class Worker {

	private String name;
	public Worker(){
		super();
		this.name = "default";
		
		System.out.println("Started worker: ");
	}

	public Worker(String name){
		super();
		this.name = name;
		
		System.out.println("Started worker: ");
	}

    public List<List<NameValuePair>> handleTask(Attacks.BasicWebAttack attack) throws RemoteException {
        List<List<NameValuePair>> successes = UrlAttacker.performAttack(attack);
        if(successes.size()>0){
            System.out.println(successes);
        }
        return successes;
        
    }

}
