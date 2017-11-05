import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UrlAttacker {

    private String url;

    public UrlAttacker(String url){
        this.url = url;
    }

    public Attack attack(List<NameValuePair> params){
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //Execute and get the response.
        HttpResponse response = null;
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

                System.out.println(result);

                if(result.toLowerCase().contains("Internal Server Error".toLowerCase())){
                    return Attack.SUCCESS_KEY_VALUE_CHECK;
                }
                else{
                    return Attack.FAIL_KEY_VALUE_CHECK;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                // do something useful
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

    public static void main(String[] args) {
        //Client should run similar code
        UrlAttacker urlAttacker = new UrlAttacker("http://localhost:5000/login");
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("param-1", "12345"));
        params.add(new BasicNameValuePair("field", "fuzzed"));
        Attack attackRes = urlAttacker.attack(params);
        System.out.println(attackRes.toString());

        List<NameValuePair> params2 = new ArrayList<NameValuePair>(2);
        params2.add(new BasicNameValuePair("username", "username"));
        params2.add(new BasicNameValuePair("password", "passwor"));//missing 'd'
        Attack attackRes2 = urlAttacker.attack(params2);
        System.out.println(attackRes2.toString());

        List<NameValuePair> params3 = new ArrayList<NameValuePair>(3);
        params3.add(new BasicNameValuePair("username", "username"));
        params3.add(new BasicNameValuePair("password", "password"));
        params3.add(new BasicNameValuePair("age", "22a")); //not only numbers
        Attack attackRes3 = urlAttacker.attack(params3);
        System.out.println(attackRes3.toString());

    }
}
