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
}
