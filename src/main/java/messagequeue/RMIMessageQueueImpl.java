package messagequeue;

import application.Worker;
import util.Attacks;
import util.Attacks.BasicWebAttack;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIMessageQueueImpl extends java.rmi.server.UnicastRemoteObject implements RMIMessageQueue {
	BlockingQueue<Attacks.BasicWebAttack> queue;
	BlockingQueue<Attacks.BasicWebAttack> successfulAttacks;
	
	public RMIMessageQueueImpl()  throws RemoteException {
		super();
        queue = new LinkedBlockingQueue<>();
        successfulAttacks = new LinkedBlockingQueue<>();
	}

	@Override
	public void createTask(Attacks.BasicWebAttack attack) throws RemoteException {
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
	}

}
