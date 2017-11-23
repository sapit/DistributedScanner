package Client;

import java.rmi.RemoteException;

import util.Attacks.BasicWebAttack;

public class ClientCallbackImpl implements ClientCallback {

	@Override
	public void update(BasicWebAttack attacks) throws RemoteException {
		System.out.println(attacks);
		
	}

}
