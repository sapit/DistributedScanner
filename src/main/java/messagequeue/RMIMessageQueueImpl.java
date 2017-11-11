package messagequeue;

import application.Worker;
import util.Attacks;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RMIMessageQueueImpl extends java.rmi.server.UnicastRemoteObject implements RMIMessageQueue {

	BlockingQueue<String> queueStrings;

	BlockingQueue<Attacks.BasicWebAttack> queue;

	Queue<Worker> workers;
	
	public RMIMessageQueueImpl()  throws RemoteException {
		super();
		queueStrings = new LinkedBlockingQueue<>();
        queue = new LinkedBlockingQueue<>();
		workers = new LinkedList<>();
	}
	
	@Override
	public void createTask(String task) throws RemoteException {
		System.out.println("Received task: "+task);
		queueStrings.add(task);

//		Worker w = workers.remove();
//        try {
//            w.handleTask(queueStrings.take());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        workers.add(w);
	}

	@Override
	public void createTask(Attacks.BasicWebAttack attack) throws RemoteException {
//        System.out.println("Received task: "+task);
        queue.add(attack);
	}


	public void registerWorker(Worker w) throws RemoteException {
		workers.add(w);
		System.out.println("Added new worker");
	}

	@Override
	public String getTaskString() throws RemoteException {
        try {
            return(queueStrings.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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
