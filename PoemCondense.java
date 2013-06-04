import java.io.*;
import java.util.*;
import java.util.regex.*;



public class PoemCondense {
	public double condenseScore = 0;
	
	//constructor 
	public PoemCondense(String format, int poemInd) {
		BufferedReader reader = new BufferedReader(new StringReader(format));
//		System.out.println("Entered Condense");
		try {
			String line = reader.readLine();
//			System.out.println("1taken line: "+line);
			int lineNum = 0;
			while(line != null) {
				if(line.compareTo("")==0) {
					while(line.compareTo("")==0) {
						line = reader.readLine();
					}
				}
				condenseScore += GetCondenseScores(line);
				line = reader.readLine();
				lineNum ++;
			}	
			condenseScore /= (double)lineNum;
		} catch (IOException e) {
			System.out.println("Format cannot be read");
			e.printStackTrace();
		}
		System.out.println(condenseScore);
	}
	
	
	//Get the condensation score of one line
	public double GetCondenseScores(String line) {
		String[] sections = line.split("\\|");
		int sectionSize = sections.length;
		Pattern pattern = Pattern.compile("(\\([^\\+|\\-|\\(]+\\))");
		int countMeaningNum = 0;
		for (int i =0; i<sections.length; i++) {
			Matcher matcherTag = pattern.matcher(sections[i]);
			if(matcherTag.find()) { //as long as we find one! 
//		        	System.out.println(matcherTag.group(1));
		        	countMeaningNum++;
		    }
		}
        return ((double)countMeaningNum/(double)sectionSize);		
	}
	

	
}