package util;

import org.apache.http.NameValuePair;

import java.io.Serializable;
import java.util.List;

public class Attacks {

    public static abstract class BasicWebAttack implements Serializable{
        public final String url;
        public final List<List<NameValuePair>> paramsBatch;       //multiple sets of parameters to try while attacking(a batch)

        public BasicWebAttack(String url, List<List<NameValuePair>> paramsBatch){
            this.url = url;
            this.paramsBatch = paramsBatch;
        }
        
        public abstract BasicWebAttack recreate(List<List<NameValuePair>> paramsBatch);

        @Override
        public String toString(){
            return "url: " + url + "\n"
                    + "Num of params: " + paramsBatch.size() + "\n";
        }
    }
    

    public static class BruteforceAttack extends BasicWebAttack implements Serializable{
        public final String successIdentifier;
        public BruteforceAttack(String url, List<List<NameValuePair>> paramsBatch, String successIdentifier){
            super(url, paramsBatch);
            this.successIdentifier = successIdentifier;
        }
        
        public BruteforceAttack recreate(List<List<NameValuePair>> paramsBatch){
            return new BruteforceAttack(this.url, paramsBatch, this.successIdentifier);
        }
        
        @Override
        public String toString(){
            return super.toString()
                    + "Success identifier: " + successIdentifier + "\n";
        }

    }
    
    public static class SQLAttack extends  BasicWebAttack{
        final List<NameValuePair> base_case;    // known unsuccessful login to compare with other unsuccessful log ins and show successful sql injection
        public static final String[] knownExpressions = {"' or ''='"};
        
        public SQLAttack(String url, List<List<NameValuePair>> paramsBatch, List<NameValuePair> base_case){
            super(url, paramsBatch);
            this.base_case = base_case;
        }
        
        public SQLAttack recreate(List<List<NameValuePair>> paramsBatch){
            return new SQLAttack(this.url, paramsBatch, this.base_case);
        }
    }
    
    public static class XSSAttack extends  BasicWebAttack{
    	public static final String[] knownExpressions = {"<script> alert(123) </script>"};
    	
        public XSSAttack(String url, List<List<NameValuePair>> paramsBatch){
            super(url, paramsBatch);
        }
        
        public XSSAttack recreate(List<List<NameValuePair>> paramsBatch){
            return new XSSAttack(this.url, paramsBatch);
        }
        
    }
}

