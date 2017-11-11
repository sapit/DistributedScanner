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
    }

    public static class BruteforceAttack extends BasicWebAttack implements Serializable{
        public final String successIdentifier;
        public BruteforceAttack(String url, List<List<NameValuePair>> paramsBatch, String successIdentifier){
            super(url, paramsBatch);
            this.successIdentifier = successIdentifier;
        }
    }

    // EXAMPLE
    public static class SQLAttack extends  BasicWebAttack{
        List<NameValuePair> credentials;    // this could be used to compare a successful log in and a successful sql injection
        public SQLAttack(String url, List<List<NameValuePair>> paramsBatch, List<NameValuePair> credentials){
            super(url, paramsBatch);
            this.credentials = credentials;
        }
    }
}
