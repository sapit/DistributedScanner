package server;

import messagequeue.RMIMessageQueue;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import util.Attacks;
import util.StringGenerator;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;


public class Producer {

    public static Attacks.BasicWebAttack prepareAttackObject(){
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("param-1", "12345"));
        params.add(new BasicNameValuePair("field", "fuzzed"));

        List<NameValuePair> params2 = new ArrayList<NameValuePair>(2);
        params2.add(new BasicNameValuePair("username", "username"));
        params2.add(new BasicNameValuePair("password", "passwor"));//missing 'd'

        List<NameValuePair> params3 = new ArrayList<NameValuePair>(3);
        params3.add(new BasicNameValuePair("username", "username"));
        params3.add(new BasicNameValuePair("password", "password"));
        params3.add(new BasicNameValuePair("age", "22a")); //not only numbers

        List<List<NameValuePair>> paramsBatch = new ArrayList<>();
        paramsBatch.add(params);
        paramsBatch.add(params2);
        paramsBatch.add(params3);

        List<NameValuePair> credentials = new ArrayList<>(2);
        credentials.add(new BasicNameValuePair("username", "username"));
        credentials.add(new BasicNameValuePair("password", "password"));

        paramsBatch.add(credentials);
        paramsBatch = createCombinations(params);        
        Attacks.BruteforceAttack bfAttack = new Attacks.BruteforceAttack("http://localhost:5000/login",paramsBatch, "Internal Server Error");
        return bfAttack;
   }
    public static List<List<NameValuePair>> createCombinations(List<NameValuePair> parameters){ //receive list of key:regex
    	
    	List<List<String>> allLists = new ArrayList<List<String>>(); 
    	int i = 0;
   
    	NameValuePair currentPair;
 
    	List<String> parameterNames = new ArrayList<String>(); //list of all the keys
    	for(i = 0;i<parameters.size();i++) {
    			currentPair = parameters.get(i);
	    		String key = currentPair.getName();
	    		String regex = currentPair.getValue();
	    		parameterNames.add(key);
	    		StringGenerator keyGenerator = new StringGenerator(regex);
	    		List<String> keyStrings = new ArrayList<String>(keyGenerator.createStrings());
	    		allLists.add(keyStrings); //for each key add all possible strings to a list matching the regex of that key
    		
    	}
    	List<NameValuePair> toPass = new ArrayList<NameValuePair>(allLists.size());
    	List<List<NameValuePair>> listOfCombinations = new ArrayList<List<NameValuePair>>();
    	generatePermutations(allLists,  listOfCombinations, 0, toPass, parameterNames);
    	System.out.println(allLists.size());
    	return listOfCombinations;
    }
    //below is the recursive method that creates all possible permutations from N lists of strings
    public static void generatePermutations(List<List<String>> lists, List<List<NameValuePair>> result, int depth, List<NameValuePair> current, List<String> parameterNames) {
    	if(depth==lists.size()) {
    		result.add(current);
    		return;
    	}
    	for(int i = 0; i < lists.get(depth).size(); i++) {
    		current.add(new BasicNameValuePair(parameterNames.get(depth),lists.get(depth).get(i)));
    		generatePermutations(lists, result, depth + 1, current, parameterNames);
    	}
    }
    

	public static void main(String[] args) {
		String reg_host = "localhost";
		
		int reg_port = 1099;

		if (args.length == 1) {
			reg_port = Integer.parseInt(args[0]);
		} else if (args.length == 2) {
			reg_host = args[0];
			reg_port = Integer.parseInt(args[1]);
		}
		int count=0;
		try {
			RMIMessageQueue queue = (RMIMessageQueue) Naming.lookup("rmi://" + reg_host + ":" + reg_port + "/MessageQueue");
			while(true) {

				System.out.println("Sending task " + count);
				count++;
                queue.createTask(prepareAttackObject());
				Thread.sleep(100);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
