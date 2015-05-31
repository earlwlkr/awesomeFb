package awesomefb.spamfilter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Huy
 *
 */
public class SpamFilter {
	
	String fileNameHamData = "datatraining/ham.data";
	String fileNameSpamData = "datatraining/spam.data";
	String labelSpam = "spam";
	String labelHam = "ham";
	
	// A HashMap to keep track of all words
	HashMap words;
	// How to split the String into  tokens
	String splitregex;
	// Regex to eliminate junk (although we really should welcome the junk)
	Pattern wordregex;
	
	public SpamFilter(){
		
	}
	
	// Return value boolean, is cmt a Spam?
	public boolean isSpam(String comment){
		try {
			return run(comment);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	// Run classification 
	boolean run(String comment) throws Exception{
		
		trainGoodData();
		trainBadData();
		
		return analyze(comment);
	}
	
	boolean analyze(String comment){
		return false;
	}
	
	// Train Ham Data
	void trainGoodData(){
		
	}
	
	// Train Spam Data
	void trainBadData() throws Exception{
//		FileReader file = null;
//		try {
//			file = new FileReader(fileNameHamData);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		try (BufferedReader reader = new BufferedReader(file)){
//			
//	    	String line = reader.readLine();
//	    	
//	    	while ((line = reader.readLine()) != null) {
//	    		String[] tokens = line.split(splitregex);
//	            int spamTotal = 0;//tokenizer.countTokens(); // How many words total
//
//	    		// For every word token
//	    		for (int i = 0; i < tokens.length; i++) {
//	                String word = tokens[i].toLowerCase();
//	    			Matcher m = wordregex.matcher(word);
//	    			if (m.matches()) {
//	    				spamTotal++;
//	    				// If it exists in the HashMap already
//	    				// Increment the count
//	    				// ...
//	    			}
//	    		}
//	    	}      
//	    }
		
	}
}
