package util;

import org.apache.http.NameValuePair;

import java.util.List;

public class Attack {

    public final AttackType type;
    public final String url;
    public final List<NameValuePair> credentialParams;        //credentials that give us a successful login
    public final List<List<NameValuePair>> paramsBatch;       //multiple sets of parameters to try while attacking(a batch)
    public final String successIdentifier;

    public Attack(AttackType type, String url, List<NameValuePair> credentialParams, List<List<NameValuePair>> paramsBatch, String success){
        this.type = type;
        this.url = url;
        this.credentialParams = credentialParams;
        this.paramsBatch = paramsBatch;
        this.successIdentifier = success;
    }

//    @Override
//    public String toString(){
//        return null;
//    }

}
