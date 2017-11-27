package application;

import messagequeue.RMIMessageQueue;
import util.Attacks;

import java.rmi.Naming;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;


public class WorkerMain {
	private static String reg_host = "localhost";
	private static int reg_port = 1099;
	private static RMIMessageQueue queue;

	public static void main(String[] args) {
		String port = System.getProperty("port");
		reg_port = port != null ? Integer.parseInt(port) : reg_port;
		String host = System.getProperty("host");
		reg_host = host != null ? host : reg_host;

		String[] names = {"John", "Tim", "Smith", "Ron", "Joshua", "Kevin", "Chris", "Michael", "Andrew", "Boris"};
		Random random = new Random();

		int i = random.nextInt(names.length);
		try {
			Worker w = new Worker(names[i]);
			queue = (RMIMessageQueue) Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/MessageQueue");
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
