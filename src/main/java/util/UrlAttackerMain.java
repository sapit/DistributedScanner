package util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import util.Attacks;
import util.UrlAttacker;

import java.util.ArrayList;
import java.util.List;

public class UrlAttackerMain {
    public static void main(String[] args) {
        List<List<NameValuePair>> attackRes;

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

        Attacks.BruteforceAttack bfAttack = new Attacks.BruteforceAttack("http://localhost:8000",paramsBatch, "Internal Server Error");
        attackRes = UrlAttacker.performAttack(bfAttack);
        if(attackRes != null)
            System.out.println(attackRes.toString());
        else
            System.out.println("Something went wrong :(");
        attackRes.clear();

        /////////////////////// XSS Attack ///////////////////////
        //param4 is protected
        List<NameValuePair> params4 = new ArrayList<NameValuePair>(2);
        params4.add(new BasicNameValuePair("inputText1", "<script> alert(123) </script>"));
        params4.add(new BasicNameValuePair("SubmitButton1", ""));
        //param5 is not protected. Should qual to success
        List<NameValuePair> params5 = new ArrayList<NameValuePair>(2);
        params5.add(new BasicNameValuePair("inputText2", "<script> alert(123) </script>"));
        params5.add(new BasicNameValuePair("SubmitButton2", ""));

        List<List<NameValuePair>> paramsBatchXSS = new ArrayList<>();
        paramsBatchXSS.add(params4);
        paramsBatchXSS.add(params5);
        
        Attacks.XSSAttack xssAttack = new Attacks.XSSAttack("http://localhost:8000",paramsBatchXSS);
        attackRes = UrlAttacker.performAttack(xssAttack);
        
        if(attackRes != null)
            System.out.println(attackRes.toString());
        else
            System.out.println("Something went wrong :(");

         attackRes.clear();
         
        /////////////////////// SQL injection Attack /////////////////////// 
        List<NameValuePair> paramsSQL = new ArrayList<NameValuePair>(3);
        paramsSQL.add(new BasicNameValuePair("username", "' or ''='"));
        paramsSQL.add(new BasicNameValuePair("password", "' or ''='"));
        paramsSQL.add(new BasicNameValuePair("SubmitButton3", ""));
        
        List<List<NameValuePair>> paramsBatchSQL = new ArrayList<>();
        paramsBatchSQL.add(paramsSQL);

        List<NameValuePair> base_case = new ArrayList<NameValuePair>(3);
        base_case.add(new BasicNameValuePair("username", ""));
        base_case.add(new BasicNameValuePair("password", ""));
        base_case.add(new BasicNameValuePair("SubmitButton3", ""));

        Attacks.SQLAttack sqlAttack = new Attacks.SQLAttack("http://localhost:8000/index.php",paramsBatchSQL, base_case);
        attackRes = UrlAttacker.performAttack(sqlAttack);
        
        if(attackRes != null)
            System.out.println(attackRes.toString());
        else
            System.out.println("Something went wrong :(");
    }
}
