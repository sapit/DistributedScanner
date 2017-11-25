package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import server.Producer;

public class ClientMain {
	private static int port = 1099;
	private static String reg_host = "localhost";
	private static Producer producer = null;
	private static ClientCallback callback = null;

	static void knownBruteforceAttack(){
		String url = "https://gentle-depths-34500.herokuapp.com/";

		String successIdentifier = "John";
		String button = "SubmitButton3";
		List<NameValuePair> paramsRegex = new ArrayList<>();
		paramsRegex.add(new BasicNameValuePair("username","Matt|Joe|John"));
		paramsRegex.add(new BasicNameValuePair("password","123|Joe|qwerty"));
		System.out.println(paramsRegex);
		try {
			producer.BruteforceAttack(url, paramsRegex, button, successIdentifier, callback, 10);

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

		knownBruteforceAttack();
	}
}
