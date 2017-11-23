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

public class Client {
	static int port = 1099;
	static String reg_host = "localhost";
	
	public static void main(String[] args) {

//		if (args.length == 1)
//			port = Integer.parseInt(args[0]);

		try {
			ClientCallback callback = new ClientCallbackImpl();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Producer producer = null;
		try {
			producer = (Producer) Naming.lookup("rmi://" + reg_host + ":" + port + "/Producer");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		String url = "https://gentle-depths-34500.herokuapp.com/";
		
		String successIdentifier = "logged in";
        String button = "SubmitButton3";
        List<NameValuePair> paramsRegex = new ArrayList<>();
        paramsRegex.add(new BasicNameValuePair("username","Matt|Joe|John"));
        paramsRegex.add(new BasicNameValuePair("password","123|Joe|qwerty"));
//        List<NameValuePair> paramsRegex = null;
		try {
			producer.BruteforceAttack(url, paramsRegex, button, successIdentifier);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
