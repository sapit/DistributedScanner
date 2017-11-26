package messagequeue;

import java.rmi.Naming; //Import naming classes to bind to rmiregistry


public class MessageQueueMain {
	private static int reg_port = 1099;
    private static String reg_host = "localhost";

	public MessageQueueMain() {
		boolean registryRunning = false;
        while(!registryRunning){
			try {
				RMIMessageQueue queue = new RMIMessageQueueImpl();
				Naming.rebind("rmi://"+ reg_host +":" + reg_port + "/MessageQueue", queue);
				registryRunning = true;
			} catch (Exception e) {
				try {
                    java.rmi.registry.LocateRegistry.createRegistry(reg_port);
                    System.out.println("RMI registry ready.");
                } catch (Exception ex) {
                    e.printStackTrace();
                    System.out.println("Exception starting RMI registry:");
					e.printStackTrace();
					System.exit(1);
				}
			}

		}
	    System.out.println("Started message queue: ");
	}
	
	public static void main(String args[]) {
        String port = System.getProperty("port");
        reg_port = port != null ? Integer.parseInt(port) : reg_port;
        String host = System.getProperty("host");
        reg_host = host != null ? host : reg_host;

		new MessageQueueMain();
	}
}
