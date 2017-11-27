package server;

import java.rmi.RemoteException;
import java.util.List;

import client.ClientCallback;
import org.apache.http.NameValuePair;

import util.Attacks;

public interface Producer extends java.rmi.Remote {

	void BruteforceAttack(String url, List<NameValuePair> paramsRegex, String button, String successIdentifier, ClientCallback callback, Integer batchSize) throws RemoteException;
	
	void XSSAttack(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams, ClientCallback callback, Integer batchSize) throws RemoteException;
	
	void SQLAttack(String url, List<String> paramNames, String button, List<List<NameValuePair>> attackParams, ClientCallback callback, Integer batchSize) throws RemoteException;

}