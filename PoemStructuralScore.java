import java.io.*;
import java.util.*;
import java.util.regex.*;


//this class specifically deal with any thing related to format of the poem
public class PoemStructuralScore {
	public Map<Integer, ArrayList<Double>>poemIDToRhymeCharacteristics = new HashMap<Integer,ArrayList<Double>>();
	public double emotionalScore  = 0;
	//constructor, splits the poems and calls on a function to add all information
	//into the database
	public PoemStructuralScore(String format,int ind) {
		getFormatScore(format,ind);
	}
	
	public void getFormatScore(String format, int ind) {
		int lineCount = 0;
		BufferedReader reader = new BufferedReader(new StringReader(format));
		String line1 = null;
		String line2 = null;
		double structureScore = 0;
		PoemSentimentScore sentimentCalculator = new PoemSentimentScore(ind);

		try {
			reader.readLine();
			//read in a pair of poem sentences
			line1 = reader.readLine();
			line2 = reader.readLine();
			//if a pair if read
			while(line1 != null && line1.compareTo("")!= 0) {
				if(line1.compareTo("") != 0 && line2.compareTo("") != 0){
					lineCount ++;
					double tempstructureScore = TwoLineMatchScore(line1, line2);
					if (tempstructureScore < 0)
						tempstructureScore = 0;
					ArrayList<Double> positionWithScore = new ArrayList<Double>();
					positionWithScore.add((double)lineCount); //which pair
					positionWithScore.add(structureScore);
					poemIDToRhymeCharacteristics.put(ind, positionWithScore);	
					sentimentCalculator.lines.add(line1);
					sentimentCalculator.lines.add(line2);
//					System.out.println("line count: "+lineCount);
					structureScore += tempstructureScore;
//					System.out.println(structureScore);
				}
				else {//if one pair is empty, continue reading another line
					while( line1.compareTo("") == 0 && line2.compareTo("") == 0)
					{
						if (line1.compareTo("") == 0) {
							try {
								line1 = reader.readLine();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						else {
							try {
								line2 = reader.readLine();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				
				line1 = reader.readLine();
				if(line1 != null) {
					line2 = reader.readLine();
				}
			}
		} catch (IOException e) {
			System.err.println("Format not readable");
			e.printStackTrace();
		}
		
		System.out.println(structureScore/(((double)lineCount)/2));
		emotionalScore = sentimentCalculator.GetEmotionalScore();
		System.out.println(emotionalScore);
	}
	
	
	public double TwoLineMatchScore(String l1, String l2) {
		/*
		 * First, define stregth of matching
		 * 		same type matches same type = 1;
		 * 		following must be be matched, if not, there must be a -1 penalty
		 * 			verb, color, number, direction, action
//		 * 		the following are close matches, receive score 0.8
//		 * 			action match feeling
//		 * 			human match thing
//		 * 			sense match scene
//		 * 			scene thing
//		 * 			scene location
		 */
//		System.out.println("String1_original is: "+l1 );
//		System.out.println("String2_original is: "+l2 );

		String[] line1Cont = l1.split("\\|");
		String[] line2Cont = l2.split("\\|");
		HashMap<Integer, Integer> line1MatchLine2= new HashMap<Integer,Integer>();
		HashMap<Integer, Integer> line2MatchLine1= new HashMap<Integer,Integer>();
		/*
		 * algorithm for comparing sections
		 */
		double structureScore = 0f;
		
		
		//first, we look at pairings at their matching locations
		int compare = Math.min(line1Cont.length, line2Cont.length);
		
		for(int i = 0 ; i < compare; i++) {
			double score = sectionMeaningMatch(line1Cont[i], line2Cont[i]); //compare two sections
			if (score == 1f) {
				line1MatchLine2.put(i, i);
				line2MatchLine1.put(i, i);
				structureScore += score;
//				System.out.println(line1Cont[i]+" and " + line2Cont[i]);
//				System.out.println("components "+ i+ " and "+ i+ " matches perfectly");
			}
			else;
//				System.out.println("the "+i+"th component is not matched");
		}
			
		//then we look at matchings across different positions in line1
		for(int index = 0 ; index < line1Cont.length; index++) {
			if(line1MatchLine2.containsKey(index)); //if match is already established, do nothing
			else{//if the match is not found at corresponding positions
				for(int index2 = 0; index2 < line2Cont.length; index2++) {
					if(line2MatchLine1.containsKey(index2)); //if the component of line2 has already been matched
					else{
						double score = sectionMeaningMatch(line1Cont[index], line2Cont[index2]); //compare two sections
						if (score == 1f && index == index2) { //match perfectly <==should not have happened
							structureScore += score;
							line2MatchLine1.put(index, index);
							line1MatchLine2.put(index, index);
//							System.out.println("#: components "+ index + " and "+ index+ " matches perfectly");
						}
						else if(Math.abs(index2-index) >= 1){
							structureScore += score*0.6/Math.abs(index2-index) ;
							line2MatchLine1.put(index2, index);
							line1MatchLine2.put(index,index2);
//							System.out.println("components "+ index+ "(line1) and "+ index2+ "line(2) matches perfectly");
						}
					}
				}
			}
		}	
		
		
		//do this for line2
		for(int index = 0 ; index < line2Cont.length; index++) {
			if(line2MatchLine1.containsKey(index)); //if match is already established, do nothing
			else{//if the match is not found at corresponding positions
				for(int index2 = 0; index2 < line1Cont.length; index2++) {
					if(line1MatchLine2.containsKey(index2)); //if the component of line2 has already been matched
					else{
						double score = sectionMeaningMatch(line2Cont[index], line1Cont[index2]); //compare two sections
						if (score > 0f && index == index2) { //matches somehow 
							structureScore += score;
							line2MatchLine1.put(index, index);
							line1MatchLine2.put(index, index);
//							System.out.println("#: components "+ index + " and "+ index+ " matches somehow");
						}
						else if(Math.abs(index2-index) >= 1){
							structureScore += score*0.6/Math.abs(index2-index) ;
							line1MatchLine2.put(index2, index);
							line2MatchLine1.put(index,index2);
//							System.out.println("components "+ index2+ "(line1) and "+ index+ "line(2) matches somehow");
						}
					}
				}
			}
		}
		return structureScore;
	}
	
	//this function returns a float number centering at 0f of whether section s1 and s2 have elements 
	//such as (scene) or (human) that matches (meaning they are the same
	//thay return a float (see above function for explanation)
	public double sectionMeaningMatch(String s1, String s2) {
		//create Array String that stores the tagss
		ArrayList<String> string1Tags = new ArrayList<String>();
		ArrayList<String> string2Tags = new ArrayList<String>();
		float MatchingScore = 0; //the score of matching
		boolean matched = false;
		

		//create pattern matcher		
		Pattern pattern = Pattern.compile("(\\([^\\+|\\-|\\(]+\\))");
				
		//match patterns from section s1 and section s2
        Matcher matcherTag = pattern.matcher(s1);
        while(matcherTag.find()) {
        	string1Tags.add(matcherTag.group(1));
//        	System.out.println("MatcherTag in S1 is "+ matcherTag.group(1));
        }
        
        matcherTag = pattern.matcher(s2);  
        while(matcherTag.find()) {
        	string2Tags.add(matcherTag.group(1));
//        	System.out.println("MatcherTag in S2 is " + matcherTag.group(1));
        }
              
        if(string1Tags.size()==0 ||string2Tags.size()==0) //if no tag is found
        	return 0f;
        
    	float totalTags = 2*Math.max(string1Tags.size(), string2Tags.size()); //total number of tags
    	float cumulativeTags = 0f;
    	
        //now tabulate the score of how s1 and s2 match in style
        if (string1Tags.equals(string2Tags) && !string1Tags.isEmpty()) { //If two sections are exactly the same
        	MatchingScore += 1f;   //if fully matched, the matching point is 1f
        	matched = true;
        }
        
        else { //if two sections differ
        	if(!string1Tags.isEmpty()) {
	        	int recursion  = string1Tags.size();
	        	while(recursion > 0 && !string1Tags.isEmpty()) { //always loop through starting from the longest string
	        		matched = false;
	        		//if the tag is found in string2 starting from the initial
	        		if(!string2Tags.isEmpty()) {
	        			int length = string2Tags.size();
		        		for(int j = 0; j<length && !matched; j++) {
		        			if (string2Tags.get(j).compareTo(string1Tags.get(0)) == 0) {
		        				cumulativeTags += 2;
		        				string2Tags.remove(j);
		        				string1Tags.remove(0);
		        				matched = true;
		        			}
		        		}
	        		}
	        		if(!matched) { //if nothing is matched to color or number
	        			if(string1Tags.get(0).contains("color")| string1Tags.get(0).contains("number")){
	        				string1Tags.remove(0);
	        				cumulativeTags --;
	        			}
	        		}
	        		else {// it is not matched, and it does not matter
	        			if(!string1Tags.isEmpty())
	        				string1Tags.remove(0);
	        		}
	        		recursion --;
	        	}
        	}
	       
        	//if in string2 there are unmached.
        	if(!string2Tags.isEmpty()) {
	        	while(string2Tags.size() > 0){
	    			if(string2Tags.get(0).contains("color")|string2Tags.get(0).contains("number")){
	    				string2Tags.remove(0);
	    				cumulativeTags --;
	    			}
	    			else
	    				string2Tags.remove(0); //delete without harm
	        	}
        	}

        	MatchingScore += cumulativeTags/totalTags;
        }
        if (MatchingScore < 0)
        	MatchingScore = 0;
		return MatchingScore ;
	}
}
	
	
