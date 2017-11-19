package util;


import java.util.ArrayList;
import java.util.List;


import com.mifmif.common.regex.Generex;
import com.mifmif.common.regex.util.Iterator;

public class StringGenerator {
	private Generex generex;
	private int numOfStrings;
	private String regex;
	private int stringsGenerated;

	public StringGenerator(int numOfStrings, String regex){
		this.numOfStrings = numOfStrings;
		this.regex = regex;
		this.stringsGenerated = 0;
		this.generex = new Generex(regex); 
	}
	
	public StringGenerator(String regex){
		this.numOfStrings = 1000000;
		this.regex = regex;
		this.stringsGenerated = 0;
		this.generex = new Generex(regex);
	}
	
	
	public List<String> createStrings(){
		List<String> listOfStrings = new ArrayList<String>();
		Iterator iterator = generex.iterator();
		while(stringsGenerated<numOfStrings && iterator.hasNext()) {
			listOfStrings.add(iterator.next());
			stringsGenerated++;
		}
		return listOfStrings;	
		
		
		
	}
	

}
