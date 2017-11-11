package messagequeue;

import application.Worker;
import util.Attacks;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIMessageQueueImpl extends java.rmi.server.UnicastRemoteObject implements RMIMessageQueue {
	BlockingQueue<Attacks.BasicWebAttack> queue;
	
	public RMIMessageQueueImpl()  throws RemoteException {
		super();
        queue = new LinkedBlockingQueue<>();
	}

	@Override
	public void createTask(Attacks.BasicWebAttack attack) throws RemoteException {
        System.out.println("Received task: ");
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

}
