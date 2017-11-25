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
	static int reg_port = 1099;
	static String reg_host = "localhost";
//	static String reg_host = "130.209.246.233";
	private static RMIMessageQueue queue;
	
	public ProducerMain() {
		try {
            java.rmi.registry.LocateRegistry.createRegistry(reg_port);
            System.out.println("RMI registry ready.");
        } catch (Exception e) {
            System.out.println("Exception starting RMI registry:");
            e.printStackTrace();
        }
		
		try {
			Producer producer = new ProducerImpl(queue);
			Naming.rebind("rmi://localhost:" + reg_port + "/Producer", producer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static Attacks.SQLAttack localVulnAppSQL(){
//        List<String> paramNames = new ArrayList<>();
//        paramNames.add("username");
//        paramNames.add("password");
//        return ProducerImpl.createSQLAttackObject("http://localhost:8000", paramNames, "SubmitButton3", null);
//    }
//
//    public static Attacks.XSSAttack localVulnAppXSS(){
//        List<String> paramNames = new ArrayList<>();
//        paramNames.add("inputText1");
//        return ProducerImpl.createXSSAttackObject("http://localhost:8000", paramNames, "SubmitButton1", null);
//    }
//
//    public static Attacks.XSSAttack localVulnAppXSS2(){
//        List<String> paramNames = new ArrayList<>();
//        paramNames.add("inputText2");
//        return ProducerImpl.createXSSAttackObject("http://localhost:8000", paramNames, "SubmitButton2", null);
//    }
//
//    public static Attacks.BruteforceAttack localVulnAppBruteforce(){
//        String successIdentifier = "logged in";
//        String button = "SubmitButton3";
//        List<NameValuePair> paramsRegex = new ArrayList<>();
//        paramsRegex.add(new BasicNameValuePair("username","Matt|Joe|Chris"));
//        paramsRegex.add(new BasicNameValuePair("password","123|Joe|Chris"));
//        return ProducerImpl.createBruteforceAttackObject("http://localhost:8000",paramsRegex, button,successIdentifier);
//    }

	public static void main(String[] args) {

		try {
			queue = (RMIMessageQueue) Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/MessageQueue");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		new ProducerMain();
	}
}
