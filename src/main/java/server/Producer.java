package server;

import com.mifmif.common.regex.Generex;
import com.mifmif.common.regex.util.Iterator;
import messagequeue.RMIMessageQueue;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import util.Attacks;

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
    public static List<List<NameValuePair>> createCombinations(List<NameValuePair> parametersRegEx){ //receive list of key:regex
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
    public static void generatePermutations(List<List<NameValuePair>> parameterPermutations, int depth, List<List<NameValuePair>> result, List<NameValuePair> current){
        //finished processing permutations
        if(depth == parameterPermutations.size()){
            List<NameValuePair> l = new ArrayList<>(current);
            result.add(l);
            return;
        }
        for(int i=0; i<parameterPermutations.get(depth).size(); i++){
            current.add(parameterPermutations.get(depth).get(i));
            generatePermutations(parameterPermutations, depth+1, result, current);
            current.remove(current.size()-1);
        }
        return;
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
