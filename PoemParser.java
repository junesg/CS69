import javax.swing.*;
import java.io.*;
import java.util.ArrayList;


public class PoemParser {
	private final static int MAXNUMFILE = 30;
	static File[] filelist = new File[MAXNUMFILE];
	static ArrayList<String>fileContent = new ArrayList<String>();
	static ProcessPoemScores processPoemScores = null;
	
	//main
	public static void main(String[] args){
		//establish file list
		JFrame window = new JFrame("Prompt for directory");
		
	
		
		filelist = FileChooser(window);
		if (filelist != null) {
			for (int ind = 0; ind <filelist.length; ind++) {
				try {
					fileContent.add(readFile(filelist[ind]));
				} catch (IOException e) {
					System.out.println("unable to read file");
					e.printStackTrace();
				}
			}
		}
		
		

		//process the string of each file
		//debugging: we only process the first file
		for(int ind = 0; ind <filelist.length; ind++) {
				System.out.println("----START OF FILE "+filelist[ind].getName()+" ---");
				processPoemScores = new ProcessPoemScores(fileContent.get(ind));
				System.out.println("END OF FILE");
		}
		
	}
		
	
	
	//allow users to choose a directory
	public static File[] FileChooser(JFrame window){
	    JFileChooser fc = new JFileChooser();
	    fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

	    if( fc.showOpenDialog(window) == JFileChooser.APPROVE_OPTION )
	    {
	    	return finder(fc.getSelectedFile().getAbsolutePath());    	
	    }
	    else
	    	return null;
	}
		
	
	//extract files from the directory, return names
	public static File[] finder(String dirName){
	    File dir = new File(dirName);
    	return dir.listFiles(new FilenameFilter() { 
	    	   public boolean accept(File dir, String filename)
	    	        { return filename.endsWith(".txt"); }
	    	} );
	    }	
	
	
	//read files into strings
	public static String readFile(File file) throws IOException {	
		//make sure the string read in is ecoded in utf-8 
	    BufferedReader reader = new BufferedReader(
	    		new InputStreamReader(new FileInputStream(file), "UTF-8"));
	    String line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }
	    return stringBuilder.toString();
	}
	
	
	
}