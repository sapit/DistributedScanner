package server;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import messagequeue.RMIMessageQueue;
import messagequeue.RMIMessageQueueImpl;
import util.Attacks;

public class ProducerMain {
	static int reg_port = 1099;
	static String reg_host = "localhost";
	private static RMIMessageQueue queue;
	
	public ProducerMain() {
	    boolean registryRunning = false;
	    while(!registryRunning){
            try {
                Producer producer = new ProducerImpl(queue);
                Naming.rebind("rmi://localhost:" + reg_port + "/Producer", producer);
                registryRunning = true;
            } catch (Exception e) {
                try {
                    java.rmi.registry.LocateRegistry.createRegistry(reg_port);
                    System.out.println("RMI registry ready.");
                } catch (Exception ex) {
                    e.printStackTrace();
                    System.out.println("Exception starting RMI registry:");
                    ex.printStackTrace();
                    System.exit(1);
                }
            }
        }
	}

	public static void main(String[] args) {
        String port = System.getProperty("port");
        reg_port = port != null ? Integer.parseInt(port) : reg_port;
        String host = System.getProperty("host");
        reg_host = host != null ? host : reg_host;

		try {
			queue = (RMIMessageQueue) Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/MessageQueue");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		new ProducerMain();
	}
}
