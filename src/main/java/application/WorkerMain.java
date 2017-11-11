package application;

import messagequeue.RMIMessageQueue;

import java.rmi.Naming;
import java.util.Random;


public class WorkerMain {
	public static void main(String[] args) {
		String[] names = {"John", "Tim", "Smith", "Ron", "A", "B", "C", "D", "E", "F"};
		Random random = new Random();

		int i = random.nextInt(names.length);

		String reg_host = "localhost";
		int reg_port = 1099;

		if (args.length == 1) {
			reg_port = Integer.parseInt(args[0]);
		} else if (args.length == 2) {
			reg_host = args[0];
			reg_port = Integer.parseInt(args[1]);
		}
		try {
			Worker w = new WorkerImpl(names[i]);
			RMIMessageQueue queue = (RMIMessageQueue) Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/MessageQueue");
			while(true){
				w.handleTask(queue.getTask());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
