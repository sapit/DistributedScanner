package util;

import org.apache.http.NameValuePair;

import java.io.Serializable;
import java.util.List;

public class Attacks {

    public static abstract class BasicWebAttack implements Serializable{
        public final String url;
        public final List<List<NameValuePair>> paramsBatch;       //multiple sets of parameters to try while attacking(a batch)
        public final Long timestamp;
        public BasicWebAttack(String url, List<List<NameValuePair>> paramsBatch){
            this.url = url;
            this.paramsBatch = paramsBatch;
            this.timestamp = System.currentTimeMillis();
        }

        public BasicWebAttack(String url, List<List<NameValuePair>> paramsBatch, long timestamp){
            this.url = url;
            this.paramsBatch = paramsBatch;
            this.timestamp = timestamp;
        }
        
        public abstract BasicWebAttack recreate(List<List<NameValuePair>> paramsBatch);

        @Override
        public String toString(){
            return "url: " + url + "\n"
                    + "Num of params: " + paramsBatch.size() + "\n"
                    + "timestamp: " + timestamp + "\n";
        }
    }

    public static class BruteforceAttack extends BasicWebAttack implements Serializable{
        public final String successIdentifier;
        public BruteforceAttack(String url, List<List<NameValuePair>> paramsBatch, String successIdentifier){
            super(url, paramsBatch);
            this.successIdentifier = successIdentifier;
        }

        private BruteforceAttack(String url, List<List<NameValuePair>> paramsBatch, String successIdentifier, long timestamp){
            super(url, paramsBatch, timestamp);
            this.successIdentifier = successIdentifier;
        }
        
        public BruteforceAttack recreate(List<List<NameValuePair>> paramsBatch){
            return new BruteforceAttack(this.url, paramsBatch, this.successIdentifier, this.timestamp);
        }
        
        @Override
        public String toString(){
            return "Bruteforce attack {\n"
                    + super.toString()
                    + "Success identifier: " + successIdentifier + "\n"
                    + "}\n";
        }

    }
    
    public static class SQLAttack extends  BasicWebAttack{
        final List<NameValuePair> base_case;    // known unsuccessful login to compare with other unsuccessful log ins and show successful sql injection
        public static final String[] knownExpressions = {"' or ''='"};
        
        public SQLAttack(String url, List<List<NameValuePair>> paramsBatch, List<NameValuePair> base_case){
            super(url, paramsBatch);
            this.base_case = base_case;
        }

        private SQLAttack(String url, List<List<NameValuePair>> paramsBatch, List<NameValuePair> base_case, long timestamp){
            super(url, paramsBatch, timestamp);
            this.base_case = base_case;
        }
        
        public SQLAttack recreate(List<List<NameValuePair>> paramsBatch){
            return new SQLAttack(this.url, paramsBatch, this.base_case, this.timestamp);
        }

        @Override
        public String toString(){
            return "SQL attack {\n"
                    + super.toString()
                    + "}\n";
        }

    }
    
    public static class XSSAttack extends  BasicWebAttack{
    	public static final String[] knownExpressions = {"<script> alert(123) </script>"};
    	
        public XSSAttack(String url, List<List<NameValuePair>> paramsBatch){
            super(url, paramsBatch);
        }

        private XSSAttack(String url, List<List<NameValuePair>> paramsBatch, long timestamp){
            super(url, paramsBatch, timestamp);
        }
        
        public XSSAttack recreate(List<List<NameValuePair>> paramsBatch){
            return new XSSAttack(this.url, paramsBatch, this.timestamp);
        }

        @Override
        public String toString(){
            return "XSS attack {\n"
                    + super.toString()
                    + "}\n";
        }
    }
}

