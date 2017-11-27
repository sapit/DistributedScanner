package messagequeue;

import application.Worker;
import client.ClientCallback;
import util.Attacks;
import util.Attacks.BasicWebAttack;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIMessageQueueImpl extends java.rmi.server.UnicastRemoteObject implements RMIMessageQueue {
	private BlockingQueue<Attacks.BasicWebAttack> queue;
	private BlockingQueue<Attacks.BasicWebAttack> successfulAttacks;
	private ConcurrentHashMap<String, Set<client.ClientCallback>> url_subscribers;
	
	public RMIMessageQueueImpl()  throws RemoteException {
		super();
        queue = new LinkedBlockingQueue<>();
        successfulAttacks = new LinkedBlockingQueue<>();
        url_subscribers = new ConcurrentHashMap<>();
	}
	
	@Override
	public void createTask(Attacks.BasicWebAttack attack, client.ClientCallback clientCallback) throws RemoteException {
		if(clientCallback != null) {
			Set<client.ClientCallback> s;
			if((s = url_subscribers.get(attack.url))==null) {
				s = Collections.newSetFromMap(new ConcurrentHashMap<client.ClientCallback, Boolean>());
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
        Set<client.ClientCallback> clients = url_subscribers.get(attack.url);
        
        Iterator<client.ClientCallback> it = clients.iterator();
        while(it.hasNext()) {
        	client.ClientCallback c = it.next();
        	try {
        		c.update(attack);        		
        	}
        	catch(Exception ex) {
        		it.remove();
        	}
        }
	}

}
