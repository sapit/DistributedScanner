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
	RMIMessageQueue queue;
	protected ProducerImpl() throws RemoteException {
		super();
		String reg_host = "localhost";
		int reg_port = 1099;
		int count=0;
		try {
			queue = (RMIMessageQueue) Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/MessageQueue");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static List<List<NameValuePair>> generatePermutationsFromRegexes(List<NameValuePair> parametersRegEx){ //receive list of key:regex
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
    public static void getCartesianProduct(List<List<NameValuePair>> parameterPermutations, int depth, List<List<NameValuePair>> result, List<NameValuePair> current){
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
        return;
    }
    
    public static List<List<NameValuePair>> getPairsFromExpressions(List<String> paramNames, String button, String[] knownExpressions) {
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	List<List<NameValuePair>> paramsBatch = new ArrayList<>();
    	for(String s : knownExpressions) {
    		for(int i=0; i<paramNames.size(); i++) {
    			params.add(new BasicNameValuePair(paramNames.get(i), s));
    		}
    		params.add(new BasicNameValuePair(button, ""));
    		paramsBatch.add(params);
    		params = new ArrayList<NameValuePair>();
    	}
    	
    	return paramsBatch;
    }

    public static Attacks.SQLAttack createSQLAttackObject(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams){
    	
    	List<NameValuePair> base_case = new ArrayList<NameValuePair>();
    	List<NameValuePair> paramsSQL = new ArrayList<NameValuePair>();
    	List<List<NameValuePair>> paramsBatchSQL = new ArrayList<>();
    	
    	//prepare the base case
    	for(int i=0; i<paramNames.size(); i++) {
    		base_case.add(new BasicNameValuePair(paramNames.get(i), ""));
    	}
    	base_case.add(new BasicNameValuePair(button, ""));
    	
    	// implement the attacks for all the known vulnerable expressions
    	paramsBatchSQL = getPairsFromExpressions(paramNames, button, Attacks.SQLAttack.knownExpressions);
    	
    	//add the user defined attackParams
    	if(attackParams != null)
    		paramsBatchSQL.addAll(attackParams);
        
        Attacks.SQLAttack sqlAttack = new Attacks.SQLAttack(url,paramsBatchSQL, base_case); 
    	return sqlAttack;
    }
    
    public static Attacks.XSSAttack createXSSAttackObject(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams){
    	List<List<NameValuePair>> paramsBatchXSS = new ArrayList<>();
    	
    	// implement the attacks for all the known vulnerable expressions
    	paramsBatchXSS = getPairsFromExpressions(paramNames, button, Attacks.XSSAttack.knownExpressions);
    	
    	//add the user defined attackParams
    	if(attackParams != null)
    		paramsBatchXSS.addAll(attackParams);
        
    	Attacks.XSSAttack xssAttack = new Attacks.XSSAttack(url, paramsBatchXSS);
    	return xssAttack;
    }

    public static Attacks.BruteforceAttack createBruteforceAttackObject(String url, List<NameValuePair> paramsRegex, String button, String successIdentifier){
        List<List<NameValuePair>> paramsPossibleValues = generatePermutationsFromRegexes(paramsRegex);
        List<NameValuePair> buttonInList = new ArrayList<>();

        buttonInList.add(new BasicNameValuePair(button, ""));
        paramsPossibleValues.add(buttonInList);

        List<List<NameValuePair>> result = new ArrayList<>();
        getCartesianProduct(paramsPossibleValues, 0, result, new ArrayList<>());
        Attacks.BruteforceAttack bruteforceAttack = new Attacks.BruteforceAttack(url, result, successIdentifier);
        return bruteforceAttack;
    }
    

	@Override
	public void BruteforceAttack(String url, List<NameValuePair> paramsRegex, String button, String successIdentifier, ClientCallback callback)
			throws RemoteException {
		Attacks.BasicWebAttack attack = createBruteforceAttackObject(url, paramsRegex, button, successIdentifier);
		queue.createTask(attack, callback);
	}

	@Override
	public void XSSAttack(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams, ClientCallback callback)
			throws RemoteException {
		Attacks.BasicWebAttack attack = createXSSAttackObject(url, paramNames, button, attackParams);
        queue.createTask(attack, callback);
	}

	@Override
	public void SQLAttack(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams, ClientCallback callback)
			throws RemoteException {
		Attacks.BasicWebAttack attack = createSQLAttackObject(url, paramNames, button, attackParams);
        queue.createTask(attack, callback);
	}
}