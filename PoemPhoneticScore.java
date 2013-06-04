import java.awt.Point;
import java.io.*;
import java.util.*;
import java.util.regex.*;

//use the least cost
public class PoemPhoneticScore {
	public ArrayList<String> lines = new ArrayList<String>();
	public double phoneticScore = 0f;
	public double phoneticType = 0f; //initialize;
	
	//constructor
	public PoemPhoneticScore(String body, int poemId){
		BufferedReader reader = new BufferedReader(new StringReader(body));
//		System.out.println(body);
		
		try {
			String line = reader.readLine();
			String nextLine = reader.readLine();
//			System.out.println("readline: "+line);
			
			while(nextLine != null) {
				while(line.compareTo("")== 0 && nextLine != null) {
					line = nextLine;
					nextLine = reader.readLine();
				}
				if(line.compareTo("END") != 0 && line.compareTo("")!=0) {
					lines.add(line); //add the line into the lines
//					System.out.println("line is : " +line);
				}
				line = nextLine;
				nextLine = reader.readLine();
			}
			if(nextLine == null && line != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			System.err.println("not able to read line");
			e.printStackTrace();
		}
		
		if(!lines.isEmpty()) {
//			System.out.println("lines has size "+lines.size());
//			for(int i = 0; i<lines.size();i++)
//				System.out.println("line("+i+") is : " +lines.get(i));
			GetPhoneticScore(lines);
		}
	}
	
	
	//this function drives the phonetic scores
	public double GetPhoneticScore(ArrayList<String> lines) { //here the lines can only be 4 or 8 lines long
		ArrayList<Integer[]> phoneFormat = new ArrayList<Integer[]>();
		if (lines.size() != 4 && lines.size() != 8) {
			System.out.println("wrong size is "+ lines.size());
			for(int i = 0; i<lines.size();i++)
				System.out.println("In GetPhoneticScore: line("+i+") is : " +lines.get(i));
			return 100000; //huge cost
		}
		else {
			if(lines.size() == 4) {
				//////debugging
				for(int i = 0; i<lines.size();i++) {
//					System.out.println("right line("+i+") is : " +lines.get(i));
					phoneFormat.add(GetTypeOfLine(lines.get(i)));
				}
				Point typeCost = OptimalAssignment(phoneFormat);
				phoneticType = (double)(typeCost.x);
				return (1f-(double)typeCost.y/(double)20);
			}
			else {
				for(int i = 0; i<lines.size()/2;i++) {
//					System.out.println("right line("+i+") is : " +lines.get(i));
					phoneFormat.add(GetTypeOfLine(lines.get(i)));
				}
				Point typeCost = OptimalAssignment(phoneFormat);
				phoneFormat.clear();
				for(int i = 4; i<lines.size();i++) {
//					System.out.println("right line("+i+") is : " +lines.get(i));
					phoneFormat.add(GetTypeOfLine(lines.get(i)));
				}
				typeCost = new Point(typeCost.x, (OptimalAssignment(phoneFormat).y+typeCost.y));
				phoneticType = (double)(typeCost.x);
				return (1f-(double)typeCost.y/(double)40);
			}
		}

	}
	
	//this funciton determines which type of phonetic arrangement the line has
	public Integer[] GetTypeOfLine(String line) {
		String phone = "";
		if (line.compareTo("")==0) {
			System.err.println("This line is empty");
			return null;
		}
		else {
			Pattern pattern = Pattern.compile("([0-9])");
			Matcher matcherTag = pattern.matcher(line);
			int index = 0;
		    while(matcherTag.find()) {
		       	if(matcherTag.group(1).compareTo("1")==0 | matcherTag.group(1).compareTo("2")==0 |
		       			matcherTag.group(1).compareTo("5")==0) {
		       		phone = phone + "A";
		       	}
		       	else {
		       		phone = phone + "B";
		       	}
		       	index ++;
		       	//System.out.println("MatcherTag in line is "+ matcherTag.group(1)+" phone is so far "+phone+"." );
		    }
		}
		int dis1 = Math.min(DifferenceInLetter(phone, "AAABB"),DifferenceInLetter(phone,"BAABB"));
		int dis2 = Math.min(DifferenceInLetter(phone, "BBAAB"), DifferenceInLetter(phone, "ABAAB"));
		int dis3 = (Math.abs(DifferenceInLetter(phone, "AABBA")));
		int dis4 = Math.min(DifferenceInLetter(phone, "ABBAA"), DifferenceInLetter(phone, "BBBAA"));
			
		Integer[] distances = new Integer[4];
		distances[0] = dis1;
		distances[1] = dis2;
		distances[2] = dis3;
		distances[3] = dis4;
		return distances; //the type among 1,2,3,4 that is the correct match
	}
	
	//this helper strings returns the number of different characters that two strings have
	public int DifferenceInLetter (String a, String b) {
		int i =0;
		int counter = 0;
		for(char c : a.toCharArray()){
		   if(i < b.length() && b.charAt(i++) != c)
		     counter++;
		}
		return counter;
	}
	
	
	//given a list of difference arrays of sentencs vs. types and four lines
	//returns the type of the poem and the cost of the pattern
	public Point OptimalAssignment(ArrayList<Integer[]> list){
		int[] type1 = {2,3,1,4};
		int[] type2 = {1,4,2,3};
		int[] type3 = {3,4,2,3};
		int[] type4 = {4,2,1,4};
		
		/* type1:
		 * 2,3,1,4
		 * type2:
		 * 1,4,2,3
		 * type3:
		 * 3,4,2,3,
		 * type4:
		 * 4,3,1,4
		 */
		
	
		//get the min cost of all types and that will be the type
		int[] typeerr = {PoemPhonePatternCost(list, type1), PoemPhonePatternCost(list, type2),
				PoemPhonePatternCost(list, type3),PoemPhonePatternCost(list, type4)};
		int min = typeerr[0];
		int type = 0;
		for(int i = 0 ; i<4;i++){
			if(min<= typeerr[i]); //do nothing
			else {
				min = typeerr[i];
				type = i;
			}
		}
//		System.out.println("Poem type is "+ (type+1));
		return (new Point(type, min)); //poem type and cost
	}
	
	
	//this function takes in a list of cost integers for each sentence (corresponding to 4 types) 
	// and a pattern for the poem as a combination of four types
	//returns the cost of the type
	public int PoemPhonePatternCost(ArrayList<Integer[]> list, int[] pattern) {
		int cost = 0;
		if(list.size() != 4 || pattern.length!=4) {
			System.err.println("Error: poem length in phonetics");
			return 10000;  //really large cost
		}
		else {
			for(int i = 0; i < list.size(); i++) {
				Integer[] subLine = list.get(i);
				cost += subLine[pattern[i]-1];
			}
			return cost;
		}
	}
	
	
	
	
}