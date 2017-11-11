package util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import util.Attacks;
import util.UrlAttacker;

import java.util.ArrayList;
import java.util.List;

public class UrlAttackerMain {
    public static void main(String[] args) {
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

        Attacks.BruteforceAttack bfAttack = new Attacks.BruteforceAttack("http://localhost:5000/login",paramsBatch, "Internal Server Error");
        List<List<NameValuePair>> attackRes = UrlAttacker.performAttack(bfAttack);
        if(attackRes != null)
            System.out.println(attackRes.toString());
        else
            System.out.println("Something went wrong :(");


    }
}
