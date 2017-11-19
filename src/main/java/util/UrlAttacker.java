package util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import util.Attacks;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UrlAttacker {

    /*
        Possible workflow:

        1. Attack with credentials, see what a successful result looks like
        2. Attack with the all the sets of parameters in the batch and look for the same output
        ^ this might be best for SQL injections

        Other possibility:

        1. Ask the user for a string which indicates success - e.g. 'You have successfully logged in'
        2. Attack with the given sets of parameters and look for the expected string
        ^ this is probably good for bruteforce

        PHP injection:
        1. Have some code snippets that can be injected
        2. Check against all of them

     */

    private static HttpPost constructRequest(String url, List<NameValuePair> params){

        HttpPost httppost = new HttpPost(url);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httppost;
    }

    private static String getResponse(HttpClient httpclient, HttpPost httppost) {

        //Execute and get the response.
        HttpResponse response = null;

        if(httpclient == null || httppost == null) { return null; }

        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream instream = null;
            try {
                instream = entity.getContent();
                Scanner s = new Scanner(instream).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";

                return result;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    public static List<List<NameValuePair>> performAttack(Attacks.BasicWebAttack attack){
        if(attack instanceof Attacks.BruteforceAttack){
            return bruteforceAttack((Attacks.BruteforceAttack) attack);
        }
        else if(attack instanceof Attacks.XSSAttack){
            return XSSAttack((Attacks.XSSAttack) attack);
        }
        else if(attack instanceof Attacks.SQLAttack){
            return SQLinjectionAttack((Attacks.SQLAttack) attack);
        }
        return null;
    }

    private static List<List<NameValuePair>> bruteforceAttack(Attacks.BruteforceAttack attack){
        if(attack==null || attack.url == null) { return null; }

        List<List<NameValuePair>> successfulParamSets = new ArrayList<>();

        HttpClient httpclient = HttpClients.createDefault();

        for (List<NameValuePair> paramsSet : attack.paramsBatch) {
            HttpPost httppost = constructRequest(attack.url, paramsSet);
            String response = getResponse(httpclient, httppost);

            if(response != null && response.contains(attack.successIdentifier)){
                System.out.println("SUCCESS!!!!");
                //System.out.println(response);
                successfulParamSets.add(paramsSet);
            }
        }
        return successfulParamSets;
    }
        /*
         SQL With the following naive approach. I check the input against a control input which is blank.
         That follows the rationale that if an empty String is given as username and password there should be
         no table return (still not clever for all cases). Better aproach could be 1. To have a valid account
         in the server to attack and check against that input or 2. Use a table of cases to find the error message
         againt bad login and test against that message. The test is the classic SQL query.
        */
        private static List<List<NameValuePair>> SQLinjectionAttack(Attacks.SQLAttack attack){
            if(attack==null || attack.url == null) { return null; }

            List<List<NameValuePair>> successfulParamSets = new ArrayList<>();

            HttpClient httpclient = HttpClients.createDefault();

            List<NameValuePair> base_case = attack.base_case;
            HttpPost httppost_base_case = constructRequest(attack.url, base_case);
            String response_base_case = getResponse(httpclient, httppost_base_case);
            
            for (List<NameValuePair> paramsSet : attack.paramsBatch) {
                HttpPost httppost = constructRequest(attack.url, paramsSet);
                String response = getResponse(httpclient, httppost);
                if(response != null && response_base_case != null && response.compareTo(response_base_case)!=0){
                    System.out.println("Success on: SQL injection");
                    successfulParamSets.add(paramsSet);
                }
            }
            return successfulParamSets;
        }
        
        /*
         XSS Attack. Send an XSS code. If returns back intact, effectivly meaning that the brower would run it, print Success
         and return the parametres.
        */
         private static List<List<NameValuePair>> XSSAttack(Attacks.XSSAttack attack){
            if(attack==null || attack.url == null) { return null; }

            List<List<NameValuePair>> successfulParamSets = new ArrayList<>();
 
            HttpClient httpclient = HttpClients.createDefault();

            for (List<NameValuePair> paramsSet : attack.paramsBatch) {
                HttpPost httppost = constructRequest(attack.url, paramsSet);
                String response = getResponse(httpclient, httppost);
                if(response != null){
                	for(int i=0; i<paramsSet.size();i++) {
//                	for(NameValuePair p : paramsSet) {
                		NameValuePair p = paramsSet.get(i);
                		if(response.contains(p.getValue())){
                			System.out.println("Success on: XSS attack");
                			successfulParamSets.add(paramsSet);
                			break;
                		}
                	}
                	
                }
            }
            return successfulParamSets;
        }
}
