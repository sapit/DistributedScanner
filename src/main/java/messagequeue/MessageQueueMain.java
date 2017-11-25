package messagequeue;

import org.apache.http.NameValuePair;
import util.Attacks;

import java.rmi.Naming; //Import naming classes to bind to rmiregistry
import java.util.ArrayList;
import java.util.List;


public class MessageQueueMain {
	private static int port = 1099;

	public MessageQueueMain() {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
            System.out.println("RMI registry ready.");
        } catch (Exception e) {
            System.out.println("Exception starting RMI registry:");
            e.printStackTrace();
        }

		try {
			RMIMessageQueue queue = new RMIMessageQueueImpl();
			Naming.rebind("rmi://localhost:" + port + "/MessageQueue", queue);
        } catch (Exception e) {
			e.printStackTrace();
		}
	    System.out.println("Started message queue: ");
	}
	
	public static void main(String args[]) {
		if (args.length == 1)
			port = Integer.parseInt(args[0]);
		new MessageQueueMain();
	}
}
