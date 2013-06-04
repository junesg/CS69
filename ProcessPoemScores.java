import java.util.*;



public class ProcessPoemScores {
	private final int MAXPOEMNUM = 60;//how many maximum poems we have
	private final int MAXMEANINGUNIT = 60; //maximum number of meaning units in one poem
	//clearly the longest poem has 7 chinese characters 8 lines (in bad poems) (56 max meaning units) so we can assume max is less than 60
	double rhyme = 0f;    //records the score of how each sentence/each pair of sentences rhymes
	double emotions= 0f; //records whether the poem has twists of emotions as Staring-Rising-Twisting-Synthesizing
	double condense= 0f;  //records how the poem has incoorperated as many meaning units into the poem as possible
	//variables that extracts the information from the information from the poems
//	public Map<String,ArrayList<String>> wordToCategory = new HashMap<String,ArrayList<String>>();
//	public Map<String,ArrayList<String>> wordToEmotions = new HashMap<String,ArrayList<String>>();
//	public Map<String,ArrayList<String>> wordToAttitude = new HashMap<String,ArrayList<String>>();
//	public Map<String,ArrayList<String>> wordToAuthor = new HashMap<String,ArrayList<String>>();
//	public Map<Integer, ArrayList<Float>> poemIDToScores= new HashMap<Integer,ArrayList<Float>>();
////	<poemID, (which pair, what type)>
	public Map<Integer, ArrayList<Double>>poemIDToRhymeCharacteristics = new HashMap<Integer,ArrayList<Double>>();
	//variables related to each poem section
	private String[] poems = new String[MAXPOEMNUM];
	private int poemID = 0;
	private String title = null;
	private String author = null;
	private String format = null;
	private String emote = null;      //stores emotional tags for the whole poem
	private String category = null;
	private String comparison = null;
	private String body = null;
		
	//class for poemsStructural Score
	PoemStructuralScore structureScore = null;
	PoemPhoneticScore phoneticScore = null;
	
	//constructor, splits the poems and calls on a function to add all information
	//into the database
	public ProcessPoemScores(String string) {
		poems = string.split("ID:");
		for(int ind = 1; ind < poems.length; ind++) {
			DerivePoemStructureScore(poems[ind], ind);
		}
	}
	
	//this funciton splits up the poem into each of its components: title, author, emotions, categorty etc.
	public void DerivePoemStructureScore(String tempStr, int ind) {
		if (tempStr.compareTo("")==0 || tempStr == null) ; //do nothing
		else {
			//first modify string to derive poemID, author, emotions, comparison and category
			String[] temp = tempStr.split("TITLE:");
			poemID = ind; //get poem id
			temp = temp[1].split("AUTHOR:");
			title = temp[0];
			temp = temp[1].split("FORMAT:");
			author = temp[0];
			temp = temp[1].split("EMOTIONS:");
			format = temp[0]; //there is always format
			temp = temp[1].split("CATEGORY:");
			emote = temp[0];
			temp = temp[1].split("COMPARISON:");
			category = temp[0];
			temp = temp[1].split("BODY:");
			comparison = temp[0];
			body = temp[1];
			
			//add information to relevant maps	
			//structure information
			
			System.out.println(ind);
			//print out structural score and emotional scores
			structureScore = new PoemStructuralScore(format, ind);
					
			
			poemIDToRhymeCharacteristics = structureScore.poemIDToRhymeCharacteristics; //here also include the poem's sentiment score
			//PoemStructuralScor.poemIDToRhymeCharacteristics:  poem "+ind+" , pair: "+ lineCount+ " and score = "+ structureScore
			
			//the condense score
			PoemCondense condenseCalculator = new PoemCondense(format,ind);
			condense = condenseCalculator.condenseScore;
			
			
			emotions = structureScore.emotionalScore;
			//phonetic score
			phoneticScore = new PoemPhoneticScore(body,ind);
			rhyme = phoneticScore.GetPhoneticScore(phoneticScore.lines);		
			System.out.println(rhyme); 
			double phoneticType = phoneticScore.phoneticType; //the tye of rymth (if it 8 line, it depends only on the first 4 lines
			//because we are looking at the 4 lines now.
			System.out.println(phoneticType+1);
		}
		
	}
	
}
