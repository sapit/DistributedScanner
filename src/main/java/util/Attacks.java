package util;

import org.apache.http.NameValuePair;

import java.io.Serializable;
import java.util.List;

public class Attacks {

    public static class BasicWebAttack implements Serializable{
        public final String url;
        public final List<List<NameValuePair>> paramsBatch;       //multiple sets of parameters to try while attacking(a batch)

        public BasicWebAttack(String url, List<List<NameValuePair>> paramsBatch){
            this.url = url;
            this.paramsBatch = paramsBatch;
        }

        @Override
        public String toString(){
            return "url: " + url
                    + "Num of params: " + paramsBatch.size();
        }
    }

    public static class BruteforceAttack extends BasicWebAttack implements Serializable{
        public final String successIdentifier;
        public BruteforceAttack(String url, List<List<NameValuePair>> paramsBatch, String successIdentifier){
            super(url, paramsBatch);
            this.successIdentifier = successIdentifier;
        }
        @Override
        public String toString(){
            return super.toString()
                    + "Success identifier: " + successIdentifier;
        }

    }

    // EXAMPLE
    public static class SQLAttack extends  BasicWebAttack{
        List<NameValuePair> credentials;    // this could be used to compare a successful log in and a successful sql injection
        public SQLAttack(String url, List<List<NameValuePair>> paramsBatch){
            super(url, paramsBatch);
            
        }
    }
    
        public static class XSSAttack extends  BasicWebAttack{
 
        public XSSAttack(String url, List<List<NameValuePair>> paramsBatch){
            super(url, paramsBatch);

        }
        
    }
}
