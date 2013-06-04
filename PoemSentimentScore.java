import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PoemSentimentScore {
	private int id;
	public ArrayList<String> lines = new ArrayList<String>();
	//constructor 
	public PoemSentimentScore(int poemind) {
		id = poemind;
	}
	
	//get emotional score of 4 lines
	public double GetEmotionalScore() {
		double emotionalScore = 0.0;
		//initializing of all necessary scores
		double begin = 0f;
		double continuation = 0f;
		double twist = 0f;
		double synthesize = 0f;
		
		if(lines.isEmpty());
		else {
			int num = lines.size();
			if (num == 4) { //this is a 8 line regulated poem curtailed verse (jue ju)
				begin = GetLineEmotionScore(lines.get(0));
				continuation = GetLineEmotionScore(lines.get(1));
				twist = GetLineEmotionScore(lines.get(2));
				synthesize = GetLineEmotionScore(lines.get(3));
//				System.out.println("score is "+ begin+" "+continuation+ " "+twist+" "+synthesize);
			}
			else if(num == 8) { //this is a 8 line regulated poem
				begin = GetLineEmotionScore(lines.get(0));
				begin += GetLineEmotionScore(lines.get(1));
				continuation = GetLineEmotionScore(lines.get(2));
				continuation += GetLineEmotionScore(lines.get(3));
				twist = GetLineEmotionScore(lines.get(4));
				twist += GetLineEmotionScore(lines.get(5));
				synthesize = GetLineEmotionScore(lines.get(6));
				synthesize += GetLineEmotionScore(lines.get(7));
//				System.out.println("score is "+ begin+" "+continuation+ " "+twist+" "+synthesize);
			}
		}
		
		/*
		 * follow the begin --> continuation --> twist --> synthesis
		 * we know that 
		 */
		
		if(Math.abs(begin+continuation) >= Math.abs(begin) &&
				Math.abs(begin+continuation) >= Math.abs(twist+begin+continuation) &&
				Math.abs(twist+begin+continuation)<= Math.abs(twist+begin+continuation+synthesize)){
			emotionalScore = 1.0;
		}
		else if (Math.abs(begin+continuation) >= Math.abs(twist+begin+continuation) && //no continuation
				Math.abs(twist+begin+continuation)<= Math.abs(twist+begin+continuation+synthesize)){
			emotionalScore = 0.6666;
		}		
		else if (Math.abs(begin+continuation) >= Math.abs(begin) &&   //this is a no twist event
				Math.abs(twist+begin+continuation)<= Math.abs(twist+begin+continuation+synthesize) )
		{
			emotionalScore = 0.6666;
//			System.out.println("emitonalScore here is "+emotionalScore);
		}
		else if(Math.abs(begin+continuation) >= Math.abs(begin) && //this is a no synthesize event
				Math.abs(begin+continuation) >= Math.abs(twist+begin+continuation)){
			emotionalScore = 0.6666;
		}
		else if(Math.abs(begin+continuation) >= Math.abs(begin)) { //no twist, no synthesis
			emotionalScore = 0.6666;
		}
		else if(Math.abs(twist+begin+continuation)<= Math.abs(twist+begin+continuation+synthesize)){ // no twist no continuations
			emotionalScore = 0.3333;
		}
		else if(Math.abs(begin+continuation) >= Math.abs(twist+begin+continuation)){  //no synthesis no continuations
			emotionalScore =0.3333;
		}
		else{
			emotionalScore = 0;
		}
//		System.out.println("emotional score is "+ emotionalScore);
		return emotionalScore;
		
	}	
	
	//the emotional of one line
	public double GetLineEmotionScore(String oneline) {
		double emote = 0f;
		if(oneline.compareTo("") == 0)
			return emote;
		else {
			Pattern pattern = Pattern.compile("(\\([\\+|\\-]+\\))");
			Matcher matcherTag = pattern.matcher(oneline);
		    	while(matcherTag.find()) {
//		    		System.out.println("patter is "+ matcherTag.group(1));
		        	if(matcherTag.group(1).compareTo("(-)") == 0) {
		        		emote --;
		        	}
		        	else if(matcherTag.group(1).compareTo("(+)")==0) {
		        		emote ++;
		        	}
		        	else {
		        		System.err.println("Symbol not found, system error to calculate emotoins");
		        	}
		        }
//		 System.out.println("line "+oneline+" has score "+emote);
		 return emote;   
		}
	}
	
}