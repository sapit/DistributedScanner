package client;

import java.rmi.RemoteException;

import util.Attacks.BasicWebAttack;

public class ClientCallbackImpl extends java.rmi.server.UnicastRemoteObject implements ClientCallback {

	protected ClientCallbackImpl() throws RemoteException {
		super();
	}

	@Override
	public void update(BasicWebAttack attacks) throws RemoteException {
	    long rtt = System.currentTimeMillis() - attacks.timestamp;
		System.out.println(attacks);
		System.out.println("Round trip time: " + rtt);
	}

}
