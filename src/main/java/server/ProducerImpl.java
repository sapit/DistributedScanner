package server;

import client.ClientCallback;
import com.mifmif.common.regex.Generex;
import com.mifmif.common.regex.util.Iterator;
import messagequeue.RMIMessageQueue;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import util.Attacks;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


public class ProducerImpl extends java.rmi.server.UnicastRemoteObject implements Producer {
	private RMIMessageQueue queue;
	String reg_host = "localhost";
//	String reg_host = "130.209.246.233";
	int reg_port = 1099;
	
	protected ProducerImpl() throws RemoteException {
		super();
		try {
			queue = (RMIMessageQueue) Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/MessageQueue");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Started producer: ");
	}
	
	protected ProducerImpl(RMIMessageQueue queue) throws RemoteException {
		super();
		this.queue = queue;
		
		System.out.println("Started producer: ");
	}

	private static List<List<NameValuePair>> generatePermutationsFromRegexes(List<NameValuePair> parametersRegEx){ //receive list of key:regex
    	List<List<NameValuePair>> parameterPermutations = new ArrayList<>();

        for(int i=0 ; i < parametersRegEx.size(); i++){
            List<NameValuePair> permutations = new ArrayList<>();
            Generex generex = new Generex(parametersRegEx.get(i).getValue());
            Iterator iter = generex.iterator();
            while(iter.hasNext()){
                permutations.add(new BasicNameValuePair(parametersRegEx.get(i).getName(), iter.next()));
            }
            parameterPermutations.add(permutations);
        }
        return parameterPermutations;
    }

    //below is the recursive method that creates all possible permutations from N lists of strings
    private static void getCartesianProduct(List<List<NameValuePair>> parameterPermutations, int depth, List<List<NameValuePair>> result, List<NameValuePair> current){
        //finished processing permutations
        if(depth == parameterPermutations.size()){
            List<NameValuePair> l = new ArrayList<>(current);
            result.add(l);
            return;
        }
        for(int i=0; i<parameterPermutations.get(depth).size(); i++){
            current.add(parameterPermutations.get(depth).get(i));
            getCartesianProduct(parameterPermutations, depth+1, result, current);
            current.remove(current.size()-1);
        }
    }
    
    private static List<List<NameValuePair>> getPairsFromExpressions(List<String> paramNames, String button, String[] knownExpressions) {
    	List<NameValuePair> params = new ArrayList<>();
    	List<List<NameValuePair>> paramsBatch = new ArrayList<>();
    	for(String s : knownExpressions) {
    		for(String paramName : paramNames){
                params.add(new BasicNameValuePair(paramName, s));
            }
    		params.add(new BasicNameValuePair(button, ""));
    		paramsBatch.add(params);
    		params = new ArrayList<>();
    	}
    	
    	return paramsBatch;
    }

    private static List<Attacks.SQLAttack> createSQLAttackObject(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams, int batchSize){
    	List<NameValuePair> base_case = new ArrayList<>();
    	List<List<NameValuePair>> paramsListSQL;
    	
    	//prepare the base case
        for(String paramName : paramNames) {
            base_case.add(new BasicNameValuePair(paramName, ""));
        }

        if(button!=null)
    	    base_case.add(new BasicNameValuePair(button, ""));
    	
    	// implement the attacks for all the known vulnerable expressions
    	paramsListSQL = getPairsFromExpressions(paramNames, button, Attacks.SQLAttack.knownExpressions);
    	
    	//add the user defined attackParams
    	if(attackParams != null)
    		paramsListSQL.addAll(attackParams);

        List<Attacks.SQLAttack> attacks = new ArrayList<>();
        List<List<NameValuePair>> batch;
        for(int i=0; i < paramsListSQL.size(); i+=batchSize){
            batch = paramsListSQL.subList(i, i+batchSize > paramsListSQL.size() ? paramsListSQL.size() : i+batchSize);
            attacks.add(new Attacks.SQLAttack(url, batch, base_case));
        }
        return attacks;
    }
    
    private static List<Attacks.XSSAttack> createXSSAttackObject(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams, int batchSize){
        List<List<NameValuePair>> paramsListXSS;
    	
    	// implement the attacks for all the known vulnerable expressions
    	paramsListXSS = getPairsFromExpressions(paramNames, button, Attacks.XSSAttack.knownExpressions);
    	
    	//add the user defined attackParams
    	if(attackParams != null)
    		paramsListXSS.addAll(attackParams);

        List<Attacks.XSSAttack> attacks = new ArrayList<>();
        List<List<NameValuePair>> batch;

        for(int i=0; i < paramsListXSS.size(); i+=batchSize){
            batch = paramsListXSS.subList(i, i+batchSize > paramsListXSS.size() ? paramsListXSS.size() : i+batchSize);
            attacks.add(new Attacks.XSSAttack(url, batch));
        }
        return attacks;
    }

    private static List<Attacks.BruteforceAttack> createBruteforceAttackObject(String url, List<NameValuePair> paramsRegex, String button, String successIdentifier, int batchSize){
        List<List<NameValuePair>> paramsPossibleValues = generatePermutationsFromRegexes(paramsRegex);
        List<NameValuePair> buttonInList = new ArrayList<>();

        buttonInList.add(new BasicNameValuePair(button, ""));
        paramsPossibleValues.add(buttonInList);

        List<List<NameValuePair>> paramsList = new ArrayList<>();
        getCartesianProduct(paramsPossibleValues, 0, paramsList, new ArrayList<>());

        List<Attacks.BruteforceAttack> attacks = new ArrayList<>();

        List<List<NameValuePair>> batch;
        for(int i=0; i < paramsList.size(); i+=batchSize){
            batch = new ArrayList<>(paramsList.subList(i, i+batchSize > paramsList.size() ? paramsList.size() : i+batchSize)); //sublist not serialisable, construct new list
            attacks.add(new Attacks.BruteforceAttack(url, batch, successIdentifier));
        }
        return attacks;
    }
    

	@Override
	public void BruteforceAttack(String url, List<NameValuePair> paramsRegex, String button, String successIdentifier, ClientCallback callback, int batchSize)
			throws RemoteException {
        List<Attacks.BruteforceAttack> attacks = createBruteforceAttackObject(url, paramsRegex, button, successIdentifier, batchSize);
        System.out.println(attacks);
        for(Attacks.BruteforceAttack a : attacks){
            queue.createTask(a, callback);
        }
	}

	@Override
	public void XSSAttack(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams, ClientCallback callback, int batchSize)
			throws RemoteException {
        List<Attacks.XSSAttack> attacks = createXSSAttackObject(url, paramNames, button, attackParams, batchSize);
        System.out.println(attacks);
        for(Attacks.XSSAttack a : attacks){
            queue.createTask(a, callback);
        }
	}

	@Override
	public void SQLAttack(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams, ClientCallback callback, int batchSize)
			throws RemoteException {
        List<Attacks.SQLAttack> attacks = createSQLAttackObject(url, paramNames, button, attackParams, batchSize);
        System.out.println(attacks);
        for(Attacks.SQLAttack a : attacks){
            queue.createTask(a, callback);
        }
	}
}