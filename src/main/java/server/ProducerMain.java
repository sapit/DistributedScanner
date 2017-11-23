package server;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import messagequeue.RMIMessageQueue;
import messagequeue.RMIMessageQueueImpl;
import util.Attacks;

public class ProducerMain {
	static int port = 1099;
	private static RMIMessageQueue queue;
	
	public ProducerMain() {
		try {
			Producer producer = new ProducerImpl();
			Naming.rebind("rmi://localhost:" + port + "/Producer", producer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Attacks.SQLAttack localVulnAppSQL(){
        List<String> paramNames = new ArrayList<>();
        paramNames.add("username");
        paramNames.add("password");
        return ProducerImpl.createSQLAttackObject("http://localhost:8000", paramNames, "SubmitButton3", null);
    }

    public static Attacks.XSSAttack localVulnAppXSS(){
        List<String> paramNames = new ArrayList<>();
        paramNames.add("inputText1");
        return ProducerImpl.createXSSAttackObject("http://localhost:8000", paramNames, "SubmitButton1", null);
    }

    public static Attacks.XSSAttack localVulnAppXSS2(){
        List<String> paramNames = new ArrayList<>();
        paramNames.add("inputText2");
        return ProducerImpl.createXSSAttackObject("http://localhost:8000", paramNames, "SubmitButton2", null);
    }

    public static Attacks.BruteforceAttack localVulnAppBruteforce(){
        String successIdentifier = "logged in";
        String button = "SubmitButton3";
        List<NameValuePair> paramsRegex = new ArrayList<>();
        paramsRegex.add(new BasicNameValuePair("username","Matt|Joe|Chris"));
        paramsRegex.add(new BasicNameValuePair("password","123|Joe|Chris"));

        return ProducerImpl.createBruteforceAttackObject("http://localhost:8000",paramsRegex, button,successIdentifier);
    }

	public static void main(String[] args) {
		String reg_host = "localhost";
		int reg_port = 1099;


//		if (args.length == 1)
//			port = Integer.parseInt(args[0]);

		
		int count=0;
		try {
			RMIMessageQueue queue = (RMIMessageQueue) Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/MessageQueue");
//			while(true) {
//				System.out.println("Sending task " + count);
//				count++;
//
//                queue.createTask(localVulnAppBruteforce(), null);
//                queue.createTask(localVulnAppSQL(), null);
//                queue.createTask(localVulnAppXSS(), null);
//                queue.createTask(localVulnAppXSS2(), null);
//
//				Thread.sleep(100);
//			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		new ProducerMain();
	}
}
