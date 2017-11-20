package server;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class test {
	public static void main(String[] args) {
		List<NameValuePair> p = new ArrayList<>();
//        p.add(new BasicNameValuePair("username","[ab][da]"));
//        p.add(new BasicNameValuePair("password","[ab][cd]"));
//        p.add(new BasicNameValuePair("age","[12][13]"));
        p.add(new BasicNameValuePair("username","[123]"));
        p.add(new BasicNameValuePair("password","[123]"));
        p.add(new BasicNameValuePair("age","[123]"));

        List<List<NameValuePair>> parameterPerms = Producer.createCombinations(p);
        ArrayList<List<NameValuePair>> result = new ArrayList<>();
        Producer.generatePermutations(parameterPerms, 0, result ,new ArrayList<>());

        System.out.println(result);
    }
}