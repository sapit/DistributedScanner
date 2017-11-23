package messagequeue;

import application.Worker;
import util.Attacks;
import util.Attacks.BasicWebAttack;


import java.rmi.RemoteException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import Client.ClientCallback;

public class RMIMessageQueueImpl extends java.rmi.server.UnicastRemoteObject implements RMIMessageQueue {
	BlockingQueue<Attacks.BasicWebAttack> queue;
	BlockingQueue<Attacks.BasicWebAttack> successfulAttacks;
	ConcurrentHashMap<String, Set<Client.ClientCallback>> url_subscribers;
	
	public RMIMessageQueueImpl()  throws RemoteException {
		super();
        queue = new LinkedBlockingQueue<>();
        successfulAttacks = new LinkedBlockingQueue<>();
	}
	
	@Override
	public void createTask(Attacks.BasicWebAttack attack, Client.ClientCallback clientCallback) throws RemoteException {
		if(clientCallback != null) {
			Set<Client.ClientCallback> s;
			if((s = url_subscribers.get(attack.url))==null) {
				s = Collections.newSetFromMap(new ConcurrentHashMap<Client.ClientCallback, Boolean>());
				url_subscribers.put(attack.url, s);
			}
			s.add(clientCallback);
		}
        System.out.println("Received task: ");
		System.out.println(attack);
        queue.add(attack);
	}

	@Override
    public Attacks.BasicWebAttack getTask() throws RemoteException{
        try {
            return(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public void updateSuccessfulAttack(BasicWebAttack attack) throws RemoteException{
		successfulAttacks.add(attack);
		System.out.println("Successful attack");
		System.out.println(attack);
        System.out.println(attack.paramsBatch);
        Set<Client.ClientCallback> clients = url_subscribers.get(attack.url);
        for (Client.ClientCallback c : clients) {
        	c.update(attack);
        }
	}

}
