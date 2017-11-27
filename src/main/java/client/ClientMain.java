package client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import server.Producer;

public class ClientMain {
	private static int reg_port = 1099;
	private static String reg_host = "localhost";
	private static Producer producer = null;
	private static ClientCallback callback = null;
    private static String url = "https://gentle-depths-34500.herokuapp.com/";

	static void knownBruteforceAttack(){
		String successIdentifier = "logged in";
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

	static void knownSQLattack(){
        List<String> paramNames = new ArrayList<>();
        paramNames.add("username");
        paramNames.add("password");
        try {
            producer.SQLAttack(url,paramNames,"SubmitButton3",null,callback,10);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    static void knownUnsuccessfulXSSattack(){
        List<String> paramNames = new ArrayList<>();
        paramNames.add("inputText1");
        try {
            producer.XSSAttack(url, paramNames, "SubmitButton1", null, callback, 10);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    static void knownXSSattack(){
        List<String> paramNames = new ArrayList<>();
        paramNames.add("inputText2");
        try {
            producer.XSSAttack(url, paramNames, "SubmitButton2", null, callback, 10);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


	public static void main(String[] args) {
		String port = System.getProperty("port");
		reg_port = port != null ? Integer.parseInt(port) : reg_port;
		String host = System.getProperty("host");
		reg_host = host != null ? host : reg_host;

		try {
			callback = new ClientCallbackImpl();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		try {
			producer = (Producer) Naming.lookup("rmi://" + reg_host + ":" + reg_port  + "/Producer");
		} catch(Exception e) {
			e.printStackTrace();
		}

		knownBruteforceAttack();
		knownSQLattack();
		knownXSSattack();
        knownUnsuccessfulXSSattack();
    }
}
