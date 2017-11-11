package messagequeue;

import java.rmi.Naming; //Import naming classes to bind to rmiregistry


public class MessageQueueMain {
	static int port = 1099;
	
	public MessageQueueMain() {
		try {
			RMIMessageQueue queue = new RMIMessageQueueImpl();
			Naming.rebind("rmi://localhost:" + port + "/MessageQueue", queue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		if (args.length == 1)
			port = Integer.parseInt(args[0]);

		new MessageQueueMain();
	}
}
