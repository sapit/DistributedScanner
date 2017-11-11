package messagequeue;

import java.rmi.Naming; //Import naming classes to bind to rmiregistry


public class MessageQueueServer {
	static int port = 1099;
	
	public MessageQueueServer() {
		try {
			RMIMessageQueue queue = new RMIMessageQueueImpl();
			Naming.rebind("rmi://localhost:" + port + "/MessageQueue", queue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		// Create the new Calculator server
		if (args.length == 1)
			port = Integer.parseInt(args[0]);

		new MessageQueueServer();
	}
}
