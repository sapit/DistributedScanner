package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import messagequeue.RMIMessageQueue;
import server.Producer;

public class ClientMain {
	static int port = 1099;
	static String reg_host = "localhost";
	static Producer producer = null;
	static ClientCallback callback = null;

	static void knownAttack(){
		String url = "https://gentle-depths-34500.herokuapp.com/";

		String successIdentifier = "John";
		String button = "SubmitButton3";
		List<NameValuePair> paramsRegex = new ArrayList<>();
		paramsRegex.add(new BasicNameValuePair("username","Matt|Joe|John"));
		paramsRegex.add(new BasicNameValuePair("password","123|Joe|qwerty"));

		try {
			producer.BruteforceAttack(url, paramsRegex, button, successIdentifier, callback);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		try {
			callback = new ClientCallbackImpl();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		try {
			producer = (Producer) Naming.lookup("rmi://" + reg_host + ":" + port + "/Producer");
		} catch(Exception e) {
			e.printStackTrace();
		}

		knownAttack();
	}
}
