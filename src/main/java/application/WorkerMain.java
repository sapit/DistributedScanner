package application;

import messagequeue.RMIMessageQueue;
import util.Attacks;

import java.rmi.Naming;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;


public class WorkerMain {
	public static void main(String[] args) {
		String[] names = {"John", "Tim", "Smith", "Ron", "Joshua", "Kevin", "Chris", "Michael", "Andrew", "Boris"};
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
				Attacks.BasicWebAttack attack = queue.getTask();
				List<List<NameValuePair>> successes = w.handleTask(attack);
				if(successes != null && successes.size() > 0) {
					queue.updateSuccessfulAttack(attack.recreate(successes));
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
