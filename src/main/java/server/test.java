package server;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class test {
	public static void main(String[] args) {
		List<NameValuePair> p = new ArrayList<>();
		p.add(new BasicNameValuePair("username"," [ab][da]"));
		p.add(new BasicNameValuePair("password"," [ab][cd]"));
		p.add(new BasicNameValuePair("age"," [12][13]"));
		List<List<NameValuePair>> attacks = Producer.createCombinations(p);
		System.out.println("start");
		System.out.println(attacks.size());
		for(List<NameValuePair> a:attacks) {
			System.out.println(a.size());
			for(int b=0;b<a.size();b++) {
				System.out.println(a.get(b).getName() + a.get(b).getValue());
			}
		}

	}
}